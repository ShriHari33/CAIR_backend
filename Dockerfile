FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 8084
CMD ["java", "-jar", "app.jar"]