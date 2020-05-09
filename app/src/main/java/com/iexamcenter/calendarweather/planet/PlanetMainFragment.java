package com.iexamcenter.calendarweather.planet;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * ChoghadiaMainFragment
 */

public class PlanetMainFragment extends Fragment {
    public static final String ARG_POSITION = "POSITION";
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    PrefManager mPref;
    PlanetPagerAdapter adapter;

    public static PlanetMainFragment newInstance() {

        return new PlanetMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  activity.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main_planet, container, false);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        //  activity.showHideBottomNavigationView(false);

        //  activity.enableBackButtonViews(true);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        res = activity != null ? activity.getResources() : null;

        setUp(rootView);
        return rootView;
    }


    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {
        ViewPager tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        adapter = PlanetPagerAdapter.newInstance(getChildFragmentManager(), activity);
        tabViewPager.setAdapter(adapter);
      //  MovableFloatingActionButton fab = rootView.findViewById(R.id.fab);

     /*   if (CalendarWeatherApp.isPanchangEng) {
            fab.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_eng);
        }
        if (mPref.getMyLanguage().contains("en")) {
            fab.hide();
        } else {
            fab.show();
        }
        fab.setOnClickListener(v -> {
            CalendarWeatherApp.updateAppResource(activity.getResources(),activity);
            if (!CalendarWeatherApp.isPanchangEng) {
                fab.setImageResource(R.drawable.ic_chevron_left_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_eng);
            }
            CalendarWeatherApp.isPanchangEng = !CalendarWeatherApp.isPanchangEng;
            adapter.notifyDataSetChanged();
        });*/
    }


}