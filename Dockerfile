FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/CryptoCreepBot-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]

LABEL authors="markomelyanenko"
