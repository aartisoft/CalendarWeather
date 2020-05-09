package com.iexamcenter.calendarweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.billingmodule.IAPFragment;
import com.iexamcenter.calendarweather.endless.DayViewMainFragment;
import com.iexamcenter.calendarweather.home.HinduTimeFrag;
import com.iexamcenter.calendarweather.sunmoon.SunMoonMainFragment;
import com.iexamcenter.calendarweather.tools.BirthAnniversaryFrag;
import com.iexamcenter.calendarweather.tools.DeathAnniversaryFrag;
import com.iexamcenter.calendarweather.tools.OtherAnniversaryFrag;
import com.iexamcenter.calendarweather.utility.AppRater;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;
import com.iexamcenter.calendarweather.wallcalendar.WallCalendarMainFragment;
import com.iexamcenter.calendarweather.weather.WeatherSlidingFragment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//import static com.iexamcenter.calendarweather.billingmodule.billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;

@SuppressWarnings("NullableProblems")
public class MainActivity extends AppCompatActivity implements /* BillingProvider, */NavigationView.OnNavigationItemSelectedListener {
    private static final String DIALOG_TAG = "dialog";

    // private BillingManager mBillingManager;
    // private AcquireFragment mAcquireFragment;
    // private IAPMainViewController mViewController;
    MainActivity mContext;


    public PrefManager mPref;
    Fragment rootFragment;
    public BottomNavigationView bottomNavigationView;
    public NavigationView navigationView;
    public Toolbar toolbar;
    DrawerLayout drawer;
    private static long sBackPressed;
    private CharSequence aTitle, aSubTitle;
    private ActionBarDrawerToggle toggle;
    public TabLayout tabLayout;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private int currentPagePosition = 0;
    MainViewModel viewModel;
    boolean workManagerStarted = false;
    Resources mRes;
    String le_menu_settings, le_menu_panchanga, le_menu_calendar, le_menu_festivals, le_menu_horoscope, le_menu_weather, le_menu_sun_moon, le_menu_vedic_time;
    String le_menu_wall_calendar, le_menu_janma_kundali, le_menu_kundali_milana, le_menu_planet, le_menu_category, le_menu_national, le_menu_international, le_menu_observance;
    String le_menu_aradhana, le_menu_on_this_day, le_menu_media, le_menu_birth_anniversary, le_menu_death_anniversary, le_menu_other_anniversary, le_menu_panchang, le_menu_choghadia, le_menu_remove_ads, le_menu_rate_app, le_menu_share;
    String le_menu_feedback, le_menu_privacy_policy;
    Menu menu;
    MenuItem nav_settings, nav_home, nav_calendar, nav_festival, nav_horoscope, nav_weather, nav_sun_moon, nav_vedic, nav_wall_calendar, nav_janma_kundali, nav_kundali_milana, nav_planet, nav_quote_cat, nav_quote_indian, nav_quote_others, nav_observance, nav_aradhana, nav_onthisday, nav_media, nav_birth_anniversary, nav_death_anniversary, nav_other_anniversary, nav_remove_ads, nav_rate, nav_share, nav_feedback, nav_privacy;

    public int getVisiblePagePosition() {
        return currentPagePosition;
    }

    public void setVisiblePagePosition(int position) {
        currentPagePosition = position;
    }

