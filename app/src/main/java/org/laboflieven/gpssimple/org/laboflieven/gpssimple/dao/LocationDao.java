package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationDao extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "locations.db";

    SQLiteDatabase db = getWritableDatabase();

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationContract.LocationEntry.TABLE_NAME + " (" +
                    LocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationContract.LocationEntry.COLUMN_NAME_DATE + " DATE," +
                    LocationContract.LocationEntry.COLUMN_NAME_LOCATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationContract.LocationEntry.TABLE_NAME;

    public LocationDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addLocation(Location location)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String date = sdf.format(new Date());
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_DATE, date);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LOCATION, location.getAltitude() +" " + location.getLatitude() + " " + location.getLongitude());
        db.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
    }

    public List<LocalLocation> getLocations()
    {
        Cursor cursor = db.query(
                LocationContract.LocationEntry.TABLE_NAME,   // The table to query
                new String[] {LocationContract.LocationEntry.COLUMN_NAME_LOCATION} ,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                 LocationContract.LocationEntry.COLUMN_NAME_DATE               // The sort order
        );
        ArrayList<LocalLocation> locations = new ArrayList<>();
        while(cursor.moveToNext())
        {
            LocalLocation location = LocationWrapper.fromString(cursor.getString(1));
            locations.add(location);
        }
        cursor.close();
        return locations;
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clearLocations() {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}
