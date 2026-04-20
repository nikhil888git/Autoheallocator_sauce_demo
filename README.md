# 🚀 Playwright BDD Automation Framework with AI Auto-Heal

## 📌 Overview

This project is a **modern automation testing framework** built using:

- **Playwright (Java)** for fast and reliable browser automation  
- **Cucumber BDD** for readable business-driven test scenarios  
- **TestNG** for execution and parallel test orchestration  

👉The framework includes an AI-powered Auto-Heal mechanism that intelligently recovers broken locators using LLMs such as:

Google Gemini
OpenAI
DeepSeek

**This approach ensures minimal test maintenance, even with frequent UI changes, by leveraging a heuristic fallback strategy that reduces reliance on expensive AI calls—saving both time and cost in most scenarios.**

---

## 🧠 Key Features

- ✔️ BDD-based automation (business-readable scenarios)  
- ✔️ AI-powered self-healing locators  
- ✔️ Page Object Model (POM) architecture  
- ✔️ Parallel execution with TestNG  
- ✔️ Allure reporting integration  
- ✔️ Heuristic + AI fallback strategy  
- ✔️ Clean, scalable, and MNC-level design  

---

## 🏗️ Tech Stack

| Layer            | Technology                                  |
|------------------|---------------------------------------------|
| Language         | Java 11                                     |
| Automation       | Microsoft Playwright                        |
| BDD              | Cucumber                                    |
| Test Runner      | TestNG                                      |
| API / AI Calls   | REST Assured                                |
| AI Integration   | Google GenAI / OpenAI / DeepSeek            |
| Reporting        | Allure Reports / Extent Reports             |

---

## 📁 Project Structure

```text
Playwright-bdd-asl/
├── pom.xml
├── src/
│   ├── test/
│   │   ├── java/com/demo/
│   │   │   ├── base/
│   │   │   │   └── BaseTest.java
│   │   │   ├── pages/
│   │   │   ├── stepdefs/
│   │   │   ├── runner/
│   │   │   └── utils/
│   │   │       ├── PlaywrightFactory.java
│   │   │       └── autoheal/
│   │   │           ├── AutoHealManager.java
│   │   │           ├── AutoHealCache.java
│   │   │           └── AutoHealConfig.java
│   │   └── resources/
│   │       ├── features/
│   │       └── autoheal.properties
