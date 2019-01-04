package org.laboflieven.gpssimple;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao.LocalLocation;
import org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao.LocationFileDao;
import org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao.LocationWrapper;

import java.io.File;
import java.io.IOException;

public class LogGPSCoordinates extends AppCompatActivity {

    private static final long LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 100f;
    private static final String TAG = "MyLocationService";
    private static Boolean activateGPSLogging = false;
    private static LocationListener listener;



    private LocationManager locationManager;

    private static class LocationListener implements android.location.LocationListener {

        private DistanceRefresher distanceRefresher;
        private final String fileDir;

        public LocationListener(String provider, DistanceRefresher refresher, String fileDir) {

            this.distanceRefresher = refresher;
            this.fileDir = fileDir;
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            LocationFileDao.addLocation(fileDir, new LocalLocation(location));
            Log.e(TAG, "onLocationChanged: " + location);
            distanceRefresher.refreshDistance(LocationFileDao.getLocations(fileDir));
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
        listener = new LocationListener("GPS", null, getApplicationContext().getFilesDir().toString());
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
            TextView view = findViewById(R.id.elapseddistance);
            view.setText("This app needs GPS access, please allow the access and restart the app");
            return;
        }
        final DistanceRefresher refresher = new DistanceRefresher((TextView) findViewById(R.id.elapseddistance));
        listener.setDistanceRefresher(refresher);
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                activateGPSLogging = !activateGPSLogging;
                if (activateGPSLogging) {
                    LocationFileDao.clearLocations(getApplicationContext().getFilesDir().toString());
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
                refresher.refreshDistance(LocationFileDao.getLocations(getApplicationContext().getFilesDir().toString()));
            }
        });
        final FloatingActionButton share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(getApplicationContext().getFilesDir(), "location.txt");
                Log.i(TAG, fileWithinMyDir.length()  + "" );
                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/txt");
//                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getApplicationContext().getFilesDir()+"/"+ "location.txt"));
                    Log.i(TAG, "file://"+getApplicationContext().getFilesDir()+"/"+ "location.txt");

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing Locations...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, LocationWrapper.toString(LocationFileDao.getLocations(getApplicationContext().getFilesDir().toString())));
                    startActivityForResult(Intent.createChooser(intentShareFile, "Share File"), 2);
                }
            }
        });

        refreshActionToolbar(fab, null);
        refresher.refreshDistance(LocationFileDao.getLocations(getApplicationContext().getFilesDir().toString()));

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("MissingPermission")
    private void refreshActionToolbar(FloatingActionButton fab, View view) {
        TextView textCaption = findViewById(R.id.action);
        String text = "Not recording GPS information.";
        if (activateGPSLogging)
        {
            text = " Recording GPS information";
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setImageResource(android.R.drawable.ic_menu_compass);
        }
        textCaption.setText(text);
        if (view != null)
        {
            Snackbar.make(view, (activateGPSLogging ? "Started" : "Stopped") + " collecting GPS data", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
