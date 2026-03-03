package com.saucedemo.tests;

import com.saucedemo.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Cart & Checkout Test Suite — covers add to cart,
 * cart management, checkout flow, and edge cases.
 */
public class CartCheckoutTest extends BaseTest {

    private static final String USER = "standard_user";
    private static final String PASS = "secret_sauce";

    private InventoryPage inventoryPage;

    /**
     * Every test starts logged in on the inventory page.
     */
    @Override
    @BeforeMethod
    public void setUp(String browser) {
        super.setUp(browser);
        LoginPage loginPage = new LoginPage(driver);
        inventoryPage = loginPage.open().loginAs(USER, PASS);
    }

    // ========== ADD TO CART TESTS ==========

    @Test(description = "Add single item to cart and verify badge count")
    public void testAddSingleItem() {
        inventoryPage.addItemToCart(0);

        Assert.assertEquals(inventoryPage.getCartCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test(description = "Add multiple items to cart")
    public void testAddMultipleItems() {
        inventoryPage.addItemToCart(0)
                     .addItemToCart(1)
                     .addItemToCart(2);

        Assert.assertEquals(inventoryPage.getCartCount(), 3,
                "Cart badge should show 3 after adding three items");
    }

    @Test(description = "Add item by name - Sauce Labs Backpack")
    public void testAddItemByName() {
        inventoryPage.addItemByName("sauce-labs-backpack");

        Assert.assertEquals(inventoryPage.getCartCount(), 1,
                "Cart should have 1 item after adding Backpack");
    }

    @Test(description = "Verify added items appear in cart with correct names")
    public void testCartContainsCorrectItems() {
        String product1 = inventoryPage.getProductName(0);
        String product2 = inventoryPage.getProductName(1);

        CartPage cart = inventoryPage
                .addItemToCart(0)
                .addItemToCart(1)
                .goToCart();

        Assert.assertEquals(cart.getCartItemCount(), 2,
                "Cart should contain exactly 2 items");
        Assert.assertTrue(cart.hasItem(product1),
                "Cart should contain: " + product1);
        Assert.assertTrue(cart.hasItem(product2),
                "Cart should contain: " + product2);
    }

    // ========== REMOVE FROM CART TESTS ==========

    @Test(description = "Remove item from cart and verify count decreases")
    public void testRemoveItemFromCart() {
        CartPage cart = inventoryPage
                .addItemToCart(0)
                .addItemToCart(1)
                .goToCart();

        Assert.assertEquals(cart.getCartItemCount(), 2);

        cart.removeItem(0);

        Assert.assertEquals(cart.getCartItemCount(), 1,
                "Cart should have 1 item after removing one");
    }

    // ========== FULL CHECKOUT FLOW ==========

    @Test(description = "Complete end-to-end checkout flow with single item")
    public void testCompleteCheckoutSingleItem() {
        CheckoutCompletePage completePage = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .clickCheckout()
                .fillInfo("John", "Doe", "10001")
                .clickContinue()
                .clickFinish();

        Assert.assertTrue(completePage.isOrderComplete(),
                "Order confirmation message should be displayed");
        Assert.assertEquals(completePage.getConfirmationHeader(),
                "Thank you for your order!",
                "Confirmation header should match expected text");
    }

    @Test(description = "Complete checkout with multiple items")
    public void testCompleteCheckoutMultipleItems() {
        String item1 = inventoryPage.getProductName(0);
        String item2 = inventoryPage.getProductName(2);

        CheckoutStepTwoPage reviewPage = inventoryPage
                .addItemToCart(0)
                .addItemToCart(2)
                .goToCart()
                .clickCheckout()
                .fillInfo("Jane", "Smith", "90210")
                .clickContinue();

        // Verify items in order summary
        Assert.assertTrue(reviewPage.getItemNames().contains(item1),
                "Order summary should contain: " + item1);
        Assert.assertTrue(reviewPage.getItemNames().contains(item2),
                "Order summary should contain: " + item2);

        // Verify price is calculated
        Assert.assertFalse(reviewPage.getTotal().isEmpty(),
                "Total price should be displayed");

        // Complete order
        CheckoutCompletePage completePage = reviewPage.clickFinish();
        Assert.assertTrue(completePage.isOrderComplete());
    }

    @Test(description = "After checkout, go back to home and verify cart is empty")
    public void testCartEmptyAfterCheckout() {
        InventoryPage returnedInventory = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .clickCheckout()
                .fillInfo("Test", "User", "12345")
                .clickContinue()
                .clickFinish()
                .backToHome();

        Assert.assertEquals(returnedInventory.getCartCount(), 0,
                "Cart should be empty after completing checkout");
    }

    // ========== CHECKOUT VALIDATION TESTS ==========

    @Test(description = "Checkout with empty customer info should show error")
    public void testCheckoutEmptyInfo() {
        CheckoutStepOnePage checkoutPage = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .clickCheckout();

        checkoutPage.clickContinueExpectingError();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error should display when submitting empty checkout form");
        Assert.assertTrue(checkoutPage.getErrorMessage()
                .contains("First Name is required"),
                "Error should indicate first name is required");
    }

    @Test(description = "Checkout missing last name should show error")
    public void testCheckoutMissingLastName() {
        CheckoutStepOnePage checkoutPage = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .clickCheckout();

        checkoutPage.fillInfo("John", "", "10001")
                .clickContinueExpectingError();

        Assert.assertTrue(checkoutPage.getErrorMessage()
                .contains("Last Name is required"),
                "Error should indicate last name is required");
    }

    @Test(description = "Checkout missing postal code should show error")
    public void testCheckoutMissingPostalCode() {
        CheckoutStepOnePage checkoutPage = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .clickCheckout();

        checkoutPage.fillInfo("John", "Doe", "")
                .clickContinueExpectingError();

        Assert.assertTrue(checkoutPage.getErrorMessage()
                .contains("Postal Code is required"),
                "Error should indicate postal code is required");
    }

    // ========== NAVIGATION TESTS ==========

    @Test(description = "Cancel checkout should return to cart with items preserved")
    public void testCancelCheckoutReturnsToCart() {
        CartPage cart = inventoryPage
                .addItemToCart(0)
                .addItemToCart(1)
                .goToCart()
                .clickCheckout()
                .cancel();

        Assert.assertEquals(cart.getCartItemCount(), 2,
                "Cart items should be preserved after canceling checkout");
    }

    @Test(description = "Continue shopping from cart returns to inventory")
    public void testContinueShoppingFromCart() {
        InventoryPage returnedInventory = inventoryPage
                .addItemToCart(0)
                .goToCart()
                .continueShopping();

        Assert.assertTrue(returnedInventory.isOnInventoryPage(),
                "Should return to inventory page after 'Continue Shopping'");
    }
}