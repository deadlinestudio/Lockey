package com.deadlinestudio.lockey.presenter.Fragment;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterApplock;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterMostApps;
import com.deadlinestudio.lockey.presenter.Controller.AppLockController;
import com.deadlinestudio.lockey.presenter.Controller.AppUsageController;
import com.deadlinestudio.lockey.presenter.Controller.CaulyAdController;
import com.deadlinestudio.lockey.presenter.Controller.GrantController;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.Item.ItemMostApps;
import com.deadlinestudio.lockey.presenter.Service.AppLockService;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FragmentApplock extends Fragment  {
    AppLockController alc;
    LogfileController lfc;
    CaulyAdController cac;
    GrantController gc;
    Context cont;
    final static String sfilename = "applock.txt";

    private Button startBtn;
    private LinearLayout selectAllBtn, selectNoneBtn;
    private Toolbar mToolbar;
    private RecyclerView mostAppListView,listView;
    private ArrayList<ItemApplock> applocks;
    private ArrayList<ItemMostApps> mostApps;
    private List<Pair<ItemApplock, Long>> appRank;
    public MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Set up
        final ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_applock, container,false);

        mainActivity = (MainActivity) this.getActivity();
        mToolbar  = rootView.findViewById(R.id.appListToolbar);
        mostAppListView = rootView.findViewById(R.id.mostAppList);
        listView = rootView.findViewById(R.id.appLockList);
        startBtn = rootView.findViewById(R.id.lockStartBtn);
        selectAllBtn = rootView.findViewById(R.id.appSelectAllBtn);
        selectNoneBtn = rootView.findViewById(R.id.appSelectNoneBtn);

        alc = new AppLockController();
        gc = new GrantController(mainActivity);
        lfc = new LogfileController();
        cac = new CaulyAdController(mainActivity);
        cac.makeInterstitialAd();
        cont = this.getContext();

        // 사용자 접근 허용 권한 확인 및 설정
        if(!gc.checkGrant()) {
            String toastMsg = "Lockey를 선택 후 사용 추적 허용을 활성화 해주세요.";
            final Toast toast = Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG);
            new CountDownTimer(6000, 1000)
            {
                public void onTick(long millisUntilFinished) {toast.show();}
                public void onFinish() {toast.show();}
            }.start();
            gc.settingGrant();
        }

        // load applist from main activity
        applocks = alc.LoadAppList(this.getActivity());
        mostApps = new ArrayList<ItemMostApps>();
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

        // get App Usage Time
        AppUsageController auc = new AppUsageController(applocks);
        auc.getAppUsageTime(mainActivity);
        appRank = auc.getAppRank();
        int appranks = 0;
        for(Pair<ItemApplock, Long> e : appRank){
            long totalTime = e.second;
            int hours   = (int) ((totalTime / (1000*60*60)) % 24);
            int minutes = (int) ((totalTime / (1000*60)) % 60);
            int seconds = (int) (totalTime / 1000) % 60 ;
            String tempTime = String.valueOf(hours)+"시간 "+String.valueOf(minutes)+"분";

            ItemMostApps tempApp = new ItemMostApps(e.first.getAppName(), tempTime, e.first.getAppIcon());
            mostApps.add(tempApp);

            Log.v("", e.first.getAppName() + " -----> " + hours + "시간 "+minutes+"분 "+seconds+"초");
        }

        final AdapterApplock adapterApplock = new AdapterApplock(this.getActivity().getApplicationContext(),applocks);
        final AdapterMostApps adapterMostApps = new AdapterMostApps(this.getActivity().getApplicationContext(),mostApps);
        listView.setAdapter(adapterApplock);
        mostAppListView.setAdapter(adapterMostApps);

        // start service by button
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gc.checkGrant()) {
                    lfc.WriteLogFile(cont, sfilename, "", 2);
                    for (int i = 0; i < applocks.size(); i++) {
                        if (applocks.get(i).getLockFlag()) {
                            lfc.WriteLogFile(cont, sfilename, applocks.get(i).getAppPackage() + ",", 1);
                        }
                    }
                    String line = lfc.ReadLogFile(cont, sfilename);
                    if (line.equals("")) {
                        Intent sintent = new Intent(cont, AppLockService.class); // 이동할 컴포넌트
                        getActivity().stopService(sintent); // 서비스 종료
                        String toastMsg = "앱 잠금 서비스를 종료합니다.";
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent sintent = new Intent(cont, AppLockService.class); // 이동할 컴포넌트
                        getActivity().startService(sintent); // 서비스 시작
                        String toastMsg = "앱 잠금 목록을 업데이트 했습니다.";
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    }
                    cac.runInterstitialAd();
                }else{
                    String toastMsg = "Lockey를 선택 후 사용 추적 허용을 활성화 해주세요.";
                    final Toast toast = Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG);
                    new CountDownTimer(6000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.show();}
                    }.start();
                    gc.settingGrant();
                }
            }
        });

        // select all apps
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ItemApplock ia : applocks) {
                    ia.setLockFlag(true);
                }
                listView.setAdapter(adapterApplock);
            }
        });
        // select all apps
        selectNoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ItemApplock ia : applocks) {
                    ia.setLockFlag(false);
                }
                listView.setAdapter(adapterApplock);
            }
        });
        return rootView;
    }

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
