package com.kamgm.doit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TaskDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "task_doit";
    private static final String TABLE_NAME = "task";

    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_CREATED = "created";
    public static final String COLUMN_NAME_FINISHED = "finished";
    public static final String COLUMN_NAME_COMPLETED = "completed";

    private static final String[] COLUMNS = {COLUMN_NAME_ID, COLUMN_NAME_TITLE, COLUMN_NAME_CREATED,
    COLUMN_NAME_FINISHED, COLUMN_NAME_COMPLETED};

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_CREATED + " TEXT," +
                    COLUMN_NAME_FINISHED + " TEXT," +
                    COLUMN_NAME_COMPLETED + " INTEGER)";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct TaskDbHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TaskDbHelper.class.getName(),"Upgrading database from version " + oldVersion +
                " to "+ newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}