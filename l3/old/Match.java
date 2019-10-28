import java.io.*;
import java.util.*;
import java.util.stream.*;

class Match {
    public static void main(String[] args) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String szs[] = in.readLine().split(" ");
        int xs = sint(szs[0]);
        int ys = sint(szs[1]);
        int es = sint(in.readLine());
        Graph xye = new Graph(xs + ys);

        while(in.ready()) xye.addEdge(in.readLine());


    }

    private static int sint(String in) {
        return Integer.parseInt(in);
    }

    private static void printSol(int xs, int ys, int es, Graph xye) {
        System.out.print(xs + " ");
        System.out.println(ys);
        System.out.println(es);
        xye.printEdges();
    }
}