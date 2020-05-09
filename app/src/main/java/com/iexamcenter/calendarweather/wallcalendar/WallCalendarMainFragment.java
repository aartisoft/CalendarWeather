package com.iexamcenter.calendarweather.wallcalendar;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.PopupMenu;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * PanchangMainFragment
 */

public class WallCalendarMainFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    MainActivity mContext;
    Resources res;
    TabLayout tabLayout;
    PrefManager mPref;
    ImageView cal_menu;
    ViewPager tabViewPager;
    WallCalendarPagerAdapter adapter;

    public static WallCalendarMainFragment newInstance() {

        return new WallCalendarMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
        // mContext.unregisterReceiver(mUpdateLocationReceiver);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mContext.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_wall_calendar_main, container, false);
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);

        // DateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.US);
        //  Date date = new Date();
        //  String today = dateFormat.format(date);
        //  mContext.toolbar.setTitle("Wall Calendar");
        //   mContext.toolbar.setSubtitle(today);
        mPref = PrefManager.getInstance(mContext);
        mPref.load();

        res = mContext != null ? mContext.getResources() : null;
        setRetainInstance(true);
        setHasOptionsMenu(true);


        setUp(rootView);
        return rootView;
    }


    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {
        cal_menu = rootView.findViewById(R.id.cal_menu);
        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        // PrefManager mPref= PrefManager.getInstance(mContext);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // mPref.load();

        tabLayout.setupWithViewPager(tabViewPager);

        setmyadapter(false);

        if (mPref.getMyLanguage().contains("or")) {
            cal_menu.setVisibility(View.VISIBLE);
        } else {
            cal_menu.setVisibility(View.GONE);
        }
        cal_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });


    }

    public void setmyadapter(boolean isStateGovt) {

        adapter = WallCalendarPagerAdapter.newInstance(getChildFragmentManager(), mContext, isStateGovt);

        tabViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {


            case R.id.nav_kohinoor:

                mPref.setPublication("kohinoor");
                mPref.load();
                tabViewPager.getAdapter().notifyDataSetChanged();
                break;
            case R.id.nav_biraja:
                mPref.setPublication("biraja");
                mPref.load();
                tabViewPager.getAdapter().notifyDataSetChanged();

                break;
            case R.id.nav_radharamana:
                mPref.setPublication("radharamana");
                mPref.load();
                tabViewPager.getAdapter().notifyDataSetChanged();

                break;
            case R.id.nav_bhagyadipa:
                mPref.setPublication("bhagyadipa");
                mPref.load();
                tabViewPager.getAdapter().notifyDataSetChanged();
                break;
            case R.id.nav_govt:
                mPref.setPublication("govtcal");
                mPref.load();
                tabViewPager.getAdapter().notifyDataSetChanged();
                break;

        }


        return false;
    }

    public void showPopup(View v) {

        PopupMenu popup = new PopupMenu(mContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        Menu m = popup.getMenu();
//Log.e("getMyLanguage","-getMyLanguage:-"+mPref.getMyLanguage().contains("or"));

        inflater.inflate(R.menu.wallcalendar, m);


        popup.show();
    }
}