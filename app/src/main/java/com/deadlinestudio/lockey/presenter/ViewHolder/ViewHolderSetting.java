package com.deadlinestudio.lockey.presenter.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class ViewHolderSetting extends RecyclerView.ViewHolder{
    private TextView menuText;

    public ViewHolderSetting(View v) {
        super(v);
        menuText = v.findViewById(R.id.settingText);
    }
    public ViewHolderSetting(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_setting, parent, false));
        menuText = itemView.findViewById(R.id.settingText);
    }
    public void setmenuText(String s){
        menuText.setText(s);
    }

    public TextView getmenuText() {
        return menuText;
    }
}
