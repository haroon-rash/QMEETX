FROM eclipse-temurin:21-jre
WORKDIR /app
COPY Appointment-Service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
