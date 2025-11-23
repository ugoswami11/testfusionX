package com.github.ugoswami11.testfusionx.drivers;

import org.openqa.selenium.WebDriver;

public final class DriverCleanup {

    private DriverCleanup() {
        // Prevent instantiation
    }

    public static void cleanup() {
        try {
            if (DriverManager.hasDriver()) {
                WebDriver driver = DriverManager.getDriver();
                try {
                    driver.quit();
                } catch (Exception e) {
                    System.err.println("⚠ Failed to quit WebDriver: " + e.getMessage());
                }
            }
        } finally {
            // Always release threadLocal reference
            DriverManager.unload();
        }
    }
}
