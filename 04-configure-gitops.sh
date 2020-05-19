#!/usr/bin/env bash

. magic.sh

load_config

echo '----------------------------------------------'
echo '         Configuring GitOps in ArgoCD'
echo '----------------------------------------------'

echo ''
echo 'Continue to switch back to CI/CD Cluster...'
pe "kubectx ${CICD_CLUSTER}"

echo ''
echo '----------------------------------------------'
echo '         Configuring RabbitMQ'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the RabbitMQ Helm chart in ArgoCD...'
cat kubernetes/cicd/argocd/rabbit.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '         Configuring Redis'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the Redis Helm chart in ArgoCD...'
cat kubernetes/cicd/argocd/redis.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '         Configuring Wavefront Proxy'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the Redis Helm chart in ArgoCD...'
cat kubernetes/cicd/argocd/wavefront-proxy.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '         Configuring Applications'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the applications in ArgoCD...'
cat kubernetes/cicd/argocd/account-service.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
cat kubernetes/cicd/argocd/confirmation-service.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
cat kubernetes/cicd/argocd/payment-service.yml | sed "s/WORKLOAD_NAMESPACE/${DEV_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '      GitOps configured successfully!'
echo '----------------------------------------------'
echo ''