#! /bin/bash -e


trap "set +x; echo ===== EXIT User Data script =====" EXIT


# Captured to /var/log/cloud-init-output.log
exec | logger -t user-data -s > /dev/console 2>&1


echo "===== Environment ====="
set -x
whoami
pwd
printenv
export HOME=/root
set +x


echo "===== Setup swap ====="
set -x
dd if=/dev/zero of=/swapfile bs=128M count=16  # 2GiB
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile
swapon -s | grep swapfile
echo "/swapfile swap swap defaults 0 0" >> /etc/fstab
set +x


echo "===== Install dependencies ====="
set -x
apt-get update
apt-get install docker.io conntrack openjdk-13-jdk -y
apt-get clean
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get install -y nodejs
docker version
java -version
node --version
npm -version
systemctl enable docker.service
systemctl start docker.service
set +x


echo "===== Install kubectl ====="
set -x
curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.22.2/bin/linux/amd64/kubectl
chmod +x ./kubectl
mv ./kubectl /usr/local/bin/kubectl
kubectl version || true
set +x


echo "===== Install minikube ====="
set -x
curl -Lo minikube https://storage.googleapis.com/minikube/releases/v1.23.2/minikube-linux-amd64
chmod +x minikube
mv minikube /usr/local/bin/
minikube version
set +x


echo "===== Build ====="
set -x
adduser --disabled-password --gecos "" builder
usermod -aG docker builder
sudo -u builder -i <<EOF
    set -xe
    git clone https://github.com/jojczykp/kafka-cqrs.git --branch master --single-branch
    cd kafka-cqrs
    ./gradlew --no-daemon --console=plain docker
    docker images | grep kafka-cqrs
EOF
set +x


echo "===== Start minikube ====="
set -x
minikube start --vm-driver=none
minikube addons enable ingress
set +x


echo "===== Wait for ingress to be ready ====="
set -x
kubectl wait pod \
  --namespace ingress-nginx  \
  --for=condition=ready \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s
set +x


echo "===== Deploy application ====="
set -x
kubectl -f /home/builder/kafka-cqrs/deployment/kubernetes apply --recursive
kubectl wait deployment --for=condition=available -l app=kafka-cqrs --timeout=600s
kubectl get pod -l app=kafka-cqrs
set +x

# Very slow on t3a.small
echo "===== Sanity check / Warm-up ====="
set -x
sudo -u builder -i <<EOF
    set -xe
    cd kafka-cqrs
    export API_GATEWAY=localhost
    ./gradlew --no-daemon --console=plain e2eTest --rerun-tasks
EOF
set +x


echo "===== Release some disk space ====="
set -x
rm -rf /home/builder/.gradle
rm -rf /home/builder/.npm
rm -rf /home/builder/kafka-cqrs/gui-service/node_modules
rm -rf /var/lib/apt/lists/*
set +x


echo "===== Done ====="
