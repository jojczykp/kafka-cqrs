FROM eclipse-temurin:21-jdk-alpine
LABEL service-name=notifier-service
LABEL maintainer="Pawel Jojczyk <abc.def@ghi.jkl>"

ADD target/distributions/notifier-service.tar /opt

ENTRYPOINT ["/opt/notifier-service/bin/notifier-service"]
