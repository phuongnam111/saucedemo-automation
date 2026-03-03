package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for SauceDemo Cart page.
 * URL: https://www.saucedemo.com/cart.html
 */
public class CartPage extends BasePage {

    private final By cartItems       = By.className("cart_item");
    private final By itemNames       = By.className("inventory_item_name");
    private final By checkoutButton  = By.id("checkout");
    private final By continueShopBtn = By.id("continue-shopping");
    private final By removeButtons   = By.cssSelector("button[id^='remove']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean hasItem(String productName) {
        return getCartItemNames().contains(productName);
    }

    public CartPage removeItem(int index) {
        List<WebElement> btns = driver.findElements(removeButtons);
        if (index < btns.size()) {
            btns.get(index).click();
        }
        return this;
    }

    public CheckoutStepOnePage clickCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }

    public InventoryPage continueShopping() {
        click(continueShopBtn);
        return new InventoryPage(driver);
    }
}