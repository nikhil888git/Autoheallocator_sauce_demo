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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeSteps {

    private static final Logger logger = LoggerFactory.getLogger(HomeSteps.class);

    private Page getPage() {
        return Hooks.getPage();
    }

    private HomePage getHomePage() {
        return new HomePage(getPage());
    }

    @Given("the user is logged into the application")
    public void the_user_is_logged_into_the_application() {
        logger.info("Setting up user session (Login)");

        Page page = Hooks.getPage();
        com.demo.pages.LoginPage loginPage = new com.demo.pages.LoginPage(page);

        // Always start from login page (no hidden conditions)
        loginPage.open();

        loginPage.login(
                ConfigReader.get("TEST_USER"),
                ConfigReader.get("TEST_PASSWORD"));

        // Validate successful login via UI (NOT just URL)
        HomePage homePage = new HomePage(page);
        assertThat(homePage.getHamburgerMenu()).isVisible();
    }

    @Then("the user should see the {string} displayed on the home page")
    public void the_user_should_see_component(String component) {
        logger.debug("Verifying visibility of component: {}", component);
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
        logger.debug("Verifying product count is exactly {}", expectedCount);
        assertEquals(getHomePage().getProductCount(), expectedCount);
    }

    @Then("each product should have a name, image, price, and add to cart button")
    public void each_product_should_have_details() {
        logger.info("Verifying all product details (name, image, price, button)");
        HomePage home = getHomePage();

        int totalProducts = home.getProductCount();

        List<Locator> productNames = home.getAllProductNames();
        List<Locator> productImages = home.getAllProductImages();
        List<Locator> productPrices = home.getAllProductPrices();
        List<Locator> addToCartButtons = home.getAllAddToCartButtons();

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
        logger.debug("Verifying all product prices contain currency symbol: {}", symbol);
        for (Locator price : getHomePage().getAllProductPrices()) {
            assertThat(price).isVisible();
            assertThat(price).containsText(symbol);
        }
    }

    @When("the user adds all products to the cart")
    public void user_adds_all_products() {
        logger.info("Adding all available products to the cart");
        getHomePage().addAllProductsToCart();
    }

    // 🔥 FIX 2: SAFE ASSERTION
    @Then("the cart badge count should be displayed as {string}")
    public void cart_badge_count_matches(String expectedCount) {
        logger.debug("Verifying cart badge count matches: {}", expectedCount);
        String actual = getHomePage().getCartItemCount();
        assertEquals(actual, expectedCount);
    }

    @Then("user should verify social media links")
    public void verify_social_links() {
        logger.info("Verifying social media href links without external navigation");
        HomePage home = getHomePage();

        // Check href directly to avoid 3rd party availability/bot-blocker flakiness (MNC standard)
        assertThat(home.linkedinLink()).hasAttribute("href", "https://www.linkedin.com/company/sauce-labs/");
        assertThat(home.facebookLink()).hasAttribute("href", "https://www.facebook.com/saucelabs");
        assertThat(home.twitterLink()).hasAttribute("href", "https://twitter.com/saucelabs");
    }
}
