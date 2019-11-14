#!/bin/sh

echo off
clear
BASEDIR=$(dirname $0)
cd "$BASEDIR"

java -classpath commander4j.jar:./lib/app/*:./lib/barcode/*:./lib/cups4j/*:./lib/db/*:./lib/devonly/*:./lib/jasper/*:./lib/jasperlib/*:./lib/mail/* com.commander4j.sys.Start
