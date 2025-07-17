# Dockerfile del microservicio eureka-server de forma independiente
# (Si SOLO subes la carpeta eureka-server a Render)

FROM eclipse-temurin:24-jdk as builder
WORKDIR /app

# Copia solo lo necesario si estás dentro de la carpeta eureka-server directamente
COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]