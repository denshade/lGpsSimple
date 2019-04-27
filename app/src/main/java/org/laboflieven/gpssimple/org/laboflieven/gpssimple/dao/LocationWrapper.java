package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.List;

public class LocationWrapper
{
    public static String toString(LocalLocation location)
    {
        return location.getAltitude() + " " + location.getLatitude() + " " + location.getLongitude()+ " " + location.getSecondSinceEpoch();
    }

    public static String toString(List<LocalLocation> locations)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Longitude,Lattitude,Altitude,Time\n");
        for (LocalLocation location : locations)
        {
            buffer.append(location.getLongitude() + "," + location.getLatitude() + "," + location.getAltitude() + "," + location.getSecondSinceEpoch() + "\n");
        }
        return buffer.toString();
    }

    @NonNull
    public static LocalLocation fromString(String location)
    {
        String[] strings = location.split(" ");

        LocalLocation l = new LocalLocation();
        l.setAltitude(Double.parseDouble(strings[0]));
        l.setLatitude(Double.parseDouble(strings[1]));
        l.setLongitude(Double.parseDouble(strings[2]));
        l.setSecondSinceEpoch(Long.parseLong(strings[3]));
        return l;
    }
}
