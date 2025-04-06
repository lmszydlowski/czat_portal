# Stage 1: Build the application
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

# Copy gradle configuration files
COPY build.gradle.kts settings.gradle.kts ./

# Copy gradle wrapper
COPY gradle ./gradle
COPY gradlew ./

# Copy source code
COPY src ./src

# Build the application in production mode
RUN chmod a+x gradlew
RUN ./gradlew bootJar --no-dae
# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Copy keystore for SSL
#COPY czat2/bin/main/keystore.p12 /app/keystore.p12

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
