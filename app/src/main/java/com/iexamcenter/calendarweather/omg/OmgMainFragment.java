package com.iexamcenter.calendarweather.omg;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
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

public class OmgMainFragment extends Fragment {
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    static String[] subPagesTitle;
    ViewPager tabViewPager;
    public static OmgMainFragment newInstance(String[] subPages) {
        subPagesTitle=subPages;
        return new OmgMainFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }
    /*private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int page = intent.getIntExtra("PAGE",0);
            int section = intent.getIntExtra("SECTION",0);

            if(page==3){
                tabViewPager.setCurrentItem(section-1);
            }

        }
    };*/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main, container, false);

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
       // LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver,new IntentFilter(AppConstants.GOTOPAGE));
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // LocalBroadcastManager.getInstance(activity).unregisterReceiver(mMessageReceiver);
    }

    public TabLayout getTabLayout(){
        return tabLayout;
}
    protected void setUp(View rootView) {
         tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setPageTransformer(true, new DepthPageTransformer());
       // tabViewPager.setOffscreenPageLimit(2);
        tabViewPager.setAdapter(OmgPagerAdapter.newInstance(getChildFragmentManager(), activity,subPagesTitle));
        tabViewPager.setCurrentItem(CalendarWeatherApp.getSelectedSubPage()-1);
       // if(activity.getVisiblePagePosition()==1) {
         //   activity.toolbar.setTitle("Today Updates");
          //  activity.toolbar.setSubtitle("From trusted channel");
        //}
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               // activity.toolbar.setTitle("Today Updates");
                //activity.toolbar.setSubtitle("From trusted channel");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}