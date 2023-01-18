/**
 *
 * @author William Grice
 */
package grice.wguc482pa;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main controller which controls logic for the main scene.
 */
public class mainController implements Initializable {
    @FXML
    private TableView<Part> mainPartTbl;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableColumn<Part, Integer> partStockCol;
    @FXML
    private TableColumn<Part, Integer> partMinCol;
    @FXML
    private TableColumn<Part, Integer> partMaxCol;
    @FXML
    private TableView<Product> mainProductTbl;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;
    @FXML
    private TableColumn<Product, Integer> productStockCol;
    @FXML
    private TableColumn<Product, Integer> productMinCol;
    @FXML
    private TableColumn<Product, Integer> productMaxCol;

    /**
     * Init method used to set up initial state of main controller screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populate Main Parts List with values from observablelist of parts
        mainPartTbl.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partMinCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        partMaxCol.setCellValueFactory(new PropertyValueFactory<>("max"));

        //Populate Main Product List with values from observables list of products
        mainProductTbl.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productMinCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        productMaxCol.setCellValueFactory(new PropertyValueFactory<>("max"));
    }

    /**
     * Event Listener attached to add Part button changes to add Part scene when called.
     */
    public void addPart(ActionEvent actionEvent) throws IOException {
        Helper.screenChange("/grice/wguc482pa/addPartView.fxml", actionEvent);
    }

    /**
     * Event Listener attached to add Product button changes to add Part scene when called.
     */
    public void addProduct(ActionEvent actionEvent) throws IOException {
        Helper.screenChange("/grice/wguc482pa/addProductView.fxml", actionEvent);
    }


