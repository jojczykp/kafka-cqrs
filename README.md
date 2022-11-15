Event Sourcing CQRS Microservices application with SSE Web Push Notifications on top of Kubernetes with Kafka and Cassandra

Online: https://kafka-cqrs.alterbit.org

# Architecture

Single EC2 Demo Deployment

![Image of Architecture](doc/arch.svg)

Once demo up and running, shows data flow between microservices and traffic details (CRUD + Push Notifications).

![Image of Demo](doc/demo.png)


# Prerequisites

- Git
  ```shell
  git version
  ```
  ```
  git version 2.31.1
  ```

- Java
  ```shell
  java -version
  ```
  ```
  java version "11.0.1" 2018-10-16 LTS
  Java(TM) SE Runtime Environment 18.9 (build 11.0.1+13-LTS)
  Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.1+13-LTS, mixed mode)
  ```  

- Docker (client only)
  ```shell
  docker version
  ```
  ```
  Client:
   Version:           20.10.9
   API version:       1.41
   Go version:        go1.16.8
   Git commit:        c2ea9bc
   Built:             Sun Oct 10 22:41:14 2021
   OS/Arch:           linux/amd64
   Context:           default
   Experimental:      true
  ...  
  ```  

- Kubernetes
  ```shell
  minikube version
  ```
  ```
  minikube version: v1.23.2
  commit: 0a0ad764652082477c00d51d2475284b5d39ceed
  ```  

  ```shell
  kubectl version
  ```
  ```
  Client Version: version.Info{Major:"1", Minor:"22", GitVersion:"v1.22.2", GitCommit:"8b5a19147530eaac9476b0ab82980b4088bbc1b2", GitTreeState:"clean", BuildDate:"2021-09-15T21:38:50Z", GoVersion:"go1.16.8", Compiler:"gc", Platform:"linux/amd64"}
  ```  

- Node
  ```shell
  node --version
  ```
  ```
  v14.18.0
  ```  

  ```shell
  npm -version
  ```
  ```
  6.14.15
  ```

- Curl
  ```shell
  curl --version
  ```
  ```
  curl 7.76.1 (x86_64-redhat-linux-gnu) ...
  Release-Date: 2021-04-14
  ```

- Terraform
  ```shell
  terraform -v
  ```
  ```
  Terraform v1.0.0
  on linux_amd64
  ```


# Run Steps

## Start Minikube

- Start minikube
  ```shell
  minikube start
  ```

- Enable minikube promiscuous mode (minikube issue workaround)

  ```shell
  minikube ssh sudo ip link set docker0 promisc on
  ```

- Enable ingress

  ```shell
  minikube addons enable ingress
  ```


## Clone

  ```shell
  git clone https://github.com/jojczykp/kafka-cqrs.git
  ```

  ```shell
  cd kafka-cqrs
  ```


