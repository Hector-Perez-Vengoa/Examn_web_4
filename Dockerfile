# Etapa de construcción
FROM amazoncorretto:17-al2-jdk AS build

# Instalar Maven
RUN yum update -y && yum install -y maven

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Descargar dependencias
RUN mvn dependency:go-offline -B

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