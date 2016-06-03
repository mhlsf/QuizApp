package com.example.ms16402.QuizApp.menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.Toast;

import com.example.ms16402.QuizApp.NotificationPublisher;
import com.example.ms16402.QuizApp.SyncDataWithPhoneActivity;
import com.example.ms16402.QuizApp.db_files.DB_Helper;
import com.example.ms16402.QuizApp.db_files.DB_Variables;
import com.example.ms16402.QuizApp.timeSelection.GridIntervalSelectionFragment;
import com.example.ms16402.QuizApp.timeSelection.GridMedicationTimeSelectionFragment;
import com.example.ms16402.gridproject.R;
import com.example.ms16402.QuizApp.quiz.QuizActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by ms16402 on 14/04/2016.
 */
public class MenuActivity extends Activity implements WearableListView.ClickListener {

    SharedPreferences preferences;
    public static final String PREFS_NAME_MENU = "MyPrefsFile";
    public static final String PREFS_IS_NOTIFICATION_ENABLED = "isNotificationEnabled";

    public static final String PREFS_HOUR_INTERVAL = "hourInterval";
    public static final String PREFS_MINUTE_INTERVAL = "minuteInterval";

    public static final String PREFS_RANDON_HOUR_INTERVAL = "hourRandomInterval";
    public static final String PREFS_RANDOM_MINUTE_INTERVAL = "minuteRandomInterval";

    public static final String PREFS_LAST_HOUR_MEDICATION_TIME = "lastMedicationTimeHour";
    public static final String PREFS_LAST_MINUTE_MEDICATION_TIME = "lastMedicationTimeMinute";

    private WearableListView wearableListView;
    private WearableListMenuAdapter wearableListMenuAdapter;

    //Setup the different menu here and add it to String[] elements.
    String menu1 = "Start a Quiz";
    String menu2 = "Medication Time";
    String menu3 = "Set alert interval";
    String menu4 = "Start Alert";
    String menu6 = "Reset Answers";
    String menu7 = "Send File to Phone";
    String menu_separator = "-----------";
    String[] elements = {menu1, menu2, menu_separator, menu3, menu4, menu_separator, menu7};

    //Counter to activate the developer mode
    int counterClickForDeveloper;
    final int numberClickForDeveloper = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Initialisation of the ArrayList for more flexibility in the WearableListView
        ArrayList<MenuItem> list_wearableListView = new ArrayList<>();
        for(String s : elements) {
           list_wearableListView.add(new MenuItem(s));
        }


        //Determining if the alert are running or not and changing the menu
        preferences = getBaseContext().getSharedPreferences(PREFS_NAME_MENU, 0);
        boolean isAlertRunning = preferences.getBoolean(PREFS_IS_NOTIFICATION_ENABLED, false);
        if (isAlertRunning) {
            list_wearableListView.get(4).setMain("Stop Alert");

        } else {
            list_wearableListView.get(4).setMain("Start Alert");
        }
        menu4 = list_wearableListView.get(4).getMain();

        wearableListView = (WearableListView) findViewById(R.id.wearable_list_menu);
        wearableListMenuAdapter = new WearableListMenuAdapter(getBaseContext(), list_wearableListView);
        wearableListView.setAdapter(wearableListMenuAdapter);

        wearableListView.setClickListener(this);

