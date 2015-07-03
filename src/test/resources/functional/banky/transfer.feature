Feature: Make a transfer Tests

  Background:
    Given we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": "34.00",
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
    Given we POST the following JSON to "/accounts":
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": "34.00",
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

    And we POST the following JSON to saved-link "fromAccountLink" and sub-path "/transfer":
    """
    {
      "accountToId": "${toAccountLinkId}",
      "amount": 24.00
    }
    """

    And the last call was OK

    And we GET the saved-link "fromAccountLink"

    Then the response matches:
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 10.0,
      "phoneNumber": "23423487"
    }
    """

    And we GET the saved-link "toAccountLink"

    And the response matches:
    """
    {
      "name": "John",
      "address": "dublin, dublin, dublin",
      "balance": 158.0,
      "phoneNumber": "23423487"
    }
    """

    And we GET the saved-link "fromAccountLink" and sub-path "/transactionsFrom"

    Then the response matches:
    """
    {
      "_embedded":{
        "transactions":[
          {
            "transactionType":"TRANSFER",
            "amount":24.0
          }
        ]
      }
    }
    """



  Scenario: Transfer but has not enough money on account
    Given we accept error responses
    And we POST the following JSON to saved-link "fromAccountLink" and sub-path "/transfer":
    """
    {
      "accountToId": "${toAccountLinkId}",
      "amount": 54.00
    }
    """

    And the last call was BAD_REQUEST



  Scenario: Transfer to account that doesn't exist
    Given we accept error responses
    And we POST the following JSON to saved-link "fromAccountLink" and sub-path "/transfer":
    """
    {
      "accountToId": "234134",
      "amount": 14.00
    }
    """

    And the last call was BAD_REQUEST



  Scenario: Transfer from account that doesn't exist
    Given we accept error responses
    And we POST the following JSON to "/accounts/3123123312/transfer":
    """
    {
      "accountToId": "${toAccountLinkId}",
      "amount": 14.00
    }
    """

    And the last call was BAD_REQUEST