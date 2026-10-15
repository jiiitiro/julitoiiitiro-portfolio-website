# Stage 1: Build Java Application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Added -Dmaven.wagon.http.retryHandler.count=3 for network resiliency
RUN mvn clean package -DskipTests -Dmaven.wagon.http.retryHandler.count=3

# Stage 2: Run Spring Boot App
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