        updateTimeMedication();
        updateTimeInterval();
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        if (menu1 == wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain()) {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }
        else if (menu4 == wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain())
        {
            preferences = getBaseContext().getSharedPreferences(PREFS_NAME_MENU, 0);
            boolean isAlertRunning = preferences.getBoolean(PREFS_IS_NOTIFICATION_ENABLED, false);
            if (isAlertRunning)
            {
                //Updating the dataset of the WearableListView
                wearableListMenuAdapter.changeDataSetMain("Start Alert", viewHolder.getAdapterPosition());
                menu4 = "Start Alert";
                //This line is for updating the dataSet of the WearableListView after we change it
                wearableListMenuAdapter.notifyDataSetChanged();
                preferences.edit().putBoolean(PREFS_IS_NOTIFICATION_ENABLED, false).apply();
                stopAlert();
                Toast.makeText(getBaseContext(), "Notification disabled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Updating the dataset of the WearableListView
                wearableListMenuAdapter.changeDataSetMain("Stop Alert", viewHolder.getAdapterPosition());
                //This line is for updating the dataSet of the WearableListView after we change it
                wearableListMenuAdapter.notifyDataSetChanged();
                menu4 = "Stop Alert";
                preferences.edit().putBoolean(PREFS_IS_NOTIFICATION_ENABLED, true).apply();
                startAlert();
                Toast.makeText(getBaseContext(), "Notification enabled", Toast.LENGTH_SHORT).show();
            }
        }
        else if (menu6 == wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain()) {
            DelayedConfirmationFragment delayedConfirmationFragment =
                    DelayedConfirmationFragment.newInstance("deleting_database","Are you sure ?",
                            "Deleting Database...", "Deleting...", 10000);
            DelayedConfirmationFragment.OnDataPass dataPass = new DelayedConfirmationFragment.OnDataPass() {
                @Override
                public void onDataPass(String id) {
                    resetDatabase();
                }
            };
            delayedConfirmationFragment.setDataPasser(dataPass);
            delayedConfirmationFragment.setEnterTransition(new Slide(Gravity.BOTTOM));
            delayedConfirmationFragment.setExitTransition(new Slide(Gravity.BOTTOM));
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(android.R.id.content, delayedConfirmationFragment).commit();
        }
        else if(menu3 ==
                wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain())
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            GridIntervalSelectionFragment gridIntervalSelectionFragment =
                    GridIntervalSelectionFragment.newInstance();
            gridIntervalSelectionFragment.setEnterTransition(new Slide(Gravity.RIGHT));
            gridIntervalSelectionFragment.setExitTransition(new Fade(Fade.OUT));
            ft.add(android.R.id.content, gridIntervalSelectionFragment,
                    "gridIntervalSelectionFragment").commit();
        }
        else if(menu2 ==
                wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain())
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            GridMedicationTimeSelectionFragment gridMedicationTimeSelection =
                    GridMedicationTimeSelectionFragment.newInstance();
            gridMedicationTimeSelection.setEnterTransition(new Slide(Gravity.RIGHT));
            gridMedicationTimeSelection.setExitTransition(new Fade(Fade.OUT));
            ft.add(android.R.id.content, gridMedicationTimeSelection,
                    "gridMedicationTimeSelection").commit();
        }
        else if(menu_separator ==
                wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain())
        {
            counterClickForDeveloper++;
            if (counterClickForDeveloper > numberClickForDeveloper)
            {
                if (wearableListMenuAdapter.contains(menu6))
                {
                    wearableListMenuAdapter.remove(menu6);
                    wearableListMenuAdapter.notifyDataSetChanged();
                }
                else
                {
                    wearableListMenuAdapter.getDataSet().add(new MenuItem(menu6));
                    wearableListMenuAdapter.notifyDataSetChanged();
                    Toast.makeText(getBaseContext(), "Developer mode", Toast.LENGTH_SHORT).show();
                }
                counterClickForDeveloper = 0;
            }
        }
        else if(menu7 == wearableListMenuAdapter.getDataSet().get(viewHolder.getAdapterPosition()).getMain())
        {
            Intent intent = new Intent(this, SyncDataWithPhoneActivity.class);
            startActivity(intent);
        }
    }

    private void startAlert() {
        //Getting the alert time set by the user
        SharedPreferences preferences = getBaseContext().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
        int hour = preferences.getInt(MenuActivity.PREFS_HOUR_INTERVAL, 0);
        int minute = preferences.getInt(MenuActivity.PREFS_MINUTE_INTERVAL, 0);

        //Calculating this time in Milliseconds
        long timeAlarm = TimeUnit.HOURS.toMillis(hour)+ TimeUnit.MINUTES.toMillis(minute);

        //Starting the Alert
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntentNotification = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeAlarm, pendingIntentNotification);
    }

    private void stopAlert() {
        //Stopping the Alert
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntentNotification = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntentNotification);
    }

    public void updateTimeMedication(){
        SharedPreferences preferences =
                getBaseContext().getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);

        //Showing last medication time in the menu if there is one
        int hour = preferences.getInt(MenuActivity.PREFS_LAST_HOUR_MEDICATION_TIME, -1);
        int minute = preferences.getInt(MenuActivity.PREFS_LAST_MINUTE_MEDICATION_TIME, -1);
        if (hour != -1 || minute != -1)
        {
            String h;
            String m;
            if (hour < 10){
                 h = "0"+hour;
            }else{
                 h = Integer.toString(hour);
            }

            if (minute < 10)
            {
                m = "0" + minute;
            }
            else {
                m = Integer.toString(minute);
            }
            wearableListMenuAdapter.changeDataSetInfo("Last Medication: " + h + "."
                    + m, 1);
        }
        else{
            wearableListMenuAdapter.changeDataSetInfo("No medication Time Set", 1);
        }

        wearableListMenuAdapter.notifyDataSetChanged();
    }

    public void updateTimeInterval(){
        //Showing interval time in the menu if there is one
        int hour = preferences.getInt(MenuActivity.PREFS_HOUR_INTERVAL, -1);
        int minute = preferences.getInt(MenuActivity.PREFS_MINUTE_INTERVAL, -1);
        if (hour != -1 || minute != -1)
        {
            String h;
            String m;
            if (hour < 10){
                h = "0"+hour;
            }else{
                h = Integer.toString(hour);
            }

            if (minute < 10)
            {
                m = "0" + minute;
            }
            else {
                m = Integer.toString(minute);
            }
            wearableListMenuAdapter.changeDataSetInfo("Interval Set: " + h + "."
                    + m , 3);
        }

        wearableListMenuAdapter.notifyDataSetChanged();
    }

    public void resetDatabase() {
        DB_Helper mDbHelper = new DB_Helper(getBaseContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(db);
        db.execSQL("DELETE FROM "+ DB_Variables.Answer.TABLE_NAME);
        db.close();
        Toast.makeText(getBaseContext(), "Database deleted", Toast.LENGTH_SHORT).show();
    }

    //Useless but cannot remove it
    @Override
    public void onTopEmptyRegionClick() {
    }



}
