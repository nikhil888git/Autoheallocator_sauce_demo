# Contributing Guidelines

## Coding Standards
1. **Clean Code principles**: Keep methods short and focused on a single responsibility.
2. **SOLID Design**: Apply object-oriented principles strictly across all utilities and services.
3. **No Hardcoded Waits**: Use native Playwright auto-waiting (`waitFor()`) and `assertThat()`. `Thread.sleep()` is completely banned.

## Naming Conventions
- **Classes**: PascalCase (e.g., `LoginPage`, `ConfigReader`).
- **Methods/Variables**: camelCase (e.g., `clickLoginButton`, `expectedResult`).
- **Constants**: SCREAMING_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`).
- **Feature Files**: Descriptive PascalCase or kebab-case (e.g., `CheckoutProcess.feature`).

## Do's and Don'ts

### Do
- Use Playwright assertions (`assertThat`).
- Utilize `@Before` and `@After` hooks for setup and teardown.
- Keep Step Definitions atomic and business-readable.
- Ask for architectural reviews before adding new dependencies to `pom.xml`.

### Don't
- **DO NOT** mix TestNG `assertEquals` with Playwright assertions.
- **DO NOT** place business logic inside Step Definitions.
- **DO NOT** hardcode credentials or URLs anywhere in the codebase.
- **DO NOT** commit the `.env` file to the repository.

### Logging Best Practices (ISO 29119 Standards)
Follow MNC-level logging standards via SLF4J to ensure high traceability:
- **Do not manually log thread IDs or Scenario names**. This is handled globally by MDC in `Hooks.java`. Just write `logger.info("Executing login")`.
- **INFO**: Log business-level actions (e.g., `logger.info("User navigated to the checkout page");`).
- **DEBUG**: Log diagnostic data (e.g., locator paths, API payloads).
- **WARN**: Log non-fatal anomalies or fallback triggers (e.g., `logger.warn("Primary locator failed, engaging AI Auto-Heal");`).
- **ERROR**: Log stack traces and critical failures before throwing exceptions.
- **NEVER** use `System.out.println()`. Always use the integrated logger.
