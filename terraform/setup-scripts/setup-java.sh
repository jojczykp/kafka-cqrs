#!/usr/bin/env bash

echo "...:: Setup Java ::..."

apt-get update
apt-get install -y default-jre
java -version

echo "...:: Setup Java - Done ::..."
