# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/assignment-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (default: 8080)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]