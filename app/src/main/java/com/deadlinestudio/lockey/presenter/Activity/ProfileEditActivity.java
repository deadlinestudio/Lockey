package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class ProfileEditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView backBtn;

    private TextView idText, serviceOut, saveBtn;
    private EditText nickText, jobText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        backBtn = findViewById(R.id.profileEditCancel);
        saveBtn = findViewById(R.id.profileEditSaveBtn);
        serviceOut = findViewById(R.id.serviceOutLink);
        idText = findViewById(R.id.proEditIdText);
        nickText = findViewById(R.id.proEditNickText);
        jobText = findViewById(R.id.proEditJobText);


        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 변경 사항 저장
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 서비스 탈퇴
        serviceOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
