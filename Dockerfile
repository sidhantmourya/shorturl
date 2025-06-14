FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/shorturl-0.0.1-SNAPSHOT.jar shorturl.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "shorturl.jar"]