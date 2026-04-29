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

    @BeforeAll
    public static void setupGlobal() {
        String browserName = ConfigReader.get("browser");
        if (browserName == null || browserName.isEmpty()) {
            browserName = "chromium";
        }
        PlaywrightFactory.initGlobalBrowser(browserName);
    }

    @AfterAll
    public static void tearDownGlobal() {
        PlaywrightFactory.closeGlobalBrowser();
    }

    @Before
    public void setUp() {
        HealingTracker.reset(); // reset per test

        factory = new PlaywrightFactory();
        page = factory.initContext();
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
            // Target the most recently opened page (e.g., failed popups) to ensure accurate screenshots
            Page activePage = page;
            if (PlaywrightFactory.getContext() != null) {
                java.util.List<Page> openPages = PlaywrightFactory.getContext().pages();
                if (!openPages.isEmpty()) {
                    activePage = openPages.get(openPages.size() - 1);
                }
            }

            // Centralized Stateless Evidence Call
            com.demo.utils.reporting.TestEvidenceManager.captureFailureEvidence(activePage, scenario.getName(), null);
            
            // Save Playwright trace ONLY for failure (Native API, no Allure IO duplicate)
            if (PlaywrightFactory.getContext() != null) {
                try {
                    String traceName = scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_trace.zip";
                    java.nio.file.Path tracePath = java.nio.file.Paths.get("target/playwright-traces/" + traceName);
                    java.nio.file.Files.createDirectories(tracePath.getParent());
                    PlaywrightFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));
                } catch (Exception e) {
                    System.err.println("Could not extract trace zip: " + e.getMessage());
                }
            }
        } else if (PlaywrightFactory.getContext() != null) {
            // Discard memory buffer instantly (zero disk writing)
            PlaywrightFactory.getContext().tracing().stop();
        }

        if (factory != null) {
            factory.closeContext();
        }
    }
}