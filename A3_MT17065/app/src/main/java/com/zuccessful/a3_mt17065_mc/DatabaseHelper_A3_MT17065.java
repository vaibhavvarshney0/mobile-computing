package com.zuccessful.a3_mt17065_mc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper_A3_MT17065 extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SensorLog.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Database_A3_MT17065.TABLE_NAME + " (" +
                    Database_A3_MT17065._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Database_A3_MT17065.TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                    Database_A3_MT17065.SENSOR + " TEXT," +
                    Database_A3_MT17065.DESCRIPTION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Database_A3_MT17065.TABLE_NAME;

    public DatabaseHelper_A3_MT17065(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

}
