package com.example.first_app_test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MotionDetectionActivity extends AppCompatActivity {
    private static final String TAG = "MotionDetectionActivity";

    private SensorManager sensorManager;

    private long timeLeftInMilli = 7000; //7 seconds
    private TextView countdown;
    private CountDownTimer timer;

    private HashMap<Timestamp, List<Float>> AccList;
    private HashMap<Timestamp, List<Float>> RotList;
    private int counter = 0;

    private SensorListenerRunnable accel;
    private SensorListenerRunnable gyro;

    private boolean stopListening = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_detection);

        AccList = new HashMap<>();
        RotList = new HashMap<>();

        countdown = findViewById(R.id.textView4);
        countdown.setText("07:00");

        Log.d(TAG, "onCreate: Initializing Sensor Services");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accel = new SensorListenerRunnable(Sensor.TYPE_LINEAR_ACCELERATION, sensorManager) {
            @Override
            public void run() {
                while (!stopListening){}
                return;
            }

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                AccList.put(new Timestamp(System.currentTimeMillis()), Arrays.asList(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
            }
        };
        gyro = new SensorListenerRunnable(Sensor.TYPE_GYROSCOPE, sensorManager) {
            @Override
            public void run() {
                while (!stopListening){}
                return;
            }

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                RotList.put(new Timestamp(System.currentTimeMillis()), Arrays.asList(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
            }
        };

        startListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startListening(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE)); //vibration
        }

        Thread accThread = new Thread(accel);
        Thread rotThread = new Thread(gyro);
        rotThread.start();
        accThread.start();


        timer = new CountDownTimer(timeLeftInMilli, 10) {
            @Override
            public void onTick(long l) {
                timeLeftInMilli = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                stopListening = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                int countRot = RotList.size() ,countAcc = AccList.size();
                Log.d(TAG, "onFinish: countAcc " + countAcc);
                Log.d(TAG, "onFinish: countRot " + countRot);
                TreeMap<Timestamp, List<Float>> sortedAcc = new TreeMap<>(AccList);
                TreeMap<Timestamp, List<Float>> sortedRot = new TreeMap<>(RotList);
                System.out.println(sortedAcc);
                System.out.println(sortedRot);
                System.out.println(sortedAcc.firstEntry());
                System.out.println(sortedRot.firstEntry());
                int sameTS = 0;
                for(Timestamp ts : sortedAcc.keySet()){
                    if(sortedRot.containsKey(ts)) sameTS++;
                }
                System.out.println(sameTS);


                finishListening();
            }
        }.start();
    }

    public void updateTimer(){
        int seconds = (int) (timeLeftInMilli/1000);
        int centiseconds = (int) (timeLeftInMilli % 1000 / 10);

        String timeLeftText = "0" + seconds;
        timeLeftText += ":";
        if (centiseconds<10) timeLeftText += "0";
        timeLeftText += centiseconds;

        countdown.setText(timeLeftText);
    }

    public void finishListening(){
        Intent intent = new Intent(this, DataValidationActivity.class);
        intent.putExtra("Rotmap", (Serializable) RotList);
        intent.putExtra("Accmap", (Serializable) AccList);
        startActivity(intent);
    }
}