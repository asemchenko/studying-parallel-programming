FROM ubuntu:18.04
COPY target/lab1-1.0-SNAPSHOT-jar-with-dependencies.jar /app/analyzer.jar
COPY src/main/resources/pathCheckers.json /app/pathCheckers.json

# === ALLOWS TO EXECUTE JAVA APPLICATIONS ===
RUN apt update
RUN apt install -y openjdk-17-jre-headless

# === ALLOWS TO MEASURE TIME ===
RUN apt install -y time


ENTRYPOINT ["/usr/bin/time", "-p", "java", "-jar", "/app/analyzer.jar", "/tmp", "/app/pathCheckers.json"]
