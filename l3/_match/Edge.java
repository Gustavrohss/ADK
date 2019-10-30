/**
 * Kant i en graf
 * Egentligen närmare en generell 2-integer-tuple
 *  både i funktion och användning
 */
class Edge {
    public int u;
    public int v;
    Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        return (o.getClass() == Edge.class 
        && ((Edge) o).u == u 
        && ((Edge) o).v == v); 
    }

    @Override
    public int hashCode() {
        return u * 20000 + v;
    }

    @Override
    public String toString() {
        return u + " " + v;
    }
}