import java.util.*;

class Graph {
    private HashMap<Integer, LinkedList<Integer>> data = null;
    private int max;
    public Graph(int cap) {
        data = new HashMap<>();
        for (int i = 1; i <= cap; i++) data.put(i, new LinkedList<>());
        max = cap;
    }

    public void addEdge(int a, int b) {
        data.get(a).add(b);
        data.get(b).add(a);
    }

    public void addEdge(String ab) {
        String[] tmp = ab.split(" ");
        int a = Integer.parseInt(tmp[0]);
        int b = Integer.parseInt(tmp[1]);
        addEdge(a, b);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= max; i++) {
            sb.append(i + ":" + data.get(i).toString() + "\n");
        }
        return sb.toString().stripTrailing();
    }

    public void printEdges() {
        for (int i = 1; i <= max; i++) {
            System.out.println(
                data.get(i)
                .toString()
                .replaceAll("[:\\[\\],]", ""));
        }
    }
}