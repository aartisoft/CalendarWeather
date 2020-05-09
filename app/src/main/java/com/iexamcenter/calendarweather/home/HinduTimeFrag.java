package com.iexamcenter.calendarweather.home;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HinduTimeFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";

    PrefManager mPref;
    HinduTimeAdapter timeListAdapter;
    RecyclerView vedicTimeList;
    private MainActivity mContext;
    List<String> timeList, timeTitleList;
    List<String> orgTimeList;
    String[] hindu_time_arr, hindu_time_cnvt_arr;
    TextInputEditText time_val;
    int timeUnitPos = 27;
    TextClock digitalClock;
    TextView vedicTime;
    Calendar sunRiseCal;
    Timer timer;
    Resources res;
    AutoCompleteTextView timeUnitView;
    ArrayAdapter<String> adapter;

    public static HinduTimeFrag newInstance() {

        return new HinduTimeFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mContext.showHideBottomNavigationView(false);
            setHasOptionsMenu(true);
            mContext.enableBackButtonViews(true);
            res = mContext.getResources();
            mContext.getSupportActionBar().setTitle("Vedic Time");
            // mContext.getSupportActionBar().setSubtitle("");

            hindu_time_arr = res.getStringArray(R.array.l_arr_hindu_time);
            timeTitleList = new ArrayList<String>(Arrays.asList(hindu_time_arr));
            hindu_time_cnvt_arr = res.getStringArray(R.array.hindu_time_cnvt_arr);
            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, hindu_time_arr);
            timeUnitView = rootView.findViewById(R.id.time_unit);
            time_val = rootView.findViewById(R.id.time_val);
            digitalClock = rootView.findViewById(R.id.digitalClock);
            vedicTime = rootView.findViewById(R.id.vedicTime);

            mPref = PrefManager.getInstance(mContext);
            orgTimeList = new ArrayList<>();
            timeList = new ArrayList<String>(Arrays.asList(hindu_time_cnvt_arr));
            orgTimeList.addAll(timeList);

            TextView gregoSSTime,gregoSRTime,vedicSRTime,vedicSSTime;
            vedicSRTime = rootView.findViewById(R.id.vedicSRTime);
            vedicSSTime = rootView.findViewById(R.id.vedicSSTime);
            gregoSRTime = rootView.findViewById(R.id.gregoSRTime);
            gregoSSTime = rootView.findViewById(R.id.gregoSSTime);




            vedicTimeList = rootView.findViewById(R.id.vedicTimeList);
            timeListAdapter = new HinduTimeAdapter(mContext, hindu_time_arr, timeList);
            vedicTimeList.setLayoutManager(new LinearLayoutManager(mContext));
            vedicTimeList.setHasFixedSize(false);
            vedicTimeList.setAdapter(timeListAdapter);
            sunRiseCal = Calendar.getInstance();

            Utility.SunDetails sunrisecal = Utility.getInstance(mContext).getTodaySunDetails(sunRiseCal,true);
            String sunRiseStr = sunrisecal.sunRise;
            if (sunRiseStr != null && !sunRiseStr.isEmpty()) {
                String[] timeArr = sunRiseStr.split(" ")[0].split(":");
                sunRiseCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
                sunRiseCal.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
                sunRiseCal.set(Calendar.SECOND,  Integer.parseInt(timeArr[2]));
                sunRiseCal.set(Calendar.MILLISECOND, 0);

            }
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.US);
            gregoSRTime.setText(format.format(sunRiseCal.getTime()));


            String sunSetStr = sunrisecal.sunSet;
            Calendar sunSetCal = Calendar.getInstance();
            if (sunSetStr != null && !sunSetStr.isEmpty()) {
                String[] timeArr = sunSetStr.split(" ")[0].split(":");
                sunSetCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0])+12);
                sunSetCal.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
                sunSetCal.set(Calendar.SECOND,  Integer.parseInt(timeArr[2]));
                sunSetCal.set(Calendar.MILLISECOND, 0);

            }


            gregoSSTime.setText(format.format(sunSetCal.getTime()));


            long diff = sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis();





            double ghati = diff / (24 * 60.0 * 1000);
            int ghatiVal = (int) ghati;
            double remGhati = ghati - ghatiVal;
            double totalVipal = remGhati * 60 * 60;
            int palVal = (int) (totalVipal / 60.0);
            int vipalVal = (int) (totalVipal % 60.0);
            String ghatiStr = "" + ghatiVal;
            if (ghatiVal < 10)
                ghatiStr = "0" + ghatiVal;
            String palStr = "" + palVal;
            if (palVal < 10)
                palStr = "0" + palVal;
            String vipalStr = "" + vipalVal;
            if (vipalVal < 10)
                vipalStr = "0" + vipalVal;

            vedicSSTime.setText(ghatiStr + ":" + palStr + ":" + vipalStr);





            time_val.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    calculateVal();
                }
            });
            time_val.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Log.e("onFocusChange", "onFocusChange:" + b);
                    if (!b)
                        hideSoftKeyboard(view);
                    if (time_val.getText().toString().isEmpty())
                        time_val.setText("1");

                }
            });

            timeUnitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    timeUnitPos = position;
                    calculateVal();


                }


            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        hindu_time_arr = res.getStringArray(R.array.l_arr_hindu_time);
        timeTitleList = new ArrayList<String>(Arrays.asList(hindu_time_arr));
        timeUnitView.setAdapter(adapter);
        timeUnitView.setText(hindu_time_arr[timeUnitPos], false);
        updateVedicTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mContext.toolbar != null)
            hideSoftKeyboard(mContext.toolbar);

        if (timer != null)
            timer.cancel();
        timer = null;

    }

    private void updateVedicTime() {
        Log.e("sunRiseCalVALL", "sunRiseCalVALL:" + sunRiseCal.getTimeInMillis());

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long sunRiseMilli = sunRiseCal.getTimeInMillis();
                        long diff = (System.currentTimeMillis() - sunRiseMilli);
                        if (diff < 0) {
                            diff = (System.currentTimeMillis() - (sunRiseMilli - 24 * 60 * 60 * 1000));
                        }
                        // int tag=(int)vedicTime.getTag();
                        //  long diff = (diffFinal+(tag*400)) / 1000;
                        //  24 *60 sec= 1ghati
                        // 24 *60 sec= 60*60 vipal
                       // Log.e("sunRiseCalVALL", "sunRiseCalVALL::::" + diff);

                        double ghati = diff / (24 * 60.0 * 1000);
                        int ghatiVal = (int) ghati;
                        double remGhati = ghati - ghatiVal;
                        double totalVipal = remGhati * 60 * 60;
                        int palVal = (int) (totalVipal / 60.0);
                        int vipalVal = (int) (totalVipal % 60.0);
                        String ghatiStr = "" + ghatiVal;
                        if (ghatiVal < 10)
                            ghatiStr = "0" + ghatiVal;
                        String palStr = "" + palVal;
                        if (palVal < 10)
                            palStr = "0" + palVal;
                        String vipalStr = "" + vipalVal;
                        if (vipalVal < 10)
                            vipalStr = "0" + vipalVal;

                        vedicTime.setText(ghatiStr + ":" + palStr + ":" + vipalStr);

                    }
                });
            }

        }, 0, 400);

    }


    private void calculateVal() {
        if (time_val.getText().toString().isEmpty())
            return;
        timeList.clear();
        double amount = Double.parseDouble(time_val.getText().toString());

        double timeVal1 = Double.parseDouble(orgTimeList.get(timeUnitPos)); //365
        for (int i = 0; i < hindu_time_arr.length; i++) {
            double timeVal2 = Double.parseDouble(orgTimeList.get(i)); //100000
            double res = (timeVal2 / timeVal1) * amount;
            String pattern = "############################################################.############################################################";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            timeList.add(decimalFormat.format(res));
        }
        timeListAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_hindu_time, null);
        setHasOptionsMenu(true);
        return rootView;

    }

}

