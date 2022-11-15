FROM --platform=linux/amd64 openjdk:13-jdk-alpine
LABEL service-name=reader-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD reader-service.tar /opt

ENTRYPOINT "/opt/reader-service/bin/reader-service"
