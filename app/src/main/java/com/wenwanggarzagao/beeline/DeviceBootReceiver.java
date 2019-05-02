package com.wenwanggarzagao.beeline;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.wenwanggarzagao.beeline.settings.NotifData;
import com.wenwanggarzagao.beeline.settings.Storage;

import java.util.Objects;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            // on device boot complete, reset the alarm
            Storage storage = new Storage(context);
            if (Storage.SHOW_NOTIFICATIONS.get(storage)) {
                String times = Storage.NOTIFY_TIMES.get(storage);
                String[] arr = times.split(":");

                for (String _notif : arr) {
                    NotifData ndata = NotifData.fromString(_notif);

                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) ndata.id, alarmIntent, 0);

                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    if (manager != null) {
                        manager.set(AlarmManager.RTC_WAKEUP, ndata.time, pendingIntent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ndata.time, pendingIntent);
                        }
                    }
                }
            }
        }
    }
}