package com.deadlinestudio.lockey.presenter.Item;

import com.deadlinestudio.lockey.model.Data;

public class ItemAnalysis {
    String title;
    String subTitle;
    Data data;

    public ItemAnalysis(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ItemAnalysis{" +
                "title='" + title + '\'' +
                '}';
    }
}
