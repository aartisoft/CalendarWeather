package com.iexamcenter.calendarweather.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.iexamcenter.calendarweather.R;

public class AppUpdater {
    private final static String APP_TITLE = "Panchanga Darpana";
    private static String PACKAGE_NAME = "com.iexamcenter.calendarweather";
    private static int DAYS_UNTIL_PROMPT = 0;
    private static int LAUNCHES_UNTIL_PROMPT = 1;
    private static long EXTRA_DAYS;
    private static long EXTRA_LAUCHES;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Activity activity;

    public static void app_launched(Activity activity1) {
        activity = activity1;

        //Configs.sendScreenView("Avaliando App", activity);

        PACKAGE_NAME = activity.getPackageName();
/*
        prefs = activity.getSharedPreferences("appupdater", Context.MODE_PRIVATE);

        editor = prefs.edit();

        EXTRA_DAYS = prefs.getLong("extra_days", 0);
        EXTRA_LAUCHES = prefs.getLong("extra_launches", 0);

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= (LAUNCHES_UNTIL_PROMPT + EXTRA_LAUCHES))
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) + EXTRA_DAYS)
            */
                showUpdaterDialog();

       // editor.commit();
    }

    public static void showUpdaterDialog() {
        //    final Dialog dialog = new Dialog(activity);
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);

        LayoutInflater inflater = activity.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_appupdater, null);
        TextView notnow = rootView.findViewById(R.id.notnow);
        TextView updates = rootView.findViewById(R.id.updates);

        //  TextView version = (TextView) rootView.findViewById(R.id.version);
        //  TextView desc = (TextView) rootView.findViewById(R.id.desc);
        PrefManager mPref = PrefManager.getInstance(activity);
        // desc.setText(mPref.getAppUpdatesDesc());

        //   version.setText("Update your app to get \n new year day info & festivals.");


        updates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Connectivity.isConnected(activity)) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                } else {
                    Utility.getInstance(activity).newToastLong("Connect internet to update app");
                }
/*
                //    Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar", activity);
                if (Connectivity.isConnected(activity)) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                    delayDays(5);
                    dialog.dismiss();
                } else {
                    Utility.getInstance(activity).newToastLong("Connect internet to update app");
                }
*/
            }
        });

        notnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar Mais Tarde", activity);
               // delayDays(3);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        //  dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(rootView);
        dialog.show();
    }

    private static void delayLaunches(int numberOfLaunches) {
        long extra_launches = prefs.getLong("extra_launches", 0) + numberOfLaunches;
        editor.putLong("extra_launches", extra_launches);
        editor.commit();
    }

    private static void delayDays(int numberOfDays) {
        Long extra_days = prefs.getLong("extra_days", 0) + (numberOfDays * 1000 * 60 * 60 * 24);
        editor.putLong("extra_days", extra_days);
        editor.commit();
    }
}