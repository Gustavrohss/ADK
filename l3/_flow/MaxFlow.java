import java.io.*;
import java.util.*;

class MaxFlow {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in)
        );

        // sint : Integer.parseInt shorthand, se below
        // Size of graph
        int size = sint(in.readLine());
        // Source, sink
        String[] tmp = in.readLine().split(" ");
        int src = sint(tmp[0]);
        int snk = sint(tmp[1]);
        // Amount of edges, can be ignored
        in.readLine();

        Graph c = new Graph(size, src, snk);
        while(in.ready()) {
            String[] edg = in.readLine().split(" ");
            // Input format <u v capacity>
            c.put(sint(edg[0]), sint(edg[1]), sint(edg[2]));
        }
        in.close();
        
        // Flow graph, all flows set to 0
        Graph f = c.zeroClone();
        // Residual capacity graph
        Graph cf = c.clone();
        
        // Ford - Fulkersson algorithm
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
        
        System.out.println(size);
        System.out.println(src + " " + snk + " " + f.flowTot());
        f.printPosFlowEdges();

    }

    public static int sint(String s) {
        return Integer.parseInt(s);
    }
}