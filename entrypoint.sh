#!/bin/sh

if [ -z "$SERVER_PORT" ]; then
  export SERVER_PORT=80
fi

echo "server port : $SERVER_PORT"

java -jar spring-boot-echo.jar
