ARG MODULE=microservice-oauth
FROM eclipse-temurin:24-jdk as builder
WORKDIR /app
ARG MODULE=microservice-oauth
COPY ${MODULE} /app/${MODULE}
COPY pom.xml .
RUN chmod +x /app/${MODULE}/mvnw && /app/${MODULE}/mvnw -f /app/${MODULE}/pom.xml clean package -DskipTests

FROM eclipse-temurin:24-jre
ARG MODULE=microservice-oauth
WORKDIR /app
COPY --from=builder /app/${MODULE}/target/*.jar app.jar
#COPY ${MODULE}/target/*.jar app.jar
EXPOSE 9100
ENTRYPOINT ["java","-jar","/app/app.jar", "microservice-oauth.jar"]