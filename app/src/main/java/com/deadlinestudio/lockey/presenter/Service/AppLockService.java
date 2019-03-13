package com.deadlinestudio.lockey.presenter.Service;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.LockActivity;
import com.deadlinestudio.lockey.presenter.Controller.AppLockController;
import com.deadlinestudio.lockey.presenter.Controller.GrantController;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AppLockService extends Service {
    LogfileController lfc;
    AppLockController alc;
    GrantController gc;
    checkThread th;
    private Context context = null;
    final static String sfilename= "applock.txt";
    boolean grantFlag;
    boolean stopSign;

    private ArrayList<String> AppLock;

    private class checkThread extends Thread{
        public void run() {
            int num = 1;

            // GET_USAGE_STATS 권한 확인
            boolean granted = gc.checkAccessGrant();

            while(granted && stopSign) {
                if(alc.CheckRunningApp(context,AppLock)) {
                    Intent intent = new Intent(context,LockActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);       // LockActivity가 중복되지 않도록 Flag설정
                    startActivity(intent);
                    //finish();
                }else{
                    //if(!gc.checkAccessGrant())                // 권한설정 리퀘스트 추가 후 수정하기 - 지금은 무한으로 뜸
                    //    gc.settingAccessGrant();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Thread", this.getId()+" : " + num++);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.e("서비스 실행",getBaseContext().toString());
        alc = new AppLockController(getApplicationContext());
        lfc = new LogfileController();
        gc = new GrantController(getApplicationContext());
        grantFlag = false;
        stopSign = true;
        context = getApplicationContext();
        AppLock = new ArrayList<String>();
        th = new checkThread();
        //th.setDaemon(true);
        th.start();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        AppLock.clear();
        String line = lfc.ReadLogFile(context, sfilename);
        StringTokenizer tokens = new StringTokenizer(line);

        Log.d("tokens : ", "" + tokens.countTokens());

        while (tokens.hasMoreTokens()) {
            AppLock.add((tokens.nextToken(",")));
        }

        Notification notification = new Notification(R.drawable.ic_launcher, "서비스 실행됨", System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel("im_channel_id", "System", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(this, "im_channel_id")
                    .setSmallIcon(R.drawable.ic_launcher)  // the status icon
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentText("AppLockService")  // the contents of the entry
                    .build();
        }
        startForeground(0,notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        stopSign = false;
    }

}