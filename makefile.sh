rm -rf bin
rm -f *.jar
mkdir -p bin
javac -d bin -sourcepath src utility/*.java server/*.java peer/*.java protocol/*.java protocol/*.java
