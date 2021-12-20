package com.example.first_app_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataPersistenceActivity extends AppCompatActivity {
    private static final String TAG = "DataPersistenceActivity";
    private HashMap<Timestamp, List<Float>> dataMap;
    int label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_persistence);
        Intent intent = getIntent();
        dataMap = (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("datamap");
        label = intent.getIntExtra("class", 0);
        createCSVfile();
    }

    private void createCSVfile(){
        StorageUtils su = new StorageUtils();
        File CSVFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/MotionData", "MotionData.CSV");
        if(!CSVFile.exists()) {
            su.createFile(CSVFile);
            su.addFileHeader(CSVFile);
        }
        su.writeDataLineByLine(CSVFile, dataMap, label);
        returnToMain();
    }

    private void returnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
