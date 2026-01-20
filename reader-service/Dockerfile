FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine
LABEL service-name=reader-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD target/distributions/reader-service.tar /opt

ENTRYPOINT "/opt/reader-service/bin/reader-service"
