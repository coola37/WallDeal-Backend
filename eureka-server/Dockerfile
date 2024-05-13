FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /opt/app

ADD target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]