package com.example.MotionDetectionApp;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
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
        if (ContextCompat.checkSelfPermission(DataPersistenceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            createCSVfile();
        else{
            ActivityCompat.requestPermissions(DataPersistenceActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createCSVfile();
        }
        else {
            returnToMain();
        }
    }

    private void createCSVfile(){
        StorageUtils su = new StorageUtils();
        File CSVFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/MotionData", "MotionData.CSV");
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


