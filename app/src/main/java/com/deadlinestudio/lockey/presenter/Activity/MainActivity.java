package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Adapter.MainPagerAdapter;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentAnalysis;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentApplock;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentSetting;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentTimer;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private LogfileController lfc;
    private Context cont;
    final String filename = "userlog.txt";

    private String sns = "";
    private static String id = "";
    private String nickname = "";
    private int age = 0;
    private String job = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* variable for log files(login process)*/
        lfc = new LogfileController();
        cont = getApplicationContext();
        String line = lfc.ReadLogFile(cont, filename);
        StringTokenizer tokens = new StringTokenizer(line, ",");

        this.setSns(tokens.nextToken());
        Log.e("SNS in Login", this.getSns());

        if(this.getSns().equals("4") == false) {
            this.setId(tokens.nextToken());

            this.setNickname(tokens.nextToken());
            this.setAge(Integer.parseInt(tokens.nextToken()));
            this.setJob(tokens.nextToken());
        }
        Log.e("logfile in mainactivity", this.getNickname()+this.getJob());

        /* bottom navigation*/
        BottomNavigationView btmNav = (BottomNavigationView) findViewById(R.id.bottomMainNav);
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);

        final MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());

        final FragmentTimer fragmentTimer = new FragmentTimer();
        final FragmentApplock fragmentApplock = new FragmentApplock();
        final FragmentAnalysis fragmentAnalysis = new FragmentAnalysis();
        final FragmentSetting fragmentSetting = new FragmentSetting();

        adapter.addItem(fragmentTimer);
        adapter.addItem(fragmentApplock);
        adapter.addItem(fragmentAnalysis);
        adapter.addItem(fragmentSetting);

        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        //loadFragment(fragmentTimer);

        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment tempFrag = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        pager.setCurrentItem(0,false);
                        break;//return loadFragment(fragmentTimer);
                    case R.id.navigation_applist:
                        pager.setCurrentItem(1,false);
                        break;
                        //return loadFragment(fragmentApplock);
                    case R.id.navigation_graph:
                        pager.setCurrentItem(2,false);
                        break;//return loadFragment(fragmentAnalysis);
                    case R.id.navigation_setting:
                        pager.setCurrentItem(3,false);
                        break;//return loadFragment(fragmentSetting);
                }
                return true;
            }
        });

    }
    /*
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }*/
    @Override
    public void onBackPressed() {
        Log.e("main back", "start!");
        //moveTaskToBack(true);
        Intent intent = new Intent(Intent.ACTION_MAIN);     // Home으로 이동
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * @brief getter and setters
     * **/
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public static String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSns() { return sns; }

    public void setSns(String sns) { this.sns = sns; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public String getJob() { return job; }

    public void setJob(String job) { this.job = job; }
}
