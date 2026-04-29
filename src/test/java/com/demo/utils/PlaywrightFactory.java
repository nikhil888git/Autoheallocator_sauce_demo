package com.demo.utils;

import com.microsoft.playwright.*;

public class PlaywrightFactory {

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
            System.out.println("Global Browser instance initialized: " + browserName);
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
                    System.out.println("Invalid browser name: " + browserName + ". Defaulting to chromium.");
                    tlBrowser.set(getPlaywright().chromium().launch(options));
                    break;
            }
            System.out.println("Browser successfully launched in background Singleton");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize browser: " + browserName);
        }
    }

    public Page initContext() {
        try {
            tlContext.set(getBrowser().newContext());

            tlContext.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

            tlPage.set(getContext().newPage());
            return getPage();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize browser Scenario context");
        }
    }

    public void closeContext() {
        try {
            if (getPage() != null) {
                getPage().close();
                tlPage.remove();
            }
            if (getContext() != null) {
                getContext().close();
                tlContext.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeGlobalBrowser() {
        try {
            if (getBrowser() != null) {
                getBrowser().close();
                tlBrowser.remove();
            }
            if (getPlaywright() != null) {
                getPlaywright().close();
                tlPlaywright.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}