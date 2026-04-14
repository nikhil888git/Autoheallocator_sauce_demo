package com.demo.utils;

import java.io.FileWriter;
import java.io.IOException;

public class HealingTracker {

    private static ThreadLocal<Integer> healCount = ThreadLocal.withInitial(() -> 0);

    public static void recordHealing(String primary, String fallback) {
        int count = healCount.get() + 1;
        healCount.set(count);

        System.out.println("[HEAL] Primary failed: " + primary);
        System.out.println("[HEAL] Using fallback: " + fallback);
        System.out.println("[HEAL COUNT]: " + count);

        // Optional: save healed locator to JSON
        saveHealing(primary, fallback);
    }

    private static void saveHealing(String primary, String fallback) {
        try (FileWriter fw = new FileWriter("target/healed-locators.json", true)) {
            fw.write("{\"failed\": \"" + primary + "\", \"used\": \"" + fallback + "\"}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getHealCount() {
        return healCount.get();
    }

    public static void reset() {
        healCount.set(0);
    }
}