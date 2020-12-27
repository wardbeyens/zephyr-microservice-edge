zephyr-microservice-edge: ![zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)

zephyr-microservice-user:![Test, Build and Upload artifact](https://github.com/cindy5656/Zephyr-userservice/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=master)

zephyr-microservice-clothes: ![Test, Build and Upload artifact](https://github.com/thibautbollen/zephyr-clothes/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)

zephyr-microservice-order: ![Test, Build and Upload artifact](https://github.com/wardbeyens/zephyr-microservice-order/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg?branch=main)

# zephyr-microservice-edge

## About The Project

School project, github actions, tests, ...

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
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

## Contact

Ward Beyens - [@wardbeyens](https://twitter.com/wardbeyens)

Project Link: [https://github.com/wardbeyens/zephyr-microservice-edge](https://github.com/wardbeyens/zephyr-microservice-edge)

