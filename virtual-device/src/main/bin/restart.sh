#!/bin/bash

DIR=`dirname $0`
echo "$DIR"
cd $DIR

./stop.sh
sleep 10
./run.sh
