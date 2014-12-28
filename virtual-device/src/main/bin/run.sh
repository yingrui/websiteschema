#!/bin/bash

DIR=`dirname $0`
echo "$DIR"
cd $DIR

ulimit -c unlimited

# set virtual X server for DISPLAY
CID=100
Xvfb :$CID -screen 0 1024x768x24 -once -fbdir /tmp&
export DISPLAY=:${CID}.0

java -Xmx4096m -jar ../lib/virtual-device.jar > /dev/null &
