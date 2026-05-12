package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.demo.utils.ConfigReader;
import com.demo.utils.SmartLocator;

public class LoginPage {

    private final Page page;
    private final SmartLocator smart;

    public LoginPage(Page page) {
        this.page = page;
        this.smart = new SmartLocator(page);
    }

    // ---------------- LOCATORS ----------------

    public Locator username() {
        return smart.find("[data-test='username']", "#user-name");
    }

    public Locator password() {
        return smart.find("[data-test='password']", "#password");
    }

    public Locator loginButton() {
        return smart.find("#login-button", "[data-test='login-button']", "input[type='submit']");
    }

    public Locator errorMessage() {
        return smart.find("[data-test='error']", ".error-message-container");
    }

    // ---------------- ACTIONS ----------------

    public void open() {
        page.navigate(ConfigReader.getBaseUrl() + "/");
    }

    public void login(String user, String pass) {
        username().fill(user);
        password().fill(pass);
        loginButton().click();
    }

    // ---------------- STATE HELPERS ----------------

    public boolean isLoginFormDisplayed() {
        return username().isVisible()
                && password().isVisible()
                && loginButton().isVisible();
    }

    public boolean isErrorVisible() {
        return errorMessage().isVisible();
    }

    public boolean isOnInventoryPage() {
        return page.url().contains("inventory.html");
    }

    public Page getPage() {
        return page;
    }
}