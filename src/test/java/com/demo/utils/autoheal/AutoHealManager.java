package com.demo.utils.autoheal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AutoHealManager {
    private Page page;

    public AutoHealManager(Page page) {
        this.page = page;
    }

    public Locator getLocator(String primarySelector, String fallbackDataTest) {
        if (!AutoHealSuiteConfig.isEnabled()) {
            return page.locator(primarySelector);
        }

        // STEP 1: Locator fails (Try primary first)
        Locator primary = page.locator(primarySelector);
        try {
            primary.first().waitFor(new Locator.WaitForOptions().setTimeout(2000));
            if (primary.count() > 0) {
                return primary;
            }
        } catch (Exception e) {
            System.out.println("[AutoHeal] Primary locator failed: " + primarySelector);
        }

        // STEP 2: Check cache
        String cachedLocator = AutoHealReportAggregator.getCachedLocator(primarySelector);
        if (cachedLocator != null) {
            System.out.println("[AutoHeal] Using cached healed locator for: " + primarySelector + " -> " + cachedLocator);
            Locator cached = page.locator(cachedLocator);
            try {
                cached.first().waitFor(new Locator.WaitForOptions().setTimeout(2000));
            } catch (Exception e) {
                System.out.println("[AutoHeal] Cached locator timeout... returning anyway.");
            }
            return cached;
        }

        System.out.println("[AutoHeal] Triggering healing process...");
        String strategy = AutoHealSuiteConfig.getStrategy();
        String finalLocatorSelector = null;

        // STEP 3: Try heuristic FIRST before reaching out to OpenAI
        Locator heuristic = page.locator(fallbackDataTest);
        try {
            heuristic.first().waitFor(new Locator.WaitForOptions().setTimeout(1000));
            if (heuristic.count() > 0) {
                System.out.println("[AutoHeal] Heuristic fallback succeeded! Bypassing expensive AI call.");
                finalLocatorSelector = fallbackDataTest;
                strategy = "heuristic";
            }
        } catch (Exception e) {
            System.out.println("[AutoHeal] Heuristic (" + fallbackDataTest + ") also failed.");
        }

        // STEP 4: Call OpenAI API (if enabled)
        if (finalLocatorSelector == null && "ai".equalsIgnoreCase(strategy)) {
            System.out.println("[AutoHeal] Calling OpenAI API for intelligent extraction...");
            finalLocatorSelector = callOpenAIAndGetSelector(primarySelector);
        }

        // Fallback safety if OpenAI completely fails or returns junk
        if (finalLocatorSelector == null || finalLocatorSelector.isEmpty() || page.locator(finalLocatorSelector).count() == 0) {
            System.err.println("[AutoHeal] ALL Healing strategies failed. Returning broken primary.");
            return primary;
        }

        System.out.println("[AutoHeal] Healed! New Locator to use: " + finalLocatorSelector);
        
        // STEP 5: Save result in cache
        AutoHealReportAggregator.recordHealing(primarySelector, finalLocatorSelector, strategy);

        return page.locator(finalLocatorSelector);
    }

    private String callOpenAIAndGetSelector(String brokenLocator) {
        try {
            String apiKey = AutoHealSuiteConfig.getApiKey();
            String baseUrl = AutoHealSuiteConfig.getBaseUrl();
            String aiModel = AutoHealSuiteConfig.getModel();
            
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
                System.err.println("[AutoHeal] AI API Key missing or invalid!");
                return null;
            }

            // Extract miniaturized DOM for GPT
            String domSnippet = "";
            try {
                // Strip scripts, styles, meta, etc, to keep token count low and accurate
                domSnippet = (String) page.evaluate("() => {" +
                        "let clone = document.body.cloneNode(true);" +
                        "['script', 'style', 'noscript', 'svg', 'iframe'].forEach(tag => {" +
                        "  Array.from(clone.getElementsByTagName(tag)).forEach(el => el.remove());" +
                        "});" +
                        "return clone.innerHTML;" +
                        "}");
                if (domSnippet.length() > 20000) domSnippet = domSnippet.substring(0, 20000); 
            } catch (Exception e) {
                domSnippet = "<html><body>Error extracting full DOM</body></html>";
            }

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("model", aiModel);
            rootNode.put("temperature", 0.0);

            ArrayNode messages = rootNode.putArray("messages");
            ObjectNode userMessage = messages.addObject();
            userMessage.put("role", "user");

            String prompt = "You are a Playwright test auto-healer. The CSS selector '" + brokenLocator +
                    "' is broken. Analyze the following HTML. Return ONLY the robust replacement CSS selector string (without tick marks, without quotes, without code blocks) that captures the intended element logically. \n\nDOM:\n" + domSnippet;
            userMessage.put("content", prompt);

            String requestBody = mapper.writeValueAsString(rootNode);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(response.body());
                String aiLocator = root.path("choices").get(0).path("message").path("content").asText().trim();
                aiLocator = aiLocator.replace("`", "").replace("\"", "").replace("'", ""); // simple sanitize
                return aiLocator;
            } else {
                System.err.println("[AutoHeal] AI API request failed: HTTP " + response.statusCode() + " - " + response.body());
            }

        } catch (Exception e) {
            System.err.println("[AutoHeal] AI Integration exception: " + e.getMessage());
        }
        return null;
    }
}
