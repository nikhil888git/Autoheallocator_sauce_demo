# AI Development Guide: Playwright BDD Project

## Overview
This project is an automated testing framework built using **Playwright**, **Cucumber BDD**, and **Java 11**, executing via **TestNG**. 
It features a highly robust, enterprise-grade **AutoHeal Engine** that intercepts locator failures. It cascades through structural heuristics and LLM inference models (Google Gemini 2.0 / OpenAI / DeepSeek) to self-repair broken UI elements sequentially. The framework also encompasses strict ISO 29119 test traceability.

## Tech Stack
- **Language**: Java 11
- **Browser Automation**: Microsoft Playwright for Java
- **Test Runner**: TestNG
- **Behavior-Driven Development**: Cucumber
- **Evidence Reporting**: Allure Reports & ISO 29119 Trace Tracking
- **AI Integration**: Custom encapsulated API hooks resolving to OpenAI / OpenRouter endpoints.

## Directory Structure
```text
Playwright-bdd-asl/
├── pom.xml                                   # Maven configuration and dependencies
├── src/
│   ├── test/
│   │   ├── java/com/demo/
│   │   │   ├── base/                         # BaseTest.java (TestNG logic & Evidence hooking)
│   │   │   ├── pages/                        # Page Object Models (e.g., LoginPage, HomePage)
│   │   │   ├── stepdefs/                     # Cucumber Step Definitions wrapper interfaces
│   │   │   ├── hooks/                        # Cucumber Hooks (BeforeAll Engine Lifecycle configs)
│   │   │   ├── runner/                       # Cucumber TestNG parallel/sequential runners
│   │   │   └── utils/                        
│   │   │       ├── PlaywrightFactory.java    # Global Browser singleton decoupled from Contexts
│   │   │       ├── reporting/                # Statutory reporting tools (TestEvidenceManager)
│   │   │       └── autoheal/                 # Core Healing Framework Root
│   │   │           ├── cache/                # Dual Storage cache handlers (AI vs Heuristics)
│   │   │           ├── engine/               # Arbitration nodes (DecisionEngine, AIHealingHook, Fallback)
│   │   │           ├── models/               # Schemas & ISO mappings (ExecutionJson, HealResult)
│   │   │           └── reporting/            # Internal trace aggregators and trace loggers
│   │   └── resources/
│   │       ├── features/                     # Cucumber .feature BDD files
│   │       ├── ai_cache_master.json          # Persisted ML-heuristic mappings
│   │       ├── heur_cache_master.json        # Persisted Structural heuristic mappings
│   │       └── autoheal.properties           # Framework routing and threshold API logic
```

## AI Auto-Heal & Arbitration Architecture (v2.0)
1. **Interception**: When a Locator encounters a hard fail threshold timeout, execution maps back to the `AutoHealManager`. 
2. **Duplication/Spam Control**: The engine extracts a `ResolutionCacheKey` to evaluate if the module was formally healed in the immediate UI sequence, applying silent execution boundaries to prevent console/trace reporting spam.
3. **Arbitration Rules (`AutoHealDecisionEngine`)**:
   - Tests bounds against a hard limit per-element (`MAX_HEAL_ATTEMPTS`).
   - Looks locally inside the `AutoHealCacheManager` (`ai`/`heur` discrete caches).
   - Engages `HeuristicFallbackEngine` structurally to apply rule-based heuristics returning confident metrics (scores ~1.0).
   - If heuristics exhaust, escalates strictly to `AIHealingHook`. The hook utilizes dynamic `.innerHTML` regex parsing to extract strict byte properties minimizing OOM browser leaks. If inference yields limits below `confidenceThreshold`, it throws execution back.
4. **ISO 29119 Output (`AutoHealTraceAggregator`)**: Outputs trace artifacts to `target/execution_evidence.json` marking `strategy`, `confidence_score` and exact failure components globally.

## Common AI Developer Instructions
If you are an AI assistant iterating on this project, adhere strictly to the following standards:

1. **Browser Lifecycles (CRITICAL)**: **DO NOT** execute `Playwright.create()` inside loop cycles or per-scenario closures to avoid Chromium Out-Of-Memory (OOM) leaks. We bind `PlaywrightFactory.initGlobalBrowser()` exclusively onto `@BeforeAll` static components and only spin up contexts per Scenario.
2. **Stateless Evidence Reporting**: When handling exceptions or native failures (`ITestResult`/`Scenario.isFailed()`), invoke `TestEvidenceManager.captureFailureEvidence(...)` to parse trace logs, URLs, JVM stack traces, and localized view-buffers entirely in-memory natively mapped into Allure byte-streams. Do not persist `.zip` archives or manual logs on `java.io.Files` paths avoiding I/O bottleneck freezes natively. Always attempt to point capturing interfaces to active internal browser windows (like Popups).
3. **Smart Element Binding**: Leverage `.find()` mappings uniformly onto the `SmartLocator` wrapper object rather than standard deep `.locator()` trees so the arbitration loop can safely trigger fallback endpoints.
4. **Isolate Component Packages**: Always decouple schema data logic from execution bindings within `com.demo.utils.autoheal`. Avoid pushing raw System interactions to Page Objects directly.

## Native Test Artifacts
- **Allure Viewers**: All native metrics bind safely inside `target/allure-results/`. Execute `mvn allure:serve` upon suite pipeline finishing gracefully to review UI execution layers.  
- **Playwright Execution Traces**: Trace bundles (`.zip`) dump safely into `target/playwright-traces/`. Automatically captured asynchronously upon native failures.
