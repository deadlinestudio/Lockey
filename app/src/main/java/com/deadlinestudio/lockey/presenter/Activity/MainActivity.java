package com.deadlinestudio.lockey.presenter.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.model.User;
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
    private static final String filename = "userlog.txt";

    private static String sns = "";

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

        User user = User.getInstance();
        if(this.getSns().equals("4") == false) {
            user.setId(tokens.nextToken());
            user.setNickname(tokens.nextToken());
            user.setAge(Integer.parseInt(tokens.nextToken()));
            user.setJob(tokens.nextToken());
        }
        Log.e("logfile in mainactivity", user.getNickname()+user.getJob());

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

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false) {
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();

            // 앱 최초 실행시 하고 싶은 작업
            Intent intent = new Intent(getBaseContext(),HelpActivity.class);
            startActivity(intent);
        }

        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment tempFrag = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        pager.setCurrentItem(0,false);
                        break;
                    case R.id.navigation_applist:
                        pager.setCurrentItem(1,false);
                        break;
                    case R.id.navigation_graph:
                        pager.setCurrentItem(2,false);
                        break;
                    case R.id.navigation_setting:
                        pager.setCurrentItem(3,false);
                        break;
                }
                return true;
            }
        });

    }
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

    public static String getSns() { return sns; }
    public static String getFilename() { return filename; }

    public void setSns(String sns) { this.sns = sns; }

}
