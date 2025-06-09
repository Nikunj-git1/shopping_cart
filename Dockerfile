# Base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file into container
COPY target/shopping_cart-0.0.1-SNAPSHOT.jar app.jar

# Port expose
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]