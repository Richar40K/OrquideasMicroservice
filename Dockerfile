# Dockerfile universal para cualquier microservicio del monorepo
# Este archivo va en la RAÍZ del proyecto OrquideasMicroservice

# Etapa de build
ARG MODULE=eureka-server
FROM eclipse-temurin:24-jdk as builder

WORKDIR /app

# Copia wrapper y configuraciones de Maven
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .
COPY ${MODULE} /app/${MODULE}

# Da permisos al wrapper y compila el microservicio deseado
RUN chmod +x mvnw && ./mvnw -f /app/${MODULE}/pom.xml clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:24-jre
ARG MODULE=eureka-server
WORKDIR /app

COPY --from=builder /app/${MODULE}/target/*.jar app.jar

# Puedes cambiar el puerto según el microservicio que vayas a desplegar
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/app.jar"]