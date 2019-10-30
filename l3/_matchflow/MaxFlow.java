import java.io.*;
import java.util.*;

class MaxFlow {
    public static void maxFlow(
        int size,
        int src,
        int snk,
        HashSet<Edge> edges
    ) throws Exception {
        
        Graph c = new Graph(size, src, snk);
        for (Edge e : edges) c.put(e.u, e.v, 1);
        
        Graph f = c.zeroClone();
        Graph cf = c.clone();
        
        while(true) {
            ArrayList<Integer> path = cf.bfs();
            if (path == null) break;
            int min = Integer.MAX_VALUE;
            for (int i = 1; i < path.size(); i++) {
                min = Math.min(min, cf.get(path.get(i), path.get(i - 1)));
            }

            for (int i = 1; i < path.size(); i++) {
                int u = path.get(i);
                int v = path.get(i - 1);
                f.put(u, v, f.get(u, v) + min);
                f.put(v, u, -f.get(u, v));
                cf.put(u, v, c.get(u, v) - f.get(u, v));
                cf.put(v, u, c.get(v, u) - f.get(v, u));
            }
        }
        
        LinkedList<Integer> out = new LinkedList<>();
        f.populatePosFlowEdgeData(out);
        ToMatches.writeMatches(f.flowTot(), out);
    }

    public static int sint(String s) {
        return Integer.parseInt(s);
    }
}