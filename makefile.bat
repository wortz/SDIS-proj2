del "bin\peer\*.class" "bin\protocol\*.class" "bin/*.class" "bin/utility/*.class"
rmdir bin\peer bin\protocol bin\utility bin
mkdir bin
javac -d bin -sourcepath src *.java peer/*.java protocol/*.java protocol/*.java

