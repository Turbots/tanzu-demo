#!/usr/bin/env bash

. magic.sh

load_config

echo '----------------------------------------------'
echo '     Configuring Credentials in Dev cluster'
echo '----------------------------------------------'

echo ''
echo 'Continue to switch to the Dev Cluster...'
pe "kubectx ${DEV_CLUSTER}"

echo ''
cat kubernetes/cicd/argocd/rabbit-secret.template.yml | sed "s/RABBITMQ_PASSWORD/$RABBITMQ_PASSWORD/" | kubectl create -n ${DEV_NAMESPACE} -f -

cat kubernetes/cicd/argocd/redis-secret.template.yml | sed "s/REDIS_PASSWORD/$REDIS_PASSWORD/" | kubectl create -n ${DEV_NAMESPACE} -f -

cat kubernetes/cicd/argocd/wavefront-token.template.yml | sed "s/WAVEFRONT_TOKEN/$WAVEFRONT_TOKEN/" | kubectl create -n ${DEV_NAMESPACE} -f -
