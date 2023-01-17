/**
 *
 * @author William Grice
 */
package grice.wguc482pa;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Helper class which contains commonly used cross application methods.
 */
public class Helper {

    /**
     * Sets and UI based alert messages.
     *
     * @param fields DAO object containing group of associated textfields and labels.
     * @param msg String to be displayed.
     */
    public static void setAlert(FieldsDAO fields, String msg){
        fields.label.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR, 18));
        fields.label.setTextFill(Color.RED);
        fields.warning.setText(msg);
        fields.setErrState(true);
    }

    /**
     * Clear any aleart that are present on the passed in DAO packaged labels and textfields.
     *
     *@param fields DAO object containing group of associated textfields and labels.
     */
    public static void clearAlert(FieldsDAO fields){
        fields.label.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        fields.label.setTextFill(Color.BLACK);
        fields.warning.setText("");
        fields.setErrState(false);
    }

    /**
     * Simple method to set color and text for a label.
     *
     * @param label Warning labels on the UI which provide user validation feedback.
     * @param msg String containing message to display
     */
    public static void setWarningLabel(Label label, String msg) {
        label.setTextFill(Color.RED);
        label.setText(msg);
    }

    /**
     * Checks if a field contains an Int and returns the value along with boolean.
     *
     * @param field DAO object containing associated textfields and labels.
     * @return Pair<Boolean, Integer>
     */
    public static Pair<Boolean, Integer> checkInt(FieldsDAO field) {
        Integer i = 0;
        try {
            i = Integer.parseInt(field.textField.getText());
            Pair p = new Pair(true, i);
            return p;
        }catch (NumberFormatException nfe){
            Pair p = new Pair(false,null);
            return p;
        }
    }
    /**
     * Checks if a field contains a Double and returns the value along with boolean.
     *
     * @param field DAO object containing associated textfields and labels.
     * @return Pair<Boolean, Double>
     */
    public static Pair<Boolean, Double> checkDbl(FieldsDAO field) {

        Double i = 0.0;
        try {
            i = Double.parseDouble(field.textField.getText());
            Pair p = new Pair(true, i);
            return p;
        }catch (NumberFormatException nfe){
            Pair p = new Pair(false,null);
            return p;
        }
    }

    /**
     * Entry validation method used on Add Product and MOD Product screens.
     * @param allFields DAO object containing associated fields and labels
     * @return boolean
     */
    public static boolean noAlerts(FieldsDAO[] allFields){
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
        intFields.add(allFields[1]);
        intFields.add(allFields[2]);
        intFields.add(allFields[3]);
        intFields.add(allFields[4]);

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
        FieldsDAO[] doubleFields = {allFields[2]};
        for (FieldsDAO field : doubleFields) {
            Pair p = Helper.checkDbl(field);
            if (p.getKey().equals(false)) {
                Helper.setAlert(field, "Enter Valid Float");
            } else {
                Helper.clearAlert(field);
            }
        }

        // Return true if there are no active alerts for the screen else return false.
        return !(allFields[0].getErrState() || allFields[1].getErrState() || allFields[2].getErrState() ||
                allFields[3].getErrState() ||   allFields[4].getErrState());

    }

    /**
     * Entry validation method used on Add Part, Mod Part
     * @param allFields DAO object containing associated fields and labels
     * @param inHouseButton Radio button indicating status of part entry source config
     * @return boolean
     */
    public static boolean noAlerts(FieldsDAO[] allFields, RadioButton inHouseButton){
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
        intFields.add(allFields[1]);
        intFields.add(allFields[2]);
        intFields.add(allFields[3]);
        intFields.add(allFields[4]);
        // Check if In-House radio button is selected.  Add partSource to inFields for Int validation if so.
        if (inHouseButton.selectedProperty().getValue()) {
            intFields.add(allFields[5]);
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
        FieldsDAO[] doubleFields = {allFields[2]};
        for (FieldsDAO field : doubleFields) {
            Pair p = Helper.checkDbl(field);
            if (p.getKey().equals(false)) {
                Helper.setAlert(field, "Enter Valid Float");
            } else {
                Helper.clearAlert(field);
            }
        }

        // Return true if there are no active alerts for the screen else return false.
        return !(allFields[0].getErrState() || allFields[1].getErrState() || allFields[2].getErrState() ||
                allFields[3].getErrState() ||   allFields[4].getErrState() || allFields[5].getErrState());

    }

    /**
     * Simple method used to change scenes easily
     * @param path to FMXL file to load
     * @param actionEvent event to pull stage from
     */
    public static void screenChange(String path, ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Helper.class.getResource(path)));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

