package com.saucedemo.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener that automatically captures screenshots on test failure.
 * Registered in testng.xml so it applies to ALL test classes.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Get browser name from test parameter
        String browser = result.getTestContext()
                .getCurrentXmlTest()
                .getParameter("browser");
        if (browser == null) browser = "unknown";

        // Build descriptive screenshot name
        String testClass = result.getTestClass().getRealClass().getSimpleName();
        String testMethod = result.getMethod().getMethodName();
        String screenshotName = testClass + "_" + testMethod + "_" + browser;

        // Auto-capture screenshot
        ScreenshotUtil.capture(DriverFactory.getDriver(), screenshotName);

        // Log failure info
        System.err.println("=== TEST FAILED ===");
        System.err.println("Test: " + testClass + "." + testMethod);
        System.err.println("Browser: " + browser);
        System.err.println("Reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
        System.out.println("✓ PASSED: " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("⊘ SKIPPED: " + testName);
    }
}