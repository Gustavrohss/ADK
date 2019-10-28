import java.util.*;
import java.io.*;

class ToMatches {
    public static void main(String[] args) throws Exception {
        writeMatches();
    }

    public static void writeMatches() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.readLine();
        String matches = in.readLine().split(" ")[2];
        in.readLine();
        int max = 0;
        LinkedList<Integer> edges = new LinkedList<>();
        while (in.ready()) {
            String[] tmp = in.readLine().split(" ");
            int x = ToBiparite.sint(tmp[0]) - 1;
            int y = ToBiparite.sint(tmp[1]) - 1;
            edges.add(x); edges.add(y);
            max = Math.max(max, Math.max(x, y));
        }

        System.out.println(ToBiparite.xs + " " + ToBiparite.ys);
        System.out.println(matches);

        while(!edges.isEmpty()) {
            int x = edges.removeFirst();
            int y = edges.removeFirst();
            if (x != 0 && y != max) 
                System.out.println(x + " " + y);
        }
    }
}