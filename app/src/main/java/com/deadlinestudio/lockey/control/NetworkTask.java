package com.deadlinestudio.lockey.control;

import android.os.AsyncTask;
import android.util.Log;

import com.deadlinestudio.lockey.control.RequestHttpConnection;
import com.deadlinestudio.lockey.model.AnalysisData;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;

import java.util.HashMap;
import java.util.Iterator;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    private String url;
    private User user;
    private Data data;
    private AnalysisData analysisData;
    private RequestHttpConnection requestHttpConnection;
    private final String[] weekdays = {"월", "화", "수", "목", "금", "토", "일"};

    public NetworkTask(String url, User user, Data data) {
        this.url = url;
        this.user = user;
        this.data = data;
        this.analysisData = new AnalysisData();
    }

    @Override
    protected String doInBackground(Void... params) {
        requestHttpConnection = new RequestHttpConnection();
        String result = "";
        switch(url) {
            case "/register-user":
                result = requestHttpConnection.registerUser(url, user);
                Log.d("register-user", result);
                break;
            case "/check-user":
               this.user = requestHttpConnection.getUser(url, user.getId());
                if(this.user.getisUser()) {
                    //Log.e("check-user", "success");
                    //Log.e("check-user", this.user.getNickname());
                    //Log.e("check-user", user.getJob());
                    result = "isUser";
                } else {
                    Log.e("check-user", "fail");
                    result = "noUser";
                }
                break;
            case "/register-time":
                result = requestHttpConnection.registerTime(url, user.getId(), data);
                Log.d("register-time", result);
                break;
            case "/classify-category":
                analysisData.setAnalysis_category(requestHttpConnection.getClassfiedTime(url, user.getId()));
                break;
            case "/classify-week":
                analysisData.setAnalysis_week(requestHttpConnection.getClassfiedTime(url, user.getId()));
                break;
            case "/classify-weekday":
                analysisData.setAnalysis_weekday(requestHttpConnection.getClassfiedTime(url, user.getId()));
                break;
            default:
                Log.e("url error", "inappropriate url: " + url);
                break;
        }
        return result;
    }

    public AnalysisData getAnalysisData() {
        Log.e("analysis test", Integer.toString(analysisData.getAnalysis_category().size()));
        return this.analysisData;
    }

    public User getUser() {
        return this.user;
    }
}
