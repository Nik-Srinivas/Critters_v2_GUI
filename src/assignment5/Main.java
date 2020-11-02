package assignment5;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import sun.reflect.generics.tree.ClassSignature;
import sun.reflect.generics.visitor.Visitor;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Main extends Application{


    private static List<String> critter_classes;
    private static Integer[] anim_speeds = {1,2,5,10,20,50,100};
    private static String myPackage;
    private static boolean run_active = true;
    private String[] classNames = new String[100];
    private int numberOfFiles = 0;
    private int numberOfCritters = 0;
    private String[] critterNames;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    // find all Critter subclasses in Project
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


    public static void main(String[] args) {
        // launching javafx
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            /*
             * Create the first window to display control
             */
            // creating Pane's
            BorderPane root = new BorderPane(); // holds all panes
            GridPane grid = new GridPane(); // holds display, quit, time step, seed, create critter
            GridPane center = new GridPane(); // holds ScrollPane
            center.setMinWidth(1000);
            AnchorPane animation_grid = new AnchorPane(); // holds animation
            ScrollPane scrollpane = new ScrollPane(); // holds runStats
            scrollpane.setContent(center);
            scrollpane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
            scrollpane.setMaxHeight(300);
            scrollpane.setTranslateY(70);
            root.setTop(grid);
            root.setCenter(scrollpane);
            root.setBottom(animation_grid);
            // adding header for control
            Text header = new Text();
            header.setText("Welcome to Critters");
            header.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
            header.setTranslateX(5);
            header.setTranslateY(10);
            // Create Critter
            Text critter_name = new Text(); // title for type of critters
            critter_name.setText("Choose a Critter:");
            critter_name.setFont(Font.font("Verdana", 12));
            critter_name.setTranslateX(5);
            critter_name.setTranslateY(40);
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
            Text critter_num = new Text(); // title for number of critters
            critter_num.setText("Choose # of Critters:");
            critter_num.setFont(Font.font("Verdana", 12));
            critter_num.setTranslateX(5);
            critter_num.setTranslateY(80);
            TextField critter_num_input = new TextField(); // user input for num of critters
            critter_num_input.setMinWidth(225);
            critter_num_input.setMaxWidth(225);
            critter_num_input.setTranslateX(150);
            critter_num_input.setTranslateY(80);
            Button critter_add_btn = new Button("Add Critters"); // button to add critters
            critter_add_btn.setTranslateX(380);
            critter_add_btn.setTranslateY(80);
            Text critters_added = new Text(); // message when added critters
            critters_added.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
            critters_added.setTranslateX(150);
            critters_added.setTranslateY(100);
            critters_added.setFill(Color.RED);
            // run the world (time steps)
            Text timestep_num = new Text(); // title for time steps
            timestep_num.setText("Choose # of Steps:");
            timestep_num.setFont(Font.font("Verdana", 12));
            timestep_num.setTranslateX(5);
            timestep_num.setTranslateY(130);
            TextField timestep_input = new TextField(); // user input for num of steps
            timestep_input.setMinWidth(225);
            timestep_input.setMaxWidth(225);
            timestep_input.setTranslateX(150);
            timestep_input.setTranslateY(130);
            Button timestep_btn = new Button("Add Steps"); // button to add steps
            timestep_btn.setTranslateX(380);
            timestep_btn.setTranslateY(130);
            Text timestep_added = new Text(); // message when added critters
            timestep_added.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
            timestep_added.setTranslateX(150);
            timestep_added.setTranslateY(150);
            timestep_added.setFill(Color.RED);
            // set the seed
            Text seed_num = new Text(); // title for time steps
            seed_num.setText("Choose Seed:");
            seed_num.setFont(Font.font("Verdana", 12));
            seed_num.setTranslateX(5);
            seed_num.setTranslateY(180);
            TextField seed_input = new TextField(); // user input for num of steps
            seed_input.setMinWidth(225);
            seed_input.setMaxWidth(225);
            seed_input.setTranslateX(150);
            seed_input.setTranslateY(180);
            Button seed_btn = new Button("Add Seed"); // button to add steps
            seed_btn.setTranslateX(380);
            seed_btn.setTranslateY(180);
            Text seed_added = new Text(); // message when added critters
            seed_added.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
            seed_added.setTranslateX(150);
            seed_added.setTranslateY(200);
            seed_added.setFill(Color.RED);
            // runStats
//            int y = 0;
//            Collections.sort(critter_classes);
//            for (int i = 0; i < critter_classes.size(); i++) {
//                Text checkboxName = new Text(); // title for critter checkbox
//                checkboxName.setText(critter_classes.get(i));
//                checkboxName.setFont(Font.font("Verdana", 12));
//                checkboxName.setTranslateX(5);
//                checkboxName.setTranslateY(y);
//                CheckBox checkbox = new CheckBox(); // checkbox to show runStats
//                checkbox.setTranslateX(75);
//                checkbox.setTranslateY(y);
//                Text test = new Text(); // text for runStats output
//                test.setFont(Font.font("Verdana", 12));
//                test.setTranslateX(100);
//                test.setTranslateY(y);
//                y += 20;
//                String critter = critter_classes.get(i);
//                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
//                    public void handle(ActionEvent e)
//                    {
//                        startTask(checkbox, grid, test, critter);
//                    }
//                };
//                // set event to checkbox
//                checkbox.setOnAction(event);
//                // add each critter runStats to center pane
//                center.getChildren().add(checkboxName);
//                center.getChildren().add(checkbox);
//                center.getChildren().add(test);
//            }
            // animation
            ChoiceBox<Integer> run_choices = new ChoiceBox(); // choices of critters
            run_choices.getItems().addAll(anim_speeds);
            run_choices.getSelectionModel().selectFirst();
            run_choices.setTranslateX(5);
            run_choices.setTranslateY(-10);
            Button run_btn = new Button("Run Animation"); // button to add steps
            run_btn.setTranslateX(70);
            run_btn.setTranslateY(-10);
            // terminate the program
            Button quit_btn = new Button("Quit Critters"); // button to add steps
            quit_btn.setTranslateX(400);
            quit_btn.setTranslateY(10);
            // view the world
            Button viewWorld_btn = new Button("Display World"); // button to add steps
            viewWorld_btn.setTranslateX(250);
            viewWorld_btn.setTranslateY(10);
            // adding nodes to grid
            grid.getChildren().add(header);
            grid.getChildren().add(critter_name);
            grid.getChildren().add(critter_num);
            //grid.getChildren().add(critterNames);
            grid.getChildren().add(critter_num_input);
            grid.getChildren().add(critter_add_btn);
            grid.getChildren().add(critters_added);
            grid.getChildren().add(timestep_num);
            grid.getChildren().add(timestep_input);
            grid.getChildren().add(timestep_btn);
            grid.getChildren().add(timestep_added);
            grid.getChildren().add(seed_num);
            grid.getChildren().add(seed_input);
            grid.getChildren().add(seed_btn);
            grid.getChildren().add(seed_added);
            grid.getChildren().add(quit_btn);
            grid.getChildren().add(viewWorld_btn);
            animation_grid.getChildren().add(run_btn);
            animation_grid.getChildren().add(run_choices);
            // setting the scene and displaying to the stage
            Scene scene = new Scene(root, 500, 600);
            primaryStage.setX(20);
            primaryStage.setY(20);
            primaryStage.setTitle("Critter Control");
            primaryStage.setScene(scene);
            primaryStage.show();
            /*
             * Create the second window to display world
             */
            Stage worldStage = new Stage(); // creates a second stage for the button.
            worldStage.setX(700);
            worldStage.setY(50);
            worldStage.setTitle("World");
            GridPane worldGrid = new GridPane();
            Scene secondScene = new Scene(worldGrid, 600, 600); // creates a second scene object with the Stackpane
            worldStage.setScene(secondScene); // puts the scene onto the second stage
            /*
             * Action to be performed when run button is pressed.
             * Runs the world continuously until stopped, all buttons disabled while running
             */
            run_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
                @Override
                public void handle(ActionEvent event) {
                    if (run_active) {
                        run_active = false;
                        run_btn.setText("Stop Animation");
                        startTask(run_choices, worldGrid, grid);
                    }
                    else {
                        run_active = true;
                        run_btn.setText("Run Animation");
                        grid.setDisable(false);
                    }
                }
            });
            /*
             * Action to be performed when view world button is pressed.
             * Shows the world
             */
            viewWorld_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
                @Override
                public void handle(ActionEvent event) {
                    Critter.displayWorld(worldGrid);
                    worldStage.show(); // display the stage with the scene
                }
            });
            /*
             * Action to be performed when quit button is pressed.
             * Exits the program
             */
            quit_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
                @Override
                public void handle(ActionEvent event) {
                    System.exit(0);
                }
            });

            /*
             * Action to be performed when seed button is pressed.
             * Sets the seed
             */
            seed_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
                @Override
                public void handle(ActionEvent event) {
                    // creating critters
                    grid.getChildren().remove(seed_added);
                    grid.getChildren().add(seed_added);
                    long count = 0;
                    try {
                        if (seed_input.getText().equals("")) {
                            count = 1;
                        }
                        else {
                            count = Long.parseLong(seed_input.getText());
                        }
                        Critter.setSeed(count);
                        seed_added.setText("Seed set to " + count);
                    } catch (NumberFormatException e) {
                        seed_added.setText("error processing: " + seed_input.getText());
                    }
                }
            });

            /*
             * Action to be performed when time step button is pressed.
             * Runs timestep with given input
             */
            timestep_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
                @Override
                public void handle(ActionEvent event) {
                    // creating critters
                    grid.getChildren().remove(timestep_added);
                    grid.getChildren().add(timestep_added);
                    int count = 0;
                    try {
                        if (timestep_input.getText().equals("")) {
                            count = 1;
                        }
                        else {
                            count = Integer.parseInt(timestep_input.getText());
                            if (count <= 0) {
                                count = 1;
                            }
                        }
                        for (int i = 0; i < count; i++) {
                            Critter.worldTimeStep();
                        }
                        timestep_added.setText(count + " steps added");
                    } catch (NumberFormatException e) {
                        timestep_added.setText("error processing: " + timestep_input.getText());
                    }
                    Critter.displayWorld(worldGrid);
                    worldStage.show(); // display the stage with the scene
                }
            });

            /*
             * Action to be performed when critter button is pressed.
             * Creates Critters
             */
