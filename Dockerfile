# Etapa de construcción
FROM amazoncorretto:17-al2-jdk AS build

# Instalar Maven 3.9.5 (versión más reciente)
RUN yum update -y && \
    yum install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz && \
    tar -xzf apache-maven-3.9.5-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.5 /opt/maven && \
    rm apache-maven-3.9.5-bin.tar.gz

# Configurar Maven en el PATH
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Copiar código fuente
COPY src ./src

# Construir la aplicación (sin descargar dependencias por separado)
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM amazoncorretto:17-alpine-jdk

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/Exam_Perez-0.0.1-SNAPSHOT.jar /api-v1.jar

# Exponer el puerto
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["java", "-jar", "/api-v1.jar"]