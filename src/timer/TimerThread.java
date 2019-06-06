package timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimerThread {
    private Timer timer = new Timer();

    private TimerData timerData;
    private TimerTask timerTask;
    private long time;
    private long period = 5;

    public TimerThread(TimerData timerData) {
        this.timerData = timerData;
    }

    private void createTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time += period;
                timerData.setTime(time);
            }
        };
    }

    public void start() {
        if(timerTask == null) {
            createTimerTask();
            timer.scheduleAtFixedRate(timerTask, period, period);
        }
    }

    public void stop() {
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void reset() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time = 0;
                timerData.setTime(time);
            }
        }, 0);
    }

    public boolean isActive() {
        return timerTask != null;
    }
}