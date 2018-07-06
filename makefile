JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	LispInterpreter.java \
	Lexer.java \
	Parser.java \
	Env.java \
	Helper.java \
	Functions.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
