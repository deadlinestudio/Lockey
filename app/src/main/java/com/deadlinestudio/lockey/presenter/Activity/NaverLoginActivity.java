package com.deadlinestudio.lockey.presenter.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class NaverLoginActivity extends AppCompatActivity {
    private static final String TAG = "OAuthSampleActivity";

    private LogfileController lfc = new LogfileController();
    final String filename = "userlog.txt";

    int InOutflag;

    private static String OAUTH_CLIENT_ID = "ZkVcwgDkigHgXDp4bogX";
    private static String OAUTH_CLIENT_SECRET = "4vrrilEIdO";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();
        InOutflag = intent.getIntExtra("InOut", 0);

        mContext = this;
        initData();
    }


    @Override
    public void onStart() {
        super.onStart();

        if (InOutflag == 1) {
            signIn();
        } else if (InOutflag == 2) {
            signOut();
            Intent restart = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
        }
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        finish(); //LoginActivity로 이동
        super.onResume();

    }

    // 로그인 처리
    public void signIn() {
        mOAuthLoginInstance.startOauthLoginActivity((Activity)mContext, mOAuthLoginHandler);
    }

    // 로그아웃 처리(토큰도 함께 삭제)
    public void signOut() {
        // 스레드로 돌려야 한다. 안 그러면 로그아웃 처리가 안되고 false를 반환한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOAuthLoginInstance.logoutAndDeleteToken(mContext);
            }
        }).start();
        new DeleteTokenTask().execute();
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);

                new RequestApiTask(accessToken).execute();
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Log.e(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.e(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }
        }

    };

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
                String toastMsg = "로그인에 실패하였습니다.";
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            //updateView();
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        private String token;

        RequestApiTask(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            Log.e("Naver : ","성공이다!1111");
        }

        protected void onPostExecute(String response) {
            Log.e("Naver : ","성공이다!2222");
            Log.e("Naver Token: ",token);
            Log.e("Naver response: ",response);
            try {
                // response는 json encoded된 상태이기 때문에 json 형식으로 decode 해줘야 한다.
                JSONObject object = new JSONObject(response);
                JSONObject innerJson = new JSONObject(object.get("response").toString());
                // 만약 이메일이 필요한데 사용자가 이메일 제공을 거부하면
                // JSON 데이터에는 email이라는 키가 없고, 이걸로 제공 여부를 판단한다.
                if(!innerJson.has("email")) {
                    Log.e("Naver : ","이메일이 없으야");
                    signOut();
                } else {
                    String EMAIL = innerJson.getString("email");
                    //String nickname = innerJson.getString("nickname");
                    //String profileImgUrl = innerJson.getString("profile_image");
                    // 원하는 모든 과정이 처리가 되면 해당 멤버 데이터를 가지고 다음 로직을 수행한다.
                    Log.e("Naver EMAIL: ",EMAIL);

                    String content =
                            "2," + EMAIL;
                    lfc.WriteLogFile(getApplicationContext(), filename, content, 2);

                    User temp_user = User.getInstance();
                    temp_user.setId(EMAIL);
                    NetworkTask networkTask = new NetworkTask(getBaseContext(), "/check-user", null);
                    networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                    if (networkTask.getUser().getisUser()) {
                        String contents =
                                "2," + EMAIL +
                                        "," + networkTask.getUser().getNickname() +
                                        "," + networkTask.getUser().getAge() +
                                        "," + networkTask.getUser().getJob();
                        lfc.WriteLogFile(getApplicationContext(), filename, contents, 2);

                        Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        intent.putExtra("SNS", 2);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
                String toastMsg = "로그인에 실패하였습니다.";
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String header = "Bearer " + token;
            try {
                final String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        responseCode == 200 ? con.getInputStream() : con.getErrorStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                br.close();
                return response.toString();

            } catch(Exception e) {
                e.printStackTrace();
                String toastMsg = "로그인에 실패하였습니다.";
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                finish();
            }

            return null;
        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String res) {
            //updateView();
        }
    }
}