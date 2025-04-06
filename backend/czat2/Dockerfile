# Stage 1: Build the application
FROM gradle:8.10-jdk-21 AS build

WORKDIR /app

# Copy gradle configuration files
COPY czat2/build.gradle.kts czat2/settings.gradle.kts ./

# Copy gradle wrapper
COPY czat2/gradle ./gradle
COPY czat2/gradlew ./

# Copy source code
COPY czat2/src ./src

# Build the application in production mode
RUN ./gradlew bootJar --no-daemon

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
