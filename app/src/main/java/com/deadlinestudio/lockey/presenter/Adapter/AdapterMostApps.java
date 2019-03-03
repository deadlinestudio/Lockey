package com.deadlinestudio.lockey.presenter.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Item.ItemMostApps;
import com.deadlinestudio.lockey.presenter.Item.MostAppsViewHolder;

import java.util.ArrayList;


public class AdapterMostApps extends RecyclerView.Adapter<MostAppsViewHolder> {
    private ArrayList<ItemMostApps> items = new ArrayList<ItemMostApps>();
    private Context context;

    public AdapterMostApps(Context c) {
        this.context = c;
    }

    public AdapterMostApps(Context c, ArrayList<ItemMostApps> list) {
        this.context = c;
        this.items = list;

    }

    public void addItem(ItemMostApps item) {
        items.add(item);
    }


    @NonNull
    @Override
    public MostAppsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mostapps, viewGroup, false);

        MostAppsViewHolder vh = new MostAppsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MostAppsViewHolder mostAppsViewHolder, int i) {

        final ItemMostApps item = items.get(i);
        mostAppsViewHolder.setAppName(item.getAppName());
        mostAppsViewHolder.setAppTime(item.getAppTime());
        mostAppsViewHolder.setAppIcon(item.getAppIcon());
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

