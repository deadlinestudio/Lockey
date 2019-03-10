package com.deadlinestudio.lockey.presenter.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class AdapterHelp extends FragmentStatePagerAdapter {
    ArrayList<Fragment> items = new ArrayList<Fragment>();

    public AdapterHelp(FragmentManager fm) {
        super(fm);
    }
    public void addItem(Fragment f){
        items.add(f);
    }
    @Override
    public Fragment getItem(int i) {
        return items.get(i);
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "page: "+position;
    }
}
