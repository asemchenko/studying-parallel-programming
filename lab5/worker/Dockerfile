FROM ubuntu:18.04


# === ALLOWS TO EXECUTE JAVA APPLICATIONS ===
RUN apt update && apt-get install -y openjdk-17-jre-headless
COPY src/main/resources/pathCheckers.json /app/pathCheckers.json
COPY target/worker-0.0.1-SNAPSHOT.jar /app/analyzer.jar

ENTRYPOINT ["java", "-jar", "/app/analyzer.jar"]