#!/usr/bin/env bash

set -e
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

WOVEN_BIN=${WOVEN_BIN:-${bin}}
WOVEN_HOME=`cd $(dirname $0)/..; pwd`
WOVEN_CONF=${WOVEN_CONF:-${WOVEN_HOME}/conf}

. ${WOVEN_CONF}/woven-env.sh
. ${WOVEN_BIN}/process-check.sh

cd ${WOVEN_HOME}

for component in more-executors df-executor pipeline-server woven-server woven-discovery;
do
    # start  component if it is present
    if [ -f "${WOVEN_HOME}"/bin/${component}.sh ];
    then
      echo "try to stop ${component}"
      env WOVEN_CONFIG_CHECKED=1 "${WOVEN_HOME}"/bin/${component}.sh stop
    else
      echo "${component} is not found and we skip it"
    fi
done

# stop livy-server
if [ -f "${WOVEN_HOME}"/livy-server/bin/livy-server ];
then
 "${WOVEN_HOME}"/livy-server/bin/livy-server stop
fi




