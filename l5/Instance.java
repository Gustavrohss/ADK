import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Instance {

    public int n;
    public int s;
    public int k;
    public ArrayList<Role> roles;
    public ArrayList<Scene> scenes;
    public ArrayList<Actor> actors;

    public static Instance readInstance() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            
            int n = sint(in.readLine()); // Roles
            int s = sint(in.readLine()); // Scenes
            int k = sint(in.readLine()); // Actors

            var roles = new ArrayList<Role>(n);
            var scenes = new ArrayList<Scene>(s);
            var actors = new ArrayList<Actor>(k);

            for (int i = 0; i < n; i++) roles.add(new Role(i + 1));
            for (int i = 0; i < s; i++) scenes.add(new Scene(i + 1));
            for (int i = 0; i < k; i++) actors.add(new Actor(i + 1));

            for (int i = 0; i < n; i++) {
                String[] nums = in.readLine().split(" ");
                for (int j = 1; j < nums.length; j++) {
                    Actor a = actors.get(sint(nums[j]) - 1);
                    a.canPlay.add(roles.get(i));
                }
            }

            for (int i = 0; i < s; i++) {
                String[] nums = in.readLine().split(" ");
                for (int j = 1; j < nums.length; j++) {
                    Role r = roles.get(sint(nums[j]) - 1);
                    r.inScenes.add(scenes.get(i));
                    scenes.get(i).playedBy.add(r);
                }
            }

            Instance i = new Instance();
            i.n = n;
            i.s = s;
            i.k = k;
            i.roles = roles;
            i.scenes = scenes;
            i.actors = actors;
            return i;

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static int sint(String s) {
        return Integer.parseInt(s);
    }
}