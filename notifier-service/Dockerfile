FROM openjdk:8-jdk-alpine
LABEL service-name=notifier-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>
COPY target/libs/notifier-service-*.jar notifier-service.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS \
 -jar notifier-service.jar
