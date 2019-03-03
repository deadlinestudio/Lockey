package com.deadlinestudio.lockey.presenter.Fragment;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterApplock;
import com.deadlinestudio.lockey.presenter.Controller.AppLockController;
import com.deadlinestudio.lockey.presenter.Controller.AppUsageController;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.Service.AppLockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FragmentApplock extends Fragment{
    AppLockController alc;
    LogfileController lfc;
    Context cont;
    final static String sfilename = "applock.txt";

    private Button startBtn, selectAllBtn, selectNoneBtn;
    private Toolbar mToolbar;
    private ListView listView;
    private ArrayList<ItemApplock> applocks;
    private List<Pair<ItemApplock, Long>> appRank;
    public MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Set up
        final ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_applock, container,false);

        mainActivity = (MainActivity) this.getActivity();
        mToolbar  = rootView.findViewById(R.id.appListToolbar);
        listView = rootView.findViewById(R.id.appLockList);
        startBtn = rootView.findViewById(R.id.lockStartBtn);
        selectAllBtn = rootView.findViewById(R.id.appSelectAllBtn);
        selectNoneBtn = rootView.findViewById(R.id.appSelectNoneBtn);

        // GET_USAGE_STATS 권한 확인
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) mainActivity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), mainActivity.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (mainActivity.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        Log.d("isRooting granted = " , String.valueOf(granted));

        if (granted == false)
        {
            // 권한이 없을 경우 권한 요구 페이지 이동
            Intent sintent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            sintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mainActivity.startActivity(sintent);
        }


        alc = new AppLockController();
        lfc = new LogfileController();
        cont = this.getContext();

        // load applist from main activity
        applocks = alc.LoadAppList(this.getActivity());

        String line;
        if((line = lfc.ReadLogFile(cont, sfilename)) != "nofile") {         // 앱 잠금 리스트 확인 후 flag 업데이트
            StringTokenizer tokens = new StringTokenizer(line);
            while(tokens.hasMoreTokens()) {
                String temp = tokens.nextToken(",");
                for (int i = 0; i < applocks.size(); i++) {
                    if (applocks.get(i).getAppPackage().equals(temp)) {
                        applocks.get(i).setLockFlag(true);
                    }
                }
            }
        }

        mToolbar  = rootView.findViewById(R.id.appListToolbar);

        final AdapterApplock adapterApplock = new AdapterApplock(this.getActivity().getApplicationContext(),applocks);

        listView.setAdapter(adapterApplock);

        // start service by button
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                lfc.WriteLogFile(cont, sfilename, "", 2);
                for (int i = 0; i < applocks.size(); i++) {
                    if (applocks.get(i).getLockFlag()) {
                        lfc.WriteLogFile(cont, sfilename, applocks.get(i).getAppPackage() + ",", 1);
                    }
                }
                String line = lfc.ReadLogFile(cont, sfilename);
                if(line.equals("")) {
                    Intent sintent = new Intent(cont, AppLockService.class); // 이동할 컴포넌트
                    getActivity().stopService(sintent); // 서비스 종료
                    String toastMsg = "앱 잠금 서비스를 종료합니다.";
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }else {
                    Intent sintent = new Intent(cont, AppLockService.class); // 이동할 컴포넌트
                    getActivity().startService(sintent); // 서비스 시작
                    String toastMsg = "앱 잠금 목록을 업데이트 했습니다.";
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }

                // get App Usage Time
                AppUsageController auc = new AppUsageController(applocks);
                auc.getAppUsageTime(mainActivity);
                appRank = auc.getAppRank();
                for(Pair<ItemApplock, Long> e : appRank){
                    long totalTime = e.second;
                    int hours   = (int) ((totalTime / (1000*60*60)) % 24);
                    int minutes = (int) ((totalTime / (1000*60)) % 60);
                    int seconds = (int) (totalTime / 1000) % 60 ;
                    Log.e("", e.first.getAppName() + " -----> " + hours + "시간 "+minutes+"분 "+seconds+"초");
                }

            }
        });

        // select all apps
        selectAllBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < applocks.size(); i++) {
                    applocks.get(i).setLockFlag(true);
                    listView.setAdapter(adapterApplock);
                }
            }
        });
        // select all apps
        selectNoneBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < applocks.size(); i++) {
                    applocks.get(i).setLockFlag(false);
                    listView.setAdapter(adapterApplock);
                }
            }
        });
        return rootView;
    }
/*
    @Override
    public void onBackPressed() {
        super.getActivity().onBackPressed();
        Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
        mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mintent);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                lfc.WriteLogFile(cont, sfilename, "", 2);
                for (int i = 0; i < applocks.size(); i++) {
                    if (applocks.get(i).getLockFlag() == true) {
                        lfc.WriteLogFile(cont, sfilename, applocks.get(i).getAppPackage() + ",", 1);
                    }
                }
                Intent sintent = new Intent(cont,AppLockService.class); // 이동할 컴포넌트
                getActivity().startService(sintent); // 서비스 시작

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
