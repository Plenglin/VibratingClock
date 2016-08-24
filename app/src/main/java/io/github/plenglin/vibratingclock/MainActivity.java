package io.github.plenglin.vibratingclock;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private boolean clockIsActive = false;

    private SeekBar shortSlider, doubleSlider, longSlider;
    private TextView shortTextView, doubleTextView, longTextView;
    private Map<SeekBar, TextView> sliderDisplayMap = new HashMap<>();
    private Map<SeekBar, IntervalIndicatorView.Indicator> sliderClockMap = new HashMap<>();
    private Map<SeekBar, Integer> sliderColorMap = new HashMap<>();

    private IntervalIndicatorView indicatorClock;

    private View clockActiveMessage, clockConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.log(Log.INFO, "Creating MainActivity");

        clockActiveMessage = findViewById(R.id.vibrationActiveMessage);
        clockConfiguration = findViewById(R.id.configuration);

        setClockState(false);

        indicatorClock = (IntervalIndicatorView) findViewById(R.id.indicatorClock);
        assert indicatorClock != null;
        indicatorClock.setOnClickListener(this);

        shortSlider = (SeekBar) findViewById(R.id.shortSlider);
        shortTextView = (TextView) findViewById(R.id.shortTextView);
        doubleSlider = (SeekBar) findViewById(R.id.doubleSlider);
        doubleTextView = (TextView) findViewById(R.id.doubleTextView);
        longSlider = (SeekBar) findViewById(R.id.longSlider);
        longTextView = (TextView) findViewById(R.id.longTextView);

        shortSlider.setOnSeekBarChangeListener(this);
        doubleSlider.setOnSeekBarChangeListener(this);
        longSlider.setOnSeekBarChangeListener(this);

        sliderColorMap.put(shortSlider, getResources().getColor(R.color.clockRed));
        sliderColorMap.put(doubleSlider, getResources().getColor(R.color.clockYellow));
        sliderColorMap.put(longSlider, getResources().getColor(R.color.clockBlue));

        sliderDisplayMap.put(shortSlider, shortTextView);
        sliderDisplayMap.put(doubleSlider, doubleTextView);
        sliderDisplayMap.put(longSlider, longTextView);

        sliderClockMap.put(shortSlider, IntervalIndicatorView.Indicator.A);
        sliderClockMap.put(doubleSlider, IntervalIndicatorView.Indicator.B);
        sliderClockMap.put(longSlider, IntervalIndicatorView.Indicator.C);

        shortSlider.setProgress(1);
        doubleSlider.setProgress(2);
        longSlider.setProgress(3);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateIntervalIndicator(SeekBar seekBar) {
        int interval = Constants.INTERVALS[seekBar.getProgress()];
        sliderDisplayMap.get(seekBar).setText(interval > 0 ? String.format(getResources().getString(R.string.min), interval) : getResources().getString(R.string.disabled));
        indicatorClock.setInterval(sliderClockMap.get(seekBar), interval);

        int color = interval > 0 ? sliderColorMap.get(seekBar) : getResources().getColor(R.color.clockUnfilled);
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateIntervalIndicator(seekBar);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (v == indicatorClock) {
            Utils.log(Log.INFO, "indicatorClock touched");
            setClockState(!clockIsActive);
        }
    }

    public void setClockState(boolean state) {
        boolean previousState = clockIsActive;
        clockIsActive = state;
        Utils.log(Log.INFO, "setClockState() called");
        if (!previousState && clockIsActive) {
            Utils.log(Log.INFO, "Turning clock on");
            clockActiveMessage.animate().translationY(0).setDuration(300);
            clockConfiguration.animate().translationY(-1000f).setDuration(300);
            Intent intent = new Intent(Constants.START_ACTION);
            intent.setClass(this, PeriodicVibrationService.class);
            intent.putExtra("a", new VibrationInterval(Constants.SHORT_PATTERN, Constants.INTERVALS[shortSlider.getProgress()]));
            intent.putExtra("b", new VibrationInterval(Constants.DOUBLE_PATTERN, Constants.INTERVALS[doubleSlider.getProgress()]));
            intent.putExtra("c", new VibrationInterval(Constants.LONG_PATTERN, Constants.INTERVALS[longSlider.getProgress()]));
            startService(intent);
        } else if (previousState && !clockIsActive) {
            Utils.log(Log.INFO, "Turning clock off");
            clockActiveMessage.animate().translationY(-1000f).setDuration(300);
            clockConfiguration.animate().translationY(0).setDuration(300);
            Intent intent = new Intent(Constants.STOP_ACTION);
            intent.setClass(this, PeriodicVibrationService.class);
            stopService(intent);
        }
    }

}
