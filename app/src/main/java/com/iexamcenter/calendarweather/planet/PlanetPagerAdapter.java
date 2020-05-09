package com.iexamcenter.calendarweather.planet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;


/**
 * Created by sasikanta on 11/14/2017.
 * MuhurtaPagerAdapter
 */

class PlanetPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle,l_planet_page,e_planet_page;
    MainActivity activity;

    private PlanetPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
       // pageTitle = new String[]{"Position", "Transit", "Asta", "Vakri"};
        l_planet_page =activity.getResources().getStringArray(R.array.l_arr_planet_page);
        e_planet_page =activity.getResources().getStringArray(R.array.e_arr_planet_page);

    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public static PlanetPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new PlanetPagerAdapter(fm, activity);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args;
        switch (position) {

            case 0:
                fragment = PlanetInfoFrag.newInstance();
                args = new Bundle();
                args.putInt(PlanetInfoFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = PlanetTransFrag.newInstance();
                args = new Bundle();
                args.putInt(PlanetTransFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = PlanetAstaFrag.newInstance();
                args = new Bundle();
                args.putInt(PlanetAstaFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            default:

                fragment = PlanetDirFrag.newInstance();
                args = new Bundle();
                args.putInt(PlanetDirFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                break;

        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (!CalendarWeatherApp.isPanchangEng) {
            return l_planet_page[position];
        } else {
            return e_planet_page[position];
        }

    }

    @Override
    public int getCount() {
        return e_planet_page.length;
    }
}
