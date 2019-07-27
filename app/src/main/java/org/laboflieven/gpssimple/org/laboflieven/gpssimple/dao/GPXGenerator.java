package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GPXGenerator
{
    public String generate(List<LocalLocation> locations)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" >");
        buffer.append("<trk>");
        buffer.append("<trkseg>");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        for (LocalLocation l : locations)
        {
            buffer.append("<trkpt ");
            buffer.append("lat=\"");
            buffer.append(l.getLatitude());
            buffer.append("\" ");
            buffer.append("lon=\"");
            buffer.append(l.getLongitude());
            buffer.append("\" >");
            buffer.append("<ele>");
            buffer.append(l.getAltitude());
            buffer.append("</ele>");
            buffer.append("<time>");
            Date d = new Date(l.getSecondSinceEpoch()*1000);
            buffer.append(sdf.format(d));
            buffer.append("</time>");
            buffer.append("</trkpt>");
        }
        buffer.append("</trkseg>");
        buffer.append("</trk>");
        buffer.append("</gpx>");
        return buffer.toString();
    }
}
