package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Checkout Step One - Customer Information form.
 */
public class CheckoutStepOnePage extends BasePage {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField  = By.id("last-name");
    private final By postalField    = By.id("postal-code");
    private final By continueBtn    = By.id("continue");
    private final By cancelBtn      = By.id("cancel");
    private final By errorMessage   = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepOnePage fillInfo(String firstName, String lastName, String zip) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalField, zip);
        return this;
    }

    public CheckoutStepTwoPage clickContinue() {
        click(continueBtn);
        return new CheckoutStepTwoPage(driver);
    }

    /**
     * Submit empty form expecting validation error
     */
    public CheckoutStepOnePage clickContinueExpectingError() {
        click(continueBtn);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public CartPage cancel() {
        click(cancelBtn);
        return new CartPage(driver);
    }
}