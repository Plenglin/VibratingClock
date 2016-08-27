package io.github.plenglin.vibratingclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class PeriodicVibrationService extends Service {

    private Timer timer;
    private List<VibrationInterval> intervals = new ArrayList<>();
    private boolean isActive;
    private Notification notification;
    private IBinder binder;
    private NotificationManager manager;

    private NotificationManager getNotificationManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

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

        for (String c: new String[] {"a", "b", "c"}) {
            try {
                intervals.add((VibrationInterval) intent.getSerializableExtra(c));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        Intent resultIntent = new Intent(this, MainActivity.class)
                .setAction(Constants.STOP_ACTION)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_schedule_white_24dp)
                .setContentTitle("Vibrating Clock")
                .setContentText("Tap to stop")
                .setContentIntent(pendingIntent);
        notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        getNotificationManager().notify(Constants.NOTIFICATION_ID, notification);

        setActiveState(true);
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.log(Log.INFO, "Service destroyed");
        getNotificationManager().cancel(Constants.NOTIFICATION_ID);
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
