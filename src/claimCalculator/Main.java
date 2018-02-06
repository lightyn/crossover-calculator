package claimCalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("claimsCalculator.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        KeyEventListener keyEventListener = new KeyEventListener(controller);

        controller.init(keyEventListener);
        root.setOnKeyPressed(keyEventListener);

        if (root instanceof GridPane) {
            for (Node child : ((GridPane) root).getChildren()) {
                if (child instanceof GridPane) {
                    ColumnConstraints col1Constraints = new ColumnConstraints();
                    col1Constraints.setHalignment(HPos.RIGHT);
                    col1Constraints.setMinWidth(150);
                    col1Constraints.setMaxWidth(150);

                    ColumnConstraints col2Constraints = new ColumnConstraints();
                    col2Constraints.setMinWidth(70);
                    col2Constraints.setMaxWidth(70);

                    ((GridPane) child).getColumnConstraints().addAll(col1Constraints, col2Constraints);
                }
            }

        }
        primaryStage.resizableProperty().set(false);
        primaryStage.setTitle("Medicaid Secondary Liability Calculator");
        primaryStage.setScene(new Scene(root, 510, 448));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
