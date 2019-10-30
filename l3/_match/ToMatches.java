import java.util.*;
import java.io.*;

/**
 * Tar en maxflödeslösning och konverterar till en matchningsproblemlösning.
 */
class ToMatches {
    public static void main(String[] args) throws Exception {
        writeMatches();
    }

    public static void writeMatches() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // Storlekar |x|, |y| behövs inte
        in.readLine(); 
        // För input <source, sink, flow> motsvarar flow antal matchningar
        // Antalet matchningar är det ända som behövs
        String matches = in.readLine().split(" ")[2];
        // Antalet kanter med positivt flöde behövs inte
        in.readLine();
        // Så som ToBiparite konstruerar grafen är sink alltid maxnumrerad
        int max = 0;
        LinkedList<Integer> edges = new LinkedList<>();

        while (in.ready()) {
            // En rad mosvarar en kant <x y>
            // Kallas oftast u respektive v, inget specifikt skäl till 
            //  avvikelse från namngivningskonventionen med obetydligt
            String[] tmp = in.readLine().split(" ");
            // Incrementeras i ToBiparite för att tillåta source == 1
            int x = ToBiparite.sint(tmp[0]) - 1;
            int y = ToBiparite.sint(tmp[1]) - 1;
            edges.add(x); edges.add(y);
            // Håll max uppdaterad
            max = Math.max(max, Math.max(x, y));
        }

        System.out.println(ToBiparite.xs + " " + ToBiparite.ys);
        System.out.println(matches);

        while(!edges.isEmpty()) {
            int x = edges.removeFirst();
            int y = edges.removeFirst();
            // Source, sink
            if (x != 0 && y != max) 
                System.out.println(x + " " + y);
        }
    }
}