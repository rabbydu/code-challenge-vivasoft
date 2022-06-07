# code-challenge-vivasoft

### Scenario 1: 
Think you have multiple services with multiple instances and each instance communicates with a server called Config Service for properties config that’s needed to run the applications. Now you face a problem and you need to change properties config but you don’t want to down your services for a single minute. How do you solve this? Remember one thing: you have multiple services with multiple instances that are running individually.

#### Solution:
I have used Spring boot Actuator to dynamically update properties file. By clicking the below end point we can update properties dynamically.
```
http://localhost:8000/actuator/refresh
```
But we faced another problem when one microservice has multiple instance then we need to hit every instance on there respective ports. We solved this problem by using Spring Cloud bus(kafka) which will broadcast configuration changes to all instance of microservices and they can update their properties respectively.
```
http://localhost:8000/actuator/busrefresh
```

### Scenario 2: 
Think this application also has a driver and customer app. A customer requested a ride but we have lots of drivers available on that route. Now in our business perspective we want to notify all the drivers and wait for their response. First we take the first request and others automatically notify this order already received by someone.

#### Solution:
I have used Firebase Cloud Messaging(FCM) to broadcast message to every clients. When a client(customer/driver) open there app, then will connect to FCM and collect token. Then by calling a backend api, the token will store into database so that server can send notification when an incident happen. In our case, when a customer send a ride request, first our server find the available drivers from storage, then send notification message to all drivers by using firebase token. Similarly when a driver accept a ride request, a notifcation will be send to other customer by using firebase token.

To do this we have wirte a **Notification Service application** which will receive notification request through message queue(kafka) and send the notification to the client using FCM.

## Dependecies
- Java 1.8
- Maven
- Docker
- Redis
- Kafka
- Git
- Firebase Cloud Messaging(FCM)

## Build
To build projects, go to every projcet directory and run below command to create docker image of that project
```
mvn spring-boot:build-image -DskipTest
```

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
