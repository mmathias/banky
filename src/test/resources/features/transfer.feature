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
      "accountFrom": "${fromAccountLink}",
      "accountTo": "${toAccountLink}",
      "amount": "24.00"
    }
    """

    And the last call was CREATED



