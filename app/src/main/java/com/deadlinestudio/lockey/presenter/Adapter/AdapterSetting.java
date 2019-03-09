package com.deadlinestudio.lockey.presenter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Activity.GoogleLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.KakaoLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.LoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Activity.NaverLoginActivity;
import com.deadlinestudio.lockey.presenter.Activity.OpenSourceActivity;
import com.deadlinestudio.lockey.presenter.Activity.VerCheckActivity;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.presenter.Fragment.FragmentSetting;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.Item.ItemSetting;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewSetting;
import com.deadlinestudio.lockey.presenter.ViewHolder.ViewHolderApplock;
import com.deadlinestudio.lockey.presenter.ViewHolder.ViewHolderSetting;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AdapterSetting extends RecyclerView.Adapter<ViewHolderSetting> {
    private ArrayList<ItemSetting> items = new ArrayList<ItemSetting>();
    private MainActivity mainActivity;
    private Context context;
    AlertDialog.Builder builder;
    Intent intent;
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
    @NonNull
    @Override
    public ViewHolderSetting onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_setting, viewGroup, false);
        ViewHolderSetting vh = new ViewHolderSetting(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSetting viewHolderSetting, int i) {
        final ItemSetting item = items.get(i);
        viewHolderSetting.setmenuText(item.getMenuTitle());
        viewHolderSetting.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int mode = i;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (mode){
                            case 0: // 도움말
                                break;
                            case 1: // 데이터 초기화
                                if(mainActivity.getSns().equals("4") == true) {
                                    String toastMsg = "비회원은 이용할 수 없습니다.";
                                    Toast.makeText(mainActivity.getBaseContext(), toastMsg, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                builder = new AlertDialog.Builder(mainActivity);
                                builder.setTitle("알림");
                                builder.setMessage("초기화 하실 경우 다시 되돌릴 수 없습니다. 그래도 계속하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                try {
                                                    NetworkTask networkTask = new NetworkTask(context, "/reset-time", null);
                                                    networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                                break;
                            case 2: // 버전 확인
                                intent = new Intent(mainActivity, VerCheckActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                mainActivity.startActivity(intent);
                                break;
                            case 3:  //로그 아웃
                                Log.e("deb/logout", "in");
                                builder = new AlertDialog.Builder(mainActivity);
                                builder.setTitle("알림");
                                builder.setMessage("초기화 하실 경우 다시 되돌릴 수 없습니다. 그래도 계속하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                lfc.WriteLogFile(context,filename,"nofile",2);

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
                                            }
                                        });
                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();

                                break;
                        }
                    }
                });
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
