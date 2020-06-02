#!/usr/bin/env bash

. magic.sh

load_config

echo ''
echo '----------------------------------------------'
echo '            Installing Spinnaker'
echo '----------------------------------------------'

echo ''
echo 'Continue to switch to the CI/CD Cluster...'
pe "kubectx ${CICD_CLUSTER}"

# echo 'Continue to download the Spinnaker Operator...'
# rm -f manifests.tgz
# pe "wget -q https://github.com/armory/spinnaker-operator/releases/download/v0.5.0/manifests.tgz"

# echo 'Continue to extract the Spinnaker Operator files...'
# pe "tar -xf manifests.tgz --directory kubernetes/cicd/spinnaker"

echo ''
echo 'Continue to install the Spinnaker Operator CRDs...'
pe "kubectl apply -f kubernetes/cicd/spinnaker/deploy/crds/"

echo ''
echo 'Continue to install the Spinnaker Operator...'
pe "kubectl create ns spinnaker-operator"
pe "kubectl apply -n spinnaker-operator -f kubernetes/cicd/spinnaker/deploy/operator/cluster/"

echo ''
echo 'Continue to install Spinnaker...'
pe "kubectl create ns spinnaker"

echo ''
echo 'Creating Spinnaker secret...'
cat kubernetes/cicd/spinnaker/deploy/spinnaker/spinnaker-secret.template.yml | sed "s/AWS_ACCESS_KEY_ID/${AWS_ACCESS_KEY_ID//\//\\/}/" | sed "s/AWS_ACCESS_KEY_SECRET/${AWS_ACCESS_KEY_SECRET//\//\\/}/" | kubectl create -n spinnaker -f -

echo ''
echo 'Creating Spinnaker Service...'
pe "kubectl -n spinnaker apply -f kubernetes/cicd/spinnaker/deploy/spinnaker/complete/"

echo ''
echo '----------------------------------------------'
echo '        Spinnaker Installation Complete'
echo '----------------------------------------------'