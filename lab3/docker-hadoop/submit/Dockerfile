FROM bde2020/hadoop-base:2.0.0-hadoop3.2.1-java8

MAINTAINER Ivan Ermilov <ivan.s.ermilov@gmail.com>

COPY lab3-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/hadoop/applications/app.jar
COPY repos.txt /opt/repos.txt

ENV JAR_FILEPATH="/opt/hadoop/applications/app.jar"
ENV CLASS_TO_RUN="App"
ENV PARAMS="/input /output"

ADD run.sh /run.sh
RUN chmod a+x /run.sh

CMD ["/run.sh"]
