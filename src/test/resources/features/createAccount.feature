Feature: Create Account Tests

  Scenario: Create a valid account
    Given we POST the following JSON to "/account":
    """
    {
      "name": "John"
      "address": "dublin, dublin, dublin"
      "balance": 234.00
      "phoneNumber": "23423487"
    }
    """

    Then the last call was CREATED

  Scenario Outline: Try to create an invalid account
    Given we POST the following JSON to "/account":
    """
    {
      "name": "<name>"
      "address": "<address>"
      "balance": <balance>
      "phoneNumber": "<phoneNumber>"
    }
    """
    And the last call was <response>
    Then the response matches:
    """
    {
      "error": {
        "code": "",
        "cause": "<responseMessage>",
        "message": "You are not allowed to perform this operation"
        "links": []
      }
    }
    """

    Examples:
    |name |address          |balance|phoneNumber|response   |responseMessage                    |
    |     |apto 216, dublin |123.56 |9879879898 |BAD_REQUEST|Name field is required             |
    |Mary |                 |123.56 |9879879898 |BAD_REQUEST|Address is required                |
    |Mary |apto 216, dublin |       |9879879898 |BAD_REQUEST|Balance is required                |
    |123M |apto 216, dublin |123.56 |9879879898 |BAD_REQUEST|Name needs to contain only letters |





