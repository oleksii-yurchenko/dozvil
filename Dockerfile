FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY target/dozvil-1.0.0.jar /app/dozvil-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/app/dozvil-1.0.0.jar"]
