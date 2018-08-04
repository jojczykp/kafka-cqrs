FROM openjdk:8-jdk-alpine
LABEL service-name=reader-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

COPY target/libs/reader-service-*.jar /reader-service.jar
COPY docker-entrypoint.sh /

RUN apk add --no-cache \
    bash \
    netcat-openbsd

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT "/docker-entrypoint.sh"
