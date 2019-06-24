#Running the application

In order to facilitate the messaging server a docker-compose file is used.

## Start RabbitMQ
docker-compose up

## Hit the endpoint which will do the transfer money process and later emmit an event:
curl -d'{"customer_id":"1a2b", "origin_account_number":"A1", "destination_account_number":"Z1", "amount":888, "external_bank":"true"}' -H "Content-Type: application/json" http://localhost:8080/transfer
