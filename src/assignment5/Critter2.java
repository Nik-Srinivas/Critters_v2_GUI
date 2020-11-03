/*
 * CRITTERS Critter2.java
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

public class Critter2 extends Critter{
    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    public javafx.scene.paint.Color viewColor() {
        return Color.ORANGE;
    }

    // Critter1 runs unless direction = 0, then it walks
    @Override
    public void doTimeStep() {
        int dir = getRandomInt(8);
        if (dir == 0) walk(dir);
        else run(dir);
    }

    // only fights if energy is greater than 5
    @Override
    public boolean fight(String oponent) {
        if (getEnergy() > 5) return true;
        return false;
    }

    public String toString() {
        return "2";
    }

    public static String runStats(List<Critter> crit2) {
        String s = "" + crit2.size() + " critters as follows -> 1: " + crit2.size();
        return s;
    }
}
