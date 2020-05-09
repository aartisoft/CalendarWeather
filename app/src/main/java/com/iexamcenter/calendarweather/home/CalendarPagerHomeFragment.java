package com.iexamcenter.calendarweather.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.DatePickerFragment;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.FestivalData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangUtility;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by sasikanta on 11/14/2017.
 * CalendarPagerFragment
 */

public class CalendarPagerHomeFragment extends Fragment {
    CalendarHomePagerAdapter tabViewPagerAdapter;
    private static Object HASHOBJ = null;
    HashMap<String, PanchangUtility.MyPanchang> mAllPanchangHashMap;

    HashMap<String, CoreDataHelper> mDayViewHashMap;
   // private BroadcastReceiver mLocationChangedReceiver;
   // IntentFilter mLocationChangedFilter;
    private static final int LOADER_COREDATA = 67000;
    int pagerPosition = 0;
    public static String ARG_POSITION = "CURRENT_MONTH";
    HashMap<String, ArrayList<String>> monthFestivalmap;
    //ArrayList<String> allfestkey;
    Resources res;
    ViewGroup rootView;
    int num;
    MainActivity activity;
    int displayMonth;
    long startTime, endTime;
    String currLang;
    int displayYearInt, displayMonthInt, dispDayInt;
    int currYearInt, currMonthInt, currDayInt;
    ImageView change_date, refresh;
    PrefManager mPref;
    TabLayout layout;
    ViewPager tabViewPager;
    FrameLayout progressCntr;
    MainViewModel viewModel;
   /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int page = intent.getIntExtra("PAGE", 0);
            int section = intent.getIntExtra("SECTION", 0);

            if (page == 1) {
                tabViewPager.setCurrentItem(section - 1);
            }

        }
    };*/


    @Override
    public void onResume() {
        super.onResume();
        activity = (MainActivity) getActivity();
        pagerPosition = tabViewPager.getCurrentItem();

    }

    public static CalendarPagerHomeFragment newInstance() {


        return new CalendarPagerHomeFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (MainActivity) context;


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar_home_extra, container, false);
        res = getResources();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        CalendarWeatherApp.updateAppResource(activity.getResources(), activity);
        //   LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver,new IntentFilter(AppConstants.GOTOPAGE));
        activity = (MainActivity) getActivity();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getPageSubpage().observe(getViewLifecycleOwner(), pageSubpage -> {

            String[] page = pageSubpage.split("_");
            if (Integer.parseInt(page[0]) == 1) {
                tabViewPager.setCurrentItem(Integer.parseInt(page[1]) - 1);
            }

        });
        DateFormat dateFormat = new SimpleDateFormat("EEEE, d-MMM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);

        activity.toolbar.setTitle(Utility.getInstance(activity).getLanguageFull() + " Panchanga Darpana");
        activity.toolbar.setSubtitle(today);

        mPref = PrefManager.getInstance(activity);
        mPref.load();
        currLang = mPref.getMyLanguage();
        Bundle bundle = getArguments();
        monthFestivalmap = new HashMap<>();
        // allfestkey = new ArrayList<>();
        FestivalData.setFestivalData(mPref.getMyLanguage());

        layout = rootView.findViewById(R.id.tabLayout1);
        progressCntr = rootView.findViewById(R.id.progressCntr);


        tabViewPager = rootView.findViewById(R.id.tabViewPagerHome);
        tabViewPager.setCurrentItem(CalendarWeatherApp.getSelectedSubPage() - 1);


        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;

        mAllPanchangHashMap = new HashMap<>();
        mDayViewHashMap = new HashMap<>();


        endTime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);

        if (CalendarWeatherApp.getDisplayMonthInt() == 0) {
            displayYearInt = currYearInt;
            displayMonthInt = currMonthInt;
            dispDayInt = currDayInt;
            CalendarWeatherApp.setDisplayYearInt(displayYearInt);
            CalendarWeatherApp.setDisplayMonthInt(displayMonthInt);
            CalendarWeatherApp.setDisplayDayInt(dispDayInt);
        } else {
            displayMonthInt = CalendarWeatherApp.getDisplayMonthInt();
            displayYearInt = CalendarWeatherApp.getDisplayYearInt();
            dispDayInt = CalendarWeatherApp.getDisplayDayInt();
        }


        layout.setupWithViewPager(tabViewPager);

        //  tabViewPager.setOffscreenPageLimit(3);

        tabViewPagerAdapter = CalendarHomePagerAdapter.newInstance(getChildFragmentManager(), activity);
        // tabViewPager.setAdapter(CalendarHomePagerAdapter.newInstance(getChildFragmentManager(), activity, mAllPanchangHashMap, displayMonthInt, mDayViewHashMap, monthFestivalmap, displayYearInt, currDayInt, allfestkey));
        tabViewPager.setAdapter(tabViewPagerAdapter);

        tabViewPager.setCurrentItem(pagerPosition);

        //  Bundle bundle1 = new Bundle();
        //  getLoaderManager().restartLoader(LOADER_COREDATA, bundle1, CalendarPagerHomeFragment.this);


      /*  mLocationChangedFilter = new IntentFilter("LOCATION_CHANGED");

        mLocationChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    pagerPosition = tabViewPager.getCurrentItem();
                    displayMonthInt = CalendarWeatherApp.getDisplayMonthInt();
                    displayYearInt = CalendarWeatherApp.getDisplayYearInt();
                    dispDayInt = CalendarWeatherApp.getDisplayDayInt();

                    //    Bundle bundle1 = new Bundle();
                    //   getLoaderManager().restartLoader(LOADER_COREDATA, bundle1, CalendarPagerHomeFragment.this);
                    tabViewPagerAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };*/


      //  activity.registerReceiver(mLocationChangedReceiver, mLocationChangedFilter);

        viewModel.getLocationChanged().observe(this, isChng -> {

            if (isChng) {
                try {
                    pagerPosition = tabViewPager.getCurrentItem();
                    displayMonthInt = CalendarWeatherApp.getDisplayMonthInt();
                    displayYearInt = CalendarWeatherApp.getDisplayYearInt();
                    dispDayInt = CalendarWeatherApp.getDisplayDayInt();
                    tabViewPagerAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {

            //  LocalBroadcastManager.getInstance(activity).unregisterReceiver(mMessageReceiver);
          //  activity.unregisterReceiver(mLocationChangedReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();


        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("dayofmonth", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");


    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            refreshPage(year, monthOfYear, dayOfMonth);


        }
    };

    public void refreshPage(int year, int monthOfYear,
                            int dayOfMonth) {
        pagerPosition = tabViewPager.getCurrentItem();
        displayMonthInt = monthOfYear;
        displayYearInt = year;
        dispDayInt = dayOfMonth;
        CalendarWeatherApp.setDisplayYearInt(displayYearInt);
        CalendarWeatherApp.setDisplayMonthInt(displayMonthInt);
        CalendarWeatherApp.setDisplayDayInt(dispDayInt);

        //   Bundle bundle1 = new Bundle();
        //  getLoaderManager().restartLoader(LOADER_COREDATA, bundle1, CalendarPagerHomeFragment.this);

    }


}
