package io.github.plenglin.vibratingclock;

public class VibrationInterval {

    /**
     * The vibration pattern to play
     */
    private int[] pattern;

    /**
     * The interval between happenings
     */
    private long interval;

    public VibrationInterval(int[] pattern, long interval) {
        this.pattern = pattern;
        this.interval = interval;
    }

    public int[] getPattern() {
        return pattern;
    }

    public long getInterval() {
        return interval;
    }

}
