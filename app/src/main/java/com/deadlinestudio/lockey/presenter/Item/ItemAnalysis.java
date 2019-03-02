package com.deadlinestudio.lockey.presenter.Item;

import com.deadlinestudio.lockey.model.Data;

public class ItemAnalysis {
    private String title;
    private String subTitle;

    private String[] xlabels = {"월", "화", "수", "목", "금", "토", "일"};

    /**여기다 넘길거야*****/
    private Data data;

    public ItemAnalysis(String title) {
        this.title = title;
    }
    public ItemAnalysis(String title, String[] xlabels) {
        this.title = title;
        this.xlabels = xlabels;
    }
    public ItemAnalysis(String title, int mode) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getXlabels(){
        return this.xlabels;
    }
    @Override
    public String toString() {
        return "ItemAnalysis{" +
                "title='" + title + '\'' +
                '}';
    }
}
