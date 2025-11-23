package com.github.ugoswami11.testfusionx.drivers;

public enum DriverType {
    CHROME,
    FIREFOX,
    EDGE;

    /**
     * Converts browser name to enum safely.
     */
    public static DriverType fromString(String browser) {
        if (browser == null || browser.isEmpty()) {
            throw new IllegalArgumentException(
                    "Browser name cannot be null or empty. Supported values: chrome, firefox, edge");
        }

        switch (browser.toLowerCase().trim()) {
            case "chrome": return CHROME;
            case "firefox": return FIREFOX;
            case "edge": return EDGE;
            default:
                throw new IllegalArgumentException(
                        "Unsupported browser: " + browser +
                                ". Supported values: chrome, firefox, edge");
        }
    }
}
