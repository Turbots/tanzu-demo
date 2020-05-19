![Tanzu](tanzu-logo.png)

# VMware Tanzu Demo

Demo material for developers on [VMware Tanzu](https://tanzu.vmware.com/).

Consists of three [Spring Boot](https://spring.io) microservices, communicating over [RabbitMQ](), backed by a Redis key value store, all running on Kubernetes.
When source code changes, the applications are rebuilt using [Build Service](https://tanzu.vmware.com/build-service) and redeployed to [Enterprise PKS](https://cloud.vmware.com/vmware-enterprise-pks) using ArgoCD GitOps.

Both Kubernetes clusters are managed using [Mission Control](https://tanzu.vmware.com/mission-control) and both infrastructure, Kubernetes, applications and services are monitored using [Observability by Wavefront](https://tanzu.vmware.com/observability).

## Use Case - Banking Application

### Application Architecture

### Deployment Diagram

### Continuous Delivery Diagram

## Installation

Follow the instructions in the `setup.sh` script.

