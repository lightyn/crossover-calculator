package claimCalculator;


import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A JavaFX event handler that listens for keystroke events and executes controller
 * methods based on key code.
 */
class KeyEventListener implements EventHandler<KeyEvent> {
    private final Controller controller;

    public KeyEventListener(Controller controller) {
        this.controller = controller;
    }

    /**
     * Handle KeyEvents based on the event.
     * @param event the specific KeyEvent that occurred.
     */
    @Override
    public void handle(KeyEvent event) {

         //If the user presses the "Enter"/"Return" key, run the calculator.

        if (event.getCode() == KeyCode.ENTER) {
            controller.calculate();
        }
    }
}
