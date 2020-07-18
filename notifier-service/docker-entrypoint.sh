#!/bin/bash

log() {
    echo "[$(date)] ${1}"
}

wait_for_open_port () {
    HOST=${1}
    PORT=${2}

    log "Waiting for ${HOST}:${PORT} to open..."

    until nc -w 5 -z "${HOST}" "${PORT}"
    do
        sleep 1
        true
    done

    log "OK!"
}


log "Starting container"

wait_for_open_port kafka-service 9092

log "Running Application"

java -jar notifier-service.jar
