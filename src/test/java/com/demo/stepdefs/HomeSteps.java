package com.demo.stepdefs;

import com.demo.hooks.Hooks;
import com.demo.pages.HomePage;
import com.demo.utils.ConfigReader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class HomeSteps {

    private Page getPage() {
        return Hooks.getPage();
    }

    private HomePage getHomePage() {
        return new HomePage(getPage());
    }

    @Given("the user is logged into the application")
    public void the_user_is_logged_into_the_application() {
        Page page = getPage();
        if (page.url().equals("about:blank") || !page.url().contains("inventory.html")) {
            String baseUrl = ConfigReader.getBaseUrl();
            page.navigate(baseUrl + "/");
            page.locator("#user-name").fill(ConfigReader.get("TEST_USER"));
            page.locator("#password").fill(ConfigReader.get("TEST_PASSWORD"));
            page.locator("#login-button").click();
            assertThat(page).hasURL(baseUrl + "/inventory.html");
        }
    }

    @Then("the user should see the {string} displayed on the home page")
    public void the_user_should_see_component(String component) {
        switch (component.toLowerCase()) {
            case "hamburger menu":
                assertThat(getHomePage().getHamburgerMenu()).isVisible();
                break;
            case "cart icon":
                assertThat(getHomePage().getCartIcon()).isVisible();
                break;
            case "filter dropdown":
                assertThat(getHomePage().getFilterDropdown()).isVisible();
                break;
            default:
                throw new IllegalArgumentException("Unknown component: " + component);
        }
    }

    @Then("the user should see {int} products")
    public void the_user_should_see_products(int expectedCount) {
        int totalProducts = getHomePage().getProductCount();
        assertEquals(totalProducts, expectedCount, "Product count mismatch!");
    }

    @Then("each product should have a name, image, price, and add to cart button")
    public void each_product_should_have_details() {
        int totalProducts = getHomePage().getProductCount();
        List<Locator> productNames = getHomePage().getAllProductNames();
        List<Locator> productImages = getHomePage().getAllProductImages();
        List<Locator> productPrices = getHomePage().getAllProductPrices();
        List<Locator> addToCartButtons = getHomePage().getAllAddToCartButtons();

        assertEquals(productNames.size(), totalProducts);
        assertEquals(productImages.size(), totalProducts);
        assertEquals(productPrices.size(), totalProducts);
        assertEquals(addToCartButtons.size(), totalProducts);

        for (Locator btn : addToCartButtons) {
            assertThat(btn).isVisible();
            assertThat(btn).hasText("Add to cart");
        }
        for (Locator img : productImages) {
            assertThat(img).isVisible();
        }
    }

    @Then("the product prices should contain {string}")
    public void product_prices_should_contain(String symbol) {
        for (Locator price : getHomePage().getAllProductPrices()) {
            assertThat(price).isVisible();
            assertThat(price).containsText(symbol);
        }
    }

    @When("the user adds all products to the cart")
    public void user_adds_all_products() {
        getHomePage().addAllProductsToCart();
    }

    @Then("the cart badge count should be displayed as {string}")
    public void cart_badge_count_matches(String expectedCount) {
        String cartCount = getHomePage().getCartItemCount();
        assertEquals(cartCount, expectedCount, "Cart badge count is incorrect.");
    }
}
