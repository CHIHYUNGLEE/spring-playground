@echo off
set JAVA_HOME=D:\Java\jdk17.0.20_12
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d D:\eclipse-r6-202503\spring-playground
java -version
call mvn clean package -DskipTests