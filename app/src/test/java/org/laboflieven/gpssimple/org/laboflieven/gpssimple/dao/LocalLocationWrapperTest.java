package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.location.Location;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocalLocationWrapperTest {

    @Test
    public void toStringT() {
        LocalLocation l = new LocalLocation();
        l.setAltitude(10);
        l.setLongitude(10.01);
        l.setLatitude(10.02);
        String location = LocationWrapper.toString(l);
        LocalLocation l2 = LocationWrapper.fromString(location);
        assertEquals(l.getAltitude(), l2.getAltitude(), 0.1);
        assertEquals(l.getLatitude(), l2.getLatitude(), 0.1);
        assertEquals(l.getLongitude(), l2.getLongitude(), 0.1);
    }

}