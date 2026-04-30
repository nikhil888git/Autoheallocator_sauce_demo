Feature: Checkout Flow

  @REQ-CHECKOUT-001 @e2e @checkout @P1
  Scenario: User completes purchase successfully
    Given user launches browser
    When user opens login page
    And user logs in with "standard_user" and "secret_sauce"
    And user adds product to cart
    And user proceeds to checkout
    And user enters checkout details
    Then user should complete the order successfully

  @REQ-CHECKOUT-002 @negative @checkout @P2
  Scenario Outline: User cannot checkout with invalid details
    Given user launches browser
    When user opens login page
    And user logs in with "standard_user" and "secret_sauce"
    And user adds product to cart
    And user proceeds to checkout
    And user enters checkout details "<firstname>" "<lastname>" "<postalcode>"
    Then user should not be able to complete the order
 
    Examples:
      | firstname | lastname | postalcode |
      | standard_user1 |  |   |