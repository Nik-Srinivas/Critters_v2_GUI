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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    /* Scanner connected to keyboard input, or input file */
    static Scanner kb;

    /* Input file, used instead of keyboard input if specified */
    private static String inputFile;

    /* If test specified, holds all console output */
    static ByteArrayOutputStream testOutputString;

    /* Use it or not, as you wish! */
    private static boolean DEBUG = false;

    /* if you want to restore output to console */
    static PrintStream old = System.out;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two
     *             parameters -- the first is a file name, and the
     *             second is test (for test output, where all output
     *             to be directed to a String), or nothing.
     */
    public static void main(String[] args) {

        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
            }
            if (args.length >= 2) {
                /* If the word "test" is the second argument to java */
                if (args[1].equals("test")) {
                    /* Create a stream to hold the output */
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    /* Save the old System.out. */
                    old = System.out;
                    /* Tell Java to use the special stream; all
                     * console output will be redirected here from
                     * now */
                    System.setOut(ps);
                }
            }
        } else { // If no arguments to main
            kb = new Scanner(System.in); // Use keyboard and console
        }
        commandInterpreter(kb);
        System.out.flush();
    }

    /* Do not alter the code above for your submission. */

    /**
     * This function reads in user commands from the keyboard.
     *
     * @param kb  This is the keyboard Scanner
     */
    private static void commandInterpreter(Scanner kb) {
        //TODO Implement this method
        Critter.clearWorld();
        System.out.print("critters> ");
        String input = kb.nextLine();
        System.out.println();
        String[] commands = input.trim().split("\\s+");

        while (!commands[0].equals("quit")){
            /////////////////////////////SHOW/////////////////////////////
            if (commands[0].equals("show")) {
                if (commands.length == 1) {
                    Critter.displayWorld();
                }
                else {
                    System.out.println("error processing: " + input);
                }
            }
            /////////////////////////////STEP/////////////////////////////
            else if (commands[0].equals("step")){
                if (commands.length > 2){
                    System.out.println("error processing: " + input);
                }
                else{
                    int count = 1;
                    if(commands.length > 1){
                        try {
                            count = Integer.parseInt(commands[1]);
                        }
                        catch (NumberFormatException e) {
                            System.out.println("error processing: " + input);
                        }
                    }

                    for (int i = 0; i < count; i++) {
                        Critter.worldTimeStep();
                    }
                }
            }
            /////////////////////////////SEED/////////////////////////////
            else if (commands[0].equals("seed")){
                if (commands.length <= 2) {
                    if (commands.length == 1) { // needs second number for seed
                        System.out.println("error processing: " + input);
                    }
                    else {
                        try {
                            Critter.setSeed(Integer.parseInt(commands[1]));
                        } catch (NumberFormatException e) {
                            System.out.println("error processing: " + input);
                        }
                    }
                }
                else { //overall error like "seed  *wrong_string*"
                    System.out.println("error processing: " + input);
                }
            }
            /////////////////////////////CREATE/////////////////////////////
            else if (commands[0].equals("create")){
                if(commands.length > 3) {
                    System.out.println("error processing: " + input);
                }
                int n = 0;
                if (commands.length == 3) {// custom critter value
                    try {
                        n = Integer.parseInt(commands[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("error processing: " + input);
                    }
                }
                else { //default critter value
                    n = 1;
                }
                try {
                    for (int i = 0; i < n; i++) {
                        Critter.createCritter(commands[1]);
                    }
                } catch (InvalidCritterException e) {
                    System.out.println("error processing: " + input);
                }
            }
            /////////////////////////////STATS/////////////////////////////
            else if (commands[0].equals("stats")){

                if(commands.length > 2) { // only 1 input for stats
                    System.out.println("error processing: " + input);
                }
                else {
                    try {
                        List<Critter> critList = Critter.getInstances(commands[1]);
                        if (commands[1].equals("Clover") || commands[1].equals("Critter")) {
                            Critter.runStats(critList);
                        } else {
                            Class<?> critClass = Class.forName(myPackage + "." + commands[1]);
                            Constructor<?> constructor = critClass.getConstructor();
                            Object new_critter = constructor.newInstance();
                            Method statMethod = new_critter.getClass().getDeclaredMethod("runStats", List.class);
                            statMethod.invoke(critClass, critList);
                        }
                    } catch (InvalidCritterException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                        System.out.println("error processing: " + input);
                        System.out.println(e);
                    }
                }
            }
            /////////////////////////////CLEAR/////////////////////////////
            else if (commands[0].equals("clear")){
                if (commands.length == 1) {
                    Critter.clearWorld();
                }
                else {
                    System.out.println("error processing: " + input);
                }
            }
            /////////////////////////////Quit Error/////////////////////////////
            else {
                System.out.println("invalid command: " + input); // invalid input
            }

            System.out.print("critters> ");
            input = kb.nextLine();
            System.out.println();
            commands = input.trim().split("\\s+");
        }


    }
}
