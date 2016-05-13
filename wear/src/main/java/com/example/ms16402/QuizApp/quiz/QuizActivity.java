package com.example.ms16402.QuizApp.quiz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Toast;

import com.example.ms16402.QuizApp.db_files.DB_Helper;
import com.example.ms16402.QuizApp.db_files.DB_Variables;
import com.example.ms16402.QuizApp.menu.MenuActivity;
import com.example.ms16402.QuizApp.SoundMeter;
import com.example.ms16402.gridproject.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class QuizActivity extends FragmentActivity implements ButtonFragment.OnClickListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    public double locationLag;
    public double locationLong;
    public double soundLevel = 0;
    public double lightLevel = 0;
    public int numberOfBtDevices = 0;

    BluetoothAdapter bluetoothAdapter;


    public static final String TAG = "tag";
    FusedLocationProviderApi locationServices;

    SensorManager sensorManager;
    Sensor lightSensor;
    SoundMeter soundmeter;
    private GoogleApiClient mGoogleApiClient;


    public GridPagerAdapterQuiz gridPagerAdapterQuiz;
    SharedPreferences preferences;
    int currentRow;

    private GridViewPager mViewPager;
    DB_Helper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ArrayList<QuestionRow> data = getQuestions();

        // Get UI references
        mViewPager = (GridViewPager) findViewById(R.id.pager);

        gridPagerAdapterQuiz = new GridPagerAdapterQuiz(getFragmentManager(), data);

        // Assigns an bluetoothAdapter to provide the content for this pager
        mViewPager.setAdapter(gridPagerAdapterQuiz);

        mViewPager.setOnPageChangeListener(listener);

        //This is to get the location of the user using the Google API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .addApi(LocationServices.API)
                .build();

        //Getting the number of bluetooth devices around
        retrieveBluetoothDevices();

        //Starting to record the sound
        //soundmeter = new SoundMeter();
        //soundmeter.start();

        //Starting the record the light level
        setLightLevel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
       //soundmeter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
        }
        mGoogleApiClient.disconnect();
        //soundmeter.stop();
        sensorManager.unregisterListener(sensorEventListener, lightSensor);
        if (bluetoothAdapter != null)
        {
            bluetoothAdapter.cancelDiscovery();
        }

        try {
            unregisterReceiver(bluetoothReceiver);
        }catch (IllegalArgumentException e){
            e.getMessage();
        }

    }

    public void setLightLevel()
    {
        sensorManager
                = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public int counter;
        public double tmp;
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                counter++;
                float currentReading = event.values[0];
                tmp = tmp + currentReading;
                lightLevel = tmp / counter;
            }

            if (counter == 50)
            {
                sensorManager.unregisterListener(sensorEventListener, lightSensor);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void setSoundLevel()
    {
        if (soundLevel == 0)
        {
            soundLevel = soundmeter.getdB();
        }
    }

    public void retrieveBluetoothDevices()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);
        if( bluetoothAdapter != null)
        {
            bluetoothAdapter.startDiscovery();
        }

    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Finding devices
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array bluetoothAdapter to show in a ListView
                numberOfBtDevices++;

            }
        }
    };

    //Getting the question from the database
    private ArrayList<QuestionRow> getQuestions(){
        ArrayList<QuestionRow> data = new ArrayList<>();
        mDbHelper = new DB_Helper(getBaseContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DB_Variables.Question.TABLE_NAME, null);

        while (c.moveToNext()){
            int id = c.getInt(c.getColumnIndex(DB_Variables.Question._ID));
            String q = c.getString(c.getColumnIndex(DB_Variables.Question.COLUMN_NAME_QUESTION));
            int s = c.getInt(c.getColumnIndex(DB_Variables.Question.COLUMN_NAME_ANSWER_SCALE));
            String d = c.getString(c.getColumnIndex(DB_Variables.Question.COLUMN_NAME_DESCRIPTION));
            if (q != null || s != 0)
            {
                data.add(new QuestionRow(id,q,d,s));
            }
        }

        return data;
    }
    private GridViewPager.OnPageChangeListener listener = new GridViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, int i1, float v, float v1, int i2, int i3) {
        }

        @Override
        public void onPageSelected(int i, int i1) {
            if (gridPagerAdapterQuiz.getFragment(i,i1) instanceof QuestionCardFragment)
            {
                currentRow = i;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /*******************Insert into database*******************/

    @Override
    public void OnClick(String id) {
        new RecordInBase(gridPagerAdapterQuiz.getQuestionRow(currentRow).getWearableListFragment().getActual_number(), currentRow).execute();
    }

    /*******************Position*******************/

    public double getLastLatitude()
    {
        if(locationServices.getLastLocation(mGoogleApiClient) != null)
            return locationServices.getLastLocation(mGoogleApiClient).getLatitude();

        return 0;
    }

    public double getLastLontitude()
    {
        if(locationServices.getLastLocation(mGoogleApiClient)
                != null)
            return locationServices.getLastLocation(mGoogleApiClient).getLongitude();

         return 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1)
                .setFastestInterval(1);

        locationServices = LocationServices.FusedLocationApi;

        locationServices
                .requestLocationUpdates(mGoogleApiClient, locationRequest, this)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.getStatus().isSuccess()) {
                            if (Log.isLoggable(TAG, Log.DEBUG)) {
                                Log.d(TAG, "Successfully requested location updates");
                            }
                        } else {
                            Log.e(TAG,
                                    "Failed in requesting location updates, "
                                            + "status code: "
                                            + status.getStatusCode()
                                            + ", message: "
                                            + status.getStatusMessage());
                        }
                    }
                });

        locationServices.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        locationLag = getLastLatitude();
        locationLong = getLastLontitude();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "connection to location client suspended");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationLag = location.getLatitude();
        locationLong = location.getLongitude();
    }

    public String getAddress(double lat, double lng)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() != 0)
            {
                Address obj = addresses.get(0);
                String c = obj.getAddressLine(0) + ", " + obj.getLocality();
                return c;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /*******************Recording Answer*******************/

    private class RecordInBase extends AsyncTask<String, Void, String> {

        int CurrentRow;
        String data;
        long newRowId;

        public RecordInBase(String d, int c) {
            data = d;
            CurrentRow = c;
        }

        @Override
        protected void onPreExecute() {
            gridPagerAdapterQuiz.getQuestionRow(CurrentRow).getButtonFragment().loadingStart();
        }

        @Override
        protected String doInBackground(String... arg0) {

            //setSoundLevel();

            mDbHelper = new DB_Helper(getBaseContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
            Date currentLocalTime = cal.getTime();
            SimpleDateFormat date = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm", Locale.US);
            // you can get seconds by adding  "...:ss" to it

            String localTime = date.format(currentLocalTime);

            ContentValues values = new ContentValues();
            values.put(DB_Variables.Answer.COLUMN_NAME_ANSWER, data);
            values.put(DB_Variables.Answer.COLUMN_NAME_QUESTION,
                    gridPagerAdapterQuiz.getQuestionRow(CurrentRow).getId());
            values.put(DB_Variables.Answer.COLUMN_NAME_TIME, localTime);
            values.put(DB_Variables.Answer.COLUMN_NAME_LATITUDE, getLastLatitude());
            values.put(DB_Variables.Answer.COLUMN_NAME_LONGITUDE, getLastLontitude());
            values.put(DB_Variables.Answer.COLUMN_NAME_LOCATION_NAME, getAddress(getLastLatitude(), getLastLontitude()));
            values.put(DB_Variables.Answer.COLUMN_NAME_NUMBER_BT_DEVICES,numberOfBtDevices );
            values.put(DB_Variables.Answer.COLUMN_NOISE_LEVEL, soundLevel);
            values.put(DB_Variables.Answer.COLUMN_LIGHT_LEVEL, lightLevel);

            SharedPreferences preferences =
                    getBaseContext().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
            int hour = preferences.getInt(MenuActivity.PREFS_LAST_HOUR_MEDICATION_TIME, -1);
            int minute = preferences.getInt(MenuActivity.PREFS_LAST_MINUTE_MEDICATION_TIME, -1);

            if (hour == -1 || minute == -1)
            {
                values.put(DB_Variables.Answer.COLUMN_NAME_LAST_MEDICATION_TIME, "");
            }
            else {
                Calendar medicationTimeCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                medicationTimeCal.set(Calendar.HOUR_OF_DAY, hour);
                medicationTimeCal.set(Calendar.MINUTE, minute);
                Date medicationDate = medicationTimeCal.getTime();

                SimpleDateFormat medicationTimeFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm", Locale.US);

                String medicationTime = medicationTimeFormat.format(medicationDate);
                values.put(DB_Variables.Answer.COLUMN_NAME_LAST_MEDICATION_TIME, medicationTime);
            }

            newRowId = db.insert(DB_Variables.Answer.TABLE_NAME, null, values);
            db.close();

            //soundmeter.stop();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            gridPagerAdapterQuiz.getQuestionRow(CurrentRow).getButtonFragment().loadingStop();

            if (newRowId == -1)
            {
                Intent intent = new Intent(getBaseContext(), ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                        ConfirmationActivity.FAILURE_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Failed to Saved");
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Answer Saved !", Toast.LENGTH_SHORT).show();
            }


            mViewPager.setCurrentItem(0,0,true);
            gridPagerAdapterQuiz.deleteQuestion(CurrentRow);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


}

