package com.deadlinestudio.lockey.presenter.Itemview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.graphics.Color;
import com.deadlinestudio.lockey.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemViewAlysLineChart extends LinearLayout {

    private TextView titleText, subText;
    private PieChart chart;
    private HashMap<String, Long> analysisData;
    private ArrayList<String> xaxis;

    public ItemViewAlysLineChart(Context context) {
        super(context);
        init(context);
    }

    public ItemViewAlysLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_alys_linechart, this, true);

        titleText = findViewById(R.id.analyTitleText);
    }

    public void setTitleText(String s) {
        titleText.setText(s);
    }
    public void setSubText(String s) {
        subText.setText(s);
    }
    /*
     * @brief Line chart
     * */
    public void setLineChart(){
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        List<Entry> entries = new ArrayList<Entry>();
        for(int i =0; i<5; i++){
            entries.add(new Entry(i,i));
        }
        LineDataSet dataSet = new LineDataSet(entries,"label");
        dataSet.setColor(Color.rgb(70,144,150));
        //dataSet.setColor(000);
        //dataSet.setValueTextColor(000);
        Legend l = lineChart.getLegend();
        l.setEnabled(false);


        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
