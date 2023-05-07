package com.example.fallcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        new NotificationHandler(context).send("Tanulj");
    }
}