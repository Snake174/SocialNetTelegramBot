FROM maven:3.8.6-jdk-11-slim
WORKDIR /app
COPY target/snbot-0.0.1-SNAPSHOT.jar /app/snbot.jar
COPY src/main/resources/snbot.yml /app/snbot.yml
RUN apt-get update && apt-get install -y fontconfig
ENTRYPOINT ["java", "-jar", "/app/snbot.jar", "--spring.config.location=/app/snbot.yml"]
