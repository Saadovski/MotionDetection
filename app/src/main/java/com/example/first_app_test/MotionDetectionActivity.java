package com.example.first_app_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MotionDetectionActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MotionDetectionActivity";

    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor gyro;

    private long timeLeftInMilli = 7000; //7 seconds
    private TextView countdown;
    private CountDownTimer timer;

    private HashMap<Timestamp, List<Float>> finalResultMap; //will contain values of motion + timestamp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_detection);

        finalResultMap = new HashMap<>();
        countdown = findViewById(R.id.textView4);
        countdown.setText("07:00");

        Log.d(TAG, "onCreate: Initializing Sensor Services");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        startListening();
    }

    public void startListening(){
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_GAME);
        SensorEventListener listener = this;
        timer = new CountDownTimer(timeLeftInMilli, 10) {
            @Override
            public void onTick(long l) {
                timeLeftInMilli = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                sensorManager.unregisterListener(listener); //we unregister the listener to stop recording the movements
                Log.d(TAG, "onFinish: " + finalResultMap.size());
                int countAcc = 0 ,countRot = 0;
                for(Map.Entry<Timestamp, List<Float>> entry : finalResultMap.entrySet()){
                    if(entry.getValue().get(0) == 0 && entry.getValue().get(1) == 0 && entry.getValue().get(2) == 0) countRot++;
                    else if(entry.getValue().get(3) == 0 && entry.getValue().get(4) == 0 && entry.getValue().get(5) == 0) countAcc++;
                }
                Log.d(TAG, "onFinish: countAcc " + countAcc);
                Log.d(TAG, "onFinish: countRot " + countRot);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor s = sensorEvent.sensor;
        float[] AccArray = new float[3];
        float[] RotArray = new float[3];

        if(s.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
            AccArray = sensorEvent.values;

        if (s.getType() == Sensor.TYPE_GYROSCOPE)
            RotArray = sensorEvent.values;
        Log.d(TAG, "onSensorChanged: \nAccX: " + AccArray[0] + "\nAccY: "+ AccArray[1] +"\nAccZ: "+ AccArray[2] +"\n" + "RotX: " + RotArray[0] +"\nRotY: "+ RotArray[1] +"\nRotZ: "+ RotArray[2] +"\n");
        List<Float> result = Arrays.asList(AccArray[0], AccArray[1], AccArray[2], RotArray[0], RotArray[1], RotArray[2]);
        finalResultMap.put(new Timestamp(System.currentTimeMillis()), result);
    }

    public void finishListening(){
        Intent intent = new Intent(this, DataValidationActivity.class);
        intent.putExtra("datamap", (Serializable) finalResultMap);
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}