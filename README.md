Event Sourcing CQRS Microservices application with SSE Web Push Notifications on top of Kubernetes with Kafka and Cassandra

# Prerequisites
- JDK 11
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

# Build Cassandra with JDK11 support

  Step to be removed once official release supporting JDK 11 is available.

  Have maven and ant installed. Then clone, build and install `cassandra-all` in local maven repo:
  
  `$ git clone https://github.com/jojczykp/cassandra.git --single-branch --branch missing-hppc-transitive-dependency-in-produced-pom`
  
  `$ cd cassandra`
  
  `$ ant mvn-install`

# Build, Run and Manual e2e test (TODO: automate)

- Make sure minikube VM has enough resources (I used 3CPU cores, 12GB RAM)

- Start minikube

  `$ minikube start`

- If the following minikube issues are not fixed and you use iptables proxy (default):

  - https://github.com/kubernetes/kubernetes/issues/20475

  - https://github.com/cloudfoundry-incubator/kubo-release/issues/212

  This needs to be executed as workaround (minikube console):

  `minikube ssh`
  
  `sudo ip link set docker0 promisc on`

- Continue with env configuration

  `$ minikube addons enable ingress`

  `$ sudo echo "$(minikube ip) minikube.local" >> /etc/hosts` (if not present yet)

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

`$ kubectl exec -it $(kubectl get pods -o name -l service=debugger-service | cut -d'/' -f2) sh`

`$ kubectl exec -it $(kubectl get pods -o name -l service=kafka-service | cut -d'/' -f2) bash`

`# kafka-console-producer.sh --broker-list kafka-service:9092 --topic documents.t`

`# kafka-console-consumer.sh --bootstrap-server kafka-service:9092 --topic documents.t`

`$ kubectl logs -f $(kubectl get pods -o name -l service=kafka-service | cut -d'/' -f2)`

`$ kubectl -n kube-system logs -f $(kubectl -n kube-system get pods -o name -l app=nginx-ingress-controller | cut -d'/' -f2)`

`$ kubectl exec -it $(kubectl get pods -o name | grep kafka-cqrs-zookeeper-service | cut -d'/' -f2) zkCli.sh`

`$ kubectl exec -it $(kubectl get pods -o name | grep kafka-cqrs-cassandra-service | cut -d'/' -f2) cqlsh`

`> select * from documents.documents;`

```
test {
    jvmArgs '--add-exports', 'java.base/jdk.internal.ref=ALL-UNNAMED'
    logging.captureStandardOutput LogLevel.DEBUG
}
```
