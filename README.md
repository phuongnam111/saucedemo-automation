====================================================================
  PROJECT STRUCTURE — saucedemo-automation
====================================================================

saucedemo-automation/                         ← Root project folder
│
│── pom.xml                                   ← Maven config (dependencies)
│── testng.xml                                ← TestNG config (parallel browsers)
│── README.md                                 ← Project documentation
│
├── .github/
│   └── workflows/
│       └── test.yml                          ← GitHub Actions CI pipeline
│
├── screenshots/                              ← Auto-created on test failure
│
└── src/
    ├── main/java/com/saucedemo/
    │   │
    │   ├── pages/                            ← PAGE OBJECT MODEL classes
    │   │   ├── BasePage.java                 ← Base class (wait, click, type)
    │   │   ├── LoginPage.java                ← Login page actions & locators
    │   │   ├── InventoryPage.java            ← Product listing page
    │   │   ├── CartPage.java                 ← Shopping cart page
    │   │   ├── CheckoutStepOnePage.java      ← Checkout form (name, zip)
    │   │   ├── CheckoutStepTwoPage.java      ← Order review / summary
    │   │   └── CheckoutCompletePage.java     ← Order confirmation
    │   │
    │   └── utils/                            ← UTILITY classes
    │       ├── DriverFactory.java            ← Browser init (ThreadLocal)
    │       ├── ScreenshotUtil.java           ← Capture screenshot on failure
    │       └── TestListener.java             ← TestNG listener (auto screenshot)
    │
    └── test/java/com/saucedemo/tests/       ← TEST classes
        ├── BaseTest.java                     ← Setup/teardown (BeforeMethod)
        ├── LoginTest.java                    ← 13 login test cases
        └── CartCheckoutTest.java             ← 12 cart & checkout test cases


# SauceDemo Automation Test Suite

## Overview
Automated test suite for [SauceDemo](https://www.saucedemo.com/) using 
**Selenium WebDriver + Java + TestNG**, with parallel cross-browser 
execution and GitHub Actions CI integration.

## Tech Stack
| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Test Framework | TestNG 7.9 |
| Browser Automation | Selenium WebDriver 4.18 |
| Driver Management | WebDriverManager 5.7 |
| Build Tool | Maven |
| CI/CD | GitHub Actions |
| Browsers | Chrome + Firefox (parallel) |

## Project Structure
```
├── pages/          # Page Object Model classes
├── utils/          # DriverFactory, ScreenshotUtil, TestListener
├── tests/          # Test classes (LoginTest, CartCheckoutTest)
├── deliverables/   # E-Wallet test suite (Problem 2): test cases, exploratory, UX
├── testng.xml      # Parallel execution config
└── pom.xml         # Maven dependencies
```

### Deliverables (E-Wallet — Problem 2)
The `deliverables/` folder contains design test suite artifacts for the E-Wallet scenario:
- **EWallet_Test_Cases.md** — Test cases (Registration, Send Money, Regression) in maintainable format
- **EWallet_Test_Cases.csv** — Same test cases for Excel/Google Sheets
- **EWallet_Exploratory_And_UX.md** — Exploratory scenarios (Wallet Top-up) and UI/UX feedback
- **README** — Mapping to assignment requirements and usage

## Test Coverage
### Login Tests (13 cases)
- Successful login with standard_user
- Locked out user handling
- Invalid username / password
- Empty credentials (both, username only, password only)
- Performance glitch user
- SQL Injection & XSS in username
- Direct URL access without auth
- Username with spaces
- Login → Logout → Verify redirect

### Cart & Checkout Tests (12 cases)
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

## Running Tests

### Local execution:
```bash
# Run all tests (Chrome + Firefox parallel)
mvn clean test

# Run specific test class
mvn test -Dtest=LoginTest

# Run with specific browser only
mvn test -Dbrowser=chrome
```

### CI (GitHub Actions):
Tests run automatically on push/PR to main branch.
Screenshots captured on failure are uploaded as artifacts.

## Design Decisions
- **Page Object Model**: Each page is a class with locators and 
  actions encapsulated. Changes to UI only require updates in 
  one place.
- **ThreadLocal WebDriver**: Enables safe parallel execution — 
  each thread gets its own browser instance.
- **Auto Screenshot on Failure**: TestListener captures screenshots 
  automatically without extra code in test methods.
- **Headless Mode**: Default for CI. Remove --headless flags 
  for local debugging.