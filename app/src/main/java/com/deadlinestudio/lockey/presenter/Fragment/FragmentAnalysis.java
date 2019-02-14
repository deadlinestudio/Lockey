package com.deadlinestudio.lockey.presenter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterAnalysis;
import com.deadlinestudio.lockey.presenter.Item.ItemAnalysis;

public class FragmentAnalysis extends Fragment{
    private ListView listView;
    private Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_analysis, container,false);
        listView = (ListView) rootView.findViewById(R.id.analysisList);
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.sr_layout);
        mToolbar  = rootView.findViewById(R.id.analysisToolbar);
        mToolbar.setTitle("분석");
        setAdapter();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               setAdapter();
               swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
    public void setAdapter(){
        AdapterAnalysis adapter = new AdapterAnalysis(getActivity().getApplicationContext());
        adapter.addItem(new ItemAnalysis("일일 집중 분석", "하루 동안의 집중력을 알려줍니다"));
        adapter.addItem(new ItemAnalysis("주간 집중 분석", "일주일 동안의 집중력을 알려줍니다"));
        adapter.addItem(new ItemAnalysis("월간 집중 분석", "한달 동안의 집중력을 알려줍니다"));
        listView.setAdapter(adapter);
    }
}
