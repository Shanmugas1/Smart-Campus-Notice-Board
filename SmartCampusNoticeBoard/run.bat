@echo off
setlocal
cd /d "%~dp0"

if not exist out mkdir out

for /r %%f in (*.java) do (
    javac -cp "lib\sqlite-jdbc-3.46.0.0.jar;lib\slf4j-api-2.0.16.jar" -d out "%%f"
)

java -cp "out;lib\sqlite-jdbc-3.46.0.0.jar;lib\slf4j-api-2.0.16.jar" Main
