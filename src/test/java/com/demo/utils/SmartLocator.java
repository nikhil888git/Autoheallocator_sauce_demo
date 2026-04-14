package com.demo.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.demo.utils.autoheal.AutoHealManager;

public class SmartLocator {

    private AutoHealManager autoHealManager;

    public SmartLocator(Page page) {
        // Delegate to the new architecture manager
        this.autoHealManager = new AutoHealManager(page);
    }

    public Locator find(String primary, String fallback) {
        return autoHealManager.getLocator(primary, fallback);
    }
}