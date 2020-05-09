package com.iexamcenter.calendarweather.planet;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlanetInfoFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";

    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView txt;
    private MainActivity mContext;
    static int theme;
    static int selYear, selMonth, selDate, selHour, selMin;
    MaterialButton datePicker, timePicker;
    ArrayList<PlanetData> planetDataList;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    RecyclerView planetinfo;
    PlanetInfoListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    String pkey_ghara;

    public static PlanetInfoFrag newInstance() {

        return new PlanetInfoFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {

            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            datePicker = rootView.findViewById(R.id.datePicker);
            timePicker = rootView.findViewById(R.id.timePicker);

            lrashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

            eplanetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            pkey_ghara = mContext.getResources().getString(R.string.l_ghara);


            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
            timePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(v);
                }
            });
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);
            Calendar cal = Calendar.getInstance();
            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);

            planetDataList = new ArrayList<>();
            planetinfo = rootView.findViewById(R.id.planetinfo);
            progressBar = rootView.findViewById(R.id.progressbar);
            mPlanetInfoAdapter = new PlanetInfoListAdapter(mContext, planetDataList);
            LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            planetinfo.setLayoutManager(riseSetll);
            planetinfo.setHasFixedSize(false);
            planetinfo.setAdapter(mPlanetInfoAdapter);

            observerPlanetInfo();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public final String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final String DATE_FORMAT_2 = "hh:mm a";

    public String getSetSelectedDateTime(int type) {
        Calendar selCal = Calendar.getInstance();
        selCal.set(Calendar.YEAR, selYear);
        selCal.set(Calendar.MONTH, selMonth);
        selCal.set(Calendar.DAY_OF_MONTH, selDate);
        selCal.set(Calendar.HOUR_OF_DAY, selHour);
        selCal.set(Calendar.MINUTE, selMin);
        SimpleDateFormat dateFormat;
        if (type == 1) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.getDefault());
        }
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    public void setPlanetInfo(EphemerisEntity planetInfo) {

        planetDataList.clear();

        planetDataList.add(calculatePlanetInfo(planetInfo.sun, planetInfo.dmsun, 1));
        planetDataList.add(calculatePlanetInfo(planetInfo.moon, planetInfo.dmmoon, 2));

        planetDataList.add(calculatePlanetInfo(planetInfo.mars, planetInfo.dmmars, 5));
        planetDataList.add(calculatePlanetInfo(planetInfo.mercury, planetInfo.dmmercury, 3));
        planetDataList.add(calculatePlanetInfo(planetInfo.jupitor, planetInfo.dmjupitor, 6));

        planetDataList.add(calculatePlanetInfo(planetInfo.venus, planetInfo.dmvenus, 4));
        planetDataList.add(calculatePlanetInfo(planetInfo.saturn, planetInfo.dmsaturn, 7));

        planetDataList.add(calculatePlanetInfo(planetInfo.node, planetInfo.dmnode, 11));
        planetDataList.add(calculatePlanetInfo(planetInfo.node, planetInfo.dmnode, 12));

        planetDataList.add(calculatePlanetInfo(planetInfo.uranus, planetInfo.dmuranus, 8));
        planetDataList.add(calculatePlanetInfo(planetInfo.neptune, planetInfo.dmneptune, 9));
        planetDataList.add(calculatePlanetInfo(planetInfo.pluto, planetInfo.dmpluto, 10));



    }

    public PlanetData calculatePlanetInfo(String planet, String dmplanet, int type) {

        //Log.e("planet", ":planet:planet:" + planet + ":" + dmplanet + ":" + type);
        String dir = "F";
        String dm = dmplanet;
        if ((type >= 3 && type <= 10)) {
            dir = dmplanet.substring(0, 1);
            dm = dmplanet.substring(1);
        }
        double dailyMotion = Double.parseDouble(dm);
        PlanetData pd = new PlanetData();
        pd.planet = lplanetList[type - 1];
        pd.planetType = type;
        if (dir.contains("R")) {
            pd.direction = 1;
        } else if (dir.contains("F")) {
            pd.direction = 0;
        } else if (dir.contains("B")) {
            pd.direction = 1;
        } else if (dir.contains("E")) {
            pd.direction = 1;
        }
        if ((type != 1 && type != 2) && dailyMotion >= 359) {
            // moving backward
            dailyMotion = (360 - dailyMotion);


        }



        Calendar selCal = Calendar.getInstance();
        selCal.set(Calendar.YEAR, selYear);
        selCal.set(Calendar.MONTH, selMonth);
        selCal.set(Calendar.DAY_OF_MONTH, selDate);
        selCal.set(Calendar.HOUR_OF_DAY, selHour);
        selCal.set(Calendar.MINUTE, selMin);


        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, selYear);
        cal1.set(Calendar.MONTH, selMonth);
        cal1.set(Calendar.DAY_OF_MONTH, selDate);
        cal1.set(Calendar.HOUR_OF_DAY, 5);
        cal1.set(Calendar.MINUTE, 30);

        double diff = ((cal1.getTimeInMillis() / 1000.0) - (selCal.getTimeInMillis() / 1000.0)) / (60 * 60.0);
        double diff1 = diff;
        boolean ispositive = false;
        if (diff > 0) {
            ispositive = true;
        }
        diff = Math.abs(diff);
        double remDeg = ((dailyMotion / 24.0) * diff);
        int deg = Integer.parseInt(planet.split("_")[0]);
        int min = Integer.parseInt(planet.split("_")[1]);
        double minToSec = ((min * 60.0) / 3600.0);
        double planetDeg = deg + minToSec;
        double currPlanetDeg = planetDeg + remDeg;
        if (ispositive)
            currPlanetDeg = planetDeg - remDeg;
        if (currPlanetDeg > 360)
            currPlanetDeg = currPlanetDeg - 360;

        if (type == 12 && currPlanetDeg<=180) {
            currPlanetDeg = currPlanetDeg + 180.0;
        }else if (type == 12 && currPlanetDeg>180) {
            currPlanetDeg = currPlanetDeg - 180.0;
        }


        //  DecimalFormat f = new DecimalFormat("##.00");
        // pd.deg = f.format(currPlanetDeg);
        pd.deg =currPlanetDeg;

      /*  String deg1Str = Utility.getInstance(mContext).getDayNo("" + pd.deg.split("\\.")[0]);
        String deg2Str = Utility.getInstance(mContext).getDayNo("" + pd.deg.split("\\.")[1]);
        pd.deg = deg1Str + "." + deg2Str;
        getLatLng(currPlanetDeg, pd);*/
        return pd;//currPlanetDeg;


    }

    public void getLatLng(double deg, PlanetData pd) {
        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        // pd.houseNo = pkey_ghara + "-" + Utility.getInstance(mContext).getDayNo("" + (index + 1));
        double fractionInSec = (deg - val) * 3600;

        long min = Math.round(fractionInSec) / 60;
        long sec = Math.round(fractionInSec) % 60;
        String remDegStr = Utility.getInstance(mContext).getDayNo("" + remDeg);
        String minStr = Utility.getInstance(mContext).getDayNo("" + min);
        String secStr = Utility.getInstance(mContext).getDayNo("" + sec);
        String latLng = remDegStr + "° " + lrashiList[index] + " " + minStr + "′" + " " + secStr + "′′";
        pd.latLng = latLng;
        // return latLng;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.frag_planet_info, null);
        setHasOptionsMenu(true);

        theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        if (Integer.parseInt(getResources().getString(R.string.isNightMode)) == 1) {
            theme = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        }

        return rootView;

    }

    public final int REQUEST_DATE_CODE = 11;
    public final int REQUEST_TIME_CODE = 12;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_DATE_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedDate");
            String[] dateStr = selectedDate.split("-");
            selDate = Integer.parseInt(dateStr[0]);
            selMonth = Integer.parseInt(dateStr[1]);
            selYear = Integer.parseInt(dateStr[2]);
            Log.e("selDate", "selDate:::" + selDate + ":" + selMonth + ":" + selYear);
            observerPlanetInfo();

        }
        if (requestCode == REQUEST_TIME_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedTime");
            String[] dateStr = selectedDate.split("-");
            selHour = Integer.parseInt(dateStr[0]);
            selMin = Integer.parseInt(dateStr[1]);
            observerPlanetInfo();
        }
    }

    private void showDatePickerDialog(View v) {
        //  DialogFragment newFragment = new DatePickerFragment();
        // newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        FragmentManager fm = getFragmentManager();


        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(PlanetInfoFrag.this, REQUEST_DATE_CODE);
        newFragment.show(fm, "datePicker");

    }

    private void showTimePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(PlanetInfoFrag.this, REQUEST_TIME_CODE);
        newFragment.show(fm, "timePicker");
    }


    public void observerPlanetInfo() {
        datePicker.setText(getSetSelectedDateTime(1));
        timePicker.setText(getSetSelectedDateTime(2));
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getPlanetInfo(selDate, selMonth, selYear).removeObservers(this);
        viewModel.getPlanetInfo(selDate, selMonth, selYear).observe(this, obj -> {
            System.out.println("PlanetInfoYEAR:");
            if (obj != null) {
                System.out.println("PlanetInfoYEAR:1");
                new doBgTask(obj).execute();

            }
        });

    }
    private  class doBgTask extends AsyncTask<Long, Integer, Long> {
        EphemerisEntity obj;
        public doBgTask(EphemerisEntity obj1) {
            obj=obj1;
        }

        protected Long doInBackground(Long... urls) {

            setPlanetInfo(obj);

            return 1L;

        }
        protected void onPostExecute(Long result) {
            datePicker.setText(getSetSelectedDateTime(1));
            timePicker.setText(getSetSelectedDateTime(2));
            mPlanetInfoAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }
    }

    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }


}

