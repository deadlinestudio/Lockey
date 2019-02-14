package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Controller.LogfileController;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.concurrent.TimeUnit;

public class GoogleLoginActivity extends AppCompatActivity{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    int InOutflag;

    private LogfileController lfc = new LogfileController();
    final String filename = "userlog.txt";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();
        InOutflag = intent.getIntExtra("InOut",0);
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(InOutflag==1){
            signIn();
        }else if(InOutflag==2){
            signOut();

            Intent restart = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
            restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(restart);
        }
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");

                                // 다음화면으로 이름과 이메일을 넘기고 화면을 띄운다
                                String EMAIL = acct.getEmail();
                                Log.e("Google Email : ",EMAIL);
                                String content =
                                        "1," + EMAIL;
                                lfc.WriteLogFile(getApplicationContext(), filename, content, 2);

                                User temp_user = new User();
                                temp_user.setId(EMAIL);
                                NetworkTask networkTask = new NetworkTask("/check-user", temp_user, null);
                                networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                                if(networkTask.getUser().getisUser()) {
                                    String contents =
                                            "1," + EMAIL +
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
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                            }
                        } catch(Exception e) {
                            Log.e("google login", e.getMessage());
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}
