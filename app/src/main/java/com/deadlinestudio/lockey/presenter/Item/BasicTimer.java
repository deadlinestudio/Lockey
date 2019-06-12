package com.deadlinestudio.lockey.presenter.Item;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

public class BasicTimer implements Runnable {
    private static BasicTimer basicTimer = new BasicTimer("0", "0");

    private long targetTime, totalTime, startTime =0, tempTarget = 0;
    private Boolean onoff = false;

    private BasicTimer(String hour, String min){
        this.targetTime = Long.valueOf(hour)*(1000 * 60 * 60) + Long.valueOf(min)*(1000*60);
    }

    public void setTimer(long targetTime){
        this.targetTime = targetTime;
        this.totalTime = 0;
    }

    /*
     * @brief time formatter
     * convert the milli sec to the HH:MM:SS format
     * can show milli sec but unable now
     * */
    public String makeToTimeFormat(){
        long t = this.targetTime;
        long millis = t % 1000;
        long second = (t / 1000) % 60;
        long minute = (t / (1000 * 60)) % 60;
        long hour = (t / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        return time;
    }
    public String makeToTimeFormat(long t){
        long millis = t % 1000;
        long second = (t / 1000) % 60;
        long minute = (t / (1000 * 60)) % 60;
        long hour = (t / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        return time;
    }

    public static BasicTimer getInstance() {
        return basicTimer;
    }

    public long getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(long targetTime) {
        this.targetTime = targetTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTempTarget() {
        return tempTarget;
    }

    public void setTempTarget(long tempTarget) {
        this.tempTarget = tempTarget;
    }

    public Boolean getOnoff() {
        return onoff;
    }

    public void setOnoff(Boolean onoff) {
        this.onoff = onoff;
    }

    public void resetTimer(){
        this.targetTime = 0;
        this.totalTime = 0;
        this.tempTarget = 0;
    }
    /*
    * @brief timer starts when start button onClicked
    * */
    @Override
    public void run(){
        startTime = SystemClock.uptimeMillis();
        totalTime = 0;
        onoff = true;
        while(true){
            if(!onoff)
                break;

            totalTime = SystemClock.uptimeMillis() - startTime;
            Log.e("timer-data", Long.toString(totalTime));
            if(targetTime>totalTime){
                tempTarget = targetTime-totalTime;
                //targetView.setText(makeToTimeFormat(tempTarget));
            }
            //totalView.setText(makeToTimeFormat(totalTime+1000));
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * @brief timer resets the target time when timer stopped
    * */
    public void timerStop(){
        onoff = false;
    }

}
