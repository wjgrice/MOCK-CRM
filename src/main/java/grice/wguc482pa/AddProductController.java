package grice.wguc482pa;
/**
 *
 * @author William Grice
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    // Declare data objects to hold fields
    FieldsDAO nameFields,stockFields,priceFields,minFields,maxFields;
    // Tag field names and labels from addProductscreen.
    @FXML private TextField addProductName, addProductInv, addProductPrice,addProductMax, addProductMin;
    @FXML private Label addProductNameLabel, addProductNameWarning,addProductStockLabel,addProductStockWarning,
                        addProductPriceLabel, addProductPriceWarning,addProductMinLabel,addProductMinLabelWarning,
                        addProductMaxLabel, addProductMaxLabelWarning;
    @FXML private TableView<Part> addProductPartTbl;
    @FXML private TableColumn<Object, Object> addProductPartIdCol;
    @FXML private TableColumn<Object, Object> addProductPartNameCol;
    @FXML private TableColumn<Object, Object> addProductPartPriceCol;
    @FXML private TableColumn<Object, Object> addProductPartStockCol;
    @FXML private TableView<Part> addProductAssociatedPartsTbl;
    @FXML private TableColumn<Object, Object> addProductAssociatedPartsIdCol;
    @FXML private TableColumn<Object, Object> addProductAssociatedPartsNameCol;
    @FXML private TableColumn<Object, Object> addProductAssociatedPartsPriceCol;
    @FXML private TableColumn<Object, Object> addProductAssociatedPartsStockCol;

    /**
     * Inits the controller each time it is called.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets ID field to accurate placeholder value using the Inventory counter method
        addProductId.setText(String.valueOf(Inventory.getCounter()));
        // Init container objects which will hold labels and text fields.
        nameFields = new FieldsDAO(addProductName, addProductNameLabel, addProductNameWarning, false);
        stockFields = new FieldsDAO(addProductInv, addProductStockLabel, addProductStockWarning, false);
        priceFields = new FieldsDAO(addProductPrice, addProductPriceLabel, addProductPriceWarning, false);
        minFields = new FieldsDAO(addProductMin, addProductMinLabel, addProductMinLabelWarning, false);
        maxFields = new FieldsDAO(addProductMax, addProductMaxLabel, addProductMaxLabelWarning, false);
        // Setup Associated Parts Table
        addProductPartTbl.setItems(Inventory.getAllParts());
        addProductPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        // Load the product parts table with all parts
        addProductAssociatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAssociatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAssociatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductAssociatedPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    @FXML private Label addProductSearchLabel;
    @FXML private TextField addProductSearch;

    @FXML
    public void addProductSearchKeystroke(){
        FilteredList<Part> nameProductFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); //obslist for name filtering
        FilteredList<Part> idProductFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // obslist for id filtering
        String filter = addProductSearch.getText(); // Initialize filter by getting text from search box
        addProductSearchLabel.setText(""); // Initialize Status label by setting to empty string.
        addProductPartTbl.getSelectionModel().clearSelection(); // Initialize tableview Selection by clearing it

        if(filter == null || filter.length() == 0) {
            // Check if filter is empty and set tableview to unfiltered if so
            nameProductFilter.setPredicate( p -> true);
            addProductPartTbl.setItems(nameProductFilter);}
        else {
            // If filter has value then apply filter to nameProductFilter list and use it to set Products list tableview
            nameProductFilter.setPredicate(Product -> Product.getName().toLowerCase().contains(filter.toLowerCase()));
            addProductPartTbl.setItems(nameProductFilter);
            if(nameProductFilter.size() == 1) {
                // If nameProductfilter has a single item set tableView to select it
                addProductPartTbl.getSelectionModel().selectLast();
            }
            if(nameProductFilter.isEmpty()){
                // If no matches using nameProductFilter check using idProductFilter list
                idProductFilter.setPredicate(Product -> Product.getIdString().contains(filter));
                addProductPartTbl.setItems(idProductFilter);
                if(idProductFilter.size() == 1) {
                    // If idProductfilter has a single item set tableView to select it
                    addProductPartTbl.getSelectionModel().selectLast();
                }
                if(idProductFilter.isEmpty()){
                    // If both searches come back empty change label to indicate "No Results Found" status.
                    addProductSearchLabel.setText("No Results Found");
                }
            }
        }
    }
    @FXML private Label addProductAddPartWarning, addProductRmvWarning;
    // Create bill of material List to contain associated parts selected by user.
    ObservableList<Part> bom = FXCollections.observableArrayList();

    public void addPartBtn(){
        // Check for part selection.  Throw warning label if no selection present
        if(addProductPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(addProductRmvWarning, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            bom.add(addProductPartTbl.getSelectionModel().getSelectedItem());
            addProductAssociatedPartsTbl.setItems(bom);
        }
    }

    public void addProductRemovePart() {
        if(addProductAssociatedPartsTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(addProductAddPartWarning, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            bom.remove(addProductPartTbl.getSelectionModel().getSelectedItem());
            addProductAssociatedPartsTbl.setItems(bom);
        }
    }
    @FXML private TextField addProductId;
    /**
     * Validates data entry fields and saves new product to inventory.
     *
     */
    @FXML
    public boolean addProductSave(ActionEvent actionEvent) throws IOException {
        // Load label containers into list
        FieldsDAO[] allFields = {nameFields, stockFields, priceFields, minFields, maxFields};

        // Setup variables for easy part creation
        String name = allFields[0].textField.getText();
        Double price = (Double) Helper.checkDbl(allFields[2]).getValue();
        Integer stock = (Integer) Helper.checkInt(allFields[1]).getValue();
        Integer min = (Integer) Helper.checkInt(allFields[3]).getValue();
        Integer max = (Integer) Helper.checkInt(allFields[4]).getValue();

        // All fields have any entry of the correct type.  Now check validity of min/max and stock levels.
        if (Helper.noAlerts(allFields)){
            if(min > max){
                Helper.setAlert(allFields[3], "MIN not less than MAX");
                return false;
            }
            if(stock < min || stock > max){
                Helper.setAlert(allFields[1], "Must be between MIN/MAX");
                return false;
            }
        }

        // Validation complete add Product to inventory
        if (Helper.noAlerts(allFields)) {
            Product newProd = new Product(Inventory.getCounter(), name, price, stock, min, max);
            for (Part part: bom) { newProd.addAssociatedPart(part); }
            Inventory.addProduct(newProd);
            toMain(actionEvent);
        }
        return true;
    }

    /**
     * Returns application to main view by loading mainController.
     *
     * @param actionEvent Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/grice/wguc482pa/mainView.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
