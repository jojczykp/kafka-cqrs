FROM --platform=linux/amd64 openjdk:13-jdk-alpine
LABEL service-name=persister-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

COPY target/libs/persister-service-*.jar /persister-service.jar
COPY docker-entrypoint.sh /

RUN apk add --no-cache \
    bash \
    netcat-openbsd

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT "/docker-entrypoint.sh"
