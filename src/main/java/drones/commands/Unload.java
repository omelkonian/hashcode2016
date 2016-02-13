package drones.commands;

/**
 * @author Orestis Melkonian
 */
public class Unload extends Command {
    int warehouseID, productID, productNo;

    public Unload(int droneID, int warehouseID, int productID, int productNo) {
        super(droneID);
        this.warehouseID = warehouseID;
        this.productID = productID;
        this.productNo = productNo;
    }

    @Override
    public String toString() {
        return droneID + " U " + warehouseID + " " + productID + " " + productNo;
    }
}
