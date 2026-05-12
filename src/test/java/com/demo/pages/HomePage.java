package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

import com.demo.utils.SmartLocator;

public class HomePage {

    private final Page page;
    private final SmartLocator smart;

    // Locators
    // private final Locator logo;
    private final Locator hamburgerMenu;
    private final Locator cartIcon;
    private final Locator filterDropdown;
    private final Locator productTiles;
    private final Locator productNames;
    private final Locator productImages;
    private final Locator productPrices;
    private final Locator addToCartButtons;

    public HomePage(Page page) {
        this.page = page;
        this.smart = new SmartLocator(page);

        // 1. Side menu bar (hamburger menu)
        this.hamburgerMenu = smart.find("#react-burger-menu-btn", "button:has-text('Open Menu')");

        // 2. Cart icon
        this.cartIcon = smart.find(".shopping_cart_link", "[data-test='shopping-cart-link']");

        // 3. Filter dropdown
        this.filterDropdown = smart.find(".product_sort_container", "[data-test='product-sort-container']");

        // 4. Product tiles
        this.productTiles = smart.find(".inventory_item", "[data-test='inventory-item']");

        // Product specific locators
        this.productNames = smart.find(".inventory_item_name", "[data-test='inventory-item-name']");
        this.productImages = smart.find(".inventory_item_img img", "[data-test='inventory-item-img']");
        this.productPrices = smart.find(".inventory_item_price", "[data-test='inventory-item-price']");

        // Using SmartLocator with text-based fallback for Add to cart buttons
        // FIXED: Generic locator for ALL add to cart buttons
        this.addToCartButtons = smart.find(
                "button[data-test^='add-to-cart']",
                "button.btn_inventory",
                "button:has-text('Add to cart')");

        // Cart badge for bonus validation is removed from constructor to prevent
        // premature AutoHeal timeouts when the cart is empty.
    }

    // public Locator getLogo() {
    // return logo;
    // }
    /* ================= SOCIAL LINKS ================= */
    public Locator getHamburgerMenu() {
        return smart.find("#react-burger-menu-btn", "button:has-text('Open Menu')");
    }

    public Locator linkedinLink() {
        return smart.find("[data-test='social-linkedin']", "a[href*='linkedin']");
    }

    public Locator facebookLink() {
        return smart.find("[data-test='social-facebook']", "a[href*='facebook']");
    }

    public Locator twitterLink() {
        return smart.find("[data-test='social-twitter'], a[href*='twitter']");
    }

    // 🔥 FIXED: Stable + fallback locators
    public Locator getCartBadge() {
        return smart.find(
                "span[data-test='shopping-cart-badge']",
                "#shopping_cart_container span",
                "a.shopping_cart_link >> span");
    }

    public Locator getCartIcon() {
        return cartIcon;
    }

    public Locator getFilterDropdown() {
        return filterDropdown;
    }

    public Locator getProductTiles() {
        return productTiles;
    }

    public List<Locator> getAllProductTiles() {
        return productTiles.all();
    }

    public int getProductCount() {
        return productTiles.count();
    }

    public List<Locator> getAllProductNames() {
        return productNames.all();
    }

    public List<Locator> getAllProductImages() {
        return productImages.all();
    }

    public List<Locator> getAllProductPrices() {
        return productPrices.all();
    }

    public List<Locator> getAllAddToCartButtons() {
        return addToCartButtons.all();
    }

    // Bonus: Add method to click "Add to cart" for all products
    public void addAllProductsToCart() {
        // We iterate through product tiles instead of the addToCartButtons,
        // because clicking a button changes its text to "Remove",
        // which would cause an index shift and timeout for subsequent clicks.
        for (Locator tile : getProductTiles().all()) {
            tile.locator("button.btn_inventory").click();
        }
    }

    // 🔥 FIX 1: OPTIONAL ELEMENT HANDLING (MNC LEVEL)
    public String getCartItemCount() {
        Locator badge = page.locator("span[data-test='shopping-cart-badge']");
        if (badge.count() > 0) {
            return badge.first().textContent().trim();
        }
        return "0"; // when badge not visible
    }

    public Page openLinkedIn() {
        return page.waitForPopup(() -> linkedinLink().click());
    }

    public Page openFacebook() {
        return page.waitForPopup(() -> facebookLink().click());
    }

    public Page openTwitter() {
        return page.waitForPopup(() -> twitterLink().click());
    }
}