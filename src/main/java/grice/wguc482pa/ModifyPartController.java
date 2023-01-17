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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controls modify part scene.
 */
public class ModifyPartController implements Initializable {
    public static Part selectedPart;

    // Screen TextFields
    @FXML private TextField modPartId, modPartName, modPartInv, modPartCost, modPartMin, modPartMax, modPartSource;
    // Screen Labels
    @FXML private Label modPartNameLabel, modPartStockLabel, modPartPriceLabel, modPartMinMaxLabel, modPartSourceLabel,
            modPartNameWarning, modPartStockWarning, modPartPriceWarning, modPartMinMaxWarning,
            modPartSourceWarning;
    // Screen Radiobuttons
    @FXML RadioButton modPartInHouse, modPartOutsource;

    // Declare DAO objects to be used containers for labels and textfields.
    FieldsDAO nameFields,stockFields,priceFields,minFields,maxFields,sourceFields;

    // Catch incoming part passed from mainController
    public static void selectedPart(Part selectedPart) {
        ModifyPartController.selectedPart = selectedPart;
    }

    /**
     * Init process which always runs first when the controller is called.  In this instance it's used to
     * load fields with part data passed from main screen.  Additionally, it's sets up the initial state for
     * the radio buttons.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create DAO objects to containerize form fields and labels.
        nameFields = new FieldsDAO(modPartName, modPartNameLabel, modPartNameWarning, false);
        stockFields = new FieldsDAO(modPartInv, modPartStockLabel, modPartStockWarning, false);
        priceFields = new FieldsDAO(modPartCost, modPartPriceLabel, modPartPriceWarning, false);
        minFields = new FieldsDAO(modPartMin, modPartMinMaxLabel, modPartMinMaxWarning, false);
        maxFields = new FieldsDAO(modPartMax, modPartMinMaxWarning, modPartMinMaxWarning, false);
        sourceFields = new FieldsDAO(modPartSource, modPartSourceLabel, modPartSourceWarning, false);

        // INIT fields with incoming part data
        modPartId.setText(String.valueOf(selectedPart.getId()));
        modPartName.setText(selectedPart.getName());
        modPartInv.setText(String.valueOf(selectedPart.getStock()));
        modPartCost.setText(String.valueOf(selectedPart.getPrice()));
        modPartMin.setText(String.valueOf(selectedPart.getMin()));
        modPartMax.setText(String.valueOf(selectedPart.getMax()));
        // Check class type and set source radio buttons and field data as required.
        if(selectedPart instanceof InHouse inHousePart){
            modPartSource.setText(String.valueOf(inHousePart.getMachineId()));
            modPartInHouse.selectedProperty().set(true);
        } else {
            Outsourced outsourcedPart = (Outsourced) selectedPart;
            modPartSource.setText(outsourcedPart.getCompanyName());
            modPartOutsource.selectedProperty().set(true);
            modPartSourceLabel.setText("Company Name");
        }
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

    public boolean modPartSave(ActionEvent actionEvent) throws IOException {
        // Load label containers into list
        FieldsDAO[] allFields = {nameFields, stockFields, priceFields, minFields, maxFields, sourceFields};

        // Setup variables for easy part creation
        int id = selectedPart.getId();
        String name = allFields[0].textField.getText();
        Double price = Helper.checkDbl(allFields[2]).getValue();
        Integer stock = Helper.checkInt(allFields[1]).getValue();
        Integer min = Helper.checkInt(allFields[3]).getValue();
        Integer max = Helper.checkInt(allFields[4]).getValue();

        // All fields have any entry of the correct type.  Now check validity of min/max and stock levels.
        if (Helper.noAlerts(allFields, modPartInHouse)){
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
        if (Helper.noAlerts(allFields, modPartInHouse)) {
            // Check if InHouse or Outsourced use int or string as required for source field.
            if (modPartInHouse.selectedProperty().getValue()) {
                Integer source = Helper.checkInt(allFields[5]).getValue();
                // Added a fail check for error logging could be a good future feature implementation
                if(Inventory.deletePart(selectedPart)) {
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, source));
                    toMain(actionEvent);
                }
            }
            if (!(modPartInHouse.selectedProperty().getValue())) {
                String sourceTxt = allFields[5].textField.getText();
                // Added a fail check for error logging could be a good future feature implementation
                if(Inventory.deletePart(selectedPart)) {
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, sourceTxt));
                    toMain(actionEvent);
                }
            }
        }
        return true;
    }



    /**
     * Listener attached to radio buttons when called it changes the source label to match the required input
     *
     */
    @FXML
    private void sourceSwitch(){
        if(modPartInHouse.selectedProperty().getValue()){
            modPartSourceLabel.setText("Machine ID");
        } else {
            modPartSourceLabel.setText("Company Name");
        }
    }
}
