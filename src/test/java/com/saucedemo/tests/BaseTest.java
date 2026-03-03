package com.saucedemo.tests;

import com.saucedemo.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/**
 * Base test class — handles driver lifecycle for all test classes.
 *
 * @Parameters("browser") receives browser name from testng.xml
 * which enables parallel execution across Chrome & Firefox.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser) {
        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}