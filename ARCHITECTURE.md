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
