package com.example.batteryapp.di;

import android.content.*;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

public class BatteryReceiver extends BroadcastReceiver {

    public void onReceive(Context c, Intent i){

        int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int status = i.getIntExtra(BatteryManager.EXTRA_STATUS,-1);

        boolean charging = status==BatteryManager.BATTERY_STATUS_CHARGING ||
                status==BatteryManager.BATTERY_STATUS_FULL;

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(c);

        float low = p.getFloat("low",20);
        float high = p.getFloat("high",80);

        if((level>=high && charging) || (level<=low && !charging)){
            c.startForegroundService(new Intent(c,AlarmBatteryService.class));
        }
    }
}
