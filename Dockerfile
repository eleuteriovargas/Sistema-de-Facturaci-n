# Dockerfile

FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR desde la subcarpeta facturacion/
COPY facturacion/target/app.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando que ejecuta el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
