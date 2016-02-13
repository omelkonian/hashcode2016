package drones.commands;

/**
 * @author Orestis Melkonian
 */
public class Load extends Command {
    int warehouseID, productID, productNo;

    public Load(int droneID, int warehouseID, int productID, int productNo) {
        super(droneID);
        this.warehouseID = warehouseID;
        this.productID = productID;
        this.productNo = productNo;
    }

    @Override
    public String toString() {
        return droneID + " L " + warehouseID + " " + productID + " " + productNo;
    }
}
