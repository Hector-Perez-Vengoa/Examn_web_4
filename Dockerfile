# Etapa de construcción
FROM maven:3.9.5-amazoncorretto-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM amazoncorretto:17-alpine-jdk

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/Exam_Perez-0.0.1-SNAPSHOT.jar /api-v1.jar

# Exponer el puerto
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["java", "-jar", "/api-v1.jar"]