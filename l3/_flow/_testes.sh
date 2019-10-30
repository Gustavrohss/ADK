# Effektiv testning av javaprogrammet jämfört med blackbox
# Tar inte egentligen hänsyn till ordning på output
# Visade sig inkonsekvent
# Input: |V| |E| { edge max capacity }
javac *.java
../utils/flowgen $1 $2 $3 > test.txt
cat test.txt | ../utils/maxflow > blackbox.txt
cat test.txt | java MaxFlow > java.txt
diff blackbox.txt java.txt
rm *.class
rm blackbox.txt java.txt
