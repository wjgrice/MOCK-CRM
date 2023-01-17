package grice.wguc482pa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A model of associated parts that together form a product.
 *
 * @author William Grice
 *
 */
public class Product {

    /**
     * A collection of parts that form the bill of materials for a product.
     */
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * The ID of the product.
     */
    private int id;

    /**
     * The Name of the product.
     */
    private String name;

    /**
     * The Price of the product.
     */
    private double price;

    /**
     * The amount of inventory available of the product.
     */
    private int stock;

    /**
     * The Minimum amount of inventory to maintain of the product.
     */
    private int min;

    /**
     * The maximum amount of inventory to maintain of the product.
     */
    private int max;

    /**
     * The constructor used for a new instance of a Product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     *
     */
    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);
    }
    /**
     *
     *
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        try {
            this.associatedParts.remove(selectedAssociatedPart);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     *
     *
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

}

