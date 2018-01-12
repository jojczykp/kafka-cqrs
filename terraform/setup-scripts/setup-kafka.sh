#!/usr/bin/env bash

scala_version=2.12
kafka_version=1.0.0

broker_id=$1
number_of_partitions=8
replication_factor=3


# Download
wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/${kafka_version}/kafka_${scala_version}-${kafka_version}.tgz -O kafka.tgz

# Extract
tar xvf kafka.tgz -C /opt
mv /opt/kafka_${scala_version}-${kafka_version} /opt/kafka
rm kafka.tgz

# Setup user and group
useradd kafka -U
chown -R kafka /opt/kafka
chgrp -R kafka /opt/kafka


##### Zookeeper #####

# Configure zookeeper
cd /opt/kafka
cat config/zookeeper.properties \
    | sed "s|broker.id=0|broker.id=${broker_id}|" \
    | sed "s|num.partitions=1|num.partitions=${number_of_partitions}|" \
    > /tmp/zookeeper.properties
echo >> /tmp/zookeeper.properties
mv /tmp/zookeeper.properties config/zookeeper.properties
cd -

# Install zookeeper service
cat > /lib/systemd/system/zookeeper.service << EOF
[Unit]
Description=Apache Zookeeper server
Documentation=http://kafka.apache.org/documentation.html
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
PIDFile=/var/run/zookeeper.pid
User=kafka
Group=kafka
ExecStart=/opt/kafka/bin/zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties
ExecStop=/opt/kafka/bin/zookeeper-server-stop.sh
Restart=on-failure
SyslogIdentifier=zookeeper

[Install]
WantedBy=multi-user.target
EOF

# Start zookeeper
systemctl start zookeeper


##### Kafka #####

# Configure kafka
cd /opt/kafka
cat config/server.properties \
    | sed "s|broker.id=0|broker.id=${broker_id}|" \
    | sed "s|num.partitions=1|num.partitions=${number_of_partitions}|" \
    > /tmp/server.properties
echo >> /tmp/server.properties
echo " " >> /tmp/server.properties
echo "# replication factor" >> /tmp/server.properties
echo "default.replication.factor=${replication_factor}" >> /tmp/server.properties
mv /tmp/server.properties config/server.properties
cd -

# Install kafka service
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

# Start kafka
systemctl start kafka
