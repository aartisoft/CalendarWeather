package com.iexamcenter.calendarweather;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.kundali.DateFragment;
import com.iexamcenter.calendarweather.kundali.TimeFragment;


/**
 * Created by sasikanta on 11/14/2017.
 * MuhurtaPagerAdapter
 */

class AppDateTimePagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle = {"DATE", "TIME"};
    MainActivity activity;
    int mPage;

    private AppDateTimePagerAdapter(FragmentManager fm, FragmentActivity activity,int page) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPage=page;

        this.activity = (MainActivity) activity;
    }

    public static AppDateTimePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity,int page) {
        return new AppDateTimePagerAdapter(fm, activity,page);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        Log.e("mType","mTypemType:3:"+mPage);
        switch (position) {
            case 0:
                Log.e("MYDATETIME","MYDATETIME::4");
                fragment = DateFragment.newInstance();
                args = new Bundle();
                args.putInt(DateFragment.ARG_POSITION, mPage);
                fragment.setArguments(args);
                return fragment;
            case 1:
                Log.e("MYDATETIME","MYDATETIME::5");
                fragment = TimeFragment.newInstance();
                args = new Bundle();
                args.putInt(TimeFragment.ARG_POSITION, mPage);
                fragment.setArguments(args);
                return fragment;
        }
        return null;

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