## Build

  - Before any docker operation, make sure switched to repository inside minikube
    
    ```shell
    eval $(minikube docker-env)
    ```
  
  - Build and upload image to docker repository
  
    ```shell
    ./gradlew clean docker
    ````
    
    First run may take longer as docker downloads base images.


## Start

  ```shell
  kubectl -f deployment/kubernetes apply --recursive
  ```

  ```shell
  kubectl wait deployment --for=condition=available -l app=kafka-cqrs --timeout=600s
  ```


## Test

  ```shell
  minikube tunnel # Mac
  ```

  ```shell
  export API_GATEWAY=127.0.0.1 # Mac
  ```
  ```shell
  export API_GATEWAY=$(minikube ip) # Linux
  ```

  ```shell
  ./gradlew e2e-tests:test --rerun-tasks
  ```
  

## Try Web UI

  ```shell
  open http://${API_GATEWAY}/gui/
  ```
  
  Note that "Copy to Clipboard (📋)" button works only when accessing page via https or at localhost.


## Try CLI

- **CONSOLE 1** (listen to data change events):

  ```shell
  curl -v http://${API_GATEWAY}/notifier/documents
  ```

  Keep watching output...


- **CONSOLE 2** (create some data)

  ```shell
  curl -v http://${API_GATEWAY}/producer/documents -H 'Content-Type: application/vnd.kafka-cqrs.create-document.1+json' -d '{"author":"Author1", "text":"Some Text"}'
  ```


- **CONSOLE 3** (read persistent data)

  ```shell
  curl -v http://${API_GATEWAY}/reader/documents/[document-id (payload.id) from CONSOLE1]
  ```


## Develop UI

  ```shell
  cd gui-service
  ```
  
  ```shell
  npm install
  ```

  ```shell
  minikube tunnel # Mac
  ```

  ```shell
  export API_GATEWAY=127.0.0.1 # Mac
  ```

  ```shell
  export API_GATEWAY=$(minikube ip) # Linux
  ``` 
  
  ```shell
  npm start
  ```
  
  Should take us to http://localhost:8080/


## Develop Backend

Until proper Java11 support is available in Cassandra libraries used, following needs to be added to Java commandline when running tests from IDE
(unfortunately my current IntelliJ version does not pick it from gradle automatically):

  `--add-exports java.base/jdk.internal.ref=ALL-UNNAMED`


## Shutdown

  ```shell
  kubectl -f deployment/kubernetes delete --recursive
  ```

  ```shell
  kubectl wait deployment --for=delete -l app=kafka-cqrs
  ```
  
  
## Cleanup
  
  ```shell
  eval $(minikube docker-env)
  ```

  ```shell
  ./gradlew clean dockerRemoveImage
  ```

  ```shell
  minikube delete
  ```


## Run in AWS EC2

  ```shell
  cd deployment/environments/minikube-aws-ec2
  ```

  ```shell
  terraform init
  ```

  ```shell
  terraform apply
  ```
  
  Above will take about 15 minutes and start EC2, clone, build and start application on it.
  
  Use command printed as terraform output to tail log from this process (use your aws key file), and URL to access GUI
  once up and running.

  In order to run e2e tests on newly created instance:

  ```shell
  export API_GATEWAY=<public_ip>
  ```

  ```shell
  ./gradlew e2e-tests:test --rerun-tasks
  ```

  To drop EC2:
  
  ```shell
  terraform destroy
  ```

------------

# Cheat sheet

  ```shell
  ./gradlew dockerRemoveImage
  ```

  ```shell
  ./gradlew clean dockerBuildImage
  ```

  ```shell
  kubectl get ingress kafka-cqrs-ingress
  ```

  ```shell
  minikube start && minikube ssh sudo ip link set docker0 promisc on
  ```

  ```shell
  minikube ssh
  ```

  ```shell
  minikube dashboard
  ```

  ```shell
  curl http://$(minikube ip)/debugger
  ```

  ```shell
  kubectl exec -it $(kubectl get pods -o name -l service=debugger-service | cut -d'/' -f2) sh
  ```

  ```shell
  kubectl exec -it $(kubectl get pods -o name -l service=kafka-service | cut -d'/' -f2) bash
  ```

  ```shell
  kafka-console-producer.sh --broker-list kafka-service:9092 --topic documents.t
  ```

  ```shell
  kafka-console-consumer.sh --bootstrap-server kafka-service:9092 --topic documents.t
  ```

  ```shell
  kubectl logs -f $(kubectl get pods -o name -l service=kafka-service | cut -d'/' -f2)
  ```

  ```shell
  kubectl -n kube-system logs -f $(kubectl -n kube-system get pods -o name -l app=nginx-ingress-controller | cut -d'/' -f2)
  ```

  ```shell
  kubectl exec -it $(kubectl get pods -o name | grep kafka-cqrs-zookeeper-service | cut -d'/' -f2) zkCli.sh
  ```

  ```shell
  kubectl exec -it $(kubectl get pods -o name | grep kafka-cqrs-cassandra-service | cut -d'/' -f2) cqlsh
  select * from documents.documents;
  ```

  ```shell
  test {
      jvmArgs '--add-exports', 'java.base/jdk.internal.ref=ALL-UNNAMED'
      logging.captureStandardOutput LogLevel.DEBUG
  }
  ```


# TODOs
- Store recent offset on client side (i.e. in cookies), so that it can continue after interruption without loosing messages
- Upgrade other elements so that no Java 11/17 TODOs left
- HTTP/2 Support


# References
- https://thenewstack.io/kubernetes-deployments-work/
- https://github.com/infrabricks/kubernetes-standalone
- https://dzone.com/articles/getting-started-with-spring-data-cassandra
- https://medium.freecodecamp.org/part-1-react-app-from-scratch-using-webpack-4-562b1d231e75
- https://medium.com/@andyccs/webpack-and-docker-for-development-and-deployment-ae0e73243db4