    @FXML
    private Label mainPartModWarning;
    /**
     * Event Listener attached to Modify Part button.  Validates item selection and passes selected
     * item to modifyPartController
     */
    public void modPart(ActionEvent actionEvent) throws IOException {
        // Check for part selection.  Throw warning label if no selection present
        if(mainPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(mainPartModWarning, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            ModifyPartController.selectedPart(mainPartTbl.getSelectionModel().getSelectedItem());
            // Load modPartController
            Helper.screenChange("/grice/wguc482pa/modifyPartView.fxml", actionEvent); }
    }

    @FXML private Label mainProductWarning;
    /**
     * Event Listener attached to Modify Product button.  Validates item selection and passes selected
     * item to modifyProductController
     */
    public void modProduct(ActionEvent actionEvent) throws IOException {
        if (mainProductTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(mainProductWarning, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            ModifyProductController.setIncomingProd(mainProductTbl.getSelectionModel().getSelectedItem());
            // Load modProductController
            Helper.screenChange("/grice/wguc482pa/modifyProductView.fxml", actionEvent);
        }
    }


    @FXML
    private TextField mainPartSearch; // Text entry field to capture parts searches
    @FXML
    private Label mainPartSearchStatusLabel; // Display parts search result status

    /**
     * Key listener attached to Part Search box.  Dynamically displays a filtered in Parts table view based on user entry
     * currently in search box.
     */
    @FXML
    public void mainPartsSearchKeyStroke() {
        FilteredList<Part> namePartFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // list for name filtering
        FilteredList<Part> idPartFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // list for id filtering
        String filter = mainPartSearch.getText(); // Initialize filter by getting text from search box
        mainPartSearchStatusLabel.setText(""); // Initialize Status label by setting to empty string.
        mainPartTbl.getSelectionModel().clearSelection(); // Initialize tableview Selection by clearing it

        if(filter == null || filter.length() == 0) {
            // Check if filter is empty and set tableview to unfiltered if so
            namePartFilter.setPredicate( p -> true);
            mainPartTbl.setItems(namePartFilter);}
        else {
            // If filter has value then apply filter to namePartFilter list and use it to set parts list tableview
            namePartFilter.setPredicate(part -> part.getName().toLowerCase().contains(filter.toLowerCase()));
            mainPartTbl.setItems(namePartFilter);
            if(namePartFilter.size() == 1) {
                // If namePartfilter has a single item set tableView to select it
                mainPartTbl.getSelectionModel().selectLast();
            }
            if(namePartFilter.isEmpty()){
                // If no matches using namePartFilter check using idPartFilter list
                  idPartFilter.setPredicate(part -> part.getIdString().contains(filter));
                mainPartTbl.setItems(idPartFilter);
                if(idPartFilter.size() == 1) {
                    // If idPartfilter has a single item set tableView to select it
                    mainPartTbl.getSelectionModel().selectLast();
                }
                if(idPartFilter.isEmpty()){
                    // If both searches come back empty change label to indicate "No Results Found" status.
                    mainPartSearchStatusLabel.setText("No Results Found");
                }
            }
        }
    }

    @FXML
    private TextField mainProductSearch; // Text entry field to capture Products searches
    @FXML
    private Label mainProductSearchStatusLabel; // Display Products search result status
    /**
     * Key listener attached to Product Search box.  Dynamically displays a filtered in Parts table view based on user entry
     * currently in search box.
     */
    @FXML
    public void mainProductSearchKeystroke(){
        FilteredList<Product> nameProductFilter = new FilteredList<>(Inventory.getAllProducts(), p -> true); //obslist for name filtering
        FilteredList<Product> idProductFilter = new FilteredList<>(Inventory.getAllProducts(), p -> true); // obslist for id filtering
        String filter = mainProductSearch.getText(); // Initialize filter by getting text from search box
        mainProductSearchStatusLabel.setText(""); // Initialize Status label by setting to empty string.
        mainProductTbl.getSelectionModel().clearSelection(); // Initialize tableview Selection by clearing it

        if(filter == null || filter.length() == 0) {
            // Check if filter is empty and set tableview to unfiltered if so
            nameProductFilter.setPredicate( p -> true);
            mainProductTbl.setItems(nameProductFilter);}
        else {
            // If filter has value then apply filter to nameProductFilter list and use it to set Products list tableview
            nameProductFilter.setPredicate(Product -> Product.getName().toLowerCase().contains(filter.toLowerCase()));
            mainProductTbl.setItems(nameProductFilter);
            if(nameProductFilter.size() == 1) {
                // If nameProductfilter has a single item set tableView to select it
                mainProductTbl.getSelectionModel().selectLast();
            }
            if(nameProductFilter.isEmpty()){
                // If no matches using nameProductFilter check using idProductFilter list
                idProductFilter.setPredicate(Product -> String.valueOf(Product.getId()).contains(filter));
                mainProductTbl.setItems(idProductFilter);
                if(idProductFilter.size() == 1) {
                    // If idProductfilter has a single item set tableView to select it
                    mainProductTbl.getSelectionModel().selectLast();
                }
                if(idProductFilter.isEmpty()){
                    // If both searches come back empty change label to indicate "No Results Found" status.
                    mainProductSearchStatusLabel.setText("No Results Found");
                }
            }
        }
    }

    /**
     * Close application
     */
    public void appExit(){
        System.exit(0);
    }

    /**
     * Confirm and delete currently selected part in Parts table view.
     */
    public void mainPartDel() {
        // Check for part selection.  Throw warning label if no selection present
        if(mainPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(mainPartModWarning, "Select item from Part list before proceeding");
        } else {
            // Setup and display dialog box to confirm action
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Confirm");
            alert.setContentText("Delete selected Part?");
            Optional<ButtonType> res = alert.showAndWait();
            if(res.get() == ButtonType.OK) {
                // Pass selected part to mod screen by setting static member in modPartController
                Inventory.deletePart(mainPartTbl.getSelectionModel().getSelectedItem());
            }
        }
    }
    /**
     * Confirm and delete currently selected product in Parts table view.
     */
    public void mainProductDel() {
        // Check for part selection.  Throw warning label if no selection present
        if(mainProductTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(mainProductWarning, "Select item from Part list before proceeding");
        } else {
            // If associated parts table empty then pass selected part to mod screen by setting static member in modPartController
            if(mainProductTbl.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()){
                // Setup and display dialog box to confirm action
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Confirm");
                alert.setContentText("Delete selected Product?");
                Optional<ButtonType> res = alert.showAndWait();
                if(res.get() == ButtonType.OK) {
                    Inventory.deleteProduct(mainProductTbl.getSelectionModel().getSelectedItem());
                }
            } else {
                // If associated parts not empty then throw warning label
                mainProductWarning.setText("Associated parts must be deleted first.");
            }
        }
    }
}