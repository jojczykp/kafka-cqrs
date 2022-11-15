FROM --platform=linux/amd64 openjdk:17-jdk-alpine
LABEL service-name=producer-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD producer-service.tar /opt

ENTRYPOINT "/opt/producer-service/bin/producer-service"
