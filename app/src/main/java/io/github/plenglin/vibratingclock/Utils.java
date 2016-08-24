package io.github.plenglin.vibratingclock;

import android.util.Log;

public class Utils {

    public static long getMinutesSinceEpoch() {
        return System.currentTimeMillis() / 60000;
    }

    public static void log(int level, Object object) {
        Log.println(level, Constants.LOG_TAG, object.toString());
    }

}
