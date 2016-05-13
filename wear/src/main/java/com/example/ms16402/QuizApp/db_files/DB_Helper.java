package com.example.ms16402.QuizApp.db_files;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Created by ms16402 on 12/04/2016.
 */
public class DB_Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    //SQL command for creating Answer Table
    private static final String SQL_CREATE_TABLE_ANSWER =
            "CREATE TABLE IF NOT EXISTS " + DB_Variables.Answer.TABLE_NAME + " (" +
                    DB_Variables.Answer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DB_Variables.Answer.COLUMN_NAME_TIME + " TEXT," +
                    DB_Variables.Answer.COLUMN_NAME_QUESTION + " INTEGER," +
                    DB_Variables.Answer.COLUMN_NAME_ANSWER + " TEXT," +
                    DB_Variables.Answer.COLUMN_NAME_LAST_MEDICATION_TIME + " TEXT," +
                    DB_Variables.Answer.COLUMN_NAME_LATITUDE + " TEXT," +
                    DB_Variables.Answer.COLUMN_NAME_LONGITUDE + " TEXT," +
                    DB_Variables.Answer.COLUMN_NAME_LOCATION_NAME + " TEXT, " +
                    DB_Variables.Answer.COLUMN_NOISE_LEVEL + " TEXT, " +
                    DB_Variables.Answer.COLUMN_NAME_NUMBER_BT_DEVICES + " INTEGER, " +
                    DB_Variables.Answer.COLUMN_LIGHT_LEVEL + " INTEGER, " +
                    " FOREIGN KEY ("+DB_Variables.Answer.COLUMN_NAME_QUESTION+") REFERENCES "+DB_Variables.Question.TABLE_NAME+"("+DB_Variables.Question._ID+"));";

    //SQL command for creating Question Table
    private static final String SQL_CREATE_TABLE_QUESTION =
            "CREATE TABLE IF NOT EXISTS " + DB_Variables.Question.TABLE_NAME + " (" +
                    DB_Variables.Question._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DB_Variables.Question.COLUMN_NAME_QUESTION + " TEXT," +
                    DB_Variables.Question.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    DB_Variables.Question.COLUMN_NAME_ANSWER_SCALE + " INTEGER "  + ")";


    public DB_Helper(Context context){
        super(context, Environment.getExternalStorageDirectory()+ "/" + DB_Variables.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("envirome;emnt", Environment.getExternalStorageDirectory().toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ANSWER);
        db.execSQL(SQL_CREATE_TABLE_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DB_Variables.Answer.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_Variables.Question.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
