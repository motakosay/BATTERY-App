package com.example.batteryapp.di;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StopAlarmService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmBatteryService.stopRing();   // ✅ correct way
        stopSelf();

        return START_NOT_STICKY;
    }
}
