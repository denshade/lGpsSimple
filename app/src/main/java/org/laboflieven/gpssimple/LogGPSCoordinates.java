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
    private static final float LOCATION_DISTANCE = 100f;
    private static final String TAG = "MyLocationService";
    private static Boolean activateGPSLogging = false;
    private static LocationListener listener = new LocationListener("GPS", null);
    private static final ArrayList<Location> locations = new ArrayList<>();



    private LocationManager locationManager;

    private static class LocationListener implements android.location.LocationListener {

        private DistanceRefresher distanceRefresher;

        public LocationListener(String provider, DistanceRefresher refresher) {
            this.distanceRefresher = refresher;
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            locations.add(location);
            Log.e(TAG, "onLocationChanged: " + location);
            distanceRefresher.refreshDistance(locations);
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

        public void setDistanceRefresher(DistanceRefresher distanceRefresher) {
            this.distanceRefresher = distanceRefresher;
        }
    }

    /**
     * Lessons learned.
     * LogGPSCoordinates is destroyed and recreated every time the app is rotated.
     * This is savage on the locations we store.
     * The location listener references an existing control. This is a problem. We need to
     * update the location listener with the reference of the new control.
     */
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
        final DistanceRefresher refresher = new DistanceRefresher((TextView) findViewById(R.id.elapseddistance));
        listener.setDistanceRefresher(refresher);
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                activateGPSLogging = !activateGPSLogging;
                if (activateGPSLogging) {
                    locations.clear();
                    locationManager.removeUpdates(listener);//Avoid listeners being registered twice.
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            LOCATION_INTERVAL,
                            LOCATION_DISTANCE,
                            listener
                    );
                    Log.e(TAG, "Added a listener");
                } else {
                    locationManager.removeUpdates(listener);
                    Log.e(TAG, "Removed a listener");
                }
                refreshActionToolbar(fab, view);
                refresher.refreshDistance(locations);
            }
        });
        refreshActionToolbar(fab, null);
        refresher.refreshDistance(locations);
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

}
