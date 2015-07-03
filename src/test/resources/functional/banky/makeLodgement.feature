Feature: Make a lodgement Tests

  Scenario: Create a lodgement successfully

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
    And we POST the following JSON to saved-link "accountLink" and sub-path "/lodge":
      """
      {
        "amount": "24.00"
      }
      """

    And the last call was OK

    And we GET the saved-link "accountLink"

    And the response matches:
      """
      {
        "name": "John",
        "address": "dublin, dublin, dublin",
        "balance": 258.0,
        "phoneNumber": "23423487"
      }
      """

    And we GET the saved-link "accountLink" and sub-path "/transactionsTo"

    Then the response matches:
      """
      {
        "_embedded":{
          "transactions":[
            {
              "transactionType":"LODGEMENT",
              "amount":24.0
            }
          ]
        }
      }
      """

  Scenario: Create a lodgement to an non-existing account
    Given we accept error responses
    And we POST the following JSON to "/accounts/12312/lodge":
    """
    {
      "amount": "124.00"
    }
    """

    Then the last call was BAD_REQUEST



