package InputHandling;


import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A Parsable Input Element with a stored value type of Double.
 */
public class DoubleValuedTextField extends ValuedTextField {
    private double aDouble;

    public DoubleValuedTextField(TextField textField, Text errorText) {
        super(errorText,textField);
        aDouble = 0;
    }

    @Override
    public Double getValue() {
        return aDouble;
    }

    public void setValue(double aDouble) {
        this.aDouble = aDouble;
    }

    @Override
    public void parse() throws NumberFormatException {
        aDouble = Double.parseDouble(this.getTextField().getText());
    }
}
