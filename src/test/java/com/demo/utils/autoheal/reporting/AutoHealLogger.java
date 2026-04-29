package com.demo.utils.autoheal.reporting;

public class AutoHealLogger {

    public static void logExecution(String message) {
        System.out.println("[EXECUTION] " + message);
    }

    public static void logAiHeal(String message) {
        System.out.println("[AI_HEAL] " + message);
    }

    public static void logHeuristicHeal(String message) {
        System.out.println("[HEURISTIC_HEAL] " + message);
    }

    public static void logAssertion(String message) {
        System.out.println("[ASSERTION] " + message);
    }

    public static void logRecovery(String message) {
        System.out.println("[RECOVERY] " + message);
    }

    public static void logCacheWrite(String message) {
        System.out.println("[CACHE_WRITE] " + message);
    }
    
    public static void logError(String prefix, String message) {
        System.err.println(prefix + " ERROR: " + message);
    }
}
