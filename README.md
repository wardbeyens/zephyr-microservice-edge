
# zephyr-microservice-edge

- [zephyr-microservice-edge](#zephyr-microservice-edge)
  * [About The Project](#about-the-project)
  * [Our micro services](#our-micro-services)
  * [Getting Started](#getting-started)
    + [Prerequisites](#prerequisites)
    + [Installation](#installation)
  * [Diagram](#diagram)
  * [PostMan](#postman)
  * [Swagger UI](#swagger-ui)
  * [Deployment op K8s](#deployment-op-k8s)
  * [Basic front-end](#basic-front-end)
  * [Contact](#contact)

## About The Project
Welcome to the Readme of the project of Ward Beyens, Thibaut Bollen and Cindy Knapen. For the Advanced Programming Topics Class, we have made a project around a clothing webshop. This project covers 4 micro services, the edge microservice, user microservice, clothes microservice and order microservice. The user microservice uses Postgres as database while the clothes microservice and order microservice make use of a MongoDB database.

## Our micro services

- 
  [zephyr-microservice-user](https://github.com/cindy5656/zephyr-microservice-user): ![Test, Build and Upload artifact](https://github.com/cindy5656/zephyr-microservice-user/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=master)
  
- 
  [zephyr-microservice-clothes](https://github.com/thibautbollen/zephyr-microservice-clothes): ![Test, Build and Upload artifact](https://github.com/thibautbollen/zephyr-microservice-clothes/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  
- 
  [zephyr-microservice-order](https://github.com/wardbeyens/zephyr-microservice-order): ![Test, Build and Upload artifact](https://github.com/wardbeyens/zephyr-microservice-order/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  
- 
  [zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge): ![Test, Build and Upload artifact](https://github.com/wardbeyens/zephyr-microservice-edge/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

Assuming you have a working docker environment.

* [required] create the databases and the necessary microservices
  ```sh
    docker run -d -p 27017-27019:27017-27019 --name mongodb-adv-clothes mongo

    docker run -d -p 17017-17019:27017-27019 --name mongodb-adv-orders mongo
    
    docker run -d -p 5432:5432 --name postgres-adv-users -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=userTest postgres

    docker run -d --name user-service -p 8051:8051 -e POSTGRESQL_DB_HOST=host.docker.internal cindy5656/userservice:latest

    docker run -d --name order-service -p 8052:8052 -e MONGODB_HOST=host.docker.internal -e MONGODB_PORT=17017 wardbeyens/orderservice:latest

    docker run -d --name clothes-service -p 8053:8053 -e MONGODB_HOST=host.docker.internal 990604/clothesservice:latest
  ```


### Installation

* if you only need the edge microservice api you can also use docker
  ```sh
  docker run -d --name edge-service -p 8050:8050 -e USER_SERVICE_HOST=host.docker.internal -e CLOTHES_SERVICE_HOST=host.docker.internal -e ORDER_SERVICE_HOST=host.docker.internal wardbeyens/zephyr-edge-service:latest
  ```

1. Clone the repo
   ```sh
   git clone https://github.com/wardbeyens/zephyr-microservice-edge.git
   ```
2. Install maven packages
   ```sh
   mvn install
   ```
## Diagram
Here you can see the diagram that describes how our services communicate with each other. 

![Diagram](https://i.imgur.com/LvCd8C1.png)

## PostMan
https://documenter.getpostman.com/view/13803101/TVzREHHq

[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.postman.co/run-collection/3f5d68cac4941e79a2ae#?env%5BZephyr%20Environment%20Keys%5D=W3sia2V5IjoiQVBJX1VSTCIsInZhbHVlIjoiaHR0cHM6Ly96ZXBoeXIud2FieXRlLmNvbSIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoiY2xvdGhlc1VVSURfMSIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJjbG90aGVzVVVJRF8yIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6ImNsb3RoZXNVVUlEX3RlbXAiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoiY2xvdGhlc05hbWVfdGVtcCIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJ1c2VyVVVJRF8xIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6InVzZXJVVUlEXzIiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoidXNlclVzZXJOYW1lX3RlbXAiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWV9LHsia2V5Ijoib3JkZXJVVUlEX3RlbXAiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWV9XQ==)

## Swagger UI
Our swagger UI describes the Get, Post, Put and Delete requests that can be used on the edge service. 

[https://zephyr.wabyte.com/swagger-ui.html](https://zephyr.wabyte.com/swagger-ui.html)


![Swagger UI](https://i.imgur.com/rixDKdr.png)

![Swagger UI](https://i.imgur.com/5Yw7CCC.png)

## Deployment op K8s

(Gebruik van K8s secrets voor environment variables bij deployment )

```
kubectl create secret generic dev-db-secret --from-literal=POSTGRES_DB=userTest --from-literal=POSTGRES_PASSWORD=postgres --from-literal=POSTGRES_USER=postgres

kubectl apply -f https://raw.githubusercontent.com/wardbeyens/zephyr-microservice-edge/main/k8.yaml
```

![Imgur](https://i.imgur.com/qKUIyqR.png)


## Basic front-end 

(Basic front-end dat communiceert met de edge-service )

[front-end repository](https://github.com/wardbeyens/zephyr-frontend)


## Contact

Ward Beyens - [@wardbeyens](https://twitter.com/wardbeyens)

Cindy Knapen

Thibaut Bollen

<!-- Project Link: [https://github.com/wardbeyens/zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge) -->

