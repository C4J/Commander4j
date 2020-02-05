echo off
cls

if exist jre\bin\java.exe goto jre_found

goto end

:jre_found

rem Use Bundled JRE
SET JAVA_HOME=.\jre\bin
SET PATH=%PATH%;.\jre\bin

:end

java -classpath commander4j.jar;.\lib\devonly\i4jruntime.jar com.commander4j.clone.Clone

exit