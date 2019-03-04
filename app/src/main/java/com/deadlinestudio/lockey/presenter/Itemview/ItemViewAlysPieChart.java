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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ItemViewAlysPieChart extends LinearLayout {
    private int[] pieColors ={
            this.getContext().getColor(R.color.catOne),
            this.getContext().getColor(R.color.catTwo),
            this.getContext().getColor(R.color.catThree),
            this.getContext().getColor(R.color.catFour),
            this.getContext().getColor(R.color.catFive),

    };
    private TextView titleText, subText;
    private PieChart chart;

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
    public void setPieChart(HashMap<String, Long> analysisData){
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        if(analysisData == null) {
            yVals.add(new PieEntry((float) 50f, "수학"));
            yVals.add(new PieEntry((float) 12f, "국어"));
            yVals.add(new PieEntry((float) 22f, "영어"));
            yVals.add(new PieEntry((float) 33f, "과학"));
        } else {
            Map<String, Long> sorted = analysisData
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

            Iterator<String> keys = sorted.keySet().iterator();
            int cnt = 4;
            long sum = 0;
            while (keys.hasNext()) {
                String key = keys.next();
                if (cnt-- > 0)
                    yVals.add(new PieEntry((float) analysisData.get(key), key));
                else
                    sum += analysisData.get(key);
            }

            if (sum != 0)
                yVals.add(new PieEntry((float) sum, "기타"));
        }

        PieDataSet dataSet = new PieDataSet(yVals,"Study Times");
        dataSet.setSliceSpace(4f);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : pieColors) {
            colors.add(color);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(13f);
        pieChart.setData(data);

        pieChart.invalidate();
    }

}
