package com.deadlinestudio.lockey.presenter.Itemview;


import android.graphics.Color;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemViewAlysBarChart extends LinearLayout {
    private TextView titleText, subText, chartAvgVals, charAvgTimes;
    private PieChart chart;

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
    private void setChartAvgVals(double avg){
        String tmp = this.getResources().getString(R.string.average)
                +" "+  String.format("%.1f", avg)
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
    public void setBarChart(HashMap<String, Long> analysisData, final String[] xLabels, int period){
        BarChart barChart = findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        // barChart.setScaleX(10);
        barChart.setDoubleTapToZoomEnabled(false);
        //barChart.getXAxis().setGranularity(1f);
        barChart.setDrawBorders(true);
        barChart.setGridBackgroundColor(this.getResources().getColor(R.color.lightGrey));
        barChart.setBorderColor(this.getResources().getColor(R.color.lightGrey));
        barChart.getDescription().setText("");
        barChart.setPinchZoom(false);
        barChart.invalidate();

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setDrawLabels(false);
        rightAxis.setTextColor(this.getResources().getColor(R.color.darkGrey));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGridColor(this.getResources().getColor(R.color.lightGrey));
        xAxis.setTextColor(this.getResources().getColor(R.color.darkGrey));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend l = barChart.getLegend();
        l.setEnabled(false);

        // data insertion part
        double avg = 0;
        for(int i =0; i<xLabels.length; i++){
            Log.e("xLabel", xLabels[i]);
            int value = analysisData.get(xLabels[i]).intValue() / 60;
            entries.add(new BarEntry(i, value));
            avg += value / period;
        }
        setChartAvgVals(avg / 60);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //Print.e(value);
                return xLabels[(int)value];
            }
        });

        BarDataSet dataSet = new BarDataSet(entries,"label");
        dataSet.setColor(Color.rgb(133,204,159));

        BarData barData = new BarData(dataSet);
        barData.setHighlightEnabled(false);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);
    }


}
