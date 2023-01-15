package grice.wguc482pa;

/**
 * A model of an out sourced part.  Extends the Part Class.
 * @author William Grice
 */
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * @return the companyName
     */
    public String getCompanyName() { return companyName; }

    /**
     * @param machineId the companyName to set
     */
    public void setCompanyName(String machineId) {
        this.companyName = machineId;
    }
}
