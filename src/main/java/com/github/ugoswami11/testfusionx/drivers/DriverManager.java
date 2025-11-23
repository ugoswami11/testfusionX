package com.github.ugoswami11.testfusionx.drivers;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    public static void setDriver(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver instance cannot be null.");
        }
        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized for this thread. " +
                            "Call DriverManager.setDriver(...) in your @Before hook."
            );
        }
        return driver;
    }

    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }

    public static void unload() {
        driverThreadLocal.remove();
    }
}
