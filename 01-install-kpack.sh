#!/usr/bin/env bash

. magic.sh

load_config

echo '--------------------------------'
echo '       Installing kpack'
echo '--------------------------------'

echo ''
echo 'Continue to switch to CI/CD Cluster...'
pe "kubectx ${CICD_CLUSTER}"

echo ''
echo 'Continue to install kpack...'
pe "kubectl apply -n kpack -f kubernetes/cicd/kpack/01-kpack-release-0.0.8.yml"

echo 'Continue to set the Github and Docker secrets...'
wait
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
