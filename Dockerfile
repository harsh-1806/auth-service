FROM amazoncorretto:17

WORKDIR /app

COPY target/expense-tracker-backend-0.0.1-SNAPSHOT.jar /app/expense-tracker-backend-0.0.1-SNAPSHOT.jar

EXPOSE 9898

ENTRYPOINT ["java", "-jar", "/app/expense-tracker-backend-0.0.1-SNAPSHOT.jar"]