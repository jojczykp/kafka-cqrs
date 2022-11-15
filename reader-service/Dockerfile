FROM --platform=linux/amd64 openjdk:17-jdk-alpine
LABEL service-name=reader-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD reader-service.tar /opt

ENTRYPOINT "/opt/reader-service/bin/reader-service"
