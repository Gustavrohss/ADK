/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {

  LinkedList<String> closestWords = null;
  int closestDistance = Integer.MAX_VALUE;
  Matrix matrix;

  int partDist(String w1, String w2) {
    int m = w2.length(); int n = w1.length();
    if (m == 0) return n; if (n == 0) return m;
    return matrix.update(w2);
  }

  public ClosestWords(String w, List<String> wordList) {

    matrix = new Matrix(w);

    for (String s : wordList) {

      if (Math.abs(w.length() - s.length()) > closestDistance) continue;
      
      int dist = partDist(w, s);

      if (dist < closestDistance) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}