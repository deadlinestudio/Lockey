package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FacebookLoginActivity extends AppCompatActivity {

    private static final String TAG = "FacebookLogin";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private CallbackManager mCallbackManager;

    private LogfileController lfc = new LogfileController();
    final String filename = "userlog.txt";

    int InOutflag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Intent intent = getIntent();
        InOutflag = intent.getIntExtra("InOut",0);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                finish();
            }
        });
        // [END initialize_fblogin]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(InOutflag==1){

        }else if(InOutflag==2){
            signOut();

            Intent restart = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
        }

    }
    // [END on_start_check_user]

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                // 다음화면으로 이메일을 넘기고 화면을 띄운다
                                String EMAIL = user.getEmail();
                                if(EMAIL == null)
                                    Toast.makeText(getApplicationContext(), "이메일이 없습니다.", Toast.LENGTH_SHORT).show();
                                String content =
                                        "3," + EMAIL;
                                lfc.WriteLogFile(getApplicationContext(), filename, content, 2);

                                User temp_user = User.getInstance();
                                temp_user.setId(EMAIL);
                                NetworkTask networkTask = new NetworkTask(getBaseContext(), "/check-user", null);
                                networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                                if (networkTask.getUser().getisUser()) {
                                    String contents =
                                            "3," + EMAIL +
                                                    "," + networkTask.getUser().getNickname() +
                                                    "," + networkTask.getUser().getAge() +
                                                    "," + networkTask.getUser().getJob();
                                    lfc.WriteLogFile(getApplicationContext(), filename, contents, 2);

                                    Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                                    intent.putExtra("SNS", 3);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                String toastMsg = "로그인에 실패하였습니다.2" + task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            String toastMsg = "로그인에 실패하였습니다.3" + e.getMessage();
                            Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
    // [END auth_with_facebook]

    public void signOut() {
        Log.d("Facebook", mAuth.getCurrentUser().getDisplayName() + "logout");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

}