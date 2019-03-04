package com.deadlinestudio.lockey.presenter.Item;

import android.util.Log;

import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ItemAnalysis {
    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final int PIE_GRAPH = 1;
    public static final int BAR_GRAPH = 2;
    private static final int periods[] = {1, 7, 30, 365};
    private String title;
    private String subTitle;
    private String[] xLabels;
    private int period;

    private HashMap<String, Long> analysisData;

    public ItemAnalysis(String title, int mode, int type) {
        this.title = title;
        this.period = periods[mode];
        setAnalysisData(mode,type);
    }

    public ItemAnalysis(String title, String[] xlabels, int mode, int type) {
        this.title = title;
        this.xLabels = xlabels;
        this.period = periods[mode];
        setAnalysisData(mode,type);
    }

    private void setAnalysisData(int mode, int type) {
        NetworkTask asyncNetwork = null;
        try {
            if (type == PIE_GRAPH) {
                asyncNetwork = new NetworkTask("/classify-category",
                        new User(MainActivity.getId(), null, 0, null), null);
            } else if (type == BAR_GRAPH) {
                switch (mode) {
                    case DAY:
                        asyncNetwork = new NetworkTask("/classify-day",
                                new User(MainActivity.getId(), null, 0, null), null);
                        break;
                    case WEEK:
                        asyncNetwork = new NetworkTask("/classify-week",
                                new User(MainActivity.getId(), null, 0, null), null);
                        break;
                    case MONTH:
                        asyncNetwork = new NetworkTask("/classify-month",
                                new User(MainActivity.getId(), null, 0, null), null);
                        break;
                    case YEAR:
                        asyncNetwork = new NetworkTask("/classify-year",
                                new User(MainActivity.getId(), null, 0, null), null);
                        break;
                    default:
                        break;
                }
            }
            asyncNetwork.setConfig(mode, type);
            asyncNetwork.execute().get(1000, TimeUnit.MILLISECONDS);
            analysisData = asyncNetwork.getAnalysisData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setxLabels() {
        Object[] keys = analysisData.keySet().toArray();
        Arrays.sort(keys);
        xLabels = new String[keys.length];
        for(int i = 0; i < keys.length; i++) {
            xLabels[i] = new String((String) keys[i]);
        }
    }

    public String[] getXLabels(){
        return this.xLabels;
    }

    public int getPeriod() {
        return this.period;
    }

    public HashMap<String, Long> getAnalysisData() {
        return this.analysisData;
    }

    @Override
    public String toString() {
        return "ItemAnalysis{" +
                "title='" + title + '\'' +
                '}';
    }
}
