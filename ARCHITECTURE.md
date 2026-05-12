# Framework Architecture

## Framework Design
The framework strictly adheres to ISO/IEC 29119 standards. It ensures total separation of concerns between test execution, test configuration, and test data management.

## Project Folder Structure
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

## Logging Architecture & Traceability
To comply with ISO/IEC 29119 standards for test documentation and traceability, the framework utilizes a robust, leveled logging strategy aligned with MNC best practices via **Logback** and **SLF4J**:
- **Log Routing**: Logs are actively split into isolated files (`automation.log`, `failures.log`, `telemetry.log`, `api.log`) without code-level intervention.
- **MDC (Mapped Diagnostic Context)**: Ensures 100% thread-safe parallel execution logging. Every log entry inherently captures `[Thread]`, `[TraceId]`, `[Test]`, `[Env]`, and `[Browser]`.
- **INFO**: For business-level actions and scenario progress.
- **DEBUG**: For detailed technical operations (DOM interaction, JSON payloads).
- **WARN**: For auto-healing events, retry mechanisms, and non-fatal anomalies.
- **ERROR**: For assertion failures, timeouts, and unrecoverable exceptions.
Logs are piped dynamically to both standard output (console) and persistent test evidence artifacts (Allure/JSON).

## Page Object Model (POM) Structure
We enforce strict encapsulation of UI locators and actions. 
- **No Assertions in Pages**: Assertions belong in the step definitions.
- **Encapsulated Locators**: Locators are private and accessed only through public action methods (e.g., `login()`, `addToCart()`).

## Driver Lifecycle & Thread Safety
Browser instances are managed by `PlaywrightFactory`. We use `ThreadLocal` storage to ensure completely thread-safe parallel execution when running tests via TestNG.

## Hooks Strategy
Playwright contexts and background actions are cleanly separated via Cucumber hooks.
- `@Before` / `@After`: Manages context initialization and cleanup (including Tracing and Evidence collection).
- Tagged Hooks (e.g., `@login`): Executes centralized prerequisites to keep feature files DRY.