//            critter_add_btn.setOnAction(new EventHandler<ActionEvent>() { // what to do when butt is pressed
//                @Override
//                public void handle(ActionEvent event) {
//                    // creating critters
//                    grid.getChildren().remove(critters_added);
//                    grid.getChildren().add(critters_added);
//                    int count = 0;
//                    try {
//                        if (critterNames.getValue() == null) {
//                            critters_added.setText("Please select a Critter");
//                        }
//                        else {
//                            if (critter_num_input.getText().equals("")) {
//                                count = 1;
//                            }
//                            else {
//                                count = Integer.parseInt(critter_num_input.getText());
//                                if (count <= 0) {
//                                    count = 1;
//                                }
//                            }
//                            for (int i = 0; i < count; i++) {
//                                Critter.createCritter(critterNames.getValue());
//                            }
//                            critters_added.setText(count + " " + critterNames.getValue() + " added");
//                        }
//                    } catch (NumberFormatException | InvalidCritterException e) {
//                        critters_added.setText("error processing: " + critter_num_input.getText());
//                    }
//                    Critter.displayWorld(worldGrid);
//                    worldStage.show(); // display the stage with the scene
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * Threading for running the animation
     */
    public void startTask(ChoiceBox run_cb, GridPane worldGrid, GridPane grid)
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                runTask(run_cb, worldGrid, grid);
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
    public void runTask(ChoiceBox run_cb, GridPane worldGrid, GridPane grid) {
        while(!run_active) {
            try {
                grid.setDisable(true);
                for (int i = 0; i < (int) run_cb.getValue(); i++) {
                    Critter.worldTimeStep();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Critter.displayWorld(worldGrid);
                    }
                });
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
            }
        }
    }
    /*
     * Thread for continuously running runStats
     */
    public void startTask(CheckBox run_stats,  GridPane grid, Text test, String critter)
    {
        // Create a Runnable
        Runnable task1 = new Runnable()
        {
            public void run()
            {
                runTask(run_stats, grid, test, critter);
            }
        };

        // Run the task in a background thread
        Thread backgroundThread1 = new Thread(task1);
        // Terminate the running thread if the application exits
        backgroundThread1.setDaemon(true);
        // Start the thread
        backgroundThread1.start();
    }
    public void runTask(CheckBox run_stats,  GridPane grid, Text test, String critter) {
        while(run_stats.isSelected()) {
            try {
                try {
                    List<Critter> critters = Critter.getInstances(critter);
                    if (run_stats.isSelected()) {
                        Class<?> critterClass = Class.forName(myPackage + "." + critter);
                        Constructor<?> constructor = critterClass.getConstructor();
                        Object new_critter = constructor.newInstance();
                        Method m = new_critter.getClass().getDeclaredMethod("runStats", List.class);
                        String str_stats = ((String) m.invoke(critterClass, critters));
                        str_stats = str_stats.replace("\n", "");
                        str_stats = str_stats.trim().replaceAll(" +", " ");
                        test.setText(str_stats);
                    }
                    else {
                        test.setText("");
                    }
                }
                catch (ClassNotFoundException
                        | NoSuchMethodException | SecurityException
                        | IllegalAccessException | InvalidCritterException
                        | IllegalArgumentException | InvocationTargetException
                        | InstantiationException k) {
                    try {
                        List<Critter> critters = Critter.getInstances(critter);
                        critters = Critter.getInstances(critter);
                        test.setText(((String) Critter.runStats(critters)));
                    } catch (InvalidCritterException e1) {
                    }
                }
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
            }
        }
        test.setText("");
    }
}