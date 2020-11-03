/*
 * CRITTERS Critter.java
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

import java.lang.reflect.InvocationTargetException;
import java.util.*;

// JavaFX Packages for Displaying World

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    /**
     * Checks whether a specific position is unoccupied
     *
     * @param direction int
     * @param steps bool
     * @return String, whether position is unoccupied
     */
    protected final String look(int direction, boolean steps) {
        energy -= Params.LOOK_ENERGY_COST;
        int[] lookLocation = move(direction, x_coord, y_coord);
        if (steps){
            lookLocation = move(direction, lookLocation[0], lookLocation[1]);
        }
        Critter hiddenCrit = findCritter(lookLocation[0],lookLocation[1]);

        if (hiddenCrit != null){
            return (hiddenCrit.toString());
        }

        return null;
    }
    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static String runStats(List<Critter> critters) {
        String stats = ("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            stats = (prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        return stats;
    }


    /**
     * Displays grid with current active critters
     *
     */

    public static void displayWorld(GridPane pane) {

        for (int y = 0; y < Main.Y_TILES; y++) {
            for (int x = 0; x < Main.X_TILES; x++) {
                Main.world[x][y] = null;
            }
        }
        for (Critter c : population) {
            Main.world[c.x_coord][c.y_coord] = c;
        }

        pane.getChildren().clear();
        //paintGridLines(pane);
        pane.setPrefSize(Main.W, Main.H);

        for (int y = 0; y < Main.Y_TILES; y++) {
            for (int x = 0; x < Main.X_TILES; x++) {
                if (Main.world[x][y] == null) {
                    Main.Square tile = new Main.Square(x, y, Main.colorIndex);
                    pane.add(tile, x, y);
                }
                else {
                    Shape s = Main.world[x][y].getShape(Main.world[x][y].viewShape(), Main.world[x][y]);
                    pane.add(new Main.Square(x, y, Main.colorIndex), x, y);
                    pane.add(s, Main.world[x][y].x_coord, Main.world[x][y].y_coord);
                }

            }
        }
    }

    /*
     * Paint the grid lines in orange.  The purpose is two-fold -- to indicate boundaries of
     * icons, and as place-holders for empty cells.  Without placeholders, grid may not display properly.
     */
//    private static void paintGridLines(GridPane grid) {
//        if (Params.WORLD_HEIGHT >= Params.WORLD_WIDTH) {
//            size = 575/Params.WORLD_HEIGHT;
//        }
//        else {
//            size = 575/Params.WORLD_WIDTH;
//        }
//        for (int i = 0; i < Params.WORLD_WIDTH; i++) { // columns
//            for (int j = 0; j < Params.WORLD_HEIGHT; j++) { // rows
//                Shape s = new Rectangle(size, size);
//                s.setFill(null);
//                s.setStroke(Color.ORANGE);
//                grid.add(s, i, j);
//            }
//        }
//    }

    /*
     * Returns a square or a circle depending on the shapeIndex parameter
     *
     */
    private Shape getShape(CritterShape shape, Critter critter) {
        Shape gridShape = null;

        if (shape.name() == "CIRCLE") gridShape = new Circle((Main.TILE_SIZE - 5)/2);

        else if (shape.name() ==  "SQUARE") gridShape = new Rectangle((Main.TILE_SIZE - 5), (Main.TILE_SIZE - 5));

        else if (shape.name() ==  "DIAMOND") {
            gridShape = new Polygon();
            ((Polygon) gridShape).getPoints().addAll(
                    (double) (Main.TILE_SIZE - 5)/2, (double) 2,
                    (double) (Main.TILE_SIZE - 5)-1, (double) (Main.TILE_SIZE - 5)/2,
                    (double) ((Main.TILE_SIZE - 5))/2, (double) (Main.TILE_SIZE - 5)-1,
                    (double) 1, (double) (Main.TILE_SIZE - 5)/2);
        }

        else if (shape.name() == "TRIANGLE") {
            gridShape = new Polygon();
            ((Polygon) gridShape).getPoints().addAll(
                    (double) (Main.TILE_SIZE - 5)-1, (double) (Main.TILE_SIZE - 5)-1,
                    (double) 1, (double) (Main.TILE_SIZE - 5)-1,
                    (double) ((Main.TILE_SIZE - 5))/2-1, 1.0);
        }


        else if (shape.name() == "STAR"){
            gridShape = new Polygon();
            ((Polygon) gridShape).getPoints().addAll(
                    (double) (Main.TILE_SIZE - 5)/2, (double) ((Main.TILE_SIZE - 5)/6)*0.5,
                    (double) ((Main.TILE_SIZE - 5)/6)*4, (double) ((Main.TILE_SIZE - 5)/6)*2,
                    (double) ((Main.TILE_SIZE - 5)/6)*5.5, (double) ((Main.TILE_SIZE - 5)/2),
                    (double) ((Main.TILE_SIZE - 5)/6)*4, (double) ((Main.TILE_SIZE - 5)/6)*4,
                    (double) ((Main.TILE_SIZE - 5)/6)*5, (double) ((Main.TILE_SIZE - 5)/6)*5.5,
                    (double) ((Main.TILE_SIZE - 5)/2), (double) ((Main.TILE_SIZE - 5)/6)*5,
                    (double) ((Main.TILE_SIZE - 5)/6), (double) ((Main.TILE_SIZE - 5)/6)*5.5,
                    (double) ((Main.TILE_SIZE - 5)/6)*2, (double) ((Main.TILE_SIZE - 5)/6)*4,
                    (double) ((Main.TILE_SIZE - 5)/6)*0.5, (double) ((Main.TILE_SIZE - 5)/2),
                    (double) ((Main.TILE_SIZE - 5)/6)*2, (double) ((Main.TILE_SIZE - 5)/6)*2
            );
        }


        else gridShape = new Circle((Main.TILE_SIZE - 5)/2);


        gridShape.setStroke(critter.viewOutlineColor());
        gridShape.setFill(critter.viewFillColor());
        return gridShape;
    }


	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */



    // Private Variables
    private int energy = 0;
    private int x_coord;
    private int y_coord;

    private static final List<Critter> population = new ArrayList<Critter>();
    private static final List<Critter> babies = new ArrayList<Critter>();

    //creating flags to check each critter's turns
    private boolean moveFlag = false;
    private static boolean  conflictPhase = false;

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static final String myPackage;
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    private static Random rand = new Random();
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }
    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }


    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name Name of Critter class
     * @throws InvalidCritterException Bad input string etc
     */
    public static void createCritter(String critter_class_name) throws InvalidCritterException {
        // TODO: Complete this method
        try {
            String critterName = myPackage + "." + critter_class_name; //how to access the individual critter functions given they all are under the same package

            Class<?> crit_class = Class.forName(critterName);
            Object crit_object = crit_class.getConstructor().newInstance();

            //setting critter's private variables
            population.add((Critter) crit_object);
            population.get(population.size()-1).energy = Params.START_ENERGY;
            population.get(population.size()-1).x_coord = Critter.getRandomInt(Params.WORLD_WIDTH);
            population.get(population.size()-1).y_coord = Critter.getRandomInt(Params.WORLD_HEIGHT);
            population.get(population.size()-1).moveFlag = false;
        }

        // Error Handling
        catch (ClassNotFoundException | NoSuchMethodException e){
            throw new InvalidCritterException(critter_class_name);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException e
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> instances = new ArrayList<>();
        Object critterClass = null;
        try {
            critterClass = Class.forName(myPackage + "." + critter_class_name).getConstructor().newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidCritterException(critter_class_name);
        }
        for (Critter c : population) {
            if(((Critter) critterClass).getClass().isInstance(c)){
                instances.add(c);
            }
        }
        return instances;

    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        // TODO: Complete this method
        population.clear();
    }

    /**
     * Is 1 in-game "move.
     * Performs the move and attack if necessary
     * Cleans up the dead critters post attack
     * Spawns babies
     */
    public static void worldTimeStep() {
        // TODO: Complete this method
        for (Critter crit : population){
            crit.doTimeStep();
            crit.moveFlag = false;
        }

        doEncounters();

        for(Critter crit : population) {
            crit.energy -= Params.REST_ENERGY_COST;
        }
        try {
            genClover();
        } catch (InvalidCritterException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).energy <= 0) {
                population.remove(i);
                i--;
            }
        }
        population.addAll(babies);
        babies.clear();
    }

    /**
     * If 2 critters are on the same space after a move, they must "fight" and the critter with the higher energy survives.
     * This method ensures only 1 critter should exist in each location after a turn.
     */
    private static void doEncounters() {
        conflictPhase = true;
        for (Critter c1 : population) {
            for (Critter c2 : population) {
                if ((c1 != c2) && (c1.x_coord == c2.x_coord && c1.y_coord == c2.y_coord)) {
                    boolean fight1 = c1.fight(c2.toString());
                    boolean fight2 = c2.fight(c1.toString());

                    if ((c1 != c2) && (c1.x_coord == c2.x_coord && c1.y_coord == c2.y_coord)) {
                        if (c1.getEnergy() > 0 && c2.getEnergy() > 0) {
                            int power1 = 0, power2 = 0;
                            if (fight1) {
                                power1 = getRandomInt(c1.energy);
                            }
                            if (fight2) {
                                power2 = getRandomInt(c2.getEnergy());
                            }

                            // c1 wins fight
                            if (power1 > power2) {
                                c1.energy += c2.energy / 2;
                                c2.energy = 0;
                            }
                            // c2 wins fight
                            else {
                                c2.energy += c1.energy / 2;
                                c1.energy = 0;
                            }
                        }
                    }
//                    if (c1.getEnergy() < 0) population.remove(c1);
//                    else if (c2.getEnergy() < 0) population.remove(c2);
                }
            }
        }

        conflictPhase = false;
    }

    /**
     * Uses Params.REFRESH_CLOVER_COUNT to determine how many clovers to generate
     * @throws InvalidCritterException e
     */
    private static void genClover() throws InvalidCritterException {
        for(int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++)
            createCritter("Clover");
    }



    /**
     * One in-game move, depends on specific critter subclass
     **/
    public abstract void doTimeStep();

    /**
     * Depends on specific critter subclass
     **/
    public abstract boolean fight(String oponent);

    /** a one-character long string that visually depicts your critter
     * in the ASCII interface **/
    public String toString() {
        return "";
    }
    protected int getEnergy() {
        return energy;
    }

    /**
     * Check to see if there is another critter in the current location
     *
     * @param x,y,direction
     * @return True/False
     */
    private Critter findCritter(int x, int y) {
        for(Critter crit : population) {
            if(crit.x_coord == x && crit.y_coord == y && crit.energy > 0)
                return crit;
        }
        return null;
    }

    /**
     * Moves critter 1 space if it hasn't yet moved this turn
     *
     * @param direction  8 possible direction on a grid (up, down, left, right and the 4 diagonals
     */
    protected final void walk(int direction) {
        // TODO: Complete this method
        if (moveFlag == false){
            int[] potentialLocation = move(direction, x_coord, y_coord);
            if (!(findCritter(potentialLocation[0], potentialLocation[1]) != null && conflictPhase)) {
                x_coord = potentialLocation[0];
                y_coord = potentialLocation[1];
            }
            moveFlag = true;
            energy -= Params.WALK_ENERGY_COST;
        }
    }

    /**
     * Moves critter 2 spaces if it hasn't yet moved this turn
     *
     * @param direction 8 possible direction on a grid (up, down, left, right and the 4 diagonals
     */
    protected final void run(int direction) {
        // TODO: Complete this method
        if (moveFlag == false){
            int[] potentialLocation = move(direction, x_coord, y_coord);
            potentialLocation = move(direction, potentialLocation[0], potentialLocation[1]);
            if (!(findCritter(potentialLocation[0], potentialLocation[1]) != null && conflictPhase)) {
                x_coord = potentialLocation[0];
                y_coord = potentialLocation[1];
            }
            moveFlag = true;
            energy -= Params.RUN_ENERGY_COST;
        }
    }

    /**
     * Finds out where the critter "would" move given a direction and starting location.
     * It doesn't actually move it because it needs to check if there is a critter already there
     * which it does in the run/walk methods
     *
     * @param direction 8 possible direction on a grid (up, down, left, right and the 4 diagonals
     * @param x  x coordinate
     * @param y  y coordinate
     * @return New x and y coordinates in an array[]
     */
    private int[] move(int direction, int x, int y) {
        int tempX = x;
        int tempY = y;
        // east
        if (direction == 0) {
            tempX++;
        }
        // northeast
        else if (direction == 1) {
            tempX++;
            tempY--;
        }
        // north
        else if (direction == 2) {
            tempY--;
        }
        // northwest
        else if (direction == 3) {
            tempX--;
            tempY--;
        }
        // west
        else if (direction == 4) {
            tempX--;
        }
        // southwest
        else if (direction == 5) {
            tempX--;
            tempY++;
        }
        // south
        else if (direction == 6) {
            tempY++;
        }
        // southeast
        else {
            tempX++;
            tempY++;
        }
        //Cases when critter wraps around the map
        if(tempX < 0)
            tempX += Params.WORLD_WIDTH;
        else if(tempX > Params.WORLD_WIDTH-1)
            tempX -= Params.WORLD_WIDTH;

        if(tempY < 0)
            tempY += Params.WORLD_HEIGHT;
        else if(tempY > Params.WORLD_HEIGHT-1)
            tempY -= Params.WORLD_HEIGHT;

        return (new int[]{tempX, tempY});
    }

    /**
     * Creates an offspring and spawns it in one of the adjacent spaces
     * Adjusts energies to reflect reproduction
     *
     * @param offspring  new critter
     * @param direction  8 possible direction on a grid (up, down, left, right and the 4 diagonals
     */
    protected final void reproduce(Critter offspring, int direction) {
        // TODO: Complete this method
        if(energy < Params.MIN_REPRODUCE_ENERGY)
            return;
        // Assigning energy
        offspring.energy = energy/2;
        energy = (int) Math.ceil(energy/2);
        // Assigning location
        int[] coordArr = move(direction, x_coord, y_coord);
        offspring.x_coord = coordArr[0];
        offspring.y_coord = coordArr[1];

        babies.add(offspring);
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
