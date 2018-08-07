Event Sourcing CQRS Microservices application with SSE Web Push Notifications on top of Kubernetes with Kafka and Cassandra

# Prerequisites
- Java8
- Kubernetes (https://kubernetes.io/)
- Minikube (with dns and ingress enabled)
- Docker (https://www.docker.com/ - no need for demon, just client to connect to server in minikube)

# WIP
- Finish e2e tests automation
- Switch to Java10
- EKS deployment

# References
- https://thenewstack.io/kubernetes-deployments-work/
- https://github.com/infrabricks/kubernetes-standalone
- https://dzone.com/articles/getting-started-with-spring-data-cassandra


# Manual e2e test (TODO: automate)

- Make sure minikube VM has enough resources (I used 3CPU cores, 12GB RAM)

- Start minikube

  `$ minikube start`

- If the following minikube issues are not fixed and you use iptables proxy (default):

  - https://github.com/kubernetes/kubernetes/issues/20475

  - https://github.com/cloudfoundry-incubator/kubo-release/issues/212

  This needs to be executed as workaround (minikube console):

  `sudo ip link set docker0 promisc on`

- Continue with env configuration

  `$ minikube addons enable ingress`

  `$ sudo echo "$(minikube ip) minikube.local" >> /etc/hosts`

  `$ eval $(minikube docker-env)`

  `$ ./gradlew clean createDockerImage`

  `$ kubectl -f e2e-tests/kubernetes create`


- Wait a bit while components are starting...

- Test:

  - **CONSOLE 1** (listen to data change events):

    `$ curl -v http://minikube.local/notifier`

    Leave waiting for output...


  - **CONSOLE 2** (create some data)

    `$ curl -v http://minikube.local/producer/documents -H 'Content-Type: application/vnd.kafka-cqrs.create-document.1+json' -d '{"author":"Author1", "text":"Some Text"}'`


  - **CONSOLE 3** (read persistent data)

    `$ curl -v http://minikube.local/reader/documents/[document-id from CONSOLE1]`

------------

# Other useful commands

`$ ./gradlew removeDockerImage`

`$ ./gradlew clean createDockerImage`

`$ kubectl -f e2e-tests/kubernetes apply`

`$ kubectl get ingress kafka-cqrs-ingress`

`$ minikube ssh`

`$ minikube dashboard`

`$ curl http://minikube.local/debugger`

`$ docker exec -it $(docker ps | grep kafka-cqrs-debugger-service | head -n 1 | cut -f1 -d' ') sh`

`$ docker exec -it $(docker ps | grep kafka-cqrs-kafka-service | head -n 1 | cut -f1 -d' ') bash`

`$ docker logs -f $(docker ps | grep kafka-cqrs-kafka-service | head -n 1 | cut -f1 -d' ')`

`$ docker exec -it $(docker ps | grep kafka-cqrs-zookeeper-service | head -n 1 | cut -f1 -d' ') zkCli.sh`

`> select * from documents.documents;`

`$ kafka-console-producer.sh --broker-list kafka-service:9092 --topic documents.t`

`$ kafka-console-consumer.sh --bootstrap-server kafka-service:9092 --topic documents.t`
