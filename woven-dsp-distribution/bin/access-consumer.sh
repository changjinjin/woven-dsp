#!/usr/bin/env bash
set -e
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

action="$1"

WOVEN_BIN=${WOVEN_BIN:-${bin}}
WOVEN_HOME=`cd $(dirname $0)/..; pwd`
WOVEN_CONF=${WOVEN_CONF:-${WOVEN_HOME}/conf}

WOVEN_COM_NAME=access-consumer
. ${WOVEN_CONF}/dsp-env.sh
. ${WOVEN_BIN}/process-check.sh

checkAction "${action}" "${WOVEN_COM_NAME}.sh"


PID_FILE="${WOVEN_COM_NAME}.pid"
#JVM_OPTS="${JVM_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8402"
JVM_OPTS="${JVM_OPTS} -DWOVEN_COM_NAME=${WOVEN_COM_NAME} -DWOVEN_APP=woven -DWOVEN_LOGS_DIR=${WOVEN_LOGS_DIR} -DKAFKA_BROKER_LIST=${KAFKA_BROKER_LIST} ${PIPELINE_SERVER_JVM_OPTS}"
JVM_OPTS="${JVM_OPTS} -classpath ${WOVEN_CONF}:${YARN_CONF_DIR}:lib/*:${ALL_EXTRA_CLASSPATH:-.} com.info.baymax.dsp.access.consumer.ConsumerStarter"


APP_CMD="${JAVA_CMD} ${JVM_OPTS}"
WORK_DIR="${WOVEN_HOME}/${WOVEN_COM_NAME}"

env WORK_DIR="${WORK_DIR}" APP_NAME="${WOVEN_COM_NAME}" PID_FILE="${PID_FILE}" APP_CMD="${APP_CMD}" ${WOVEN_BIN}/process_helper.sh $@
