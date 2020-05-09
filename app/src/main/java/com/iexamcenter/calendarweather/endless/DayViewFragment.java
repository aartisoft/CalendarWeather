package com.iexamcenter.calendarweather.endless;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangListAdapter;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.os.Looper.getMainLooper;

public class DayViewFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";

    public static boolean active = false;
    PanchangListAdapter mPanchangAdapter;
    private RecyclerView panchangInfo;
    MainActivity mContext;
    Resources res;
    HashMap<String, ArrayList<String>> monthFestivalmap;

    HashMap<String, CoreDataHelper> mDayViewHashMap;
    int displayYearInt, displayDayInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt, selectedDay, selectedMonth, selectedYear, pagePos;
    View rootView;

    public static Fragment newInstance() {
        return new DayViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        CalendarWeatherApp.updateAppResource(mContext.getResources(), mContext);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        pagePos = args.getInt(ARG_POSITION);
        rootView = inflater.inflate(R.layout.fragment_day_info, container, false);

        res = getResources();


        selectedDay = args.getInt("DAY");
        selectedMonth = args.getInt("MONTH");
        selectedYear = args.getInt("YEAR");

        Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);
        displayYearInt = selectedYear;
        displayMonthInt = selectedMonth;
        displayDayInt = selectedDay;


        HashMap<String, CoreDataHelper> mDayViewHashMap1;
        HashMap<String, ArrayList<String>> monthFestivalmap1;


        mDayViewHashMap1 = CalendarWeatherApp.getHashMapAllPanchang1();
        monthFestivalmap1 = CalendarWeatherApp.getHashMapAllFest();


        PrefManager mPref = PrefManager.getInstance(mContext);
        mPref.load();
        monthFestivalmap = new HashMap<>();
        mDayViewHashMap = new HashMap<>();


        mDayViewHashMap.putAll(mDayViewHashMap1);
        monthFestivalmap.putAll(monthFestivalmap1);


        panchangInfo = rootView.findViewById(R.id.panchangInfo);


        mPanchangAdapter = new PanchangListAdapter(mContext, mDayViewHashMap, monthFestivalmap, displayMonthInt, displayYearInt, displayDayInt, pagePos);
        LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        panchangInfo.setLayoutManager(riseSetll);

        panchangInfo.setAdapter(mPanchangAdapter);

        Handler mainHandler = new Handler(getMainLooper());
        Runnable runnable = () ->  riseSetll.scrollToPositionWithOffset(selectedDay - 1, 0);
        mainHandler.postDelayed(runnable, 200);

        return rootView;
    }


}
