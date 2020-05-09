package com.iexamcenter.calendarweather.weather;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.iexamcenter.calendarweather.CenteredImageSpan;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;


/**
 * Created by sasikanta on 11/14/2017.
 * WeatherPagerAdapter
 */

class WeatherPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;
    MainActivity activity;

    private WeatherPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.activity = (MainActivity) activity;
        pageTitle = subPageTitle;

    }

    public static WeatherPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        return new WeatherPagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        switch (position){

            default:
                fragment = WeatherPagerFragment.newInstance();
                args = new Bundle();
                args.putInt(WeatherPagerFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_home);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + pageTitle[position]);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_home);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;

        // return pageTitle[position];

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
