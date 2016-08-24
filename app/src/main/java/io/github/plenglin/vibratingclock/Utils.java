package io.github.plenglin.vibratingclock;

import android.os.Vibrator;

public class Utils {

    public static long getMinutesSinceEpoch() {
        return System.currentTimeMillis() / 60000;
    }

}
