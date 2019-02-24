package com.deadlinestudio.lockey.presenter.Itemview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemViewAlysPieChart extends LinearLayout {

    private TextView titleText, subText;
    private PieChart chart;
    private HashMap<String, Long> analysisData;
    private ArrayList<String> xaxis;

    public ItemViewAlysPieChart(Context context) {
        super(context);
        init(context);
    }

    public ItemViewAlysPieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_alys_piechart, this, true);

        titleText = findViewById(R.id.analyTitleText);
    }

    public void setTitleText(String s) {
        titleText.setText(s);
    }
    public void setSubText(String s) {
        subText.setText(s);
    }

    /*
     * @brief pie chart
     * */
    public void setPieChart(){
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(8f, "라키"));
        yVals.add(new PieEntry(15f, "짱짱맨"));
        yVals.add(new PieEntry(12f, "회사"));
        yVals.add(new PieEntry(25f, "가기"));
        yVals.add(new PieEntry(23f, "싫다"));
        yVals.add(new PieEntry(17f, "리얼"));

        PieDataSet dataSet = new PieDataSet(yVals,"Study Times");
        dataSet.setSliceSpace(4f);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
    }

}
