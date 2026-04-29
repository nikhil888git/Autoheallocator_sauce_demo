package com.demo.utils.autoheal.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.utils.autoheal.models.HealResult;
import com.demo.utils.autoheal.reporting.AutoHealLogger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoHealCacheManager {
    private static final Map<String, HealResult> aiCache = new ConcurrentHashMap<>();
    private static final Map<String, HealResult> heurCache = new ConcurrentHashMap<>();

    private static final String AI_CACHE_FILE = "src/test/resources/ai_cache_master.json";
    private static final String HEUR_CACHE_FILE = "src/test/resources/heur_cache_master.json";

    static {
        loadCache(AI_CACHE_FILE, aiCache);
        loadCache(HEUR_CACHE_FILE, heurCache);
    }

    private static void loadCache(String filePath, Map<String, HealResult> targetMap) {
        try {
            File cacheFile = new File(filePath);
            if (cacheFile.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                List<HealResult> stored = mapper.readValue(cacheFile, new TypeReference<List<HealResult>>() {});
                for (HealResult heal : stored) {
                    targetMap.put(heal.original, heal);
                }
            }
        } catch (Exception e) {
            AutoHealLogger.logError("[CACHE_READ]", "Could not load cache " + filePath + ": " + e.getMessage());
        }
    }

    public static HealResult getCachedResult(String original, boolean preferAi) {
        if (preferAi && aiCache.containsKey(original)) {
            return aiCache.get(original);
        }
        if (heurCache.containsKey(original)) {
            return heurCache.get(original);
        }
        return null;
    }

    public static synchronized void recordCache(HealResult result) {
        if ("ai".equalsIgnoreCase(result.strategy)) {
            aiCache.put(result.original, result);
        } else {
            heurCache.put(result.original, result);
        }
    }

    public static synchronized void flushCaches() {
        AutoHealLogger.logCacheWrite("Flushing dual caches to disk...");
        flushCache(AI_CACHE_FILE, aiCache);
        flushCache(HEUR_CACHE_FILE, heurCache);
    }

    private static void flushCache(String filePath, Map<String, HealResult> cacheMap) {
        try {
            if (!cacheMap.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), cacheMap.values());
                AutoHealLogger.logCacheWrite("Saved " + cacheMap.size() + " items to " + filePath);
            }
        } catch (Exception e) {
            AutoHealLogger.logError("[CACHE_WRITE]", "Failed to write cache " + filePath + ": " + e.getMessage());
        }
    }
}
