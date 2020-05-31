package com.iexamcenter.calendarweather;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.billingmodule.IAPFragment;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("NullableProblems")
public class MainActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
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
    MainViewModel viewModel;
    boolean workManagerStarted = false;
    Resources mRes;

    String le_menu_settings, le_menu_panchanga, le_menu_calendar, le_menu_festivals, le_menu_horoscope, le_menu_weather, le_menu_sun_moon, le_menu_vedic_time;
    String le_menu_wall_calendar, le_menu_janma_kundali, le_menu_kundali_milana, le_menu_planet, le_menu_category, le_menu_national, le_menu_international, le_menu_observance;
    String le_menu_aradhana, le_menu_on_this_day, le_menu_media, le_menu_birth_anniversary, le_menu_death_anniversary, le_menu_other_anniversary, le_menu_panchang, le_menu_choghadia, le_menu_remove_ads, le_menu_rate_app, le_menu_share;
    String le_menu_feedback, le_menu_privacy_policy;
    String[] le_arr_main_menu;

    public void getMyResource() {

        if (!CalendarWeatherApp.isPanchangEng) {
            le_arr_main_menu = mRes.getStringArray(R.array.l_arr_main_menu);
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
            le_arr_main_menu = mRes.getStringArray(R.array.e_arr_main_menu);
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
        mRes = mContext.getResources();

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
            mPref.load();
            setDefaultLatLng();
        }

        viewModel.getEphemerisData(System.currentTimeMillis(), -1).observe(this, obj -> {
            if (obj.size() == 0 && !workManagerStarted) {
                workManagerStarted = true;
                viewModel.startWorkmanager();
            }
        });
        CalendarWeatherApp.updateAppResource(getResources(), getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.closeCntr).setOnClickListener(v -> {
            if (mPref.isRemovedAds()) {
                CalendarWeatherApp.isPremiumAccessGrp1 = false;
                CalendarWeatherApp.isPremiumAccessGrp2 = false;
            }
            finish();
        });
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
        if (!mPref.isFirstUse()) {
            setUpHome();
        } else {
            mPref.setMyLanguage("en");
            mPref.load();
            selectlang();
        }
        if (Connectivity.isConnected(mContext) && mPref.isFirstUse()) {
            System.out.println("IAPFragment::2:");
            IAPFragment.newInstance(mContext).handleRemoveAds(0, mContext);
        }
        setDefaultAlarm();
        onNewIntent(getIntent());
        TextView versionTxt = findViewById(R.id.version);
        SwitchMaterial switch1 = findViewById(R.id.switch1);
        expListView = findViewById(R.id.expandableListView);
        versionTxt.setText(version);
        showHideAds();
        clearAllFile(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMyResource();
                expandMenu();
            }
        }, 1000);

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CalendarWeatherApp.isPanchangEng = isChecked;
            viewModel.isEngChanged(isChecked);
            getMyResource();
            expandMenu();
        });
        viewModel.getCurrLang().observe(this, lang -> {
            ActionBar actionBar = getSupportActionBar();
            DateFormat dateFormat = new SimpleDateFormat("EEEE, d-MMM-yyyy", Locale.US);
            Date date = new Date();
            String today = dateFormat.format(date);
            actionBar.setTitle(Utility.getInstance(mContext).getLanguageFull() + " Panchanga Darpana");
            actionBar.setSubtitle(today);
            CalendarWeatherApp.updateAppResource(getResources(), this);
            getMyResource();
            expandMenu();
        });


    }

    public void expandMenu() {
        prepareListData();
        listAdapter = new ExpandableMenuListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            int page = groupPosition + 1, subpage = childPosition + 1;
            switch (page) {
                case 1:
                    switch (subpage) {
                        case 1:
                            Intent intent = new Intent(mContext, MySettingsActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            goToPage(1, 1);
                            break;
                        case 3:
                            goToPage(1, 2);
                            break;
                        case 4:
                            goToPage(1, 3);
                            break;
                        case 5:
                            openFragment(0);//weather
                            break;
                        case 6:
                            openFragment(1); //vedic
                            break;
                        case 7:
                            openFragment(2); //sunmoon
                            break;
                        case 8:
                            openFragment(3); //wallcale
                            break;
                    }
                    break;
                case 2:
                    switch (subpage) {
                        case 1:
                            goToPage(2, 1);
                            break;
                        case 2:
                            goToPage(2, 2);
                            break;
                        case 3:
                            goToPage(2, 3);
                            break;
                    }
                    break;
                case 3:

                    goToPage(3, subpage);
                    break;

                case 4:
                    switch (subpage) {
                        case 1:
                            goToPage(4, 1);
                            break;
                        case 2:
                            goToPage(4, 2);
                            break;
                        case 3:
                            goToPage(4, 3);
                            break;
                        case 4:
                            goToPage(4, 4);
                            break;
                        case 5:
                            goToPage(4, 5);
                            break;
                    }
                    break;
                case 5:
                    switch (subpage) {
                        case 1:
                            openFragment(7);
                            break;
                        case 2:
                            openFragment(8);
                            break;
                        case 3:
                            openFragment(9);
                            break;
                        case 4:
                            openFragment(10);
                            break;
                        case 5:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://static.iexamcenter.com/calendarweather/privacy_policy.html"));
                            startActivity(browserIntent);
                            break;
                    }
                    break;
            }
            return false;
        });
        expListView.expandGroup(0);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<>();
        int len = le_arr_main_menu.length;
        for (int i = 0; i < len; i++) {
            String[] arr = le_arr_main_menu[i].split("_");
            if (arr[1].contentEquals("0")) {
                listDataHeader.add(arr[2]);
            }
        }
        int size = listDataHeader.size();
        for (int j = 0; j < size; j++) {
            List<String> child = new ArrayList<String>();
            for (int i = 0; i < len; i++) {
                String[] arr = le_arr_main_menu[i].split("_");
                if (Integer.parseInt(arr[0]) == (j + 1) && !arr[1].contentEquals("0")) {
                    child.add(arr[2]);
                }
            }
            listDataChild.put(listDataHeader.get(j), child);
        }
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

            String TAG = "MY_APP_ALARM";
            OneTimeWorkRequest mywork =
                    new OneTimeWorkRequest.Builder(MyAlaramWorker.class)
                            .addTag(TAG)
                            .setInitialDelay(duration, TimeUnit.MINUTES)
                            .build();

            WorkManager.getInstance(this).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, mywork);

        }
    }

    public void loadBannerAds(AdView mAdView) {

        mAdView.setVisibility(View.GONE);
        MobileAds.initialize(this, initializationStatus -> {
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
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    public void showHideAds() {
        AdView mAdView = findViewById(R.id.adView);
        if (!mPref.isRemovedAds() && !mPref.isFirstUse()) {
            CalendarWeatherApp.isPremiumAccessGrp1 = false;
            CalendarWeatherApp.isPremiumAccessGrp2 = false;
            loadBannerAds(mAdView);
            loadInterstitialAds(1);
            loadInterstitialAds(2);
            loadRewardedAds(1);
            loadRewardedAds(2);
        } else {
            CalendarWeatherApp.isPremiumAccessGrp1 = true;
             CalendarWeatherApp.isPremiumAccessGrp2 = true;
             mAdView.setVisibility(View.GONE);
        }
        // CalendarWeatherApp.isPremiumAccessGrp1=true;
        //  CalendarWeatherApp.isPremiumAccessGrp2=true;

    }

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
                    CalendarWeatherApp.setSelectedPage(4, 5);

                } else if (extras.containsKey("widget_ele_prayers")) {
                    CalendarWeatherApp.setSelectedPage(4, 1);
                } else if (extras.containsKey("widget_ele_todayinfo")) {
                    CalendarWeatherApp.setSelectedPage(1, 1);
                } else if ((extras.containsKey("TodayUpdates") || extras.containsKey("widget_ele_today_updates"))) {
                    CalendarWeatherApp.setSelectedPage(4, 3);
                } else if (extras.containsKey("weather") || extras.containsKey("widget_ele_weather")) {
                    // CalendarWeatherApp.setSelectedPage(1, 1);
                    openFragment(0);
                } else if (extras.containsKey("horoscope") || extras.containsKey("widget_ele_horoscope")) {

                    CalendarWeatherApp.setSelectedPage(1, 3);
                } else if (extras.containsKey("widget_ele_event")) {

                    CalendarWeatherApp.setSelectedPage(4, 4);
                } else if (extras.containsKey("birthday")) {

                    CalendarWeatherApp.setSelectedPage(4, 4);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    ArrayList<String> stateList = new ArrayList();

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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.getInstance().hideKeyboard(mContext);
        CalendarWeatherApp.isForeground = true;
        CalendarWeatherApp.updateAppResource(mContext.getResources(), mContext);
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
                if (!mPref.isRemovedAds()) {
                    CalendarWeatherApp.isPremiumAccessGrp1 = false;
                    CalendarWeatherApp.isPremiumAccessGrp2 = false;
                }
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
           /* case 4:
                frag = BirthAnniversaryFrag.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;
            case 5:
                frag = DeathAnniversaryFrag.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;
            case 6:
                frag = OtherAnniversaryFrag.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                ft.commit();
                break;*/
            case 7:
                if (Connectivity.isConnected(mContext)) {
                    IAPFragment feedbackDialog = IAPFragment.newInstance(this);
                    feedbackDialog.show(ft, "IAPFragment");

                } else {
                    Toast.makeText(mContext, "Please use internet. Try again.", Toast.LENGTH_LONG).show();
                }
                break;
            case 8:
                if (Connectivity.isConnected(this))
                    AppRater.app_launched(this, true);
                else {
                    Utility.getInstance(this).newToastLong(getResources().getString(R.string.internet_required));
                }
                break;
            case 9:
                drawer.closeDrawer(Gravity.LEFT);
                frag = fm.findFragmentByTag("share");
                if (frag != null) {
                    ft.remove(frag);
                }
                AppShareDialog shareDialog = AppShareDialog.newInstance(this);
                shareDialog.show(ft, "share");
                break;
            case 10:
                drawer.closeDrawer(Gravity.LEFT);
                frag = fm.findFragmentByTag("feedback");
                if (frag != null) {
                    ft.remove(frag);
                }
                FeedbackDialog feedbackDialog = FeedbackDialog.newInstance(this, 0);
                feedbackDialog.show(ft, "feedback");
                break;
        }
    }

    public void goToPage(int page, int section) {
        Log.e("goToPage","goToPage:MAIN:"+page + "_" + section);
        viewModel.setPageSubpage(page + "_" + section);
    }


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

    public void setDefaultLatLng() {

        switch (mPref.getMyLanguage()) {
            case "or":
                mPref.setLatitude("19.8876");
                mPref.setLongitude("86.0945");
                mPref.setAreaAdmin("Konark Sun Temple, Puri, Odisha");
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
                mPref.setLatitude("28.6129");
                mPref.setLongitude("77.2295");
                mPref.setAreaAdmin("India Gate");

        }
        mPref.load();
    }

    public RewardedAd mRewardedAdGrp1, mRewardedAdGrp2;
    public InterstitialAd mInterstitialAdGrp1, mInterstitialAdGrp2;

    public void loadRewardedAds(int type) {

        switch (type) {
            case 1:
                mRewardedAdGrp1 = createAndLoadRewardedAd(mRes.getString(R.string.reward_ad_unit_id_1));
                break;
            case 2:
                mRewardedAdGrp2 = createAndLoadRewardedAd(mRes.getString(R.string.reward_ad_unit_id_2));
                break;
        }
    }
    public void loadInterstitialAds(int type) {

        switch (type) {
            case 1:
                mInterstitialAdGrp1=createAndLoadInterstitialAd(mRes.getString(R.string.interstitial_ad_unit_id_1));
                break;
            case 2:
                mInterstitialAdGrp2=createAndLoadInterstitialAd(mRes.getString(R.string.interstitial_ad_unit_id_2));
                break;
        }
    }
    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this, adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }

        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public InterstitialAd createAndLoadInterstitialAd(String adUnitId) {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(adUnitId);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        return interstitialAd;
    }
}
