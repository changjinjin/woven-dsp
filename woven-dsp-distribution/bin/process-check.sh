#!/usr/bin/env bash

function checkAction()
{
    actions=(start restart stop)
    if ! [[ " ${actions[*]} " == *" $1 "* ]]; then
       echo "Usage: $2 start|stop|restart"
       exit 1
    fi
}

function checkConfig()
{
    APP_LOG_DIR=${WOVEN_LOGS_DIR}/${WOVEN_COM_NAME}
    mkdir -p ${APP_LOG_DIR}

    if [ "`readlink -f ${WOVEN_LOGS_DIR}`" != "`readlink -f ${WOVEN_HOME}/logs`" ]; then
        if [ "`readlink -f ${WOVEN_HOME}`/logs" != `readlink -f ${WOVEN_HOME}/logs` ]; then
            rm ${WOVEN_HOME}/logs
        else
            mv "${WOVEN_HOME}/logs" "${WOVEN_HOME}/logs-back-$(date +%Y-%m-%d_%H-%M)"
        fi

        ln -s ${WOVEN_LOGS_DIR} ${WOVEN_HOME}/logs
    fi


    if [ ${WOVEN_CONFIG_CHECKED:-0} == 1 ]; then
        return
    fi

    export WOVEN_CONFIG_CHECKED=1
}