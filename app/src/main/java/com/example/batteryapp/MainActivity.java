package com.example.batteryapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.batteryapp.di.BatteryService;

public class MainActivity extends Activity {

    EditText low, high;

    public void onCreate(Bundle b) {
        super.onCreate(b);

        int layoutId = getResources().getIdentifier(
                "activity_main", "layout", getPackageName());
        setContentView(layoutId);

        int lowId = getResources().getIdentifier("editTextA","id",getPackageName());
        int highId = getResources().getIdentifier("editTextB","id",getPackageName());
        int startId = getResources().getIdentifier("buttonStart","id",getPackageName());
        int stopId = getResources().getIdentifier("buttonStop","id",getPackageName());

        low = (EditText)findViewById(lowId);
        high = (EditText)findViewById(highId);

        Button start = (Button)findViewById(startId);
        Button stop = (Button)findViewById(stopId);

        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);

        low.setText(""+p.getFloat("low",20));
        high.setText(""+p.getFloat("high",80));

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor e = p.edit();

                try{ e.putFloat("low", Float.parseFloat(low.getText().toString())); }catch(Exception ignored){}
                try{ e.putFloat("high", Float.parseFloat(high.getText().toString())); }catch(Exception ignored){}

                e.apply();

                startForegroundService(new Intent(MainActivity.this, BatteryService.class));
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, BatteryService.class));
            }
        });
    }
}
