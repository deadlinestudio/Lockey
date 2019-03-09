package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.deadlinestudio.lockey.presenter.Activity.BaseActivity;

public class GrantController extends BaseActivity {
    Context cont;
    public GrantController(Context cont){
        this.cont = cont;
    }
    public boolean checkAccessGrant(){
        // GET_USAGE_STATS 권한 확인
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) cont.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), cont.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (cont.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        Log.d("isRooting granted = " , String.valueOf(granted));

        return granted;
    }

    public void settingAccessGrant(){
        // 권한이 없을 경우 권한 요구 페이지 이동
        String toastMsg = "Lockey를 선택 후 사용 추적 허용해주세요.";
        final Toast toast = Toast.makeText(cont, toastMsg, Toast.LENGTH_LONG);
        new CountDownTimer(6000, 1000)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();
        Intent sintent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
        sintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        cont.startActivity(sintent);
    }

    public boolean checkAlertGrant(NotificationManager mNotificationManager){
        // Notification Policy 권한 확인
        if (!mNotificationManager.isNotificationPolicyAccessGranted())
            return false;
        else
            return true;
    }

    public void settingAlertGrant(){
        // 권한이 없을 경우 권한 요구 페이지 이동
        String toastMsg = "Lockey를 선택 후 방해 금지 모드를 허용해주세요.";
        final Toast toast = Toast.makeText(cont, toastMsg, Toast.LENGTH_LONG);
        new CountDownTimer(6000, 1000)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        cont.startActivity(intent);
    }
}
