FROM eclipse-temurin:24-jre

WORKDIR /app

COPY . .

RUN chmod +x mvnw

# Cambia aquí el nombre del módulo (carpeta del microservicio) que quieres compilar
ARG MODULE=eureka-server
RUN ./mvnw clean package -DskipTests -pl ${MODULE} -am

WORKDIR /app/${MODULE}

# Cambia el nombre del jar según corresponda
CMD ["java", "-jar", "target/eureka-server-0.0.1-SNAPSHOT.jar"]
