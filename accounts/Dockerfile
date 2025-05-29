FROM openjdk:21-jdk-slim

COPY target/accounts-*.jar /app/accounts.jar

ENTRYPOINT ["java", "-jar", "/app/accounts.jar"]
