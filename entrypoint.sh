#!/bin/sh

if [[ -z "${SERVER_PORT}" ]]; then
  export SERVER_PORT=80
fi

java -jar spring-boot-echo.jar
