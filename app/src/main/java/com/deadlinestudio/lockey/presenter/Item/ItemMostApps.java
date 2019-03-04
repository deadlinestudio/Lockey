package com.deadlinestudio.lockey.presenter.Item;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ItemMostApps {
    private String appName;
    private String appTime;

    private Drawable appIcon;

    public ItemMostApps(String appName, String appTime, Drawable appIcon){
        this.appName = appName;
        this.appTime = appTime;
        this.appIcon = appIcon;

    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
