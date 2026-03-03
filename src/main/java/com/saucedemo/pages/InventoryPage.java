package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage extends BasePage {

    // Locators
    private final By pageTitle       = By.className("title");
    private final By inventoryItems  = By.className("inventory_item");
    private final By cartBadge       = By.className("shopping_cart_badge");
    private final By cartLink        = By.className("shopping_cart_link");
    private final By burgerMenuBtn   = By.id("react-burger-menu-btn");
    private final By logoutLink      = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnInventoryPage() {
        return getText(pageTitle).equals("Products");
    }

    /**
     * Add item to cart by index (0-based).
     * Clicks the "Add to cart" button on the nth inventory item.
     */
    public InventoryPage addItemToCart(int index) {
        List<WebElement> items = driver.findElements(inventoryItems);
        if (index < items.size()) {
            WebElement addBtn = items.get(index)
                    .findElement(By.cssSelector("button[id^='add-to-cart']"));
            addBtn.click();
        }
        return this;
    }

    /**
     * Add item to cart by exact product name.
     */
    public InventoryPage addItemByName(String productName) {
        String id = productName.toLowerCase()
                .replace(" ", "-");
        By addBtn = By.id("add-to-cart-" + id);
        click(addBtn);
        return this;
    }

    /**
     * Get the cart badge count (number of items in cart).
     * Returns 0 if badge is not displayed.
     */
    public int getCartCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public String getProductName(int index) {
        List<WebElement> items = driver.findElements(inventoryItems);
        return items.get(index)
                .findElement(By.className("inventory_item_name"))
                .getText();
    }

    public int getInventoryItemCount() {
        return driver.findElements(inventoryItems).size();
    }

    public void logout() {
        click(burgerMenuBtn);
        click(logoutLink);
    }
}