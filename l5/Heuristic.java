import java.util.ArrayList;
import java.util.Random;

class Heuristic {

    public final Random rng = new Random();
    public Instance instance;

    public Heuristic(Instance i) {
        this(i.n, i.s, i.k, i.roles, i.scenes, i.actors);
        instance = i;
    }

    public Heuristic(int n, int s, int k, ArrayList<Role> roles, ArrayList<Scene> scenes, ArrayList<Actor> actors) {
        
        Actor d1 = Actor.d1;
        Actor d2 = Actor.d2;

        // Divas
        roleloop:
        for (Role r1 : d1.canPlay) for (Role r2 : d2.canPlay) {
            if (r1.equals(r2)) continue;

            boolean compatible = true;
            sceneloop:
            for (Scene s1 : r1.inScenes) {
                if (r2.inScenes.contains(s1)) {
                    compatible = false;
                    break sceneloop;
                }
            }

            if (compatible) {
                d1.assign(r1);
                d2.assign(r2);
                break roleloop;
            }
        }

        for (Role role : roles) {
            for (Actor actor : actors) {
                if (actor.canAssign(role)) actor.assign(role);
            }
        }

        int superActor = k + 1;
        for (Role role : roles) {
            if (role.assignedTo == null) {
                Actor actor = new Actor(superActor++);
                actor.assign(role);
                actors.add(actor);
            }
        }
    }

    public boolean localSearchStep() {
        // Find actor to give additional role
        Actor plus = instance.actors.get(rng.nextInt(instance.actors.size()));
        // Try again if superactor
        if (plus.id > instance.k) return false;
        // Try again if it would mean additional assignments
        if (plus.assignedRoles.size() == 0) return false;

        // Find test role
        Role additional = plus.canPlay.get(rng.nextInt(plus.canPlay.size()));

        // If not an option, try again
        if (!plus.canAssign(additional)) return false;

        boolean better = false;
        if (additional.assignedTo.assignedRoles.size() == 1) better = true;
        plus.assign(additional);
        return better;
    }

    public void printSolution() {
        ArrayList<Actor> assigned = new ArrayList<>();
        for (Actor a : instance.actors) if (a.assignedRoles.size() > 0) assigned.add(a);
        System.out.println(assigned.size());
        for (Actor a : assigned) {
            String s = a.id + " " + a.assignedRoles.size();
            for (Role r : a.assignedRoles) {
                s += " " + r.id;
            }
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        Instance instance = Instance.readInstance();
        Heuristic heuristic = new Heuristic(instance);

        int steps = 3000;
        for (int i = 0; i < steps; i++) {
            heuristic.localSearchStep();
        }
        heuristic.printSolution();
    }

}