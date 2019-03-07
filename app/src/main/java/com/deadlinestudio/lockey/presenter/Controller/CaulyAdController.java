package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.util.Log;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;

public class CaulyAdController implements CaulyInterstitialAdListener {

    // 광고 요청을 위한 App Code
    private static final String APP_CODE = "aiTN1a2v ";
    private CaulyAdView javaAdView;
    private CaulyInterstitialAd interstitialAd;
    Activity act;

    public CaulyAdController(Activity act){
        this.act = act;
    }

    public void makeInterstitialAd(){
        // CaulyAdInfo 생성
        // 배너 광고와 동일하게 광고 요청을 설정할 수 있다.
        CaulyAdInfo interstitialAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();

        // 전면 광고 생성
        interstitialAd = new CaulyInterstitialAd();
        interstitialAd.setAdInfo(interstitialAdInfo);
        interstitialAd.setInterstialAdListener(this);
    }

    public void runInterstitialAd(){
        interstitialAd.requestInterstitialAd(act);
    }

    // CaulyInterstitialAdListener
    //	전면 광고의 경우, 광고 수신 후 자동으로 노출되지 않으므로,
    //	반드시 onReceiveInterstitialAd 메소드에서 노출 처리해 주어야 한다.
    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, boolean b) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (b == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        }
        else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }

        // 광고 노출
        caulyInterstitialAd.show();
    }

    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, int i, String s) {
        // 전면 광고 수신 실패할 경우 호출됨.
        Log.d("CaulyExample", "failed to receive interstitial AD.");
    }

    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
        interstitialAd.cancel();
    }
}
