package com.deadlinestudio.lockey.presenter.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deadlinestudio.lockey.presenter.Item.BasicTimer;

public class TimerService extends Service {
    private BasicTimer bt = BasicTimer.getInstance();

    IMyTimerService.Stub binder = new IMyTimerService.Stub() {
        @Override
        public long getTempTargetTime() throws RemoteException {
            return bt.getTempTarget();
        }
        @Override
        public long getTargetTime() throws RemoteException {
            return bt.getTargetTime();
        }
        @Override
        public long getTotalTime() throws RemoteException {
            return bt.getTotalTime();
        }
    };

    public TimerService() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Thread timer = new Thread(bt);
        timer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("sertest", String.valueOf(bt.getTargetTime()));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bt.timerStop();
        Log.v("restest", String.valueOf(bt.getTotalTime()));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bt.timerStop();
        return super.onUnbind(intent);
    }

}