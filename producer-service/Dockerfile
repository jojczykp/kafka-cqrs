FROM --platform=linux/amd64 openjdk:13-jdk-alpine
LABEL service-name=producer-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

COPY target/libs/producer-service-*.jar producer-service.jar
COPY docker-entrypoint.sh /

RUN apk add --no-cache \
    bash \
    netcat-openbsd

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT "/docker-entrypoint.sh"
