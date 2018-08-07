Event Sourcing CQRS Microservices app with Web Push Notifications on top of Kafka, Cassandra 

# Prerequisites
- AWS account (https://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)
- Terraform (https://www.terraform.io/intro/getting-started/install.html)
- Docker (https://www.docker.com/)
- Kubernetes (https://kubernetes.io/)
- Minikube (with `minikube addons enable ingress`)

# WIP
- Fix Kagka connectivity issue
- EKS deployment
- Use of J9 embedded HttpServer in IT

# References
- https://thenewstack.io/kubernetes-deployments-work/
- https://github.com/infrabricks/kubernetes-standalone
- https://dzone.com/articles/getting-started-with-spring-data-cassandra


# Manual e2e test (TODO: automate)

If not done yet (from root project folder):

Make sure minikube VM has enough resources (I used 4CPU, 16GB RAM)

`$ minikube start`

If the following minikube issues are not fixed and you use iptables proxy (default):

- https://github.com/kubernetes/kubernetes/issues/20475

- https://github.com/cloudfoundry-incubator/kubo-release/issues/212

This needs to be executed as workaround (minikube console):

`sudo ip link set docker0 promisc on`

Continue:

`$ minikube addons enable ingress`

`$ echo "$(minikube ip) minikube" >> /etc/hosts`

`$ eval $(minikube docker-env)`

`$ ./gradlew clean createDockerImage`

`$ kubectl -f e2e-tests/kubernetes create`


Wait a bit while components are starting...


CONSOLE1:

`$ curl http://minikube/notifier` --connect-timeout 600 --max-time 600

(leave waiting for output)


CONSOLE2:

`$ curl http://minikube/producer/documents -v -H 'Content-Type: application/vnd.kafka-cqrs.create-document.1+json' -d '{"author":"Author1", "text":"Some Text"}'`


CONSOLE3:

`$ curl http://minikube/reader/documents/[document-id from CONSOLE1]`

------------


`./gradlew removeDockerImage`

`./gradlew clean createDockerImage`

`kubectl -f e2e-tests/kubernetes apply`

`kubectl get ingress kafka-cqrs-ingress`

`curl http://minikube/debugger`
