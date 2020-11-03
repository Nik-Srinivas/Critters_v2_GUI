/*
 * CRITTERS Critter1.java
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

import javafx.scene.paint.Color;

import java.util.List;

public class Critter1 extends Critter{
    @Override
    public CritterShape viewShape() {
        return CritterShape.CIRCLE;
    }

    public javafx.scene.paint.Color viewColor() {
        return Color.RED;
    }

    // Critter1 reproduces unless direction = 2, then it runs
    @Override
    public void doTimeStep() {
        int dir = getRandomInt(8);
        if (dir == 2) {run(dir);}
        else {reproduce(this,dir);};
    }

    // only fights Critter4
    @Override
    public boolean fight(String oponent) {
        if (oponent.equals("4")){
            int dir = 0;
            while (dir < 8) {
                if (look(dir, true) == null) {
                    run(dir);
                    break;
                }
                dir++;
            }
            return false;
        }

        return true;
    }

    public String toString() {
        return "1";
    }

    public static String runStats(List<Critter> crit1) {
        String s = "" + crit1.size() + " critters as follows -> 1: " + crit1.size();
        return s;
    }
}
