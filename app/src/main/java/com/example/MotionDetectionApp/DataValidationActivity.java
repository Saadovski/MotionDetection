package com.example.MotionDetectionApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class DataValidationActivity extends AppCompatActivity {
    private static final String TAG = "DataValidationActivity";
    private HashMap<Timestamp, List<Float>> rotMap;
    private HashMap<Timestamp, List<Float>> accMap;
    private Button validation;
    private Button annulation;
    private RadioGroup radiogroup;
    private RadioGroup radiogroup2;
    private int choiceId;

    RadioGroup.OnCheckedChangeListener listener = (new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if(group.getCheckedRadioButtonId() != -1) {
                validation.setEnabled(true);
                choiceId = checkedId;
                radiogroup2.setOnCheckedChangeListener(null);
                radiogroup2.clearCheck();
                radiogroup2.setOnCheckedChangeListener(listener2);
            }
        }
    });
    RadioGroup.OnCheckedChangeListener listener2 = (new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if(group.getCheckedRadioButtonId() != -1) {
                validation.setEnabled(true);
                choiceId = checkedId;
                radiogroup.setOnCheckedChangeListener(null);
                radiogroup.clearCheck();
                radiogroup.setOnCheckedChangeListener(listener);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_validation);
        Intent intent = getIntent();
        rotMap = (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("Rotmap");
        accMap = (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("Accmap");
        radiogroup = findViewById(R.id.RadioGroup);
        radiogroup2 = findViewById(R.id.RadioGroup2);

        RadioGroup.OnCheckedChangeListener radioGroup1CheckedChangeListener;

        radiogroup.setOnCheckedChangeListener(listener);
        radiogroup2.setOnCheckedChangeListener(listener2);

        validation = findViewById(R.id.button3);
        annulation = findViewById(R.id.button2);
    }

    public void annulerEnregistrement(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void validerChoix(View view){
        Intent intent = new Intent(this, DataPersistenceActivity.class);
        intent.putExtra("Accmap", (Serializable) accMap);
        intent.putExtra("Rotmap", (Serializable) rotMap);
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
            case R.id.radioButton6:
                intent.putExtra("class", 5);
                break;
            case R.id.radioButton7:
                intent.putExtra("class", 6);
                break;
            case R.id.radioButton8:
                intent.putExtra("class", 7);
                break;
            case R.id.radioButton9:
                intent.putExtra("class", 8);
                break;
            case R.id.radioButton10:
                intent.putExtra("class", 9);
                break;
        }
        startActivity(intent);
    }
}