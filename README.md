🚀 Playwright BDD Automation Framework (AI-Powered)
📌 Overview
This is a production-grade test automation framework designed using:
Playwright (Java) – fast, reliable browser automation
Cucumber BDD – readable business-driven scenarios
TestNG – execution + parallel orchestration
REST Assured – API testing layer
AI Auto-Heal Engine – self-healing locators (USP 🔥)
Docker + GitHub Actions – CI/CD ready
👉 Built with scalability, maintainability, and enterprise-grade design principles
🏆 Key Features
✅ UI Automation
Cross-browser support (Chromium, Firefox, WebKit)
Parallel execution (Thread-safe)
Page Object Model + Component-based design
✅ BDD (Behavior Driven Development)
Gherkin-based feature files
Step definitions mapped to business flows
Readable for non-technical stakeholders
✅ API Testing
REST Assured integration
Request/Response validation
JSON schema validation support
🔥 AI Auto-Heal (USP)
Heuristic-based locator recovery
Fallback locator strategy
Optional LLM integration
Self-learning cache mechanism
📊 Reporting
Allure Reports
Extent Reports
Screenshots on failure
Logs + execution traces
⚙️ CI/CD Ready
GitHub Actions pipeline
Dockerized execution
Artifact publishing
📂 Project Structure
src/
 ├── main/java/com/framework/
 │   ├── core/        # BaseTest, Hooks, Driver setup
 │   ├── pages/       # Page Object Models
 │   ├── utils/       # Wait, Config, Logger
 │   ├── ai/          # Auto-heal engine
 │   ├── api/         # REST Assured layer
 │   ├── models/      # Request/Response POJOs
 │   └── listeners/   # TestNG listeners
 ├── test/java/com/tests/
 │   ├── ui/
 │   ├── api/
 │   └── integration/
 ├── test/resources/
 │   ├── testdata/
 │   ├── config/
 │   └── features/   # BDD
reports/
logs/
docker/
.github/workflows/
⚙️ Setup & Installation
1️⃣ Clone Repository
git clone <repo-url>
cd project-name
2️⃣ Install Dependencies
mvn clean install
3️⃣ Install Playwright Browsers
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
▶️ Test Execution
Run all tests
mvn test
Run specific suite
mvn test -DsuiteXmlFile=testng.xml
Run in parallel
Configured via testng.xml
🧪 BDD Execution
mvn test -Dcucumber.options="src/test/resources/features"
🐳 Run with Docker
docker build -t test-framework .
docker run test-framework
⚡ CI/CD (GitHub Actions)
Pipeline includes:
Build project
Install Playwright browsers
Execute tests
Generate reports
Upload artifacts
Location:
.github/workflows/ci.yml
🧠 AI Auto-Heal Flow
Try original locator
Apply heuristic matching
Use fallback locator
(Optional) Query LLM
Store working locator
👉 Ensures resilient test execution
📊 Reporting
After execution:
Allure → allure-report/
Extent → extent-reports/
Logs → logs/
🔐 Configuration
Environment configs:
src/test/resources/config/
Supports:
dev / qa / prod environments
externalized credentials
.env support
🧩 Design Principles
Separation of concerns
Reusable components
Scalable architecture
Thread-safe execution
Clean reporting & logging
🤝 Contributing
Please refer to CONTRIBUTING.md
📄 Design Documentation
Refer to:
DESIGN.md
👨‍💻 Author
Built for SDET / QA Architect level automation
⭐ Why This Framework Stands Out
AI-powered auto-healing 🔥
Full-stack testing (UI + API + Integration)
CI/CD + Docker ready
Enterprise-grade structure
👉 Designed to match real-world MNC expectations
