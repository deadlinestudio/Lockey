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
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AppLockService extends Service {

    LogfileController lfc;
    AppLockController alc;
    checkThread th;
    private Context context = null;
    final static String sfilename= "applock.txt";
    boolean grantFlag;

    private ArrayList<String> AppLock;

    private class checkThread extends Thread{
        public void run() {
            int num = 1;

            // GET_USAGE_STATS 권한 확인
            boolean granted = false;
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), context.getPackageName());

            if (mode == AppOpsManager.MODE_DEFAULT) {
                granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            } else {
                granted = (mode == AppOpsManager.MODE_ALLOWED);
            }

            while(granted && !isInterrupted()) {
                if(alc.CheckRunningApp(context,AppLock)) {
                    Intent intent = new Intent(context,LockActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    //finish();
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

        alc = new AppLockController();
        lfc = new LogfileController();
        grantFlag = false;
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
        startForeground(1,notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
    }

}