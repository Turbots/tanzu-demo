if [ $# -ne 3 ]; then
    echo "Please provide 3 arguments"
else
    echo "Setting up GitOps for View Cluster $1"
    kubectx $1
    kapp deploy -a view-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/view-cluster/tanzu-demo.yaml -y -y

    echo "Setting up GitOps for Build Cluster $2"
    kubectx $2
    kapp deploy -a build-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/build-cluster/tanzu-demo.yaml -y

    echo "Setting up GitOps for Run Cluster $3"
    kubectx $3
    kapp deploy -a run-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/run-cluster/tanzu-demo.yaml -y
fi