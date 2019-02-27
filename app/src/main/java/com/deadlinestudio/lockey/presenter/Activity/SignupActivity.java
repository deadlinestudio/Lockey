package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;

import java.lang.reflect.Field;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {
    private Button signupBtn;
    private EditText nicknameText, jobText, ageText;
    private Spinner jobSpinner;
    private LogfileController lfc;
    private Context cont;
    private final String filename = "userlog.txt";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        lfc = new LogfileController();
        nicknameText = findViewById(R.id.nicknameeditText);
        ageText = findViewById(R.id.ageeditText);
        jobSpinner = findViewById(R.id.jobSpinner);
        signupBtn = findViewById(R.id.signupBtn);
        jobText = findViewById(R.id.jobText);
        jobText.setVisibility(View.GONE);
        cont = this.getApplicationContext();

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

        signupBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String nick = nicknameText.getText().toString();
                    int age = Integer.parseInt(ageText.getText().toString());
                    String job = jobSpinner.getSelectedItem().toString();
                    if(job.equals("기타")) {
                        job = jobText.getText().toString();
                    }
                    if (nick.equals("") ||
                            ageText.getText().equals("") ||
                            job.equals("")) {
                        Toast.makeText(cont, "빈칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        String contents =
                                "," + nick +
                                        "," + age +
                                        "," + job;
                        lfc.WriteLogFile(cont, filename, contents, 1);
                        String line = lfc.ReadLogFile(cont, filename);
                        StringTokenizer tokens = new StringTokenizer(line, ",");

                        tokens.nextToken();
                        String id = tokens.nextToken();
                        User user = new User(id, nick, age, job);
                        NetworkTask networkTask = new NetworkTask("/register-user", user, null);
                        networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                        Intent intent = new Intent(getBaseContext(), LoadActivity.class);
                        startActivity(intent);
                    }
                } catch(Exception e ) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                lfc = new LogfileController();
                lfc.WriteLogFile(this, filename,"nofile",2);
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
