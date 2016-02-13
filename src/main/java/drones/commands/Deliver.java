package drones.commands;

/**
 * @author Orestis Melkonian
 */
public class Deliver extends Command {
    int orderID, productID, productNo;

    public Deliver(int droneID, int orderID, int productID, int productNo) {
        super(droneID);
        this.orderID = orderID;
        this.productID = productID;
        this.productNo = productNo;
    }

    @Override
    public String toString() {
        return droneID + " D " + orderID + " " + productID + " " + productNo;
    }
}
