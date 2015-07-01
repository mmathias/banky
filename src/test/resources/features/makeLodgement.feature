Feature: Make a lodgement Tests

  Background:
    Given we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 234.00,
      "phoneNumber": "23423487"
    }
    """

    And the last call was CREATED
    And we save the returned location as a saved-link "accountLink"

  Scenario: Create a lodgement successfully
    Given we POST the following JSON to saved-link "accountLink" and sub-path "/lodge":
    """
    {
      "amount": "24.00"
    }
    """

    And the last call was OK

    Given we GET the saved-link "accountLink"

    Then the response matches:
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 258.0,
      "phoneNumber": "23423487"
    }
    """



