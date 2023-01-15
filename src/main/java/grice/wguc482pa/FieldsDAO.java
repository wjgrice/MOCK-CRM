package grice.wguc482pa;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class FieldsDAO {
    public TextField textField;

    public Label label;
    public Label warning;
    public boolean errState;

    public FieldsDAO(TextField textField, Label label,Label warning, boolean errState) {
        this.textField = textField;
        this.label = label;
        this.warning = warning;
        this.errState = errState;
    }
    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getWarning() {
        return warning;
    }

    public void setWarning(Label warning) {
        this.warning = warning;
    }

    public boolean getErrState() {
        return errState;
    }

    public void setErrState(boolean errState) {
        this.errState = errState;
    }
}
