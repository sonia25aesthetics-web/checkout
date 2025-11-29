# Use official Java 17 image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy all project files into container
COPY . .

# Build the project using Maven wrapper (or fallback to system Maven)
RUN ./mvnw clean package || mvn clean package

# Run the Spring Boot JAR
CMD ["java", "-jar", "target/checkout-0.0.1-SNAPSHOT.jar"]
