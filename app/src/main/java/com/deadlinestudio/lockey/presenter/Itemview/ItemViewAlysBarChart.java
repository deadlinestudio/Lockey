package com.deadlinestudio.lockey.presenter.Itemview;


import android.graphics.Color;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemViewAlysBarChart extends LinearLayout {

    private TextView titleText, subText, chartAvgVals, charAvgTimes;
    private PieChart chart;
    private HashMap<String, Long> analysisData;
    private ArrayList<String> xaxis;

    public ItemViewAlysBarChart(Context context) {
        super(context);
        init(context);
    }

    public ItemViewAlysBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_alys_barchart, this, true);

        titleText = findViewById(R.id.analyTitleText);
        chartAvgVals = findViewById(R.id.chartAvgVals);
        charAvgTimes = findViewById(R.id.chartAvgTimes);
    }

    public void setTitleText(String s) {
        titleText.setText(s);
    }
    public void setSubText(String s) {
        subText.setText(s);
    }
    public void setChartAvgVals(String s){
        String tmp = this.getResources().getString(R.string.average)
                +" "+s
                +this.getResources().getString(R.string.time);
        chartAvgVals.setText(tmp);
    }
    public void setChartAvgTimes(String start, String end){
        String tmp = start+"-"+end;
        charAvgTimes.setText(tmp);
    }
    /*
     * @brief bar chart
     * */
    public void setBarChart(){
        BarChart barChart = findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        // barChart.setScaleX(10);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getDescription().setText("");
        barChart.setPinchZoom(false);
        barChart.invalidate();

        Legend l = barChart.getLegend();
        l.setEnabled(false);
        // data insertion part
        for(int i =0; i<5; i++){
            entries.add(new BarEntry(i,i*10));
        }

        BarDataSet dataSet = new BarDataSet(entries,"label");
        dataSet.setColor(Color.rgb(133,204,159));

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
    }


}