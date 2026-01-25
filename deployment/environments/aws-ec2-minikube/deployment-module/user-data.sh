#! /bin/bash -e


trap "set +x; echo ===== EXIT User Data script =====" EXIT


# Captured to /var/log/cloud-init-output.log
exec | logger -t user-data -s > /dev/console 2>&1


echo "===== Environment ====="
set -x
whoami
pwd
export HOME=/root
printenv
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
apt-get install docker.io conntrack openjdk-21-jdk -y
apt-get clean
curl -sL https://deb.nodesource.com/setup_25.x | sudo -E bash -
sudo apt-get install -y nodejs
docker version
java -version
node --version
npm -version
systemctl enable docker.service
systemctl start docker.service
set +x


echo "===== Add runner user ====="
set -x
adduser --disabled-password --gecos "" runner
usermod -aG docker runner
set +x


echo "===== Install kubectl ====="
set -x
curl -LO https://dl.k8s.io/release/v1.35.0/bin/linux/amd64/kubectl
chmod +x ./kubectl
mv ./kubectl /usr/local/bin/kubectl
kubectl version || true
set +x


echo "===== Install minikube ====="
set -x
curl -Lo minikube https://storage.googleapis.com/minikube/releases/v1.37.0/minikube-linux-amd64
chmod +x minikube
mv minikube /usr/local/bin/
minikube version
set +x


echo "===== Start minikube ====="
sudo -u runner -i <<EOF
    set -xe
    minikube start --vm-driver=docker
    minikube addons enable ingress
EOF


echo "===== Wait for ingress ready ====="
sudo -u runner -i <<EOF
    set -xe
    date
    kubectl wait \
      --namespace ingress-nginx \
      --for=condition=ready pod \
      --selector=app.kubernetes.io/component=controller \
      --timeout=120s
    date
    kubectl wait \
      --namespace ingress-nginx \
      --for=condition=complete job/ingress-nginx-admission-create \
      --timeout=120s
    date
    kubectl wait \
      --namespace ingress-nginx \
      --for=condition=complete job/ingress-nginx-admission-patch \
      --timeout=120s
    date
EOF

echo "===== Build ====="
set -x
sudo -u runner -i <<'EOF'
    set -xe
    git clone https://github.com/jojczykp/kafka-cqrs.git --branch master --single-branch
    cd kafka-cqrs
    eval $(minikube -p minikube docker-env)
    ./gradlew --no-daemon --console=plain buildDockerImage
    docker images | grep kafka-cqrs
EOF
set +x


echo "===== Deploy application ====="
sudo -u runner -i <<EOF
    set -xe
    kubectl -f /home/runner/kafka-cqrs/deployment/kubernetes apply --recursive
    date
    kubectl wait deployment --for=condition=available -l app=kafka-cqrs --timeout=600s
    date
    kubectl get pod -l app=kafka-cqrs
EOF

# Very slow on t3a.small
echo "===== Sanity check / Warm-up ====="
set -x
sudo -u runner -i <<EOF
    set -xe
    cd kafka-cqrs
    export API_GATEWAY=localhost
    ./gradlew --no-daemon --console=plain e2eTest --rerun-tasks
EOF
set +x


echo "===== Release some disk space ====="
sudo -u runner -i <<EOF
    set -xe
    minikube ssh -- docker system prune -f
    docker system prune -f
EOF
set -x
rm -rf /home/runner/.gradle
rm -rf /home/runner/.npm
rm -rf /home/runner/kafka-cqrs/gui-service/node_modules
rm -rf /var/lib/apt/lists/*
set +x
df


echo "===== Done ====="
