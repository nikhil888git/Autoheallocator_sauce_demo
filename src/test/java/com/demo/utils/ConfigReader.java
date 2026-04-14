package com.demo.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();
    private static Dotenv dotenv;

    static {
        // Load dotenv securely, ignore if missing (so CI/CD can use system env)
        try {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        } catch (Exception e) {
            System.err.println("Could not load .env file. Falling back to system properties/config.properties");
        }

        try {
            InputStream input = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");
            if (input != null) {
                prop.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        // Priority 1: System Property (from Jenkins/GitHub Actions or CLI -D property)
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) return value;

        // Priority 2: .env file
        if (dotenv != null) {
            value = dotenv.get(key);
            if (value != null && !value.isEmpty()) return value;
        }

        // Priority 3: config.properties default
        return prop.getProperty(key);
    }

    public static String getBaseUrl() {
        String env = get("ENV");
        if (env == null || env.isEmpty()) {
            env = "PROD"; // Default
        }
        String baseUrl = get(env.toUpperCase() + "_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = get("url"); // fallback to config.properties
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }
}