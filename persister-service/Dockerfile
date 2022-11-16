FROM --platform=linux/amd64 openjdk:13-jdk-alpine
LABEL service-name=persister-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD persister-service.tar /opt

ENTRYPOINT "/opt/persister-service/bin/persister-service"
