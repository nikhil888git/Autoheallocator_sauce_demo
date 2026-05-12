package com.demo.stepdefs;

import com.demo.hooks.Hooks;
import com.demo.pages.LoginPage;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.regex.Pattern;

import io.cucumber.java.en.*;

public class LoginSteps {

    LoginPage loginPage = new LoginPage(Hooks.getPage());

    @Given("user launches browser")
    public void launch_browser() {
        System.out.println("Browser launched");
    }

    @When("user opens login page")
    public void open_login_page() {
        loginPage.open();
    }

    @When("user logs in with {string} and {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("user should see homepage")
    public void verify_homepage() {

        Hooks.getPage().waitForURL(
                "**/inventory.html",
                new Page.WaitForURLOptions().setTimeout(5000));

        assertThat(Hooks.getPage())
                .hasURL(Pattern.compile(".*inventory.html"));

        System.out.println(
                "User landed on Home Page: " + Hooks.getPage().url());
    }

    @Then("user should not be redirected to inventory page")
    public void user_should_not_be_redirected_to_inventory_page() {

        assertThat(Hooks.getPage())
                .not()
                .hasURL(Pattern.compile(".*inventory.html"));

        assertThat(loginPage.loginButton())
                .isVisible();

        System.out.println("User remained on login page");
    }

    @Then("user should see login error message")
    public void verify_login_error() {

        assertThat(loginPage.errorMessage())
                .isVisible();

        assertThat(loginPage.errorMessage())
                .containsText("Username and password do not match");

        System.out.println("Login error displayed successfully");
    }
}