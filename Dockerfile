FROM openjdk:17
WORKDIR /app
COPY target/ordering-system-with-messaging-0.0.1-SNAPSHOT.jar /app/ordering-system-with-messaging-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "ordering-system-with-messaging-0.0.1-SNAPSHOT.jar"]