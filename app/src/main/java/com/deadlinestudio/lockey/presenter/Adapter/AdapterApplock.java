package com.deadlinestudio.lockey.presenter.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewApplock;

import java.util.ArrayList;

public class AdapterApplock extends BaseAdapter{
    private ArrayList<ItemApplock> items = new ArrayList<ItemApplock>();
    private Context context;

    public AdapterApplock(Context c){
        this.context = c;
    }
    public AdapterApplock(Context c, ArrayList<ItemApplock> list){
        this.context = c;
        this.items = list;

    }
    public void addItem(ItemApplock item){
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
        ItemViewApplock v = new ItemViewApplock(context);
        final ItemApplock item = items.get(i);
        v.setAppName(item.getAppName());
        v.setAppIcon(item.getAppIcon());
        //v.setAppIcon(new BitmapDrawable(view.getContext().getResources(),item.getAppIcon()));
        v.setAppSwitch(item.getLockFlag());
        v.getAppSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setLockFlag(b);
            }
        });

        return v;
    }
}
