# 🚀 Playwright BDD Automation Framework with AI Auto-Heal

---

## 📌 Overview

This project is a **modern, enterprise-grade automation framework** built using:

* **Playwright (Java)** – fast and reliable browser automation
* **Cucumber BDD** – business-readable test scenarios
* **TestNG** – parallel execution and orchestration

👉 The framework integrates an **AI-powered Auto-Heal engine** that intelligently recovers broken locators using LLMs such as:

* Google Gemini
* OpenAI
* DeepSeek

It follows a **heuristic-first → AI fallback strategy**, ensuring:

* minimal test maintenance
* reduced AI cost
* faster execution

---

## 🧠 Key Features

✔️ BDD-based automation (Gherkin scenarios)
✔️ AI-powered self-healing locators 🔥
✔️ Page Object Model (POM) architecture
✔️ Parallel execution with TestNG
✔️ Cross-browser support (Playwright)
✔️ Heuristic + AI fallback strategy
✔️ Scalable and modular framework design

---

## 🏗️ Tech Stack

| Layer          | Technology                 |
| -------------- | -------------------------- |
| Language       | Java 11                    |
| Automation     | Playwright                 |
| BDD            | Cucumber                   |
| Test Runner    | TestNG                     |
| API / AI Calls | REST Assured               |
| AI Integration | Gemini / OpenAI / DeepSeek |
| Reporting      | Allure / Extent            |

---

## 📂 Project Structure

```id="p2h4k9"
Playwright-bdd-asl/
├── pom.xml
├── src/
│   ├── main/java/com/framework/
│   │   ├── core/        # Driver, Base, Hooks
│   │   ├── pages/       # Page Objects
│   │   ├── utils/       # Wait, Config, Logger
│   │   ├── ai/          # Auto-heal engine (USP)
│   │   ├── api/         # REST Assured layer
│   │   ├── models/      # POJOs
│   │   └── listeners/   # TestNG listeners
│
│   ├── test/java/com/tests/
│   │   ├── ui/
│   │   ├── api/
│   │   └── integration/
│
│   ├── test/resources/
│   │   ├── testdata/
│   │   ├── config/
│   │   └── features/
│
├── reports/
├── logs/
├── docker/
├── .github/workflows/
```

---

## ⚙️ Setup & Installation

### Clone the repository

```bash id="1w2x3y"
git clone <your-repo-url>
cd Playwright-bdd-asl
```

### Install dependencies

```bash id="9x8y7z"
mvn clean install
```

### Install Playwright browsers

```bash id="3k8d2p"
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

---

## ▶️ Test Execution

### Run all tests

```bash id="8h3k2l"
mvn test
```

### Run with TestNG suite

```bash id="0k2l3m"
mvn test -DsuiteXmlFile=testng.xml
```

### Run BDD scenarios

```bash id="2j3h4k"
mvn test -Dcucumber.options="src/test/resources/features"
```

---

## ⚡ Parallel Execution

* Enabled via **TestNG**
* Thread-safe driver handling
* Supports multi-browser execution

---

## 🐳 Docker Execution

```bash id="9l8k7j"
docker build -t playwright-framework .
docker run playwright-framework
```

---

## ⚙️ CI/CD Integration

GitHub Actions pipeline:

* Build project
* Install Playwright browsers
* Execute tests
* Generate reports
* Upload artifacts

Location:

```
.github/workflows/ci.yml
```

---

## 🧠 AI Auto-Heal Strategy

1. Try original locator
2. Apply heuristic matching
3. Use cached fallback
4. Call LLM (if required)
5. Store working locator

👉 This reduces:

* flaky failures
* maintenance cost
* locator breakage impact

---

## 📊 Reporting

* Allure Reports
* Extent Reports
* Logs & screenshots on failure

---

## 🔐 Configuration

Config files located at:

```
src/test/resources/config/
```

Supports:

* multiple environments (dev/qa/prod)
* externalized credentials
* `.env` integration

---

## 🧩 Design Principles

* Separation of concerns
* Reusable components
* Scalable architecture
* Thread-safe execution
* Maintainable test design

---

## ⭐ Why This Framework Stands Out

* AI-powered auto-healing 🔥
* Hybrid UI + API testing
* CI/CD + Docker ready
* Designed for real-world scalability
