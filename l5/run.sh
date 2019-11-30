javac *.java
java ${1:-Heuristic} < ${2:-basecase}
rm *.class