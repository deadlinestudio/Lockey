package com.deadlinestudio.lockey.presenter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterAnalysis;
import com.deadlinestudio.lockey.presenter.Item.ItemAnalysis;

import java.util.HashMap;

public class FragmentGraphYear extends Fragment {
    private ListView listView;
    private MainActivity mainActivity;

    private String[] xlabels = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
            "10월", "11월", "12월"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_graph, container,false);
        mainActivity = (MainActivity) this.getActivity();

        listView = (ListView) rootView.findViewById(R.id.analysisList);
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.sr_layout);

        setAdapter();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Log.v("created","a");
        return rootView;
    }
    public void setAdapter(){
        AdapterAnalysis adapter = new AdapterAnalysis(mainActivity.getApplicationContext());
        adapter.addItem(new ItemAnalysis(mainActivity.getBaseContext(),"집중 분포", ItemAnalysis.YEAR, ItemAnalysis.PIE_GRAPH));
        adapter.addItem(new ItemAnalysis(mainActivity.getBaseContext(),"기록", xlabels, ItemAnalysis.YEAR, ItemAnalysis.BAR_GRAPH));
// adapter.addItem(new ItemAnalysis("토탈"));
        listView.setAdapter(adapter);
    }

}
