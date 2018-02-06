package claimCalculator;


import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A Parsable Input Element with a stored value type of Integer.
 */
public class IntegerParsableInputElement extends ParsableInputElement {
    private int integer;

    public IntegerParsableInputElement(TextField textField, Text errorText) {
        super(errorText,textField);
        integer = 0;
    }

    @Override
    public Integer getValue() {
        return integer;
    }

    public void setValue(int integer) {
        this.integer = integer;
    }

    @Override
    public void parse() throws NumberFormatException {
        integer = Integer.parseInt(super.getTextField().getText());
    }
}
