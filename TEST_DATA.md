# Test Data Management

## ISO 29119 Alignment
Test data is completely isolated from step definitions and execution logic. Hardcoded data inside Java classes is strictly prohibited.

## CSV Usage for Bulk Data
For data-driven scenarios (like multiple login permutations), we use CSV files.
A dedicated `CsvReader` utility parses these files into memory (`List<Map<String, String>>`) for rapid execution.

**Example `login.csv`**:
```csv
username, password, expected_status
standard_user, secret_sauce, success
locked_out_user, secret_sauce, failure
```

## JSON Strategy
For complex, nested test data (e.g., API payloads or complex user profiles), JSON files are preferred. The `ObjectMapper` (Jackson) is used to deserialize JSON directly into Java POJOs.

## POJO Mapping
Data extracted from CSVs or JSONs is instantly mapped to Plain Old Java Objects (POJOs).
This ensures type-safety and provides clean getter methods for Step Definitions, eliminating risky string manipulations during test execution.
