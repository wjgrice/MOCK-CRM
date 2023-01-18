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
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controls logic for add part screen.
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

    /**
     * Initializes add part id text field to next available part number and setups up DAO objects to manage field and
     * label names.
     */
    @Override
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/grice/wguc482pa/mainView.fxml")));
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
        // Load label containers into list
        FieldsDAO[] allFields = {nameFields, stockFields, priceFields, minFields, maxFields, sourceFields};

        // Setup source field name based on current selection.
        if(addPartInHouse.selectedProperty().getValue()){
            sourceFields.label.setText("Machine ID");
        } else {
            sourceFields.label.setText("Company Name");
        }

        // Setup variables for easy part creation
        int id = Inventory.getCounter();
        String name = allFields[0].textField.getText();
        Double price = Helper.checkDbl(allFields[2]).getValue();
        Integer stock = Helper.checkInt(allFields[1]).getValue();
        Integer min = Helper.checkInt(allFields[3]).getValue();
        Integer max = Helper.checkInt(allFields[4]).getValue();

        // All fields have any entry of the correct type.  Now check validity of min/max and stock levels.
        if (Helper.noAlerts(allFields, addPartInHouse)){
            if(min > max){
                Helper.setAlert(allFields[3], "MIN not less than MAX");
                return false;
            }
            if(stock < min || stock > max){
                Helper.setAlert(allFields[1], "Must be between MIN/MAX");
                return false;
            }
        }

        // All validation has passed.  Create the parts in inventory.
        if (Helper.noAlerts(allFields, addPartInHouse)) {
            // Check if InHouse or Outsourced use int or string as required for source field.
            if (addPartInHouse.selectedProperty().getValue()) {
                Integer source = Helper.checkInt(allFields[5]).getValue();
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, source));
                toMain(actionEvent);
            }
            if (!(addPartInHouse.selectedProperty().getValue())) {
                String sourceTxt = allFields[5].textField.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, sourceTxt));
                toMain(actionEvent);
            }
        }
    return true;
    }

    /**
     *  Changes name of addPart Source label between two options when called
     */
    @FXML
    private void sourceSwitch(){
        if(addPartInHouse.selectedProperty().getValue()){
            System.out.print("HERE");
            addPartSourceLbl.setText("Machine ID");
        } else {
            addPartSourceLbl.setText("Company Name");
        }
    }
}



