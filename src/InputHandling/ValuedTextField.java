package InputHandling;


import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**A TextField container class capable of parsing and storing its TextField's input value.
 * This object acts as a container for a JavaFX TextField, the resolved value
 * of its input, and an error text to be presented if the parsing of input fails.
 */
public abstract class ValuedTextField implements Valued {
    private final Text errorText;
    private final TextField textField;

    /**Constructs a new ValuedTextField with specified error text.
     *
     * @param errorText a JavaFX Text used to relay element errors to the user.
     * @param textField a JavaFX TextField used to receive string input from user.
     */
    ValuedTextField(Text errorText, TextField textField) {
        this.textField = textField;
        this.errorText = errorText;
        errorText.setFill(Color.TRANSPARENT);
    }

    /**Construct a new InputHandling with default error text.
     *
     * @param textField a JavaFX TextField used to receive string input from user.
     */
    @SuppressWarnings("unused")
    public ValuedTextField(TextField textField) {
        this.errorText = new Text();
        errorText.setText("Unspecified Error");
        this.textField = textField;
    }

    public Text getErrorText() {
        return errorText;
    }

    public TextField getTextField() {
        return textField;
    }

    /**
     * Implementations will use this method to convert string input to a stored
     * value of a specific type. Exceptions raised by parsing errors can be handled
     * within this method or thrown back to the caller.
     */
    public abstract void parse();
}
