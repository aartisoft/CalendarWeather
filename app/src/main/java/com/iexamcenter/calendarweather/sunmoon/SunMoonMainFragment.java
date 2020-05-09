package com.iexamcenter.calendarweather.sunmoon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iexamcenter.calendarweather.DatePickerFragment;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * PanchangMainFragment
 */

public class SunMoonMainFragment extends Fragment {
    public static final String ARG_POSITION = "POSITION";
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    ViewPager mPager;
    SunMoonPagerAdapter adapter;

    public static int dayPosition = 0;
    public static int currMonthPos = 0;
    public static int maxCalendarYear ;
    public static int minCalendarYear ;

    public static SunMoonMainFragment newInstance() {

        return new SunMoonMainFragment();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.showHideBottomNavigationView(true);
        // mContext.unregisterReceiver(mUpdateLocationReceiver);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  activity.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun_moon_main, container, false);
        activity.showHideBottomNavigationView(false);

        activity.enableBackButtonViews(true);
        DateFormat dateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
        // activity.toolbar.setTitle("Sun & Moon Info");
        // activity.toolbar.setSubtitle(today);
        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setHasOptionsMenu(true);
        maxCalendarYear = Integer.parseInt(res.getString(R.string.maxYear));
        minCalendarYear = Integer.parseInt(res.getString(R.string.minYear));


        setUp(rootView);
        return rootView;
    }


    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {
        mPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        PrefManager mPref = PrefManager.getInstance(activity);
        mPref.load();

        tabLayout.setupWithViewPager(mPager);
        adapter = SunMoonPagerAdapter.newInstance(getChildFragmentManager(), activity);
        mPager.setAdapter(adapter);


        ImageView prev, next, refresh, goto_date;
        TextView month_name;

        prev = rootView.findViewById(R.id.prev);
        next = rootView.findViewById(R.id.next);
        refresh = rootView.findViewById(R.id.refresh);
        goto_date = rootView.findViewById(R.id.goto_date);
        prev.setTag(-1);
        next.setTag(1);
        refresh.setTag(0);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currMonthPos = 0;
                dayPosition = 0;
                adapter.notifyDataSetChanged();
            }
        });
        goto_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currMonthPos--;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, currMonthPos);
                if (cal.get(Calendar.YEAR) < minCalendarYear) {
                    Toast.makeText(activity, "Sorry, We are not getting calendar data.", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currMonthPos++;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, currMonthPos);
                if (cal.get(Calendar.YEAR) > maxCalendarYear) {
                    Toast.makeText(activity, "Sorry, We are not getting calendar data.", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });


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
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            endCal.set(year, monthOfYear, 1);
            dayPosition = dayOfMonth;
            currMonthPos = monthsBetweenDates(startCal, endCal);
            adapter.notifyDataSetChanged();
            Log.e("dayOfMonth", year + ":" + monthOfYear + ":dayOfMonthdayOfMonth" + dayOfMonth);


        }
    };

    public int monthsBetweenDates(Calendar start, Calendar end) {


        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

}