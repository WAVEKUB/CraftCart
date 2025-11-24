FROM eclipse-temurin:24-jdk AS build
WORKDIR /app
COPY . .
# Ensure mvnw is executable
RUN chmod +x mvnw
# Build the application skipping tests to speed up the process
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
