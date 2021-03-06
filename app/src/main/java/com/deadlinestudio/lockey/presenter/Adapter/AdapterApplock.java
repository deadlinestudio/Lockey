package com.deadlinestudio.lockey.presenter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.ViewHolder.ViewHolderApplock;

import java.util.ArrayList;

public class AdapterApplock extends RecyclerView.Adapter<ViewHolderApplock>{
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
    @NonNull
    @Override
    public ViewHolderApplock onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_applock, viewGroup, false);

        ViewHolderApplock vh = new ViewHolderApplock(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderApplock viewHolderApplock, int i) {

        final ItemApplock item = items.get(i);
        viewHolderApplock.setAppName(item.getAppName());
        viewHolderApplock.setAppIcon(item.getAppIcon());
        viewHolderApplock.setAppSwitch(item.getLockFlag());
        viewHolderApplock.getAppSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setLockFlag(b);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
