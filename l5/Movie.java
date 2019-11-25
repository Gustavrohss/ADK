import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Role extends ArrayList<Integer> {

    private static final Random r = new Random();
    private final int id;

    public Role(int id) {
        super();
        this.id = id;
    }

    public int getRandom() {
        return get(r.nextInt(size()));
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ROLE " + id + ": " + super.toString();
    }
}

class Scene extends ArrayList<Role> {

    private static final Random r = new Random();
    private final int id;

    public Scene(int id) {
        super();
        this.id = id;
    }

    public Role getRandom() {
        return get(r.nextInt(size()));
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SCENE " + id + ": " + super.toString();
    }
}

class Verifier {
    /** 
     * Assumes that the problem instance is soundly constructed.
     *  Divas are numbered 1, 2 respectively.
     *  All scenes have at least two participating actors.
     * 
     * This is meant to verify a solution for a yes-instance of RB.
     */
    public static boolean verifyAllowed(
        Map<Role, Integer>  assignments,
        ArrayList<Scene>    scenes,
        ArrayList<Role>     roles,
        int                 maxActors
    ) {

        // Verify that all roles have been assigned.
        for (Role r : roles)
            if (!assignments.keySet().contains(r)) {
                System.out.println("Role not assigned: " + r.getId());
                System.out.println("Assignments: " + assignments.toString());
                return false;
            }

        // Verify that divas have roles
        if (!assignments.values().contains(1) ||
            !assignments.values().contains(2)) {
                System.out.println("Diva lacks role");
                System.out.println("Assignments: " + assignments.toString());
                return false;
            }

        // Verify that divas never play in the same scene
        // Find all roles of diva 1
        ArrayList<Role> diva1Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) == 1) 
                diva1Roles.add(r);
        }

        // Same for diva 2
        ArrayList<Role> diva2Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) == 2)
                diva2Roles.add(r);
        }

        // Check for clashes between divas
        for (Scene s : scenes) {
            boolean d1 = false;
            boolean d2 = false;
            for (Role r : diva1Roles)
                if (s.contains(r))
                    d1 = true;
            for (Role r : diva2Roles)
                if (s.contains(r))
                    d2 = true;
            if (d1 && d2) {
                System.out.println("Both divas appear in scene: " + s.toString());
                System.out.println("Assignments: " + assignments.toString());
                return false;
            }
        }

        // Check other clashes
        // p for person
        for (int p = 1; p <= maxActors; p++) {
            ArrayList<Role> rolesByActor = new ArrayList<>();
            for (Role r : roles) 
                if (assignments.get(r) == p)
                    rolesByActor.add(r);
            for (Scene s : scenes) {
                int rolesInScene = 0;
                for (Role r : rolesByActor)
                    if (s.contains(r))
                        rolesInScene++;
                if (rolesInScene > 1) {
                    System.out.println("Actor " + p + " has several role in scene: " + s.toString());
                    System.out.println("Actor has been given following roles: " + rolesByActor.toString());
                    System.out.println("Assignments: " + assignments.toString());
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Ignores checking if all roles are assigned.
     */
    public static boolean partialVerify(
        Map<Role, Integer>  assignments,
        ArrayList<Scene>    scenes,
        ArrayList<Role>     roles,
        int                 maxActors
    ) {

        // Verify that divas have roles
        if (!assignments.values().contains(1) ||
            !assignments.values().contains(2)) {
                System.out.println("Diva lacks role");
                System.out.println("Assignments: " + assignments.toString());
                return false;
            }

        // Verify that divas never play in the same scene
        // Find all roles of diva 1
        ArrayList<Role> diva1Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) != null && assignments.get(r) == 1) 
                diva1Roles.add(r);
        }

        // Same for diva 2
        ArrayList<Role> diva2Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) != null && assignments.get(r) == 2)
                diva2Roles.add(r);
        }

        // Check for clashes between divas
        for (Scene s : scenes) {
            boolean d1 = false;
            boolean d2 = false;
            for (Role r : diva1Roles)
                if (s.contains(r))
                    d1 = true;
            for (Role r : diva2Roles)
                if (s.contains(r))
                    d2 = true;
            if (d1 && d2) {
                System.out.println("Both divas appear in scene: " + s.toString());
                System.out.println("Assignments: " + assignments.toString());
                return false;
            }
        }

        // Check other clashes
        // p for person
        for (int p = 1; p <= maxActors; p++) {
            ArrayList<Role> rolesByActor = new ArrayList<>();
            for (Role r : roles) 
                if (assignments.get(r) != null && assignments.get(r) == p)
                    rolesByActor.add(r);
            for (Scene s : scenes) {
                int rolesInScene = 0;
                for (Role r : rolesByActor)
                    if (s.contains(r))
                        rolesInScene++;
                if (rolesInScene > 1) {
                    System.out.println("Actor " + p + " has several role in scene: " + s.toString());
                    System.out.println("Actor has been given following roles: " + rolesByActor.toString());
                    System.out.println("Assignments: " + assignments.toString());
                    return false;
                }
            }
        }
        
        return true;
    }

    public static void main(String[] args) {
        int maxActors = 3;

        ArrayList<Role> roles = new ArrayList<>();
        Role r1 = new Role(1);
        Role r2 = new Role(2);
        Role r3 = new Role(3);
        Role r4 = new Role(4);
        r1.add(1);
        r2.add(2);
        r3.add(3);
        roles.add(r1);
        roles.add(r2);
        roles.add(r3);
        roles.add(r4);

        ArrayList<Scene> scenes = new ArrayList<>();
        Scene s1 = new Scene(1);
        Scene s2 = new Scene(2);
        s1.add(r1);
        s1.add(r3);
        s2.add(r2);
        s2.add(r3);
        s2.add(r4);
        scenes.add(s1);
        scenes.add(s2);

        Map<Role, Integer> assignments = new HashMap<>();
        assignments.put(r1, 1);
        assignments.put(r2, 2);
        assignments.put(r3, 3);
        System.out.println(
            Verifier.verifyAllowed(assignments, scenes, roles, maxActors)
        );
        System.out.println(
            Verifier.partialVerify(assignments, scenes, roles, maxActors)
        );
    }
}