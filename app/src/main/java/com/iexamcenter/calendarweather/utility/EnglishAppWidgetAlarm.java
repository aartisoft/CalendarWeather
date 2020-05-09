package com.iexamcenter.calendarweather.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class EnglishAppWidgetAlarm {
    private final int ALARM_ID = 0;

    private Context mContext;


    public EnglishAppWidgetAlarm(Context context) {
        mContext = context;
    }




    public void stopAlarm() {

        Intent alarmIntent = new Intent(mContext, ExtraInfoEngAppWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(pendingIntent);
    }
}

