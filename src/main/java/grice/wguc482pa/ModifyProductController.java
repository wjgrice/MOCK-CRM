package grice.wguc482pa;

/**
 * The controller for the modify product view.
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

    @FXML private TextField modProductSearch;
    @FXML private Label modProductSearchLabel;
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

    public void modProdAddBtn(ActionEvent actionEvent) {
        // Check for part selection.  Throw warning label if no selection present
        if(modProdPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(modProdAddPartWarn, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            incomingProd.addAssociatedPart(modProdPartTbl.getSelectionModel().getSelectedItem());
            modProdAscTbl.setItems(incomingProd.getAllAssociatedParts());
        }
    }
    public void modProdRmvBtn() {
        if(modProdAscTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(modProdRmvWarn, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            incomingProd.deleteAssociatedPart(modProdAscTbl.getSelectionModel().getSelectedItem());
            modProdAscTbl.setItems(incomingProd.getAllAssociatedParts());
        }
    }
//End of Class
}

