JC = javac

all: bin/com/heatmap/GUI.class
	jar -cfm heatmap.jar bin/Manifest.txt bin/com/heatmap/*.class

bin/com/heatmap/GUI.class: src/com/heatmap/GUI.java
	$(JC) src/com/heatmap/GUI.java -d bin/
