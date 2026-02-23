package com.example.batteryapp.di;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;

public class AlarmBatteryService extends Service {

    private static Ringtone ringtone;
    private static Vibrator vibrator;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();

        try {
            if (ringtone == null) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (uri == null)
                    uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                ringtone.setLooping(true);
                ringtone.play();
            }
        } catch (Throwable ignored) {}

        try {
            vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null)
                vibrator.vibrate(1000);
        } catch (Throwable ignored) {}

        return START_STICKY;
    }

    private void createNotification() {

        String channelId = "alarm_battery";

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    channelId,
                    "Battery Alarm",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager nm =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm != null)
                nm.createNotificationChannel(ch);
        }

        Notification.Builder b;

        if (Build.VERSION.SDK_INT >= 26)
            b = new Notification.Builder(this, channelId);
        else
            b = new Notification.Builder(this);

        Notification n = b
                .setContentTitle("Battery Alarm")
                .setContentText("Battery reached limit")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .build();

        startForeground(2, n);
    }

    public static void stopRing() {
        try {
            if (ringtone != null) {
                ringtone.stop();
                ringtone = null;
            }
        } catch (Throwable ignored) {}

        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Throwable ignored) {}
    }

    public void onDestroy() {
        stopRing();
        super.onDestroy();
    }
}
