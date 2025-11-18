package com.github.ugoswami11.testfusionx.config;

import org.testng.ITestContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigProvider (Corrected Version)
 * -------------------------------------
 * Central configuration loader for TestFusionX.
 * Loads:
 *  - Global config.properties (browser, framework settings)
 *  - Environment-specific properties (env.qa.properties, env.dev.properties, etc.)
 *
 * Does NOT use ThreadLocal. Configuration is global and read-only.
 * Parallel test execution should ONLY rely on ThreadLocal WebDriver,
 * not on per-thread configuration.
 */
public final class ConfigProvider {

    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private static final String ENVIRONMENT_FILE_FORMAT = "env.%s.properties";

    /** Properties loaded once for entire test run */
    private static final Properties properties = new Properties();

    // Static block executes once on framework startup
    static {
        loadBaseConfig();
        loadEnvironmentConfig();
    }

    private ConfigProvider() {
        // Prevent instantiation
    }

    // ---------------------------------------------------------------------------------------
    // LOADERS
    // ---------------------------------------------------------------------------------------

    /** Loads global config.properties */
    private static void loadBaseConfig() {
        try (InputStream input = ConfigProvider.class
                .getClassLoader()
                .getResourceAsStream(DEFAULT_CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath.");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /** Loads environment-specific env.<env>.properties */
    private static void loadEnvironmentConfig() {
        String env = properties.getProperty("env", "qa").toLowerCase();
        String envFileName = String.format(ENVIRONMENT_FILE_FORMAT, env);

        try (InputStream input = ConfigProvider.class
                .getClassLoader()
                .getResourceAsStream(envFileName)) {

            if (input == null) {
                throw new RuntimeException("Environment properties file not found: " + envFileName);
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error loading environment properties: " + envFileName, e);
        }
    }

    // ---------------------------------------------------------------------------------------
    // OVERRIDES
    // ---------------------------------------------------------------------------------------

    /**
     * Override configuration with TestNG XML parameters.
     * Example:
     * <suite>
     *     <parameter name="browser" value="firefox"/>
     * </suite>
     */
    public static void overrideWithTestNGParameters(ITestContext context) {
        if (context == null) return;

        context.getSuite().getXmlSuite().getAllParameters().forEach((key, value) ->
                properties.setProperty(key, value)
        );
    }

    /**
     * Override configuration with System properties.
     * Example: mvn clean test -Dbrowser=edge
     */
    public static void overrideWithSystemProperties() {
        System.getProperties().forEach((key, value) -> {
            if (key instanceof String && value instanceof String) {
                properties.setProperty((String) key, (String) value);
            }
        });
    }

    // ---------------------------------------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------------------------------------

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getEnv() {
        return properties.getProperty("env", "qa");
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome").toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public static boolean isRemoteExecution() {
        return Boolean.parseBoolean(properties.getProperty("remote.execution", "false"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "20"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "20"));
    }

    /** Useful for debugging or report generation */
    public static Properties getAllProperties() {
        return properties;
    }
}
