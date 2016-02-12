package drones;

/**
 * @author Orestis Melkonian
 */
public class Coords {
    public int x, y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
