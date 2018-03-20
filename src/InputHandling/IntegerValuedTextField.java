package InputHandling;


import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A Parsable Input Element with a stored value type of Integer.
 */
public class IntegerValuedTextField extends ValuedTextField {
    private int integer;

    public IntegerValuedTextField(TextField textField, Text errorText) {
        super(errorText,textField);
        integer = 0;
    }

    @Override
    public Integer getValue() {
        return integer;
    }

    public void setValue(Integer integer) {
        this.integer = integer;
    }

    @Override
    public void parse() throws NumberFormatException {
        integer = Integer.parseInt(super.getTextField().getText());
    }
}
