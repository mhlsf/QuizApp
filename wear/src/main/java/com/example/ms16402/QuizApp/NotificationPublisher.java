package com.example.ms16402.QuizApp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.ms16402.gridproject.R;
import com.example.ms16402.QuizApp.quiz.QuizActivity;

public class NotificationPublisher extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        int notificationId = 1;

        Intent viewIntent = new Intent(context, QuizActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(context, 0, viewIntent, 0);

        Bitmap background = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background3);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Question Time !")
                        .setContentText("Swipe left to open")
                        .extend(new NotificationCompat.WearableExtender().setBackground(background))
                        .setOngoing(true)
                        .setContentIntent(viewPendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibrationPattern = { 0, 500, 0, 500 };
        vibrator.vibrate(vibrationPattern, -1);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}