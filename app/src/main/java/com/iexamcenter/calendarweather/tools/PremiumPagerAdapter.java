package com.iexamcenter.calendarweather.tools;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.kundali.KundaliMainFragment;
import com.iexamcenter.calendarweather.kundali.KundaliMatchFrag;
import com.iexamcenter.calendarweather.planet.PlanetMainFragment;


/**
 * Created by sasikanta on 11/14/2017.
 * OmgPagerAdapter
 */

class PremiumPagerAdapter extends FragmentPagerAdapter {

    MainActivity activity;

    private PremiumPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }

    public static PremiumPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {

        return new PremiumPagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        switch (position){
            case 0:
                fragment = CalculatorMainFragment.newInstance();
                args = new Bundle();
                args.putInt(CalculatorMainFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            default:
                fragment = YogaMainFragment.newInstance();
                args = new Bundle();
                args.putInt(YogaMainFragment.ARG_POSITION, position);
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
                return "Panchanga Calculator";
            case 1:
                return "Auspicious Days";
            default:
                return "Event Day";
        }


    }

    @Override
    public int getCount() {
        return 2;
    }
}
