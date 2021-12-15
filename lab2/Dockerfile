FROM ubuntu:18.04

RUN apt update && apt install -y openjdk-17-jre-headless time

COPY target/lab1-1.0-SNAPSHOT-jar-with-dependencies.jar /app/analyzer.jar
COPY src/main/resources/pathCheckers.json /app/pathCheckers.json

ENTRYPOINT ["/usr/bin/time", "-p", "java", "-jar", "/app/analyzer.jar", "/tmp", "/app/pathCheckers.json"]
