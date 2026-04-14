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

    public Page initBrowser(String browserName) {
        try {
            System.out.println("Browser context initialized: " + browserName);
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

            tlContext.set(getBrowser().newContext());

            tlContext.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));

            tlPage.set(getContext().newPage());
            System.out.println("Browser launched");
            return getPage();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize browser: " + browserName);
        }
    }

    public void closeBrowser() {
        try {
            if (getPage() != null) {
                getPage().close();
                tlPage.remove();
            }
            if (getContext() != null) {
                getContext().close();
                tlContext.remove();
            }
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