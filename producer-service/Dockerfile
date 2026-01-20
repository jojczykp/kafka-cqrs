FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine
LABEL service-name=producer-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD target/distributions/producer-service.tar /opt

ENTRYPOINT "/opt/producer-service/bin/producer-service"
