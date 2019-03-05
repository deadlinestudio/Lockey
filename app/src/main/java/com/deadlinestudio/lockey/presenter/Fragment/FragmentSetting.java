package com.deadlinestudio.lockey.presenter.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Activity.OpenSourceActivity;
import com.deadlinestudio.lockey.presenter.Activity.ProfileEditActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterSetting;
import com.deadlinestudio.lockey.presenter.Item.ItemSetting;

public class FragmentSetting extends Fragment{

    private TextView profileName;
    private RelativeLayout profileLayout;
    private MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);
        mainActivity = (MainActivity) this.getActivity();
        profileLayout = rootView.findViewById(R.id.profile);
        profileName = rootView.findViewById(R.id.profileName);
        String nick = mainActivity.getNickname();
        profileName.setText(!(nick.equals("")) ? nick : "비회원");

        profileLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // go to profile edit
                if(mainActivity.getSns().equals("4") == true) {
                    String toastMsg = "비회원은 이용할 수 없습니다.";
                    Toast.makeText(mainActivity.getBaseContext(), toastMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mainActivity, ProfileEditActivity.class);
                    intent.putExtra("id", MainActivity.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    mainActivity.startActivity(intent);
                }
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.settingList);
        AdapterSetting adapter = new AdapterSetting(mainActivity,getActivity().getApplicationContext());

        adapter.addItem(new ItemSetting("공지 사항"));
        adapter.addItem(new ItemSetting("도움말"));
        adapter.addItem(new ItemSetting("데이터 초기화"));
        adapter.addItem(new ItemSetting("버전 확인"));
        adapter.addItem(new ItemSetting("오픈소스 라이센스"));
        adapter.addItem(new ItemSetting("로그아웃"));

        listView.setAdapter(adapter);

        return rootView;
    }

    public void setProfileName(String nickname) {
        this.profileName.setText(nickname);
    }
}
