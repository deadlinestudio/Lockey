package com.deadlinestudio.lockey.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @brief Model Class with data analysis
 */
public class AnalysisData {
    private HashMap<String, Long> analysis_category;
    private HashMap<String, Long> analysis_week;
    private HashMap<String, Long> analysis_weekday;
    private long time_data;
    private float achievement_rate;

    /**
     * @brief Constructor
     */
    public AnalysisData() {
        analysis_category = new HashMap<>();
        analysis_week = new HashMap<>();
        analysis_weekday = new HashMap<>();
    }

    public HashMap<String, Long> getAnalysis_category() {
        return analysis_category;
    }

    public void setAnalysis_category(HashMap<String, Long> analysis_category) {
        this.analysis_category = analysis_category;
    }

    public HashMap<String, Long> getAnalysis_week() {
        return analysis_week;
    }

    public void setAnalysis_week(HashMap<String, Long> analysis_week) {
        this.analysis_week = analysis_week;
    }

    public HashMap<String, Long> getAnalysis_weekday() {
        return analysis_weekday;
    }

    public void setAnalysis_weekday(HashMap<String, Long> analysis_weekday) {
        this.analysis_weekday = analysis_weekday;
    }

    public long getTime_data() {
        return time_data;
    }

    public void setTime_data(long time_data) {
        this.time_data = time_data;
    }

    public float getAchievement_rate() {
        return achievement_rate;
    }

    public void setAchievement_rate(float achievement_rate) {
        this.achievement_rate = achievement_rate;
    }
}
