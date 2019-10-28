javac *.java
cat test | java ToBiparite | ../utils/maxflow | java ToMatches
rm *.class
