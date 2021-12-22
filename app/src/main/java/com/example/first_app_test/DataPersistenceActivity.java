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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class DataPersistenceActivity extends AppCompatActivity {
    private static final String TAG = "DataPersistenceActivity";
    private TreeMap<Timestamp, List<Float>> sortedRot;
    private TreeMap<Timestamp, List<Float>> sortedAcc;

    int label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_persistence);
        Intent intent = getIntent();
        HashMap<Timestamp, List<Float>> rotMap = (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("Rotmap");
        sortedRot = new TreeMap<>(rotMap);
        HashMap<Timestamp, List<Float>> accMap= (HashMap<Timestamp, List<Float>>) intent.getSerializableExtra("Accmap");
        sortedAcc = new TreeMap<>(accMap);
        label = intent.getIntExtra("class", 10);
        createCSVfile();
    }

    private void createCSVfile(){
        StorageUtils su = new StorageUtils();
        File CSVFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/MotionData", "MotionData.CSV");
        if(!CSVFile.exists()) {
            su.createFile(CSVFile);
            su.addFileHeader(CSVFile);
        }
        while(sortedAcc.size() > sortedRot.size()){
            sortedAcc.remove(sortedAcc.firstKey());
        }
        while(sortedRot.size() > sortedAcc.size()){
            sortedRot.remove(sortedRot.firstKey());
        }
        su.writeDataLineByLine(CSVFile, sortedAcc, sortedRot, label);
        returnToMain();
    }

    private void returnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
