#!/usr/bin/env bash


echo "...:: Setup Zookeeper ::..."

# Install Zookeeper Service
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

# Start Zookeeper
systemctl start zookeeper


echo "...:: Setup Zookeeper - Done ::..."
