/*
 * CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Nik Srinivas
 * ns29374
 * 16160
 * Reza Mohideen
 * rm54783
 * 16160
 * Slip days used: <0>
 * Spring 2020
 */

package assignment5;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;


import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;



/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main extends Application {
    private static String myPackage;
    static {
        myPackage = Main.class.getPackage().toString().split(" ")[1];
    }

    public static final int TILE_SIZE = 40;
    public static final int W = Params.WORLD_WIDTH * TILE_SIZE;
    public static final int H = Params.WORLD_HEIGHT * TILE_SIZE;
    public static final int X_TILES = W / TILE_SIZE;
    public static final int Y_TILES = H / TILE_SIZE;
    public static final int numColors = 6;
    public static final int colorIndex = (int)(Math.random() * Main.numColors);
    public static ByteArrayOutputStream testOutputString;

    private Scene scene;

    private String[] classNames = new String[100];
    private int numberOfFiles = 0;
    private int numberOfCritters = 0;
    private String[] critterNames;
    public static Critter[][] world = new Critter[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];

     // tile object
    public static class Square extends StackPane {
        private int x, y;
        private Color[] colorArray = {Color.BLACK, Color.LIGHTBLUE, Color.LIGHTBLUE, Color.GREEN, Color.RED,Color.SALMON};
        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);

        public Square(int x, int y, int color) {
            this.x = x;
            this.y = y;

            border.setStroke(Color.WHITE);
            border.setFill(colorArray[color]);

            getChildren().addAll(border);

            //setTranslateX(x * TILE_SIZE);
            //setTranslateY(y * TILE_SIZE);
        }
    }


    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                //System.out.println(fileEntry.getName());
                classNames[numberOfFiles] = fileEntry.getName();
                numberOfFiles += 1;
            }
        }
    }
    static GridPane gridz = new GridPane();
    public class SecondStage extends Stage {

        SecondStage(){

            scene = new Scene(gridz);

            this.setScene(scene);
            this.show();
        }
    }

    //Jonah
    @Override
    public void start(Stage primaryStage)  {


        //Initial creation of most major controls and layouts
        Stage worldStage = new SecondStage();
        Critter.displayWorld(gridz);
        worldStage.setTitle("World of Critters");

        BorderPane borders = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        GridPane center = new GridPane();
        primaryStage.setTitle("Welcome to Critters!");
        Button show = new Button("Show");
        Button make = new Button("Make");
        Button seed = new Button("Set Seed");
        Button step = new Button("Run Time Step");
        Button quit = new Button("Quit");
        Button animate = new Button("Animate");
        Label make_error = new Label("");
        Label step_error = new Label("");
        Label seed_error = new Label("");

        // Obtain Critter subclasses for make
        String workingDir = System.getProperty("user.dir") + "/src/assignment5";
        File myCritters = new File(workingDir);
        listFilesForFolder(myCritters);
        for (int k = 0; k < numberOfFiles - 1; k += 1) {
            try {
                int cutoff = classNames[k].indexOf(".");
                if (cutoff == 0) { continue; }
                String correctName = classNames[k].substring(0, cutoff);
                if (correctName.equals("Critter")) { continue; }
                Class<?> className = Class.forName(myPackage + "." + correctName);
                className.asSubclass(Critter.class);
                classNames[numberOfCritters] = correctName;
                numberOfCritters += 1;
            } catch (Exception e){
                // Do nothing; name is not a class or a subclass of critter
            }
        }
        critterNames = new String[numberOfCritters];
        for (int i = 0; i < numberOfCritters; i += 1){
            critterNames[i] = classNames[i];
        }

        ObservableList<String> differentCritters = FXCollections.observableArrayList();
        for (int i = 0; i < numberOfCritters; i += 1) {
            differentCritters.add(critterNames[i]);
        }

        // ComboBox for critter selection (make)
        final ComboBox<String> listOfCritters = new ComboBox<>(differentCritters);
        listOfCritters.setValue(critterNames[0]);

        // Create Stats window
        //ThirdStage statistics = new ThirdStage();

        // TextFields for integer input (make and step)
        TextField number_critters = new TextField();
        number_critters.setPromptText("Number of Critters...");
        TextField step_number = new TextField();
        step_number.setPromptText("Number of steps...");
        TextField seed_number = new TextField();
        seed_number.setPromptText("Seed value...");

        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Critter.displayWorld(new GridPane());

            }
        } ) ;
        make.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String critterType = listOfCritters.getValue();
                try {
                    int val = Integer.parseInt(number_critters.getText());
                    if (val < 0) {
                        make_error.setText("Please enter a positive integer!");
                    }
                    else {
                        for (int i = 0; i < val; i += 1) {
                            Critter.createCritter(critterType);
                        }
                        make_error.setText("");
                        Critter.displayWorld(gridz);
                    }
                } catch (InvalidCritterException e1) {

                } catch (NumberFormatException e1) {
                    make_error.setText("Please enter a positive integer!");
                }
                //statistics.refreshStats();
                Critter.displayWorld(gridz);
            }
        });
        step.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int val = Integer.parseInt(step_number.getText());
                    if (val < 0) {
                        step_error.setText("Please enter a positive integer!");
                    }
                    else {
                        for (int i = 0; i < val; i += 1){
                            Critter.worldTimeStep();
                            Critter.displayWorld(gridz);
                        }
                        //statistics.refreshStats();
                        step_error.setText("");
                        Critter.displayWorld(gridz);
                    }
                } catch (NumberFormatException e1) {
                    step_error.setText("Please enter a positive integer!");
                }
            }
        });
        seed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    long val = Long.parseLong(seed_number.getText(), 10);
                    if (val < 0) {
                        seed_error.setText("Please enter a positive integer!");
                    }
                    else {
                        seed_error.setText("");
                        Critter.setSeed(val);
                        //statistics.refreshStats();
                        Critter.displayWorld(gridz);
                    }
                } catch (NumberFormatException e1) {
                    seed_error.setText("Please enter a positive integer!");
                }
            }
        });
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(1);
            }
        });
