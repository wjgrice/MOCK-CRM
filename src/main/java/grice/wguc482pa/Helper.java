package grice.wguc482pa;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

public class Helper {
    public static void setAlert(FieldsDAO fields, String msg){
        fields.label.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR, 18));
        fields.label.setTextFill(Color.RED);
        fields.warning.setText(msg);
        fields.setErrState(true);
    }
    public static void clearAlert(FieldsDAO fields){
        fields.label.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        fields.label.setTextFill(Color.BLACK);
        fields.warning.setText("");
        fields.setErrState(false);
    }
    public static Pair checkInt(FieldsDAO field) {

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
    public static Pair checkDbl(FieldsDAO field) {

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
}

