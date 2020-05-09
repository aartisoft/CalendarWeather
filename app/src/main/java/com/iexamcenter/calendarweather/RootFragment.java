package com.iexamcenter.calendarweather;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iexamcenter.calendarweather.utility.NonScrollViewPager;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 12/1/2017.
 * RootFragment
 */

public class RootFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    NonScrollViewPager mPager;
    MainActivity activity;
    // BroadcastReceiver langChngReceiver;
    RootPagerAdapter mPagerAdapter;
    PrefManager mPref;
    ArrayList<Integer> al;
  /*  private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int page = intent.getIntExtra("PAGE", 0);
            mPager.setCurrentItem(page - 1);
        }
    };*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new RootFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_root_page, container, false);
        bottomNavigationView = activity.bottomNavigationView;
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        setRetainInstance(true);
        setUp(rootView);



        viewModel.getPageSubpage().observe(getViewLifecycleOwner(), pageSubpage -> {

            String[] page=pageSubpage.split("_");
            mPager.setCurrentItem(Integer.parseInt(page[0]) - 1);

        });
        viewModel.getCurrLang().observe(getViewLifecycleOwner(), lang -> {
            mPref.load();
            al.clear();
            al.add(1);
            al.add(1);
            al.add(1);
            al.add(1);

            CalendarWeatherApp.updateAppResource(activity.getResources(), activity);
            mPager.getAdapter().notifyDataSetChanged();

        });
        viewModel.getEngChanged().observe(getViewLifecycleOwner(), lang -> {
            mPref.load();
            al.clear();
            al.add(1);
            al.add(1);
            al.add(1);
            al.add(1);

            CalendarWeatherApp.updateAppResource(activity.getResources(), activity);
            mPager.getAdapter().notifyDataSetChanged();

        });


        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp(View rootView) {
        mPager = rootView.findViewById(R.id.pager);
        mPager.setPagingEnabled(true);

        al = new ArrayList<>();
        if (!mPref.isFirstUse()) {
            al.add(1);
            al.add(1);
            al.add(1);
            al.add(1);
        }

        mPagerAdapter = new RootPagerAdapter(getChildFragmentManager(), activity, al);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(CalendarWeatherApp.getSelectedPage() - 1);
        bottomNavigationView.getMenu().getItem(CalendarWeatherApp.getSelectedPage() - 1).setChecked(true);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                activity.setVisiblePagePosition(position);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int position = item.getItemId();
            switch (position) {

                case R.id.nav_home:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.nav_quote:
                    mPager.setCurrentItem(2);
                    break;
                case R.id.nav_media:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.nav_other:
                    mPager.setCurrentItem(3);
                    break;
              //  case R.id.nav_calendar:
                //    mPager.setCurrentItem(1);
                 //   break;
            }


            return false;
        });


    }


}
