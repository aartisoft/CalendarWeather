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

class KundaliPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle = {"Diagram", "Information", "Dasha"};
    MainActivity activity;

    private KundaliPagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }

    public static KundaliPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity) {

        return new KundaliPagerAdapter(fm, activity);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;

        switch (position) {
            case 0:
                fragment = KundaliDiagramFrag.newInstance();
                args = new Bundle();
                args.putInt(KundaliDiagramFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;
            case 1:
                fragment = KundaliInfoFrag.newInstance();
                args = new Bundle();
                args.putInt(KundaliInfoFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;

            default:
                fragment = KundaliDashaFrag.newInstance();
                args = new Bundle();
                args.putInt(KundaliDashaFrag.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;
        }


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











