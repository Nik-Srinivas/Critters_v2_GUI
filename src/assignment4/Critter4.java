/*
 * CRITTERS Critter4.java
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

public class Critter4 extends Critter{
    public boolean fought = true;

    // Critter4 walk if energe < 10, reproduces if energy between 10 - 15, and runs otherwise
    @Override
    public void doTimeStep() {
        int dir = getRandomInt(8);
        if (getEnergy() < 10) walk(dir);
        else if (getEnergy() > 10 && getEnergy() < 15) reproduce(this,dir);
        else run(dir);
    }

    // only fights Critter4
    @Override
    public boolean fight(String oponent) {
        if (oponent.equals("3") && !fought) {
            fought = true;
            return true;
        }
        return false;
    }

    public String toString() {
        return "4";
    }
}
