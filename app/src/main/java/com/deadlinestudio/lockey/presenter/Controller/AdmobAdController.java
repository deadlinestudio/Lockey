package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AdmobAdController implements RewardedVideoAdListener{
    private RewardedVideoAd mRewardedVideoAd;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";      //ca-app-pub-8600536608045213~4285578189
    private static final String APP_ID = "ca-app-pub-3940256099942544~3347511713";          //ca-app-pub-8600536608045213/7270108792
    private Activity act;
    private boolean load;
    private NotificationManager mNotificationManager;
    private int prevNotificationFilter = NotificationManager.INTERRUPTION_FILTER_ALL;

    private GrantController gc;

    public AdmobAdController(Activity activity){
        this.act = activity;
        this.load = false;
        this.gc = new GrantController(act);

        MobileAds.initialize(act, APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(act);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        if(!mRewardedVideoAd.isLoaded()) {
            /* do not disturb mode NotificationManager */
            mNotificationManager = (NotificationManager) act.getSystemService(NOTIFICATION_SERVICE);
            if(!gc.checkAlertGrant(mNotificationManager))
                gc.settingAlertGrant();
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    public void runVideoAd(){
        if (mRewardedVideoAd.isLoaded()) {
            Log.e("동영상 광고","Run!!");
            mRewardedVideoAd.show();
        }else{
            Log.e("동영상 광고가","로드 안댐요");
            //loadRewardedVideoAd();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.e("보상형"," 광고 로드댐");
        runVideoAd();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.e("보상형"," 광고 열렸다");
        prevNotificationFilter = mNotificationManager.getCurrentInterruptionFilter();
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);       // turn on DO NOT DISTURB MODE
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.e("보상형"," 광고 시작한다");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.e("보상형"," 광고 껐다");
        mNotificationManager.setInterruptionFilter(prevNotificationFilter);       // turn off DO NOT DISTURB MODE
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        // 보상형 광고
        Log.e("보상형"," 광고 다봤다");
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);       // turn off DO NOT DISTURB MODE
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.e("보상형"," 광고 로드실패");
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public boolean getLoad(){
        return this.load;
    }
}
