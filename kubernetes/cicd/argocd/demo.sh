#!/usr/bin/env bash

. demo-magic.sh

clear

CICD_CLUSTER=cicd-cluster
TARGET_CLUSTER=dev-cluster

pe "kubectx ${CICD_CLUSTER}"

p "kubectl create namespace argocd"

p "kubectl -n argocd apply -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml"

p "kubectl -n argocd patch svc argocd-server -p '{\"spec\": {\"type\": \"LoadBalancer\"}}'"

ARGOCD_USERNAME=admin
ARGOCD_PASSWORD=`kubectl -n argocd get pods -l app.kubernetes.io/name=argocd-server -o name | cut -d'/' -f 2`
ARGOCD_IP=`kubectl -n argocd get svc/argocd-server -o json | jq '.status.loadBalancer.ingress[0].ip' -j`

pe "argocd login ${ARGOCD_IP} --name argocd --username ${ARGOCD_USERNAME} --password ${ARGOCD_PASSWORD} --insecure"

pe "kubectx ${TARGET_CLUSTER}"
p "argocd cluster add `kubectx -c`"

pe "kubectl -n default apply -f wavefront-token.yaml"

pe "kubectx ${CICD_CLUSTER}"

pe "kubectl -n argocd apply -f rabbit.yaml"
pe "kubectl -n argocd apply -f redis.yaml"

pe "kubectl -n argocd apply -f account-service.yaml"
pe "kubectl -n argocd apply -f payment-service.yaml"
pe "kubectl -n argocd apply -f confirmation-service.yaml"