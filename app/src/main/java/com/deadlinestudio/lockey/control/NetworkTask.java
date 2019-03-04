package com.deadlinestudio.lockey.control;

import android.os.AsyncTask;
import android.util.Log;

import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;

import java.util.HashMap;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    private static final String period[] = {"day", "week", "month", "year"};
    private String url;
    private User user;
    private Data data;
    private HashMap<String, Long> analysisData;
    private RequestHttpConnection requestHttpConnection;
    private String column;
    private String new_data;
    private int mode;
    private int type;

    public NetworkTask(String url, User user, Data data) {
        this.url = url;
        this.user = user;
        this.data = data;
        this.analysisData = new HashMap<>();
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
                    result = "isUser";
                } else {
                    Log.e("check-user", "fail");
                    result = "noUser";
                }
                break;
            case "/update-user":
                // TODO : fix it
               result = requestHttpConnection.updateUser(url, user.getId(), column, new_data);
                if(result.equals("complete")) {
                    result = "complete";
                } else {
                    Log.e("update-user", "fail");
                    result = "noUser";
                }
                break;
            case "/register-time":
                result = requestHttpConnection.registerTime(url, user.getId(), data);
                Log.d("register-time", result);
                break;
            case "/classify-category":
                analysisData = requestHttpConnection.getClassfiedTime(url, user.getId(), period[mode]);
                break;
            case "/classify-week":
                analysisData = requestHttpConnection.getClassfiedTime(url, user.getId(), period[mode]);
                break;
            case "/classify-month":
                analysisData = requestHttpConnection.getClassfiedTime(url, user.getId(), period[mode]);
                break;
            case "/classify-year":
                analysisData = requestHttpConnection.getClassfiedTime(url, user.getId(), period[mode]);
                break;
            default:
                Log.e("url error", "inappropriate url: " + url);
                break;
        }
        return result;
    }

    public HashMap<String, Long> getAnalysisData() {
        return this.analysisData;
    }

    public User getUser() {
        return this.user;
    }

    public void prepareUpdate(String column, String new_data) {
        this.column = column;
        this.new_data = new_data;
    }

    public void setConfig(int mode, int type) {
        this.mode = mode;
        this.type = type;
    }
}
