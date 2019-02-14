package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.util.concurrent.TimeUnit;


public class KakaoLoginActivity extends AppCompatActivity {

    //카카오용 콜백
    private SessionCallback callback;

    private LogfileController lfc = new LogfileController();
    final String filename = "userlog.txt";

    int InOutflag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        InOutflag = intent.getIntExtra("InOut",0);


    }

    @Override
    public void onStart() {
        super.onStart();
        if(InOutflag==1) {

            callback = new SessionCallback();
            Session.getCurrentSession().addCallback(callback);
            Session.getCurrentSession().checkAndImplicitOpen();
            Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT,this);
        }
        else if(InOutflag==2){
            //LoginManager.getInstance().logOut();
            Session.getCurrentSession().close();

            Intent restart = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
        }
    }


    private class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp");
                }

                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    try {
                    /*
                    Log.e("SessionCallback :: ", "onSuccess");
                    String nickname = userProfile.getNickname();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    long id = userProfile.getId();

                    Log.e("Profile : ", nickname + "");
                    Log.e("Profile : ", email + "");
                    Log.e("Profile : ", profileImagePath  + "");
                    Log.e("Profile : ", thumnailPath + "");
                    Log.e("Profile : ", UUID + "");
                    Log.e("Profile : ", id + "");
                    */

                        // 다음화면으로 이메일을 넘기고 화면을 띄운다
                        String EMAIL = userProfile.getEmail();
                        Log.e("KAKAO EMAIL : ",EMAIL);
                        String content =
                                "3," + EMAIL;
                        lfc.WriteLogFile(getApplicationContext(), filename, content, 2);

                        User temp_user = new User();
                        temp_user.setId(EMAIL);
                        NetworkTask networkTask = new NetworkTask("/check-user", temp_user, null);
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
                            startActivity(intent);
                            finish();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
                }
            });
        }
    }

}
