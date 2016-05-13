package com.example.ms16402.QuizApp.db_files;

import android.provider.BaseColumns;

/**
 * Created by ms16402 on 12/04/2016.
 */
public class DB_Variables {

    public static final String DATABASE_NAME = "QuizApp.db";

    public DB_Variables() {}

    //Variable for the Answer Database
    public static abstract class Answer implements BaseColumns {
        public static final String TABLE_NAME = "Answer";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_QUESTION = "Question";
        public static final String COLUMN_NAME_ANSWER = "Answer";
        public static final String COLUMN_NAME_LAST_MEDICATION_TIME = "Last_medication";
        public static final String COLUMN_NAME_LATITUDE = "Latitude";
        public static final String COLUMN_NAME_LONGITUDE = "Longitude";
        public static final String COLUMN_NAME_LOCATION_NAME = "Location_name";
        public static final String COLUMN_NAME_NUMBER_BT_DEVICES = "Number_of_BT_devices";
        public static final String COLUMN_NOISE_LEVEL = "Noise_level";
        public static final String COLUMN_LIGHT_LEVEL = "Light_level";
    }

    //Variable for the Question Database
    public static abstract class Question implements BaseColumns {
        public static final String TABLE_NAME = "Question";
        public static final String COLUMN_NAME_QUESTION = "Question";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_ANSWER_SCALE = "AnswerScale";
    }


}