    public void getMyResource() {
        mRes = mContext.getResources();
        Log.e("xx", "xxxxxx::::::" + CalendarWeatherApp.isPanchangEng);
        if (!CalendarWeatherApp.isPanchangEng) {
            le_menu_settings = mRes.getString(R.string.l_menu_settings);
            Log.e("xx", "xxxxxx::::::::::" + le_menu_settings);
            le_menu_panchanga = mRes.getString(R.string.l_menu_panchanga);
            le_menu_calendar = mRes.getString(R.string.l_menu_calendar);
            le_menu_festivals = mRes.getString(R.string.l_menu_festivals);
            le_menu_horoscope = mRes.getString(R.string.l_menu_horoscope);
            le_menu_weather = mRes.getString(R.string.l_menu_weather);
            le_menu_sun_moon = mRes.getString(R.string.l_menu_sun_moon);
            le_menu_vedic_time = mRes.getString(R.string.l_menu_vedic_time);
            le_menu_wall_calendar = mRes.getString(R.string.l_menu_wall_calendar);
            le_menu_janma_kundali = mRes.getString(R.string.l_menu_janma_kundali);
            le_menu_kundali_milana = mRes.getString(R.string.l_menu_kundali_milana);
            le_menu_planet = mRes.getString(R.string.l_menu_planet);
            le_menu_category = mRes.getString(R.string.l_menu_category);
            le_menu_national = mRes.getString(R.string.l_menu_national);
            le_menu_international = mRes.getString(R.string.l_menu_international);
            le_menu_observance = mRes.getString(R.string.l_menu_observance);
            le_menu_aradhana = mRes.getString(R.string.l_menu_aradhana);
            le_menu_on_this_day = mRes.getString(R.string.l_menu_on_this_day);
            le_menu_media = mRes.getString(R.string.l_menu_media);

            le_menu_birth_anniversary = mRes.getString(R.string.l_menu_birth_anniversary);
            le_menu_death_anniversary = mRes.getString(R.string.l_menu_death_anniversary);
            le_menu_other_anniversary = mRes.getString(R.string.l_menu_other_anniversary);


            le_menu_panchang = mRes.getString(R.string.l_menu_panchang);
            le_menu_choghadia = mRes.getString(R.string.l_menu_choghadia);
            le_menu_remove_ads = mRes.getString(R.string.l_menu_remove_ads);
            le_menu_rate_app = mRes.getString(R.string.l_menu_rate_app);
            le_menu_share = mRes.getString(R.string.l_menu_share);
            le_menu_feedback = mRes.getString(R.string.l_menu_feedback);
            le_menu_privacy_policy = mRes.getString(R.string.l_menu_privacy_policy);
        } else {
            le_menu_settings = mRes.getString(R.string.e_menu_settings);
            le_menu_panchanga = mRes.getString(R.string.e_menu_panchanga);
            le_menu_calendar = mRes.getString(R.string.e_menu_calendar);
            le_menu_festivals = mRes.getString(R.string.e_menu_festivals);
            le_menu_horoscope = mRes.getString(R.string.e_menu_horoscope);
            le_menu_weather = mRes.getString(R.string.e_menu_weather);
            le_menu_sun_moon = mRes.getString(R.string.e_menu_sun_moon);
            le_menu_vedic_time = mRes.getString(R.string.e_menu_vedic_time);
            le_menu_wall_calendar = mRes.getString(R.string.e_menu_wall_calendar);
            le_menu_janma_kundali = mRes.getString(R.string.e_menu_janma_kundali);
            le_menu_kundali_milana = mRes.getString(R.string.e_menu_kundali_milana);
            le_menu_planet = mRes.getString(R.string.e_menu_planet);
            le_menu_category = mRes.getString(R.string.e_menu_category);
            le_menu_national = mRes.getString(R.string.e_menu_national);
            le_menu_international = mRes.getString(R.string.e_menu_international);
            le_menu_observance = mRes.getString(R.string.e_menu_observance);
            le_menu_aradhana = mRes.getString(R.string.e_menu_aradhana);
            le_menu_on_this_day = mRes.getString(R.string.e_menu_on_this_day);
            le_menu_media = mRes.getString(R.string.e_menu_media);
            le_menu_birth_anniversary = mRes.getString(R.string.e_menu_birth_anniversary);
            le_menu_death_anniversary = mRes.getString(R.string.e_menu_death_anniversary);
            le_menu_other_anniversary = mRes.getString(R.string.e_menu_other_anniversary);

            le_menu_panchang = mRes.getString(R.string.e_menu_panchang);
            le_menu_choghadia = mRes.getString(R.string.e_menu_choghadia);
            le_menu_remove_ads = mRes.getString(R.string.e_menu_remove_ads);
            le_menu_rate_app = mRes.getString(R.string.e_menu_rate_app);
            le_menu_share = mRes.getString(R.string.e_menu_share);
            le_menu_feedback = mRes.getString(R.string.e_menu_feedback);
            le_menu_privacy_policy = mRes.getString(R.string.e_menu_privacy_policy);
        }
    }

