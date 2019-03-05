package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.deadlinestudio.lockey.R;

public class LockActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_lock);
        ImageView lockBaseImg = findViewById(R.id.lockBaseImg);
        ImageView lockRingImg = findViewById(R.id.lockRingImg);
        Button backBtn = findViewById(R.id.backToBtn);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
        lockRingImg.startAnimation(animation);

        backBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

}
