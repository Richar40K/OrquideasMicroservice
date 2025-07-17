ARG MODULE=eureka-server
FROM eclipse-temurin:24-jdk as builder

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw -f ${MODULE}/pom.xml clean package -DskipTests

FROM eclipse-temurin:24-jre
ARG MODULE=eureka-server
WORKDIR /app

COPY --from=builder /app/${MODULE}/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
