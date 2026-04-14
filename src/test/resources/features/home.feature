Feature: Home Page Verifications

  Background:
    Given the user is logged into the application

  Scenario Outline: Verify visibility of essential UI components
    Then the user should see the "<component>" displayed on the home page

    Examples:
      | component         |
      | Hamburger menu    |
      | Cart icon         |
      | Filter dropdown   |

  Scenario: Verify product details
    Then the user should see 6 products
    And each product should have a name, image, price, and add to cart button
    And the product prices should contain "$"
    
  Scenario: Verify adding all products to cart
    When the user adds all products to the cart
    Then the cart badge count should be displayed as "6"