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
    public static Part lookupPart(int partId){

        return null;
    }

    /**
     *
     *
     */
    public static Product lookupProduct(int productId){

        return null;
    }

    /**
     *
     *
     */
    public static  ObservableList<Part> lookupPart(String partName){

        return null;
    }

    /**
     *
     *
     */
    public static  ObservableList<Product> lookupProduct(String productName){

        return null;
    }

    /**
     *
     *
     */
    public static  void updatePart(int index, Part selectedPart){

    }

    /**
     *
     *
     */
    public static void updateProduct(int index, Part selectedProduct){

    }

    /**
     *
     *
     */
    public static boolean deletePart(Part selectedPart){

        return false;
    }

    /**
     *
     *
     */
    public static boolean deleteProduct(Part selectedProduct){

        return false;
    }

    /**
     * Return a filtered list containing all Parts
     *
     * @return allParts List
     */
    public static  ObservableList<Part> getAllParts(){
        FilteredList<Part> filteredPartList = new FilteredList<>(allParts, p -> true);
        return filteredPartList;
    }

    /**
     * Return a filtered list containing all Products
     *
     * @return allProducts List
     */
    public static  ObservableList<Product> getAllProducts(){
        FilteredList<Product> filteredProductList = new FilteredList<>(allProducts, p -> true);
        return filteredProductList;
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
        String s = String.valueOf(counter+1);
        return s;
    }

}
