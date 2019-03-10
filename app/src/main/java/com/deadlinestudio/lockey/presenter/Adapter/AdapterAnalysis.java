package com.deadlinestudio.lockey.presenter.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Item.ItemAnalysis;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewAlysBarChart;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewAlysLineChart;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewAlysPieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AdapterAnalysis extends BaseAdapter{
    private final int GRAPH_COUNT = 3;
    private ArrayList<ItemAnalysis> items = new ArrayList<ItemAnalysis>();
    private Context context;
    private HashMap<String, Long>[] analysisData = new HashMap[GRAPH_COUNT];

    private String[] weekdays = {"월", "화", "수", "목", "금", "토", "일"};
    private ArrayList<String>[] xaxis = new ArrayList[GRAPH_COUNT];
    private String sns;
    private User user;
    private LogfileController lfc;
    private static final String filename = "userlog.txt";

    public AdapterAnalysis(Context cont){
        this.context = cont;
        lfc = new LogfileController();

        String line = lfc.ReadLogFile(this.context, filename);
        StringTokenizer tokens = new StringTokenizer(line, ",");

        this.sns = tokens.nextToken();
    }
    public void addItem(ItemAnalysis item){
        items.add(item);
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemAnalysis item = items.get(i);

        if(i ==0){
            ItemViewAlysPieChart pie = new ItemViewAlysPieChart(context);
            pie.setTitleText(item.getTitle());
            pie.setPieChart(item.getAnalysisData());
            return pie;
        }else if(i==1){
            ItemViewAlysBarChart bar = new ItemViewAlysBarChart(context);
            bar.setTitleText(item.getTitle());
            bar.setBarChart(item.getAnalysisData(), item.getXLabels(), item.getPeriod());
            return bar;
        }
//        else if(i==2){
//            ItemViewAlysLineChart line = new ItemViewAlysLineChart(context);
//            line.setTitleText(item.getTitle());
//            line.setLineChart();
//            return line;
//        }
        return null;
    }
}
