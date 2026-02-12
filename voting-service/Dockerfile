FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests -pl voting-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/voting-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
