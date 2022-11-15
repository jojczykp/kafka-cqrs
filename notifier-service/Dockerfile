FROM --platform=linux/amd64 openjdk:13-jdk-alpine
LABEL service-name=notifier-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

COPY target/libs/notifier-service-*.jar notifier-service.jar
COPY docker-entrypoint.sh /

RUN apk add --no-cache \
    bash \
    netcat-openbsd

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT "/docker-entrypoint.sh"
