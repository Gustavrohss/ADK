import java.util.*;

class Graph {

    private int src;
    private int snk;
    private LinkedList<Edge>[] data;

    public static void main(String[] args) {
        Graph g = new Graph(9, 9, 5);
        g.qa(1, 2);
        g.qa(1, 3);
        g.qa(1, 4);
        g.qa(2, 3);
        g.qa(2, 6);
        g.qa(6, 4);
        g.qa(1, 5);
        g.qa(7, 2);
        g.qa(7, 6);
        g.qa(7, 8);
        g.qa(9, 7);
        g.qa(8, 3);
        g.qa(9, 3);
        System.out.println(g.bfs());
    }

    Graph(int size, int source, int sink) {
        src = source;
        snk = sink;
        data = new LinkedList[size + 1];
        for (int i = 1; i <= size; i++) data[i] = new LinkedList<>();
    }

    public void qa(int a, int b) {
        add(a, b, 1);
        add(b, a, 1);
    }

    public LinkedList<Integer> bfs() {

        /**
         * Kanske hålla koll på om kanterna har flöde
         * Tas hand om vid skapande av grafen?
         */

        // Queue
        LinkedList<Integer> q = new LinkedList<>();
        // Has been visited
        boolean[] visited = new boolean[data.length];
        // For path retracing
        int[] prev = new int[data.length];

        // Start at source
        int start = src;
        boolean found = false;
        visited[start] = true;
        q.add(start);

        // Search for sink
        while(!q.isEmpty()) {
            int current = q.removeFirst();
            for (Edge edge : data[current]) {
                int neighbour = edge.to;
                if (visited[neighbour]) continue;
                visited[neighbour] = true;
                prev[neighbour] = current;
                q.addLast(neighbour);
                if (neighbour == snk) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) return null;

        // Retrace
        LinkedList<Integer> out = new LinkedList<>();
        int x = snk;
        while(x != src) {
            out.add(x);
            x = prev[x];
        }
        out.add(src);
        return out;
    }

    public int get(int from, int to) {
        var edges = data[from];
        for (Edge e : edges) if (e.to == to) return e.flow;
        System.out.println("Get failed; no such edge");
        System.exit(1);
        return 420;
    }

    public void add(int from, int to, int flow) {
        data[from].add(new Edge(to, flow));
    }

    private class Edge {
        int to;
        int flow;
        Edge(int t, int f) {
            to = t; flow = f;
        }
    }
}