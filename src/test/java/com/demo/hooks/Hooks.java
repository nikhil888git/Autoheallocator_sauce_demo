package com.demo.hooks;

import com.demo.utils.ConfigReader;
import com.demo.utils.PlaywrightFactory;
import com.demo.utils.HealingTracker;
import com.microsoft.playwright.*;
import io.cucumber.java.*;

public class Hooks {

    private PlaywrightFactory factory;
    private Page page;

    public static Page getPage() {
        return PlaywrightFactory.getPage();
    }

    @Before
    public void setUp() {
        HealingTracker.reset(); // reset per test

        String browserName = ConfigReader.get("browser");
        if (browserName == null || browserName.isEmpty()) {
            browserName = "chromium";
        }

        factory = new PlaywrightFactory();
        page = factory.initBrowser(browserName);
    }

    @After
    public void tearDown(Scenario scenario) {

        int healCount = HealingTracker.getHealCount();

        // Attach healing info exactly as requested
        String healingInfo = "Total locator heals: " + healCount;
        scenario.attach(healingInfo, "text/plain", "Healing Report");

        // Fail if healing exceeds threshold
        if (healCount > 2) {
            throw new RuntimeException(
                    "Test failed due to excessive self-healing. Count: " + healCount);
        }

        if (scenario.isFailed() && page != null) {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            
            // Save and attach trace
            try {
                String traceName = scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_trace.zip";
                java.nio.file.Path tracePath = java.nio.file.Paths.get("target/traces/" + traceName);
                PlaywrightFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));
                
                // Directly feed to Allure for Trace Viewer compatibility
                io.qameta.allure.Allure.addAttachment("Playwright Trace Viewer Archive", "application/zip", new java.io.FileInputStream(tracePath.toFile()), ".zip");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (PlaywrightFactory.getContext() != null) {
            // Drop trace if passed
            PlaywrightFactory.getContext().tracing().stop();
        }

        if (factory != null) {
            factory.closeBrowser();
        }
    }
}