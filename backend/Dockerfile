# Etapa de compilación (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY pom.xml .
COPY src ./src
# Compila el proyecto y salta los tests para ir más rápido
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

