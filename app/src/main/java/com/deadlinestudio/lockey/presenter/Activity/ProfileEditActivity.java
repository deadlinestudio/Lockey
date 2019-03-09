package com.deadlinestudio.lockey.presenter.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;

import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProfileEditActivity extends AppCompatActivity {
    private Context cont;
    private Activity activity;
    private Toolbar mToolbar;
    private ImageView backBtn;

    private TextView idText, serviceOut, saveBtn;
    private EditText nickText, jobText;
    private Spinner jobSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        this.cont = getBaseContext();
        this.activity = this;

        backBtn = findViewById(R.id.profileEditCancel);
        saveBtn = findViewById(R.id.profileEditSaveBtn);
        serviceOut = findViewById(R.id.serviceOutLink);
        idText = findViewById(R.id.proEditIdText);
      
        nickText = findViewById(R.id.proEditNickText);
        nickText.setSelection(nickText.getText().length());
        nickText.setText(User.getInstance().getNickname());

        jobText = findViewById(R.id.proEditJobText);
        jobText.setSelection(jobText.getText().length());

        jobSpinner = findViewById(R.id.editJobSpinner);
        idText.setText(User.getInstance().getId());
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
                    if (nick.equals("") || job.equals("")) {
                        Toast.makeText(getBaseContext(), "빈칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        User user = User.getInstance();
                        user.setData(user.getId(), nick, user.getAge(), job);
                        NetworkTask networkTask = new NetworkTask(getBaseContext(), "/update-user", null);
                        networkTask.execute().get(1000, TimeUnit.MILLISECONDS);

                        LogfileController lfc = new LogfileController();
                        String contents =
                                MainActivity.getSns() +
                                        "," + user.getId() +
                                        "," + nick +
                                        "," + user.getAge() +
                                        "," + job;
                        lfc.WriteLogFile(getBaseContext(), MainActivity.getFilename(), contents, 2);
                        Toast.makeText(getBaseContext(), "프로필이 변경 되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
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
                if(MainActivity.getSns().equals("4") == true) {
                    String toastMsg = "비회원은 이용할 수 없습니다.";
                    Toast.makeText(cont, toastMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(cont);
                builder.setTitle("알림");
                builder.setMessage("회원탈퇴 하실 경우 다시 되돌릴 수 없습니다. 그래도 계속하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    NetworkTask networkTask = new NetworkTask(getBaseContext(), "/leave-user", null);
                                    networkTask.execute().get(1000, TimeUnit.MILLISECONDS);

                                    LogfileController lfc = new LogfileController();
                                    lfc.WriteLogFile(cont, LoginActivity.filename,"nofile",2);

                                    Intent intent = null;
                                    if(MainActivity.getSns().equals("1")) {
                                        intent = new Intent(activity, GoogleLoginActivity.class);
                                        intent.putExtra("InOut", 2);
                                    }else if(MainActivity.getSns().equals("2")){
                                        intent = new Intent(activity, NaverLoginActivity.class);
                                        intent.putExtra("InOut", 2);
                                    }else if(MainActivity.getSns().equals("3")){
                                        intent = new Intent(activity, KakaoLoginActivity.class);
                                        intent.putExtra("InOut", 2);
                                    } else {
                                        intent = new Intent(cont, LoginActivity.class);
                                    }
                                    activity.startActivity(intent);
                                    activity.finish();
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
