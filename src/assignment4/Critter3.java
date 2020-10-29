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


package assignment4;

public class Critter3 extends Critter{

    public boolean walk = false;
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

    // only fights Critter4
    @Override
    public boolean fight(String oponent) {
        if (getEnergy() > 20 && oponent.equals("2")) return true;
        return false;
    }

    public String toString() {
        return "3";
    }
}
