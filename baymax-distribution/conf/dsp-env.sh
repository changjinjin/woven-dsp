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

export JAVA_CMD=${JAVA_HOME}/bin/java

## set hadoop native client library path
export JAVA_LIBRARY_PATH=$JAVA_LIBRARY_PATH:/usr/hdp/current/hadoop-client/lib/native
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/hdp/current/hadoop-client/lib/native

## set extra jar dirs
#ALL_EXTRA_CLASSPATH="/usr/hdp/current/kafka-broker/libs/kafka-clients-0.10.1.2.6.0.3-8.jar"
export WOVEN_SERVER_EXTRA_LIBS="/app/hdp/hbase-client/lib"
export WOVEN_DFSPARK_EXTRA_LIBS="${ALL_EXTRA_CLASSPATH},/app/hdp/hbase-client/lib"

ACCESS_CONSUMER_JVM_OPTS="-Xmx1g -Xss256k"
ACCESS_DATAAPI_JVM_OPTS="-Xmx1g -Xss256k"
ACCESS_PLATFORM_JVM_OPTS="-Xmx1g -Xss256k"
AUTH_SERVER_JVM_OPTS="-Xmx1g -Xss256k"
DSP_GATEWAY_JVM_OPTS="-Xmx1g -Xss256k"
JOB_EXEC_JVM_OPTS="-Xmx4g -Xss256k"
JOB_SCHEDULE_JVM_OPTS="-Xmx2g -Xss256k"
