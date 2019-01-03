package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.location.Location;
import android.support.annotation.NonNull;

public class LocationWrapper
{
    public static String toString(LocalLocation location)
    {
        return location.getAltitude() + " " + location.getLatitude() + " " + location.getLongitude();
    }

    @NonNull
    public static LocalLocation fromString(String location)
    {
        String[] strings = location.split(" ");

        LocalLocation l = new LocalLocation();
        l.setAltitude(Double.parseDouble(strings[0]));
        l.setLatitude(Double.parseDouble(strings[1]));
        l.setLongitude(Double.parseDouble(strings[2]));
        return l;
    }
}
