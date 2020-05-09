package com.iexamcenter.calendarweather.endless;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.MovableFloatingActionButton;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * ChoghadiaMainFragment
 */

public class DayViewMainFragment extends Fragment {
    public static final String ARG_POSITION = "POSITION";
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    int selectedDay, selectedMonth, selectedYear;
    PrefManager mPref;
    TextView vedicTime;
    Timer timer;
    long sunRiseMilli;

    public static DayViewMainFragment newInstance() {

        return new DayViewMainFragment();
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
    public void onResume() {
        super.onResume();

        timer = new Timer();

        updateVedicTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_dayview, container, false);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        //  activity.showHideBottomNavigationView(false);

        //  activity.enableBackButtonViews(true);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        res = activity != null ? activity.getResources() : null;

        //  activity.getSupportActionBar().setTitle("Panchanga Details");
        // activity.getSupportActionBar().setSubtitle("");


        Bundle args = getArguments();
        selectedDay = args.getInt("DAY");
        selectedMonth = args.getInt("MONTH");
        selectedYear = args.getInt("YEAR");

        setUp(rootView);
        return rootView;
    }


    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {
        ViewPager tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        //  tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        settodaydetails(rootView);

        tabLayout.setupWithViewPager(tabViewPager);
        DayViewPagerAdapter adapter = DayViewPagerAdapter.newInstance(getChildFragmentManager(), activity, selectedDay, selectedMonth, selectedYear, Utility.getInstance(activity).getRegLanguageFull(), mPref.getMyLanguage());
        tabViewPager.setAdapter(adapter);
       /* MovableFloatingActionButton fab = rootView.findViewById(R.id.fab);

        if (CalendarWeatherApp.isPanchangEng) {
            fab.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_eng);
        }
        if (mPref.getMyLanguage().contains("en")) {
            fab.hide();
        } else {
            fab.show();
        }
        fab.setOnClickListener(v -> {
            if (!CalendarWeatherApp.isPanchangEng) {
                fab.setImageResource(R.drawable.ic_chevron_left_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_eng);
            }
            CalendarWeatherApp.isPanchangEng = !CalendarWeatherApp.isPanchangEng;
            adapter.notifyDataSetChanged();
        }); */

        String le_normal_time_unit1, le_vedic_time_unit1;
        if (CalendarWeatherApp.isPanchangEng) {
            le_normal_time_unit1 = res.getString(R.string.e_normal_time_unit1);
            le_vedic_time_unit1 = res.getString(R.string.e_vedic_time_unit1);
        } else {
            le_normal_time_unit1 = res.getString(R.string.l_normal_time_unit1);
            le_vedic_time_unit1 = res.getString(R.string.l_vedic_time_unit1);
        }
        TextView vedicTimeLbl, digitalClockLbl;
        digitalClockLbl = rootView.findViewById(R.id.digitalClockLbl);
        vedicTimeLbl = rootView.findViewById(R.id.vedicTimeLbl);

        digitalClockLbl.setText(le_normal_time_unit1);
        vedicTimeLbl.setText(le_vedic_time_unit1);
    }

