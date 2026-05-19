package com.demo.stepdefs;

import com.microsoft.playwright.*;

public class testmobile {
    public static void main(String[] args) {
        System.out.println("Starting Playwright Mobile Emulation Demo...");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            // Note: Playwright Java does not have the 'devices' dictionary natively.
            // We have to set the device options manually to emulate iPhone 13.
            Browser.NewContextOptions mobileOptions = new Browser.NewContextOptions()
                    .setUserAgent(
                            "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
                    .setViewportSize(390, 844)
                    .setDeviceScaleFactor(3)
                    .setIsMobile(true)
                    .setHasTouch(true);

            BrowserContext context = browser.newContext(mobileOptions);
            Page page = context.newPage();

            // 2. Navigate to login page
            System.out.println("Navigating to SauceDemo Login...");
            page.navigate("https://www.saucedemo.com/");

            // 3. Perform login actions
            System.out.println("Entering credentials...");
            page.locator("[data-test='username']").fill("standard_user");
            page.locator("[data-test='password']").fill("secret_sauce");

            System.out.println("Clicking login...");
            page.locator("[data-test='login-button']").click();

            // 4. Verify successful login
            page.waitForURL("https://www.saucedemo.com/inventory.html");
            boolean isLoggedIn = page.locator(".app_logo").isVisible();
            System.out.println("Login Successful! Inventory page visible: " + isLoggedIn);

            // Wait for 3 seconds to visually see the result
            page.waitForTimeout(3000);

            // Cleanup
            context.close();
            browser.close();
            System.out.println("Demo completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
