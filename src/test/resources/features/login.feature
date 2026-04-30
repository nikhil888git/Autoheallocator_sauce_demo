
Feature: Login Test

@REQ-LOGIN-001 @smoke @login @P1
Scenario Outline: Verify user can login with valid credentials "<username>"
  Given user launches browser
  When user opens login page
  And user logs in with "<username>" and "<password>"
  Then user should see homepage

Examples:
  | username      | password      |
  | standard_user | secret_sauce |
  | problem_user  | secret_sauce |

@REQ-LOGIN-002 @medium @ui
Scenario: Verify login page is displayed
  Given user launches browser
  When user opens login page
  Then login form should be visible

 @REQ-LOGIN-003 @negative @login @P2
Scenario Outline: Verify user cannot login with invalid credentials
  Given user launches browser
  When user opens login page
  And user logs in with "<username>" and "<password>"
  Then user should not be redirected to inventory page

Examples:
  | username     | password      |
  | invalid_user | secret_sauce  |
  