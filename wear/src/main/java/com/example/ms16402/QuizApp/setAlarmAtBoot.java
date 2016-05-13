package com.example.ms16402.QuizApp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ms16402.QuizApp.menu.MenuActivity;

/**
 * Created by ms16402 on 19/04/2016.
 */
public class setAlarmAtBoot extends BroadcastReceiver {
    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(MenuActivity.PREFS_NAME_MENU, 0);
        boolean b = preferences.getBoolean(MenuActivity.PREFS_IS_NOTIFICATION_ENABLED, false);
        if (b)
        {
            Intent notificationIntent = new Intent(context, NotificationPublisher.class);
            PendingIntent pendingIntentNotification = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 10000, pendingIntentNotification);
        }
    }
}