package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterApplock;
import com.deadlinestudio.lockey.presenter.Service.AppLockService;
import com.deadlinestudio.lockey.presenter.Controller.AppLockController;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AppLockActivity extends AppCompatActivity {
    AppLockController alc;
    LogfileController lfc;
    Context cont;
    final static String sfilename = "applock.txt";

    private Button startBtn;
    private Toolbar mToolbar;
    private ListView listView;
    private ArrayList<ItemApplock> applocks;
    private Intent mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);

        alc = new AppLockController();
        lfc = new LogfileController();
        cont = getApplicationContext();

        // load applist from main activity
        applocks = alc.LoadAppList(this);
        String line = lfc.ReadLogFile(cont, sfilename);

        if((line = lfc.ReadLogFile(cont, sfilename)) != "nofile") {
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
        mToolbar  = findViewById(R.id.appListToolbar);
        mToolbar.setTitle("앱 목록");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.appLockList);


        final AdapterApplock adapterApplock = new AdapterApplock(this.getApplicationContext(),applocks);

        listView.setAdapter(adapterApplock);

        // start service by button
        startBtn = findViewById(R.id.lockStartBtn);
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                lfc.WriteLogFile(cont, sfilename, "", 2);
                for (int i = 0; i < applocks.size(); i++) {
                    if (applocks.get(i).getLockFlag() == true) {
                        lfc.WriteLogFile(cont, sfilename, applocks.get(i).getAppPackage() + ",", 1);
                    }
                }

                Intent sintent = new Intent(getApplicationContext(),AppLockService.class); // 이동할 컴포넌트
                startService(sintent); // 서비스 시작

                Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
                mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mintent);
            }
        });
        mainIntent = new Intent(getApplicationContext(),MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mainIntent);
    }
    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
        mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mintent);
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

                Intent sintent = new Intent(getApplicationContext(),AppLockService.class); // 이동할 컴포넌트
                startService(sintent); // 서비스 시작

                Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
                mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mintent);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
