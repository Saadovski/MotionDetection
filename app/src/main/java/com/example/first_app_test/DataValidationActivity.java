package com.example.first_app_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataValidationActivity extends AppCompatActivity {
    private static final String TAG = "DataValidationActivity";
    private HashMap<Timestamp, List<Float>> dataMap;
    private Button validation;
    private Button annulation;
    private RadioGroup radiogroup;
    private int choiceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_validation);
        Intent intent = getIntent();
        dataMap = (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("datamap");

        radiogroup = findViewById(R.id.RadioGroup);
        validation = findViewById(R.id.button3);
        annulation = findViewById(R.id.button2);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                validation.setEnabled(true);
                choiceId = checkedId;
            }
        });
    }

    public void annulerEnregistrement(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void validerChoix(View view){
        Intent intent = new Intent(this, DataPersistenceActivity.class);
        intent.putExtra("datamap", (Serializable) dataMap);
        switch (choiceId){
            case R.id.radioButton:
                intent.putExtra("class", 0);
                break;
            case R.id.radioButton2:
                intent.putExtra("class", 1);
                break;
            case R.id.radioButton3:
                intent.putExtra("class", 2);
                break;
            case R.id.radioButton4:
                intent.putExtra("class", 3);
                break;
            case R.id.radioButton5:
                intent.putExtra("class", 4);
                break;
        }
        startActivity(intent);
    }
}