# Deploying Concourse to Kubernetes

## Installing using Helm

Use this command to install Concourse:
```bash
$ helm upgrade concourse stable/concourse -f concourse/values.yaml --set concourse.web.externalUrl=http://concourse.fqdn.com --install
```

Credentials will be read from
[Kubernetes secrets](https://github.com/helm/charts/tree/master/stable/concourse#secrets).

In case you want to use [Vault](https://www.vaultproject.io/) as a credentials manager,
use this command:
```bash
$ helm upgrade concourse stable/concourse -f concourse/values.yaml --set concourse.web.externalUrl=http://concourse.fqdn.com --set concourse.web.kubernetes.enabled=false --set concourse.web.vault.enabled=true --set concourse.web.vault.url=http://vault:8200 --set concourse.web.vault.authBackend=token --set secrets.vaultClientToken=s.fgrlYzp3DagSJlJlievOf7yd --install
```

## Using Concourse

You can now access Concourse. Get the allocated IP address:
```bash
$ kubectl get svc concourse-web
NAME            TYPE           CLUSTER-IP      EXTERNAL-IP                   PORT(S)                       AGE
concourse-web   LoadBalancer   10.100.200.15   10.197.47.130,100.64.112.31   80:31336/TCP,2222:30058/TCP   5m10s
```

You should map your DNS entry to the allocated IP address.
Once you're done with this configuration, you can access Concourse
using the default user `test` with password `test`.

Use the `fly` CLI to login:
```bash
$ fly login -t concourse -c http://concourse.fqdn.com -u test -p test
logging in to team 'main'


target saved
```

Check everything is working using this command:
```bash
$ fly -t concourse workers
name                containers  platform  tags  team  state    version
concourse-worker-0  0           linux     none  none  running  2.2
concourse-worker-1  0           linux     none  none  running  2.2
```
