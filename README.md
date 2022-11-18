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
  git version 2.37.0 (Apple Git-136)
  ```

- Java
  ```shell
  java -version
  ```
  ```
  openjdk version "13.0.2" 2020-01-14
  OpenJDK Runtime Environment AdoptOpenJDK (build 13.0.2+8)
  OpenJDK 64-Bit Server VM AdoptOpenJDK (build 13.0.2+8, mixed mode, sharing)
  ```  

- Docker (client only)
  ```shell
  docker version
  ```
  ```
  Client:
   Cloud integration: v1.0.29
   Version:           20.10.17
   API version:       1.41
   Go version:        go1.17.11
   Git commit:        100c701
   Built:             Mon Jun  6 23:04:45 2022
   OS/Arch:           darwin/arm64
   Context:           default
   Experimental:      true
  ...  
  ```  

- Kubernetes
  ```shell
  minikube version
  ```
  ```
  minikube version: v1.28.0
  commit: 986b1ebd987211ed16f8cc10aed7d2c42fc8392f
  ```  

  ```shell
  kubectl version
  ```
  ```
  ...
  Client Version: version.Info{Major:"1", Minor:"25", GitVersion:"v1.25.4", GitCommit:"872a965c6c6526caa949f0c6ac028ef7aff3fb78", GitTreeState:"clean", BuildDate:"2022-11-09T13:28:30Z", GoVersion:"go1.19.3", Compiler:"gc", Platform:"darwin/arm64"}
  ...
  ```  

- Node
  ```shell
  node --version
  ```
  ```
  v14.19.3
  ```  

  ```shell
  npm -version
  ```
  ```
  6.14.17
  ```

- Curl
  ```shell
  curl --version
  ```
  ```
  curl 7.79.1 (x86_64-apple-darwin21.0) libcurl/7.79.1 (SecureTransport) LibreSSL/3.3.6 zlib/1.2.11 nghttp2/1.45.1
  Release-Date: 2021-09-22
  ...
  ```

- Terraform
  ```shell
  terraform -v
  ```
  ```
  Terraform v1.3.4
  on darwin_arm64
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
    ./gradlew clean test docker
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
  kubectl -n ingress-nginx port-forward service/ingress-nginx-controller 8080:80 # Mac (separate console)
  ```

  ```shell
  export API_GATEWAY=127.0.0.1:8080 # Mac
  ```
  ```shell
  export API_GATEWAY=$(minikube ip) # Linux
  ```

  ```shell
  ./gradlew e2eTest --rerun-tasks
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
  kubectl -n ingress-nginx port-forward service/ingress-nginx-controller 8080:80 # Mac (separate console)
  ```

  ```shell
  export API_GATEWAY=127.0.0.1:8080 # Mac
  ```

  ```shell
  export API_GATEWAY=$(minikube ip) # Linux
  ``` 
  
  ```shell
  npm start
  ```
  
  Should take us to http://localhost:8080/


## Develop Backend

Until proper Java9+ support is available in Cassandra libraries used, following needs to be added to Java commandline when running tests from IDE
(unfortunately my current IntelliJ version does not pick it from gradle automatically):

```
--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED
```


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
  cd deployment/environments/minikube-aws-ec2/blue
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
  ./gradlew clean test docker
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

  ```groovy
  test {
      useJUnitPlatform()
      jvmArgs = [
          '--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED',
          '--add-opens=java.base/sun.nio.ch=ALL-UNNAMED',
          '--add-opens=java.base/java.io=ALL-UNNAMED',
          '--add-opens=java.base/java.nio=ALL-UNNAMED',
          '--add-opens=java.base/java.lang=ALL-UNNAMED',
          '--add-opens=java.base/java.util=ALL-UNNAMED',
          '--add-opens=java.base/java.util.concurrent=ALL-UNNAMED',
          '--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED'
      ]
      logging.captureStandardOutput LogLevel.DEBUG
  }
  ```


# TODOs
- Store recent offset on client side (i.e. in cookies), so that it can continue after interruption without loosing messages
- Upgrade to Java 17 once Cassandra Unit is ready
- WebSockets / HTTP/2 Support
- GraalVM + distroless images


# References
- https://thenewstack.io/kubernetes-deployments-work/
- https://github.com/infrabricks/kubernetes-standalone
- https://dzone.com/articles/getting-started-with-spring-data-cassandra
- https://medium.freecodecamp.org/part-1-react-app-from-scratch-using-webpack-4-562b1d231e75
- https://medium.com/@andyccs/webpack-and-docker-for-development-and-deployment-ae0e73243db4
