package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Checkout Step Two - Order Summary / Review page.
 */
public class CheckoutStepTwoPage extends BasePage {

    private final By itemNames    = By.className("inventory_item_name");
    private final By itemTotal    = By.className("summary_subtotal_label");
    private final By taxLabel     = By.className("summary_tax_label");
    private final By totalLabel   = By.className("summary_total_label");
    private final By finishBtn    = By.id("finish");
    private final By cancelBtn    = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getSubtotal() {
        return getText(itemTotal);
    }

    public String getTax() {
        return getText(taxLabel);
    }

    public String getTotal() {
        return getText(totalLabel);
    }

    public CheckoutCompletePage clickFinish() {
        click(finishBtn);
        return new CheckoutCompletePage(driver);
    }

    public CartPage cancel() {
        click(cancelBtn);
        return new CartPage(driver);
    }
}