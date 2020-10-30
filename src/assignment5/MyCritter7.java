package assignment5;

import assignment5.Critter.TestCritter;

/**
 * Fighter, always fight when encounter other critters.
 */
public class MyCritter7 extends TestCritter {

    @Override
    public CritterShape viewShape() {
        return null;
    }

    @Override
    public void doTimeStep() {
    }

    @Override
    public boolean fight(String opponent) {
        return true;
    }

    @Override
    public String toString() {
        return "7";
    }
}
