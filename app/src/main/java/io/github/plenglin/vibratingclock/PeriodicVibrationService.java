package io.github.plenglin.vibratingclock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class PeriodicVibrationService extends Service {

    private Timer timer;
    private List<VibrationInterval> intervals = new ArrayList<>();
    private boolean isActive;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.log(Log.INFO, "Service created");
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Utils.log(Log.INFO, "Service started");

        intervals.add((VibrationInterval) intent.getSerializableExtra("a"));
        intervals.add((VibrationInterval) intent.getSerializableExtra("b"));
        intervals.add((VibrationInterval) intent.getSerializableExtra("c"));
        setActiveState(true);
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.log(Log.INFO, "Service destroyed");
        setActiveState(false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setActiveState(boolean state) {
        this.isActive = state;
        if (isActive) {
            long nextMinuteMillis = 60000 * (Utils.getMinutesSinceEpoch() + 1);
            timer.schedule(new PeriodicVibrationTask((Vibrator) getSystemService(VIBRATOR_SERVICE), intervals), nextMinuteMillis - System.currentTimeMillis(), 60000);
        } else {
            timer.cancel();
        }
    }

}
