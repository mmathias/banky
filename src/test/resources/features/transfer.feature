Feature: Make a transfer Tests

  Background:
    Given we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": "234.00",
      "phoneNumber": "23423487"
    }
    """

    And the last call was CREATED
    And we save the returned location as a saved-link "fromAccountLink"

    And we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": "134.00",
      "phoneNumber": "23423487"
    }
    """

    And the last call was CREATED
    And we save the returned location as a saved-link "toAccountLink"


  Scenario: Transfer successfully
    Given we POST the following JSON to "/transfer":
    """
    {
      "accountFromId": "${fromAccountLinkId}",
      "accountToId": "${toAccountLinkId}",
      "amount": 24.00
    }
    """

    And the last call was OK

    Given we GET the saved-link "toAccountLink"

    Then the response matches:
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 158.0,
      "phoneNumber": "23423487"
    }
    """

    Given we GET the saved-link "fromAccountLink"

    Then the response matches:
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 210.0,
      "phoneNumber": "23423487"
    }
    """

