package claimCalculator;


import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A Parsable Input Element with a stored value type of Double.
 */
public class DoubleParsableInputElement extends ParsableInputElement implements Valued {
    private double aDouble;

    public DoubleParsableInputElement(TextField textField, Text errorText) {
        super(errorText,textField);
        aDouble = 0;
    }

    public Double getValue() {
        return aDouble;
    }

    public void setValue(double aDouble) {
        this.aDouble = aDouble;
    }

    public void parse() throws NumberFormatException {
        aDouble = Double.parseDouble(super.getTextField().getText());
    }
}
