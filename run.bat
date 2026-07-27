@echo off
echo Compiling Tarung Bogor Ticketing...
javac -d out src\Main.java src\model\*.java src\service\*.java src\view\*.java

echo Running app...
java -cp out Main

pause