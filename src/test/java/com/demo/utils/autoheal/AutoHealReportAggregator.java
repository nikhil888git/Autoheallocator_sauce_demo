package com.demo.utils.autoheal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoHealReportAggregator {
    private static Map<String, HealedLocator> cachedHeals = new ConcurrentHashMap<>();
    private static List<HealedLocator> sessionHeals = new ArrayList<>();
    private static final String CACHE_FILE = "src/test/resources/autoheal-cache.json";
    private static final String REPORT_FILE = "target/autoheal-report.json";

    static {
        loadCache();
    }

    private static void loadCache() {
        try {
            File cacheFile = new File(CACHE_FILE);
            if (cacheFile.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                List<HealedLocator> stored = mapper.readValue(cacheFile, new TypeReference<List<HealedLocator>>(){});
                for (HealedLocator heal : stored) {
                    cachedHeals.put(heal.original, heal);
                }
                System.out.println("[AutoHeal] Loaded " + cachedHeals.size() + " healed locators from cache.");
            }
        } catch (Exception e) {
            System.err.println("[AutoHeal] Could not load cache: " + e.getMessage());
        }
    }

    public static String getCachedLocator(String original) {
        HealedLocator cached = cachedHeals.get(original);
        if (cached != null) {
            return cached.healed;
        }
        return null;
    }

    public static synchronized void recordHealing(String original, String healed, String strategy) {
        HealedLocator newHeal = new HealedLocator(original, healed, strategy);
        sessionHeals.add(newHeal);
        cachedHeals.put(original, newHeal);
    }

    public static void generateReport() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 1. Save all known heals to cache for next run
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CACHE_FILE), cachedHeals.values());
            
            // 2. Generate report for strictly what happened this session
            if (!sessionHeals.isEmpty()) {
                File reportFile = new File(REPORT_FILE);
                mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, sessionHeals);
                System.out.println("[AutoHeal] Report generated at " + reportFile.getAbsolutePath() + " with " + sessionHeals.size() + " elements.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class HealedLocator {
        public String original;
        public String healed;
        public String strategy;

        public HealedLocator() {} // For Jackson
        public HealedLocator(String o, String h, String s) {
            this.original = o;
            this.healed = h;
            this.strategy = s;
        }
    }
}
