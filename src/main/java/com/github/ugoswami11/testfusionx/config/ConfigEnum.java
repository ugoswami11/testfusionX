package com.github.ugoswami11.testfusionx.config;

public enum ConfigEnum {

    ENV("env", "qa"),
    BROWSER("browser", "chrome"),
    BASE_URL("base.url", ""),
    HEADLESS("headless", "false"),
    IMPLICIT_WAIT("implicit.wait", "10"),
    EXPLICIT_WAIT("explicit.wait", "20"),
    PAGE_LOAD_TIMEOUT("page.load.timeout", "20"),
    REMOTE_EXECUTION("remote.execution", "false");

    private final String key;
    private final String defaultValue;

    ConfigEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    // Fetch string value from ConfigProvider
    public String value() {
        return ConfigProvider.getProperty(key, defaultValue);
    }

    // Typed conversions
    public boolean asBoolean() {
        return Boolean.parseBoolean(value());
    }

    public int asInt() {
        try {
            return Integer.parseInt(value());
        } catch (NumberFormatException e) {
            return Integer.parseInt(defaultValue);
        }
    }
}