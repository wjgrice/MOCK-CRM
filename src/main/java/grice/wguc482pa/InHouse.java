package grice.wguc482pa;

/**
 * A model of an in-house.  Extends the Part class.
 * @author William Grice
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
