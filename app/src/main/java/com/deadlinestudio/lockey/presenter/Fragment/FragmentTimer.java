package com.deadlinestudio.lockey.presenter.Fragment;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Controller.GrantController;
import com.deadlinestudio.lockey.presenter.Item.BasicTimer;
import com.deadlinestudio.lockey.presenter.Item.CustomScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

public class FragmentTimer extends Fragment{
    private TextView targetView, totalView;
    private Button startBtn;
    private int maxHeight, heightRemain;
    private long targetTime = 0;
    public static BasicTimer bt;
    private boolean timerOn;
    private Data tempData;
    private boolean receiverRegied;
    //CaulyAdController cac;
    GrantController gc;
    private NotificationManager mNotificationManager;
    private int prevNotificationFilter = NotificationManager.INTERRUPTION_FILTER_ALL;

    private int priColor;
    private int whiColor;
    private boolean hitmax = false;
    private int actionBarHeight = 0;

    private FrameLayout timerTopFrame, timerBottomFrame;
    private CustomScrollView timerScroll;
    private SensorManager mSensorManager = null;

    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;
    private Vibrator vibrator;
    private boolean endAlert = true;
    public MainActivity mainActivity;

    public FragmentTimer(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* View Set up*/
        final ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_timer, container,false);
        mainActivity = (MainActivity) this.getActivity();

        priColor = getResources().getColor(R.color.colorPrimary_trans);
        whiColor = getResources().getColor(R.color.colorWhite);

        targetView = (TextView)rootView.findViewById(R.id.TargetTimeText);
        totalView = (TextView)rootView.findViewById(R.id.TotalTimeText);
        startBtn = rootView.findViewById(R.id.timerStartBtn);

        timerTopFrame = rootView.findViewById(R.id.timerTopFrame);
        timerBottomFrame = rootView.findViewById(R.id.timerBottomFrame);
        timerScroll = rootView.findViewById(R.id.timerScroll);

        /**
         * @brief timer adjust by scrolls
         * */
        TypedValue tv = new TypedValue();
        if (mainActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            //Log.v("akq", String.valueOf(actionBarHeight));
        }

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        timerTopFrame.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        timerBottomFrame.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        maxHeight = height;
        heightRemain = 150;
        maxHeight -= heightRemain;

        int step = (maxHeight-actionBarHeight)/48;
        timerScroll.setScrollY(actionBarHeight);

        timerScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = timerScroll.getScrollY();

