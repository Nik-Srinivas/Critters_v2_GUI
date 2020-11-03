package assignment5;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RunWorld {
    int tracker = 0;
    TimerTask animationTask = new TimerTask()  {
        public void run() {
            Critter.worldTimeStep();
            Critter.displayWorld(Main.gridz);
            tracker++;
            System.out.println("Seconds passed: " + tracker);
        }
    };
    public int start(int count) {
        try {
            for (int i = 0; i < count; i++) {
                TimeUnit.MILLISECONDS.sleep(500);

                animationTask.run();
            }
        } catch (Exception e1) {
            System.out.println("Exception Thrown");
        }
        return 0;

    }

    public static void run(int animationCount) {
        RunWorld animation = new RunWorld();
        animation.start(animationCount);
    }
}
