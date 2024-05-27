FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /opt/app

ADD target/walldeal-eureka.jar app.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]