    private void settodaydetails(View rootView) {
        Calendar sunRiseCal = Calendar.getInstance();
        Utility.SunDetails sunrisecal = Utility.getInstance(activity).getTodaySunDetails(sunRiseCal);
        Utility.MoonDetails moonrisecal = Utility.getInstance(activity).getTodayMoonDetails(sunRiseCal);
        try {
            String sunRiseStr = sunrisecal.sunRise;
            String sunSetStr = sunrisecal.sunSet;
            String moonRiseStr = moonrisecal.moonRise;
            String moonSetStr = moonrisecal.moonSet;

            Log.e("moonRiseStr", "moonRiseStr|" + moonRiseStr);
            Log.e("moonRiseStr", "moonRiseStr|" + moonSetStr);
            String[] tmp1 = moonRiseStr.split(" ");
            String[] tmp2 = moonSetStr.split(" ");
            String[] tmpArr1 = tmp1[0].split(":");
            String[] tmpArr2 = tmp2[0].split(":");
            int today = sunRiseCal.get(Calendar.DAY_OF_MONTH);
            String moonRiseTime = "-", moonSetTime = "-";

            moonRiseTime = tmpArr1[1] + ":" + tmpArr1[2] + " " + tmp1[1].toLowerCase();
            moonSetTime = tmpArr2[1] + ":" + tmpArr2[2] + " " + tmp2[1].toLowerCase();
            if (Integer.parseInt(tmpArr1[0]) != today) {
                moonRiseTime = tmpArr1[1] + ":" + tmpArr1[2] + " " + tmp1[1].toLowerCase() + "+";
            }
            if (Integer.parseInt(tmpArr2[0]) != today) {
                moonSetTime = tmpArr2[1] + ":" + tmpArr2[2] + " " + tmp2[1].toLowerCase() + "+";
            }


            String[] timeArr = sunRiseStr.split(" ")[0].split(":");
            sunRiseCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
            sunRiseCal.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
            sunRiseCal.set(Calendar.SECOND, 0);
            sunRiseCal.set(Calendar.MILLISECOND, 0);
            TextView sunrise, sunset, moonrise, moonset;
            sunrise = rootView.findViewById(R.id.sunrise);
            sunset = rootView.findViewById(R.id.sunset);
            moonrise = rootView.findViewById(R.id.moonrise);
            moonset = rootView.findViewById(R.id.moonset);
            LinearLayout sunCntr, moonCntr, vedicCntr, gregorianCntr;
            sunCntr = rootView.findViewById(R.id.sunCntr);
            moonCntr = rootView.findViewById(R.id.moonCntr);
            vedicCntr = rootView.findViewById(R.id.vedicCntr);
            gregorianCntr = rootView.findViewById(R.id.gregorianCntr);
            //  sunCntr.setOnClickListener(view -> openFragment(1));
            // moonCntr.setOnClickListener(view -> openFragment(1));
            // vedicCntr.setOnClickListener(view -> openFragment(2));
            // gregorianCntr.setOnClickListener(view -> openFragment(2));
            sunrise.setText(sunRiseStr.toLowerCase());
            sunset.setText(sunSetStr.toLowerCase());
            moonrise.setText(moonRiseTime);
            moonset.setText(moonSetTime);
            sunRiseMilli = sunRiseCal.getTimeInMillis();

        } catch (Exception e) {
            Log.e("moonRiseStr", "moonRiseStr|" + e.getMessage());
            e.printStackTrace();
        }
        sunRiseMilli = sunRiseCal.getTimeInMillis();
        // digitalClock = rootView.findViewById(R.id.digitalClock);
        vedicTime = rootView.findViewById(R.id.vedicTime);
    }

    private void updateVedicTime() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        long diff = (System.currentTimeMillis() - sunRiseMilli);
                        if (diff < 0) {
                            diff = (System.currentTimeMillis() - (sunRiseMilli - 24 * 60 * 60 * 1000));
                        }
                        // int tag=(int)vedicTime.getTag();
                        //  long diff = (diffFinal+(tag*400)) / 1000;
                        //  24 *60 sec= 1ghati
                        // 24 *60 sec= 60*60 vipal
                        // Log.e("sunRiseCalVALL", "sunRiseCalVALL::::" + diff);

                        double ghati = diff / (24 * 60.0 * 1000);
                        int ghatiVal = (int) ghati;
                        double remGhati = ghati - ghatiVal;
                        double totalVipal = remGhati * 60 * 60;
                        int palVal = (int) (totalVipal / 60.0);
                        int vipalVal = (int) (totalVipal % 60.0);
                        String ghatiStr = "" + ghatiVal;
                        if (ghatiVal < 10)
                            ghatiStr = "0" + ghatiVal;
                        String palStr = "" + palVal;
                        if (palVal < 10)
                            palStr = "0" + palVal;
                        String vipalStr = "" + vipalVal;
                        if (vipalVal < 10)
                            vipalStr = "0" + vipalVal;

                        vedicTime.setText(ghatiStr + ":" + palStr + ":" + vipalStr);

                    }
                });
            }

        }, 0, 400);

    }
}