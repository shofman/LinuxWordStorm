#! /bin/bash
Xvfb :2 2>/dev/null &
export DISPLAY=":2"
java -jar $1 $2
killall -9 Xvfb
