package com.demo.utils.autoheal;

import com.demo.utils.autoheal.reporting.AutoHealLogger;
import com.demo.utils.autoheal.reporting.AutoHealTraceAggregator;
import com.demo.utils.autoheal.cache.AutoHealCacheManager;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class AutoHealSuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        if (AutoHealSuiteConfig.isEnabled()) {
            AutoHealLogger.logExecution("AutoHealing is ENABLED. Core Strategies and Iso-Trace logic loaded.");
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        if (AutoHealSuiteConfig.isEnabled()) {
            AutoHealLogger.logExecution("Commencing Suite Termination Cleanup...");
            AutoHealCacheManager.flushCaches();
            AutoHealTraceAggregator.flushTraces();
        }
    }
}
