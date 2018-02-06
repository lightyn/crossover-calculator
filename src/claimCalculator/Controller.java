package claimCalculator;

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
public class Controller {
    private final LinkedList<ParsableInputElement> inputElements = new LinkedList<ParsableInputElement>();
    private IntegerParsableInputElement coinsuranceDays;
    private IntegerParsableInputElement coveredDays;
    private DoubleParsableInputElement medicaidRate;
    private DoubleParsableInputElement medicaidPatientLiability;
    private DoubleParsableInputElement medicarePaidAmount;
    private HashMap<String,Double> medicareRates = new HashMap<String, Double>();
    private DoubleParsableInputElement medicareSequestrationAmount;

    //Global color pallete variables.
    private final Color colorError = Color.web("FF2222", 1);
    private final Color colorClear = Color.web("000000", 0);

    //The following are all FXML tags shared by the FXML document and the controller.
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
     * controller constructor.
     *
     * @param keyEventListener the KeyEventListener being used to send actions
     *                         to this controller.
     */
    public void init(KeyEventListener keyEventListener) {

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
        coveredDays = new IntegerParsableInputElement(inputCoveredDays, inputCoveredDaysError);
        inputElements.add(coveredDays);

        coinsuranceDays = new IntegerParsableInputElement(inputCoinsuranceDays, inputCoinsuranceDaysError);
        inputElements.add(coinsuranceDays);

        medicaidRate = new DoubleParsableInputElement(inputMedicaidRate, inputMedicaidRateError);
        inputElements.add(medicaidRate);

        medicaidPatientLiability = new DoubleParsableInputElement(inputMedicaidPatientLiability, inputMedicaidPatientLiabilityError);
        inputElements.add(medicaidPatientLiability);

        medicarePaidAmount = new DoubleParsableInputElement(inputMedicarePaidAmount, inputMedicarePaidAmountError);
        inputElements.add(medicarePaidAmount);

        medicareSequestrationAmount = new DoubleParsableInputElement(inputMedicareSequestrationAmount, inputMedicareSequestrationAmountError);
        inputElements.add(medicareSequestrationAmount);

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
        for (ParsableInputElement pie : inputElements) {
            Boolean isInteger = pie instanceof IntegerParsableInputElement;
            Boolean isDouble = pie instanceof DoubleParsableInputElement;
            Boolean isValid = true;
            try {
                pie.parse();
                pie.getErrorText().setFill(colorClear);
              } catch (NumberFormatException nfe) {

                /* The error message for invalid characters is going to be the same as the error
                 * message for an out of bounds number defined below. So we set it to a negative
                 * number to force it out of bounds to prevent further exceptions
                 * and move on.
                 */
                if (isInteger) {
                    ((IntegerParsableInputElement) pie).setValue(-1);
                }
                if (isDouble){
                    ((DoubleParsableInputElement) pie).setValue(-1);
                }
            }
            if (isInteger) {
                // All current integer input fields are days.
                // Can't have billing for zero or negative days.
                isValid = ((IntegerParsableInputElement) pie).getValue() > 0;
            }
            if (isDouble) {
                // No negative amounts should exist here either.
                isValid = ((DoubleParsableInputElement) pie).getValue() >= 0;
            }
            if (!isValid) {
                if (isInteger){
                    pie.getErrorText().setText("Must be a whole number greater than zero");
                }
                if (isDouble) {
                    pie.getErrorText().setText("Must be a number greater than zero");
                }
                pie.getErrorText().setFill(colorError);
            }
        }
        // Don't know how the user could store a null in a drop down selector,
        // but if they do...
        if (inputMedicareRate.getValue() == null) {
            inputMedicareRate.setValue("2018");
        }
    }

    /**
     * Checks all calculations to see if the necessary input elements have valid
     * values and then performs any calcuations whose validation requirements are
     * met. <p>Sends calculation results to output text fields rounded to hundredths</p>
     */
    public void calculate(){
        clearOutput();
        double output = 0;
        if (checkPerDiemRequirements()) {
            output = getMedicarePaidPerDiem();
            //Hide the error message text by reducing its opacity to 0.
            outputMedicarePerDiemError.setFill(colorClear);
            outputMedicarePerDiem.setText("$"+ roundToHundredths(output));
        } else {
            //Reveal the error message text by increasing its opacity.
            outputMedicarePerDiemError.setFill(colorError);
        }
        if (checkNetOwedRequirements()) {
            outputMedicaidNetOwedError.setFill(colorClear);
            outputPatientOwedError.setFill(colorClear);
            /*This code block essentially takes the remaining secondary owed and
            * assigns it first to the patient up to their liability cap, and then
            * to Medicaid.
            */
            double remainder = getNetOwed();
            double patientMax = medicaidPatientLiability.getValue();
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
            outputMedicaidNetOwedError.setFill(colorError);
            outputPatientOwedError.setFill(colorError);
        }
    }

    /**
     * Rounds a Double to the hundredths place and returns as a String.
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
    private boolean checkRequirements(ParsableInputElement[] requirements) {
        validateInput();
        for (ParsableInputElement pie : requirements) {
            if (pie instanceof DoubleParsableInputElement) {
                if (((DoubleParsableInputElement) pie).getValue() < 0) {
                    return false;
                }
            } else if(pie instanceof IntegerParsableInputElement) {
                if (((IntegerParsableInputElement) pie).getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculates the Medicare paid per diem from stored input element values
     * and returns as a double.
     * @return Returns the calculation of Medicare paid per diem as a double.
     */
    private double getMedicarePaidPerDiem() {
        return (medicarePaidAmount.getValue() +
                medicareSequestrationAmount.getValue() +
                (coinsuranceDays.getValue() *
                    medicareRates.get(inputMedicareRate.getValue()))) /
                        coveredDays.getValue();
    }

    private boolean checkPerDiemRequirements() {
        ParsableInputElement[] requirements = {
                coveredDays, coinsuranceDays,
                medicarePaidAmount, medicareSequestrationAmount
        };
        return checkRequirements(requirements);
    }

    /**
     * Calculates the total secondary liability owed from stored input element values
     * and returns as a double.
     * @return the calculation of secondary liability as a double.
     */
    private double getNetOwed() {
        return (medicaidRate.getValue() -
                getMedicarePaidPerDiem() +
                medicareRates.get(inputMedicareRate.getValue()))
                *coinsuranceDays.getValue();
    }

    private boolean checkNetOwedRequirements() {
        ParsableInputElement[] requirements = {medicaidRate, medicaidPatientLiability};
        return checkRequirements(requirements) && checkPerDiemRequirements();
    }

    /**
     * Clears all input text fields in this controller's inputElement list and
     * clears any of their error prompts
     */
    private void reset() {
        for (ParsableInputElement pie : inputElements) {
            pie.getTextField().setText("");
            pie.getErrorText().setFill(colorClear);
        }
        clearOutput();
    }

    /**
     * Clears the output text fields and their respective error message prompts
     */
    private void clearOutput() {
        outputMedicaidNetOwed.setText(" ");
        outputMedicaidNetOwedError.setFill(colorClear);

        outputMedicarePerDiem.setText(" ");
        outputMedicarePerDiemError.setFill(colorClear);

        outputPatientOwed.setText(" ");
        outputPatientOwedError.setFill(colorClear);
    }

}
