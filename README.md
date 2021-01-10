
# zephyr-microservice-edge

## About The Project
Welcome to the Readme of the project of Ward Beyens, Thibaut Bollen and Cindy Knapen. For the Advanced Programming Topics Class, we have made a project around a clothing webshop. This project covers 4 micro services, the edge microservice, user microservice, clothes microservice and order microservice. The user microservice uses Postgres as database while the clothes and order services make use of a MongoDB database.

## Our micro services


- 
  [zephyr-microservice-user](https://github.com/cindy5656/zephyr-microservice-user): ![Test, Build and Upload artifact](https://github.com/cindy5656/zephyr-microservice-user/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=master)
  
- 
  [zephyr-microservice-clothes](https://github.com/thibautbollen/zephyr-microservice-clothes): ![Test, Build and Upload artifact](https://github.com/thibautbollen/zephyr-microservice-clothes/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  
- 
  [zephyr-microservice-order](https://github.com/wardbeyens/zephyr-microservice-order): ![Test, Build and Upload artifact](https://github.com/wardbeyens/zephyr-microservice-order/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  
- 
  [zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge): ![Test, Build and Upload artifact](https://github.com/wardbeyens/zephyr-microservice-edge/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)
  


### Built With

* []()java
* []()spring boot
* []()swagger

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* docker
  ```sh
    docker run -d -p 27017-27019:27017-27019 --name mongodb-adv-clothes mongo

    docker run -d -p 17017-17019:27017-27019 --name mongodb-adv-orders mongo
    
    docker run -d -p 5432:5432 --name postgres-adv-users -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=userTest postgres

    docker run -d --name user-service -p 8051:8051 -e POSTGRESQL_DB_HOST=host.docker.internal cindy5656/userservice:latest

    docker run -d --name order-service -p 8052:8052 -e MONGODB_HOST=host.docker.internal -e MONGODB_PORT=17017 wardbeyens/orderservice:latest

    docker run -d --name clothes-service -p 8053:8053 -e MONGODB_HOST=host.docker.internal 990604/clothesservice:latest

    (docker run -d --name edge-service -p 8050:8050 -e USER_SERVICE_HOST=host.docker.internal -e CLOTHES_SERVICE_HOST=host.docker.internal -e ORDER_SERVICE_HOST=host.docker.internal wardbeyens/zephyr-edge-service:latest)
  ```

### Installation

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

## Swagger UI
Our swagger UI describes the Get, Post, Put and Delete requests that can be used on the edge service. 

![Swagger UI](https://i.imgur.com/rixDKdr.png)


## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

## Contact

Ward Beyens - [@wardbeyens](https://twitter.com/wardbeyens)

Project Link: [https://github.com/wardbeyens/zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge)

