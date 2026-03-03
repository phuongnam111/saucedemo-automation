# SauceDemo Automation

Automated test suite for [SauceDemo](https://www.saucedemo.com/) using **Selenium WebDriver**, **Java**, and **TestNG**, with parallel cross-browser execution and GitHub Actions CI integration.

---

## Tech Stack

| Component           | Technology                    |
|---------------------|-------------------------------|
| Language            | Java 17                       |
| Test Framework      | TestNG 7.9                    |
| Browser Automation  | Selenium WebDriver 4.18       |
| Driver Management   | WebDriverManager 5.7          |
| Build Tool          | Maven                         |
| CI/CD               | GitHub Actions                |
| Browsers            | Chrome, Firefox (parallel)    |

---

## Project Structure

```text
saucedemo-automation/
|-- pom.xml                    # Maven config & dependencies
|-- testng.xml                 # TestNG config (parallel Chrome + Firefox)
|-- README.md
|-- .github/workflows/
|   +-- test.yml               # CI pipeline (GitHub Actions)
|-- screenshots/               # Screenshots on test failure (auto-created)
+-- src/
    |-- main/java/com/saucedemo/
    |   |-- pages/             # Page Object Model
    |   |   |-- BasePage.java
    |   |   |-- LoginPage.java
    |   |   |-- InventoryPage.java
    |   |   |-- CartPage.java
    |   |   |-- CheckoutStepOnePage.java
    |   |   |-- CheckoutStepTwoPage.java
    |   |   +-- CheckoutCompletePage.java
    |   +-- utils/
    |       |-- DriverFactory.java    # Browser init (ThreadLocal)
    |       |-- ScreenshotUtil.java   # Screenshot on failure
    |       +-- TestListener.java     # TestNG listener (auto screenshot)
    +-- test/java/com/saucedemo/tests/
        |-- BaseTest.java      # Setup/teardown (BeforeMethod)
        |-- LoginTest.java     # 13 login test cases
        +-- CartCheckoutTest.java  # 12 cart & checkout test cases
```

---

## Test Coverage

### Login (13 test cases)

- Successful login with `standard_user`
- Locked out user handling (`locked_out_user`)
- Invalid username / password
- Empty credentials (both, username only, password only)
- Performance glitch user
- SQL Injection & XSS in username
- Direct URL access without login
- Username with spaces
- Login → Logout → Verify redirect

### Cart & Checkout (12 test cases)

- Add single / multiple items
- Add item by name
- Verify cart contents match added items
- Remove item from cart
- Full checkout: single item & multiple items
- Order confirmation verification
- Cart empty after checkout
- Checkout validation: empty form, missing fields
- Cancel checkout preserves cart
- Continue shopping navigation

---

## Running Tests

### Local execution

```bash
# Run all tests (Chrome + Firefox in parallel)
mvn clean test

# Run a specific test class
mvn test -Dtest=LoginTest
mvn test -Dtest=CartCheckoutTest

# Run with a single browser
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
```

### CI (GitHub Actions)

- **Trigger:** Push or pull request to `main` / `develop`, or manual run (workflow_dispatch).
- **Steps:** Set up JDK 17, Chrome, Firefox → run `mvn clean test`.
- **On failure:** Screenshots are saved to `screenshots/` and uploaded as artifact `failure-screenshots`.
- **Reports:** Artifact `test-reports` contains Surefire reports, retained for 30 days.

---

## Design Decisions

| Decision                  | Rationale |
|---------------------------|-----------|
| **Page Object Model**     | Each page is a class with locators and actions; UI changes require updates in one place. |
| **WebDriver ThreadLocal** | Each thread gets its own browser instance; safe for parallel execution. |
| **Screenshot on failure** | `TestListener` captures screenshots automatically; no extra code in test methods. |
| **Headless (CI)**         | Headless by default in CI; remove `--headless` for local debugging if needed. |

---

## Requirements

- **Java 17**
- **Maven 3.6+**
- **Chrome** and/or **Firefox** (WebDriverManager downloads the matching drivers automatically)

---

## License

This project is for learning and test automation purposes.
