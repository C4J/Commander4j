#!/bin/sh
#!/bin/sh

echo off
clear
BASEDIR=$(dirname $0)
cd "$BASEDIR"

file="./.install4j/jre.bundle/Contents/Home/bin/java"
if [ -f "$file" ]
then
	./.install4j/jre.bundle/Contents/Home/bin/java -classpath commander4j.jar:./lib/devonly/* com.commander4j.cfg.Setup
else
	java -classpath commander4j.jar:./lib/devonly/* com.commander4j.cfg.Setup
fi

exit
