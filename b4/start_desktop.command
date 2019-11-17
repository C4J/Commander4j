#!/bin/sh

echo off
clear
BASEDIR=$(dirname $0)
cd "$BASEDIR"

java -classpath commander4j.jar:./lib/devonly/* com.commander4j.sys.Start
