all: main

main:
	javac *.java

run: main
	bash -c "export CLASSPATH=$CLASSPATH\:.\:/oracle/jdbc/lib/ojdbc5.jar"
	java Main

clean: 
	rm *.class *~
