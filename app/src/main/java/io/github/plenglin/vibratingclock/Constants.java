package io.github.plenglin.vibratingclock;

public class Constants {

    public static final int SERVICE_STOP_CODE = 348927;

    /**
     * Periodic Vibration Intervals
     */
    public static final int[] INTERVALS = {1, 5, 10, 15, 30, 60};

    /**
     * Vibration lengths
     */
    public static final int SHORT_VIBRATION = 200, DOUBLE_PAUSE = 150, LONG_VIBRATION = 1000;

    /**
     * Vibration patterns
     */
    public static final int[]
    SHORT_PATTERN = new int[] {0, SHORT_VIBRATION},
    DOUBLE_PATTERN = new int[] {0, SHORT_VIBRATION, DOUBLE_PAUSE, SHORT_VIBRATION},
    LONG_PATTERN = new int[] {0, LONG_VIBRATION};

}
