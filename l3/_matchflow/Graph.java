import java.util.*;

/**
 * Grafrepresentation
 */
class Graph {

    private int src;
    private int snk;
    // Notera:
    // Edge motsvarar inte grafkanter i denna implementation
    // data.get(u).get(v) == x
    //  => graph[u, v] == x
    //  => x kan motsvara kapacitet eller flöde som passande
    private ArrayList<LinkedList<Edge>> data;

    Graph(int size, int source, int sink) {
        src = source;
        snk = sink;
        // Indexeras från 1
        data = new ArrayList<>(size + 1);
        for (int i = 0; i <= size; i++) data.add(new LinkedList<>());
    }

    /**
     * Breadth first search som returnerar stig från sink till source
     * Notera att ordningen kan tyckas omvänd från den förväntade
     * @return En stig v1, v2, v3 ... från source till sink, där varje kant har >0 kapacitet/flöde
     */
    public ArrayList<Integer> bfs() {

        // Queue
        LinkedList<Integer> q = new LinkedList<>();
        // Has been visited
        boolean[] visited = new boolean[data.size()];
        // For path retracing
        int[] prev = new int[data.size()];

        // Start at source
        int start = src;
        boolean found = false;
        visited[start] = true;
        q.add(start);

        // Search for sink
        while(!q.isEmpty()) {
            int current = q.removeFirst();
            for (Edge edge : data.get(current)) {
                int neighbour = edge.u;
                // Node is visited or to be considered unreachable along this edge
                if (visited[neighbour] || edge.v <= 0) continue;

                visited[neighbour] = true;
                prev[neighbour] = current;
                q.addLast(neighbour);

                // Done!
                if (neighbour == snk) {
                    found = true;
                    break;
                }
            }
        }

        // Meta-algorithm stop condition
        if (!found) return null;

        // Retrace
        ArrayList<Integer> out = new ArrayList<>();
        int x = snk;
        while(x != src) {
            out.add(x);
            x = prev[x];
        }
        out.add(src);
        return out;
    }

    // Returnera x sådant att graph[from, to] == x
    public int get(int from, int to) {
        var edges = data.get(from);
        for (Edge e : edges) if (e.u == to) return e.v;
        // If nothing is found, return 0
        return 0;
    }

    // Instantiera eller uppdatera kant graph[from, to] := flow
    public void put(int from, int to, int flow) {
        for (Edge e : data.get(from)) {
            if (e.u == to) {
                e.v = flow;
                return;
            }
        }
        data.get(from).add(new Edge(to, flow));
    }

    // En kopia av grafen
    public Graph clone() {
        Graph out = new Graph(data.size() - 1, src, snk);
        for (int i = 1; i < data.size(); i++) {
            for (Edge e : data.get(i)) {
                out.put(i, e.u, e.v);
            }
        }
        return out;
    }

    // En kopia av grafen där varje kapacitet/flöde := 0
    public Graph zeroClone() {
        Graph out = new Graph(data.size() - 1, src, snk);
        for (int i = 1; i < data.size(); i++) {
            for (Edge e : data.get(i)) {
                out.put(i, e.u, 0);
            }
        }
        return out;
    }

    // Totala flödet i grafen om tolkad som en flödesgraf
    // Exakt lika med flödet ut ur source
    public int flowTot() {
        LinkedList<Edge> outs = data.get(src);
        int tot = 0;
        for (Edge e : outs) tot += e.v;
        return tot;
    }

    public void populatePosFlowEdgeData(LinkedList<Integer> out) {
        LinkedList<Integer> prnt = new LinkedList<>();
        for (int u = 1; u < data.size(); u++) 
            for (Edge e : data.get(u)) 
                if (e.v > 0) {
                    out.add(u);
                    out.add(e.u);
                }
        for (int x : prnt) out.add(x);
    }
}