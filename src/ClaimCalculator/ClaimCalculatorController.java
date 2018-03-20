package ClaimCalculator;

import InputHandling.DoubleValuedTextField;
import InputHandling.IntegerValuedTextField;
import InputHandling.ValuedTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("ALL")
/**
 *The controller initializes the control input elements; receives, validates,
 * and handles user input, and propagates output information back to the user.
 * This class is doing a little too much right now and validation/parsing could be
 * passed to an external class.
 */
public class ClaimCalculatorController {

    private final LinkedList<ValuedTextField> inputElements = new LinkedList<ValuedTextField>();
    private IntegerValuedTextField coinsuranceDaysInputElement;
    private IntegerValuedTextField coveredDaysInputElement;
    private DoubleValuedTextField medicaidRateInputElement;
    private DoubleValuedTextField medicaidPatientLiabilityInputElement;
    private DoubleValuedTextField medicarePaidAmountInputElement;
    private HashMap<String,Double> medicareRates = new HashMap<String, Double>();
    private DoubleValuedTextField medicareSequestrationAmountInputElement;

    //Global color pallete variables.
    private final Color COLOR_ERROR = Color.web("FF2222", 1);
    private final Color COLOR_CLEAR = Color.web("000000", 0);

    //The following are all FXML tags to bind FXML document elements to the controller.
    @FXML
    private TextField inputCoveredDays;
    @FXML
    private Text inputCoveredDaysError;

    @FXML
    private TextField inputCoinsuranceDays;
    @FXML
    private Text inputCoinsuranceDaysError;

    @FXML
    private TextField inputMedicaidRate;
    @FXML
    private Text inputMedicaidRateError;

    @FXML
    private TextField inputMedicaidPatientLiability;
    @FXML
    private Text inputMedicaidPatientLiabilityError;

    @FXML
    private TextField inputMedicarePaidAmount;
    @FXML
    private Text inputMedicarePaidAmountError;

    @FXML
    private ComboBox<String> inputMedicareRate;

    @FXML
    private TextField inputMedicareSequestrationAmount;
    @FXML
    private Text inputMedicareSequestrationAmountError;

    @FXML
    private Button calculateButton;

    @FXML
    private Button resetButton;

    @FXML
    private TextField outputMedicarePerDiem;

    @FXML
    private Text outputMedicarePerDiemError;

    @FXML
    private TextField outputMedicaidNetOwed;

    @FXML
    private Text outputMedicaidNetOwedError;

    @FXML
    private TextField outputPatientOwed;

    @FXML
    private Text outputPatientOwedError;

