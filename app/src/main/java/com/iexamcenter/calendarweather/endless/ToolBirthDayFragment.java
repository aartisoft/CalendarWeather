package com.iexamcenter.calendarweather.endless;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.FestivalData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.utility.MovableFloatingActionButton;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by sasikanta on 11/14/2017.
 * MonthListFragment
 */
//LoaderManager.LoaderCallbacks<Cursor>
public class ToolBirthDayFragment extends Fragment {
    String duplicateTimestamp;
    MovableFloatingActionButton fab;
    HashMap<String, CoreDataHelper> mDayViewHashMap;
    private static final int LOADER_COREDATA = 67000;
    RecyclerView panchangInfo;
    ProgressBar progressbar;
    MonthListAdapter mPanchangAdapter;
    public static String ARG_POSITION = "CURRENT_MONTH";
    HashMap<String, ArrayList<String>> monthFestivalmap;
    Resources res;
    ViewGroup rootView;
    String currLang;
    MainActivity activity;

    int displayYearInt, displayDayInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt;

    PrefManager mPref;
    ArrayList<MonthData> md;
    LinearLayoutManager mLayoutManager;
    int pagePosition;
    int pagerCurrPos = MonthPagerFragment.pagerCurrPos;


    @Override
    public void onResume() {
        super.onResume();
        duplicateTimestamp = null;
        activity = (MainActivity) getActivity();
        CalendarWeatherApp.updateAppResource(activity.getResources(), activity);


    }

    public static ToolBirthDayFragment newInstance() {


        return new ToolBirthDayFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (MainActivity) context;


    }

    public static class MonthData {
        HashMap<String, CoreDataHelper> mDayViewHashMap;
        HashMap<String, ArrayList<String>> monthFestivalmap;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tool_birth_day, container, false);

        res = getResources();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();

        activity = (MainActivity) getActivity();

        mPref = PrefManager.getInstance(activity);
        mPref.load();
        currLang = mPref.getMyLanguage();
        mDayViewHashMap = new HashMap<>();

        md = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.MONTH, pagePosition);


        displayYearInt = cal.get(Calendar.YEAR);
        displayMonthInt = cal.get(Calendar.MONTH);
        displayDayInt = 1;


        Bundle bundle1 = new Bundle();
        bundle1.putInt("displayYearInt", displayYearInt);
        bundle1.putInt("displayMonthInt", displayMonthInt);


        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        cal1.set(Calendar.MONTH, displayMonthInt);
        cal1.set(Calendar.YEAR, (100 + displayYearInt));
        long curr = cal1.getTimeInMillis();

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        String fileName = displayYearInt + "-" + displayMonthInt;


              viewModel.getEphemerisData(curr, 1).observe(getViewLifecycleOwner(), obj -> {
                 if (obj != null && obj.size() != 0) {
                      PanchangTask ptObj = new PanchangTask();
                    HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), activity);

                    new handlecalendarTask(myPanchangHashMap).execute();
                }
            });



        return rootView;
    }


    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap) {
        try {

            mDayViewHashMap.clear();
            if (!mAllPanchangHashMap.isEmpty()) {
                Calendar mycal = new GregorianCalendar(displayYearInt, displayMonthInt, displayDayInt);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= daysInMonth; i++) {
                    String key = displayYearInt + "-" + displayMonthInt + "-" + i;
                    CoreDataHelper myPanchangObj = mAllPanchangHashMap.get(key);

                    mDayViewHashMap.put(key, myPanchangObj);

                    ArrayList<String> myFestivallist = FestivalData.calculateFestival(myPanchangObj, currLang, activity);
                    if (!myFestivallist.get(0).isEmpty()) {
                        String festkey = myPanchangObj.getmYear() + "-" + myPanchangObj.getmMonth() + "-" + myPanchangObj.getmDay();

                        monthFestivalmap.put(festkey, myFestivallist);

                    }
                }
                md.clear();
                MonthData mdObj = new MonthData();
                mdObj.monthFestivalmap = monthFestivalmap;
                mdObj.mDayViewHashMap = mAllPanchangHashMap;
                md.add(mdObj);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class handlecalendarTask extends AsyncTask<String, Integer, Long> {
        HashMap<String, CoreDataHelper> myPanchangHashMap;

        public handlecalendarTask(HashMap<String, CoreDataHelper> panchangHashMap) {
            myPanchangHashMap = panchangHashMap;
        }

        protected Long doInBackground(String... urls) {
              setCalendarData(myPanchangHashMap);
            return 1L;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        protected void onPostExecute(Long result) {
            Log.e("TIMETAKENVAL", "TIMETAKENVAL:5:" + System.currentTimeMillis());
            progressbar.setVisibility(View.GONE);
            mPanchangAdapter.notifyDataSetChanged();
            fab.show();
            Log.e("TIMETAKENVAL", "TIMETAKENVAL:6:" + System.currentTimeMillis());
        }
    }
}
