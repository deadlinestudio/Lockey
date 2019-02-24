package com.deadlinestudio.lockey.presenter.Itemview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemViewAnalysis extends LinearLayout {
    private TextView titleText, subText;
    private LineChart chart;
    private HashMap<String, Long> analysisData;
    private ArrayList<String> xaxis;

    public ItemViewAnalysis(Context context) {
        super(context);
        init(context);
    }

    public ItemViewAnalysis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_analysis, this, true);

        titleText = findViewById(R.id.analyTitleText);
    }

    public void setTitleText(String s) {
        titleText.setText(s);
    }
    public void setSubText(String s) {
        subText.setText(s);
    }

    /*
     * @brief combine chart (line + bar graph)
     * set the line and bar data by generate functions
     * */
    public void setCombinedChart(int index, HashMap<String, Long> _analysisData, ArrayList<String> _xaxis){
        this.xaxis = _xaxis;
        this.analysisData = _analysisData;
        CombinedChart combinedChart = findViewById(R.id.combinedChart);
        combinedChart.getDescription().setEnabled(false);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);
        combinedChart.setPinchZoom(false);
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        Legend l = combinedChart.getLegend();
        l.setEnabled(false);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawLabels(false);
        rightAxis.setAxisLineColor(000);


        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis.setAxisLineColor(000);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xaxis.get((int) value % xaxis.size());
            }
        });

        CombinedData combinedData = new CombinedData();
        combinedData.setData(generateLineData());
        combinedData.setData(generateBarData());
        combinedChart.setPinchZoom(false);
        combinedChart.setDoubleTapToZoomEnabled(false);
        combinedChart.setData(combinedData);
        combinedChart.invalidate();
    }

    public LineData generateLineData(){
        List<Entry> entries = new ArrayList<Entry>();
        int sum = 0;
        for(int i = 1; i < xaxis.size()-1; i++) {
            sum += (int) (analysisData.get(xaxis.get(i)) / 60);
        }
        int avg = 40;
        if(xaxis.size()-2 > 0)
            avg = sum / (xaxis.size()-2);
        for(int i =0; i<xaxis.size(); i++){
            entries.add(new Entry(i, avg));
        }
        LineDataSet dataSet = new LineDataSet(entries,"label");
        dataSet.setColor(Color.rgb(33,33,33));
        dataSet.setLineWidth(1f);
        //dataSet.setValueTextColor(000);
        dataSet.setDrawCircles(false);
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);
        return lineData;
    }

    public BarData generateBarData(){
        List<BarEntry> entries = new ArrayList<BarEntry>();

        for(int i = 1; i<xaxis.size()-1; i++){
            entries.add(new BarEntry(i, (int) (analysisData.get(xaxis.get(i)) / 60)));
        }
        entries.add(new BarEntry(xaxis.size()-1,0));

        BarDataSet dataSet = new BarDataSet(entries,"label");
        dataSet.setColor(Color.rgb(133,204,159));

        BarData barData = new BarData(dataSet);
        barData.setDrawValues(false);
        return barData;
    }

}

