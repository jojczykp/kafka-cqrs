FROM openjdk:8-jdk-alpine
LABEL service-name=reader-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>
COPY target/libs/reader-service-*.jar reader-service.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS \
 -jar reader-service.jar
