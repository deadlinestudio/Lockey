package com.deadlinestudio.lockey.presenter.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class MostAppsViewHolder extends RecyclerView.ViewHolder{
    private TextView appName, appTime;
    private ImageView appIcon;

    public MostAppsViewHolder(View v) {
        super(v);
        appName = v.findViewById(R.id.mostAppNameText);
        appTime = v.findViewById(R.id.mostAppTimeText);
        appIcon = v.findViewById(R.id.mostAppIcon);
    }
    public MostAppsViewHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_mostapps, parent, false));

        appName = itemView.findViewById(R.id.mostAppNameText);
        appTime = itemView.findViewById(R.id.mostAppTimeText);
        appIcon = itemView.findViewById(R.id.mostAppIcon);
    }

    public void setAppName(String s){
        appName.setText(s);
    }

    public void setAppTime(String s){
        appTime.setText(s);
    }
    public void setAppIcon(Drawable d){
        appIcon.setImageDrawable(d);
    }
}
