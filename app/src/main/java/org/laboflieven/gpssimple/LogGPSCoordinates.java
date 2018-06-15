package org.laboflieven.gpssimple;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LogGPSCoordinates extends AppCompatActivity {

    private static final long LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private static final String TAG = "MyLocationService";
    private static Boolean activateGPSLogging = false;

    private static final ArrayList<Location> locations = new ArrayList<>();

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER)
    };

    private LocationManager locationManager;

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            locations.add(location);
            Log.e(TAG, "onLocationChanged: " + location);
            refreshDistance();
        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider + " " +status + " " + extras);
        }
    }

    private void refreshDistance() {
        TextView elapseddistance = findViewById(R.id.elapseddistance);

        double distanceInMeters = 0;
        for (int a = 0; a < locations.size(); a++)
        {
            Location curLocation = locations.get(a);
            if (a > 0)
            {
                Location prev = locations.get(a - 1);
                distanceInMeters += LocationCalculator.distance(prev.getLatitude(), curLocation.getLatitude(),
                        prev.getLongitude(), curLocation.getLongitude(),
                        prev.getAltitude(), curLocation.getAltitude()
                        );
            }
        }
        Log.e(TAG, "drawing locations " + locations);
        DecimalFormat df = new DecimalFormat("#.###");
        elapseddistance.setText("Elapsed distance: "  + df.format(distanceInMeters / 1000.0) + " km");
    }

    public LogGPSCoordinates()
    {
        Log.e(TAG, "Constructing");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Log.e(TAG, "Permissions ok");

        setContentView(R.layout.activity_log_gpscoordinates);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                activateGPSLogging = !activateGPSLogging;
                if (activateGPSLogging)
                {
                    locations.clear();
                    locationManager.removeUpdates(mLocationListeners[0]);//Avoid listeners being registered twice.
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            LOCATION_INTERVAL,
                            LOCATION_DISTANCE,
                            mLocationListeners[0]
                    );
                    Log.e(TAG, "Added a listener");
                } else {
                    locationManager.removeUpdates(mLocationListeners[0]);
                    Log.e(TAG, "Removed a listener");
                }
                refreshActionToolbar(fab, view);
                refreshDistance();
            }
        });
        refreshActionToolbar(fab, null);
        refreshDistance();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    private void refreshActionToolbar(FloatingActionButton fab, View view) {
        TextView textCaption = findViewById(R.id.action);
        String text = "Not recording GPS information.";
        if (activateGPSLogging)
        {
            text = " Recording GPS information";
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setImageResource(android.R.drawable.ic_media_play);
        }
        textCaption.setText(text);
        if (view != null)
        {
            Snackbar.make(view, (activateGPSLogging ? "Started" : "Stopped") + " collecting GPS data", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    protected void onPause() {
        super.onPause();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_gpscoordinates, menu);
        return true;
    }

}
