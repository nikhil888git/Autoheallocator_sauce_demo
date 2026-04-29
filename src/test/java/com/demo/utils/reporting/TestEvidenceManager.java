package com.demo.utils.reporting;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestEvidenceManager {

    /**
     * MNC-Grade Failure Reporting strictly isolated from filesystem dependencies.
     * Compliant with ISO 29119 defect trace principles.
     */
    public static byte[] captureFailureEvidence(Page page, String testName, Throwable exception) {
        if (page == null) return null;

        try {
            // 1. Current URL Address
            String url = page.url();
            Allure.addAttachment("Defect Origin Context (URL)", "text/plain", url, ".txt");

            // 2. Exception Stack Trace Code Evidence
            if (exception != null) {
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));
                Allure.addAttachment("JVM Exception Stack Sequence", "text/plain", sw.toString(), ".txt");
            }   

            // 3. Automated UI Browser State
            byte[] screenshotBytes;
            try {
                // Attempt full page capture (Fails natively on Playwright error protocol pages like chrome-error://)
                screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            } catch (Exception fullPageEx) {
                // Fallback to viewport bounds
                screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(false));
            }
            Allure.addAttachment("Automated UI Failure State", "image/png", new ByteArrayInputStream(screenshotBytes), ".png");
            
            return screenshotBytes;
        } catch (Exception e) {
            System.err.println("Critical Error inside TestEvidenceManager while binding native artifacts: " + e.getMessage());
            return null;
        }
    }
}
