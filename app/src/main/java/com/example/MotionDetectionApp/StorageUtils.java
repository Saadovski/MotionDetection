package com.example.MotionDetectionApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
            String[] header = { "AccTimeStamp", "AccX", "AccY", "AccZ", "rotTimeStamp", "RotX", "RotY", "RotZ", "Label" };
            writer.writeNext(header);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeDataLineByLine(File dataFile, TreeMap<Timestamp, List<Float>> accMap, TreeMap<Timestamp, List<Float>> rotMap, int label)
    {

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(dataFile, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            Set<Map.Entry<Timestamp, List<Float>>> accEntries = accMap.entrySet();
            Set<Map.Entry<Timestamp, List<Float>>> rotEntries = rotMap.entrySet();
            Iterator<Map.Entry<Timestamp, List<Float>>> accIterator = accEntries.iterator();
            Iterator<Map.Entry<Timestamp, List<Float>>> rotIterator = rotEntries.iterator();

            // add data to csv
            while(accIterator.hasNext() && rotIterator.hasNext()){
                //retrieving the entries
                Map.Entry<Timestamp, List<Float>> accEntry = accIterator.next();
                Map.Entry<Timestamp, List<Float>> rotEntry = rotIterator.next();

                //getting the accelerometer timestamp and values
                List<Float> accList = accEntry.getValue();
                String accTS = ((Timestamp)accEntry.getKey()).toString().replace('.', ':');

                //getting the gyroscope's timestamp and values
                List<Float> rotList = rotEntry.getValue();
                String rotTS = ((Timestamp)rotEntry.getKey()).toString().replace('.', ':');

                String[] motionData = {accTS,    //AccTimestamp
                                    accList.get(0).toString(), //AccX
                                    accList.get(1).toString(), //AccY
                                    accList.get(2).toString(), //AccZ
                                    rotTS,                     //RotTimestamp
                                    rotList.get(0).toString(), //RotX
                                    rotList.get(1).toString(), //RotY
                                    rotList.get(2).toString(), //RotZ
                                    ""+label};                 //Label
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
