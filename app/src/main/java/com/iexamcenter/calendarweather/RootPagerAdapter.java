package com.iexamcenter.calendarweather;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.today.HomeMainFragment;
import com.iexamcenter.calendarweather.home.CalendarPagerHomeFragment;
import com.iexamcenter.calendarweather.more.MoreMainFragment;
import com.iexamcenter.calendarweather.omg.OmgMainFragment;
import com.iexamcenter.calendarweather.quote.QuoteMainFragment;
import com.iexamcenter.calendarweather.tools.CalculatorMainFragment;
import com.iexamcenter.calendarweather.tools.PremiumMainFragment;

import java.util.ArrayList;

/**
 * Created by sasikanta on 11/14/2017.
 * RootPagerAdapter
 */

public class RootPagerAdapter extends FragmentPagerAdapter {
    private Resources res;
    ArrayList<Integer> al;

    RootPagerAdapter(FragmentManager fm, MainActivity activity,ArrayList<Integer> al1) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
      //  MainActivity activity1 = activity;
        al=al1;
        res = activity.getResources();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                int page = 0;
                Fragment fragment = CalendarPagerHomeFragment.newInstance();

                Bundle args = new Bundle();
                args.putInt(CalendarPagerHomeFragment.ARG_POSITION, page);

                fragment.setArguments(args);
                return fragment;

          // case 1:
           //   return HomeMainFragment.newInstance(res.getStringArray(R.array.tab_home));
             case 2:
                return PremiumMainFragment.newInstance(res.getStringArray(R.array.tab_omg));
            case 1:
                return OmgMainFragment.newInstance(res.getStringArray(R.array.tab_omg));
           // case 3:
             //   return QuoteMainFragment.newInstance();
            default:
                return MoreMainFragment.newInstance();


        }

    }

    @Override
    public int getCount() {
        return al.size();
    }

}
