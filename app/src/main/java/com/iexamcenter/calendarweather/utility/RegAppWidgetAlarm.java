package com.iexamcenter.calendarweather.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class RegAppWidgetAlarm {
    private final int ALARM_ID = 0;

    private Context mContext;


    public RegAppWidgetAlarm(Context context) {
        mContext = context;
    }


    public void startAlarm() {

        Calendar calendar = Calendar.getInstance();
        int INTERVAL_MILLIS = 900000;
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS);

        Intent alarmIntent = new Intent(mContext, ExtraInfoRegAppWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis() + INTERVAL_MILLIS, INTERVAL_MILLIS, pendingIntent);

    }


    public void stopAlarm() {

        Intent alarmIntent = new Intent(mContext, ExtraInfoRegAppWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

