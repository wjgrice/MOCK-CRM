/**
 *
 * @author William Grice
 */
package grice.wguc482pa;
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
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Modify Product controller
 */
public class ModifyProductController implements Initializable {
    // Declare data objects to hold fields
    FieldsDAO nameFields,stockFields,priceFields,minFields,maxFields;
    // Tag field names and labels from addProductscreen.
    @FXML
    private TextField modProdName, modProductInv, modProdPrice,modProdMax, modProdMin, modProdId;
    @FXML private Label modProdNameLabel, modProdNameWarn,modProdStockLabel,modProdStockWarn,
            modProdPriceLabel, modProdPriceWarn,modProdMinLabel,modProdMinWarn,
            modProdMaxLabel, modProdMaxWarn;
    @FXML private TableView<Part> modProdPartTbl;
    @FXML private TableColumn<Object, Object> modProdPartIdCol;
    @FXML private TableColumn<Object, Object> modProdPartNameCol;
    @FXML private TableColumn<Object, Object> modProdPartPriceCol;
    @FXML private TableColumn<Object, Object> modProdPartInvCol;
    @FXML private TableView<Part> modProdAscTbl;
    @FXML private TableColumn<Object, Object> modProdAscIdCol;
    @FXML private TableColumn<Object, Object> modProdAscNameCol;
    @FXML private TableColumn<Object, Object> modProdAscPriceCol;
    @FXML private TableColumn<Object, Object> modProdAscStockCol;
    // Recieve passed in list of parts from inventory
    public static Product incomingProd;

    /** Init method used to set up scene with incoming data and display parts and associated parts tabelviews.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Init container objects which will hold labels and text fields.
        nameFields = new FieldsDAO(modProdName, modProdNameLabel, modProdNameWarn, false);
        stockFields = new FieldsDAO(modProductInv, modProdStockLabel, modProdStockWarn, false);
        priceFields = new FieldsDAO(modProdPrice, modProdPriceLabel, modProdPriceWarn, false);
        minFields = new FieldsDAO(modProdMin, modProdMinLabel, modProdMinWarn, false);
        maxFields = new FieldsDAO(modProdMax, modProdMaxLabel, modProdMaxWarn, false);
        // Setup Associated Parts Table
        modProdPartTbl.setItems(Inventory.getAllParts());
        modProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProdPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        // Load the product parts table with all parts
        modProdAscTbl.setItems(incomingProd.getAllAssociatedParts());
        modProdAscIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdAscNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdAscPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProdAscStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        // Load fields with incoming data
        modProdId.setText(String.valueOf(incomingProd.getId()));
        modProdName.setText(incomingProd.getName());
        modProductInv.setText(String.valueOf(incomingProd.getStock()));
        modProdPrice.setText(String.valueOf(incomingProd.getPrice()));
        modProdMax.setText(String.valueOf(incomingProd.getMax()));
        modProdMin.setText(String.valueOf(incomingProd.getMin()));
    }

    /**
     * Returns application to main view by loading mainController.
     *
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/grice/wguc482pa/mainView.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private TextField modProductSearch;
    @FXML private Label modProductSearchLabel;
    /**
     * Key listener attached to prod Search box.  Dynamically displays a filtered in product table view based on user entry
     * currently in search box.
     */
    @FXML
    public void modProdSearchKeystroke(){
        FilteredList<Part> nameProductFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); //obslist for name filtering
        FilteredList<Part> idProductFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // obslist for id filtering
        String filter = modProductSearch.getText(); // Initialize filter by getting text from search box
        modProductSearchLabel.setText(""); // Initialize Status label by setting to empty string.
        modProdPartTbl.getSelectionModel().clearSelection(); // Initialize tableview Selection by clearing it

        if(filter == null || filter.length() == 0) {
            // Check if filter is empty and set tableview to unfiltered if so
            nameProductFilter.setPredicate( p -> true);
            modProdPartTbl.setItems(nameProductFilter);}
        else {
            // If filter has value then apply filter to nameProductFilter list and use it to set Products list tableview
            nameProductFilter.setPredicate(Product -> Product.getName().toLowerCase().contains(filter.toLowerCase()));
            modProdPartTbl.setItems(nameProductFilter);
            if(nameProductFilter.size() == 1) {
                // If nameProductfilter has a single item set tableView to select it
                modProdPartTbl.getSelectionModel().selectLast();
            }
            if(nameProductFilter.isEmpty()){
                // If no matches using nameProductFilter check using idProductFilter list
                idProductFilter.setPredicate(Product -> Product.getIdString().contains(filter));
                modProdPartTbl.setItems(idProductFilter);
                if(idProductFilter.size() == 1) {
                    // If idProductfilter has a single item set tableView to select it
                    modProdPartTbl.getSelectionModel().selectLast();
                }
                if(idProductFilter.isEmpty()){
                    // If both searches come back empty change label to indicate "No Results Found" status.
                    modProductSearchLabel.setText("No Results Found");
                }
            }
        }
    }

    public static void setIncomingProd(Product incomingProd) {
        ModifyProductController.incomingProd = incomingProd;
    }

    @FXML private Label modProdAddPartWarn, modProdRmvWarn;

    /**
     * Key Listener attached to add button.  Validates user entry and add parts from assocated parts table to
     * inventory object for selected product
     */
    public void modProdAddBtn() {
        // Check for part selection.  Throw warning label if no selection present
        if(modProdPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(modProdAddPartWarn, "Select item from Part list before proceeding");
        } else {
            incomingProd.addAssociatedPart(modProdPartTbl.getSelectionModel().getSelectedItem());
            modProdAscTbl.setItems(incomingProd.getAllAssociatedParts());
        }
    }

    /**
     * Key listener attached to Remove button.  Validates user entry and updates associated products table.
     */
    public void modProdRmvBtn() {
        if(modProdAscTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(modProdRmvWarn, "Select item from Part list before proceeding");
        } else {
            // Remove part from associated parts list for selected product.
            // Setup and display dialog box to confirm action
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Confirm");
            alert.setContentText("Delete selected Part?");
            Optional<ButtonType> res = alert.showAndWait();
            if(res.get() == ButtonType.OK) {
                // Pass selected part to mod screen by setting static member in modPartController
                incomingProd.deleteAssociatedPart(modProdAscTbl.getSelectionModel().getSelectedItem());
                modProdAscTbl.setItems(incomingProd.getAllAssociatedParts());
            }

        }
    }

    /**
     * Key listener attached to Save button validates user entry. Sets up DAO object for asscociated fields/labels, and
     * Checks fields for input validity.  If all checks pass adds product to inventory and returns to main.
     *
     * @return boolean
     */
    public boolean modProdSaveBtn(ActionEvent actionEvent) throws IOException {
        // Load label containers into list
        FieldsDAO[] allFields = {nameFields, stockFields, priceFields, minFields, maxFields};

        // Setup variables for easy part creation
        String name = allFields[0].textField.getText();
        Double price = Helper.checkDbl(allFields[2]).getValue();
        Integer stock = Helper.checkInt(allFields[1]).getValue();
        Integer min = Helper.checkInt(allFields[3]).getValue();
        Integer max = Helper.checkInt(allFields[4]).getValue();

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
            if(Inventory.deleteProduct(incomingProd)) {
                Product newProd = new Product(Inventory.getCounter(), name, price, stock, min, max);
                for (Part part : incomingProd.getAllAssociatedParts()) {
                    newProd.addAssociatedPart(part);
                }
                Inventory.addProduct(newProd);
                toMain(actionEvent);
            }
        }
        return true;
    }
}

