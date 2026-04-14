# AI Development Guide: Playwright BDD Project

## Overview
This project is an automated testing framework built using **Playwright**, **Cucumber BDD**, and **Java 11**, executing via **TestNG**. 
It includes a custom **AutoHeal** mechanism that uses AI (Google Gemini 2.0 / OpenAI / DeepSeek) to dynamically find replacement element locators when UI elements break during test execution.

## Tech Stack
- **Language**: Java 11
- **Browser Automation**: Microsoft Playwright for Java
- **Test Runner**: TestNG
- **Behavior-Driven Development**: Cucumber
- **Reporting**: Allure Reports, Extent Reports (optional backup)
- **AI Integration**: Google GenAI API (`google-genai` library), REST Assured

## Directory Structure
```text
Playwright-bdd-asl/
├── pom.xml                        # Maven configuration and dependencies
├── src/
│   ├── test/
│   │   ├── java/com/demo/
│   │   │   ├── base/              # BaseTest.java (TestNG setup/teardown mechanics)
│   │   │   ├── pages/             # Page Object Models (e.g., LoginPage, HomePage)
│   │   │   ├── stepdefs/          # Cucumber Step Definitions mappings
│   │   │   ├── runner/            # Cucumber TestNG runners
│   │   │   └── utils/             # Utilities (PlaywrightFactory, AutoHeal configurations)
│   │   │       └── autoheal/      # AI AutoHeal logic (AutoHealManager, Cache, Configuration)
│   │   └── resources/
│   │       ├── features/          # Cucumber .feature files
│   │       └── autoheal.properties# AI credentials and auto-healing settings
```

## AI Auto-Heal Architecture
- When a `Locator` fails to find an element, the `AutoHealManager` intercepts the failure.
- It first attempts to use a cached healed locator if it previously encountered this failure.
- If no cache exists, it evaluates heuristic fallbacks (`data-test` attributes).
- Finally, if heuristics fail, it extracts a miniaturized DOM snippet and queries the LLM API using configuration settings from `autoheal.properties`.

## Common AI Developer Instructions
If you are an AI assistant working on this project, adhere to the following rules:
1. **Maintain Proper Structure**: When creating new pages, strictly follow the Page Object Model (POM) pattern inside `com.demo.pages`. Do not scatter locators in step definitions.
2. **BDD Best Practices**: Keep `*.feature` files readable. Ensure Step Definitions (`com.demo.stepdefs`) are simply calling methods defined in the Page layers. 
3. **Robust Locators**: Playwright's user-facing locators (e.g., `getByRole`, `getByText`) are preferred over deep CSS/XPath unless testing specific attributes.
4. **AutoHeal Configurations**: All AI and configuration changes should occur centrally inside `src/test/resources/autoheal.properties` and the `com.demo.utils.autoheal` package.
5. **Maven Compilation**: Ensure code is compatible with Java 11, particularly focusing on resolving diamond operator (`<>`) or interface logic accordingly in Maven. Run `mvn clean test` for execution.

## Reporting
- Ensure `allure-maven` configurations are properly synced with test outputs. Allure results should map correctly onto all Cucumber parallel runs instead of only capturing subsets.
