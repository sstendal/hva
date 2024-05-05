package hva;

import hva.config.Config;
import hva.config.ConfigLoader;

import java.util.Timer;
import java.util.TimerTask;

public class HvaTimer {

    private static final Config config = ConfigLoader.load();

    private Timer timer;
    private final Runnable action;
    private Integer customDelay = null;

    public HvaTimer(Runnable action) {
        this.action = action;
    }

    public void start() {
        int delay = customDelay == null ? config.delay() : customDelay;
        if (timer != null) {
            Log.info("Restarting scheduler with delay = " + delay + " seconds");
            timer.cancel();
        } else {
            Log.info("Starting scheduler with delay = " + delay + " seconds");
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.info("Running scheduled task");
                action.run();
            }
        };
        timer.schedule(task, delay * 1000L);

    }

    public void setCustomDelay(int value) {
        this.customDelay = value;
    }


}
