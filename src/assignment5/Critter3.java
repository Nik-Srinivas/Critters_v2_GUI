/*
 * CRITTERS Critter3.java
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

public class Critter3 extends Critter{

    public boolean walk = false;

    @Override
    public CritterShape viewShape() {
        return CritterShape.SQUARE;
    }

    public javafx.scene.paint.Color viewColor() {
        return Color.BLUE;
    }

    // Critter3 does intervals of walking and running
    @Override
    public void doTimeStep() {
        int dir = getRandomInt(8);

        if (walk) {
            walk(dir);
            walk = false;
        }
        else run(dir);
    }

    // only fights if it has a lot of energy
    @Override
    public boolean fight(String oponent) {
        if(getEnergy() > 50) {
            super.look(getRandomInt(8), true);
            return true;
        }
        return false;
    }

    public String toString() {
        return "3";
    }
}
