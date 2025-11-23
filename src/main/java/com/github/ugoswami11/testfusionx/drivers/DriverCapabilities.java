package com.github.ugoswami11.testfusionx.drivers;

import org.openqa.selenium.chrome.ChromeOptions;

public class DriverCapabilities {

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        return options;
    }
}
