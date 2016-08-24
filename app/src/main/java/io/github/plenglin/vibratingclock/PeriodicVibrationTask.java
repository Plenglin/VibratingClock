package io.github.plenglin.vibratingclock;

import android.os.Vibrator;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimerTask;

public class PeriodicVibrationTask extends TimerTask {

    private List<VibrationInterval> vibrationList;
    private Vibrator vibrator;

    public static final Comparator<VibrationInterval> vibrationIntervalComparator = new Comparator<VibrationInterval>() {
        @Override
        public int compare(VibrationInterval lhs, VibrationInterval rhs) {
            return (int) (rhs.getInterval() - lhs.getInterval());
        }
    };

    public PeriodicVibrationTask(Vibrator vibrator, List<VibrationInterval> vibrationIntervals) {
        this.vibrationList = vibrationIntervals;
        this.vibrator = vibrator;
        Collections.sort(vibrationList, vibrationIntervalComparator);
    }

    @Override
    public void run() {
        Utils.log(Log.INFO, "PeriodicVibrationTask");
        long minutes = Utils.getMinutesSinceEpoch();
        for (VibrationInterval v: vibrationList) {
            if (minutes % v.getInterval() == 0) {
                vibrator.vibrate(v.getPattern(), -1);
                return;
            }
        }
    }

}
