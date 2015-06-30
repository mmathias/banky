Feature: Create Account Tests


  Scenario Outline: Try to create an account
    Given we POST the following JSON to "/account":
    '''
    {
    "name": "<name>"
    "address": "<address>"
    "balance": <balance>
    "phoneNumber": "<phoneNumber>"
    }
    '''
    And the response code should be <response>
    Then the response message should be <responseMessage>

    Examples:
    |name |address          |balance|phoneNumber|response   |responseMessage                    |
    |Mary |apto 216, dublin |123.56 |9879879898 |CREATED    |Account Created Sucessully         |
    |     |apto 216, dublin |123.56 |9879879898 |BAD_REQUEST|Name field is required             |
    |Mary |                 |123.56 |9879879898 |BAD_REQUEST|Address is required                |
    |Mary |apto 216, dublin |       |9879879898 |BAD_REQUEST|Balance is required                |
    |123M |apto 216, dublin |123.56 |9879879898 |BAD_REQUEST|Name needs to contain only letters |





