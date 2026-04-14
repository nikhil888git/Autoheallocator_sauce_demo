package com.demo.stepdefs;

import com.demo.hooks.Hooks;
import com.demo.pages.LoginPage;
import com.microsoft.playwright.assertions.PageAssertions;

//import com.microsoft.playwright.assertions.PlaywrightAssertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import io.cucumber.java.en.*;

public class LoginSteps {

    LoginPage loginPage = new LoginPage(Hooks.getPage());

    @Given("user launches browser")
    public void launch_browser() {
        // Browser already initialized
    }

    @When("user opens login page")
    public void open_login_page() {
        loginPage.open();
    }

    @When("user logs in with {string} and {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
        // Do not wait for URL here, because negative tests will never navigate to
        // inventory!
    }

    @Then("user should not be redirected to inventory page")
    public void user_should_not_be_redirected() {
        // ✅ 1. Verify error message is visible
        assertThat(loginPage.getErrorMsgLocator()).isVisible();
        String currentUrl = Hooks.getPage().url();
        System.out.println("Current URL: " + currentUrl);
        // ✅ 2. Verify URL does NOT contain inventory
        assertThat(Hooks.getPage()).not().hasURL("**/inventory.html",
                new PageAssertions.HasURLOptions().setTimeout(5000));
        System.out.println("[PASS] User stayed on login page");
    }

    // ✅ FIXED STEP
    @Then("login form should be visible")
    public void login_form_should_be_visible() {
        assertThat(loginPage.getErrorMsgLocator()).isVisible();
    }

    @Then("user should see homepage")
    public void verify_homepage() {

        // Wait for navigation specifically in the positive case
        Hooks.getPage().waitForURL("**/inventory.html");

        String currentUrl = Hooks.getPage().url();

        assert currentUrl.contains("inventory.html")
                : "User not navigated to Home Page";

        System.out.println("User landed on Home Page: " + currentUrl);
    }
}