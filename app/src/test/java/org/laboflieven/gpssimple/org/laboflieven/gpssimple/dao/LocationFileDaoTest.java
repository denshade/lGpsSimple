package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationFileDaoTest {

    @Test
    public void getLocations()
    {
        String tmp = System.getProperty("java.io.tmpdir");
        LocationFileDao.clearLocations(tmp);
        assertEquals(0,LocationFileDao.getLocations(tmp).size());
        LocationFileDao.addLocation(tmp, new LocalLocation());
        assertEquals(1,LocationFileDao.getLocations(tmp).size());
        LocationFileDao.clearLocations(tmp);
        assertEquals(0,LocationFileDao.getLocations(tmp).size());
    }
}