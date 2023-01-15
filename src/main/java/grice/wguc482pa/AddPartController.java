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
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
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
    private  Label addPartStockWarning;
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
    public boolean addPartSave(ActionEvent actionEvent) throws IOException {
        Boolean[] errorArray = new Boolean[14];
        Boolean safeSave = false;
        String name = addPartName.getText();
        String stock = addPartInv.getText();
        String price = addPartCost.getText();
        String min = addPartMin.getText();
        String max = addPartMax.getText();
        String source = addPartSourceInput.getText();
        double priceValue = 0.0;
        int stockValue = 0;
        int minValue = 0;
        int maxValue = 0;
        int sourceValue = 0;

        // This block of code checks the user input fields for missing input and provides feedback as required.
        if(name.equals("")){
            errorMessage("SET", addPartNameLabel, addPartNameWarning,"Missing Input");
            errorArray[0] = true;
        } else {
            errorMessage("CLEAR", addPartNameLabel, addPartNameWarning, "");
            errorArray[0] = false;
        }

        if (stock.equals("")) {
            errorMessage("SET", addPartInvLabel, addPartStockWarning,"Missing Input");
            errorArray[1] = true;
        } else {
            errorMessage("CLEAR", addPartInvLabel, addPartStockWarning, "");
            errorArray[1] = false;
        }

        if (price.equals("")) {
            errorMessage("SET", addPartCostLabel, addPartCostWarning,"Missing Input");
            errorArray[2] = true;
        } else {
            errorMessage("CLEAR", addPartCostLabel, addPartCostWarning, "");
            errorArray[2] = false;
        }

        if (min.equals("")) {
            errorMessage("SET", addPartMinMaxLabel, addPartMinMaxWarning,"Missing Input");
            errorArray[3] = true;
        } else {
            errorMessage("CLEAR", addPartMinMaxLabel, addPartMinMaxWarning, "");
            errorArray[3] = false;
        }
        if (max.equals("")) {
            errorMessage("SET", addPartMinMaxLabel, addPartMinMaxWarning,"Missing Input");
            errorArray[4] = true;
        } else {
            errorMessage("CLEAR", addPartMinMaxLabel, addPartMinMaxWarning, "");
            errorArray[4] = false;
        }

        if (source.equals("")) {
            errorMessage("SET", addPartSourceLbl, addPartSourceWarning,"Missing Input");
            errorArray[5] = true;
        } else {
            errorMessage("CLEAR", addPartSourceLbl, addPartSourceWarning, "");
            errorArray[5] = false;
        }

        // Set gatekeeper boolean safeSave to true if any errors are present in previous validation checks
        safeSave = !(Arrays.asList(errorArray).contains(true));

        // Exit early to allow user to correct input validation failures
        if(!safeSave) {return false;}

        // All previous input validation checks have passed proceed with additional validation checks.  User must use
        // double's for price, and integer's for min, max, and stock levels.
        // Check price value is double
        try
            { priceValue = Double.parseDouble(price); errorArray[6] = false; }
        catch (NumberFormatException nfe) {
            errorMessage("SET", addPartCostLabel, addPartCostWarning,"Enter a valid number");
            errorArray[6] = true;
        }
        // Check stock value is integer
        try
            { stockValue = Integer.parseInt(stock); errorArray[7] = false; }
        catch (NumberFormatException nfe) {
            errorMessage("SET", addPartInvLabel, addPartStockWarning,"Enter a valid number");
            errorArray[7] = true;
        }
        // Check min value is integer
        try
        { minValue = Integer.parseInt(min); errorArray[8] = false; }
        catch (NumberFormatException nfe) {
            errorMessage("SET", addPartMinMaxLabel, addPartMinMaxWarning,"Enter a valid number");
            errorArray[8] = true;
        }
        // Check max value is integer
        try
        { maxValue = Integer.parseInt(max); errorArray[9] = false; }
        catch (NumberFormatException nfe) {
            errorMessage("SET", addPartMinMaxLabel, addPartMinMaxWarning,"Enter a valid number");
            errorArray[9] = true;
        }
        // Check source value is integer
        if (addPartInHouse.selectedProperty().getValue()) {
            try {
                sourceValue = Integer.parseInt(source);
                errorArray[10] = false;
            } catch (NumberFormatException nfe) {
                errorMessage("SET", addPartSourceLbl, addPartSourceWarning, "Enter a valid number");
                errorArray[10] = true;
            }
        }

        // Set gatekeeper boolean safeSave to true if any errors are present in previous validation checks
        safeSave = !(Arrays.asList(errorArray).contains(true));

        // Exit early to allow user to correct input validation failures
        if(!safeSave) {return false;}

        // All previous input validation checks have passed proceed with additional validation checks.  Stock level
        // must be between min and max values and min must be less than max.

        // Validate stock level entries
        if(stockValue > maxValue ) {
            errorMessage("SET", addPartInvLabel, addPartStockWarning,"Higher than MAX");
            errorArray[11] = true;
        }

        // Validate stock level entries
        if(stockValue < minValue ) {
            errorMessage("SET", addPartInvLabel, addPartStockWarning,"Less than MIN");
            errorArray[12] = true;
        }

        // Validate min/max entries
        if(minValue > maxValue) {
            errorMessage("SET", addPartMinMaxLabel, addPartMinMaxWarning,"MIN must be less than MAX");
            errorArray[13] = true;
        }

        // Exit early to allow user to correct input validation failures
        if(!safeSave) {return false;}

        if(addPartInHouse.selectedProperty().getValue()){
            Inventory.addPart(new InHouse(Inventory.getCounter(),name,priceValue,stockValue,minValue,maxValue,sourceValue));
        } else {
            Inventory.addPart(new Outsourced(Inventory.getCounter(),name,priceValue,stockValue,minValue,maxValue,source));
        }
        toMain(actionEvent);
        return true;
    }
    /**
     * Updates the color, font weight, and value of text labels where are passed in.
     *
     * @param flag String which indicates which type of processing is required
     * @param inputLabel Label input which has its font weight and color changed.
     * @param inputWarningLabel Text and color to a warning label.
     * @param msg Text to be loaded into label.
     */
    private static void errorMessage(String flag, Label inputLabel,Label inputWarningLabel, String msg){
        switch (flag) {
            case "SET":
                inputLabel.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR, 18));
                inputLabel.setTextFill(Color.RED);
                inputWarningLabel.setText(msg);
                break;
            case "CLEAR":
                inputLabel.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 18));
                inputLabel.setTextFill(Color.BLACK);
                inputWarningLabel.setText(msg);
                break;
        }
    }
}
