package com.iexamcenter.calendarweather.thisday;

import android.os.Bundle;
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

class FamousPeoplePagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle={"History","Quotes"};
    MainActivity activity;
    static String mHistory,mPkey;

    private FamousPeoplePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.activity = (MainActivity) activity;
    }

    public static FamousPeoplePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String history, String pkey) {
        mHistory=history;
        mPkey=pkey;
        return new FamousPeoplePagerAdapter(fm, activity);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = FamousPeopleFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt("TYPE", position);
        args.putString("HISTORY", mHistory);
        args.putString("PKEY", mPkey);
        fragment.setArguments(args);
        return fragment;
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
