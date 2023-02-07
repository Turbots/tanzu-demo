if [ -z $2 ] ; then

    echo "Setting up GitOps for View Cluster"
    #kubectx $2
    #kapp deploy -a view-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/view-cluster/tanzu-demo.yaml -y -y

fi

if [ -z $3 ] ; then

    echo "Setting up GitOps for Build Cluster"
    #kubectx $3
    #kapp deploy -a build-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/build-cluster/tanzu-demo.yaml -y

fi

if [ -z $4 ] ; then

    echo "Setting up GitOps for Run Cluster"
    #kubectx $4
    #kapp deploy -a run-cluster-gitops -f https://raw.githubusercontent.com/Turbots/gitops/main/kapp/run-cluster/tanzu-demo.yaml -y

fi