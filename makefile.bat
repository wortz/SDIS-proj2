
del "bin\peer\*.class" "bin\protocol\*.class" "bin\utility\*.class" "bin\server\*.class"
rmdir bin\peer bin\protocol bin\utility bin bin\server
mkdir bin
javac -d bin -sourcepath src utility/*.java server/*.java peer/*.java protocol/*.java protocol/*.java 
cd bin