    public void setMenuItem() {
        Log.e("xx", "xxxxxx::::::" + le_menu_settings);
        nav_settings.setTitle(le_menu_settings);
        nav_home.setTitle(le_menu_panchanga);
        nav_calendar.setTitle(le_menu_calendar);
        nav_festival.setTitle(le_menu_festivals);
        nav_horoscope.setTitle(le_menu_horoscope);
        nav_weather.setTitle(le_menu_weather);
        nav_sun_moon.setTitle(le_menu_sun_moon);
        nav_vedic.setTitle(le_menu_vedic_time);
        nav_wall_calendar.setTitle(le_menu_wall_calendar);
        nav_janma_kundali.setTitle(le_menu_janma_kundali);
        nav_kundali_milana.setTitle(le_menu_kundali_milana);
        nav_planet.setTitle(le_menu_planet);
        nav_quote_cat.setTitle(le_menu_category);
        nav_quote_indian.setTitle(le_menu_national);
        nav_quote_others.setTitle(le_menu_international);
        nav_observance.setTitle(le_menu_observance);
        nav_aradhana.setTitle(le_menu_aradhana);
        nav_onthisday.setTitle(le_menu_on_this_day);
        nav_media.setTitle(le_menu_media);
        nav_birth_anniversary.setTitle(le_menu_birth_anniversary);
        nav_death_anniversary.setTitle(le_menu_death_anniversary);
        nav_other_anniversary.setTitle(le_menu_other_anniversary);
        nav_remove_ads.setTitle(le_menu_remove_ads);
        nav_rate.setTitle(le_menu_rate_app);
        nav_share.setTitle(le_menu_share);
        nav_feedback.setTitle(le_menu_feedback);
        nav_privacy.setTitle(le_menu_privacy_policy);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, MySettingsActivity.class);
                //  Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void selectlang() {
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        Fragment prev0 = getSupportFragmentManager().findFragmentByTag("MYLANG");
        if (prev0 != null) {
            ft0.remove(prev0);
        }
        final DialogFragment appLangDialog = AppLangDialog.newInstance(this);
        appLangDialog.setCancelable(false);
        appLangDialog.show(ft0, "MYLANG");
        Log.e("PrefManager", "PrefMaPrefManagernager213");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        mPref = PrefManager.getInstance(this);
        PackageInfo pInfo;


        String version = "";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = getResources().getString(R.string.version) + pInfo.versionName;
            if (mPref.getAppUpdatesVersionCode() != pInfo.versionCode) {
                clearAllFile(true);
            }
            mPref.setAppUpdatesVersionCode(pInfo.versionCode);
            mPref.load();


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent arg = getIntent();
        if (arg.hasExtra("MY_LANG")) {
            mPref.setMyLanguage(arg.getStringExtra("MY_LANG"));
        }
        mPref.load();
        setDefaultLatLng();

        // if (mPref.isFirstUse()) {

        // }


        viewModel.getEphemerisData(System.currentTimeMillis(), -1).observe(this, obj -> {
            if (obj.size() == 0 && !workManagerStarted) {
                workManagerStarted = true;
                viewModel.startWorkmanager();
            }
        });


