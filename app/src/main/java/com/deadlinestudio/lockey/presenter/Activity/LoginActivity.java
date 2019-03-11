package com.deadlinestudio.lockey.presenter.Activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.GrantController;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class LoginActivity extends BaseActivity{
    private FrameLayout kakaoBtn, NaverBtn, googleBtn;
    private TextView noMemberBtn;
    private Button signUpGoBtn, logInGoBtn;

    private LogfileController lfc;
    private GrantController gc;
    private Context cont;
    public static final String filename = "userlog.txt";

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.e("Hash key", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String a = getKeyHash(this);
        Log.e("Hash : ",a);
        lfc = new LogfileController();
        cont = getApplicationContext();
        gc = new GrantController(cont);

        /* 가장 많이 사용한 앱 리스트 로드하기 위해 권한 설정*/
        if(!gc.checkAccessGrant())
            gc.settingAccessGrant();

        //check login log
        String line = lfc.ReadLogFile(cont,filename);
        try {
            if (!line.equals("nofile")) {
                String sns = "";
                StringTokenizer tokens = new StringTokenizer(line, ",");
                User temp_user = User.getInstance();
                if(tokens.hasMoreTokens()) {
                    sns = tokens.nextToken();
                    temp_user.setId(tokens.nextToken());
                    NetworkTask networkTask = new NetworkTask(getBaseContext(),"/check-user", null);
                    networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                    if (networkTask.getUser().getisUser()) {
                        String contents =
                                sns +
                                        "," + networkTask.getUser().getId() +
                                        "," + networkTask.getUser().getNickname() +
                                        "," + networkTask.getUser().getAge() +
                                        "," + networkTask.getUser().getJob();
                        Log.e("writeLog", contents);
                        lfc.WriteLogFile(getApplicationContext(), filename, contents, 2);

                        Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        } catch(Exception e) {
            Log.e("error in loginActivity", e.toString());
        }
        noMemberBtn = findViewById(R.id.noMemberBtn);
        signUpGoBtn = findViewById(R.id.selectSignUpBtn);
        logInGoBtn = findViewById(R.id.selectLoginBtn);

        signUpGoBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });
        logInGoBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
        /**
        * @brief for Non-member user
        **/
        noMemberBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = "4,";
                lfc.WriteLogFile(getApplicationContext(), filename, content, 2);
                Intent intent = new Intent(cont, LoadActivity.class);
                intent.putExtra("SNS",4);
                startActivity(intent);
                finish();
            }
        });

        /**
         * terms links
         * */
        TextView termsText = findViewById(R.id.termsText);
        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String s) {
                return "";
            }
        };
        Pattern pattern1 = Pattern.compile("서비스이용약관");
        Pattern pattern2 = Pattern.compile("개인정보처리방침");
        Pattern pattern3 = Pattern.compile("개인정보수집이용");
        Linkify.addLinks(termsText, pattern1, "http://thedeadlinestudio.com/lockey/terms/",null,mTransform);
        Linkify.addLinks(termsText, pattern2, "http://thedeadlinestudio.com/lockey/terms/",null,mTransform);
        Linkify.addLinks(termsText, pattern3, "http://thedeadlinestudio.com/lockey/terms/",null,mTransform);


    }

    public void showSignUpDialog() {
        // Create an instance of the dialog fragment and show it
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_signup, null));

        final AlertDialog dialog = builder.create();
        dialog.show();

        googleBtn = dialog.findViewById(R.id.googleBtn);
        kakaoBtn = dialog.findViewById(R.id.kakaoBtn);
        NaverBtn = dialog.findViewById(R.id.naverBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_signup);

        /**
         * @brief click Listener for each sign in buttons
         **/
        googleBtn.setOnClickListener(view -> {
            ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null){
                Log.e("인터넷","연결댐");
                Intent intent = new Intent(cont,GoogleLoginActivity.class);
                intent.putExtra("InOut",1);
                startActivity(intent);
            }else{
                String toastMsg = "인터넷을 연결해주세요.";
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
            }
        });

        kakaoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null){
                    Log.e("인터넷","연결댐");
                    Intent intent = new Intent(cont,KakaoLoginActivity.class);
                    intent.putExtra("InOut",1);
                    startActivity(intent);
                }else{
                    String toastMsg = "인터넷을 연결해주세요.";
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        NaverBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null){
                    Log.e("인터넷","연결댐");
                    Intent intent = new Intent(cont,NaverLoginActivity.class);
                    intent.putExtra("InOut",1);
                    startActivity(intent);
                }else{
                    String toastMsg = "인터넷을 연결해주세요.";
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    public void showLoginDialog() {
        // Create an instance of the dialog fragment and show it
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_login, null));

        final AlertDialog dialog = builder.create();
        dialog.show();

        googleBtn = dialog.findViewById(R.id.googleBtn);
        kakaoBtn = dialog.findViewById(R.id.kakaoBtn);
        NaverBtn = dialog.findViewById(R.id.naverBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_signup);

        /**
         * @brief click Listener for each sign in buttons
         **/
        googleBtn.setOnClickListener(view -> {
            ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null){
                Log.e("인터넷","연결댐");
                Intent intent = new Intent(cont,GoogleLoginActivity.class);
                intent.putExtra("InOut",1);
                startActivity(intent);
            }else{
                String toastMsg = "인터넷을 연결해주세요.";
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
            }
        });

        kakaoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null){
                    Log.e("인터넷","연결댐");
                    Intent intent = new Intent(cont,KakaoLoginActivity.class);
                    intent.putExtra("InOut",1);
                    startActivity(intent);
                }else{
                    String toastMsg = "인터넷을 연결해주세요.";
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        NaverBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork != null){
                    Log.e("인터넷","연결댐");
                    Intent intent = new Intent(cont,NaverLoginActivity.class);
                    intent.putExtra("InOut",1);
                    startActivity(intent);
                }else{
                    String toastMsg = "인터넷을 연결해주세요.";
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
    }
    // [END on_start_check_user]

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
