package com.demo.hooks;

import com.demo.utils.ConfigReader;
import com.demo.utils.PlaywrightFactory;
import com.demo.utils.HealingTracker;
import com.microsoft.playwright.*;
import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

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
        logger.info("[Global] Setting up global browser: {}", browserName);
        PlaywrightFactory.initGlobalBrowser(browserName);
    }

    @AfterAll
    public static void tearDownGlobal() {
        logger.info("[Global] Tearing down global browser");
        PlaywrightFactory.closeGlobalBrowser();
    }

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        logger.info("[Thread {}] Starting scenario: {}", Thread.currentThread().getId(), scenario.getName());
        HealingTracker.reset(); // reset per test

        factory = new PlaywrightFactory();
        page = factory.initContext();
    }

    @Before(value = "@login", order = 2)
    public void login() {
        logger.info("[Thread {}] Executing @login hook", Thread.currentThread().getId());
        page.navigate(ConfigReader.getBaseUrl());
        com.demo.pages.LoginPage loginPage = new com.demo.pages.LoginPage(page);
        loginPage.login(ConfigReader.get("TEST_USER"), ConfigReader.get("TEST_PASSWORD"));
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("[Thread {}] Tearing down scenario: {} | Status: {}", 
            Thread.currentThread().getId(), scenario.getName(), scenario.getStatus());

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
            logger.error("[Thread {}] Scenario Failed, capturing evidence", Thread.currentThread().getId());
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
                    logger.info("[Thread {}] Captured trace: {}", Thread.currentThread().getId(), tracePath.toString());
                } catch (Exception e) {
                    logger.error("[Thread {}] Could not extract trace zip: {}", Thread.currentThread().getId(), e.getMessage());
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