package com.demo.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@CucumberOptions(features = "src/test/resources/features", glue = { "com.demo.stepdefs", "com.demo.hooks" }, plugin = {
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" })
@Listeners(com.demo.utils.autoheal.AutoHealSuiteListener.class)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}