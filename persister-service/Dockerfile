FROM eclipse-temurin:21-jdk-alpine
LABEL service-name=persister-service
LABEL maintainer="Pawel Jojczyk <abc.def@ghi.jkl>"

ADD target/distributions/persister-service.tar /opt

ENTRYPOINT ["/opt/persister-service/bin/persister-service"]
