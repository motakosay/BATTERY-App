package com.example.batteryapp.di;

import android.app.*;
import android.content.*;
import android.os.Build;
import android.os.IBinder;

public class BatteryService extends Service {

    BatteryReceiver r;

    public int onStartCommand(Intent i,int f,int id){

        String channelId = "battery_monitor";

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    channelId,
                    "Battery Monitor",
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
                .setContentTitle("Battery Monitor Running")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .build();

        startForeground(1,n);

        r = new BatteryReceiver();
        registerReceiver(r,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return START_STICKY;
    }

    public void onDestroy(){
        try{ unregisterReceiver(r); }catch(Exception ignored){}
    }

    public IBinder onBind(Intent i){ return null; }
}
