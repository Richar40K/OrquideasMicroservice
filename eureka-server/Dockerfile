FROM eclipse-temurin:24-jre

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Dale permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Compila el proyecto (sin correr tests)
RUN ./mvnw clean package -DskipTests

# Cambia el nombre del jar aquí si es diferente
CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
