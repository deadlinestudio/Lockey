package com.deadlinestudio.lockey.presenter.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class ViewHolderApplock extends RecyclerView.ViewHolder{
    private TextView appName;
    private ImageView appIcon;
    private Switch appSwitch;


    public ViewHolderApplock(View v) {
        super(v);
        appName = v.findViewById(R.id.appLockText);
        appIcon = v.findViewById(R.id.appLockIcon);
        appSwitch = v.findViewById(R.id.appLockSwitch);
    }
    public ViewHolderApplock(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_applock, parent, false));
        appName = itemView.findViewById(R.id.appLockText);
        appIcon = itemView.findViewById(R.id.appLockIcon);
        appSwitch = itemView.findViewById(R.id.appLockSwitch);
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
