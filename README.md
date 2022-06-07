# code-challenge-vivasoft

Scenario 1: Think you have multiple services with multiple instances and each instance communicates with a server called Config Service for properties config that’s needed to run the applications. Now you face a problem and you need to change properties config but you don’t want to down your services for a single minute. How do you solve this? Remember one thing: you have multiple services with multiple instances that are running individually.

Solution:
I have used Spring boot Actuator to dynamically update properties file. By clicking the below end point we can update properties dynamically.
```
http://localhost:8080/actuator/refresh
```

But we faced another problem when one microservice has multiple instance then we need to hit 

Scenario 2: Think this application also has a driver and customer app. A customer requested a ride but we have lots of drivers available on that route. Now in our business perspective we want to notify all the drivers and wait for their response. First we take the first request and others automatically notify this order already received by someone.

## Dependecies
- Redis
- Kafka
- Firebase Cloud Messaging
- Git

## Ports

| Application | Port  |
| :-----: | :-: |
| Cloud Config Server | 8888 |
| Eureka Naming Server | 8761 |
| Api Gateway | 8765 |
| Ride Service | 8000, 8001, 8002, ... |
| Customer Service | 8100, 8101, 8102, ... |
| Driver Service | 8200, 8201, 8202, ... |
| Notificatoin Service | 8300, 8301, 8302, ... |

## URLs

### Cloud Config Server
- http://localhost:8888/naming-service/default
- http://localhost:8888/api-gateway/default
- http://localhost:8888/ride-service/default
- http://localhost:8888/customer-service/default
- http://localhost:8888/driver-service/default
- http://localhost:8888/notification-service/default

### Ride service
- http://localhost:8000/currency-exchange/from/USD/to/INR

### Customer Service
- http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
- http://localhost:8100/currency-conversion-feign/from/USD/to/INR/quantity/10

### Eureka
- http://localhost:8761/
