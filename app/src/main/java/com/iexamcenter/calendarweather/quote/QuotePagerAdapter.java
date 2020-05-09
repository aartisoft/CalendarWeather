package com.iexamcenter.calendarweather.quote;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;

import com.iexamcenter.calendarweather.MainActivity;


/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerAdapter
 */

class QuotePagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle={"Category","National","International"};
    MainActivity activity;

    private QuotePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }

    public static QuotePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new QuotePagerAdapter(fm, activity);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = QuotePagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(QuotePagerFragment.ARG_POSITION, position);
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
