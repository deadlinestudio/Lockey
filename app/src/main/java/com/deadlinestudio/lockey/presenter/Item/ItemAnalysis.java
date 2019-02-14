package com.deadlinestudio.lockey.presenter.Item;

import com.deadlinestudio.lockey.model.Data;

public class ItemAnalysis {
    String title;
    String subTitle;
    Data data;

    public ItemAnalysis(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "ItemAnalysis{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}
