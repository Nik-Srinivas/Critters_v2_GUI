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

import javafx.application.Application;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main extends Application {
    private static final int TILE_SIZE = 40;
    private static final int W = Params.WORLD_WIDTH * 40;
    private static final int H = Params.WORLD_HEIGHT * 40;

    private static final int X_TILES = W / TILE_SIZE;
    private static final int Y_TILES = H / TILE_SIZE;
    public static ByteArrayOutputStream testOutputString;

    private Square[][] grid = new Square[X_TILES][Y_TILES];
    private Scene scene;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Square tile = new Square(x, y);

                grid[x][y] = tile;
                root.getChildren().add(tile);

            }
        }

        return root;
    }

    private class Square extends StackPane {
        private int x, y;
        private boolean isOpen = false;

        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        private Text text = new Text();

        public Square(int x, int y) {
            this.x = x;
            this.y = y;

            border.setStroke(Color.WHITE);
            border.setFill(Color.LIGHTGRAY);

            text.setFont(Font.font(18));
            text.setVisible(false);

            getChildren().addAll(border, text);

            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(createContent());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
