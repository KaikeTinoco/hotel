FROM maven:3.8.1-openjdk-17-slim AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -Dspring.profiles.active=test

FROM eclipse-temurin:17-jre


COPY --from=build /app/target/hotel-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

