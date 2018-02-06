package claimCalculator;


import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**A JavaFX input element object capable of parsing and storing it's own value.
 * Allows a customized error text to be stored to be returned
 * to the user if needed.
 */
abstract class ParsableInputElement implements Valued {
    private final Text errorText;
    private final TextField textField;

    /**Constructs a new ParsableInputElement with specified error text.
     *
     * @param errorText a JavaFX Text used to relay element errors to the user.
     * @param textField a JavaFX TextField used to receive string input from user.
     */
    ParsableInputElement(Text errorText, TextField textField) {
        this.textField = textField;
        this.errorText = errorText;
        errorText.setFill(Color.TRANSPARENT);
    }

    /**Construct a new ParsableInputElement with default error text.
     *
     * @param textField a JavaFX TextField used to receive string input from user.
     */
    @SuppressWarnings("unused")
    public ParsableInputElement(TextField textField) {
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
