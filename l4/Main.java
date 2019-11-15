import java.util.*;
import java.io.*;

class Main {

    // Reduce to RB
    public static void main(String[] args) throws Exception {

        // Read input

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Integer>  v = new ArrayList<>();
        HashSet<Edge>       e = new HashSet<>();
        int                 m;

        int maxV = Integer.parseInt(in.readLine());
        in.readLine();
        m = Integer.parseInt(in.readLine());

        for (int i = 1; i <= maxV; i++) v.add(i);
        Collections.sort(v);

        while(in.ready()) {
            String[] tmp = in.readLine().split(" ");
            int x = Integer.parseInt(tmp[0]);
            int y = Integer.parseInt(tmp[1]);
            e.add(new Edge(x, y));
        }
        
        // Reduce

        ArrayList<Scene>  scenes = new ArrayList<>();
        ArrayList<Role>             roles  = new ArrayList<>();
        ArrayList<Integer>          actors = new ArrayList<>();

        actors.add(-1); actors.add(0); actors.add(1);
        Role ra = new Role(-1);
        ra.add(-1);
        Role rb = new Role(0);
        rb.add(0);
        Role r1 = new Role(1);
        r1.add(1);
        roles.add(ra); roles.add(rb); roles.add(r1);

        Scene sa = new Scene(-1);
        sa.add(ra); sa.add(r1);
        Scene sb = new Scene(0);
        sb.add(rb); sb.add(r1);
        scenes.add(sa); scenes.add(sb);

        for (int vertix : v) {
            scenes.add(new Scene(vertix));
        }

        int roleCounter = 2;
        for (Edge edge : e) {
            Role r = new Role(roleCounter++);
            for (int actor : actors) if (actor != -1 && actor != 0) r.add(actor);
            for (Scene scene : scenes) {
                //System.out.println(scene.scene_no + " " + edge.u + " " + edge.v);
                if (scene.scene_no == edge.u || scene.scene_no == edge.v) {
                    scene.add(r);
                    //System.out.println("Added scene");
                }
            }
        }

        // Translate

        System.out.println(roles.size());
        System.out.println(scenes.size());
        System.out.println(actors.size());

        for (Role role : roles) {
            System.out.print(role.size());
            for (int actor : role) System.out.print(" " + Integer.toString(actor + 2));
            System.out.println();
        }

        for (Scene scene : scenes) if (scene.size() > 0) {
            System.out.print(scene.size());
            for (Role role : scene) System.out.print(" " + Integer.toString(role.role_no + 2));
            System.out.println();
        }
    }

    static int degree(int v, HashSet<Edge> e) {
        int count = 0;
        for (Edge edge : e) {
            if (edge.u == v || edge.v == v) count++;
        }
        return count;
    }

    static class Role extends ArrayList<Integer> {
        int role_no;
        Role(int number) {
            super();
            role_no = number;
        }
    }

    static class Scene extends ArrayList<Role> {
        int scene_no;
        Scene(int number) {
            super();
            scene_no = number;
        }
    }

    static class Edge {
        int u;
        int v;
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
}