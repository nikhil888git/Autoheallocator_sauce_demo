package com.demo.stepdefs;

import com.demo.hooks.Hooks;
import com.demo.pages.LoginPage;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.regex.Pattern;

import io.cucumber.java.en.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginSteps {

    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    LoginPage loginPage = new LoginPage(Hooks.getPage());

    @Given("user launches browser")
    public void launch_browser() {
        logger.info("Browser launched and initialized successfully");
    }

    @When("user opens login page")
    public void open_login_page() {
        logger.debug("Navigating to login page");
        loginPage.open();
    }

    @When("user logs in with {string} and {string}")
    public void login(String username, String password) {
        logger.info("Attempting login with username: {}", username);
        loginPage.login(username, password);
    }

    @Then("user should see homepage")
    public void verify_homepage() {

        Hooks.getPage().waitForURL(
                "**/inventory.html",
                new Page.WaitForURLOptions().setTimeout(5000));

        assertThat(Hooks.getPage())
                .hasURL(Pattern.compile(".*inventory.html"));

        logger.info("User successfully landed on Home Page");
    }

    @Then("user should not be redirected to inventory page")
    public void user_should_not_be_redirected_to_inventory_page() {

        assertThat(Hooks.getPage())
                .not()
                .hasURL(Pattern.compile(".*inventory.html"));

        assertThat(loginPage.loginButton())
                .isVisible();

        logger.info("User remained on login page as expected");
    }

    @Then("user should see login error message")
    public void verify_login_error() {

        assertThat(loginPage.errorMessage())
                .isVisible();

        assertThat(loginPage.errorMessage())
                .containsText("Username and password do not match");

        logger.info("Login error displayed successfully");
    }

    @Then("login form should be visible")
    public void login_form_should_be_visible() {
        logger.debug("Verifying login form visibility");
        assertThat(loginPage.loginButton()).isVisible();
    }
}