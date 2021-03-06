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


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;



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

    private Scene scene;
    private String[] classNames = new String[100];
    private int numberOfFiles = 0;
    private int numberOfCritters = 0;
    private String[] critterNames;
    public static Critter[][] world = new Critter[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];

     // tile object
    public static class Square extends StackPane {
        private int x, y;
        private Color[] colorArray = {Color.LAVENDER, Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTSALMON, Color.LIGHTSLATEGREY,Color.LIGHTYELLOW};
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
                classNames[numberOfFiles] = fileEntry.getName();
                numberOfFiles += 1;
            }
        }
    }

    // World Stage
    static GridPane gridz = new GridPane();
    public class SecondStage extends Stage {

        SecondStage(){

            scene = new Scene(gridz);

            this.setScene(scene);
            this.show();
        }
    }

    // Stat Stage
    static GridPane stats = new GridPane();
    public class statStage extends Stage {

        statStage(){

            scene = new Scene(stats);

            this.setScene(scene);
            this.show();
        }
    }

    // refresh stats
    public int statRow = 0;
    public int statCol = 0;
    public void updateStats(GridPane pane) {
        String stats = Critter.runStats(Critter.population);
        pane.add(new Label(stats),statCol, statRow);
        statRow++;
        if (statRow >= 30) {
            statCol++;
            statRow = 0;
        }

    }

    @Override
    public void start(Stage primaryStage)  {

        primaryStage.setMaxHeight(600);
        primaryStage.setMaxWidth(600);

        // Stage Initialization
        Stage worldStage = new SecondStage();
        worldStage.setMaxWidth(Params.WORLD_WIDTH * 40);
        worldStage.setMaxHeight(Params.WORLD_HEIGHT * 41);
        Critter.displayWorld(gridz);
        worldStage.setTitle("World of Critters");

        // Stage Initialization
        Stage statStage = new statStage();
        statStage.setTitle("stats");
        statStage.setMaxHeight(600);
        statStage.setMaxWidth(600);

        // Layout Initialization
        BorderPane borders = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        GridPane center = new GridPane();

        // Button Initialization
        primaryStage.setTitle("Welcome to Critters!");
        Button seed = new Button("Set Seed");
        Button show = new Button("Show");
        Button make = new Button("Make");
        Button step = new Button("Time Step");
        Button run = new Button("Hold to Run");
        Button quit = new Button("Quit");

        Label make_error = new Label("");
        Label step_error = new Label("");
        Label seed_error = new Label("");

        // get Critter classes for create
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

        // TextFields for integer input
        TextField number_critters = new TextField();
        number_critters.setPromptText("Number of Critters...");
        TextField step_number = new TextField();
        step_number.setPromptText("Number of steps...");
        TextField seed_number = new TextField();
        seed_number.setPromptText("Seed value...");

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
                        Critter.displayWorld(gridz);
                    }
                } catch (NumberFormatException e1) {
                    seed_error.setText("Please enter a positive integer!");
                }
            }
        });

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
                        step_error.setText("");
                        Critter.displayWorld(gridz);
                        updateStats(stats);
                    }
                } catch (NumberFormatException e1) {
                    step_error.setText("Please enter a positive integer!");
                }
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(1);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), (ActionEvent event) -> {
            //System.out.println(run.isPressed() ? "pressed" : "released");
            if (run.isPressed()) {
                RunWorld.run(Integer.parseInt(step_number.getText()));
                updateStats(stats);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Find size of screen and set window sizes
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

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
        center.setPadding(new Insets(15, 12, 15, 12));
        center.add(new Label("Set Seed:"), 0, row+1);
        center.add(seed_number, 0, row+2);
        center.add(seed, 1, row+2 );
        center.add(new Label("Make Critters:"), 0, row+3);
        center.add(listOfCritters, 0, row + 4);
        center.add(number_critters, 1, row + 4);
        center.add(make, 2, row + 4);
        center.add(new Label("Run Game:"), 0, row + 5);
        center.add(step_number, 0, row + 6);
        center.add(step, 1, row + 6);
        center.add(run, 0, row + 7);
        center.add(quit, 1, row + 7);



        // Error messages
        center.add(make_error, 0, 10);
        center.add(step_error, 0, 11);
        center.add(seed_error, 0, 9);

        // Final steps to display
        borders.setTop(top);
        borders.setBottom(bottom);
        borders.setRight(right);
        borders.setLeft(left);
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
