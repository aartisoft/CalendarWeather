package com.iexamcenter.calendarweather.endless;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * Created by sasikanta on 11/14/2017.
 * CalendarHomePagerAdapter
 */

class MonthPagerAdapter extends FragmentPagerAdapter {


    private MonthPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT      );

    }

    public static MonthPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new MonthPagerAdapter(fm, activity);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {


        Fragment fragment = MonthListFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(MonthListFragment.ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;


    }



    @Override
    public int getCount() {
        return 200*12;
    }
}
