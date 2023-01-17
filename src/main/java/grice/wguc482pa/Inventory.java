package grice.wguc482pa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * A model of an inventory containing parts and part assemblies known as products.
 * @author William Grice
 */
public class Inventory {

    /**
     *
     * A counter used to increment ID number creation.
     */
    private static int counter = 1000;

    /**
     *
     * A list of Parts
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     *
     * A list of Products
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * Add newly created Parts to the list of all parts and increment the counter
     */
    public static  void addPart(Part newPart){

        allParts.add(newPart);
    }

    /**
     *
     * Add newly created Products to the list of all products and increment the counter
     */
    public static void addProduct(Product newProduct){

        allProducts.add(newProduct);
    }

    /**
     *
     *
     */
    public static Part lookupPart(int id){
        for (Part part : allParts){
            if (id == part.getId()){
                // Return Found Part
                return part;
            }
        }
        // Part not found return Null
        return null;
    }

    /**
     *
     *
     */
    public static Product lookupProduct(int id){
        for (Product product : allProducts){
            if (id == product.getId()){
                // Return Found Part
                return product;
            }
        }
        // Part not found return Null
        return null;
    }

    /**
     *
     *
     */
    public static  ObservableList<Part> lookupPart(String partName){
        // Create list to hold response
        ObservableList<Part> resList = FXCollections.observableArrayList();
        // If name is empty return entire product list
        if(partName.length() == 0) {
            resList = allParts;
        }
        else {
            // Else iterate through all products return just the name that was required
            for (Part part : allParts) {
                if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                    resList.add(part);
                }
            }
        }
        return resList;
    }

    /**
     *
     *
     */
    public static  ObservableList<Product> lookupProduct(String productName){
        // Create list to hold response
        ObservableList<Product> resList = FXCollections.observableArrayList();
        // If name is empty return entire product list
        if(productName.length() == 0) {
            resList = allProducts;
        }
        else {
            // Else iterate through all products return just the name that was required
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                    resList.add(product);
                }
            }
        }
        return resList;
    }

    /**
     *
     *
     */
    public static  void updatePart(int index, Part selectedPart){

        allParts.set(index, selectedPart);
    }

    /**
     *
     *
     */
    public static void updateProduct(int index, Product selectedProduct){

        allProducts.set(index, selectedProduct);
    }

    /**
     *
     *
     */
    public static boolean deletePart(Part selectedPart){
        try {
            allParts.remove(selectedPart);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     *
     *
     */
    public static boolean deleteProduct(Product selectedProduct){
        try {
            allProducts.remove(selectedProduct);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Return a filtered list containing all Parts
     *
     * @return allParts List
     */
    public static  ObservableList<Part> getAllParts(){
        return new FilteredList<>(allParts, p -> true);
    }

    /**
     * Return a filtered list containing all Products
     *
     * @return allProducts List
     */
    public static  ObservableList<Product> getAllProducts(){
        return new FilteredList<>(allProducts, p -> true);
    }

    /**
     *
     * @return the counter
     */
    public static int getCounter() {
        counter++;
        return counter;
    }
    /**
     *
     * @return next value without incrementing counter
     */
    public static String getTempCounter() {
        return String.valueOf(counter+1);
    }

}
