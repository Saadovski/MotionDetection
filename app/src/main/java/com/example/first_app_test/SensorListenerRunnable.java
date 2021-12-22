package com.example.first_app_test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorListenerRunnable implements Runnable, SensorEventListener {
    private final SensorManager sensormanager;
    private final Sensor sensor;

    public SensorListenerRunnable(int sensortype, SensorManager sm){
       sensormanager = sm;
       sensor = sensormanager.getDefaultSensor(sensortype);
       sensormanager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public abstract void run();

    @Override
    public abstract void onSensorChanged(SensorEvent sensorEvent);

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
