package com.deadlinestudio.lockey.presenter.Itemview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class ItemViewMostApps extends LinearLayout {
    private TextView appName, appTime;
    private ImageView appIcon;

    public ItemViewMostApps(Context context) {
        super(context);
        init(context);
    }

    public ItemViewMostApps(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_mostapps, this, true);

        appName = findViewById(R.id.mostAppNameText);
        appTime = findViewById(R.id.mostAppTimeText);
        appIcon = findViewById(R.id.mostAppIcon);
    }



    public void setAppName(String s){
        appName.setText(s);
    }

    public void setAppTime(String s){
        appName.setText(s);
    }
    public void setAppIcon(Drawable d){
        appIcon.setImageDrawable(d);
    }

    public TextView getAppName() {
        return appName;
    }
    public TextView getAppTime() {
        return appTime;
    }
    public ImageView getAppIcon() {
        return appIcon;
    }

}
