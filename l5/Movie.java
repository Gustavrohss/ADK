import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    public int hashCode() {
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
    public int hashCode() {
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
        ArrayList<Role>     roles
    ) {

        // Verify that all roles have been assigned.
        for (Role r : roles)
            if (!assignments.keySet().contains(r)) {
                return false;
            }

        // Verify that divas have roles
        if (!assignments.values().contains(1) ||
            !assignments.values().contains(2)) {
                return false;
        }

        // Verify that divas never play in the same scene
        // Find all roles of divas
        ArrayList<Role> diva1Roles = new ArrayList<>();
        ArrayList<Role> diva2Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) != null && assignments.get(r) == 1) {
                if (assignments.get(r) == 1) diva1Roles.add(r);
                else 
                if (assignments.get(r) == 2) diva2Roles.add(r);
            }  
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
                return false;
            }
        }

        // Check other clashes
        for (Scene s : scenes) {
            int rolesInScene = s.size();
            Set<Integer> actorsInScene = new HashSet<>();
            for (Role r : s) actorsInScene.add(assignments.get(r));
            if (rolesInScene != actorsInScene.size()) {
                return false;
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
        ArrayList<Role>     roles
    ) {

        // Verify that divas have roles
        if (!assignments.values().contains(1) ||
            !assignments.values().contains(2)) {
                return false;
        }

        // Verify that divas never play in the same scene
        // Find all roles of divas
        ArrayList<Role> diva1Roles = new ArrayList<>();
        ArrayList<Role> diva2Roles = new ArrayList<>();
        for (Role r : roles) {
            if (assignments.get(r) != null && assignments.get(r) == 1) {
                if (assignments.get(r) == 1) diva1Roles.add(r);
                else 
                if (assignments.get(r) == 2) diva2Roles.add(r);
            }  
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
                return false;
            }
        }

        // Check other clashes
        for (Scene s : scenes) {
            int rolesInScene = s.size();
            int actorsInSceneNum = 0;
            Set<Integer> actorsInScene = new HashSet<>();
            for (Role r : s) {
                Integer a = assignments.get(r);
                if (a == null) actorsInSceneNum++;
                else actorsInScene.add(a);
            }
            actorsInSceneNum += actorsInScene.size();

            if (rolesInScene != actorsInSceneNum) {
                return false;
            }
        }
        
        return true;
    }
}