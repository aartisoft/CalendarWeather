package com.iexamcenter.calendarweather.omg;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.horoscope.HoroscopeFrag;
import com.iexamcenter.calendarweather.kundali.KundaliMainFragment;
import com.iexamcenter.calendarweather.kundali.KundaliMatchFrag;
import com.iexamcenter.calendarweather.news.ListFragment;
import com.iexamcenter.calendarweather.planet.PlanetMainFragment;
import com.iexamcenter.calendarweather.youtube.MantraFragment;


/**
 * Created by sasikanta on 11/14/2017.
 * OmgPagerAdapter
 */

class OmgPagerAdapter extends FragmentPagerAdapter {

    MainActivity activity;

    private OmgPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }

    public static OmgPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {

        return new OmgPagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        switch (position) {

            case 0:
                fragment = KundaliMainFragment.newInstance();
                args = new Bundle();
                args.putInt(KundaliMainFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = KundaliMatchFrag.newInstance();
                args = new Bundle();
                args.putInt(KundaliMatchFrag.ARG_POSITION, position);
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
                return "Birth Kundali";
            case 1:
                return "Kundali Match";
            default:
                return "Planet";
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
}
