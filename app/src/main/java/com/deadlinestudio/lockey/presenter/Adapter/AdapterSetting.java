package com.deadlinestudio.lockey.presenter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.deadlinestudio.lockey.presenter.Item.ItemSetting;
import com.deadlinestudio.lockey.presenter.Itemview.ItemViewSetting;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
                Intent intent = null;
                switch (mode){
                    case 0: // 도움말
                        break;
                    case 1: // 데이터 초기화
                        break;
                    case 2: // 버전 확인
                        intent = new Intent(mainActivity, VerCheckActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mainActivity.startActivity(intent);
                        break;
                    case 3:  //로그 아웃
                        Log.e("deb/logout", "in");
                        lfc.WriteLogFile(context,filename,"",2);
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
                        break;
                }
            }
        });
        return v;
    }

    /**
     * @brief dialog message with edit text for changing the nickname

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Get the layout inflater
        LayoutInflater inflater = this.mainActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_nickname, null));

        final AlertDialog dialog = builder.create();
        dialog.show();
        Button saveBtn = dialog.findViewById(R.id.saveChangeBtn);
        final EditText ed = dialog.findViewById(R.id.newNicknameText);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    if(mainActivity.getSns().equals("4") == false) {
                        String user_id = mainActivity.getId();
                        User user = new User(user_id, null, 0, null);
                        String new_nick = ed.getText().toString();
                        NetworkTask networkTask = new NetworkTask(mainActivity.getBaseContext(), "/update-user", user, null);
                        networkTask.prepareUpdate("nickname", new_nick);
                        networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                        String toastMsg = "닉네임이 변경됐습니다";
                        TextView profileName = mainActivity.findViewById(R.id.profileName);
                        FragmentSetting.setProfileName(new_nick);
                        profileName.setText(new_nick);
                        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
                    } else {
                        String toastMsg = "비회원은 저장되지 않습니다";
                        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
     **/
}
