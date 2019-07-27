package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GPXGeneratorTest {

    @Test
    public void generate() {
        List<LocalLocation> l = new ArrayList<>();
        LocalLocation location = new LocalLocation();
        location.setAltitude(0.0);
        location.setLatitude(37.0);
        location.setLongitude(-122.0);
        location.setSecondSinceEpoch(1557070121);
        l.add(location);
        GPXGenerator gen = new GPXGenerator();
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" ><trk><trkseg><trkpt lat=\"37.0\" lon=\"-122.0\" ><ele>0.0</ele><time>2019-05-05T17:28:41.000+02:00</time></trkpt></trkseg></trk></gpx>", gen.generate(l));
    }
}