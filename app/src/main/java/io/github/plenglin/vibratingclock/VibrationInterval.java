package io.github.plenglin.vibratingclock;

import java.io.Serializable;

public class VibrationInterval implements Serializable {

    private static final long serialVersionUID = 543789341L;

    /**
     * The vibration pattern to play
     */
    private long[] pattern;

    /**
     * The interval between happenings
     */
    private long interval;

    public VibrationInterval(long[] pattern, long interval) {
        assert interval > 0;
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
