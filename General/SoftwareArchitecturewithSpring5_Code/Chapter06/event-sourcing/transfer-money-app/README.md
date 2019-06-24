## Create a new bank customer

```
curl -H "Content-Type: application/json" -X POST -d '{"account_type": "savings", "name": "Rene", "last_name": "Enriquez", "initial_amount": 1000}' http://localhost:8080/customer
```


## Recreate the app state

```
curl -X POST http://localhost:8080/events/<EVENT_ID>
```
