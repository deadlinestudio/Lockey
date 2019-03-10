package com.deadlinestudio.lockey.presenter.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterHelp;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentHelpFive;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentHelpFour;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentHelpOne;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentHelpThree;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentHelpTwo;

public class HelpActivity extends AppCompatActivity {
    int MAX_PAGE=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ViewPager viewPager=(ViewPager)findViewById(R.id.helpPager);
        viewPager.setOffscreenPageLimit(MAX_PAGE);
        AdapterHelp adapterHelp = new AdapterHelp(getSupportFragmentManager());

        FragmentHelpOne frag1 = new FragmentHelpOne();
        FragmentHelpTwo frag2 = new FragmentHelpTwo();
        FragmentHelpThree frag3 = new FragmentHelpThree();
        FragmentHelpFour frag4 = new FragmentHelpFour();
        FragmentHelpFive frag5 = new FragmentHelpFive();

        adapterHelp.addItem(frag1);
        adapterHelp.addItem(frag2);
        adapterHelp.addItem(frag3);
        adapterHelp.addItem(frag4);
        adapterHelp.addItem(frag5);

        viewPager.setAdapter(adapterHelp);
        viewPager.setCurrentItem(0);
    }
}
