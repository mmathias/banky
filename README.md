# Banky
Online Banking

## Running application

mvn spring-boot:run

## Testing the application

mvn test

## Actions
### Create an account
```
POST /accounts 
{
  "name": "John",
  "address": "dublin, dublin, dublin",
  "balance": 158.0,
  "phoneNumber": "23423487"
}
```
      
### Make a lodgement

```
POST /accounts/{accountId}/lodge
{
  "amount": "124.00"
}
```

### Transfer 
```
POST /accounts/{accountId}/transfer
{
  "accountToId": "234134",
  "amount": 14.00
}
```
### View Transactions
```
GET /accounts/{accountId}/transactionsFrom
GET /accounts/{accountId}/transactionsTo
```
