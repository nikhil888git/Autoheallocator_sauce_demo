package com.demo.utils.autoheal.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.demo.utils.autoheal.AutoHealSuiteConfig;
import com.demo.utils.autoheal.models.HealResult;
import com.demo.utils.autoheal.reporting.AutoHealLogger;
import com.microsoft.playwright.Page;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIHealingHook {

    public HealResult healWithAI(Page page, String brokenLocator) {
        AutoHealLogger.logAiHeal("Requesting AI extraction for broken locator: " + brokenLocator);
        try {
            String apiKey = AutoHealSuiteConfig.getApiKey();
            String baseUrl = AutoHealSuiteConfig.getBaseUrl();
            String aiModel = AutoHealSuiteConfig.getModel();

            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
                AutoHealLogger.logError("[AI_HEAL]", "API Key missing or invalid.");
                return null;
            }

            String domSnippet = "";
            try {
                // Fetch direct body HTML without in-browser deep-copying (prevents Chrome OOM)
                domSnippet = (String) page.evaluate("() => document.body.innerHTML");
                
                // Clean heavy unneeded elements purely in Java memory boundary
                domSnippet = domSnippet.replaceAll("(?is)<script.*?</script>", "");
                domSnippet = domSnippet.replaceAll("(?is)<style.*?</style>", "");
                domSnippet = domSnippet.replaceAll("(?is)<svg.*?</svg>", "");
                domSnippet = domSnippet.replaceAll("(?is)<noscript.*?</noscript>", "");
                domSnippet = domSnippet.replaceAll("(?is)<iframe.*?</iframe>", "");
                
                if (domSnippet.length() > 20000) {
                    domSnippet = domSnippet.substring(0, 20000);
                }
            } catch (Exception e) {
                AutoHealLogger.logError("[AI_HEAL]", "Cannot extract DOM: " + e.getMessage());
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
                    "' is broken. Analyze the following HTML. Return a JSON object with two keys: 'selector' (robust CSS selector string) and 'confidence' (float from 0.0 to 1.0). Do not use markdown backticks.\n\nDOM:\n" + domSnippet;
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
                JsonNode root = mapper.readTree(response.body());
                String content = root.path("choices").get(0).path("message").path("content").asText().trim();

                if (content.startsWith("```json")) content = content.substring(7);
                else if (content.startsWith("```")) content = content.substring(3);
                if (content.endsWith("```")) content = content.substring(0, content.length() - 3);
                content = content.trim();

                JsonNode aiResponse = mapper.readTree(content);
                String aiLocator = aiResponse.path("selector").asText().trim();
                double aiConfidence = aiResponse.path("confidence").asDouble(1.0);

                aiLocator = aiLocator.replace("`", "").replace("\"", "").replace("'", "");
                
                String confidenceLabel = "RISKY_HEAL";
                if (aiConfidence >= 0.9) confidenceLabel = "SAFE_HEAL";
                else if (aiConfidence >= 0.5) confidenceLabel = "MEDIUM_CONFIDENCE_HEAL";

                AutoHealLogger.logAiHeal("AI Match formulated - Selector: " + aiLocator + " | Confidence: " + aiConfidence + " (" + confidenceLabel + ")");
                return new HealResult(brokenLocator, aiLocator, "ai", aiConfidence, null);

            } else {
                AutoHealLogger.logError("[AI_HEAL]", "API request failed: HTTP " + response.statusCode() + " - " + response.body());
            }

        } catch (Exception e) {
            AutoHealLogger.logError("[AI_HEAL]", "Integration exception: " + e.getMessage());
        }
        return null;
    }
}
