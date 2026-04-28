# 🏗️ DESIGN.md — Automation Framework Architecture

---

## 📌 Overview

This framework is designed as a **scalable, enterprise-grade automation platform** supporting:

* UI Automation (Playwright)
* API Testing (REST Assured)
* BDD (Cucumber)
* Parallel Execution (TestNG)
* AI-powered Auto-Healing

👉 The architecture follows **clean separation of concerns**, ensuring:

* maintainability
* scalability
* reusability
* test stability

---

## 🧠 High-Level Architecture

```
                  ┌──────────────────────────────┐
                  │        Feature Files         │
                  │   (Cucumber - Gherkin)       │
                  └─────────────┬────────────────┘
                                │
                                ▼
                  ┌──────────────────────────────┐
                  │      Step Definitions        │
                  │   (Glue Code / Mapping)      │
                  └─────────────┬────────────────┘
                                │
                                ▼
                  ┌──────────────────────────────┐
                  │        Test Services         │
                  │  (Business Logic Layer)      │
                  └─────────────┬────────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        ▼                       ▼                       ▼
┌───────────────┐     ┌──────────────────┐     ┌──────────────────┐
│   UI Layer    │     │    API Layer     │     │   AI Auto-Heal   │
│ (Page Objects)│     │ (REST Assured)   │     │   Engine         │
└──────┬────────┘     └────────┬─────────┘     └────────┬─────────┘
       │                       │                        │
       ▼                       ▼                        ▼
┌───────────────┐     ┌──────────────────┐     ┌──────────────────┐
│ UI Actions    │     │ API Clients      │     │ Heuristics       │
│ (Reusable)    │     │ Builders         │     │ LLM Integration  │
└──────┬────────┘     └────────┬─────────┘     └────────┬─────────┘
       │                       │                        │
       ▼                       ▼                        ▼
┌──────────────────────────────────────────────────────────────┐
│                    Core Framework Layer                      │
│ DriverManager | ConfigManager | Logger | Retry | Hooks       │
└──────────────────────────────────────────────────────────────┘
                                │
                                ▼
                  ┌──────────────────────────────┐
                  │     Execution Environment    │
                  │ (Local / Docker / CI/CD)     │
                  └──────────────────────────────┘
```

---

## 🧩 Layered Architecture

### 1️⃣ Presentation Layer (BDD)

* Feature files (Gherkin)
* Business-readable scenarios
* Decoupled from implementation

---

### 2️⃣ Step Definition Layer

* Maps Gherkin steps → code
* No business logic here
* Delegates to service layer

---

### 3️⃣ Service Layer (CRITICAL)

👉 This is the **core of scalability**

* Combines UI + API flows
* Encapsulates business logic
* Reusable across tests

Example:

```
userService.loginUser()
```

---

### 4️⃣ UI Layer

* Page Object Model (POM)
* Component-based design
* No test logic inside pages

---

### 5️⃣ API Layer

* REST Assured clients
* Request builders
* Response validators

---

### 6️⃣ AI Auto-Heal Engine (USP 🔥)

Handles broken locators intelligently.

#### Flow:

```
Step Execution
     ↓
Try Original Locator
     ↓ (fail)
Apply Heuristics
     ↓ (fail)
Check Cached Locator
     ↓ (fail)
Call LLM (OpenAI/Gemini)
     ↓
Store Working Locator
     ↓
Continue Execution
```

#### Components:

* `engine/` → orchestration
* `heuristics/` → fallback strategies
* `llm/` → AI integration
* `cache/` → locator memory

---

### 7️⃣ Core Framework Layer

Provides reusable infrastructure:

* DriverManager (ThreadLocal)
* ConfigManager
* Logger
* Retry mechanism
* Hooks (setup/teardown)

---

## ⚡ Execution Flow

```
1. Test Runner starts
2. Feature file is parsed
3. Step Definition is triggered
4. Service Layer executes logic
5. UI/API layer performs actions
6. AI Auto-Heal resolves failures (if any)
7. Results logged + reported
```

---

## 🔁 Parallel Execution Design

* Thread-safe driver using `ThreadLocal`
* Independent browser sessions
* Parallel scenarios via TestNG

👉 Ensures:

* faster execution
* no test interference

---

## 📊 Reporting & Observability

* Allure Reports
* Extent Reports
* Logs (structured)
* Screenshots on failure
* AI healing logs

---

## 🐳 Execution Environments

| Environment | Usage                |
| ----------- | -------------------- |
| Local       | Development          |
| Docker      | Consistent execution |
| CI/CD       | Automated pipelines  |

---

## 🔐 Configuration Strategy

* External config files (`.properties`)
* Environment-based switching (dev/qa/prod)
* Secrets via `.env`

---

## 🧠 Design Principles

* Separation of concerns
* Reusability over duplication
* Fail-safe execution (auto-heal)
* Observability (logs + reports)
* Scalability (parallel + CI/CD)

---

## 🚀 Future Enhancements

* Visual regression testing
* Contract testing (API)
* AI-based test generation
* Cloud execution (BrowserStack / LambdaTest)

---

## 🏆 Summary

This framework is designed to:

* Reduce maintenance cost
* Improve execution reliability
* Scale across teams and environments
* Enable intelligent automation using AI

👉 Built with **5+ years SDET / Architect mindset**
