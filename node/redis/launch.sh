#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

REDIS_DIR="/tmp/redis/redis-"

function start() {
	if [ -n "$1" ]; then
		echo "Starting redis-$1.conf"
		mkdir -p ${REDIS_DIR}${1}
		redis-server ${DIR}/redis-${1}.conf
	fi

	if [ -n "$2" ]; then
		echo "Starting redis-$2.conf"
		mkdir -p ${REDIS_DIR}${2}
		redis-server ${DIR}/redis-${2}.conf
	fi

	if [ -z "$1" ] && [ -z "$2" ]; then
		start "1" "2"
	fi
}

function stop() {
	if [ -n "$1" ]; then
		echo "Killing redis-$1.conf"
		pkill -9 -f "redis-server ${DIR}/redis-${1}.conf"
	fi

	if [ -n "$2" ]; then
		echo "Killing redis-$2.conf"
		pkill -9 -f "redis-server ${DIR}/redis-${2}.conf"
	fi

	if [ -z "$1" ] && [ -z "$2" ]; then
		stop "1" "2"
	fi
}

if [ "$1" == "start" ]; then
	start $2 $3;
elif [ "$1" == "restart" ]; then
	stop $2 $3;
	start $2 $3;
elif [ "$1" == "stop" ]; then
	stop $2 $3;
fi

	
