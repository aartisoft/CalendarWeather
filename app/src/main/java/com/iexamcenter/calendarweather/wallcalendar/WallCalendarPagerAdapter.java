package com.iexamcenter.calendarweather.wallcalendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;


/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerAdapter
 */

class WallCalendarPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle = {"Governor & Ministers", "Holidays"};
    MainActivity activity;
 //   boolean mIsStateGovt;
    PrefManager mPref;

    private WallCalendarPagerAdapter(FragmentManager fm, FragmentActivity activity, boolean isStateGovt) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
      //  mIsStateGovt = isStateGovt;
       /* if (!mPref.getPublication().contains("govtcal")) {
            pageTitle = CalendarWeatherApp.l_month_arr;
        }*/
        this.activity = (MainActivity) activity;
    }

    public static WallCalendarPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, boolean isStateGovt) {

        return new WallCalendarPagerAdapter(fm, activity, isStateGovt);
    }

    @Override
    public Fragment getItem(int position) {

            Fragment fragment = WallCalendarFragment.newInstance();
            Bundle args = new Bundle();
            args.putInt(WallCalendarFragment.ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;


    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mPref.getPublication().contains("govtcal") && mPref.getMyLanguage().contains("or")) {
            return pageTitle[position];
        } else if (mPref.getMyLanguage().contains("or")) {
            return mPref.getPublication()+" - "+Calendar.getInstance().get(Calendar.YEAR);
        }else{
            return "Year - "+Calendar.getInstance().get(Calendar.YEAR);
        }

    }

    @Override
    public int getCount() {
        if (mPref.getPublication().contains("govtcal")  && mPref.getMyLanguage().contains("or"))
            return 2;
        else
            return 1;

    }
}
