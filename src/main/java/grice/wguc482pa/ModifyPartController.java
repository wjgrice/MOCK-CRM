package grice.wguc482pa;

/**
 * The controller for the modify part view
 * @author William Grice
 */

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
import java.util.ResourceBundle;


public class ModifyPartController implements Initializable {
    public static Part selectedPart;
    public static Part getSelectedPart() {
        return selectedPart;
    }

    public static void selectedPart(Part selectedPart) {
        ModifyPartController.selectedPart = selectedPart;
    }

    /**
     * Returns application to main view by loading mainController.
     *
     * @param actionEvent Passed from parent method.
     * @throws IOException From FXMLLoader.
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/grice/wguc482pa/mainView.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void modPartSave(ActionEvent actionEvent) {
    }
    // Screen TextFields
    @FXML private TextField modPartId, modPartName, modPartInv, modPartCost, modPartMin, modPartMax, modPartSource;

    // Screen Labels
    @FXML private Label modPartNameLabel, modPartStockLabel, modPartPriceLabel, modPartMinMaxLabel, modPartSourceLabel,
                        modPartNameWarning, modPartStockWarning, modPartPriceWarning, modPartMinMaxWarning,
                        modPartSourceWarning;

    // Screen Radiobuttons
    @FXML RadioButton modPartInHouse, modPartOutsource;

    // Create DAO objects to containerize form fields and labels.
    FieldsDAO name = new FieldsDAO(modPartName, modPartNameLabel, modPartNameWarning, false);
    FieldsDAO stock = new FieldsDAO(modPartInv, modPartStockLabel, modPartStockWarning, false);
    FieldsDAO price = new FieldsDAO(modPartCost, modPartPriceLabel, modPartPriceWarning, false);
    FieldsDAO min = new FieldsDAO(modPartMin, modPartMinMaxLabel, modPartMinMaxWarning, false);
    FieldsDAO max = new FieldsDAO(modPartMax, modPartMinMaxWarning, modPartMinMaxWarning, false);
    FieldsDAO source = new FieldsDAO(modPartSource, modPartSourceLabel, modPartSourceWarning, false);

    /**
     * Init process which always runs first when the controller is called.  In this instance it's used to
     * load fields with part data passed from main screen.  Addtionally it's sets up the innitial state for
     * the radio buttons.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modPartId.setText(String.valueOf(selectedPart.getId()));
        modPartName.setText(selectedPart.getName());
        modPartInv.setText(String.valueOf(selectedPart.getStock()));
        modPartCost.setText(String.valueOf(selectedPart.getPrice()));
        modPartMin.setText(String.valueOf(selectedPart.getMin()));
        modPartMax.setText(String.valueOf(selectedPart.getMax()));
        // Check class type and set source radio buttons and field data as required.
        if(selectedPart instanceof InHouse ){
                InHouse inhousePart = (InHouse) selectedPart;
                modPartSource.setText(String.valueOf(inhousePart.getMachineId()));
                modPartInHouse.selectedProperty().set(true);
            } else {
                Outsourced outsourcedPart = (Outsourced) selectedPart;
                modPartSource.setText(outsourcedPart.getCompanyName());
                modPartOutsource.selectedProperty().set(true);
                modPartSourceLabel.setText("Company Name");
            }
    }
    /**
     * Listener attached to radio buttons when called it changes the source label to match the required input
     *
     */
    @FXML
    private void sourceSwtich(){
        if(modPartInHouse.selectedProperty().getValue()){
            modPartSourceLabel.setText("Machine ID");
        } else {
            modPartSourceLabel.setText("Company Name");
        }
    }
}
