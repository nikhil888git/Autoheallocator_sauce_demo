package com.demo.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.demo.utils.SmartLocator;

/**
 * ============================================================================
 * Page Name : CheckoutPage
 * Description : Handles cart and checkout operations
 * ============================================================================
 */

public class CheckoutPage {

    private Page page;
    private SmartLocator smart;

    public CheckoutPage(Page page) {
        this.page = page;
        this.smart = new SmartLocator(page);
    }

    /* ================= LOCATORS ================= */

    private Locator addToCartBtn() {
        return smart.find("[data-test='add-to-cart-sauce-labs-backpack']");
    }

    private Locator cartIcon() {
        return smart.find("[data-test='shopping-cart-link']");
    }

    private Locator checkoutBtn() {
        return smart.find("[data-test='checkout']");
    }

    private Locator firstName() {
        return smart.find("[data-test='firstName']");
    }

    private Locator lastName() {
        return smart.find("[data-test='lastName']");
    }

    private Locator postalCode() {
        return smart.find("[data-test='postalCode']");
    }

    private Locator continueBtn() {
        return smart.find("[data-test='continue']");
    }

    private Locator finishBtn() {
        return smart.find("[data-test='finish']");
    }

    private Locator successMessageLocator() {
        return smart.find("text=Thank you for your order");
    }

    /* ================= ACTIONS ================= */

    public void addProductToCart() {
        addToCartBtn().click();
    }

    public void goToCart() {
        cartIcon().click();
    }

    public void clickCheckout() {
        checkoutBtn().click();
    }

    public void enterDetails(String fName, String lName, String zip) {
        firstName().fill(fName);
        lastName().fill(lName);
        postalCode().fill(zip);
    }

    public void completeCheckout() {
        continueBtn().click();
        finishBtn().click();
    }

    /* ================= VALIDATIONS ================= */

    /**
     * Validation: Verify order completion success message is visible
     * 
     * @return Locator (used for Playwright assertions)
     */
    public Locator successMessage() {
        return successMessageLocator();
    }
}