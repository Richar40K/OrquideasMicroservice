FROM eclipse-temurin:24-jre

WORKDIR /app

COPY . .

RUN chmod +x mvnw

# Compila SOLO el módulo eureka-server (y sus dependencias)
RUN ./mvnw clean package -DskipTests -pl eureka-server -am

WORKDIR /app/eureka-server

CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
