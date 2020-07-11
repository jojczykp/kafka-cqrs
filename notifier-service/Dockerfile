FROM openjdk:13-jdk-alpine
LABEL service-name=notifier-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>
COPY target/libs/notifier-service-*.jar notifier-service.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS \
 -jar notifier-service.jar



FROM openjdk:13-jdk-alpine
LABEL service-name=notifier-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

COPY target/libs/notifier-service-*.jar notifier-service.jar
COPY docker-entrypoint.sh /

RUN apk add --no-cache \
    bash \
    netcat-openbsd

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT "/docker-entrypoint.sh"
