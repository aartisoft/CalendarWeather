package com.iexamcenter.calendarweather;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.TrafficStats;
import android.os.Build;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class CalendarWeatherApp extends Application {
    public static boolean isForeground = false;
    public static HashMap<String, CoreDataHelper> hashMapAllPanchang1;
    public static ArrayList<String> hashMapAllFestKey;
    public static HashMap<String, CoreDataHelper> hashMapAllDay1;
    public static HashMap<String, ArrayList<String>> hashMapAllFest;
    public static HashMap<String, ArrayList<String>> hashMapCurrFest;
    public static int displayYearInt = 0;
    public static int displayMonthInt = 0;
    public static int displayDayInt = 0;
    public static boolean isPanchangEng=false;
    public static boolean ForTesting;

    public static void setDisplayYearInt(int val) {
        displayYearInt = val;
    }

    public static void setDisplayMonthInt(int val) {
        displayMonthInt = val;
    }

    public static void setDisplayDayInt(int val) {
        displayDayInt = val;
    }

    public static int getDisplayYearInt() {
        return displayYearInt;
    }

    public static int getDisplayMonthInt() {
        return displayMonthInt;
    }

    public static int getDisplayDayInt() {
        return displayDayInt;
    }


    public static void setHashMapAllPanchang1(HashMap<String, CoreDataHelper> val) {
        hashMapAllPanchang1 = val;
    }

    public static void setHashMapCurrMonth(HashMap<String, CoreDataHelper> val) {
        hashMapAllDay1 = val;
    }

    public static void setHashMapAllFest(HashMap<String, ArrayList<String>> val) {
        hashMapAllFest = val;
    }

    public static void setHashMapCurrFest(HashMap<String, ArrayList<String>> val) {
        hashMapCurrFest = val;
    }

    public static HashMap<String, CoreDataHelper> getHashMapAllPanchang1() {
        return hashMapAllPanchang1;
    }

    public static HashMap<String, CoreDataHelper> getHashMapCurrMonth() {
        return hashMapAllDay1;

    }


    public static HashMap<String, ArrayList<String>> getHashMapAllFest() {

        return hashMapAllFest;

    }

    public static HashMap<String, ArrayList<String>> getHashMapCurrFest() {
        return hashMapCurrFest;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //private Tracker mTracker;
    public Location mLastLocation;

    private boolean DEVELOPER_MODE = false;
    public static boolean isJustInstalled = false;
    public static boolean REFRESHEVENT = false;
    public static int notificationTapped = 0;
    public static int selectedPage = 1;
    public static int selectedSubPage = 1;
  //  private static final int THREAD_ID = 10000;
    static CalendarWeatherApp sInstance;
    PrefManager pref;

    public static int getSelectedPage() {
        return selectedPage;
    }

    public static int getSelectedSubPage() {
        return selectedSubPage;
    }

    public static void setSelectedPage(int page, int spage) {
        selectedPage = page;
        selectedSubPage = spage;

    }

    HashMap<String, LatLngBounds> hm;
    public static String[] tithi_arr, en_ritu_arr, ritu_arr, en_rasi_kundali_arr, masa_arr, paksha_arr, nakshatra_arr, rasi_kundali_arr, l_nakshatra_arr, l_tithi_arr, l_paksha_arr, l_rasi_kundali_arr, e_month_short_arr, l_ritu_arr, l_month_arr, e_month_arr, l_masa_arr, l_bara_arr, l_ebara_arr, l_festival_arr, l_number, bara_arr, e_bara_full_arr;
    public static String myLang;
    // public static boolean moreThanDays = false;

    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        //  configuration.setLayoutDirection(locale);


        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }


    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onCreate() {

        super.onCreate();
        isForeground = true;
        pref = PrefManager.getInstance(this);
        pref.load();
        myLang = pref.getMyLanguage();
        //int THREAD_ID = 10000;
        //TrafficStats.setThreadStatsTag(THREAD_ID);
        //  hashMapAllPanchang = new HashMap<>();
        hashMapAllPanchang1 = new HashMap<>();
        hashMapAllFestKey = new ArrayList<>();
        // hashMapAllDay = new HashMap<>();
        hashMapAllDay1 = new HashMap<>();
        hashMapAllFest = new HashMap<>();

       /* new WebView(getApplicationContext());
        if (!getProcessName(this).equals("YOUR_SECOND_PROCESS_NAME")) {
            MobileAds.initialize(this);
        } else {
            WebView.setDataDirectorySuffix("dir_name_no_separator");
        }*/
       // TrafficStats.setThreadStatsTag(THREAD_ID);
        sInstance = this;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()

                    .detectAll()/*
                    .penaltyLog()*/
                    .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .detectNonSdkApiUsage()
                            .penaltyLog()/*
                            .penaltyDeath()*/
                            .detectAll()
                            .build());
        }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int version = pInfo.versionCode;
            if (pref.getAppUpdatesVersionCode() != version) {
                pref.setLastWeatherUpdatedTime(0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        try {

            setLatLngBounds();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   /* public  String getProcessName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }

        return null;
    }*/

    public void setLatLngBounds() {
        LatLngBounds BOUNDS_EN = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
        LatLngBounds BOUNDS_OR = new LatLngBounds(new LatLng(17.735218, 80.993756), new LatLng(23.460352, 87.003277));
        LatLngBounds BOUNDS_BN = new LatLngBounds(new LatLng(21.288539, 86.056478), new LatLng(27.478240, 89.901693));
        LatLngBounds BOUNDS_TE = new LatLngBounds(new LatLng(12.714232, 76.642519), new LatLng(19.548340, 84.838319));
        LatLngBounds BOUNDS_TA = new LatLngBounds(new LatLng(8.043721, 76.801632), new LatLng(13.673469, 80.580929));
        LatLngBounds BOUNDS_KN = new LatLngBounds(new LatLng(11.508719, 74.301962), new LatLng(19.131314, 79.026083));
        LatLngBounds BOUNDS_ML = new LatLngBounds(new LatLng(8.115328, 76.168542), new LatLng(13.263017, 77.003503));
        LatLngBounds BOUNDS_MR = new LatLngBounds(new LatLng(15.516885, 72.882503), new LatLng(22.583276, 80.836605));
        LatLngBounds BOUNDS_HI = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
        LatLngBounds BOUNDS_GU = new LatLngBounds(new LatLng(19.643058, 68.613688), new LatLng(25.364333, 74.348551));
        LatLngBounds BOUNDS_PA = new LatLngBounds(new LatLng(29.557245, 73.637152), new LatLng(32.512573, 76.867132));
        hm = new HashMap<>();
        hm.put("or", BOUNDS_OR);
        hm.put("bn", BOUNDS_BN);
        hm.put("te", BOUNDS_TE);
        hm.put("ta", BOUNDS_TA);
        hm.put("kn", BOUNDS_KN);
        hm.put("ml", BOUNDS_ML);
        hm.put("mr", BOUNDS_MR);
        hm.put("hi", BOUNDS_HI);
        hm.put("gu", BOUNDS_GU);
        hm.put("pa", BOUNDS_PA);
        hm.put("en", BOUNDS_EN);

    }

    public LatLngBounds getLatLngBounds() {
        return hm.get(pref.getMyLanguage());
    }


    public static void updateAppResource(Resources resource, Context context) {
        PrefManager mPref = PrefManager.getInstance(context);
        CalendarWeatherApp.updateResources(context, mPref.getMyLanguage());


    }

    public synchronized static CalendarWeatherApp getInstance() {
        return sInstance;
    }



}