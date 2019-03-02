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

public class FragmentGraphMonth extends Fragment {
    private ListView listView;
    private MainActivity mainActivity;



    private int mode;

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
        adapter.addItem(new ItemAnalysis("집중 분포"));
        adapter.addItem(new ItemAnalysis("기록",2));
        adapter.addItem(new ItemAnalysis("먼뜨",3));
        listView.setAdapter(adapter);
    }

}
