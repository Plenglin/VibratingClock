package io.github.plenglin.vibratingclock;

import android.os.Vibrator;

import java.util.List;
import java.util.TimerTask;

public class PeriodicVibrationTask extends TimerTask {

    private List<VibrationInterval> vibrationList;
    private Vibrator vibrator;

    public PeriodicVibrationTask(Vibrator vibrator, List<VibrationInterval> vibrationIntervals) {
        this.vibrationList = vibrationIntervals;
        this.vibrator = vibrator;
    }

    @Override
    public void run() {
        long minutes = Utils.getMinutesSinceEpoch();
        for (VibrationInterval v: vibrationList) {
            if (minutes % v.getInterval() == 0) {
                vibrator.vibrate(v.getPattern(), -1);
                return;
            }
        }
    }

}
