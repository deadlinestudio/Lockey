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

public class ItemViewApplock extends LinearLayout{
    private TextView appName;
    private ImageView appIcon;
    private Switch appSwitch;

    public ItemViewApplock(Context context) {
        super(context);
        init(context);
    }

    public ItemViewApplock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_applock, this, true);

        appName = findViewById(R.id.appLockText);
        appIcon = findViewById(R.id.appLockIcon);
        appSwitch = findViewById(R.id.appLockSwitch);
    }

    public void setAppName(String s){
        appName.setText(s);
    }
    public void setAppIcon(Drawable d){
        appIcon.setImageDrawable(d);
    }
    public void setAppSwitch(Boolean b){
        appSwitch.setChecked(b);
    }

    public TextView getAppName() {
        return appName;
    }

    public ImageView getAppIcon() {
        return appIcon;
    }

    public Switch getAppSwitch() {
        return appSwitch;
    }

}
