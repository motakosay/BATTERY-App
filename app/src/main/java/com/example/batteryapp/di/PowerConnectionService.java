package com.example.batteryapp.di;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.IBinder;

public class PowerConnectionService extends Service {

    private BroadcastReceiver receiver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();

        if (receiver == null) {
            receiver = new PowerConnectionReceiver();

            IntentFilter f = new IntentFilter();
            f.addAction(Intent.ACTION_POWER_CONNECTED);
            f.addAction(Intent.ACTION_POWER_DISCONNECTED);

            registerReceiver(receiver, f);
        }

        return START_STICKY;
    }

    private void createNotification() {

        String channelId = "power_monitor";

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    channelId,
                    "Power Monitor",
                    NotificationManager.IMPORTANCE_LOW);

            NotificationManager nm =
                    (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            if (nm != null)
                nm.createNotificationChannel(ch);
        }

        Notification.Builder b;

        if (Build.VERSION.SDK_INT >= 26)
            b = new Notification.Builder(this, channelId);
        else
            b = new Notification.Builder(this);

        Notification n = b
                .setContentTitle("Power connection monitoring")
                .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
                .build();

        startForeground(3, n);
    }

    public void onDestroy() {
        try {
            if (receiver != null)
                unregisterReceiver(receiver);
        } catch (Throwable ignored) {}

        super.onDestroy();
    }
}
