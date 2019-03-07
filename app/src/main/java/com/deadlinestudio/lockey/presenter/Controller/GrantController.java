package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.deadlinestudio.lockey.presenter.Activity.BaseActivity;

public class GrantController extends BaseActivity {
    Activity act;
    public GrantController(Activity activity){
        this.act = activity;
    }
    public boolean checkGrant(){
        // GET_USAGE_STATS 권한 확인
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) act.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), act.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (act.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        Log.d("isRooting granted = " , String.valueOf(granted));

        return granted;
    }

    public void settingGrant(){
        // 권한이 없을 경우 권한 요구 페이지 이동
        Intent sintent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
        sintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        act.startActivity(sintent);
    }
}
