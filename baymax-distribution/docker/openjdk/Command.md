### 1.创建镜像

```bash
docker build . -t 192.168.1.74/library/openjdk:8-jdk-alpine-bash
```

### 2.推送镜像

```bash
docker push 192.168.1.74/library/openjdk:8-jdk-alpine-bash
```

### 3.拉取镜像

```bash
docker pull 192.168.1.74/library/openjdk:8-jdk-alpine-bash
```
