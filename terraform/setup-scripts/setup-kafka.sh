#!/usr/bin/env bash

BROKER_ID=$1
NUMBER_OF_PARTITIONS=8
REPLICATION_FACTOR=3


echo "...:: Setup Kafka ::..."

# Configure Kafka
cd /opt/kafka
cat config/server.properties \
    | sed "s|broker.id=0|broker.id=${BROKER_ID}|" \
    | sed "s|num.partitions=1|num.partitions=${NUMBER_OF_PARTITIONS}|" \
    > /tmp/server.properties
echo >> /tmp/server.properties
echo " " >> /tmp/server.properties
echo "# replication factor" >> /tmp/server.properties
echo "default.replication.factor=${REPLICATION_FACTOR}" >> /tmp/server.properties
mv /tmp/server.properties config/server.properties
cd -

# Install Kafka Service
cat > /lib/systemd/system/kafka.service << EOF
[Unit]
Description=Apache Kafka server (broker)
Documentation=http://kafka.apache.org/documentation.html
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
PIDFile=/var/run/kafka.pid
User=kafka
Group=kafka
ExecStart=/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties
ExecStop=/opt/kafka/bin/kafka-server-stop.sh
Restart=on-failure
SyslogIdentifier=kafka

[Install]
WantedBy=multi-user.target
EOF

# Start Kafka
systemctl start kafka

echo "...:: Setup Kafka - Done ::..."
