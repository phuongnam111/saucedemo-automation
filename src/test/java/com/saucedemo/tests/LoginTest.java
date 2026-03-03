package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login Test Suite — covers all SauceDemo login scenarios.
 * 
 * SauceDemo provides these test users:
 *   - standard_user      → normal login
 *   - locked_out_user     → account locked
 *   - problem_user        → buggy UI behavior
 *   - performance_glitch_user → slow responses
 *   - error_user          → triggers errors
 *   - visual_user         → visual bugs
 *   Password for all: secret_sauce
 */
public class LoginTest extends BaseTest {

    private static final String VALID_USER = "standard_user";
    private static final String VALID_PASS = "secret_sauce";

    // ========== POSITIVE TESTS ==========

    @Test(description = "Login with valid standard_user credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventory = loginPage.open()
                .loginAs(VALID_USER, VALID_PASS);

        Assert.assertTrue(inventory.isOnInventoryPage(),
                "Should redirect to inventory/products page after successful login");
    }

    @Test(description = "Verify all 6 products display after login")
    public void testProductsDisplayAfterLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventory = loginPage.open()
                .loginAs(VALID_USER, VALID_PASS);

        Assert.assertEquals(inventory.getInventoryItemCount(), 6,
                "Inventory page should display exactly 6 products");
    }

    @Test(description = "Login and logout, then verify redirect to login page")
    public void testLogoutRedirectsToLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventory = loginPage.open()
                .loginAs(VALID_USER, VALID_PASS);

        inventory.logout();

        LoginPage returnedLogin = new LoginPage(driver);
        Assert.assertTrue(returnedLogin.isOnLoginPage(),
                "After logout, user should be on login page");
    }

    // ========== NEGATIVE TESTS ==========

    @Test(description = "Login with locked_out_user should show locked error")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("locked_out_user", VALID_PASS);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("locked out"),
                "Error should mention account is locked");
    }

    @Test(description = "Login with invalid username")
    public void testInvalidUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("invalid_user", VALID_PASS);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid username");
        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("Username and password do not match"),
                "Error should indicate credentials mismatch");
    }

    @Test(description = "Login with invalid password")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError(VALID_USER, "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for wrong password");
        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("Username and password do not match"),
                "Error should indicate credentials mismatch");
    }

    @Test(description = "Login with empty username and password")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("", "");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for empty fields");
        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("Username is required"),
                "Error should indicate username is required");
    }

    @Test(description = "Login with empty username only")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("", VALID_PASS);

        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("Username is required"),
                "Error should indicate username is required");
    }

    @Test(description = "Login with empty password only")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError(VALID_USER, "");

        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("Password is required"),
                "Error should indicate password is required");
    }

    // ========== EDGE CASE TESTS ==========

    @Test(description = "Login with performance_glitch_user - slow but should succeed")
    public void testPerformanceGlitchUser() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventory = loginPage.open()
                .loginAs("performance_glitch_user", VALID_PASS);

        Assert.assertTrue(inventory.isOnInventoryPage(),
                "Performance glitch user should still login successfully");
    }

    @Test(description = "Login with username containing leading/trailing spaces")
    public void testUsernameWithSpaces() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("  standard_user  ", VALID_PASS);

        // SauceDemo does NOT trim spaces — login should fail
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Username with spaces should not match any valid user");
    }

    @Test(description = "Login with SQL injection attempt in username")
    public void testSqlInjectionUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("' OR '1'='1", VALID_PASS);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "SQL injection should not bypass authentication");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page after injection attempt");
    }

    @Test(description = "Login with XSS script in username field")
    public void testXssInUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .loginExpectingError("<script>alert('xss')</script>", VALID_PASS);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "XSS payload should be rejected as invalid credentials");
    }

    @Test(description = "Access inventory URL directly without login should redirect")
    public void testDirectAccessWithoutLogin() {
        driver.get("https://www.saucedemo.com/inventory.html");

        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Direct access to inventory without login should show error");
        Assert.assertTrue(loginPage.getErrorMessage()
                .contains("You can only access"),
                "Error should indicate login is required");
    }
}