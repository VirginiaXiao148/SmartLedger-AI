# Etapa de compilación (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila el proyecto y salta los tests para ir más rápido
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia el jar generado desde la carpeta /app/target de la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Spring Boot detectará las variables de entorno de Render automáticamente
ENTRYPOINT ["java", "-jar", "app.jar"]