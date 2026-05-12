package com.demo.utils;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaywrightFactory {

    private static final Logger logger = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static void initGlobalBrowser(String browserName) {
        if (tlPlaywright.get() != null) return;
        try {
            logger.info("[Thread {}] Global Browser instance initialized: {}", Thread.currentThread().getId(), browserName);
            tlPlaywright.set(Playwright.create());

            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

            switch (browserName.toLowerCase()) {
                case "chromium":
                    tlBrowser.set(getPlaywright().chromium().launch(options));
                    break;
                case "firefox":
                    tlBrowser.set(getPlaywright().firefox().launch(options));
                    break;
                case "webkit":
                    tlBrowser.set(getPlaywright().webkit().launch(options));
                    break;
                default:
                    logger.warn("[Thread {}] Invalid browser name: {}. Defaulting to chromium.", Thread.currentThread().getId(), browserName);
                    tlBrowser.set(getPlaywright().chromium().launch(options));
                    break;
            }
            logger.info("[Thread {}] Browser successfully launched in background Singleton", Thread.currentThread().getId());
        } catch (Exception e) {
            logger.error("[Thread {}] Failed to initialize browser: {}", Thread.currentThread().getId(), browserName, e);
            throw new RuntimeException("Failed to initialize browser: " + browserName);
        }
    }

    public Page initContext() {
        try {
            if (getBrowser() == null) {
                String browserName = com.demo.utils.ConfigReader.get("browser");
                initGlobalBrowser(browserName != null && !browserName.isEmpty() ? browserName : "chromium");
            }
            logger.info("[Thread {}] Initializing new BrowserContext and Page", Thread.currentThread().getId());
            tlContext.set(getBrowser().newContext());

            tlContext.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

            tlPage.set(getContext().newPage());
            return getPage();
        } catch (Exception e) {
            logger.error("[Thread {}] Failed to initialize browser Scenario context", Thread.currentThread().getId(), e);
            throw new RuntimeException("Failed to initialize browser Scenario context");
        }
    }

    public void closeContext() {
        try {
            logger.info("[Thread {}] Closing BrowserContext and Page", Thread.currentThread().getId());
            if (getPage() != null) {
                getPage().close();
                tlPage.remove();
            }
            if (getContext() != null) {
                getContext().close();
                tlContext.remove();
            }
        } catch (Exception e) {
            logger.error("[Thread {}] Error while closing context", Thread.currentThread().getId(), e);
        }
    }

    public static void closeGlobalBrowser() {
        try {
            logger.info("[Thread {}] Closing Global Browser and Playwright instances", Thread.currentThread().getId());
            if (getBrowser() != null) {
                getBrowser().close();
                tlBrowser.remove();
            }
            if (getPlaywright() != null) {
                getPlaywright().close();
                tlPlaywright.remove();
            }
        } catch (Exception e) {
            logger.error("[Thread {}] Error while closing global browser", Thread.currentThread().getId(), e);
        }
    }
}