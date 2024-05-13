FROM openjdk:17

ARG JAR_FILE=target/*.jar

# Define the directory where the serviceAccountKey.json will be copied
WORKDIR /opt/app

ADD target/cache-service-0.0.1-SNAPSHOT.jar app.jar

# Copy the serviceAccountKey.json file into the container
COPY serviceAccountKey.json /opt/app/serviceAccountKey.json

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Specify the entry point command to run the application
ENTRYPOINT ["java","-jar","app.jar"]
