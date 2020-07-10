![Account Service CI](https://github.com/Turbots/tanzu-demo/workflows/Account%20Service%20CI/badge.svg)

![Confirmation Service CI](https://github.com/Turbots/tanzu-demo/workflows/Confirmation%20Service%20CI/badge.svg)

![Payment Service CI](https://github.com/Turbots/tanzu-demo/workflows/Payment%20Service%20CI/badge.svg)

![Tanzu](tanzu-logo.png)

# VMware Tanzu Demo

Demo material for developers on [VMware Tanzu](https://tanzu.vmware.com/).

Consists of three [Spring Boot](https://spring.io) microservices, communicating over [RabbitMQ](), backed by a Redis key value store, all running on Kubernetes.
When source code changes, the applications are rebuilt using [Build Service](https://tanzu.vmware.com/build-service) and redeployed to [Enterprise PKS](https://cloud.vmware.com/vmware-enterprise-pks) using ArgoCD GitOps.

Both Kubernetes clusters are managed using [Mission Control](https://tanzu.vmware.com/mission-control) and both infrastructure, Kubernetes, applications and services are monitored using [Observability by Wavefront](https://tanzu.vmware.com/observability).

## Use Case - Banking Application

#### Application Architecture

![Tanzu](app-architecture.png)

#### Deployment Diagram

![Tanzu](deployment-diagram.png)

#### Continuous Delivery Diagram

![Tanzu](dev-workflow.png)

## Installation

First copy the `config.template.yml` to config.yml and fill in the values.

Then follow the instructions in the `setup_all.sh` script to install everything from scratch.

Alternatively, install the different components by running the various setup scripts.

- Install kpack using `01-install-kpack.sh`.
- Install argocd using `02-install-argocd.sh`.
- Configure the credentials for the various services in `03-configure-credentials.sh`.
- Configure the GitOps resources using `03-configure-gitops.sh`.
