package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.location.Location;

public class LocalLocation
{
    private double altitude;
    private double longitude;
    private double latitude;
    private long secondSinceEpoch;

    public LocalLocation()
    {

    }
    public LocalLocation(Location location)
    {
        altitude = location.getAltitude();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        setSecondSinceEpoch(System.currentTimeMillis()/1000);
    }



    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Long:" + getLongitude() + ", Lat.:" + getLatitude() + ", Alt.: " + getAltitude() + ", time: " + getSecondSinceEpoch();
    }

    public long getSecondSinceEpoch() {
        return secondSinceEpoch;
    }

    public void setSecondSinceEpoch(long secondSinceEpoch) {
        this.secondSinceEpoch = secondSinceEpoch;
    }
}