                Log.v("pro",String.valueOf(maxHeight));
                if(scrollY<=actionBarHeight){
                    timerScroll.setScrollY(actionBarHeight);
                    targetTime = 0;
                }else if(scrollY>maxHeight){
                    targetTime = 48*300000;
                    if(!hitmax){
                        timerTopFrame.setBackgroundColor(priColor);
                        hitmax = true;
                    }
                }else if(scrollY<maxHeight){
                    int progress = ((scrollY-actionBarHeight)/step);
                    Log.v("proa",String.valueOf(actionBarHeight));
                    targetTime = progress*300000;
                    if(hitmax){
                        timerTopFrame.setBackgroundColor(whiColor);
                        hitmax = false;
                    }
                }
                bt.setTargetTime(targetTime);
                updateTextview();
            }
        });

        /**scrolll done*/

        /* timer set up*/
        bt = new BasicTimer(targetTime);
        targetView.setText(bt.makeToTimeFormat(this.targetTime));
        totalView.setText(bt.makeToTimeFormat(0));
        timerOn = false;

        tempData = new Data();
        mainActivity = (MainActivity) this.getActivity();

        /*for timer service*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TIMER_BROAD_CAST_ACK");
        getActivity().registerReceiver(br,intentFilter);
        receiverRegied = true;
        final Intent sendIntent = new Intent("TIMER_BROAD_CAST_REQ");
        getActivity().sendBroadcast(sendIntent);


        /* Cauly AD */
        //cac = new CaulyAdController(mainActivity);
        //cac.makeInterstitialAd();

        /* GrantController */
        gc = new GrantController(mainActivity);

        /* do not disturb mode NotificationManager */
        mNotificationManager = (NotificationManager) mainActivity.getSystemService(NOTIFICATION_SERVICE);

        /*Using the Gyroscope & Accelometer*/
        mSensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);

        /*Using the Accelometer*/
        mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroLis = new GyroscopeListener(this);
        vibrator = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);

        /**
         * @brief timer btn listener, make the timer stop/start & load pop dialog
         * timer started by button is just for a performance to make user think timer is working
         * but real timer service is not started yet, it only starts through Gyro sensor
         **/
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerOn) {
                    // timer stop!!
                    mSensorManager.unregisterListener(mGyroLis);
                    startBtn.setText("시작");
                    timerOn = false;
                    bt.timerStop();

                    // need delay to get broadcast msg
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //achievement = (bt.getTotalTime()/bt.getTargetTime())*100;
                            tempData.setTarget_time(String.valueOf(bt.makeToTimeFormat(targetTime)));
                            tempData.setAmount(String.valueOf(bt.makeToTimeFormat(bt.getTotalTime())));
                            Log.v("saved", String.valueOf(bt.makeToTimeFormat(bt.getTotalTime())));
                            showNoticeDialog(tempData);
                        }
                    }, 500);

                    // get real time
                    Date currentTime = new Date();
                    SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
                    tempData.setDate(time.format(currentTime));

                    // reset the timer values
                    setTargetTime(0);
                    timerTopFrame.setBackgroundColor(whiColor);
                    timerScroll.setScrollY(actionBarHeight);
                    timerScroll.setEnableScrolling(true);
                    updateTextview();
                } else {
                    /// timer start!
                    if (bt.getTargetTime() == 0) {
                        Toast.makeText(getContext(), "목표시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!gc.checkAlertGrant(mNotificationManager)) {    // 방해금지모드 권한 검사
                        gc.settingAlertGrant();
                        return;
                    }

                    prevNotificationFilter = mNotificationManager.getCurrentInterruptionFilter();
                    mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);       // turn on DO NOT DISTURB MODE
                    mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
                    timerOn = true;
                    startBtn.setText("정지");
                    timerScroll.setEnableScrolling(false);
                    endAlert = true;
                    Toast.makeText(getContext(), "타이머가 시작됩니다\n휴대폰을 뒤집어주세요",
                            Toast.LENGTH_SHORT).show();
                    bt.timerStart();

                    //timer text change
                    final Handler timerViewHandler = new Handler();
                    timerViewHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            targetView.setText(bt.makeToTimeFormat(bt.getTempTarget()));
                            totalView.setText(bt.makeToTimeFormat(bt.getTotalTime() + 1000));
                            if (endAlert && bt.getTempTarget() <= 1000) {
                                Log.e("시간 다됬음","진동울리자");
                                mNotificationManager.setInterruptionFilter(prevNotificationFilter);        // turn off DO NOT DISTURB MODE
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run(){vibrator.vibrate(500);}
                                }, 500);
                                endAlert = false;
                            }

                            timerViewHandler.postDelayed(this, 1000);
                            if (!bt.getOnoff()) {
                                Log.e("시간 다됬음","설정시간 : "+bt.getTargetTime()+" 남은시간 : "+bt.getTempTarget());
                                timerViewHandler.removeMessages(0);
                                updateTextview();
                            }
                            Log.e("타이머 : ","설정시간 : "+bt.getTargetTime()+" 남은시간 : "+bt.getTempTarget());
                        }
                    });
                }
            }
        });

        return rootView;
    }

    /**
     * @brief boardcast receiver for timer service
     **/
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bt = intent.getParcelableExtra("timer");
            Log.v("actREC",bt.makeToTimeFormat(bt.getTotalTime()));
        }
    };

    /**
     * @brief unregister the boardcast receiver while activity on pause
     **/
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiverRegied){
            getActivity().unregisterReceiver(br);
            receiverRegied = false;
        }
    }

    public long getTargetTime(){
        return this.targetTime;
    }
    public void setTargetTime(long l){
        this.targetTime = l;
    }
    private void updateTextview(){
        targetView.setText(bt.makeToTimeFormat(targetTime));
        totalView.setText(bt.makeToTimeFormat(0));
    }

    /**
     * @brief dialog message with edit text for save category
     * @param time_data uses for save category value
     * @TODO add achievement value if need
     **/
    public void showNoticeDialog(final Data time_data) {
        // Create an instance of the dialog fragment and show it
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        // Get the layout inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_category, null));

        final AlertDialog dialog = builder.create();
        dialog.show();
        mNotificationManager.setInterruptionFilter(prevNotificationFilter);        // turn off DO NOT DISTURB MODE

        Button cancelSaveBtn = dialog.findViewById(R.id.cancelSaveBtn);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);
        final EditText ed = dialog.findViewById(R.id.categoryText);
        ed.setSelection(ed.getText().length());
        TextView ctText = dialog.findViewById(R.id.completeTimeText);
        ctText.setText(time_data.getAmount());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    time_data.setCategory(String.valueOf(ed.getText()));
                    dialog.dismiss();
                    if(mainActivity.getSns().equals("4") == false) {
                        NetworkTask networkTask = new NetworkTask(mainActivity.getBaseContext(), "/register-time", time_data);
                        networkTask.execute().get(1000, TimeUnit.MILLISECONDS);
                        String toastMsg = time_data.getCategory() + " " + time_data.getAmount() + " 저장됐습니다";
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    } else {
                        String toastMsg = "비회원은 저장되지 않습니다";
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }
                    //cac.runInterstitialAd();
                } catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    bt.resetTimer();
                }
            }
        });
        cancelSaveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String toastMsg = "집중시간 저장을 취소했습니다";
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @brief GyroSenSensor
     * for a gyrosensor timer run by service
     **/
    private class GyroscopeListener implements SensorEventListener {
        //Roll and Pitch
        private double pitch;

        //timestamp and dt
        private double timestamp;
        private double dt;

        // for radian -> dgree
        private double RAD2DGR = 180 / Math.PI;
        private static final float NS2S = 1.0f/1000000000.0f;
        private long millisecond = 300;
        private boolean isFirst, isReversed;
        private FragmentTimer fragmentTimer;

        public GyroscopeListener(FragmentTimer _fragmentTimer) {
            this.isFirst = true;
            this.fragmentTimer = _fragmentTimer;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            /* 각 축의 각속도 성분을 받는다. */
            double gyroY = event.values[1];

            /* 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)을 구한다.
             * dt : 센서가 현재 상태를 감지하는 시간 간격
             * NS2S : nano second -> second */
            dt = (event.timestamp - timestamp) * NS2S;
            timestamp = event.timestamp;

            /* 맨 센서 인식을 활성화 하여 처음 timestamp가 0일때는 dt값이 올바르지 않으므로 넘어간다. */
            if (dt - timestamp*NS2S != 0) {

                /* 각속도 성분을 적분 -> 회전각(pitch, roll)으로 변환.
                 * 여기까지의 pitch, roll의 단위는 '라디안'이다.
                 * SO 아래 로그 출력부분에서 멤버변수 'RAD2DGR'를 곱해주어 degree로 변환해줌.  */
                pitch = pitch + gyroY*dt;
                if (Math.abs(pitch * RAD2DGR) > 130.0) {
                    //textX.setText("           [Pitch]: 뒤집힘");
                    if (isFirst && timerOn) {
                        Log.e("진동","처음 뒤집힘요");
                        mNotificationManager.setInterruptionFilter(prevNotificationFilter);                             // turn off Do Not Disturb mode
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                vibrator.vibrate(millisecond);
                                mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);       // turn on Do Not Disturb mode
                            }
                        }, 500);
                        isFirst = false;
                        isReversed = true;
                    }
                    startBtn.setText("정지");
                    timerScroll.setEnableScrolling(false);
                } else {
                    if (isReversed && timerOn) {
                        Log.e("진동","다시 뒤집힘요");
                        mNotificationManager.setInterruptionFilter(prevNotificationFilter);                         // turn off Do Not Disturb mode
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                vibrator.vibrate(millisecond);
                            }
                        }, 500);
                        mSensorManager.unregisterListener(mGyroLis);
                        isFirst = true;
                        isReversed = false;
                        timerOn = false;
                        startBtn.setText("시작");

                        bt.timerStop();

                        // need delay to get broadcast msg
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //achievement = (bt.getTotalTime()/bt.getTargetTime())*100;
                                tempData.setTarget_time(String.valueOf(bt.makeToTimeFormat(targetTime)));
                                tempData.setAmount(String.valueOf(bt.makeToTimeFormat(bt.getTotalTime())));
                                Log.v("saved",String.valueOf(bt.makeToTimeFormat(bt.getTotalTime())));
                                showNoticeDialog(tempData);
                            }
                        },500);

                        fragmentTimer.setTargetTime(0);
                        timerTopFrame.setBackgroundColor(whiColor);
                        timerScroll.setScrollY(actionBarHeight);
                        timerScroll.setEnableScrolling(true);
                        fragmentTimer.updateTextview();
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}