package com.iexamcenter.calendarweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.iexamcenter.calendarweather.MyAlaramWorker;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {


            PrefManager mPref = PrefManager.getInstance(context);
            mPref.load();
            mPref.setLastWeatherUpdatedTime(0);
            int hrmin, hr, min;
            if ((hrmin = mPref.getSettingReporting1()) > 0) {
                hr = hrmin / 60;
                min = (hrmin - (hrmin / 60) * 60);
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                cal1.set(Calendar.HOUR_OF_DAY, hr);
                cal1.set(Calendar.MINUTE, min);

                long diff = cal1.getTimeInMillis() - cal.getTimeInMillis();
                long duration;
                if (diff > 0) {
                    duration = diff / (1000 * 60 );
                } else {
                    duration = (24 * 60) + (diff / (1000 * 60));
                }

                String TAG="MY_APP_ALARM";
                OneTimeWorkRequest mywork =
                        new OneTimeWorkRequest.Builder(MyAlaramWorker.class)
                                .addTag(TAG)
                                .setInitialDelay(duration, TimeUnit.MINUTES)
                                .build();

                WorkManager.getInstance(context).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, mywork);

            }
        }
    }
}

