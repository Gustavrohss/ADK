javac *.java
cat _test | java ToBiparite | ../utils/maxflow | java ToMatches
rm *.class
