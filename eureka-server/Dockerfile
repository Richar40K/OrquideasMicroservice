# Etapa de construcción (builder)
ARG MODULE=eureka-server
FROM eclipse-temurin:24-jdk AS builder

WORKDIR /app

# Copia el wrapper y la config de Maven si están en la raíz
COPY mvnw ./
COPY .mvn/ .mvn/

# Copia el pom padre
COPY pom.xml .

# Copia la carpeta del microservicio
COPY ${MODULE} /app/${MODULE}

# Compila solo el microservicio
RUN chmod +x mvnw && ./mvnw -f /app/${MODULE}/pom.xml clean package -DskipTests

# Etapa de runtime (solo el jar y Java JRE)
FROM eclipse-temurin:24-jre
ARG MODULE=eureka-server
WORKDIR /app

# Copia el jar desde la etapa de builder
COPY --from=builder /app/${MODULE}/target/*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
