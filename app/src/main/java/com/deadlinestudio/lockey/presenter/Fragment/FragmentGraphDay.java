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

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterAnalysis;
import com.deadlinestudio.lockey.presenter.Item.ItemAnalysis;

public class FragmentGraphDay extends Fragment {
    private ListView listView;
    private MainActivity mainActivity;

    private String[] xlabels = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                                "12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
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
        adapter.addItem(new ItemAnalysis(mainActivity.getBaseContext(),"집중 분포", ItemAnalysis.DAY, ItemAnalysis.PIE_GRAPH));
        adapter.addItem(new ItemAnalysis(mainActivity.getBaseContext(),"기록", xlabels, ItemAnalysis.DAY, ItemAnalysis.BAR_GRAPH));
// adapter.addItem(new ItemAnalysis("데이",3));
        listView.setAdapter(adapter);
    }

}
