package com.demo.stepdefs;

import com.demo.hooks.Hooks;
import com.demo.pages.CheckoutPage;
import static org.testng.Assert.assertEquals;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import io.cucumber.java.en.*;

/**
 * ============================================================================
 * Class Name : CheckoutSteps
 * Description: Step Definitions for Checkout Flow
 * ============================================================================
 */

public class CheckoutSteps {

    private CheckoutPage getCheckoutPage() {
        return new CheckoutPage(Hooks.getPage());
    }

    @When("user adds product to cart")
    public void add_product_to_cart() {
        getCheckoutPage().addProductToCart();
    }

    @When("user proceeds to checkout")
    public void proceed_to_checkout() {
        getCheckoutPage().goToCart();
        getCheckoutPage().clickCheckout();
    }

    @When("user enters checkout details")
    public void enter_checkout_details() {
        getCheckoutPage().enterDetails("test", "test", "12345");
    }

    @Then("user should complete the order successfully")
    public void verify_order_success() {
        getCheckoutPage().completeCheckout();
        assertThat(getCheckoutPage().successMessage()).isVisible();
    }

    @When("user enters checkout details {string} {string} {string}")
    public void enter_checkout_details(String firstName, String lastName, String postalCode) {
        getCheckoutPage().enterDetails(firstName, lastName, postalCode);
    }

    @Then("user should not be able to complete the order")
    public void verify_order_failure() {
        getCheckoutPage().clickContinue();
        assertThat(getCheckoutPage().errorMessage()).isVisible();
    }
}