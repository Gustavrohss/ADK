import java.util.*;
import java.io.*;

class ToMatches {

    public static void writeMatches(int matches, LinkedList<Integer> matchData) throws Exception {
        LinkedList<Integer> edges = new LinkedList<>();

        System.out.println(ToBiparite.xs + " " + ToBiparite.ys);
        System.out.println(matches);

        while(!matchData.isEmpty()) {
            int x = matchData.removeFirst() - 1;
            int y = matchData.removeFirst() - 1;
            if (x != 0 && y != ToBiparite.maxVertex) 
                System.out.println(x + " " + y);
        }
    }
}