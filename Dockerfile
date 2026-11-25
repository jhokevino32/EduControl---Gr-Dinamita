# Etapa 1: Construir el JAR con Maven
FROM maven:3.9.0-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación con una imagen ligera
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/EduControl-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]