package io.github.plenglin.vibratingclock;

public class Constants {

    public static final int
    SERVICE_STOP_CODE = 348927,
    SERVICE_START_CODE = 782452,
    SERVICE_DESTROY_CODE =  540892;

    public static final String
    START_ACTION = "io.github.plenglin.VCLOCK_STARTITY_START",
    STOP_ACTION = "io.github.plenglin.VCLOCK_STOPPITY_STOP";

    public static final String LOG_TAG = "VibratingClock";

    /**
     * Periodic Vibration Intervals
     */
    public static final int[] INTERVALS = {-1, 1, 5, 10, 15, 30, 60};

    /**
     * Vibration lengths
     */
    public static final int SHORT_VIBRATION = 200, DOUBLE_PAUSE = 150, LONG_VIBRATION = 1000;

    /**
     * Vibration patterns
     */
    public static final long[]
    SHORT_PATTERN = new long[] {0, SHORT_VIBRATION},
    DOUBLE_PATTERN = new long[] {0, SHORT_VIBRATION, DOUBLE_PAUSE, SHORT_VIBRATION},
    LONG_PATTERN = new long[] {0, LONG_VIBRATION};

}
