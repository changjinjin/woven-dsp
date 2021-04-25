### 1.创建镜像拉取的secret，指定namespace

```bash
kubectl create secret docker-registry dsp-secret --docker-server=192.168.1.74 --docker-username=admin --docker-password=admin --namespace=dsp 
```

### 2.创建镜像拉取的secret，指定namespace输出到yaml文件

```bash
kubectl create secret docker-registry dsp-secret --docker-server=192.168.1.74 --docker-username=admin --docker-password=admin --namespace=dsp --dry-run=client -o yaml > baymax-dsp-secret.yaml
```
