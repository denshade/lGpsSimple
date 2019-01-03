package org.laboflieven.gpssimple.org.laboflieven.gpssimple.dao;

import android.provider.BaseColumns;

public class LocationContract {

    private LocationContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_LOCATION + " TEXT," +
                    LocationEntry.COLUMN_NAME_DATE + " TEXT)";

        /* Inner class that defines the table contents */
    public static class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DATE = "date";
    }

}
