JC = javac

all: bin/com/heatmap/GUI.class

bin/com/heatmap/GUI.class: src/com/heatmap/GUI.java
	$(JC) src/com/heatmap/GUI.java -d bin/
