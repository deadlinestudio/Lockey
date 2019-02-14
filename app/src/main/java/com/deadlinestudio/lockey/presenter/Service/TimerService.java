package com.deadlinestudio.lockey.presenter.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deadlinestudio.lockey.presenter.Item.BasicTimer;

public class TimerService extends Service {

    private BasicTimer bt;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bt = intent.getParcelableExtra("timer");
        bt.timerStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TIMER_BROAD_CAST_REQ");
        registerReceiver(br,intentFilter);

        Log.v("sertest", String.valueOf(bt.getTargetTime()));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bt.timerStop();
        unregisterReceiver(br);
        Log.v("restest", String.valueOf(bt.getTotalTime()));
    }


    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent("TIMER_BROAD_CAST_ACK");
            sendIntent.putExtra("timer",bt);
            sendBroadcast(sendIntent);
            Log.v("serREC","done");
        }
    };
}