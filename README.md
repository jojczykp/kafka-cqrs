Event Sourcing CQRS Microservices application with SSE Web Push Notifications on top of Kubernetes with Kafka and Cassandra

Online: https://kafka-cqrs.alterbit.org

# Architecture

Once demo up and running, shows data flow between microservices and traffic details (CRUD + Push Notifications).

![Image of Demo](doc/demo.png)


# Prerequisites

- Git
  ```
  $ git version
  git version 2.25.1
  ```

- Java
  ```
  $ java -version
  java version "11.0.1" 2018-10-16 LTS
  Java(TM) SE Runtime Environment 18.9 (build 11.0.1+13-LTS)
  Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.1+13-LTS, mixed mode)
  ```  

- Docker (client only)
  ```
  $ docker version
     Client:
      Version:           19.03.11
      API version:       1.40
      Go version:        go1.13.11
      Git commit:        42e35e6
      Built:             Sun Jun  7 21:18:14 2020
      OS/Arch:           linux/amd64
      Experimental:      false
  ```  

- Kubernetes
  ```
  $ minikube version
  minikube version: v1.11.0
  commit: 57e2f55f47effe9ce396cea42a1e0eb4f611ebbd
  ```  

  ```
  $ kubectl version
  Client Version: version.Info{Major:"1", Minor:"18", GitVersion:"v1.18.5", GitCommit:"e6503f8d8f769ace2f338794c914a96fc335df0f", GitTreeState:"clean", BuildDate:"2020-06-26T03:47:41Z", GoVersion:"go1.13.9", Compiler:"gc", Platform:"linux/amd64"}
  ```  

- Node
  ```
  $ node --version
  v12.16.3
  ```  

  ```
  $ npm -version
  6.14.4
  ```

- Curl
  ```
  $ curl --version
  curl 7.66.0 (x86_64-redhat-linux-gnu)
  Release-Date: 2019-09-11
  ```

- Terraform
  ```
  $ terraform -v
  Terraform v0.12.28
  ```


# Run Steps

## Start Minikube

- Start minikube

  `$ minikube start`

- Enable minikube promiscuous mode (minikube issue workaround)

  `$ minikube ssh sudo ip link set docker0 promisc on`

- Enable ingress

  `$ minikube addons enable ingress`


## Clone

  `$ git clone https://github.com/jojczykp/kafka-cqrs.git`

  `$ cd kafka-cqrs`


## Build

  - Before any docker operation, make sure switched to repository inside of minikube
    
    `$ eval $(minikube docker-env)`
  
  - Build and upload image to docker repository
  
    `$ ./gradlew clean dockerBuildImage`
    
    First run may take longer as docker downloads base images.


## Start

  `$ kubectl -f deployment/kubernetes apply --recursive`

  `$ kubectl wait deployment --for=condition=available -l app=kafka-cqrs --timeout=600s`


## Test

  `$ export API_GATEWAY=$(minikube ip)`

  `$ ./gradlew e2e-tests:test --rerun-tasks`
  

## Try Web UI

  `$ google-chrome http://$(minikube ip)/gui`
  
  Note that "Copy to Clipboard (📋)" button works only when accessing page via https.


## Try CLI

- **CONSOLE 1** (listen to data change events):

  `$ curl -v http://$(minikube ip)/notifier/documents`

  Keep watching output...


- **CONSOLE 2** (create some data)

  `$ curl -v http://$(minikube ip)/producer/documents -H 'Content-Type: application/vnd.kafka-cqrs.create-document.1+json' -d '{"author":"Author1", "text":"Some Text"}'`


- **CONSOLE 3** (read persistent data)

  `$ curl -v http://$(minikube ip)/reader/documents/[document-id (payload.id) from CONSOLE1]`


## Develop UI

  `$ cd gui-service`
  
  `$ npm install`
  
  `$ export API_GATEWAY=$(minikube ip)`
  
  `$ npm start`
  
  Should take us to http://localhost:8080/


## Develop Backend

Until proper Java11 support is available in Cassandra libraries used, following needs to be added to Java commandline when running tests from IDE
(unfortunately my current IntelliJ version does not pick it from gradle automatically):

  `--add-exports java.base/jdk.internal.ref=ALL-UNNAMED`


## Shutdown

  `kubectl -f deployment/kubernetes delete --recursive`

  `kubectl wait deployment --for=delete -l app=kafka-cqrs`
  
  
## Cleanup
  
  `$ eval $(minikube docker-env)`

  `$ ./gradlew clean dockerRemoveImage`

  `$ minikube delete`


## Run in AWS EC2

  `$ cd deployment/environments/minikube-aws-ec2`

  `$ terraform apply`
  
  Above will take about 15 minutes and start EC2, clone, build and start application on it.
  
  Use command printed as terraform output to tail log from this process (use your aws key file), and URL to access GUI
  once up and running.

  To drop EC2:
  
  `$ terraform destroy`

------------

# Cheat sheet

`$ ./gradlew dockerRemoveImage`

`$ ./gradlew clean dockerBuildImage`

`$ kubectl get ingress kafka-cqrs-ingress`

`$ minikube start && minikube ssh sudo ip link set docker0 promisc on`

`$ minikube ssh`

`$ minikube dashboard`

`$ curl http://$(minikube ip)/debugger`

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


# TODOs
- Store recent offset on client side (i.e. in cookies), so that it can continue after interruption without loosing messages
- Upgrade Cassandra to version supporting Java 11
- Upgrade other elements so that no Java 11 TODOs left


# References
- https://thenewstack.io/kubernetes-deployments-work/
- https://github.com/infrabricks/kubernetes-standalone
- https://dzone.com/articles/getting-started-with-spring-data-cassandra
- https://medium.freecodecamp.org/part-1-react-app-from-scratch-using-webpack-4-562b1d231e75
- https://medium.com/@andyccs/webpack-and-docker-for-development-and-deployment-ae0e73243db4
