/**
 *
 * @author William Grice
 */
package grice.wguc482pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controls logic for add part screen.
 *
 *
 */
public class AddPartController implements Initializable {


    @FXML
    private TextField addPartId;
    @FXML
    private Button addPartSavebtn;

    @Override
    /**
     * Initializes add pard id text field to next available part number
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPartId.setText(Inventory.getTempCounter());
    }

    /**
     * Returns application to main view by loading mainController.
     *
     * @param actionEvent Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/grice/wguc482pa/mainView.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField addPartName;
    @FXML
    private TextField addPartInv;
    @FXML
    private TextField addPartCost;
    @FXML
    private TextField addPartMin;
    @FXML
    private TextField addPartMax;
    @FXML
    private TextField addPartSourceInput;
    @FXML
    private Label addPartNameLabel;
    @FXML
    private Label addPartNameWarning;
    @FXML
    private Label addPartStockWarning;
    @FXML
    private Label addPartInvLabel;
    @FXML
    private Label addPartCostWarning;
    @FXML
    private Label addPartMinMaxWarning;
    @FXML
    private Label addPartMinMaxLabel;
    @FXML
    private Label addPartSourceWarning;
    @FXML
    private Label addPartSourceLbl;
    @FXML
    private Label addPartCostLabel;
    @FXML
    private RadioButton addPartInHouse;

    /**
     * This method is run once the user clicks the Save button.  All Textfields are validated providing user
     * feedback as needed.  Once validation has completed the user new part is added to the Inventory datastore.
     * Upon part creation an action event is passed to the toMain() method which reloads the main screen.
     *
     * @param actionEvent Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    public void addPartSave(ActionEvent actionEvent) throws IOException {
        FieldsDAO partName = new FieldsDAO(addPartName, addPartNameLabel, addPartNameWarning, false);
        FieldsDAO partStock = new FieldsDAO(addPartInv, addPartInvLabel, addPartStockWarning, false);
        FieldsDAO partPrice = new FieldsDAO(addPartCost, addPartCostLabel, addPartCostWarning, false);
        FieldsDAO partMin = new FieldsDAO(addPartMin, addPartMinMaxLabel, addPartMinMaxWarning, false);
        FieldsDAO partMax = new FieldsDAO(addPartMax, addPartMinMaxLabel, addPartMinMaxWarning, false);
        FieldsDAO partSource = new FieldsDAO(addPartSourceInput, addPartSourceLbl, addPartSourceWarning, false);

        // Check all the fields for input and alert for any that are empty.
        // If not empty clear any alerts that are present
        // INIT list of with all fields loaded to check entry validation
        FieldsDAO[] allFields = {partName, partStock, partPrice, partMin, partMax, partSource};
        for (FieldsDAO field : allFields) {
            if (field.textField.getText().trim().equals("")) {
                Helper.setAlert(field, "Missing Input");
            } else {
                Helper.clearAlert(field);
            }
        }

        // Check validity of integer entries. INT validations are variable due to nature of source field.
        // Setup Arraylist which can be expanded as needed.
        ArrayList<FieldsDAO> intFields = new ArrayList<>();
        intFields.add(partStock);
        intFields.add(partPrice);
        intFields.add(partMin);
        intFields.add(partMax);
        // Check if In-House radio button is selected.  Add partSource to inFields for Int validation if so.
        if (addPartInHouse.selectedProperty().getValue()) {
            intFields.add(partSource);
        }

        for (FieldsDAO field : intFields) {
            Pair p = Helper.checkInt(field);
            if (p.getKey().equals(false)) {
                Helper.setAlert(field, "Enter Valid Integer");
            } else {
                Helper.clearAlert(field);
            }
        }

        // Check validity of double entries
        // INIT small list with just double value field that need to be checked.
        FieldsDAO[] doubleFields = {partPrice};
        for (FieldsDAO field : doubleFields) {
            Pair p = Helper.checkDbl(field);
            if (p.getKey().equals(false)) {
                Helper.setAlert(field, "Enter Valid Float");
            } else {
                Helper.clearAlert(field);
            }
        }

        if (!(partName.getErrState() && partPrice.getErrState() && partStock.getErrState() && partMin.getErrState() &&
                partMax.getErrState() && partSource.getErrState())) { System.out.print(partName.getErrState());



        // Setup variables for easy part creation
        int id = Inventory.getCounter();
        String name = partName.textField.getText();
        Double price = (Double) Helper.checkDbl(partPrice).getValue();
        Integer stock = (Integer) Helper.checkInt(partStock).getValue();
        Integer min = (Integer) Helper.checkInt(partMin).getValue();
        Integer max = (Integer) Helper.checkInt(partMax).getValue();
        // Check if InHouse or Outsourced use int or string as required for source field.
        if (addPartInHouse.selectedProperty().getValue()) {
            Integer source = (Integer) Helper.checkInt(partSource).getValue();
            Inventory.addPart(new InHouse(id, name, price, stock, min, max, source));
            toMain(actionEvent);
        }
        if (!(addPartInHouse.selectedProperty().getValue())) {
            String sourceTxt = partSource.textField.getText();
            Inventory.addPart(new Outsourced(id, name, price, stock, min, max, sourceTxt));
            toMain(actionEvent);
            }
        }

    }
    }


