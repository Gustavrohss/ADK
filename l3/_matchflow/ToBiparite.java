import java.util.*;
import java.io.*;

class ToBiparite {

    static int xs;
    static int ys;
    static int maxVertex;

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String szs[] = in.readLine().split(" ");
        // x size, y size, edges size where G = (x union y, edges)
        // sint(x) equals Integer.parseInt(x)
        xs = sint(szs[0]);
        ys = sint(szs[1]);
        int es = sint(in.readLine());
        HashSet<Integer> x = new HashSet<>();
        HashSet<Integer> y = new HashSet<>();
        HashSet<Edge> edges = new HashSet<>();

        while(in.ready()) {
            String[] tmp = in.readLine().split(" ");
            // Increment all vertex numberings by 1.
            // 1 is the new source, xs + ys + 2 the new sink.
            int a = sint(tmp[0]) + 1;
            int b = sint(tmp[1]) + 1;
            x.add(a); y.add(b); 
            // Helper class for edges. A Pair, basically.
            edges.add(new Edge(a, b));
        }
        
        // Generate source, sink.
        for (int v : x) edges.add(new Edge(1, v));
        for (int w : y) edges.add(new Edge(w, xs + ys + 2));

        int outSize = xs + ys + 2;
        int outSrc = 1;
        int outSnk = xs + ys + 2;
        maxVertex = xs + ys + 1;

        MaxFlow.maxFlow(outSize, outSrc, outSnk, edges);
    }

    public static int sint(String in) {
        return Integer.parseInt(in);
    }
}