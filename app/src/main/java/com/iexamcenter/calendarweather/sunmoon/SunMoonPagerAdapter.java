package com.iexamcenter.calendarweather.sunmoon;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;


/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerAdapter
 */

class SunMoonPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle={"Basic","Details"};
    MainActivity activity;

    private SunMoonPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public static SunMoonPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new SunMoonPagerAdapter(fm, activity);
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = SunmoonFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(SunmoonFragment.ARG_POSITION, position);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
