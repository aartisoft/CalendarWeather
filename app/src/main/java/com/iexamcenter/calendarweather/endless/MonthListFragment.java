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
import androidx.lifecycle.ViewModelProviders;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sasikanta on 11/14/2017.
 * MonthListFragment
 */
//LoaderManager.LoaderCallbacks<Cursor>
public class MonthListFragment extends Fragment {
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

    public static MonthListFragment newInstance() {


        return new MonthListFragment();
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

    private String readFromFile(String fileName, Context context) {

        String ret = "";

        //  }
        try {
            InputStream inputStream = context.openFileInput("mycache-"+mPref.getMyLanguage()+"-"+ fileName +".txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String fileName, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mycache-"+mPref.getMyLanguage()+"-"+ fileName +".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_calendar_endless_month, container, false);

        res = getResources();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        pagePosition = args.getInt(ARG_POSITION) - pagerCurrPos;


        activity = (MainActivity) getActivity();

        mPref = PrefManager.getInstance(activity);
        mPref.load();
        currLang = mPref.getMyLanguage();

        monthFestivalmap = new HashMap<>();
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

        panchangInfo = rootView.findViewById(R.id.endlesscal);
        fab = rootView.findViewById(R.id.fab);
        fab.hide();
        progressbar = rootView.findViewById(R.id.progressbar);


        Bundle bundle1 = new Bundle();
        bundle1.putInt("displayYearInt", displayYearInt);
        bundle1.putInt("displayMonthInt", displayMonthInt);

        //  getLoaderManager().restartLoader(LOADER_COREDATA, bundle1, MonthListFragment.this);


        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        cal1.set(Calendar.MONTH, displayMonthInt);
        cal1.set(Calendar.YEAR, (100 + displayYearInt));
        long curr = cal1.getTimeInMillis();

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        String fileName = displayYearInt + "-" + displayMonthInt;

        Gson gson = new Gson();
        String json1 = readFromFile(fileName, activity);

        if (!json1.isEmpty()) {
            Log.e("TIMETAKENVAL", "fileName:::" + fileName);
            progressbar.setVisibility(View.GONE);

            MonthData obj = gson.fromJson(json1, MonthData.class);
            md.add(obj);
            fab.show();
        } else {
            System.out.println("RELEASECHECK:1:fileName:::" + fileName);
            viewModel.getEphemerisData(curr, 1).observe(getViewLifecycleOwner(), obj -> {
                System.out.println("RELEASECHECK:1:fileName:::null" );
                if (obj != null && obj.size() != 0) {
                    System.out.println("RELEASECHECK:1:fileName:::null"+obj.size() );
                    PanchangTask ptObj = new PanchangTask();
                    HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), activity);
                    progressbar.setVisibility(View.VISIBLE);
                    new handlecalendarTask(myPanchangHashMap).execute();
                }
            });
        }

        mPanchangAdapter = new MonthListAdapter(activity, md, pagePosition);
        mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        panchangInfo.setLayoutManager(mLayoutManager);
        panchangInfo.setAdapter(mPanchangAdapter);


        fab.setOnClickListener(v -> {
            if (v.getTag().toString().contains("1")) {
                v.setTag("0");
                panchangInfo.scrollBy(100000, 100000);
            } else {
                v.setTag("1");
                panchangInfo.scrollBy(-100000, -100000);
            }
        });

        return rootView;
    }


    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap) {
        try {

            Log.e("CalendarData", "CalendarData:1:" + System.currentTimeMillis());
            mDayViewHashMap.clear();
            monthFestivalmap.clear();
            if (!mAllPanchangHashMap.isEmpty()) {
                Calendar mycal = new GregorianCalendar(displayYearInt, displayMonthInt, displayDayInt);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                String fileName = displayYearInt + "-" + displayMonthInt;
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
                System.out.println("RELEASECHECK:1:fileName:::mdObj"+mdObj.mDayViewHashMap.size() );

                Gson gson = new Gson();
                String json = gson.toJson(mdObj);
                writeToFile(fileName, json, activity);
                //  String json1 = readFromFile(fileName, activity);
                //  MonthData obj = gson.fromJson(json1, MonthData.class);
                // md.add(obj);
                //  Log.e("CalendarData", "CalendarData:json:::::" + obj.mDayViewHashMap.size() + ":" + obj.monthFestivalmap.size());
                //  Log.e("CalendarData", "CalendarData:json:" + json);
                // Log.e("CalendarData", "CalendarData:json::"+System.currentTimeMillis());
                //   progressbar.setVisibility(View.GONE);
                //  mPanchangAdapter.notifyDataSetChanged();
                // fab.show();


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
            Log.e("TIMETAKENVAL", "TIMETAKENVAL:4:" + System.currentTimeMillis());
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
