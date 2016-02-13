package drones.commands;

/**
 * @author Orestis Melkonian
 */
public abstract class Command {
    public int droneID;

    public Command(int droneID) {
        this.droneID = droneID;
    }
}
