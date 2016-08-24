package io.github.plenglin.vibratingclock;

public class VibrationInterval {

    /**
     * The vibration pattern to play
     */
    private long[] pattern;

    /**
     * The interval between happenings
     */
    private long interval;

    public VibrationInterval(long[] pattern, long interval) {
        this.pattern = pattern;
        this.interval = interval;
    }

    public long[] getPattern() {
        return pattern;
    }

    public long getInterval() {
        return interval;
    }

}
