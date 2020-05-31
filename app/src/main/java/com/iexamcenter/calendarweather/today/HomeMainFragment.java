package com.iexamcenter.calendarweather.today;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * QuoteMainFragment
 */

public class HomeMainFragment extends Fragment {
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    static String[] subPagesTitle;
    ViewPager tabViewPager;

    public static HomeMainFragment newInstance(String[] subPages) {
        subPagesTitle = subPages;
        return new HomeMainFragment();
    }


   /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int page = intent.getIntExtra("PAGE", 0);
            int section = intent.getIntExtra("SECTION", 0);

            if (page == 2) {
                tabViewPager.setCurrentItem(section - 1);
            }

        }
    };*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("PrefManager", "PrefMaPrefManagernager9");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main, container, false);

        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);

        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setUp(rootView);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getPageSubpage().observe(getViewLifecycleOwner(), pageSubpage -> {

            String[] page=pageSubpage.split("_");
            if (Integer.parseInt(page[0]) == 2) {
                tabViewPager.setCurrentItem(Integer.parseInt(page[1]) - 1);
            }

        });
        Log.e("PrefManager", "PrefMaPrefManagernager10");
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {
        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(tabViewPager);

        tabViewPager.setAdapter(HomePagerAdapter.newInstance(getChildFragmentManager(), activity, subPagesTitle));
        tabViewPager.setPageTransformer(true, new DepthPageTransformer());
       // tabViewPager.setOffscreenPageLimit(3);
        tabViewPager.setCurrentItem(CalendarWeatherApp.getSelectedSubPage() - 1);

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // Date date = new Date();
                // String today = dateFormat.format(date);
                // activity.toolbar.setTitle(today);
                //  activity.toolbar.setSubtitle("");


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}