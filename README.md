# User Service Project
This project interacts with user information via a restcontroller and a jparepository that connects to a mysql or h2
instance.

This project is built on springboot 3 and Java 17.

# Testing
## Unit And Code Coverage
The unit tests can be run with:
```
mvn surefire-report:report jacoco:report
```

The html reports can be found under:

[Unit tests report](target/site/surefire-report.html)

[Code coverage report](target/site/jacoco/index.html)

## Running on IDE
Configure your IDE to run the UserServiceApplication on your local IDE. You will need a configured
Mysql instance else you can spin one up by running the following command (2 docker compose commands for 2 different versions)
``` bash
docker compose -f docker-compose-local.yml up -d percona
#or
docker-compose -f docker-compose-local.yml up -d percona
```

Once running for the first time via IDE, you can execute the [curl bash script](call_services.sh)  (This script is not
idempotent and will only really work if the database is empty. You can remove the volume for the percona docker 
container to remove persistence or change the [application.yaml](src/main/resources/application.yaml) setting ddl-auto: update
to ddl-auto: create

or run the curl commands manually:

```bash
echo -e "Get All users: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:8081/users)"

echo -e "\nAdd a user: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:8081/users -d '{"firstName":"Test1", "lastName":"Test1Last", "email":"email1@email.com"}') "

echo -e "\nAdd a another user: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X POST http://localhost:8081/users -d '{"firstName":"Test3", "lastName":"Test3Last", "email":"email3@email.com"}') "

echo -e "\nGet User for ID 1: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" http://localhost:8081/users/1)"

echo -e "\nUpdate a user with id 1: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X PUT http://localhost:8081/users/1 -d '{"firstName":"Test2Update", "lastName":"Test2LastUpdate", "email":"email2Update@email.com"}') "

echo -e "\nDelete a user with id 2: $(curl  --silent --show-error --write-out '\nhttp_response_code: %{http_code}' -H "Content-Type: application/json" -X DELETE http://localhost:8081/users/2) "
```

# Build Steps
## Manual Build
Navigate to the root of the project then:
- Run maven build
```
mvn package
```

- Run docker build
```
docker build -t user-service-tomcat:1_0_0 .
```

## Build Script
- Run ./build.sh

# Run Steps

Make sure port 8081 and 3316 is clear else configure different ports and update the relevant config.
```
docker compose -f docker-compose-local.yml
```


# Http Server Expected Input and Methods
## Expected Input object
### User
Mandatory Input: firstName, lastName, email
Validation: firstName, lastName, email cannot be blank and email must be a valid email address
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "email@email.com"
}
```
## Expected Responses
### Success
#### User
```json
{
  "id": "1",
  "firstName": "John",
  "lastName": "Smith",
  "email": "email@email.com"
}
```

### Error
#### Error Response

```json
{
  "failureReson": "Validation failed",
  "failedInput": ["Lastname is blank", "Firstname is blank"]
}
```

## HTTP Request Methods
### get all users

```
Method: GET 
Input: None
Response: List of User 200 Ok
ErrorResponse: No Content 204 or 503 Service Unavailable
URL (IDE): /users
URL (Container): /user_service/users
```

### get user for Id
```
Method: GET
Input: PathVariable Long Id (id of user)
Response: User
ErrorResponse: No Content 204 or 503 Service Unavailable
URL (IDE): /users/{id}
URL (Container): /user_service/users/{id}
```

### create user
```
Method: POST
Input: User Object (application/json)
Response: User and 201 Created
ErrorResponse: 400 Bad Request (Failed validation) or 503 Service Unavailable
URL (IDE): /users
URL (Container): /user_service/users
```

### Update User
```
Method: PUT
Input: User Object (application/json) and PathVariable Long id
Response: User and 201 Created
ErrorResponse: 400 Bad Request (Failed validation and user does not exist) or 503 Service Unavailable
URL (IDE): /users/{id}
URL (Container): /user_service/users/{id}
```

### Delete User
```
Method: DELETE
Input: PathVariable Long id
Response: User and 200 Ok
ErrorResponse: 400 Bad Request (Failed validation and user does not exist) or 503 Service Unavailable
URL (IDE): /users/{id}
URL (Container): /user_service/users/{id}
```