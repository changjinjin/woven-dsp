FROM openjdk:8-jdk-alpine
MAINTAINER inforefiner info@inforefiner.com

##install some commands
##echo "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.9/main/" > /etc/apk/repositories \
RUN apk update \
	&& apk upgrade \
	&& apk add --no-cache bash bash-doc bash-completion tzdata dos2unix ca-certificates wget \
	&& cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone \
	&& apk del tzdata \
    && rm -rf /var/cache/apk/* 
	
# Install language pack
# Iterate through all locale and install it
# Note that locale -a is not available in alpine linux, use `/usr/glibc-compat/bin/locale -a` instead
# wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.25-r0/glibc-2.25-r0.apk
# wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.25-r0/glibc-bin-2.25-r0.apk 
# wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.25-r0/glibc-i18n-2.25-r0.apk 
COPY ./locale.md /locale.md
COPY ./glibc-2.25-r0.apk .
COPY ./glibc-bin-2.25-r0.apk .
COPY ./glibc-i18n-2.25-r0.apk .
RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub \
    && apk add ./glibc-bin-2.25-r0.apk ./glibc-i18n-2.25-r0.apk ./glibc-2.25-r0.apk \
    && cat locale.md | xargs -i /usr/glibc-compat/bin/localedef -i {} -f UTF-8 {}.UTF-8 \
    && rm -rfv ./glibc-bin-2.25-r0.apk ./glibc-i18n-2.25-r0.apk ./glibc-2.25-r0.apk \
    && rm -rfv locale.md 

# Set the lang, you can also specify it as a environment variable through docker-compose.yml
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8
