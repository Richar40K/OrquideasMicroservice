FROM eclipse-temurin:24-jre

WORKDIR /app

# Copia todo el repo al contenedor (incluye pom padre y todos los módulos)
COPY . .

# Da permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw

# Cambia al directorio del microservicio
WORKDIR /app/eureka-server

# Compila SOLO el módulo eureka-server (el .. antes de mvnw es porque está en la raíz)
RUN ../mvnw clean package -DskipTests

# Cambia el nombre del jar si es diferente en tu proyecto
CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
