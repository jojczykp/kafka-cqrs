FROM openjdk:8-jdk-alpine
LABEL service-name=persister-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>
COPY target/libs/persister-service-*.jar persister-service.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS \
 -jar persister-service.jar
