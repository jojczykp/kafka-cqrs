#!/bin/bash

log() {
    echo "[$(date)] ${1}"
}

wait_for_open_port () {
    HOST=${1}
    PORT=${2}

    log "Waiting for ${HOST}:${PORT} to be open... "

    while ! nc -w 5 -z ${HOST} ${PORT}
    do
        true
    done

    log "OK!"
}


log "Starting container"

wait_for_open_port kafka-service 9092

log "Running Application"

java -jar producer-service.jar
