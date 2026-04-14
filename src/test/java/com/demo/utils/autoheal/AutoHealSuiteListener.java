package com.demo.utils.autoheal;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class AutoHealSuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        if (AutoHealSuiteConfig.isEnabled()) {
            System.out.println("[AutoHeal] AutoHealing is ENABLED. Strategy: " + AutoHealSuiteConfig.getStrategy());
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        if (AutoHealSuiteConfig.isEnabled()) {
            AutoHealReportAggregator.generateReport();
        }
    }
}
