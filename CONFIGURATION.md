# Configuration Management

## Single Source of Truth Strategy
The framework completely decouples credentials and environments using a tiered property system.

### `.env` File (Secrets & Environments)
Holds all environment variables and secrets. **This file must never be committed to Version Control.**

```properties
ENV=QA
QA_BASE_URL=https://qa.saucedemo.com
TEST_USER=standard_user
TEST_PASSWORD=secret_sauce
```

### `config.properties` (Framework Defaults)
Holds static framework defaults that are safe to commit.

```properties
browser=chromium
timeout=30000
```

## ConfigReader Priority Resolution
`ConfigReader.java` dynamically resolves configuration following strict hierarchy:
1. `System.getenv()` (Perfect for CI/CD pipeline injection)
2. `.env` (Perfect for local development)
3. `config.properties` (Fallback defaults)

## Dynamic Environment Switching
You can switch test environments seamlessly by changing the `ENV` variable in your `.env` file (e.g., `ENV=UAT`). The framework automatically builds the target URL by appending `_BASE_URL` (e.g., `UAT_BASE_URL`).

## Logging Configuration (Logback & MDC)
To maintain ISO 29119 compliance and MNC-level traceability, logging is dynamically managed via `src/test/resources/logback-test.xml`.

- **Log Routing**: Logs are piped automatically into isolated files (`automation.log`, `failures.log`, `telemetry.log`, `api.log`).
- **MDC Isolation**: Context fields (`traceId`, `scenario`, `env`, `browser`) are injected during the `@Before` hook, preventing thread-bleeding during parallel test execution.

```properties
# Example logging configuration in config.properties or .env
LOG_LEVEL=INFO # Change to DEBUG for deep tracing
```

- **DEBUG**: Deep technical tracing (AI payloads, DOM interactions).
- **INFO**: Standard execution milestones.
- **WARN**: Recoverable issues (e.g., Auto-Heal fallbacks).
- **ERROR**: Test failures and critical exceptions.
