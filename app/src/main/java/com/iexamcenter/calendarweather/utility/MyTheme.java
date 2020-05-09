package com.iexamcenter.calendarweather.utility;

import android.app.Activity;

import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;

import com.iexamcenter.calendarweather.R;

public class MyTheme {
    private static int sTheme;

    public final static int THEME_ALWAYS =1;
    public final static int THEME_NIGHT_MODE = 0;
    public final static int THEME_NONE = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity)
    {
        Log.e("IAPMainViewController","::MainViewControlleronCreate:changeToTheme");
        activity.recreate();
       // activity.getTheme().applyStyle(R.style.AppTheme, true);
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        PrefManager mPref=PrefManager.getInstance(activity);
        mPref.load();

        Log.e("IAPMainViewController","::MainViewControlleronCreate:changeToTheme:"+mPref.getThemeType());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);


        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES && mPref.getThemeType()==THEME_NIGHT_MODE) {
            Log.e("IAPMainViewController","::MainViewControlleronCreate:changeToTheme:MODE_NIGHT_YES");
            activity.setTheme(R.style.AppTheme);
          //  activity.getTheme().applyStyle(R.style.AppTheme, true);

        }else
        {

            Log.e("IAPMainViewController","::MainViewControlleronCreate:changeToTheme:ELSE:MODE_NIGHT_YES");

            switch (mPref.getThemeType()) {

                case THEME_ALWAYS:
                    Log.e("IAPMainViewController","::MainViewControlleronCreate:THEME_ALWAYS:MODE_NIGHT_YES");

                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    activity.setTheme(R.style.AppTheme);
                    Log.e("IAPMainViewController","::MainViewControlleronCreate:THEME_ALWAYS:END:MODE_NIGHT_YES");

                    break;
                case THEME_NIGHT_MODE:
                    Log.e("IAPMainViewController","::MainViewControlleronCreate:THEME_NIGHT_MODE:MODE_NIGHT_YES");

                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    activity.setTheme(R.style.AppTheme);
                    break;

                case THEME_NONE:
                    Log.e("IAPMainViewController","::MainViewControlleronCreate:THEME_NONE:MODE_NIGHT_YES");

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    activity.setTheme(R.style.AppTheme);
                    break;


            }

           // activity.getTheme().applyStyle(R.style.AppTheme, true);
        }
    }

}
