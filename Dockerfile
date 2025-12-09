# ---------- Stage 1: build the JAR ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copy pom.xml and src
COPY pom.xml .
COPY src src

# Build the JAR (skip tests to speed up)
RUN mvn -q clean package -DskipTests


# ---------- Stage 2: run the app ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /build/target/instructions-capture-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