        CalendarWeatherApp.updateAppResource(getResources(), getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        findViewById(R.id.closeCntr).setOnClickListener(v -> finish());

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Helper.getInstance().hideKeyboard(mContext);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        if (!mPref.isFirstUse()) {
            setUpHome();
        } else {
            mPref.setMyLanguage("en");
            mPref.load();

            selectlang();
        }
        menu = navigationView.getMenu();


        nav_settings = menu.findItem(R.id.nav_settings);
        nav_home = menu.findItem(R.id.nav_home);
        nav_calendar = menu.findItem(R.id.nav_calendar);
        nav_festival = menu.findItem(R.id.nav_festival);
        nav_horoscope = menu.findItem(R.id.nav_horoscope);
        nav_weather = menu.findItem(R.id.nav_weather);
        nav_sun_moon = menu.findItem(R.id.nav_sun_moon);
        nav_vedic = menu.findItem(R.id.nav_vedic);
        nav_wall_calendar = menu.findItem(R.id.nav_wall_calendar);
        nav_janma_kundali = menu.findItem(R.id.nav_janma_kundali);
        nav_kundali_milana = menu.findItem(R.id.nav_kundali_milana);
        nav_planet = menu.findItem(R.id.nav_planet);
        nav_quote_cat = menu.findItem(R.id.nav_quote_cat);
        nav_quote_indian = menu.findItem(R.id.nav_quote_indian);
        nav_quote_others = menu.findItem(R.id.nav_quote_others);
        nav_observance = menu.findItem(R.id.nav_observance);
        nav_aradhana = menu.findItem(R.id.nav_aradhana);
        nav_onthisday = menu.findItem(R.id.nav_onthisday);
        nav_media = menu.findItem(R.id.nav_media);

        nav_birth_anniversary = menu.findItem(R.id.nav_birth_anniversary);
        nav_death_anniversary = menu.findItem(R.id.nav_death_anniversary);
        nav_other_anniversary = menu.findItem(R.id.nav_other_anniversary);
        nav_remove_ads = menu.findItem(R.id.nav_remove_ads);
        nav_rate = menu.findItem(R.id.nav_rate);
        nav_share = menu.findItem(R.id.nav_share);
        nav_feedback = menu.findItem(R.id.nav_feedback);
        nav_privacy = menu.findItem(R.id.nav_privacy);


        System.out.println("IAPFragment::1:" + mPref.isFirstUse());
        if (Connectivity.isConnected(mContext) && mPref.isFirstUse()) {
            System.out.println("IAPFragment::2:");
            IAPFragment.newInstance(mContext).handleRemoveAds(0, mContext);
        }

        setDefaultAlarm();

        onNewIntent(getIntent());


        TextView versionTxt = header.findViewById(R.id.version);
        SwitchMaterial switch1 = header.findViewById(R.id.switch1);
        versionTxt.setText(version);
        //  initBilling();
        showHideBannerAds();
        // handleAds();
        clearAllFile(false);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMyResource();
                setMenuItem();
            }
        }, 1000);

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.e("xx", "xxxxxx:" + isChecked);
            //  if(isChecked){
            CalendarWeatherApp.isPanchangEng = isChecked;
            viewModel.isEngChanged(isChecked);

            getMyResource();
            setMenuItem();
            // }
        });
        viewModel.getCurrLang().observe(this, lang -> {
            CalendarWeatherApp.updateAppResource(getResources(), this);
            getMyResource();
            setMenuItem();

        });
    }

    private void clearAllFile(boolean all) {
        try {
            File dir = getFilesDir();
            File[] files = dir.listFiles();
            assert files != null;
            int fileCnt = files.length;
            if (all) {
                for (File file : files) {
                    try {
                        file.delete();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (fileCnt > 10) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                String lang = mPref.getMyLanguage();
                String FN1 = "mycache-" + lang + "-" + year;
                String FN2 = "mycache-" + lang + "-" + (year - 1);
                String FN3 = "mycache-" + lang + "-" + (year + 1);
                String FN0 = "mycache-";
                for (File file : files) {
                    try {
                        String fn = file.getName();
                        if (fn.startsWith(FN0) && (!fn.contains(FN1) && !fn.contains(FN2) && !fn.contains(FN3))) {
                            file.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultAlarm() {
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
                duration = diff / (1000 * 60);
            } else {
                duration = (24 * 60) + (diff / (1000 * 60));
            }

            System.out.println(diff + ":PeriodicWorkRequestStart:min:" + duration);
            String TAG = "MY_APP_ALARM";
            OneTimeWorkRequest mywork =
                    new OneTimeWorkRequest.Builder(MyAlaramWorker.class)
                            .addTag(TAG)
                            .setInitialDelay(duration, TimeUnit.MINUTES)
                            .build();

            WorkManager.getInstance(this).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, mywork);

        }
    }

    public void handleAds() {
        // initBilling();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showHideBannerAds();
            }
        });
    }


    public void showHideBannerAds() {
        AdView mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        if (!mPref.isRemovedAds() && !mPref.isFirstUse()) {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });


            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("onAdonAd", "onAdonAd::" + errorCode);
                    // Code to be executed when an ad request fails.
                    mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    public void removeAds() {


        onPurchaseButtonClicked();

    }


    public void onPurchaseButtonClicked() {

      /*  if (mAcquireFragment == null) {
            mAcquireFragment = new AcquireFragment();
        }

        if (!isAcquireFragmentShown()) {
            mAcquireFragment.show(mContext.getSupportFragmentManager(), DIALOG_TAG);

            if (mBillingManager != null
                    && mBillingManager.getBillingClientResponseCode()
                    > BILLING_MANAGER_NOT_INITIALIZED) {


                mAcquireFragment.onManagerReady(this);
            }
        }*/
    }

    /*
        public boolean isAcquireFragmentShown() {
            return mAcquireFragment != null && mAcquireFragment.isVisible();
        }
    */
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        CalendarWeatherApp.setSelectedPage(1, 1);
        try {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey("PlayNow")) {
                    Bundle data = extras.getBundle("PlayNow");
                    if (data == null)
                        return;

                    CalendarWeatherApp.setSelectedPage(4, 3);
                } else if (extras.containsKey("widget_ele_observance")) {
                    CalendarWeatherApp.setSelectedPage(4, 1);

                } else if (extras.containsKey("widget_ele_prayers")) {
                    CalendarWeatherApp.setSelectedPage(4, 2);
                } else if (extras.containsKey("widget_ele_todayinfo")) {
                    CalendarWeatherApp.setSelectedPage(1, 1);
                } else if ((extras.containsKey("TodayUpdates") || extras.containsKey("widget_ele_today_updates"))) {
                    CalendarWeatherApp.setSelectedPage(4, 4);
                } else if (extras.containsKey("weather") || extras.containsKey("widget_ele_weather")) {
                    // CalendarWeatherApp.setSelectedPage(1, 1);
                    openFragment(0);
                } else if (extras.containsKey("horoscope") || extras.containsKey("widget_ele_horoscope")) {

                    CalendarWeatherApp.setSelectedPage(1, 3);
                } else if (extras.containsKey("widget_ele_event")) {

                    CalendarWeatherApp.setSelectedPage(4, 3);
                } else if (extras.containsKey("birthday")) {

                    CalendarWeatherApp.setSelectedPage(4, 3);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setUpHome() {

        FragmentManager fm = getSupportFragmentManager();
        rootFragment = fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (rootFragment == null)
            rootFragment = RootFragment.newInstance();
        ft.replace(R.id.constraintLayout, rootFragment, AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.addToBackStack(AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        CalendarWeatherApp.isForeground = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mPref.isFirstUse()) {
                mPref.setFirstUse(false);
            }


            // LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.e("MYDATETIME", "::onActivityResult:m:" + requestCode + "::" + resultCode);

            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {


                  /*   case Constant.REQUEST_CODE_LOCATION_PICKER_MAP:
                    case Constant.REQUEST_CODE_LOCATION_PICKER:
                        String[] retVal=Helper.getInstance().getAddressLatLon(data, mContext);
                        if (retVal!=null && retVal.length>2) {

                            mPref.setLatitude(retVal[0]);
                            mPref.setLongitude(retVal[1]);
                            mPref.setWeatherCityId("");
                            mPref.setAreaAdmin(retVal[2]);
                            viewModel.isLocationChanged(true);
                        }

                        break;
                   case Constant.REQUEST_CODE_LOCATION_PICKER:
                        Place place1 = PlacePicker.getPlace(this, data);

                        LatLng latLng1 = place1.getLatLng();
                        mPref.setLatitude(String.valueOf(latLng1.latitude));
                        mPref.setLongitude(String.valueOf(latLng1.longitude));
                        mPref.setAreaAdmin(place1.getAddress().toString());
                        sendBroadcast(new Intent("LOCATION_CHANGED"));
                        break;*/

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> stateList = new ArrayList();

    public String getAddress(double latitude, double longitude) {
        try {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            //  String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            //  String knownName = addresses.get(0).getFeatureName();
            String str = state;
            if (!stateList.contains(state))
                stateList.add(state);
            if (str == null) {
                str = city;
            }
            if (str == null) {

                if (addresses.size() > 1) {
                    city = addresses.get(1).getLocality();
                    state = addresses.get(1).getAdminArea();
                    postalCode = addresses.get(1).getPostalCode();
                    if (!stateList.contains(state))
                        stateList.add(state);
                    str = state;
                    if (str == null) {
                        str = city;
                    }
                    if (str == null && postalCode != null) {
                        str = "PIN:" + postalCode;
                    } else {
                        Log.e("Geocoder", latitude + "::" + longitude + "::Geocoder::" + "-2:" + city + "-3:" + state + "-4:" + postalCode);

                    }


                }


            }


            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "XXXXXX";
    }

    public void enableBackButtonViews(boolean enable) {
        try {
            ActionBar actionBar = getSupportActionBar();
            if (enable) {
                aTitle = actionBar != null ? actionBar.getTitle() : null;
                aSubTitle = actionBar != null ? actionBar.getSubtitle() : null;
                toggle.setDrawerIndicatorEnabled(false);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }

                if (!mToolBarNavigationListenerIsRegistered) {
                    toggle.setToolbarNavigationClickListener(
                            v -> MainActivity.this.onBackPressed()
                    );
                    mToolBarNavigationListenerIsRegistered = true;
                }

            } else {
                if (actionBar != null) {
                    actionBar.setTitle(aTitle);
                    actionBar.setSubtitle(aSubTitle);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                }


                toggle.setDrawerIndicatorEnabled(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mToolBarNavigationListenerIsRegistered = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.getInstance().hideKeyboard(mContext);
        CalendarWeatherApp.isForeground = true;
    }


    @Override
    public void onBackPressed() {
        if (Connectivity.isConnected(this))
            AppRater.app_launched(this);
        int fragCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (fragCnt > 1) {
            if (fragCnt == 2) {
                enableBackButtonViews(false);
            } else {
                enableBackButtonViews(true);
            }
            getSupportFragmentManager().popBackStack();
        } else {

            if (sBackPressed + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                drawer.openDrawer(Gravity.LEFT);

            }
            sBackPressed = System.currentTimeMillis();
        }


    }

    public void showHideToolBarView(Boolean visible) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        if (visible) {
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);

        }


    }

    public void showHideBottomNavigationView(Boolean visible) {
        try {
            bottomNavigationView.setVisibility(visible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openFragment(int type) {
        FragmentManager fm = mContext.getSupportFragmentManager();
        Fragment frag;
        FragmentTransaction ft = fm.beginTransaction();
        switch (type) {
            case 0:
                frag = WeatherSlidingFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_WEATHER_TAG);
                ft.addToBackStack(AppConstants.FRAG_WEATHER_TAG);
                ft.commit();
                break;
            case 1:
                frag = SunMoonMainFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_SUN_MOON_TAG);
                ft.addToBackStack(AppConstants.FRAG_SUN_MOON_TAG);
                ft.commit();
                break;
            case 2:
                frag = HinduTimeFrag.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_VEDIC_TAG);
                ft.addToBackStack(AppConstants.FRAG_VEDIC_TAG);
                ft.commit();
                break;
            case 3:
                frag = WallCalendarMainFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_WALL_CAL_TAG);
                ft.addToBackStack(AppConstants.FRAG_WALL_CAL_TAG);
                ft.commit();
                break;
            case 4:
                frag = BirthAnniversaryFrag.newInstance(type);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;
            case 5:
                frag = DeathAnniversaryFrag.newInstance(type);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;
            case 6:
                frag = OtherAnniversaryFrag.newInstance(type);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;

        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return false;
        }
        FragmentManager fm = getSupportFragmentManager();

        Fragment frag;
        FragmentTransaction ft = fm.beginTransaction();
        switch (id) {
            case R.id.nav_settings:
                Intent intent = new Intent(this, MySettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                Calendar cal = Calendar.getInstance();
                frag = DayViewMainFragment.newInstance();
                Bundle args = new Bundle();
                args.putInt("DAY", cal.get(Calendar.DAY_OF_MONTH));
                args.putInt("MONTH", cal.get(Calendar.MONTH));
                args.putInt("YEAR", cal.get(Calendar.YEAR));
                frag.setArguments(args);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_DAYINFO_TAG);

                ft.addToBackStack(AppConstants.FRAG_DAYINFO_TAG);
                ft.commit();

                break;
            case R.id.nav_calendar:
                goToPage(1, 1);
                break;
            case R.id.nav_festival:
                goToPage(1, 2);

                break;


            case R.id.nav_horoscope:
                goToPage(1, 3);

                break;

            case R.id.nav_planet:
                goToPage(2, 3);
                break;


            case R.id.nav_janma_kundali:
                goToPage(2, 1);

                break;
            case R.id.nav_kundali_milana:
                goToPage(2, 2);
                break;


            case R.id.nav_weather:
                openFragment(0);
                break;
            case R.id.nav_vedic:
                openFragment(2);
                break;
            case R.id.nav_sun_moon:
                openFragment(1);
                break;
            case R.id.nav_wall_calendar:
                openFragment(3);
                break;
            case R.id.nav_birth_anniversary:

                openFragment(4);
                break;
            case R.id.nav_death_anniversary:

                openFragment(5);
                break;
            case R.id.nav_other_anniversary:

                openFragment(6);
                break;

            case R.id.nav_quote_cat:
                goToPage(3, 1);
                break;
            case R.id.nav_quote_indian:
                goToPage(3, 2);
                break;
            case R.id.nav_quote_others:
                goToPage(3, 3);
                break;


            case R.id.nav_observance:
                goToPage(4, 1);
                break;
            case R.id.nav_aradhana:
                goToPage(4, 2);
                break;
            case R.id.nav_media:
                goToPage(4, 4);
                break;
            case R.id.nav_onthisday:
                goToPage(4, 3);
                break;


            case R.id.nav_remove_ads:
                if (Connectivity.isConnected(this)) {


                          /*  frag = IAPFragment.newInstance();
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_WEATHER_TAG);
                            ft.addToBackStack(AppConstants.FRAG_WEATHER_TAG);
                            ft.commit();*/

                    IAPFragment feedbackDialog = IAPFragment.newInstance(this);
                    feedbackDialog.show(ft, "IAPFragment");

                    //  removeAds();
                } else {
                    Toast.makeText(mContext, "Please use internet. Try again.", Toast.LENGTH_LONG).show();
                }
                break;


/*
                try {
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(beginTime.get(Calendar.YEAR), beginTime.get(Calendar.MONTH), beginTime.get(Calendar.DATE), 6, 30);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DATE), 18, 30);

                    Intent intentEvent = new Intent(Intent.ACTION_INSERT)
                            .setType("vnd.android.cursor.item/event")
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                            .putExtra(CalendarContract.Events.VISIBLE, false)
                            .putExtra(CalendarContract.Events.ALL_DAY, false)
                            .putExtra(CalendarContract.Reminders.METHOD, true)
                            .putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    startActivity(intentEvent);
                } catch (Exception e) {
                    Toast.makeText(this, "No Event Calendar Found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
*/
            //break;


            case R.id.nav_rate:
                if (Connectivity.isConnected(this))
                    AppRater.app_launched(this, true);
                else {
                    Utility.getInstance(this).newToastLong(getResources().getString(R.string.internet_required));
                }
                break;
            case R.id.nav_share:
                drawer.closeDrawer(Gravity.LEFT);
                frag = fm.findFragmentByTag("share");
                if (frag != null) {
                    ft.remove(frag);
                }
                AppShareDialog shareDialog = AppShareDialog.newInstance(this);
                shareDialog.show(ft, "share");


                // Intent intent1 = new Intent(this, FullscreenActivity.class);
                // startActivity(intent1);


                break;
            case R.id.nav_feedback:
                drawer.closeDrawer(Gravity.LEFT);
                frag = fm.findFragmentByTag("feedback");
                if (frag != null) {
                    ft.remove(frag);
                }
                FeedbackDialog feedbackDialog = FeedbackDialog.newInstance(this, 0);
                feedbackDialog.show(ft, "feedback");
                break;

            case R.id.nav_privacy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://static.iexamcenter.com/calendarweather/privacy_policy.html"));
                startActivity(browserIntent);
                break;
            case R.id.nav_test:
                if (CalendarWeatherApp.ForTesting) {
                    CalendarWeatherApp.ForTesting = false;
                    Toast.makeText(mContext, "Tester access removed", Toast.LENGTH_SHORT).show();

                } else {
                    CalendarWeatherApp.ForTesting = true;
                    Toast.makeText(mContext, "You are a tester now", Toast.LENGTH_SHORT).show();

                }

                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToPage(int page, int section) {

        viewModel.setPageSubpage(page + "_" + section);
      /* Intent intent = new Intent(AppConstants.GOTOPAGE);

        intent.putExtra("PAGE", page);
        intent.putExtra("SECTION", section);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);*/

    }

  /*  public void showRefreshedUi() {
        if (mAcquireFragment != null) {
            mAcquireFragment.refreshUI();
        }
    }*/

    @UiThread
    public void alert(@StringRes int messageId) {
        alert(messageId, null);
    }


    @UiThread
    public void alert(@StringRes int messageId, @Nullable Object optionalParam) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new RuntimeException("Dialog could be shown only from the main thread");
        }

        AlertDialog.Builder bld = new AlertDialog.Builder(mContext);
        bld.setNeutralButton("OK", null);

        if (optionalParam == null) {
            bld.setMessage(messageId);
        } else {
            bld.setMessage(getResources().getString(messageId, optionalParam));
        }

        bld.create().show();
    }

/*
    public void onBillingManagerSetupFinished() {
        if (mAcquireFragment != null) {
            mAcquireFragment.onManagerReady(this);
        }
    }

    @Override
    public BillingManager getBillingManager() {
        return mBillingManager;
    }

    @Override
    public boolean isPremiumOnePurchased() {
        return mViewController.isPremiumOnePurchased();
    }

    @Override
    public boolean isPremiumTwoPurchased() {
        return mViewController.isPremiumTwoPurchased();
    }

    @Override
    public boolean isPremiumThreePurchased() {
        return mViewController.isPremiumThreePurchased();
    }

    @Override
    public boolean isPremiumFourPurchased() {
        return mViewController.isPremiumFourPurchased();
    }

    @Override
    public boolean isPremiumFivePurchased() {
        return mViewController.isPremiumFivePurchased();
    }
*/

    public void setDefaultLatLng() {
        if (mPref.getLatitude() == null && mPref.getLongitude() == null) {
            switch (mPref.getMyLanguage()) {
                case "or":
                    mPref.setLatitude("19.8048");
                    mPref.setLongitude("85.8179");
                    mPref.setAreaAdmin("Jagannath Temple, Puri, Odisha");
                    break;
                case "bn":
                    mPref.setLatitude("22.572645");
                    mPref.setLongitude("88.363892");
                    mPref.setAreaAdmin("Kolkata, West Bengal");
                    break;
                case "hi":
                    mPref.setLatitude("28.644800");
                    mPref.setLongitude("77.216721");
                    mPref.setAreaAdmin("New Delhi, India");
                    break;
                case "pa":
                    mPref.setLatitude("30.741482");
                    mPref.setLongitude("76.768066");
                    mPref.setAreaAdmin("Chandigarh, Punjab");
                    break;
                case "gu":
                    mPref.setLatitude("23.237560");
                    mPref.setLongitude("72.647781");
                    mPref.setAreaAdmin("Gandhinagar, Gujarat");
                    break;
                case "mr":
                    mPref.setLatitude("18.516726");
                    mPref.setLongitude("73.856255");
                    mPref.setAreaAdmin("Pune, Maharashtra");
                    break;
                case "kn":
                    mPref.setLatitude("12.972442");
                    mPref.setLongitude("77.580643");
                    mPref.setAreaAdmin("Bengaluru, Karnataka");
                    break;
                case "ml":
                    mPref.setLatitude("8.524139");
                    mPref.setLongitude("76.936638");
                    mPref.setAreaAdmin("Thiruvananthapuram, Kerala");
                    break;
                case "ta":
                    mPref.setLatitude("13.067439");
                    mPref.setLongitude("80.237617");
                    mPref.setAreaAdmin("Chennai, Tamil Nadu");
                    break;
                case "te":
                    mPref.setLatitude("17.387140");
                    mPref.setLongitude("78.491684");
                    mPref.setAreaAdmin("Hyderabad, Telangana");
                    break;
                default:
                    mPref.setLatitude("20.7652");
                    mPref.setLongitude("86.1752");
                    mPref.setAreaAdmin("Jajpur, Odisha");

            }
            mPref.load();
        }
    }


    public void writeToFile(String data, Context context, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
public void parseCityJson(){
    BufferedReader reader = null;
    String receiveString;
    StringBuilder stringBuilder = new StringBuilder();
    try {
        reader = new BufferedReader(
                new InputStreamReader(mContext.getAssets().open(  "citylist.json")));

        while ((receiveString = reader.readLine()) != null) {
            stringBuilder.append(receiveString);
        }

    Gson gson = new Gson();
    CityListResponse[] res1 = gson.fromJson(stringBuilder.toString(), CityListResponse[].class);
    if(res1!=null){
        int size=res1.length;
        int cityCnt=0;
        String str;
        stringBuilder = new StringBuilder();
        for(int i=0;i<size;i++){
            CityListResponse obj = res1[i];
            if(obj.getCountry().equalsIgnoreCase("IN")) {
                cityCnt++;
                 str=obj.getId()+" "+obj.getCoord().getLat()+" "+obj.getCoord().getLon()+" "+obj.getName()+"\n";
                stringBuilder.append(str);
                Log.e("CityList", cityCnt+":"+obj.getCountry() + ":cityList:"+ str);
            }
        }
        writeToFile(stringBuilder.toString(), mContext, "MYCOUNTRYJSON.txt");
    }
    }catch (Exception e){
        e.printStackTrace();
    }

}
    public String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }



    public void getCityAddress() {
        BufferedReader reader = null;
        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();
        int cnt = 0;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("location.txt")));

            while ((receiveString = reader.readLine()) != null) {
                cnt++;
                String[] tmp = receiveString.split(" ");
                double lat = Double.parseDouble(tmp[1]);
                double lng = Double.parseDouble(tmp[2]);
                String str = getAddress(lat, lng);
                stringBuilder.append((receiveString + "," + str + "\n"));


                //  stringBuilder.append(receiveString);
            }
            writeToFile(stringBuilder.toString(), mContext, "WeatherLocation.txt");
            // for(int i=0;i<stateList.size();i++){
            Log.e("stateList", "::Geocoder::state:" + stateList.size());
            //  }

            Gson gson = new Gson();
            CityListResponse[] res1 = gson.fromJson(stringBuilder.toString(), CityListResponse[].class);
            if(res1!=null){
                int size=res1.length;
                int cityCnt=0;
                String str;
                stringBuilder = new StringBuilder();
                for(int i=0;i<size;i++){
                    CityListResponse obj = res1[i];
                    if(obj.getCountry().equalsIgnoreCase("IN")) {
                        cityCnt++;
                        str=obj.getId()+" "+obj.getCoord().getLat()+" "+obj.getCoord().getLon()+" "+obj.getName()+"\n";
                        stringBuilder.append(str);
                        Log.e("CityList", cityCnt+":"+obj.getCountry() + ":cityList:"+ str);
                    }
                }
                //  writeToFile(stringBuilder.toString(), mContext, "MYCOUNTRYJSON.txt");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
     */

}