//        animate.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Animate.animate(3);
//
//            }
//        });

        // Find size of screen and set window sizes
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth()/2);
        primaryStage.setHeight(primaryScreenBounds.getHeight()/2);

        // Adding objects/children and placing children
        top.setSpacing(10);
        bottom.setSpacing(10);
        left.setSpacing(10);
        right.setSpacing(10);
        center.setHgap(10);
        center.setVgap(10);

        top.setPadding(new Insets(15, 12, 15, 12));
        bottom.setPadding(new Insets(15, 12, 15, 12));
        left.setPadding(new Insets(15, 12, 15, 12));
        right.setPadding(new Insets(15, 12, 15, 12));

        int row = 0;
        top.getChildren().addAll(listOfCritters, number_critters, make);
        bottom.getChildren().addAll(quit);
        left.getChildren().addAll(seed);
        center.setPadding(new Insets(15, 12, 15, 12));
        center.add(new Label("Make Critters:"), 0, row);
        center.add(listOfCritters, 0, row + 1);
        center.add(number_critters, 1, row + 1);
        center.add(make, 2, row + 1);
        center.add(new Label("Critter Motion Handling:"), 0, row + 3);
        center.add(step_number, 0, row + 4);
        center.add(step, 1, row + 4);
        center.add(animate, 0, row + 5);
        // Add a slider if we can
        center.add(new Label("Set Seed:"), 0, row + 7);
        center.add(seed_number, 0, row + 8);
        center.add(seed, 1, row + 8);
        center.add(new Label("Scale Grid:"), 0, row + 10);
//        center.add(grid, 1, row + 11);
//        center.add(grid_scale, 0, row + 11);
        center.add(quit, 2, row + 11);

        // Error messages
        center.add(make_error, 3, row + 1);
        center.add(step_error, 2, row + 4);
        center.add(seed_error, 2, row + 8);

        // Final steps to display
        borders.setTop(top);
        borders.setBottom(bottom);
        borders.setRight(right);
        borders.setLeft(left);
        //borders.setCenter(center);
        Scene myScene = new Scene(center, primaryScreenBounds.getWidth()/2, primaryScreenBounds.getHeight()/2);
        myScene.setUserAgentStylesheet("Ordo.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
        Critter.displayWorld(new GridPane());
    }


    public static void main(String[] args) {
        launch(args);
    }

}
