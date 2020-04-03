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

```
kubens default
``` 
or whichever namespace you want to run your apps in.

Install the RabbitMQ cluster through the provided Helm chart. These can be found in [Bitnami's extensive catalog](https://github.com/bitnami/charts/tree/master/bitnami/rabbitmq).

```
helm install rabbitmq bitnami/rabbitmq --values kubernetes/services/rabbitmq-helm-values.yaml
``` 

#### Redis

Make sure you are targeting the correct Kubernetes namespace:

```
kubens default
``` 
or whichever namespace you want to run your apps in.

Install the Redis cluster through the provided Helm chart.
These can be found in [Bitnami's extensive catalog](https://github.com/bitnami/charts/tree/master/bitnami/redis).
Disable the Sentinel feature for now, since Spring Cloud Stream has had issues connecting to it.

```
helm install redis bitnami/redis --values kubernetes/services/redis-helm-values.yaml
```

### Tanzu Build Service

Tanzu Build Service is a supported, Enterprise-ready version of `kpack` with added features like advanced authentication, management UI and more.
For demo purposes, we'll simply install the version.

