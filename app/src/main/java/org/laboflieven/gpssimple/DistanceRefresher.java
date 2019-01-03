package org.laboflieven.gpssimple;

import android.util.Log;
import android.widget.TextView;

import org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao.LocalLocation;

import java.text.DecimalFormat;
import java.util.List;

public class DistanceRefresher
{

    private TextView distanceField;


    public DistanceRefresher(TextView distanceField)
    {
        this.distanceField = distanceField;
    }

    public void refreshDistance(List<LocalLocation> locations) {
        double distanceInMeters = 0;
        for (int a = 0; a < locations.size(); a++)
        {
            LocalLocation curLocation = locations.get(a);
            if (a > 0)
            {
                LocalLocation prev = locations.get(a - 1);
                distanceInMeters += LocationCalculator.distance(prev.getLatitude(), curLocation.getLatitude(),
                        prev.getLongitude(), curLocation.getLongitude(),
                        prev.getAltitude(), curLocation.getAltitude()
                );
            }
        }
        Log.e("DistanceRefresher", "drawing locations " + locations);
        DecimalFormat df = new DecimalFormat("#.###");
        if (distanceField != null)
        {
            distanceField.setText("Elapsed distance: "  + df.format(distanceInMeters / 1000.0) + " km");
        }
    }
}
