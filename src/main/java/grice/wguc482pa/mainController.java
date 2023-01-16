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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    public void addPart(ActionEvent actionEvent) throws IOException {
        screenChange("/grice/wguc482pa/addPartView.fxml", actionEvent);
    }

    @FXML
    private Label mainPartModWarning;
    public void modPart(ActionEvent actionEvent) throws IOException {
        // Check for part selection.  Throw warning label if no selection present
        if(mainPartTbl.getSelectionModel().isEmpty()) {
            Helper.setWarningLabel(mainPartModWarning, "Select item from Part list before proceeding");
        } else {
            // Pass selected part to mod screen by setting static member in modPartController
            ModifyPartController.selectedPart(mainPartTbl.getSelectionModel().getSelectedItem());
            // Load modPartController
            screenChange("/grice/wguc482pa/modifyPartView.fxml", actionEvent); }
    }

    public void addProduct(ActionEvent actionEvent) throws IOException {
        screenChange("/grice/wguc482pa/addProductView.fxml", actionEvent);
    }

    public void modProduct(ActionEvent actionEvent) throws IOException {
        screenChange("/grice/wguc482pa/modifyProductView.fxml", actionEvent);
    }
    public void screenChange(String path, ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField mainPartSearch; // Text entry field to capture parts searches
    @FXML
    private Label mainPartSearchStatusLabel; // Display parts search result status
    FilteredList<Part> namePartFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // list for name filtering
    FilteredList<Part> idPartFilter = new FilteredList<>(Inventory.getAllParts(), p -> true); // list for id filtering
    @FXML
    public void mainPartsSearchKeyStroke() throws IOException {
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
    FilteredList<Product> nameProductFilter = new FilteredList<>(Inventory.getAllProducts(), p -> true); //obslist for name filtering
    FilteredList<Product> idProductFilter = new FilteredList<>(Inventory.getAllProducts(), p -> true); // obslist for id filtering
    @FXML
    public void mainProductSearchKeystroke() throws IOException {
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
                idProductFilter.setPredicate(Product -> Product.getIdString().contains(filter));
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

    public void appExit(ActionEvent actionEvent) throws IOException {
        System.exit(0);
    }

}