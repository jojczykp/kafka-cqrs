FROM openjdk:8-jdk-alpine
LABEL service-name=producer-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>
COPY target/libs/producer-service-*.jar producer-service.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS \
 -jar producer-service.jar
