FROM eclipse-temurin:24-jre

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests -pl eureka-server -am

WORKDIR /app/eureka-server

CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
