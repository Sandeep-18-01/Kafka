# Stage 1: Build the JAR
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY wait-for.sh wait-for.sh

RUN chmod +x wait-for.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for.sh"]
