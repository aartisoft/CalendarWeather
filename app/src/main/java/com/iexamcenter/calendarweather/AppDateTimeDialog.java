package com.iexamcenter.calendarweather;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.utility.PrefManager;


public class AppDateTimeDialog extends DialogFragment {
    public static int cntTab = 0;
    public static final String ARG_POSITION = "position";
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;
    MainViewModel viewModel;
    View view;
    static int mType;
    static int selYear, selMonth, selDate, selHour, selMin;
    ViewPager tabViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());

    }

    public static AppDateTimeDialog newInstance(MainActivity ctx) {
        AppDateTimeDialog f = new AppDateTimeDialog();
        mContext = ctx;
        cntTab = 0;
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_date_time, container);
        Bundle b = getArguments();
        mType = b.getInt(ARG_POSITION);
        Log.e("mType", "mTypemType:1:" + mType);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cntTab == 0) {
                    int currPg = tabViewPager.getCurrentItem();
                    if (currPg == 0)
                        tabViewPager.setCurrentItem(1);
                    else if (currPg == 1)
                        tabViewPager.setCurrentItem(0);

                    return;
                }

                Log.e("mType", "mTypemType:2:" + mType);
                if (mType == 100 || mType == 101) {
                    viewModel.setDateTimePicker1("");
                } else if (mType == 200 || mType == 201) {
                    viewModel.setDateTimePicker2("");
                } else {
                    viewModel.setDateTimePicker("");
                }

                AppDateTimeDialog.this.dismiss();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDateTimeDialog.this.dismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // int screenWidth = (int) (metrics.widthPixels * 0.8);
        // int screenHeight = (int) (metrics.heightPixels * 0.9);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        tabViewPager = view.findViewById(R.id.tabViewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(AppDateTimePagerAdapter.newInstance(getChildFragmentManager(), mContext, mType));
        tabViewPager.setCurrentItem(mType);

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cntTab++;
                Log.e("onPageSelected", ":::onPageSelected::" + cntTab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

