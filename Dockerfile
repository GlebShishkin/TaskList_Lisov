#FROM openjdk:21-jdk-slim
#COPY /target/*.jar application.jar
#ENTRYPOINT ["java", "-jar", "application.jar"]

FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /
COPY pom.xml .
COPY /src /src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
