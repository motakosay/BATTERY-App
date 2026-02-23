package com.example.batteryapp.di;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerConnectionReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {

            // Stop alarm if unplugged
            context.startService(new Intent(context, StopAlarmService.class));

            // Restart monitoring
            context.startForegroundService(new Intent(context, BatteryService.class));

            // Stop temporary power service
            context.stopService(new Intent(context, PowerConnectionService.class));
        }
    }
}
