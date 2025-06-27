# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR al contenedor
COPY target/securityservice-0.0.1-SNAPSHOT.jar security-service.jar

# Exponer el puerto 8888
EXPOSE 8888

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "security-service.jar"]
