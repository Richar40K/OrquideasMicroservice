ARG MODULE=microservice-payment
FROM eclipse-temurin:21-jdk as builder
WORKDIR /app
ARG MODULE=microservice-payment
COPY ${MODULE} /app/${MODULE}
COPY pom.xml .
RUN chmod +x /app/${MODULE}/mvnw && /app/${MODULE}/mvnw -f /app/${MODULE}/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
ARG MODULE=microservice-payment
WORKDIR /app
COPY --from=builder /app/${MODULE}/target/*.jar app.jar
#COPY ${MODULE}/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]