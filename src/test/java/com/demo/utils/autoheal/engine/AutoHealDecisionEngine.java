package com.demo.utils.autoheal.engine;

import com.demo.utils.autoheal.AutoHealSuiteConfig;
import com.demo.utils.autoheal.cache.AutoHealCacheManager;
import com.demo.utils.autoheal.models.HealResult;
import com.demo.utils.autoheal.reporting.AutoHealLogger;
import com.demo.utils.autoheal.reporting.AutoHealTraceAggregator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoHealDecisionEngine {
    private HeuristicFallbackEngine heuristicEngine;
    private AIHealingHook aiHook;
    
    private static final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private static final int MAX_HEAL_ATTEMPTS = 2;

    public AutoHealDecisionEngine() {
        this.heuristicEngine = new HeuristicFallbackEngine();
        this.aiHook = new AIHealingHook();
    }

    public HealResult executeArbitration(Page page, String originalLocator, boolean isSilent, String... fallbacks) {
        if (failedAttempts.getOrDefault(originalLocator, 0) >= MAX_HEAL_ATTEMPTS) {
            AutoHealLogger.logError("[ARBITRATOR]", "Locator LOCKED. Exceeded MAX_HEAL_ATTEMPTS for: " + originalLocator);
            return null;
        }
        
        String strategy = AutoHealSuiteConfig.getStrategy();
        
        // 1. Check AI Cache (if preferred strategy is AI)
        if ("ai".equalsIgnoreCase(strategy)) {
            HealResult cached = AutoHealCacheManager.getCachedResult(originalLocator, true);
            if (cached != null) {
                if (!isSilent) AutoHealLogger.logAiHeal("Using cached AI healed locator for: " + originalLocator + " -> " + cached.healed);
                return cached;
            }
        }

        // 2. Check Heuristic Cache
        HealResult cachedHeur = AutoHealCacheManager.getCachedResult(originalLocator, false);
        if (cachedHeur != null && "heuristic".equalsIgnoreCase(cachedHeur.strategy)) {
            if (!isSilent) AutoHealLogger.logHeuristicHeal("Using cached Heuristic locator for: " + originalLocator + " -> " + cachedHeur.healed);
            return cachedHeur;
        }

        if (!isSilent) AutoHealLogger.logExecution("Commencing structural arbitration for locator: " + originalLocator);

        // 3. Always attempt Heuristics first if provided
        HealResult heurResult = heuristicEngine.evaluate(page, originalLocator, fallbacks);
        if (heurResult != null && heurResult.heurConfidenceScore >= 0.9) {
            AutoHealLogger.logHeuristicHeal("Heuristic engine provided SAFE_HEAL. Bypassing AI.");
            AutoHealCacheManager.recordCache(heurResult);
            return heurResult;
        }

        // 4. If Heuristic didn't yield a SAFE_HEAL and AI is enabled, call AI Hook
        if ("ai".equalsIgnoreCase(strategy)) {
            HealResult aiResult = aiHook.healWithAI(page, originalLocator);
            if (aiResult != null) {
                double threshold = AutoHealSuiteConfig.getConfidenceThreshold();
                if (aiResult.aiConfidenceScore < threshold) {
                    AutoHealLogger.logAiHeal("AI generated RISKY_HEAL (" + aiResult.aiConfidenceScore + "). Below threshold " + threshold + ", rejecting.");
                    // Fall back to heuristic if we had a medium confidence one? We don't have partial heuristics right now.
                } else {
                    AutoHealCacheManager.recordCache(aiResult);
                    return aiResult;
                }
            }
        }

        // 5. If we had a heuristic response (even below threshold) and AI failed, we might use it or just fail?
        // Right now Heuristic Engine returns either 1.0 or null.
        if (heurResult != null) {
            AutoHealCacheManager.recordCache(heurResult);
            return heurResult;
        }

        failedAttempts.put(originalLocator, failedAttempts.getOrDefault(originalLocator, 0) + 1);
        AutoHealLogger.logRecovery("All arbitration logic failed for: " + originalLocator);
        return null;
    }
}
