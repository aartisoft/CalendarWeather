package com.iexamcenter.calendarweather.festival;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.DatePickerFragment;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.FestivalData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class FestivalFragment extends Fragment {

    PrefManager mPref;
    String lang;
    Resources res;

    int displayYearInt, displayDayInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt;

    String currLang;
    int pos = 0;
    HashMap<String, CoreDataHelper> mDayViewHashMap;
    long dispYearMilli;
    Calendar todayCal;
    HashMap<String, ArrayList<String>> monthFestivalmap;
    ArrayList<MonthData> md;
    Button dispYear, nextYear, prevYear;
    RecyclerView panchangInfo;
    FestivalListAdapter mPanchangAdapter;
    TextView header;
    private MainActivity mContext;
    MainViewModel viewModel;
    ProgressBar prog;
    int maxCalendarYear, minCalendarYear;

    public static Fragment newInstance() {
        return new FestivalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class MonthData {

        CoreDataHelper mDay;
        ArrayList<String> mFestival;
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            currMonthInt = monthOfYear;
            currYearInt = year;
            currDayInt = dayOfMonth;
            cal.set(currYearInt, currMonthInt, currDayInt);
            dispYearMilli = cal.getTimeInMillis();


            dispYear.setTag(year);

            if ((maxCalendarYear >= year && year >= minCalendarYear)) {
                getData(year);
            }


        }
    };

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("dayofmonth", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(mContext.getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();

            res = mContext.getResources();

            dispYear = rootView.findViewById(R.id.dispYear);
            prevYear = rootView.findViewById(R.id.prevYear);
            nextYear = rootView.findViewById(R.id.nextYear);
            header = rootView.findViewById(R.id.header);
            prog = rootView.findViewById(R.id.prog);

            res = getResources();
            maxCalendarYear = Integer.parseInt(res.getString(R.string.maxYear));
            minCalendarYear = Integer.parseInt(res.getString(R.string.minYear));
            setRetainInstance(true);
            setHasOptionsMenu(true);
            Bundle args = getArguments();
            currLang = mPref.getMyLanguage();

            monthFestivalmap = new HashMap<>();
            mDayViewHashMap = new HashMap<>();

            md = new ArrayList<>();


            Calendar cal = Calendar.getInstance();
            currMonthInt = cal.get(Calendar.MONTH);
            currYearInt = cal.get(Calendar.YEAR);
            currDayInt = cal.get(Calendar.DAY_OF_MONTH);
            dispYearMilli = cal.getTimeInMillis();
            todayCal = Calendar.getInstance();


            displayYearInt = cal.get(Calendar.YEAR);
            displayMonthInt = cal.get(Calendar.MONTH);
            displayDayInt = 1;
            dispYear.setTag(displayYearInt);


            dispYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                }
            });
            prevYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    currMonthInt = cal.get(Calendar.MONTH);
                    currYearInt = cal.get(Calendar.YEAR);
                    currDayInt = cal.get(Calendar.DAY_OF_MONTH);
                    dispYearMilli = cal.getTimeInMillis();

                    int year = (int) dispYear.getTag();


                    if ((maxCalendarYear >= year && year > minCalendarYear)) {
                        dispYear.setTag(year - 1);
                        getData(year - 1);
                    }
                }
            });
            nextYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    currMonthInt = cal.get(Calendar.MONTH);
                    currYearInt = cal.get(Calendar.YEAR);
                    currDayInt = cal.get(Calendar.DAY_OF_MONTH);
                    dispYearMilli = cal.getTimeInMillis();
                    int year = (int) dispYear.getTag();
                    dispYear.setTag(year + 1);

                    if ((maxCalendarYear > year && year >= minCalendarYear)) {
                        dispYear.setTag(year + 1);
                        getData(year + 1);
                    }
                }
            });

            Bundle bundle1 = new Bundle();
            bundle1.putInt("displayYearInt", displayYearInt);
            bundle1.putInt("displayMonthInt", displayMonthInt);


            viewModel = new ViewModelProvider(this).get(MainViewModel.class);

            getData(displayYearInt);


            panchangInfo = rootView.findViewById(R.id.endlesscal);
            mPanchangAdapter = new FestivalListAdapter(mContext, md, 0);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            panchangInfo.setLayoutManager(mLayoutManager);
            panchangInfo.setAdapter(mPanchangAdapter);

            panchangInfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    try {
                        if (newState != 0) {
                            dispYear.setEnabled(false);
                            prevYear.setEnabled(false);
                            nextYear.setEnabled(false);
                        } else {
                            dispYear.setEnabled(true);
                            prevYear.setEnabled(true);
                            nextYear.setEnabled(true);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.e("ScrollState", "::ScrollStateChanged::" + newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.e("ScrollState", "::ScrollStateScrolled::" + dy + "--" + dx);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(int year) {

        header.setText("List of festivals for year " + year);
        prog.setVisibility(View.VISIBLE);
        dispYear.setText("" + year);
        prevYear.setText("<<" + (year - 1));
        nextYear.setText("" + (year + 1) + ">>");

        dispYear.setEnabled(false);
        prevYear.setEnabled(false);
        nextYear.setEnabled(false);
        displayYearInt = year;
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        cal1.set(Calendar.MONTH, 0);
        cal1.set(Calendar.YEAR, (100 + year));
        long curr = cal1.getTimeInMillis();


        viewModel.getEphemerisData(curr, 2).removeObservers(this);
        viewModel.getEphemerisData(curr, 2).observe(mContext, obj -> {
            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), mContext);

                new handlecalendarTask(myPanchangHashMap).execute();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_festival, null);
        return rootView;

    }


    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap) {
        try {

            md.clear();
            pos = 0;

            if (!mAllPanchangHashMap.isEmpty()) {
                int index=0;
                for (int j = 0; j < 12; j++) {
                    Calendar mycal = new GregorianCalendar(displayYearInt, j, 1);
                    int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= daysInMonth; i++) {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(displayYearInt, j, i);


                        String key = displayYearInt + "-" + j + "-" + i;
                        CoreDataHelper myPanchangObj = mAllPanchangHashMap.get(key);
                        ArrayList<String> myFestivallist = FestivalData.calculateFestival(myPanchangObj, currLang, mContext);
                        Log.e("key",(++index)+"::keykey::"+key+"-"+myPanchangObj.getmDay()+":::"+myPanchangObj.getmMonth());
                        if (!myFestivallist.get(0).isEmpty()) {

                            if ((cal1.getTimeInMillis() < dispYearMilli) && (cal1.get(Calendar.YEAR) == currYearInt)) {
                                pos++;
                            }


                            MonthData mdObj = new MonthData();
                            mdObj.mDay = myPanchangObj;
                            mdObj.mFestival = myFestivallist;
                            md.add(mdObj);
                        }
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class handlecalendarTask extends AsyncTask<String, Integer, Long> {
        HashMap<String, CoreDataHelper> myPanchangHashMap;

        public handlecalendarTask(HashMap<String, CoreDataHelper> panchangHashMap) {
            Log.e("setCellData", "11::::setCellData::::");
            myPanchangHashMap = panchangHashMap;
        }

        protected Long doInBackground(String... urls) {
            Log.e("setCellData", "1::::setCellData::::");
            setCalendarData(myPanchangHashMap);
            return 1L;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        protected void onPostExecute(Long result) {
            Log.e("setCellData", md.size() + "::::setCellData::::");

            mPanchangAdapter.notifyDataSetChanged();
            panchangInfo.scrollToPosition(pos);
            dispYear.setEnabled(true);
            prevYear.setEnabled(true);
            nextYear.setEnabled(true);
            prog.setVisibility(View.GONE);
        }
    }
}

