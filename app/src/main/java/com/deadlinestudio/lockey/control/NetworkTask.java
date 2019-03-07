package com.deadlinestudio.lockey.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;

import java.util.HashMap;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    private static final String period[] = {"day", "week", "month", "year"};
    private Context cont;
    private String url;
    private User user;
    private Data data;
    private HashMap<String, Long> analysisData;
    private RequestHttpConnection requestHttpConnection;
    private int mode;

    public NetworkTask(Context cont, String url, Data data) {
        this.cont = cont;
        this.url = url;
        this.user = User.getInstance();
        this.data = data;
        this.analysisData = null;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            requestHttpConnection = new RequestHttpConnection();
            switch (url) {
                case "/register-user":
                    requestHttpConnection.registerUser(url, user);
                    break;
                case "/check-user":
                    requestHttpConnection.getUser(url, user.getId());
                    break;
                case "/update-user":
                    requestHttpConnection.updateUser(url, user);
                    break;
                case "/leave-user":
                    requestHttpConnection.updateUser(url, user);
                    break;
                case "/register-time":
                    requestHttpConnection.registerTime(url, user.getId(), data);
                    break;
                case "/reset-time":
                    requestHttpConnection.resetTime(url, user.getId());
                    break;
                case "/get-time":
                    analysisData = requestHttpConnection.getClassfiedTime(url, user.getId(), period[mode]);
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
        } catch(Exception e) {
            return "fail";
        }
        return "success";
    }

    public HashMap<String, Long> getAnalysisData() {
        return this.analysisData;
    }

    public User getUser() {
        return this.user;
    }

    public void setConfig(int mode) {
        this.mode = mode;
    }
}
