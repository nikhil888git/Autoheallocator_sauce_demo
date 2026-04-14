package com.demo.utils;

import org.testng.annotations.AfterSuite;

public class AllureReportLauncher {

    @AfterSuite
    public void generateAllureReport() {
        try {
            System.out.println("Opening Allure Report...");

            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "allure serve target/allure-results"
            );

            builder.inheritIO();
            builder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}