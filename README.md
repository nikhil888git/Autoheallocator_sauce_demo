Playwright BDD Automation Framework with AI Auto-Heal
📌 Overview

This project is a modern automation testing framework built using:

Playwright (Java) for fast and reliable browser automation
Cucumber BDD for readable business-driven test scenarios
TestNG for execution and parallel test orchestration

👉 The framework includes an AI-powered Auto-Heal mechanism that intelligently recovers broken locators using LLMs like:

Google Gemini
OpenAI
DeepSeek

This ensures minimum test maintenance, even when UI changes frequently.

🧠 Key Features

✔️ BDD-based automation (business-readable scenarios)
✔️ AI-powered self-healing locators
✔️ Page Object Model (POM) architecture
✔️ Parallel execution with TestNG
✔️ Allure reporting integration
✔️ Heuristic + AI fallback strategy
✔️ Clean, scalable, and MNC-level design

🏗️ Tech Stack
Layer	Technology
Language	Java 11
Automation	Microsoft Playwright
BDD	Cucumber
Test Runner	TestNG
API / AI Calls	REST Assured
AI Integration	Google GenAI / OpenAI / DeepSeek
Reporting	Allure Reports / Extent Reports
📁 Project Structure
Playwright-bdd-asl/
├── pom.xml
├── src/
│   ├── test/
│   │   ├── java/com/demo/
│   │   │   ├── base/
│   │   │   │   └── BaseTest.java
│   │   │   ├── pages/
│   │   │   │   └── (Page Object Models)
│   │   │   ├── stepdefs/
│   │   │   │   └── (Cucumber Step Definitions)
│   │   │   ├── runner/
│   │   │   │   └── (TestNG Cucumber Runners)
│   │   │   └── utils/
│   │   │       ├── PlaywrightFactory.java
│   │   │       └── autoheal/
│   │   │           ├── AutoHealManager.java
│   │   │           ├── AutoHealCache.java
│   │   │           └── AutoHealConfig.java
│   │   └── resources/
│   │       ├── features/
│   │       │   └── *.feature
│   │       └── autoheal.properties
🤖 AI Auto-Heal Architecture

The framework follows a multi-layer recovery strategy when locators fail:

🔁 Flow:
❌ Locator fails
🔍 Check AutoHeal Cache
🧩 Apply heuristic fallback
data-test
aria-label
🧠 Extract DOM snippet
🤖 Send request to AI (LLM)
✅ Get healed locator
💾 Store in cache for future runs
⚙️ Configuration
📄 autoheal.properties
ai.provider=gemini
ai.api.key=YOUR_API_KEY
autoheal.enabled=true
cache.enabled=true

👉 Supported providers:

gemini
openai
deepseek
▶️ How to Run Tests
🔧 Prerequisites
Java 11+
Maven installed
Playwright browsers installed
mvn clean install
▶️ Execute Tests
mvn clean test
📊 Run with Allure Report
mvn allure:serve
🧪 BDD Example
Feature File
Feature: Login Functionality

  Scenario: Successful Login
    Given user is on login page
    When user enters valid credentials
    Then user should land on home page
🧱 Framework Design Principles
✅ Page Object Model (POM)
All locators inside pages package
No locators inside step definitions
✅ Clean BDD Layer
StepDefs → only method calls
Business logic → inside Pages
✅ Reusability
Utility classes for driver, waits, config
Centralized AI logic
🧠 AI Development Guidelines

If you're extending this framework:

🚫 Do NOT:

Add locators in step definitions
Hardcode waits
Scatter AI logic

✅ Always:

Follow POM structure
Use Playwright locators (getByRole, getByText)
Keep AI configs centralized
Reuse AutoHealManager
📊 Reporting
Allure Reports (Primary)
Extent Reports (Optional Backup)

✔️ Supports parallel execution
✔️ Captures:

Steps
Screenshots
Failures
Logs
🚀 Future Enhancements
CI/CD integration (GitHub Actions / Jenkins)
Visual testing support
Smart locator ranking via AI
Test analytics dashboard
👨‍💻 Author

Nikhil Kashiv
Automation Tester | Selenium | Playwright | API | AI Testing

⭐ Contribution

Feel free to fork, improve, and raise PRs!

🛡️ License

This project is for learning and professional use.

💡 Final Thought

“Flaky tests are expensive. Self-healing tests are the future.”
