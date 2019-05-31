
del "bin\peer\*.class" "bin\protocol\*.class" "bin\utility\*.class" "bin\server\*.class" "bin\storage\*.class"
rmdir bin\peer bin\protocol bin\utility bin bin\server bin\storage
mkdir bin
javac -d bin -sourcepath src utility/*.java server/*.java peer/*.java protocol/*.java protocol/*.java storage/*.java
cd bin