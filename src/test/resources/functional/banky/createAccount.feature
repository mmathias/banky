Feature: Create Account Tests

  Scenario: Create a valid account
    Given we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": "234.00",
      "phoneNumber": "23423487"
    }
    """

    Then the last call was CREATED

  Scenario Outline: Try to create an invalid account
    Given we accept error responses
    And we POST the following JSON to "/accounts":
    """
    {
      "name": "<name>",
      "address": "<address>",
      "balance": "<balance>",
      "phoneNumber": "<phoneNumber>"
    }
    """
    And the last call was <response>

    Examples:
    |name |address          |balance|phoneNumber|response   |
    |     |apto 216, dublin |123.56 |9879879898 |FORBIDDEN  |
    |Mary |                 |123.56 |9879879898 |FORBIDDEN  |
    |Mary |apto 216, dublin |       |9879879898 |FORBIDDEN  |
    |Mary |apto 216, dublin |123.56 |           |FORBIDDEN  |





