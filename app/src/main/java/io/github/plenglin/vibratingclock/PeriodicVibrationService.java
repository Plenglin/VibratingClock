package io.github.plenglin.vibratingclock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.FileDescriptor;
import java.util.Timer;

public class PeriodicVibrationService extends Service implements IBinder {

    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        //timer.schedule(new PeriodicVibrationTask(getSystemService(VIBRATOR_SERVICE)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this;
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return null;
    }

    @Override
    public boolean pingBinder() {
        return false;
    }

    @Override
    public boolean isBinderAlive() {
        return false;
    }

    @Override
    public IInterface queryLocalInterface(String descriptor) {
        return null;
    }

    @Override
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {

    }

    @Override
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {

    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code == Constants.SERVICE_STOP_CODE) {
            stopSelf();
        }
        return false;
    }

    @Override
    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {

    }

    @Override
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return false;
    }
}
