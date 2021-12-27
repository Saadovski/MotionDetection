package com.example.MotionDetectionApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long timeLeftInMilli = 3000; //3 seconds
    private Button countdown;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countdown = findViewById(R.id.button);
    }

    public void startListening(View view){
        Intent intent = new Intent(this, MotionDetectionActivity.class);
        timer = new CountDownTimer(timeLeftInMilli, 10) {
            @Override
            public void onTick(long l) {
                timeLeftInMilli = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                startActivity(intent);
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

}