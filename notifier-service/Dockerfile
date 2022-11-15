FROM --platform=linux/amd64 openjdk:17-jdk-alpine
LABEL service-name=notifier-service
MAINTAINER Pawel Jojczyk <abc.def@ghi.jkl>

ADD notifier-service.tar /opt

ENTRYPOINT "/opt/notifier-service/bin/notifier-service"
