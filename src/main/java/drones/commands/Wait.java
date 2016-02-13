package drones.commands;

/**
 * @author Orestis Melkonian
 */
public class Wait extends Command {
    int turns;

    public Wait(int droneID, int turns) {
        super(droneID);
        this.turns = turns;
    }

    @Override
    public String toString() {
        return droneID + " W " + turns;
    }
}
