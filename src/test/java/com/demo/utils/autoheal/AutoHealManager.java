package com.demo.utils.autoheal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.utils.autoheal.engine.AutoHealDecisionEngine;
import com.demo.utils.autoheal.models.ExecutionJson;
import com.demo.utils.autoheal.models.HealResult;
import com.demo.utils.autoheal.reporting.AutoHealLogger;
import com.demo.utils.autoheal.reporting.AutoHealTraceAggregator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AutoHealManager {
    private Page page;
    private AutoHealDecisionEngine decisionEngine;
    
    private static final Set<Integer> sessionReportedHeals = ConcurrentHashMap.newKeySet();

    public AutoHealManager(Page page) {
        this.page = page;
        this.decisionEngine = new AutoHealDecisionEngine();
    }

    public Locator getLocator(String primarySelector, String... fallbacks) {
        if (!AutoHealSuiteConfig.isEnabled()) {
            return page.locator(primarySelector);
        }

        // STEP 1: Fast Path Verification
        Locator primary = page.locator(primarySelector);
        try {
            primary.first().waitFor(new Locator.WaitForOptions().setTimeout(2000));
            if (primary.count() > 0) {
                return primary; // Unbroken execution. TestNG logs as normal PASS
            }
        } catch (Exception e) {
            // Evaluated implicitly, no need to log here yet as it might be cached
        }

        int resolutionCacheKeyAi = (primarySelector + "_" + page.url() + "_ai").hashCode();
        int resolutionCacheKeyHeur = (primarySelector + "_" + page.url() + "_heuristic").hashCode();
        boolean isDuplicate = sessionReportedHeals.contains(resolutionCacheKeyAi) || sessionReportedHeals.contains(resolutionCacheKeyHeur);

        if (!isDuplicate) {
            AutoHealLogger.logExecution("Primary locator extraction timeout threshold breached: " + primarySelector);
            AutoHealLogger.logExecution("Initiating decision engine orchestration...");
        }
        
        // STEP 2: Strict Arbitration Orchestration
        HealResult result = decisionEngine.executeArbitration(page, primarySelector, isDuplicate, fallbacks);

        if (result == null || result.healed == null || page.locator(result.healed).count() == 0) {
            AutoHealLogger.logError("[RECOVERY]", "ALL resolution frameworks exhausted. Escalating test failure trajectory.");
            
            // ISO Execution Protocol Tracking
            ExecutionJson payloadUnresolved = new ExecutionJson();
            payloadUnresolved.step = "Element Locator Resolution";
            payloadUnresolved.locator_resolution = new ExecutionJson.LocatorResolution();
            payloadUnresolved.locator_resolution.original = primarySelector;
            payloadUnresolved.status = "HEAL_FAILED";
            AutoHealTraceAggregator.logTrace(payloadUnresolved);
            
            return primary; 
        }

        // STEP 3: Segregate Execution Logging & Trace Logic By Enforced Guidelines
        int resolutionCacheKey = (primarySelector + "_" + page.url() + "_" + result.strategy).hashCode();

        if (!sessionReportedHeals.contains(resolutionCacheKey)) {
            sessionReportedHeals.add(resolutionCacheKey);
            
            String allureStepName = "ai".equalsIgnoreCase(result.strategy) ? "AI-Healed Step" : "Heuristic Fallback Step";
            String statusLabel = "ai".equalsIgnoreCase(result.strategy) ? "AI_HEALED_PASS" : "HEURISTIC_HEALED_PASS";

            ExecutionJson payload = new ExecutionJson();
            payload.step = "Element Locator Resolution";
            payload.locator_resolution = new ExecutionJson.LocatorResolution();
            payload.locator_resolution.original = result.original;
            payload.locator_resolution.resolved = result.healed;
            payload.locator_resolution.strategy = result.strategy;
            
            payload.locator_resolution.confidence = new ExecutionJson.Confidence();
            if (result.aiConfidenceScore != null) {
                payload.locator_resolution.confidence.ai_confidence_score = result.aiConfidenceScore;
            } else if ("heuristic".equalsIgnoreCase(result.strategy)) {
                payload.locator_resolution.confidence.ai_confidence_score = 0.0;
                payload.locator_resolution.confidence.ai_skipped_reason = "heuristic_safe_path";
            }
            if (result.heurConfidenceScore != null) {
                payload.locator_resolution.confidence.heur_confidence_score = result.heurConfidenceScore;
            }
            
            payload.status = statusLabel;

            // Propagate centrally to `target/execution_evidence.json` via Aggregator Layer
            AutoHealTraceAggregator.logTrace(payload);

            // STEP 4: Force nested step generation directly onto the live TestNG Thread (Allure Report Injector)
            Allure.step(allureStepName, () -> {
                Allure.step("Original Selector: " + result.original);
                Allure.step("Healing Strategy Used: " + ("ai".equalsIgnoreCase(result.strategy) ? "AI Model Inference" : "Heuristics Extractor"));
                if (result.aiConfidenceScore != null) Allure.step("AI Confidence Score: " + result.aiConfidenceScore);
                if (result.heurConfidenceScore != null) Allure.step("Heuristic Confidence Score: " + result.heurConfidenceScore);
                Allure.step("Final Resolved Locator: " + result.healed);
                
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String attachPayload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
                    Allure.addAttachment("Execution Structure Trace", "application/json", attachPayload, "json");
                } catch (Exception e) {}
            });
        }

        if (!isDuplicate) AutoHealLogger.logExecution("Successfully hijacked DOM execution pointer routing to -> " + result.healed);
        return page.locator(result.healed);
    }
}
