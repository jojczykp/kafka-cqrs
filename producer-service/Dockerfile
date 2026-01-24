FROM eclipse-temurin:21-jdk-alpine
LABEL service-name=producer-service
LABEL maintainer="Pawel Jojczyk <abc.def@ghi.jkl>"

ADD target/distributions/producer-service.tar /opt

ENTRYPOINT ["/opt/producer-service/bin/producer-service"]
