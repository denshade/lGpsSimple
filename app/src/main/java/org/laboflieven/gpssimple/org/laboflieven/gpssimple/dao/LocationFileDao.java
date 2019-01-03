package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LocationFileDao  {
    // If you change the database schema, you must increment the database version.


    public LocationFileDao() {

    }

    public static void addLocation(String locationDir, LocalLocation location)
    {
        try {
            FileWriter fw = new FileWriter(new File(locationDir, "location.txt"), true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(LocationWrapper.toString(location));
            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<LocalLocation> getLocations(String locationDir) {
        List<LocalLocation> localLocations = new ArrayList<>();
        File f = new File(locationDir, "location.txt");
        if (f.exists())
        {
            try{
                FileReader reader = new FileReader(f);
                BufferedReader br = new BufferedReader(reader);
                String currentLine = br.readLine();
                while (currentLine != null) {
                    localLocations.add(LocationWrapper.fromString(currentLine));
                    currentLine = br.readLine();
                }
                br.close();
                reader.close();
            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return localLocations;
    }


    public static void clearLocations(String locationDir)
    {
        File f = new File(locationDir, "location.txt");
        f.delete();
    }
}
