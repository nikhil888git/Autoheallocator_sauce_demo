Feature: Checkout Flow

  @e2e @checkout @P1
  Scenario: User completes purchase successfully
    Given user launches browser
    When user opens login page
    And user logs in with "standard_user" and "secret_sauce"
    And user adds product to cart
    And user proceeds to checkout
    And user enters checkout details
    Then user should complete the order successfully