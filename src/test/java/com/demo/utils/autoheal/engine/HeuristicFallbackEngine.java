package com.demo.utils.autoheal.engine;

import com.demo.utils.autoheal.models.HealResult;
import com.demo.utils.autoheal.reporting.AutoHealLogger;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeuristicFallbackEngine {

    public HealResult evaluate(Page page, String originalLocator, String... fallbacks) {
        if (fallbacks == null || fallbacks.length == 0) {
            return null;
        }

        AutoHealLogger.logHeuristicHeal("Evaluating " + fallbacks.length + " heuristics for locator: " + originalLocator);

        for (String fallback : fallbacks) {
            try {
                Locator heuristic = page.locator(fallback);
                heuristic.first().waitFor(new Locator.WaitForOptions().setTimeout(1000));
                if (heuristic.count() > 0) {
                    AutoHealLogger.logHeuristicHeal("Heuristic match found! Selector: " + fallback);
                    return new HealResult(originalLocator, fallback, "heuristic", null, 1.0);
                }
            } catch (Exception e) {
                AutoHealLogger.logHeuristicHeal("Heuristic (" + fallback + ") failed.");
            }
        }

        return null;
    }
}
