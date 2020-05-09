package com.iexamcenter.calendarweather.kundali;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.MainActivity;


/**
 * Created by sasikanta on 11/14/2017.
 * MuhurtaPagerAdapter
 */

class KundaliMatchPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle = {"Boy/Groom Birth Details", "Girl/Bride Birth Details"};
    MainActivity activity;
    Bundle args;

    private KundaliMatchPagerAdapter(FragmentManager fm, FragmentActivity activity, Bundle args) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.args = args;
        this.activity = (MainActivity) activity;
    }

    public static KundaliMatchPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, Bundle args) {

        return new KundaliMatchPagerAdapter(fm, activity, args);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;


        switch (position) {
            case 0:
                Bundle args1 = new Bundle();
                fragment = KundaliInfoFrag.newInstance();

                args1.putString("TYPE", "KUNDALI_MATCH");
                args1.putInt(KundaliDiagramFrag.ARG_POSITION, position);
                args1.putString("area", args.getString("area1"));
                args1.putString("latitude", args.getString("latitude1"));
                args1.putString("longitude", args.getString("longitude1"));
                args1.putInt("year", args.getInt("year1"));
                args1.putInt("month", args.getInt("month1"));
                args1.putInt("dayOfmonth", args.getInt("dayOfmonth1"));
                args1.putInt("hour", args.getInt("hour1"));
                args1.putInt("min", args.getInt("min1"));
                fragment.setArguments(args1);
                return fragment;
            case 1:
                Bundle args2 = new Bundle();
                fragment = KundaliInfoFrag.newInstance();
                args2.putString("TYPE", "KUNDALI_MATCH");
                args2.putInt(KundaliDiagramFrag.ARG_POSITION, position);
                args2.putString("area", args.getString("area2"));
                args2.putString("latitude", args.getString("latitude2"));
                args2.putString("longitude", args.getString("longitude2"));
                args2.putInt("year", args.getInt("year2"));
                args2.putInt("month", args.getInt("month2"));
                args2.putInt("dayOfmonth", args.getInt("dayOfmonth2"));
                args2.putInt("hour", args.getInt("hour2"));
                args2.putInt("min", args.getInt("min2"));
                fragment.setArguments(args2);
                return fragment;


        }
        return null;

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       /* Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_nav_quote);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + pageTitle[position]);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_nav_quote);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        return pageTitle[position];//sb;

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
