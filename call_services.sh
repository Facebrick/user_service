#!/bin/bash

#Docker
#app_port=8081/user_service

#IDE
app_port=8081

#This script works when you first start run the app. Otherwise it's just a place to save my curls.

echo -e "Get All users: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:$app_port/users)"

echo -e "\nAdd a user: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:$app_port/users -d '{"firstName":"Test1", "lastName":"Test1Last", "email":"email1@email.com"}') "

echo -e "\nAdd a another user: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:$app_port/users -d '{"firstName":"Test3", "lastName":"Test3Last", "email":"email3@email.com"}') "

echo -e "\nGet User for ID 1: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:$app_port/users/1)"

echo -e "\nUpdate a user with id 1: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X PUT http://localhost:$app_port/users/1 -d '{"firstName":"Test2Update", "lastName":"Test2LastUpdate", "email":"email2Update@email.com"}') "

echo -e "\nGet All users: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:$app_port/users)"

echo -e "\nDelete a user with id 2: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X DELETE http://localhost:$app_port/users/2) "

echo -e "\nGet All users: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:$app_port/users)"

echo -e "\nGet User that doesn't exist: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:$app_port/users/1234)"

echo -e "\nUpdate a user that doesn't exist: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X PUT http://localhost:$app_port/users/1234 -d '{"firstName":"Test2Update", "lastName":"Test2LastUpdate", "email":"email2Update@email.com"}') "

echo -e "\nUpdate a user with invalid data: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X PUT http://localhost:$app_port/users/1234 -d '{"firstName":"", "lastName":"", "email":""}') "

echo -e "\nAdd a user with no first name: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:$app_port/users -d '{"firstName":"", "lastName":"Test1Last", "email":"email1@email.com"}') "

echo -e "\nAdd a user with no last name: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:$app_port/users -d '{"firstName":"John", "lastName":"", "email":"email1@email.com"}') "

echo -e "\nAdd a user with invalid email: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:$app_port/users -d '{"firstName":"John", "lastName":"Test2Update", "email":"email1"}') "

echo -e "\nDelete a user that doesn't exist: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X DELETE http://localhost:$app_port/users/1234) "
#curl -H "Content-Type: application/json" -X POST http://localhost:3000/data -d '{"key1":"value1", "key2":"value2"}'
