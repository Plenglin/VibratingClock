package io.github.plenglin.vibratingclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private boolean clockIsActive = false;

    private SeekBar shortSlider, doubleSlider, longSlider;
    private TextView shortTextView, doubleTextView, longTextView;
    private HashMap<SeekBar, TextView> sliderDisplayMap;
    private HashMap<SeekBar, IntervalIndicatorView.Indicator> sliderClockMap;

    private IntervalIndicatorView indicatorClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicatorClock = (IntervalIndicatorView) findViewById(R.id.indicatorClock);

        sliderDisplayMap = new HashMap<>();
        sliderClockMap = new HashMap<>();

        shortSlider = (SeekBar) findViewById(R.id.shortSlider);
        shortTextView = (TextView) findViewById(R.id.shortTextView);
        doubleSlider = (SeekBar) findViewById(R.id.doubleSlider);
        doubleTextView = (TextView) findViewById(R.id.doubleTextView);
        longSlider = (SeekBar) findViewById(R.id.longSlider);
        longTextView = (TextView) findViewById(R.id.longTextView);

        shortSlider.setOnSeekBarChangeListener(this);
        doubleSlider.setOnSeekBarChangeListener(this);
        longSlider.setOnSeekBarChangeListener(this);

        sliderDisplayMap.put(shortSlider, shortTextView);
        sliderDisplayMap.put(doubleSlider, doubleTextView);
        sliderDisplayMap.put(longSlider, longTextView);

        sliderClockMap.put(shortSlider, IntervalIndicatorView.Indicator.A);
        sliderClockMap.put(doubleSlider, IntervalIndicatorView.Indicator.B);
        sliderClockMap.put(longSlider, IntervalIndicatorView.Indicator.C);

        shortSlider.setProgress(0);
        doubleSlider.setProgress(1);
        longSlider.setProgress(2);

    }

    public void updateIntervalIndicator(SeekBar seekBar) {
        Log.d("VibratingClock", "Seekbar updated");
        int interval = Constants.INTERVALS[seekBar.getProgress()];
        sliderDisplayMap.get(seekBar).setText(String.format(getResources().getString(R.string.min), interval));
        indicatorClock.setInterval(sliderClockMap.get(seekBar), interval);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("VibratingClock", "Seekbar progress changed");
        updateIntervalIndicator(seekBar);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
