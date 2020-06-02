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
cat kubernetes/cicd/argocd/rabbit-secret.template.yml | sed "s/RABBITMQ_PASSWORD/$DEV_RABBITMQ_PASSWORD/" | kubectl create -n ${DEV_NAMESPACE} -f -

cat kubernetes/cicd/argocd/wavefront-token.template.yml | sed "s/WAVEFRONT_TOKEN/$DEV_WAVEFRONT_TOKEN/" | kubectl create -n ${DEV_NAMESPACE} -f -

echo '----------------------------------------------'
echo '     Configuring Credentials in Prod cluster'
echo '----------------------------------------------'

echo ''
echo 'Continue to switch to the Prod Cluster...'
pe "kubectx ${PROD_CLUSTER}"

echo ''
cat kubernetes/cicd/argocd/rabbit-secret.template.yml | sed "s/RABBITMQ_PASSWORD/$PROD_RABBITMQ_PASSWORD/" | kubectl create -n ${PROD_NAMESPACE} -f -

cat kubernetes/cicd/argocd/wavefront-token.template.yml | sed "s/WAVEFRONT_TOKEN/$PROD_WAVEFRONT_TOKEN/" | kubectl create -n ${PROD_NAMESPACE} -f -
