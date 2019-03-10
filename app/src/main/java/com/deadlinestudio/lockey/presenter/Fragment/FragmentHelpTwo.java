package com.deadlinestudio.lockey.presenter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.deadlinestudio.lockey.R;

public class FragmentHelpTwo extends Fragment {
    private ImageView helpImg;
    private Button confirmBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_help, container, false);
        helpImg = rootView.findViewById(R.id.helpImg);
        helpImg.setImageDrawable(getResources().getDrawable(R.drawable.help2));

        return rootView;
    }
}