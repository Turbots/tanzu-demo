#!/usr/bin/env bash

. magic.sh

load_config

echo '----------------------------------'
echo '       Installing ArgoCD'
echo '----------------------------------'

echo ''
echo 'Continue to switch to CI/CD Cluster...'
pe "kubectx ${CICD_CLUSTER}"

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

if [[ ! -f argocd ]]; then
  echo ''
  echo 'Continue to install the correct ArgoCD binary locally'
  pe "wget --no-check-certificate https://${ARGOCD_IP}/download/argocd-darwin-amd64 -O argocd && chmod +x argocd"
else
  continue
fi

echo ''
echo 'Continue to login to ArgoCD using "argocd login $ARGOCD_IP --name argocd --username $ARGOCD_USERNAME --password $ARGOCD_PASSWORD --insecure"'
pe "./argocd login ${ARGOCD_IP} --name argocd --username ${ARGOCD_USERNAME} --password ${ARGOCD_PASSWORD} --insecure"

echo ''
echo 'Continue to add the DEV_CLUSTER to the ArgoCD cluster configuration...'
pe "kubectx ${DEV_CLUSTER}"
pe "./argocd cluster add `kubectx -c`"
pe "./argocd proj create development"
pe "./argocd proj add-source development https://github.com/Turbots/tanzu-demo"
pe "./argocd proj add-source development https://github.com/Turbots/tanzu-demo-gitops"
pe "./argocd proj add-source development https://charts.bitnami.com/bitnami"
pe "./argocd proj add-destination development ${DEV_CLUSTER_URL} ${DEV_NAMESPACE}"

echo ''
echo 'Continue to add the PROD_CLUSTER to the ArgoCD cluster configuration...'
pe "kubectx ${PROD_CLUSTER}"
pe "./argocd cluster add `kubectx -c`"
pe "./argocd proj create production"
pe "./argocd proj add-source production https://github.com/Turbots/tanzu-demo"
pe "./argocd proj add-source production https://github.com/Turbots/tanzu-demo-gitops"
pe "./argocd proj add-source production https://charts.bitnami.com/bitnami"
pe "./argocd proj add-destination production ${PROD_CLUSTER_URL} ${PROD_NAMESPACE}"

echo '----------------------------------------------'
echo '      ArgoCD configured successfully!'
echo '----------------------------------------------'
echo ''