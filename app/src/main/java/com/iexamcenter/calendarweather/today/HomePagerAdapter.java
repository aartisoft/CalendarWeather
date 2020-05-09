package com.iexamcenter.calendarweather.today;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.horoscope.HoroscopeFrag;
import com.iexamcenter.calendarweather.planet.PlanetMainFragment;
import com.iexamcenter.calendarweather.sunmoon.SunMoonMainFragment;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;


/**
 * Created by sasikanta on 11/14/2017.
 * HomePagerAdapter
 */

class HomePagerAdapter extends FragmentPagerAdapter {
    private static String[] pageTitle;
    MainActivity activity;
    PrefManager mPref;

    private HomePagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.activity = (MainActivity) activity;
        pageTitle = subPageTitle;
        mPref = PrefManager.getInstance(activity);
        mPref.load();

    }

    public static HomePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        return new HomePagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        Calendar calendar = Calendar.getInstance();
        Bundle b = new Bundle();
        switch (position) {


            case 0:
                fragment = HoroscopeFrag.newInstance();
                args = new Bundle();
                args.putInt(HoroscopeFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                break;

            case 1:
                fragment = SunMoonMainFragment.newInstance();
                args = new Bundle();

                b.putInt("MONTH", calendar.get(Calendar.MONTH));
                b.putInt("YEAR", calendar.get(Calendar.YEAR));
                args.putInt(SunMoonMainFragment.ARG_POSITION, position);
                fragment.setArguments(args);

                break;

            default:
                fragment = PlanetMainFragment.newInstance();
                args = new Bundle();
                args.putInt(PlanetMainFragment.ARG_POSITION, position);
                fragment.setArguments(args);


                break;

        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Horoscope";
            case 1:
                return "Sun Moon";

            default:

                return "Planet";


        }

    }

    @Override
    public int getCount() {
      //  if (mPref.getMyLanguage().contains("or"))
            return 2;
       // else
         //   return 2;
    }
}
