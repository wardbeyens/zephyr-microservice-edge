FROM openjdk:8-jdk-alpine
EXPOSE 8050
ARG JAR_FILE=target/edge-0.0.1.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]