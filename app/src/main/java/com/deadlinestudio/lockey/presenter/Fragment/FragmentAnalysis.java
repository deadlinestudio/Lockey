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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterGraph;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FragmentAnalysis extends Fragment{
    private static TextView nickText;
    private TextView totalText;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout graphTabLayout;
    private MainActivity mainActivity;
    private FrameLayout noMemFrame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_analysis, container,false);
        mainActivity = (MainActivity) this.getActivity();
        nickText = rootView.findViewById(R.id.profileName);
        String nick = User.getInstance().getNickname();
        nickText.setText((nick.equals("")) ? "비회원":nick);
        // set up Toolbars
        mToolbar  = rootView.findViewById(R.id.analysisToolbar);
        noMemFrame = rootView.findViewById(R.id.noMemberFrame);
        totalText = rootView.findViewById(R.id.userTotalTimeText);
        setTotalText();

        if(MainActivity.getSns().equals("4")) {
            noMemFrame.setVisibility(View.VISIBLE);
        }

        // graph tab fragments
        graphTabLayout = (TabLayout)rootView.findViewById(R.id.analysisTabLayout);
        viewPager =(ViewPager)rootView.findViewById(R.id.graph_pager);
        viewPager.setOffscreenPageLimit(3);

        // Tab pager Adapter
        AdapterGraph pagerAdapter = new AdapterGraph(mainActivity.getSupportFragmentManager());

        // FragmentGraphDay dailyTab = new FragmentGraphDay();
        FragmentGraphWeek weeklyTab = new FragmentGraphWeek();
        FragmentGraphMonth monthlyTab = new FragmentGraphMonth();
        FragmentGraphYear yearTab = new FragmentGraphYear();

        // pagerAdapter.addItem(dailyTab);
        pagerAdapter.addItem(weeklyTab);
        pagerAdapter.addItem(monthlyTab);
        pagerAdapter.addItem(yearTab);

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

    private void setTotalText() {
        try {
            NetworkTask asyncNetwork = new NetworkTask(mainActivity.getBaseContext(), "/get-time", null);
            asyncNetwork.execute().get(1000, TimeUnit.MILLISECONDS);
            HashMap<String, Long> analysisData = asyncNetwork.getAnalysisData();
            if(analysisData != null) {
                totalText.setText(Long.toString(analysisData.get("total") / 60) + " 시간");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNickText(String nickname) {
        nickText.setText(nickname);
    }
}
