package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CritterWorld extends Application {

    int counter = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextField tf = new TextField();
        tf.setText("Counter: " + String.valueOf(counter));
        Button button = new Button("Counter");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                counter++;
                tf.setText("Counter: " + String.valueOf(counter));
            }
        });

        primaryStage.setTitle("Counter");
        // GridPane?
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.add(button,0 ,0 );
        root.add(tf, 0, 1);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}


