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

`$ minikube start`

`$ minikube addons enable ingress`

`$ echo "$(minikube ip) minikube" >> /etc/hosts`

`$ eval $(minikube docker-env)`

`$ ./gradlew clean createDockerImage`

`$ kubectl -f e2e-tests/kubernetes create`


Wait a bit while components are starting...


CONSOLE1:

`$ curl --connect-timeout 600 --max-time 600 minikube/notifier`

(leave waiting for output)


CONSOLE2:

`$ curl minikube/producer/documents -v -H 'Content-Type: application/vnd.kafka-cqrs.create-document.1+json' -d '{"author":"Author1", "text":"Some Text"}'`


CONSOLE3:

`$ curl minikube/reader/documents/83d10005-c6e8-424b-870f-334dd188a19c`


------------

`minikube addons enable ingress`

`eval $(minikube docker-env)`

`./gradlew removeDockerImage`

`./gradlew clean createDockerImage`

`kubectl -f e2e-tests/kubernetes delete`

`kubectl -f e2e-tests/kubernetes create`

`kubectl -f e2e-tests/kubernetes apply`

`kubectl get ingress kafka-cqrs-ingress`

`echo "$(minikube ip) minikube" >> /etc/hosts`

`curl minikube/debugger`
