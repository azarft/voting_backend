FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
RUN chmod +x mvnw
COPY .mvn .mvn
COPY pom.xml .
COPY . .
RUN ./mvnw clean package -DskipTests -pl result-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/result-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
