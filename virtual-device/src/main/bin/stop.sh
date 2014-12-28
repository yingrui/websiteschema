#!/bin/bash
cat device.pid | xargs -n 1 kill -9
