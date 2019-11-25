import java.io.*;
import java.util.*;

/**
 * Heuristic:
 * Solves the Rollbesättningsproblem (RB) using a heuristic.
 * Cheats. (Superactors)
 * 
 * RBInstanceBean is used to describe an instance of RB.
 * All instances read are yes-instances, thanks to the use of superactors.
 * This Bean is read through a method that reads an RB instance on standard input.
 * The Bean is then passed to the Heuristic class constructor.
 * 
 * A naive and poor solution is first generated.
 * This solution is then to be used in some heuristic.
 * Notably, local search.
 */

class Heuristic {

    private Map<Role, Integer> currentAssignment;

    public Heuristic(RBInstanceBean instance) {
        
        currentAssignment = new HashMap<>();

        // Construct naive solution for local search

        /* First, assign roles to divas
           Find their respective possible roles,
           ensure compatibility between any two of them
           At least one solution is ensured by problem description
           Behavior is undefined otherwise
         */

        ArrayList<Role> rd1 = new ArrayList<>();
        ArrayList<Role> rd2 = new ArrayList<>();
        for (Role r : instance.roles) {
            if (r.contains(1)) rd1.add(r);
            if (r.contains(2)) rd2.add(r);
        }

        roleloop:
        for (Role d1 : rd1) for (Role d2 : rd2) {
            boolean compatible = true;

            sceneloop:
            for (Scene s : instance.scenes) {
                if (s.contains(d1) && s.contains(d2)) {
                    compatible = false;
                    break sceneloop;
                }
            }
            
            if (compatible) {
                currentAssignment.put(d1, 1);
                currentAssignment.put(d2, 2);
                break roleloop;
            }
        }

        // Time to fill the rest of the assignment

        // Attempt very naively to assign as many roles as possible to each actor
        for (int i = 3; i <= instance.k; i++) {
            for (Role r : instance.roles) {
                if (r.contains(i)) {
                    currentAssignment.put(r, i);
                    if (Verifier.partialVerify(currentAssignment, 
                        instance.scenes, instance.roles, instance.n + instance.k - 1)) 
                        continue;
                    else {
                        currentAssignment.remove(r, i);
                    }
                }
            }
        }

        // Finally, for unassigned roles, assign a superactor
        int superActorCount = instance.k + 1;
        for (Role r : instance.roles) {
            if (currentAssignment.get(r) == null) {
                currentAssignment.put(r, superActorCount++);
            }
        }
    }

    @Override
    public String toString() {
        return currentAssignment != null ? currentAssignment.toString() : "No current assignment";
    }

    public static void main(String[] args) {
        Heuristic heu = new Heuristic(readRBInstance());
        System.out.println(heu);
    }

    private static RBInstanceBean readRBInstance() {
        try (
            final BufferedReader stdin = new BufferedReader(
                new InputStreamReader(System.in)
            )) {

                int n = Integer.parseInt(stdin.readLine()); // Roles
                int s = Integer.parseInt(stdin.readLine()); // Scenes
                int k = Integer.parseInt(stdin.readLine()); // Actors

                ArrayList<Role> roles = new ArrayList<>(n);
                ArrayList<Scene> scenes = new ArrayList<>(s);

                for (int i = 1; i <= n; i++) roles.add(new Role(i));
                for (int i = 1; i <= s; i++) scenes.add(new Scene(i));

                // Read what roles can be played by what actors
                for (int i = 0; i < n; i++) {
                    String[] nums = stdin.readLine().split(" ");
                    for (int j = 1; j < nums.length; j++) 
                        roles.get(i).add(Integer.parseInt(nums[j]));
                }

                // Read what roles participate in what scenes
                for (int i = 0; i < s; i++) {
                    String[] nums = stdin.readLine().split(" ");
                    for (int j = 1; j < nums.length; j++) {
                        scenes.get(i).add(roles.get(Integer.parseInt(nums[j]) - 1));
                    }
                }

                return new RBInstanceBean(n, s, k, roles, scenes);
                
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    static class RBInstanceBean {
        final int n, s, k;
        final ArrayList<Role> roles;
        final ArrayList<Scene> scenes;

        RBInstanceBean(int n, int s, int k, ArrayList<Role> roles, ArrayList<Scene> scenes) {
            this.n = n;
            this.s = s;
            this.k = k;
            this.roles = roles;
            this.scenes = scenes;
        }
    }
}