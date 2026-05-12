# Playwright BDD Automation Framework with AI Auto-Heal

## 📌 Overview
This project is a modern, enterprise-grade test automation framework built using:

- **Playwright (Java)** – Fast and reliable browser automation
- **Cucumber BDD** – Business-readable test scenarios
- **TestNG** – Parallel execution and orchestration

👉 **The Unique Selling Proposition (USP):**
The framework integrates an **AI-powered Auto-Heal engine** that intelligently recovers broken locators using advanced LLMs such as:
- Google Gemini
- OpenAI
- DeepSeek

It follows a **heuristic-first → AI fallback strategy**, ensuring:
- Minimal test maintenance
- Reduced AI token costs
- Significantly faster execution times

## 🧠 Key Features
✔️ **BDD-based automation** (Clean Gherkin scenarios)  
✔️ **AI-powered self-healing locators** 🔥  
✔️ **Page Object Model (POM)** architecture  
✔️ **Parallel execution** with TestNG (Thread-safe)  
✔️ **Cross-browser support** via Playwright  
✔️ **Heuristic + AI fallback strategy**  
✔️ **Scalable and modular** enterprise design  

## 🏗️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 11+ |
| **Automation** | Playwright for Java |
| **BDD Framework** | Cucumber |
| **Test Runner** | TestNG |
| **API / AI Calls** | REST Assured / Native HTTP Client |
| **AI Integration** | OpenAI / Gemini / DeepSeek |
| **Reporting** | Allure & ISO-Trace (Execution Evidence) |

## 📂 Project Structure
```text
Playwright-bdd-asl/
├── pom.xml
├── src/
│   ├── main/java/com/demo/
│   │   ├── hooks/       # Setup, teardown, tagged hooks
│   │   ├── pages/       # Page Objects (POM)
│   │   ├── utils/       # ConfigReader, CsvReader, PlaywrightFactory
│   │   └── utils/autoheal/ # AI Auto-heal engine (USP)
│
│   ├── test/java/com/demo/stepdefs/
│   │   ├── ui/          # UI-focused steps
│   │   └── api/         # API-focused steps (Optional)
│
│   ├── test/resources/
│   │   ├── testdata/    # CSV/JSON data-driven files
│   │   ├── config.properties
│   │   └── features/    # BDD Feature files
│
├── target/              # Reports, Traces, AI caches
├── .github/workflows/   # CI/CD Pipelines
└── .env                 # Local Environment Secrets (Git-ignored)
```

## ⚙️ Setup & Installation

**1. Clone the repository**
```bash
git clone <your-repo-url>
cd Playwright-bdd-asl
```

**2. Configure Environment**
Copy `.env.example` to `.env` and add your secure credentials.
```properties
ENV=QA
TEST_USER=standard_user
TEST_PASSWORD=secret_sauce
API_KEY=sk-your-ai-api-key
```

**3. Install dependencies**
```bash
mvn clean install -DskipTests
```

**4. Install Playwright browsers**
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

## ▶️ Test Execution

**Run all tests (Default):**
```bash
mvn clean test
```

**Run with TestNG suite file:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

**Run specific BDD scenarios by tags:**
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

## ⚡ Parallel Execution
- **Enabled via TestNG**: Utilizes `dataproviderthreadcount` in `maven-surefire-plugin`.
- **Thread-safe driver handling**: Managed entirely via `ThreadLocal` in `PlaywrightFactory`.
- **Cross-browser multi-threading**: Zero state contamination between parallel threads.

## 🐳 Docker Execution
```bash
docker build -t playwright-framework .
docker run playwright-framework
```

## ⚙️ CI/CD Integration
GitHub Actions pipeline automates the testing lifecycle:
1. Build project
2. Install Playwright browsers
3. Execute parallel tests
4. Generate Allure reports
5. Upload ISO execution evidence and trace artifacts

*Location:* `.github/workflows/ci.yml`

## 🧠 AI Auto-Heal Strategy
Our robust, multi-tiered self-healing engine guarantees execution continuity:
1. **Try original locator** (Fastest)
2. **Apply heuristic matching** (Regex / DOM tree structural checks)
3. **Use cached fallback** (Zero-cost instantaneous fix)
4. **Call LLM** (If required, dynamically patches the locator)
5. **Store working locator** (Updates dual-cache for future runs)

👉 **This radically reduces:**
- Flaky test failures
- Daily maintenance cost
- Impact of major UI refactoring

## 📊 Reporting
- **Allure Reports**: Rich visualization of BDD execution.
- **ISO-Trace JSON**: ISO 29119 compliant execution evidence output.
- **Playwright Trace Viewer**: In-depth DOM/Network traces dynamically attached upon failure.

## 🔐 Configuration Management
Powered by `ConfigReader.java` utilizing a strict **Single Source of Truth**:
- Supports multiple environments dynamically (`QA_BASE_URL`, `UAT_BASE_URL`).
- `.env` strictly used for local secrets (preventing credentials in VCS).
- `config.properties` strictly used for framework defaults.

## 🧩 Design Principles
- **Separation of Concerns**: UI, Step Definitions, and Data are 100% isolated.
- **Reusable Components**: Utilities are heavily abstracted.
- **Thread-safe Execution**: Designed for enterprise scaling.

## ⭐ Why This Framework Stands Out
- **AI-powered Auto-Healing** 🔥
- **Hybrid UI + Data-driven testing**
- **Designed for real-world MNC scalability**
- **Zero hardcoded waits** (Leverages Playwright auto-wait capabilities)
