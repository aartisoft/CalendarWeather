package com.iexamcenter.calendarweather.quote;

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

import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * QuoteMainFragment
 */

public class QuoteMainFragment extends Fragment {
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    ViewPager tabViewPager;
   /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int page = intent.getIntExtra("PAGE",0);
            int section = intent.getIntExtra("SECTION",0);

            if(page==4){
                tabViewPager.setCurrentItem(section-1);
            }

        }
    };*/
    public static QuoteMainFragment newInstance() {
        Log.e("getInstance","getInstance:QuoteMainFragment");
        return new QuoteMainFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main, container, false);
           DateFormat dateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
       // activity.toolbar.setTitle("Legends Quotes");
      //  activity.toolbar.setSubtitle(today);
        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setHasOptionsMenu(true);
        setUp(rootView);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getPageSubpage().observe(getViewLifecycleOwner(), pageSubpage -> {

            String[] page=pageSubpage.split("_");
            if (Integer.parseInt(page[0]) == 3) {
                tabViewPager.setCurrentItem(Integer.parseInt(page[1]) - 1);
            }

        });
        return rootView;
    }


public TabLayout getTabLayout(){
        return tabLayout;
}
    protected void setUp(View rootView) {

        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
      //  tabViewPager.setOffscreenPageLimit(3);
        tabViewPager.setAdapter(QuotePagerAdapter.newInstance(getChildFragmentManager(), activity));
        tabViewPager.setCurrentItem(CalendarWeatherApp.getSelectedSubPage()-1);
    }

}