package com.example.first_app_test;

import android.os.Environment;
import android.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageUtils {

    public static void createFile(File file){
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFileHeader(File file){
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "TimeStamp", "AccX", "AccY", "AccZ", "RotX", "RotY", "RotZ", "Label" };
            writer.writeNext(header);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeDataLineByLine(File dataFile, HashMap<Timestamp, List<Float>> dataMap, int label)
    {

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(dataFile, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // add data to csv
            for(Map.Entry<Timestamp, List<Float>> entry : dataMap.entrySet()){
                List<Float> ValueList = entry.getValue();
                String ts = ((Timestamp)entry.getKey()).toString().replace('.', ':');
                String[] motionData = {ts,    //timestamp
                                    ValueList.get(0).toString(), //AccX
                                    ValueList.get(1).toString(), //AccY
                                    ValueList.get(2).toString(), //AccZ
                                    ValueList.get(3).toString(), //RotX
                                    ValueList.get(4).toString(), //RotY
                                    ValueList.get(5).toString(), //RotZ
                                    ""+label};                   //Label
                writer.writeNext(motionData);
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
