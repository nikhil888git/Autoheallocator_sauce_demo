package com.demo.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.ITestResult;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.demo.utils.HealingTracker;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();
        // Headless mode can be set to true if needed for CI
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
    }

    @BeforeMethod
    public void beforeMethod() {
        HealingTracker.reset();
        context.tracing().startChunk();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        int healCount = HealingTracker.getHealCount();
        String healingInfo = "Total locator heals: " + healCount;
        Allure.addAttachment("Healing Report", "text/plain", new ByteArrayInputStream(healingInfo.getBytes()), ".txt");

        if (!result.isSuccess() && page != null) {
            Page activePage = page;
            if (context != null) {
                java.util.List<Page> openPages = context.pages();
                if (!openPages.isEmpty()) {
                    activePage = openPages.get(openPages.size() - 1);
                }
            }

            com.demo.utils.reporting.TestEvidenceManager.captureFailureEvidence(activePage, result.getMethod().getMethodName(), result.getThrowable());

            try {
                String traceName = result.getMethod().getMethodName() + "_trace.zip";
                Path tracePath = Paths.get("target/playwright-traces/" + traceName);
                Files.createDirectories(tracePath.getParent());
                context.tracing().stopChunk(new com.microsoft.playwright.Tracing.StopChunkOptions().setPath(tracePath));
            } catch (Exception e) {
                System.err.println("Could not extract trace zip: " + e.getMessage());
            }
        } else if (context != null) {
            context.tracing().stopChunk(); // Drop the trace chunk
        }

        if (healCount > 2) {
            throw new RuntimeException("Test failed due to excessive self-healing. Count: " + healCount);
        }
    }

    @AfterClass
    public void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
