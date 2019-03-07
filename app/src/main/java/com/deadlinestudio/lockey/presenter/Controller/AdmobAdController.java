package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdmobAdController implements RewardedVideoAdListener{
    private RewardedVideoAd mRewardedVideoAd;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";      //ca-app-pub-8600536608045213~4285578189
    private static final String APP_ID = "ca-app-pub-3940256099942544~3347511713";          //ca-app-pub-8600536608045213/7270108792
    private Activity act;
    private boolean load;

    public AdmobAdController(Activity activity){
        this.act = activity;
        this.load = false;

        MobileAds.initialize(act, APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(act);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        if(!mRewardedVideoAd.isLoaded())
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
    }

    public void runVideoAd(){
        if (mRewardedVideoAd.isLoaded()) {
            Log.e("동영상 광고","Run!!");
            mRewardedVideoAd.show();
        }else{
            Log.e("동영상 광고가","로드 안댐요");
            loadRewardedVideoAd();
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
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.e("보상형"," 광고 시작한다");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.e("보상형"," 광고 껐다");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        // 보상형 광고
        Log.e("보상형"," 광고 다봤다");
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
