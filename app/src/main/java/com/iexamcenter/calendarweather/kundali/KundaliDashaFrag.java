package com.iexamcenter.calendarweather.kundali;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class KundaliDashaFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    TextView txt1;
    String[] moonLatLngNow;
    String planet1stDasha;
    double balanceDashaYear;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    private MainActivity mContext;
    ArrayList<dasha> dashaList;
    static int selYear, selMonth, selDate, selHour, selMin;
    String[] le_arr_rasi_kundali, le_arr_planet;
    ProgressBar progressBar;
    String pkey_ghara, le_dasha_header;
    LinearLayout diagramCntr;
    String latitude, longitude;
    Calendar selCalPrevday;
    boolean considerPrevday = false;
    DashaListAdapter dashaListAdapter;
    RecyclerView dashaRView;
    int mType;
    Resources res;

    public static KundaliDashaFrag newInstance() {

        return new KundaliDashaFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);

            le_arr_planet = mContext.getResources().getStringArray(R.array.l_arr_planet);
            le_dasha_header = mContext.getResources().getString(R.string.l_dasha_header);

        } else {
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_planet = mContext.getResources().getStringArray(R.array.e_arr_planet);
            le_dasha_header = mContext.getResources().getString(R.string.e_dasha_header);
        }
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {

            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            diagramCntr = rootView.findViewById(R.id.diagramCntr);


            txt1 = rootView.findViewById(R.id.txt1);

            pkey_ghara = mContext.getResources().getString(R.string.l_ghara);
            dashaList = new ArrayList<>();
            dashaRView = rootView.findViewById(R.id.dashaList);


            Calendar cal = Calendar.getInstance();

            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);

            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            // planetDataList = new ArrayList<>();
            //  titleInfo1 = rootView.findViewById(R.id.titleInfo1);
            //  progressBar = rootView.findViewById(R.id.progressbar);

            if (!viewModel.getDatePicker().isEmpty() && viewModel.getDatePicker().contains("-")) {

                String[] dateArr = viewModel.getDatePicker().split("-");
                selYear = Integer.parseInt(dateArr[0]);
                selMonth = Integer.parseInt(dateArr[1]);
                selDate = Integer.parseInt(dateArr[2]);

            }
            if (!viewModel.getTimePicker().isEmpty() && viewModel.getTimePicker().contains(":")) {

                String[] timeArr = viewModel.getTimePicker().split(":");
                selHour = Integer.parseInt(timeArr[0]);
                selMin = Integer.parseInt(timeArr[1]);
            }
            observerPlanetInfo();


            viewModel.getLocationChanged().observe(getViewLifecycleOwner(), picker -> {
                        observerPlanetInfo();
                    }
            );


            viewModel.getBirthPlace().observe(getViewLifecycleOwner(), picker -> {
                        if (!picker.isEmpty()) {
                            String[] place = picker.split(",", 3);
                            latitude = place[0];
                            longitude = place[1];
                            observerPlanetInfo();
                        }
                    }
            );


        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frag_kundali_dosha, null);
        setHasOptionsMenu(true);

        return rootView;

    }

    public void observerPlanetInfo() {


        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear + 100);
        selCal1.set(Calendar.MONTH, selMonth);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);
        selCal1.set(Calendar.SECOND, 0);
        selCal1.set(Calendar.MILLISECOND, 0);

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).observe(getViewLifecycleOwner(), obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);

                new handlecalendarTask(myPanchangHashMap).execute();
            }
        });

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

        protected void onPostExecute(Long result) {
            try {
                Calendar dob = Calendar.getInstance();
                dob.set(selYear, selMonth, selDate, selHour, selMin);

                String title1 = le_dasha_header;
                txt1.setText(title1);
                txt1.setGravity(Gravity.CENTER);

                dashaListAdapter = new DashaListAdapter(mContext, dashaList, le_arr_planet, dob);

                LinearLayoutManager llm = new LinearLayoutManager(mContext);
                dashaRView.setLayoutManager(llm);
                dashaRView.setHasFixedSize(false);
                dashaRView.setAdapter(dashaListAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap) {
        try {


            if (!mAllPanchangHashMap.isEmpty()) {

                Calendar selCal = Calendar.getInstance();
                selCal.set(Calendar.YEAR, selYear);
                selCal.set(Calendar.MONTH, selMonth);
                selCal.set(Calendar.DAY_OF_MONTH, selDate);
                selCal.set(Calendar.HOUR_OF_DAY, selHour);
                selCal.set(Calendar.MINUTE, selMin);
                selCal.set(Calendar.SECOND, 0);
                selCal.set(Calendar.MILLISECOND, 0);

                Utility.SunDetails sun = Utility.getInstance(mContext).getTodaySunDetails(selCal, true);
                Log.e("sunRise", "sunsunRiseRise:" + sun.sunRise);
                String[] HrMinArr = sun.sunRise.split(" ")[0].split(":");
                Calendar selCalSR = Calendar.getInstance();
                selCalSR.set(Calendar.YEAR, selYear);
                selCalSR.set(Calendar.MONTH, selMonth);
                selCalSR.set(Calendar.DAY_OF_MONTH, selDate);
                selCalSR.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HrMinArr[0]));
                selCalSR.set(Calendar.MINUTE, Integer.parseInt(HrMinArr[1]));
                selCalSR.set(Calendar.SECOND, Integer.parseInt(HrMinArr[2]));
                selCalSR.set(Calendar.MILLISECOND, 0);
                CoreDataHelper myCoreData;
                EphemerisEntity planetInfo;
                if (selCal.getTimeInMillis() < selCalSR.getTimeInMillis()) {
                    considerPrevday = true;
                    selCalPrevday = Calendar.getInstance();
                    selCalPrevday.setTimeInMillis(selCal.getTimeInMillis() - 24 * 60 * 60 * 1000);
                    int selYear1 = selCalPrevday.get(Calendar.YEAR);
                    int selMonth1 = selCalPrevday.get(Calendar.MONTH);
                    int selDate1 = selCalPrevday.get(Calendar.DAY_OF_MONTH);
                    String key1 = selYear1 + "-" + selMonth1 + "-" + selDate1;
                    myCoreData = mAllPanchangHashMap.get(key1);
                } else {
                    considerPrevday = false;
                    String key = selYear + "-" + selMonth + "-" + selDate;
                    myCoreData = mAllPanchangHashMap.get(key);
                }
                planetInfo = myCoreData.getPlanetInfo();
                setPlanetInfo(planetInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setPlanetInfo(EphemerisEntity planetInfo) {
        calculatePlanetInfo(planetInfo.moon, planetInfo.dmmoon, 2);
    }

    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }

    public void calculatePlanetInfo(String planet, String dmplanet, int type) {

        String dir = "F";
        String dm = dmplanet;
        if ((type >= 3 && type <= 10)) {
            dir = dmplanet.substring(0, 1);
            dm = dmplanet.substring(1);
        }
        double dailyMotion = Double.parseDouble(dm);

        if ((type != 1 && type != 2) && dailyMotion >= 359) {
            dailyMotion = (360 - dailyMotion);


        }

//selCalPrevday
        Calendar selCal, cal1;
        selCal = Calendar.getInstance();
        selCal.set(Calendar.YEAR, selYear);
        selCal.set(Calendar.MONTH, selMonth);
        selCal.set(Calendar.DAY_OF_MONTH, selDate);
        selCal.set(Calendar.HOUR_OF_DAY, selHour);
        selCal.set(Calendar.MINUTE, selMin);

        if (!considerPrevday) {
            cal1 = Calendar.getInstance();
            cal1.set(Calendar.YEAR, selYear);
            cal1.set(Calendar.MONTH, selMonth);
            cal1.set(Calendar.DAY_OF_MONTH, selDate);
            cal1.set(Calendar.HOUR_OF_DAY, 5);
            cal1.set(Calendar.MINUTE, 30);
        } else {
            cal1 = Calendar.getInstance();
            cal1.set(Calendar.YEAR, selCalPrevday.get(Calendar.YEAR));
            cal1.set(Calendar.MONTH, selCalPrevday.get(Calendar.MONTH));
            cal1.set(Calendar.DAY_OF_MONTH, selCalPrevday.get(Calendar.DAY_OF_MONTH));
            cal1.set(Calendar.HOUR_OF_DAY, 5);
            cal1.set(Calendar.MINUTE, 30);
        }

        double diff = ((cal1.getTimeInMillis() / 1000.0) - (selCal.getTimeInMillis() / 1000.0)) / (60 * 60.0);
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

        if (type == 12 && currPlanetDeg <= 180) {
            currPlanetDeg = currPlanetDeg + 180.0;
        } else if (type == 12 && currPlanetDeg > 180) {
            currPlanetDeg = currPlanetDeg - 180.0;
        }

        moonLatLngNow = getLatLng(currPlanetDeg);
        calc(currPlanetDeg);


    }

    public String[] getLatLng(double deg) {
        String[] strArr;

        DecimalFormat f = new DecimalFormat("##.00");
        String degStr = f.format(deg);
        String deg1Str;
        String deg2Str;


        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        double fractionInSec = (deg - val) * 3600;
        long min = Math.round(fractionInSec) / 60;
        long sec = Math.round(fractionInSec) % 60;
        String remDegStr;
        String minStr;
        String secStr;
        String latLng;
        if (CalendarWeatherApp.isPanchangEng) {
            remDegStr = "" + remDeg;
            minStr = "" + min;
            secStr = "" + sec;
            latLng = remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";
            deg1Str = degStr.split("\\.")[0];
            deg2Str = degStr.split("\\.")[1];

            degStr = deg1Str + "." + deg2Str;

        } else {
            Log.e("degStr", deg + "::" + remDeg + ":remDegdegStr:" + degStr);
            deg1Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[0]);
            deg2Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[1]);
            remDegStr = Utility.getInstance(mContext).getDayNo("" + remDeg);
            minStr = Utility.getInstance(mContext).getDayNo("" + min);
            secStr = Utility.getInstance(mContext).getDayNo("" + sec);
            latLng = remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";
            degStr = deg1Str + "." + deg2Str;
        }
        strArr = new String[]{latLng, degStr};

        return strArr;
    }

    public static class houseinfo {
        public int houseno, rashi;
        public String planetList;
    }

    public static class dasha {
        public int planet, totalyear;
        double remYear;
    }

    public dasha calc(double moonLng1) {

        int moonLng = (((int) moonLng1) * 60) + (int) ((moonLng1 - (int) moonLng1) * 60); // in minute
        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};

        int nakshetraIndex1 = moonLng / 800;
        double nakshetraDiv = moonLng / 800.0;
        double nakshetraDiv1 = nakshetraDiv - ((int) (nakshetraDiv));
        double nakshetraRem = 800 - (nakshetraDiv1 * 800.0);
        int index = nakshetraIndex1 % 9;
        int planetLordIndex = orderOfDasha[index];
        int planetLordYear = orderOfDashaYear[index];

        double yearsrem = (planetLordYear / 800.0) * nakshetraRem;
        String planetLord = le_arr_planet[planetLordIndex];
        dasha dasha = new dasha();
        dasha.planet = planetLordIndex;
        dasha.remYear = yearsrem;
        dasha.totalyear = planetLordYear;
        dashaList.clear();
        dashaList.add(dasha);

        planet1stDasha = planetLord;
        balanceDashaYear = yearsrem;

        for (int i = 1; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.planet = orderOfDasha[index];
            dasha1.remYear = planetLordYear;
            dasha1.totalyear = orderOfDashaYear[index];
            dashaList.add(dasha1);
        }

        // Log.e("remYear", "total:" + moonLng1 + ":moonLng:" + moonLng + ":remYear:" + yearsrem + ":planetLord:" + planetLord + ":" + (nakshetraIndex1 + 1) + ":year1:" + planetLordYear + "::" + yearMonthDay(yearsrem));
        //  Log.e("moonLng",":moonLng:--:"+yearsrem+"::"+planetLord+"::"+planetLordYear);
        return dasha;

/*
    int nakshetraIndex = 2;



    // planetLord = l_planet_arr[planetLordIndex];
    double elapseLng =  moonLng  % (10 * 60.0); //3 star = 1 planet(moon)

    double remNakshetraLng = ((13 * 60 + 20) - elapseLng);

    double remYear = (planetLordYear / ((13 * 60 + 20.0)))*remNakshetraLng;
    Log.e("remYear","total:"+(13 * 60 + 20)+":moonLng:"+moonLng+":elapseLng:"+elapseLng+":"+planetLordYear+":"+planetLord+":"+remNakshetraLng+":remYear:"+remYear);
*/
    }

    public String yearMonthDay(double year) {
        int compYear = (int) year;
        double fracYear = year - compYear;
        double days = fracYear * 365.2425;
        int compMonth = (int) days / 30;
        int remDay = (int) days % 30;
        return compYear + " year(s) " + compMonth + "month(s) " + remDay + " days";

    }

}

