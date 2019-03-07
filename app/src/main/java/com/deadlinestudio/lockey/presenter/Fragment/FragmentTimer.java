package com.deadlinestudio.lockey.presenter.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.model.Data;
import com.deadlinestudio.lockey.model.User;
import com.deadlinestudio.lockey.presenter.Activity.MainActivity;
import com.deadlinestudio.lockey.presenter.Controller.CaulyAdController;
import com.deadlinestudio.lockey.presenter.Item.BasicTimer;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;
import com.deadlinestudio.lockey.presenter.Service.TimerService;
import com.deadlinestudio.lockey.control.NetworkTask;
import com.triggertrap.seekarc.SeekArc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FragmentTimer extends Fragment{
    private TextView targetView, totalView;
    private Button startBtn, quaterBtn, halfBtn, tripleBtn, fullBtn;
    private long targetTime = 0;
    private double achievement;
    public static BasicTimer bt;
    private boolean timerOn;
    private Data tempData;
    private SeekArc seekBar;
    private boolean receiverRegied;
    private boolean seekbarLimit = false;
    private boolean dialogClosed = true;
    CaulyAdController cac;

    //private TimerService timerService;
    //private Intent tService;
    //Using the Accelometer & Gyroscoper
    private SensorManager mSensorManager = null;

    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;
    private Vibrator vibrator;
    public MainActivity mainActivity;

    public FragmentTimer(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* View Set up*/
        final ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_timer, container,false);
        targetView = (TextView)rootView.findViewById(R.id.TargetTimeText);
        totalView = (TextView)rootView.findViewById(R.id.TotalTimeText);
        startBtn = rootView.findViewById(R.id.timerStartBtn);
        seekBar = rootView.findViewById(R.id.seekArc);
        quaterBtn = rootView.findViewById(R.id.quickQuaterBtn);
        halfBtn = rootView.findViewById(R.id.quickHalfBtn);
        tripleBtn = rootView.findViewById(R.id.quickTripleBtn);
        fullBtn = rootView.findViewById(R.id.quickFullBtn);

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
        cac = new CaulyAdController(mainActivity);
        cac.makeInterstitialAd();

        /*
        //send timer object to
        tService = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(tService, mConnection, Context.BIND_AUTO_CREATE);
*/

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
            public void onClick(View view){
                if(timerOn){
                    // timer stop!!
                    mSensorManager.unregisterListener(mGyroLis);
                    startBtn.setBackgroundResource(R.drawable.lock_icon_grey);
                    timerOn = false;
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

                    // get real time
                    Date currentTime = new Date();
                    SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
                    tempData.setDate(time.format(currentTime));

                    // reset the timer values
                    setTargetTime(0);
                    seekBar.setProgress(0);
                    updateTextview();
                    seekBar.setEnabled(true);
                }else{
                    /// timer start!
                    mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
                    startBtn.setBackgroundResource(R.drawable.lock_icon_color);
                    timerOn = true;
                    seekBar.setEnabled(false);

                    Toast.makeText(getContext(), "타이머가 시작됩니다\n휴대폰을 뒤집어주세요",
                            Toast.LENGTH_SHORT).show();
                    bt.timerStart();

                    //timer text change
                    final Handler timerViewHandler = new Handler();
                    timerViewHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            targetView.setText(bt.makeToTimeFormat(bt.getTempTarget()));
                            totalView.setText(bt.makeToTimeFormat(bt.getTotalTime()+1000));
                            timerViewHandler.postDelayed(this,1000);
                            if(!bt.getOnoff()){
                                timerViewHandler.removeMessages(0);
                                updateTextview();
                            }
                        }
                    });
                }
            }
        });
        seekBar.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if(targetTime == 0){
                    targetTime = 1;
                }else{
                    //Log.v("aaa", String.valueOf(b));
                    //Log.v("angle", String.valueOf(seekBar.getSweepAngle()));
                    int progress = i;
                    if(i == 48 && !seekbarLimit){
                        seekbarLimit = true;
                    }
                    // timer max time is 4hours
                    targetTime = progress*300000;
                    bt.setTargetTime(targetTime);
                    updateTextview();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
        });



        quaterBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetTime = 3600000; // one hour
                seekBar.setProgress(12);
                bt.setTargetTime(targetTime);
                updateTextview();
            }
        });
        halfBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetTime = 7200000; // two hour
                seekBar.setProgress(24);
                bt.setTargetTime(targetTime);
                updateTextview();
            }
        });
        tripleBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetTime = 10800000; // three hour
                seekBar.setProgress(36);
                bt.setTargetTime(targetTime);
                updateTextview();
            }
        });
        fullBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetTime = 14400000; // one hour
                seekBar.setProgress(48);
                bt.setTargetTime(targetTime);
                updateTextview();
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
                    cac.runInterstitialAd();
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
                        vibrator.vibrate(millisecond);
                        isFirst = false;
                        isReversed = true;
                    }
                } else {
                    if (isReversed && timerOn) {
                        vibrator.vibrate(millisecond);
                        mSensorManager.unregisterListener(mGyroLis);
                        isFirst = true;
                        isReversed = false;
                        timerOn = false;

                        startBtn.setBackgroundResource(R.drawable.lock_icon_grey);
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
                        seekBar.setProgress(0);
                        fragmentTimer.updateTextview();
                        seekBar.setEnabled(true);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}