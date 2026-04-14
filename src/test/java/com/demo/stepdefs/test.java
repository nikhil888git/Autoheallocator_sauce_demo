package com.demo.stepdefs;
    import com.microsoft.playwright.*;


public class test {
  public static void main(String[] args) {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(false));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();
      page.navigate("https://www.saucedemo.com/");
      page.locator("[data-test=\"username\"]").click();
      page.locator("[data-test=\"password\"]").click();
      page.locator("[data-test=\"password\"]").fill("");
      page.locator("[data-test=\"username\"]").click();
      page.locator("[data-test=\"username\"]").fill("standard_user");
      page.locator("[data-test=\"password\"]").click();
      page.locator("[data-test=\"password\"]").fill("secret_sauce");
      page.locator("[data-test=\"login-button\"]").click();
      page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();
      page.locator("[data-test=\"shopping-cart-link\"]").click();
      page.locator("[data-test=\"checkout\"]").click();
      page.locator("[data-test=\"firstName\"]").click();
      page.locator("[data-test=\"firstName\"]").fill("test");
      page.locator("[data-test=\"lastName\"]").click();
      page.locator("[data-test=\"lastName\"]").fill("test");
      page.locator("[data-test=\"postalCode\"]").click();
      page.locator("[data-test=\"postalCode\"]").fill("test");
      page.locator("[data-test=\"continue\"]").click();
      page.locator("[data-test=\"finish\"]").click();
      page.locator("[data-test=\"back-to-products\"]").click();
    }
  }
}

