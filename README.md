![Tanzu](tanzu-logo.png)

# VMware Tanzu Demo

Demo material for developers on [VMware Tanzu](https://tanzu.vmware.com/).

Consists of three [Spring Boot](https://spring.io) microservices, communicating over [RabbitMQ](), backed by a Redis key value store, all running on Kubernetes.
When source code changes, the applications are rebuilt using [Build Service](https://tanzu.vmware.com/build-service) and redeployed to [Enterprise PKS](https://cloud.vmware.com/vmware-enterprise-pks) using [Concourse](https://tanzu.vmware.com/concourse).

Both platform and applications are managed using [Mission Control](https://tanzu.vmware.com/mission-control) and monitored using [Observability by Wavefront](https://tanzu.vmware.com/observability).

## Use Case - Payment Application

## Installation

### Data Services

#### RabbitMQ

Make sure you are targeting the correct Kubernetes namespace:

```bash
kubens default
``` 
or whichever namespace you want to run your apps in.

Install the RabbitMQ cluster through the provided Helm chart. These can be found in [Bitnami's extensive catalog](https://github.com/bitnami/charts/tree/master/bitnami/rabbitmq).

```bash
helm install rabbitmq bitnami/rabbitmq --values kubernetes/services/rabbitmq-helm-values.yaml
``` 

#### Redis

Make sure you are targeting the correct Kubernetes namespace:

```bash
kubens default
``` 
or whichever namespace you want to run your apps in.

Install the Redis cluster through the provided Helm chart.
These can be found in [Bitnami's extensive catalog](https://github.com/bitnami/charts/tree/master/bitnami/redis).
Disable the Sentinel feature for now, since Spring Cloud Stream has had issues connecting to it.

```
helm install redis bitnami/redis --values kubernetes/services/redis-helm-values.yaml
```

### Tanzu Build Service (kpack)

Tanzu Build Service is a supported, Enterprise-ready version of `kpack` with added features like advanced authentication, management UI and more.
For demo purposes, we'll simply install the opensource version `kpack`.

Navigate to the `kubernetes/cicd/kpack` folder and execute the YAML files one by one:

```bash
kubectl apply -f 01-kpack-release-0.0.8.yaml 
kubectl apply -f 02-github-creds.yml
kubectl apply -f 03-dockerhub-creds.yml
kubectl apply -f 04-custom-java-builder.yml
```

### Concourse

Installing Concourse is, just like the data services, done through a Helm chart.

```bash
helm install concourse concourse/concourse -f concourse-helm-values.yaml
```

Once the pods and services are running, find the public LoadBalancer IP of the Concourse webapp.
Configure your DNS by adding an A Record linking the Concourse FQDN to that IP.

Then try to login to Concourse through the Web UI or by using the `fly` CLI:

```bash
fly login -t concourse -c http://concourse.beijing.cf-app.com -k -u USERNAME -p USERNAME
```

