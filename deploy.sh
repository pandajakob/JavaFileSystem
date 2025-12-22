javac -d /Users/jakobmichaelsen/scripts ./src/main/java/Main.java
jar cf /Users/jakobmichaelsen/scripts/filetree.jar /Users/jakobmichaelsen/scripts/Main.class
jar cfm /Users/jakobmichaelsen/scripts/filetree.jar /Users/jakobmichaelsen/scripts/manifest.txt -C /Users/jakobmichaelsen/scripts .