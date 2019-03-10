package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Controller.AdmobAdController;

public class LockActivity extends AppCompatActivity {
    AdmobAdController aac;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        aac = new AdmobAdController(this);

        setContentView(R.layout.activity_lock);
        //ImageView lockBaseImg = findViewById(R.id.lockBaseImg);
        ImageView lockRingImg = findViewById(R.id.lockRingImg);
        Button backBtn = findViewById(R.id.backToBtn);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
        lockRingImg.startAnimation(animation);

        backBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent restart = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
                restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(restart);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        aac.runVideoAd();
    }

    @Override
    public void onBackPressed() {
        Log.e("lock back", "start!");
        //moveTaskToBack(true);
        Intent intent = new Intent(Intent.ACTION_MAIN);     // Home으로 이동
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
