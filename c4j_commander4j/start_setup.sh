#!/bin/sh
#!/bin/sh

echo off
clear
BASEDIR=$(dirname $0)
cd "$BASEDIR"

file="./.install4j/jre.bundle/Contents/Home/bin/java"
if [ -f "$file" ]
then
	./.install4j/jre.bundle/Contents/Home/bin/java --enable-native-access=ALL-UNNAMED -classpath commander4j.jar:./lib/devonly/* com.commander4j.cfg.Setup
else
	java --enable-native-access=ALL-UNNAMED -classpath commander4j.jar:./lib/devonly/* com.commander4j.cfg.Setup
fi

exit
