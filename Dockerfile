FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY --from=build /app/target/FoodDeliveryApp-0.0.1-SNAPSHOT.jar FoodDeliveryApp.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "FoodDeliveryApp.jar"]