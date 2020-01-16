#!/usr/bin/env bash

export JAVA_HOME=/usr/java/jdk1.8.0_60
export YARN_CONF_DIR=/etc/hadoop/conf
export HADOOP_CONF_DIR=/etc/hadoop/conf
#export SPARK_HOME=/app/spark
export SPARK_HOME=/app/spark2.4
export FLINK_HOME=/app/flink
export WOVEN_HOME=`cd $(dirname $0)/..; pwd`
export WOVEN_CONF=${WOVEN_CONF:-${WOVEN_HOME}/conf}
export WOVEN_LOGS_DIR=${WOVEN_LOGS_DIR:-${WOVEN_HOME}/logs}

## grpc server address and port configurations
#export GRPC_WOVEN_SERVER_URL=192.168.1.81:18801
#export GRPC_WOVEN_PIPELINE_URL=192.168.1.81:18802
#export GRPC_WOVEN_DFEXECUTOR_URL=192.168.1.81:18803
#export GRPC_WOVEN_MOREEXECUTOR_URL=192.168.1.81:18804

## resource config(m)
#export WOVEN_ROOT_TENANT_MAXRAM=1024000
#export WOVEN_ROOT_TENANT_MAXCPUS=10000

export JAVA_CMD=${JAVA_HOME}/bin/java

## set hadoop native client library path
export JAVA_LIBRARY_PATH=$JAVA_LIBRARY_PATH:/usr/hdp/current/hadoop-client/lib/native
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/hdp/current/hadoop-client/lib/native

## set extra jar dirs
#ALL_EXTRA_CLASSPATH="/usr/hdp/current/kafka-broker/libs/kafka-clients-0.10.1.2.6.0.3-8.jar"
export WOVEN_SERVER_EXTRA_LIBS="/app/hdp/hbase-client/lib"
export WOVEN_DFSPARK_EXTRA_LIBS="${ALL_EXTRA_CLASSPATH},/app/hdp/hbase-client/lib"
LOGKEEPER_JVM_OPTS="-Xmx512m -Xss256k"
MORE_EXECUTORS_JVM_OPTS="-Xmx1g -Xss256k"
DF_EXECUTOR_JVM_OPTS="-Xmx1g -Xss256k"
#PIPELINE_SERVER_JVM_OPTS="-Xmx2g -Xss256k"
WOVEN_SERVER_JVM_OPTS="-Xmx4g -Xss256k -Djava.security.egd=file:/dev/urandom"
