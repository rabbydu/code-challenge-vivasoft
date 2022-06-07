# code-challenge-vivasoft



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
