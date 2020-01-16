#!/usr/bin/env bash

cd ${WORK_DIR}
echo "workdir:" `pwd`

stop()
{
   if [ -f "${PID_FILE}" ];
    then
    cat ${PID_FILE} | xargs kill
    echo "killing ${APP_NAME}"
    sleep 3
    cat ${PID_FILE} | xargs kill -9
    rm ${PID_FILE}
   fi
}

start()
{
    echo "cmd-line:"
    echo "         ${APP_CMD}"
    echo "pid-file:" ${PID_FILE}
    ${APP_CMD} > /dev/null 2>&1&
    echo $! > ${PID_FILE}
}

action="${1}"
case ${action} in
    'start')
        start
        echo "${APP_NAME} started"
        ;;
    'stop')
        stop
        echo "${APP_NAME} was killed"
        ;;
    'restart')
        stop
        sleep 1
        start
        ;;
    *)
        echo 'e.g: env APP_CMD=app-cmd PID_FILE=pid-file APP_NAME=app-name process_helper start|stop|restart'
        ;;
esac