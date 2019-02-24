package com.deadlinestudio.lockey.presenter.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class AdapterGraph extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> items = new ArrayList<Fragment>();

    public AdapterGraph(FragmentManager fm) {
        super(fm);
    }
    public void addItem(Fragment item) {
        items.add(item);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
