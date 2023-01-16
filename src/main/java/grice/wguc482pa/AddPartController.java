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
    private TextField addPartName, addPartInv, addPartCost, addPartMin, addPartMax, addPartSourceInput;
    @FXML
    private Label addPartNameLabel, addPartNameWarning, addPartStockWarning, addPartInvLabel, addPartCostWarning,
            addPartMinMaxWarning, addPartMinMaxLabel, addPartSourceWarning, addPartSourceLbl, addPartCostLabel;
    @FXML
    private RadioButton addPartInHouse;

    // Setup containers for labels and text fields to make label alerts easier
    FieldsDAO nameFields,stockFields,priceFields,minFields,maxFields,sourceFields;

    @Override
    /**
     * Initializes add pard id text field to next available part number
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Init ID field with temp id number
        addPartId.setText(Inventory.getTempCounter());
        // Init label containers
        nameFields = new FieldsDAO(addPartName, addPartNameLabel, addPartNameWarning, false);
        stockFields = new FieldsDAO(addPartInv, addPartInvLabel, addPartStockWarning, false);
        priceFields = new FieldsDAO(addPartCost, addPartCostLabel, addPartCostWarning, false);
        minFields = new FieldsDAO(addPartMin, addPartMinMaxLabel, addPartMinMaxWarning, false);
        maxFields = new FieldsDAO(addPartMax, addPartMinMaxLabel, addPartMinMaxWarning, false);
        sourceFields = new FieldsDAO(addPartSourceInput, addPartSourceLbl, addPartSourceWarning, false);
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

    /**
     * This method is run once the user clicks the Save button.  All Textfields are validated providing user
     * feedback as needed.  Once validation has completed the user new part is added to the Inventory datastore.
     * Upon part creation an action event is passed to the toMain() method which reloads the main screen.
     *
     * @param actionEvent Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    public boolean addPartSave(ActionEvent actionEvent) throws IOException {


        // Check all the fields for input and alert for any that are empty.
        // If not empty clear any alerts that are present
        // INIT list of with all fields loaded to check entry validation
        FieldsDAO[] allFields = {nameFields, stockFields, priceFields, minFields, maxFields, sourceFields};
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
        intFields.add(stockFields);
        intFields.add(priceFields);
        intFields.add(minFields);
        intFields.add(maxFields);
        // Check if In-House radio button is selected.  Add partSource to inFields for Int validation if so.
        if (addPartInHouse.selectedProperty().getValue()) {
            intFields.add(sourceFields);
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
        FieldsDAO[] doubleFields = {priceFields};
        for (FieldsDAO field : doubleFields) {
            Pair p = Helper.checkDbl(field);
            if (p.getKey().equals(false)) {
                Helper.setAlert(field, "Enter Valid Float");
            } else {
                Helper.clearAlert(field);
            }
        }


        // Setup variables for easy part creation
        int id = Inventory.getCounter();
        String name = nameFields.textField.getText();
        Double price = (Double) Helper.checkDbl(priceFields).getValue();
        Integer stock = (Integer) Helper.checkInt(stockFields).getValue();
        Integer min = (Integer) Helper.checkInt(minFields).getValue();
        Integer max = (Integer) Helper.checkInt(maxFields).getValue();

        // All fields have any entry of the correct type.  Now check validity of min/max and stock levels.
        if (noAlerts()){
            if(min > max){
                System.out.print("HEre");
                Helper.setAlert(minFields, "MIN not less than MAX");
                return false;
            }
            if(stock < min || stock > max){
                System.out.print("HEre");
                Helper.setAlert(stockFields, "Must be between MIN/MAX");
                return false;
            }
        }

        if (noAlerts()) {
            // Check if InHouse or Outsourced use int or string as required for source field.
            if (addPartInHouse.selectedProperty().getValue()) {
                Integer source = (Integer) Helper.checkInt(sourceFields).getValue();
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, source));
                toMain(actionEvent);
            }
            if (!(addPartInHouse.selectedProperty().getValue())) {
                String sourceTxt = sourceFields.textField.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, sourceTxt));
                toMain(actionEvent);
            }
        }
    return true;
    }

    // Return true if there are no active alerts for the screen.
    private boolean noAlerts(){
        return !(nameFields.getErrState() || priceFields.getErrState() || stockFields.getErrState() ||
                minFields.getErrState() ||   maxFields.getErrState() || sourceFields.getErrState());
    }

}



