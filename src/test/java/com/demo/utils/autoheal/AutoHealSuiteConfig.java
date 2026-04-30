package com.demo.utils.autoheal;

import java.io.InputStream;
import java.util.Properties;

public class AutoHealSuiteConfig {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = AutoHealSuiteConfig.class.getClassLoader().getResourceAsStream("autoheal.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isEnabled() {
        return Boolean.parseBoolean(properties.getProperty("autoheal.enabled", "false"));
    }

    public static String getStrategy() {
        return properties.getProperty("autoheal.strategy", "heuristic");
    }

    public static String getApiKey() {
        String envKey = com.demo.utils.ConfigReader.get("API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey;
        }
        return properties.getProperty("autoheal.ai.apiKey", "");
    }

    public static String getBaseUrl() {
        return properties.getProperty("autoheal.ai.baseUrl", "https://api.openai.com/v1");
    }

    public static String getModel() {
        return properties.getProperty("autoheal.ai.model", "gpt-4o-mini");
    }

    public static int getMaxRetries() {
        return Integer.parseInt(properties.getProperty("autoheal.maxRetries", "1"));
    }

    public static double getConfidenceThreshold() {
        return Double.parseDouble(properties.getProperty("autoheal.ai.confidenceThreshold", "0.8"));
    }
}
