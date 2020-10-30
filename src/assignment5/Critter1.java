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

public class Critter1 extends Critter{
    @Override
    public CritterShape viewShape() {
        return null;
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
        if (oponent.equals("4")) return true;

        return false;
    }

    public String toString() {
        return "1";
    }
}
