package com.iexamcenter.calendarweather.more;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.news.ListFragment;
import com.iexamcenter.calendarweather.observance.ObservanceFragment;
import com.iexamcenter.calendarweather.quote.QuoteMainFragment;
import com.iexamcenter.calendarweather.thisday.ThisDayFragment;
import com.iexamcenter.calendarweather.youtube.MantraFragment;

import java.util.Calendar;


/**
 * Created by sasikanta on 11/14/2017.
 * ChoghadiaPagerAdapter
 */

class MorePagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;
    MainActivity activity;

    private MorePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
        pageTitle = activity.getResources().getStringArray(R.array.l_arr_bara);
    }

    public static MorePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new MorePagerAdapter(fm, activity);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        Calendar calendar = Calendar.getInstance();
        Bundle args;
        switch (position) {


            case 1:
                fragment = QuoteMainFragment.newInstance();
                args = new Bundle();
                args.putInt("MONTH", calendar.get(Calendar.MONTH));
                args.putInt("YEAR", calendar.get(Calendar.YEAR));
                fragment.setArguments(args);
                return fragment;
            case 4:
                fragment = ObservanceFragment.newInstance();
                args = new Bundle();
                args.putInt("MONTH", calendar.get(Calendar.MONTH));
                args.putInt("YEAR", calendar.get(Calendar.YEAR));
                fragment.setArguments(args);
                return fragment;
            case 0:
                fragment = MantraFragment.newInstance();
                args = new Bundle();
                args.putInt(MantraFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;

            case 3:
                args = new Bundle();
                fragment = ThisDayFragment.newInstance();

                args.putInt("MONTH", calendar.get(Calendar.MONTH));
                args.putInt("YEAR", calendar.get(Calendar.YEAR));
              //  args.putInt(ThisDayFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;
            case 2:
                fragment = ListFragment.getInstance();
                args = new Bundle();
                args.putInt(ListFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;

        }
        return null;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 4:
                return "Observance";

            case 1:
                return "Quote";
            case 0:
                return "Aradhana";
            case 3:
                return "This Day";
            case 2:
                return "Media";
        }

        return "";

    }

    @Override
    public int getCount() {
        return 5;
    }
}
