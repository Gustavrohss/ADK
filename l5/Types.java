import java.util.ArrayList;
import java.util.HashSet;

class Actor {
    public final int id;
    public final ArrayList<Role> canPlay = new ArrayList<>();
    public final ArrayList<Role> assignedRoles = new ArrayList<>();
    
    public static Actor d1;
    public static Actor d2;

    public Actor(int id) {
        this.id = id;
        if (id == 1) d1 = this;
        if (id == 2) d2 = this;
    }

    public void assign(Role r) {
        if (r.assignedTo != null) {
            r.assignedTo.assignedRoles.remove(r);
        }
        r.assignedTo = this;
        assignedRoles.add(r);
    }

    public boolean canAssign(Role r) {
        if (!canPlay.contains(r)) return false;

        if (id == 1 || id == 2) {
            Actor d = id == 1 ? d2 : d1;
            for (Scene s : r.inScenes) {
                if (playsIn().contains(s) || d.playsIn().contains(s))
                    return false;
            }
            return true;
        }

        if (r.assignedTo != null && r.assignedTo.equals(d1)) {
            if (d1.assignedRoles.size() == 1) return false;
        }

        if (r.assignedTo != null && r.assignedTo.equals(d2)) {
            if (d2.assignedRoles.size() == 1) return false;
        }

        for (Scene s : r.inScenes) {
            if (playsIn().contains(s)) return false;
        }
        return true;
    }

    public HashSet<Scene> playsIn() {
        HashSet<Scene> out = new HashSet<>();
        for (Role r : assignedRoles) for (Scene s : r.inScenes)
            out.add(s);
        return out;
    }

    @Override
    public String toString() { return "Actor " + id; }
    @Override
    public int hashCode() { return id; }
}

class Role {
    public final int id;
    public final ArrayList<Scene> inScenes = new ArrayList<>();
    public Actor assignedTo = null;

    public Role(int id) {
        this.id = id;
    }

    @Override
    public String toString() { return "Role " + id; }
    @Override
    public int hashCode() { return id; }
}

class Scene {
    public final int id;
    public final ArrayList<Role> playedBy = new ArrayList<>();

    public Scene(int id) {
        this.id = id;
    }

    @Override
    public String toString() { return "Scene " + id; }
    @Override
    public int hashCode() { return id; }
}