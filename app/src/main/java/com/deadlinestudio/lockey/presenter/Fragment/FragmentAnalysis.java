package com.deadlinestudio.lockey.presenter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterGraph;

public class FragmentAnalysis extends Fragment{

    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout graphTabLayout;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_analysis, container,false);
        mainActivity = (MainActivity) this.getActivity();

        // set up Toolbars
        mToolbar  = rootView.findViewById(R.id.analysisToolbar);

        // graph tab fragments
        graphTabLayout = (TabLayout)rootView.findViewById(R.id.analysisTabLayout);
        viewPager =(ViewPager)rootView.findViewById(R.id.graph_pager);
        viewPager.setOffscreenPageLimit(4);

        // Tab pager Adapter
        AdapterGraph pagerAdapter = new AdapterGraph(mainActivity.getSupportFragmentManager());

        FragmentGraphDay dailyTab = new FragmentGraphDay();
        FragmentGraphWeek weeklyTab = new FragmentGraphWeek();
        FragmentGraphMonth monthlyTab = new FragmentGraphMonth();
        FragmentGraphTotal totalTab = new FragmentGraphTotal();

        pagerAdapter.addItem(dailyTab);
        pagerAdapter.addItem(weeklyTab);
        pagerAdapter.addItem(monthlyTab);
        pagerAdapter.addItem(totalTab);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(graphTabLayout));

        graphTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
                Log.v("pos: ", String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }
}
