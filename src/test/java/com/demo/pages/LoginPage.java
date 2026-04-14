package com.demo.pages;

import com.microsoft.playwright.Page;
import com.demo.utils.ConfigReader;
import com.demo.utils.SmartLocator;

public class LoginPage {

    private Page page;
    private SmartLocator smart;

    public LoginPage(Page page) {
        this.page = page;
        this.smart = new SmartLocator(page);
    }

    private com.microsoft.playwright.Locator getUsernameLocator() {
        return smart.find("[data-test='username']", "#user-name");
    }

    private com.microsoft.playwright.Locator getPasswordLocator() {
        return smart.find("[data-test='password']", "#password");
    }

    private com.microsoft.playwright.Locator getLoginBtnLocator() {

        return smart.find("[data-test='login-button']", "input[type='submit']");
    }

    // private com.microsoft.playwright.Locator getLogoLocator() {
    // return smart.find("text=Swag Labs", ".login_logo");
    // }

    public com.microsoft.playwright.Locator getErrorMsgLocator() {
        return smart.find("[data-test='error']", ".error-message-container");
    }

    public void open() {
        page.navigate(ConfigReader.getBaseUrl() + "/");
    }

    public void login(String user, String pass) {
        getUsernameLocator().waitFor();
        getUsernameLocator().fill(user);
        getPasswordLocator().waitFor();
        getPasswordLocator().fill(pass);
        getLoginBtnLocator().waitFor();
        getLoginBtnLocator().click();
        page.waitForLoadState();
    }

    public boolean isLoginSuccessful() {
        return page.url().contains("inventory");
    }

    // public boolean isLogoDisplayed() {
    // return getLogoLocator().isVisible();
    // }

    public boolean isErrorMessageDisplayed() {
        try {
            getErrorMsgLocator().waitFor();
            return getErrorMsgLocator().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonVisible() {
        try {
            return getLoginBtnLocator().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

}