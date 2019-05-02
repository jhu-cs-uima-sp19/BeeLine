package com.wenwanggarzagao.beeline;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.wenwanggarzagao.beeline.MainActivity;

import java.util.Objects;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.err.println("\tRECEIVED CONTEXT");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context));
        SharedPreferences.Editor sharedPrefEditor = prefs.edit();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, Landing.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Daily Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Daily Notification");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, "default");
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.queen_bee)
                .setTicker("One of your Beelines is happening soon!")
                .setContentTitle("Beeline")
                .setContentText("One of your Beelines is happening soon!")
                .setContentInfo("INFO")
                .setContentIntent(pendingI);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(new Random().nextInt(50000), b.build());
    }
}