#!/usr/bin/env bash

. magic.sh

clear

echo '--------------------------------'
echo 'Welcome to the Tanzu Demo setup!'
echo '--------------------------------'

echo '-----------------------------------------------------------------------'
echo 'Make sure you have kubectl, kubectx and kubens installed in your shell!'
echo '-----------------------------------------------------------------------'

echo ''
kubectx
echo ''

echo 'Please provide the clusters you would like to use for this demo. The name of the kube context is sufficient.'
read -p 'CI/CD Cluster: ' CICD_CLUSTER
CICD_CLUSTER=${CICD_CLUSTER:-cicd}
read -p 'Dev: ' DEV_CLUSTER
DEV_CLUSTER=${DEV_CLUSTER:-dev}

echo ''
echo 'Thank you! Setting up Tanzu Demo with CICD_CLUSTER ['${CICD_CLUSTER}'] and DEV_CLUSTER ['${DEV_CLUSTER}']'
echo ''

echo ''
echo 'Continue to switch to CI/CD Cluster...'
pe "kubectx ${CICD_CLUSTER}"
echo ''

echo '--------------------------------'
echo '       Installing kpack'
echo '--------------------------------'
echo ''

echo 'Continue to install kpack...'
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/01-kpack-release-0.0.8.yml"

echo ''
echo 'Please provide your Github username and password. This will be used to fetch the application source code.'
read -p 'Github username: ' GITHUB_USERNAME
read -s -p 'Github password: ' GITHUB_PASSWORD

echo ''
echo 'Please provide your Dockerhub username and password. This will be used to push your built container images.'
read -p 'Dockerhub username: ' DOCKERHUB_USERNAME
read -s -p 'Dockerhub password: ' DOCKERHUB_PASSWORD

echo 'Continue to set the Github and Docker secrets...'
read
cat kubernetes/cicd/kpack/02-github-creds.template.yml | sed "s/GITHUB_USERNAME/$GITHUB_USERNAME/" | sed "s/GITHUB_PASSWORD/$GITHUB_PASSWORD/" | kubectl create -n kpack -f -
cat kubernetes/cicd/kpack/03-dockerhub-creds.template.yml | sed "s/DOCKERHUB_USERNAME/$DOCKERHUB_USERNAME/" | sed "s/DOCKERHUB_PASSWORD/$DOCKERHUB_PASSWORD/" |kubectl create -n kpack -f -

echo 'Continue to install the default kpack builder...'
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/04-java-builder.yml"

echo 'Continue to configure the application images...'
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/images/account-image.yml"
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/images/confirmation-image.yml"
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/images/payment-image.yml"

echo ''
echo '----------------------------------------------'
echo '      kpack configured successfully!'
echo '----------------------------------------------'

echo ''
echo 'Continue?'
echo ''
read

echo '----------------------------------'
echo '       Installing ArgoCD'
echo '----------------------------------'

echo ''
echo 'Continue to create ArgoCD namespace...'
pe "kubectl create namespace argocd"

echo ''
echo 'Continue to install ArgoCD on the CI/CD cluster...'
pe "kubectl -n argocd apply -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml"

echo ''
echo 'Continue to fetch a Load Balanced IP for the ArgoCD Server...'
pe "kubectl -n argocd patch svc argocd-server -p '{\"spec\": {\"type\": \"LoadBalancer\"}}'"

ARGOCD_USERNAME=admin
ARGOCD_PASSWORD=`kubectl -n argocd get pods -l app.kubernetes.io/name=argocd-server -o name | cut -d'/' -f 2`

echo ''
echo 'Continue to store the ArgoCD IP address...'
pe "ARGOCD_IP=`kubectl -n argocd get svc/argocd-server -o json | jq '.status.loadBalancer.ingress[0].ip' -j`"

echo ''
echo 'Continue to install the correct ArgoCD binary locally'
pe "wget --no-check-certificate https://${ARGOCD_IP}/download/argocd-darwin-amd64 -O argocd && chmod +x argocd"

echo ''
echo 'Continue to login to ArgoCD using "argocd login $ARGOCD_IP --name argocd --username $ARGOCD_USERNAME --password $ARGOCD_PASSWORD --insecure"'
pe "./argocd login ${ARGOCD_IP} --name argocd --username ${ARGOCD_USERNAME} --password ${ARGOCD_PASSWORD} --insecure"

echo ''
echo 'Continue to add the DEV_CLUSTER to the ArgoCD cluster configuration...'
pe "kubectx ${DEV_CLUSTER}"
pe "./argocd cluster add `kubectx -c`"

echo '----------------------------------------------'
echo '      ArgoCD configured successfully!'
echo '----------------------------------------------'
echo ''

echo 'Continue?'
echo ''
read

echo '----------------------------------------------'
echo '     Configuring Credentials in Dev cluster'
echo '----------------------------------------------'
echo ''

echo 'Continue to switch to the Dev Cluster...'
pe "kubectx ${DEV_CLUSTER}"
echo ''

echo ''
echo 'Please provide the URL of the dev cluster ("kubectl cluster-info" in another window to find out).'
read -p 'Dev Cluster URL: ' DEV_CLUSTER_URL
echo ''

echo 'Please provide the namespace you want to install the apps and services in (ENTER for default).'
read -p 'Workload Namespace: ' WORKLOAD_NAMESPACE
WORKLOAD_NAMESPACE=${WORKLOAD_NAMESPACE:-default}
echo "Installing workloads to ${DEV_CLUSTER_URL} in namespace ${WORKLOAD_NAMESPACE}..."
echo ''

echo 'Please provide your preferred RabbitMQ password.'
read -s -p 'RabbitMQ password: ' RABBITMQ_PASSWORD
cat kubernetes/cicd/argocd/rabbit-secret.template.yml | sed "s/RABBITMQ_PASSWORD/$RABBITMQ_PASSWORD/" | kubectl create -n ${WORKLOAD_NAMESPACE} -f -
echo ''

echo 'Please provide your preferred Redis password.'
read -s -p 'Redis password: ' REDIS_PASSWORD
cat kubernetes/cicd/argocd/redis-secret.template.yml | sed "s/REDIS_PASSWORD/$REDIS_PASSWORD/" | kubectl create -n ${WORKLOAD_NAMESPACE} -f -
echo ''

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
cat kubernetes/cicd/argocd/rabbit.yml | sed "s/WORKLOAD_NAMESPACE/${WORKLOAD_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '         Configuring Redis'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the Redis Helm chart in ArgoCD...'
cat kubernetes/cicd/argocd/redis.yml | sed "s/WORKLOAD_NAMESPACE/${WORKLOAD_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '         Configuring Applications'
echo '----------------------------------------------'
echo ''

echo 'Continue to configure the applications in ArgoCD...'
cat kubernetes/cicd/argocd/account-service.yml | sed "s/WORKLOAD_NAMESPACE/${WORKLOAD_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
cat kubernetes/cicd/argocd/confirmation-service.yml | sed "s/WORKLOAD_NAMESPACE/${WORKLOAD_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
cat kubernetes/cicd/argocd/payment-service.yml | sed "s/WORKLOAD_NAMESPACE/${WORKLOAD_NAMESPACE}/" | sed "s/DEV_CLUSTER_URL/${DEV_CLUSTER_URL//\//\\/}/" | kubectl create -n argocd -f -
echo ''

echo '----------------------------------------------'
echo '      GitOps configured successfully!'
echo '----------------------------------------------'
echo ''