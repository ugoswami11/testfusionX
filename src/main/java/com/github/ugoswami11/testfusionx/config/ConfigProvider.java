package com.github.ugoswami11.testfusionx.config;

import org.testng.ITestContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
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

    /** Single source of truth for all config values */
    private static final Properties properties = new Properties();

    /** Tracks whether config has been initialized */
    private static boolean initialized = false;

    private ConfigProvider() {
        // Prevent instantiation
    }

    // ---------------------------------------------------------------------------------------
    // INITIALIZATION (Lazy Loading)
    // ---------------------------------------------------------------------------------------

    public static synchronized void init(ITestContext testContext) {
        if (initialized) return;

        // 1. Load base config.properties
        loadBaseConfig();

        // 2. Apply System property overrides BEFORE env load
        overrideWithSystemProperties();

        // 3. Apply TestNG parameters BEFORE env load
        overrideWithTestNGParameters(testContext);

        // 4. Now load env.<env>.properties (correct env)
        loadEnvironmentConfig();

        initialized = true;
    }

    private static void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException(
                    "ConfigProvider.init(context) must be called before accessing configuration."
            );
        }
    }

    // ---------------------------------------------------------------------------------------
    // LOADERS
    // ---------------------------------------------------------------------------------------

    private static void loadBaseConfig() {
        try (InputStream input = getResource(DEFAULT_CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath.");
            }

            properties.load(input);
            System.out.println("✓ Loaded config.properties");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private static void loadEnvironmentConfig() {
        String env = properties.getProperty("env", "qa").toLowerCase();
        String envFileName = String.format(ENVIRONMENT_FILE_FORMAT, env);

        try (InputStream input = getResource(envFileName)) {

            if (input == null) {
                throw new RuntimeException("Environment properties file not found: " + envFileName);
            }

            properties.load(input);
            System.out.println("✓ Loaded " + envFileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment properties: " + envFileName, e);
        }
    }

    private static InputStream getResource(String file) {
        return ConfigProvider.class.getClassLoader().getResourceAsStream(file);
    }

    // ---------------------------------------------------------------------------------------
    // OVERRIDES
    // ---------------------------------------------------------------------------------------

    /** Only override keys that match our config properties — prevents JVM pollution */
    public static void overrideWithSystemProperties() {
        Properties systemProps = System.getProperties();

        for (String key : systemProps.stringPropertyNames()) {
            // Override ONLY if key exists in loaded config
            if (properties.containsKey(key)) {
                properties.setProperty(key, systemProps.getProperty(key));
            }
        }
    }

    public static void overrideWithTestNGParameters(ITestContext context) {
        if (context == null) return;

        Map<String, String> params = context.getSuite().getXmlSuite().getAllParameters();

        params.forEach((key, value) -> {
            if (properties.containsKey(key)) {
                properties.setProperty(key, value);
            }
        });
    }

    // ---------------------------------------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------------------------------------

    public static String getProperty(String key, String defaultValue) {
        checkInitialized();
        return properties.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        checkInitialized();
        return properties.getProperty(key);
    }

    public static Properties getAllProperties() {
        checkInitialized();
        return properties;
    }

    // ---------------------------------------------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------------------------------------------

    public static void validateRequiredProperties(String... keys) {
        checkInitialized();

        for (String key : keys) {
            if (!properties.containsKey(key)) {
                throw new RuntimeException("Missing required property: " + key);
            }
        }
    }
}
