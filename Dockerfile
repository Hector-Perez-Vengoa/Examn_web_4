# Etapa de construcción
FROM maven:3.9.5-amazoncorretto-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar solo pom.xml primero para aprovechar la cache de Docker
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir la aplicación con optimizaciones
RUN mvn clean package -DskipTests -B -q

# Etapa de ejecución
FROM amazoncorretto:17-alpine-jdk

# Agregar usuario no-root por seguridad
RUN addgroup -g 1000 appuser && adduser -u 1000 -G appuser -s /bin/sh -D appuser

# Crear directorio de la aplicación
RUN mkdir -p /app && chown appuser:appuser /app

# Cambiar al usuario no-root
USER appuser

# Copiar el JAR desde la etapa de construcción
COPY --from=build --chown=appuser:appuser /app/target/Exam_Perez-0.0.1-SNAPSHOT.jar /app/api-v1.jar

# Establecer directorio de trabajo
WORKDIR /app

# Exponer el puerto
EXPOSE 8080

# Configurar JVM para contenedor
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# Punto de entrada con configuración optimizada
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-v1.jar --spring.profiles.active=prod"]
