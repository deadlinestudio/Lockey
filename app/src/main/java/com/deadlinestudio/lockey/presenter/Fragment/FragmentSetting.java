package com.deadlinestudio.lockey.presenter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Adapter.AdapterSetting;
import com.deadlinestudio.lockey.presenter.Item.ItemSetting;

public class FragmentSetting extends Fragment{
    private TextView profileName;
    public MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_setting, container,false);
        mainActivity = (MainActivity) this.getActivity();
        profileName = rootView.findViewById(R.id.profileName);
        String nick = mainActivity.getNickname();
        profileName.setText(!(nick.equals("")) ? nick : "비회원");
        ListView listView = (ListView) rootView.findViewById(R.id.settingList);
        AdapterSetting adapter = new AdapterSetting(mainActivity,getActivity().getApplicationContext());

        adapter.addItem(new ItemSetting("계정 관리"));
        adapter.addItem(new ItemSetting("결제 내역 보기"));
        adapter.addItem(new ItemSetting("오픈소스 라이센스"));
        adapter.addItem(new ItemSetting("로그아웃"));
        listView.setAdapter(adapter);

        return rootView;
    }
}
