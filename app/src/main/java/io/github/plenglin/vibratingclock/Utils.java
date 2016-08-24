package io.github.plenglin.vibratingclock;

public class Utils {

    public static long getMinutesSinceEpoch() {
        return System.currentTimeMillis() / 60000;
    }

}
