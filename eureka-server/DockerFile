FROM eclipse-temurin:24-jre
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw clean package -DskipTests
CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