    /**Initializes the controller. Should be called after the JavaFX default
     * controller constructor. JavaFX would call this method automatically if
     * the FXML annotation was added, but for this implementation we're passing
     * in an external listener referenced in Main, so we'll need Main to
     * pass it to us.
     *
     * @param keyEventListener the KeyEventListener being used to send actions
     *                         to this controller.
     */
    public void initialize(KeyEventListener keyEventListener) {

        //Override default behavior for TextAreas to enable calculation on Enter key press.
        outputMedicarePerDiem.setOnKeyPressed(keyEventListener);
        outputMedicaidNetOwed.setOnKeyPressed(keyEventListener);
        outputPatientOwed.setOnKeyPressed(keyEventListener);

        //Initialize Medicare rate table.
        medicareRates.put("2018",167.50);
        medicareRates.put("2017",164.00);
        medicareRates.put("2016",161.00);
        medicareRates.put("2015",157.50);
        medicareRates.put("2014",152.00);
        inputMedicareRate.getItems().setAll(medicareRates.keySet());
        inputMedicareRate.setValue("2018");

        //Initialize input elements and add them to the list of available input elements.
        coveredDaysInputElement = new IntegerValuedTextField(
                inputCoveredDays, inputCoveredDaysError);
        inputElements.add(coveredDaysInputElement);

        coinsuranceDaysInputElement = new IntegerValuedTextField(
                inputCoinsuranceDays, inputCoinsuranceDaysError);
        inputElements.add(coinsuranceDaysInputElement);

        medicaidRateInputElement = new DoubleValuedTextField(
                inputMedicaidRate, inputMedicaidRateError);
        inputElements.add(medicaidRateInputElement);

        medicaidPatientLiabilityInputElement = new DoubleValuedTextField(
                inputMedicaidPatientLiability, inputMedicaidPatientLiabilityError);
        inputElements.add(medicaidPatientLiabilityInputElement);

        medicarePaidAmountInputElement = new DoubleValuedTextField(
                inputMedicarePaidAmount, inputMedicarePaidAmountError);
        inputElements.add(medicarePaidAmountInputElement);

        medicareSequestrationAmountInputElement = new DoubleValuedTextField(
                inputMedicareSequestrationAmount, inputMedicareSequestrationAmountError);
        inputElements.add(medicareSequestrationAmountInputElement);

        //Bind appropriate FXML buttons to controller methods "calculate" and "reset" respectively
        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calculate();
            }
        });

        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });
    }

    /**
     * Attempts to parse and validate the values in each input element.
     * Displays error text back to the user for any invalid field entries.
     */
    private void validateInput() {
        for (ValuedTextField pie : inputElements) {
            Boolean isInteger = pie instanceof IntegerValuedTextField;
            Boolean isDouble = pie instanceof DoubleValuedTextField;
            Boolean isValid = true;
            try {
                pie.parse();
                pie.getErrorText().setFill(COLOR_CLEAR);
              } catch (NumberFormatException nfe) {

                /* The error message for invalid characters is going to be the same as the error
                 * message for an out of bounds number defined below. So we set it to a negative
                 * number to force it out of bounds to prevent further exceptions
                 * and move on.
                 */
                if (isInteger) {
                    ((IntegerValuedTextField) pie).setValue(-1);
                }
                if (isDouble){
                    ((DoubleValuedTextField) pie).setValue(-1);
                }
            }
            if (isInteger) {
                // All current integer input fields are days.
                // Can't have billing for zero or negative days.
                isValid = ((IntegerValuedTextField) pie).getValue() > 0;
            }
            if (isDouble) {
                // No negative amounts should exist here either.
                isValid = ((DoubleValuedTextField) pie).getValue() >= 0;
            }
            if (!isValid) {
                if (isInteger){
                    pie.getErrorText().setText("Must be a whole number greater than zero");
                }
                if (isDouble) {
                    pie.getErrorText().setText("Must be a number greater than zero");
                }
                pie.getErrorText().setFill(COLOR_ERROR);
            }
        }
        // Don't know how anything could store a null in a drop down selector,
        // but it happens...
        if (inputMedicareRate.getValue() == null) {
            inputMedicareRate.setValue("2018");
        }
    }

    /**
     * Checks all calculations to see if the necessary input elements have valid
     * values and then performs any calcuations whose validation requirements are
     * met. <p>Sends calculation results to output text fields rounded to hundredths</p>
     */
    protected void calculate(){
        clearOutput();
        double output = 0;
        if (checkPerDiemRequirements()) {
            output = CrossoverCalculator.getMedicarePaidPerDiem(
                    coinsuranceDaysInputElement.getValue(),
                    coveredDaysInputElement.getValue(),
                    medicarePaidAmountInputElement.getValue(),
                    medicareRates.get(inputMedicareRate.getValue()),
                    medicareSequestrationAmountInputElement.getValue()
            );
            //Hide the error message text by reducing its opacity to 0.
            outputMedicarePerDiemError.setFill(COLOR_CLEAR);
            outputMedicarePerDiem.setText("$"+ roundToHundredths(output));
        } else {
            //Reveal the error message text by setting it's color to global error color.
            outputMedicarePerDiemError.setFill(COLOR_ERROR);
        }
        if (checkNetOwedRequirements()) {
            outputMedicaidNetOwedError.setFill(COLOR_CLEAR);
            outputPatientOwedError.setFill(COLOR_CLEAR);
            /*This code block essentially takes the remaining secondary owed and
            * assigns it first to the patient up to their liability cap, and then
            * to Medicaid.
            */
            double remainder = CrossoverCalculator.getNetOwed(
                    coinsuranceDaysInputElement.getValue(),
                    coveredDaysInputElement.getValue(),
                    medicaidRateInputElement.getValue(),
                    medicarePaidAmountInputElement.getValue(),
                    medicareRates.get(inputMedicareRate.getValue()),
                    medicareSequestrationAmountInputElement.getValue()
            );
            double patientMax = medicaidPatientLiabilityInputElement.getValue();
            //If the remaining owed exceeds the patient cap
            if (remainder - patientMax > 0) {
                // Medicaid owes the difference
                output = remainder - patientMax;
            } else {
                output = 0;
            }
            outputMedicaidNetOwed.setText("$" + roundToHundredths(output));
            //If there's any remaining owed
            if (remainder > 0) {
                //If the remainder is less than the patient's liability cap
                if (remainder < patientMax) {
                    //They owe the full remainder
                    output = remainder;
                //If the remainder is greater than or equal to the liability cap
                } else if (remainder >= patientMax) {
                    //They only owe the cap
                    output = patientMax;
                }
            } else {
                //If there's no remainder, nothing is owed
                output = 0;
            }
            outputPatientOwed.setText("$"+roundToHundredths(output));
        } else {
            outputMedicaidNetOwedError.setFill(COLOR_ERROR);
            outputPatientOwedError.setFill(COLOR_ERROR);
        }
    }

    /**
     * Helper method to round a double to the hundredths place and return a String.
     * @param aDouble the Double number to be rounded.
     * @return the String conversion of the Double passed in rounded to hundredths.
     */
    private String roundToHundredths(Double aDouble) {
        aDouble = Math.round(aDouble*100.00)/100.00;
        String outputString = String.format("%.2f",aDouble);
        return outputString;
    }

    /**
     * Takes a array of instantiated input elements required to perform a calculation
     * and returns false if any are missing valid values. Otherwise returns true.
     * @param requirements the array of ParseableInputElements to check
     * @return returns FALSE if any elements are missing. Otherwise returns TRUE
     */
    private boolean checkRequirements(ValuedTextField[] requirements) {
        validateInput();
        for (ValuedTextField pie : requirements) {
            if (pie instanceof DoubleValuedTextField) {
                if (((DoubleValuedTextField) pie).getValue() >= 0) {
                    return true;
                }
            } else if(pie instanceof IntegerValuedTextField) {
                if (((IntegerValuedTextField) pie).getValue() > 0) {
                    return true;
                }
            }
        }
        return false;
    }



    private boolean checkPerDiemRequirements() {
        ValuedTextField[] requirements = {
                coveredDaysInputElement, coinsuranceDaysInputElement,
                medicarePaidAmountInputElement, medicareSequestrationAmountInputElement
        };
        return checkRequirements(requirements);
    }



    private boolean checkNetOwedRequirements() {
        ValuedTextField[] requirements = {medicaidRateInputElement, medicaidPatientLiabilityInputElement};
        return checkRequirements(requirements) && checkPerDiemRequirements();
    }

    /**
     * Clears all input text fields in this controller's inputElement list and
     * clears any of their error prompts
     */
    private void reset() {
        for (ValuedTextField pie : inputElements) {
            pie.getTextField().setText("");
            pie.getErrorText().setFill(COLOR_CLEAR);
        }
        clearOutput();
    }

    /**
     * Clears the output text fields and their respective error message prompts
     */
    private void clearOutput() {
        outputMedicaidNetOwed.setText(" ");
        outputMedicaidNetOwedError.setFill(COLOR_CLEAR);

        outputMedicarePerDiem.setText(" ");
        outputMedicarePerDiemError.setFill(COLOR_CLEAR);

        outputPatientOwed.setText(" ");
        outputPatientOwedError.setFill(COLOR_CLEAR);
    }

}
