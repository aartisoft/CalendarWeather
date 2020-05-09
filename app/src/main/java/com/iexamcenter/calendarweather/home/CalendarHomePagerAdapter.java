package com.iexamcenter.calendarweather.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.endless.MonthPagerFragment;
import com.iexamcenter.calendarweather.festival.FestivalFragment;
import com.iexamcenter.calendarweather.horoscope.HoroscopeFrag;
import com.iexamcenter.calendarweather.kundali.KundaliInfoFrag;
import com.iexamcenter.calendarweather.kundali.KundaliMainFragment;
import com.iexamcenter.calendarweather.planet.PlanetMainFragment;
import com.iexamcenter.calendarweather.wallcalendar.WallCalendarMainFragment;
import com.iexamcenter.calendarweather.weather.WeatherSlidingFragment;


/**
 * Created by sasikanta on 11/14/2017.
 * CalendarHomePagerAdapter
 */

class CalendarHomePagerAdapter extends FragmentPagerAdapter {


    private CalendarHomePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    public static CalendarHomePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new CalendarHomePagerAdapter(fm, activity);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:


                fragment = MonthPagerFragment.newInstance();

                return fragment;
            case 1:
                fragment = FestivalFragment.newInstance();
                return fragment;
            case 2:

                fragment = HoroscopeFrag.newInstance();
                Bundle args = new Bundle();
                args.putInt(HoroscopeFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;
            default:

                return WeatherSlidingFragment.newInstance();


        }


    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Calendar";
            case 1:
                return "Festival";
            case 2:
                return "Horoscope";
            default:
                return "Weather";
        }

       // return "";

    }

    @Override
    public int getCount() {
        return 3;
    }
}
