#!/usr/bin/env bash

CICD_CLUSTER=cicd-cluster
TARGET_CLUSTER=dev-cluster
echo 'Setting up ArgoCD with CICD_CLUSTER ['${CICD_CLUSTER}'] and TARGET_CLUSTER ['${TARGET_CLUSTER}']'

kubectx ${CICD_CLUSTER}

echo 'Creating ArgoCD namespace'
kubectl create namespace argocd

echo 'Installing ArgoCD...'
kubectl -n argocd apply -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

echo 'Fetching a load balanced IP for the ArgoCD server...'
kubectl -n argocd patch svc argocd-server -p '{"spec": {"type": "LoadBalancer"}}'

ARGOCD_USERNAME=admin
ARGOCD_PASSWORD=`kubectl -n argocd get pods -l app.kubernetes.io/name=argocd-server -o name | cut -d'/' -f 2`
ARGOCD_IP=`kubectl -n argocd get svc/argocd-server -o json | jq '.status.loadBalancer.ingress[0].ip' -j`

echo 'You can now login to ArgoCD using '$fg[green]'"argocd login $ARGOCD_IP --name argocd --username $ARGOCD_USERNAME --password $ARGOCD_PASSWORD --insecure"'
argocd login ${ARGOCD_IP} --name argocd --username ${ARGOCD_USERNAME} --password ${ARGOCD_PASSWORD} --insecure

echo 'Setting up ArgoCD cluster for the current Kubernetes context ("'${TARGET_CLUSTER}'" or current cluster when empty)'
kubectx ${TARGET_CLUSTER}
argocd cluster add `kubectx -c`

echo 'Setting up Wavefront token...'
kubectl -n default apply -f wavefront-token.yaml

echo 'Switching back to CI/CD cluster'
kubectx ${CICD_CLUSTER}

echo 'Configuring data services...'
kubectl -n argocd apply -f rabbit.yaml
kubectl -n argocd apply -f redis.yaml

echo 'Configuring applications...'
kubectl -n argocd apply -f account-service.yaml
kubectl -n argocd apply -f payment-service.yaml
kubectl -n argocd apply -f confirmation-service.yaml