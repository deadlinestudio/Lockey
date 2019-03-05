package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;

import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class ProfileEditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView backBtn;

    private TextView idText, serviceOut, saveBtn;
    private EditText nickText, jobText;
    private Spinner jobSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        backBtn = findViewById(R.id.profileEditCancel);
        saveBtn = findViewById(R.id.profileEditSaveBtn);
        serviceOut = findViewById(R.id.serviceOutLink);
        idText = findViewById(R.id.proEditIdText);
        nickText = findViewById(R.id.proEditNickText);
        jobSpinner = findViewById(R.id.editJobSpinner);

        jobText = findViewById(R.id.editJobText);
        idText.setText(id);
        jobText.setVisibility(View.GONE);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            ListPopupWindow window = (ListPopupWindow) popup.get(jobSpinner);
            window.setHeight(400);
        } catch(Exception e) {
            e.printStackTrace();
        }

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if(jobSpinner.getItemAtPosition(position).equals("기타")) {
                    jobText.setVisibility(View.VISIBLE);
                } else {
                    jobText.setText("");
                    jobText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });

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
                try {
                    String nick = nickText.getText().toString();
                    String job = jobSpinner.getSelectedItem().toString();
                    if(job.equals("기타")) {
                        job = jobText.getText().toString();
                    }
                    if (nick.equals("") ||
                            job.equals("")) {
                        Toast.makeText(getBaseContext(), "빈칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        User user = new User(id, nick, MainActivity.getAge(), job);
                        NetworkTask networkTask = new NetworkTask(getBaseContext(), "/update-user", user, null);
                        networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                        Toast.makeText(getBaseContext(), "프로필이 변경 되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), LoadActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch(Exception e ) {
                    e.printStackTrace();
                }
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
