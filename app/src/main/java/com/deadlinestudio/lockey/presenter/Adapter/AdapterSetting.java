package com.deadlinestudio.lockey.presenter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deadlinestudio.lockey.presenter.Activity.GoogleLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.KakaoLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.LoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Activity.NaverLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.OpenSourceActivity;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Item.ItemSetting;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewSetting;

import java.util.ArrayList;

public class AdapterSetting extends BaseAdapter{
    ArrayList<ItemSetting> items = new ArrayList<ItemSetting>();
    private MainActivity mainActivity;
    private Context context;
    private LogfileController lfc;
    final String filename = "userlog.txt";

    public AdapterSetting(MainActivity m, Context c){
        this.mainActivity = m;
        this.context = c;
        lfc = new LogfileController();
    }
    public void addItem(ItemSetting item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int mode = i;
        ItemViewSetting v = new ItemViewSetting(context);
        ItemSetting item = items.get(i);
        v.setMenuText(item.getMenuTitle());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mode){
                    case 2:
                        Intent intents = new Intent(mainActivity, OpenSourceActivity.class);
                        intents.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mainActivity.startActivity(intents);
                        break;
                    case 3:
                        Log.e("deb/logout", "in");
                        lfc.WriteLogFile(context,filename,"",2);
                        lfc.WriteLogFile(context,filename,"nofile",2);
                        Intent intent;

                        if(mainActivity.getSns().equals("1")) {
                            intent = new Intent(mainActivity, GoogleLoginActivity.class);
                            intent.putExtra("InOut", 2);
                        }else if(mainActivity.getSns().equals("2")){
                            intent = new Intent(mainActivity, NaverLoginActivity.class);
                            intent.putExtra("InOut", 2);
                        }else if(mainActivity.getSns().equals("3")){
                            intent = new Intent(mainActivity, KakaoLoginActivity.class);
                            intent.putExtra("InOut", 2);
                        } else {
                            intent = new Intent(mainActivity, LoginActivity.class);
                        }
                        mainActivity.startActivity(intent);
                        mainActivity.finish();
                        break;
                }
            }
        });
        return v;
    }
}
