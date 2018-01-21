#!/usr/bin/env bash


SCALA_VERSION=2.12
KAFKA_VERSION=1.0.0

TARGET=/opt


echo "...:: Download Zookeeper and Kafka ::..."

# Download / Extract
wget -qO- "http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" | tar xvz -C "${TARGET}"
mv "${TARGET}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}" "${TARGET}/kafka"

# Setup user and group
useradd kafka -U
chown -R kafka /opt/kafka
chgrp -R kafka /opt/kafka

echo "...:: Download Zookeeper and Kafka - Done ::..."
