package io.github.plenglin.vibratingclock;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private boolean clockActiveState = false;
    private boolean clockInterfaceState = false;

    private IBinder binder;

    private SeekBar shortSlider, doubleSlider, longSlider;
    private TextView shortTextView, doubleTextView, longTextView;
    private Map<SeekBar, TextView> sliderDisplayMap = new HashMap<>();
    private Map<SeekBar, IntervalIndicatorView.Indicator> sliderClockMap = new HashMap<>();
    private Map<SeekBar, Integer> sliderColorMap = new HashMap<>();

    private IntervalIndicatorView indicatorClock;

    private View clockActiveMessage, clockConfiguration;

    private NotificationManager manager;

    public NotificationManager getNotificationManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.log(Log.INFO, "Creating MainActivity");

        clockActiveMessage = findViewById(R.id.vibrationActiveMessage);
        clockConfiguration = findViewById(R.id.configuration);

        setClockInterfaceState(false);

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
    protected void onStart() {
        super.onStart();
        Utils.log(Log.DEBUG, getIntent().getAction());
        if (getIntent().getAction().equals(Constants.STOP_ACTION)) {
            setClockState(false);
        } else {
            setClockState(clockActiveState);
        }
        Utils.log(Log.INFO, "Starting MainActivity");
        Utils.log(Log.DEBUG, clockActiveState);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Utils.log(Log.INFO, "Received new intent");
        Utils.log(Log.DEBUG, intent.getAction());
        if (getIntent().getAction().equals(Constants.STOP_ACTION)) {
            setClockState(false);
        } else {
            setClockState(clockActiveState);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.log(Log.INFO, "Destroying MainActivity");
        setClockState(false);
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
            setClockState(!clockActiveState);
        }
    }

    public void setClockState(boolean state) {
        boolean previousState = clockActiveState;
        clockActiveState = state;
        Utils.log(Log.INFO, "setClockState() called");
        if (!previousState && clockActiveState) {
            Utils.log(Log.INFO, "Turning clock on");
            Intent intent = new Intent(Constants.START_ACTION);
            intent.setClass(this, PeriodicVibrationService.class);
            intent.putExtra("a", new VibrationInterval(Constants.SHORT_PATTERN, Constants.INTERVALS[shortSlider.getProgress()]));
            intent.putExtra("b", new VibrationInterval(Constants.DOUBLE_PATTERN, Constants.INTERVALS[doubleSlider.getProgress()]));
            intent.putExtra("c", new VibrationInterval(Constants.LONG_PATTERN, Constants.INTERVALS[longSlider.getProgress()]));
            startService(intent);
        } else if (previousState && !clockActiveState) {
            Utils.log(Log.INFO, "Turning clock off");
            Intent intent = new Intent(Constants.STOP_ACTION);
            intent.setClass(this, PeriodicVibrationService.class);
            stopService(intent);
        }
        setClockInterfaceState(state);
    }

    public void setClockInterfaceState(boolean state) {
        boolean previousState = clockInterfaceState;
        clockInterfaceState = state;
        Utils.log(Log.INFO, "setClockInterfaceState() called");
        if (!previousState && clockInterfaceState) {
            Utils.log(Log.INFO, "Turning clock interface on");
            clockActiveMessage.animate().translationY(0).setDuration(300);
            clockConfiguration.animate().translationY(-1000f).setDuration(300);
        } else if (previousState && !clockInterfaceState) {
            Utils.log(Log.INFO, "Turning clock interface off");
            clockActiveMessage.animate().translationY(-1000f).setDuration(300);
            clockConfiguration.animate().translationY(0).setDuration(300);
        }
    }

}
