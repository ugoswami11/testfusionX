package com.github.ugoswami11.testfusionx.drivers;

import com.github.ugoswami11.testfusionx.config.ConfigEnum;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
        // Prevent instantiation
    }

    public static WebDriver createDriver() {
        DriverType type = DriverType.fromString(ConfigEnum.BROWSER.value());
        boolean headless = ConfigEnum.HEADLESS.asBoolean();

        try {
            WebDriver driver = buildDriver(type, headless);

            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(ConfigEnum.IMPLICIT_WAIT.asInt())
            );

            try {
                driver.manage().window().maximize();
            } catch (Exception ignored) {
                // Some drivers in headless cannot maximize - safe to ignore
            }

            return driver;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebDriver for browser: " + type, e);
        }
    }

    private static WebDriver buildDriver(DriverType type, boolean headless) {

        switch (type) {

            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless=new");
                return new ChromeDriver(chromeOptions);

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("-headless");
                return new FirefoxDriver(firefoxOptions);

            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + type);
        }
    }
}
