package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;

public class VerCheckActivity extends AppCompatActivity {
    private TextView openSource;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkver);

        openSource = findViewById(R.id.OpenSourceGo);
        backBtn = findViewById(R.id.checkVerBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 서비스 탈퇴
        openSource.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerCheckActivity.this,OpenSourceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }
}
