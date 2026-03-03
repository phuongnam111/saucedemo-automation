package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Checkout Complete - Order confirmation page.
 */
public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By completeText   = By.className("complete-text");
    private final By backHomeBtn    = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationHeader() {
        return getText(completeHeader);
    }

    public String getConfirmationText() {
        return getText(completeText);
    }

    public boolean isOrderComplete() {
        return getConfirmationHeader().contains("Thank you for your order");
    }

    public InventoryPage backToHome() {
        click(backHomeBtn);
        return new InventoryPage(driver);
    }
}