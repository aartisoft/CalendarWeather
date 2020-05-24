package com.iexamcenter.calendarweather.tools;


import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.festival.FestivalFragment;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class PanchangaYogaFrag extends Fragment implements LocationDialog.LocationChangeEvents {

    String sundayStar, mondayStar, tuesdayStar, wednesdayStar, thursdayStar, fridayStar, saturdayStar;
    String puskarStar, puskarTithi, puskarDay;
    public static final String ARG_POSITION = "POSITION";
    int pageType;
    String placeDateHelp, headerTitle, lbl_place_txt, lbl_date_txt, pageTitle, subTitle;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    String titleval, subTitleVal;
    private MainActivity mContext;
    int displayYearInt, displayDayInt, displayMonthInt;
    String[] le_arr_planet, le_arr_rasi_kundali;
    TextView nextYear, prevYear;
    RecyclerView panchangInfo;
    YogaTimeAdapter mPanchangAdapter;
    ProgressBar prog;
    int maxCalendarYear, minCalendarYear;
    TextView header;
    ArrayList<YogaDateTiming> md;
    String[] le_arr_sarpa_yoga, le_arr_dasha_name, le_name_initials, le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String le_yoga_header, le_kala_sarpa_dosha, le_pitru_dosha, le_name_initials_title, le_nakshetra_pada, le_mangala_dosha;
    String le_dina, le_paksha;
    String le_tithi, le_nakshetra, le_lunar_rashi;
    String latitude, longitude;
    String area;
    String PAGE_TITLE_ENG,PAGE_TITLE_LOCAL;
    Resources res;
    int mType;
    int selDate, selMonth, selYear;
    TextView city, latLng;
    LinearLayout placeCntr;
    public final String DATE_FORMAT_1 = "MMM dd, yyyy, EEEE";
    public final String DATE_FORMAT_2 = "hh:mm a";
    public final String DATE_FORMAT_3 = "hh:mm a, MMM dd";

    String PAGE_TITLE_VAL;
    DialogFragment appLangDialog;


    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }

    public static PanchangaYogaFrag newInstance() {
        PanchangaYogaFrag myFragment = new PanchangaYogaFrag();
        return myFragment;
    }


    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_yoga_header = res.getString(R.string.l_yoga_header);
            le_kala_sarpa_dosha = res.getString(R.string.l_kala_sarpa_dosha);
            le_pitru_dosha = res.getString(R.string.l_pitru_dosha);
            le_arr_sarpa_yoga = mContext.getResources().getStringArray(R.array.l_arr_sarpa_yoga);
            le_name_initials_title = res.getString(R.string.l_name_initials_str);
            le_arr_dasha_name = mContext.getResources().getStringArray(R.array.l_arr_dasha_name);
            le_name_initials = mContext.getResources().getStringArray(R.array.l_name_initials);
            le_nakshetra_pada = res.getString(R.string.l_nakshetra_pada);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.l_mangala_dosha);
            le_arr_planet = mContext.getResources().getStringArray(R.array.l_arr_planet);
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.l_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            le_arr_bara = mContext.getResources().getStringArray(R.array.l_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.l_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.l_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.l_paksha);
            le_dina = mContext.getResources().getString(R.string.l_dina);
            le_tithi = mContext.getResources().getString(R.string.l_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.l_nakshetra);
            le_lunar_rashi = mContext.getResources().getString(R.string.l_planet_chandra);

        } else {
            le_yoga_header = res.getString(R.string.e_yoga_header);
            le_kala_sarpa_dosha = res.getString(R.string.e_kala_sarpa_dosha);
            le_arr_sarpa_yoga = mContext.getResources().getStringArray(R.array.e_arr_sarpa_yoga);
            le_pitru_dosha = res.getString(R.string.e_pitru_dosha);
            le_name_initials_title = res.getString(R.string.e_name_initials_str);
            le_arr_dasha_name = mContext.getResources().getStringArray(R.array.e_arr_dasha_name);
            le_name_initials = mContext.getResources().getStringArray(R.array.e_name_initials);
            le_nakshetra_pada = res.getString(R.string.e_nakshetra_pada);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.e_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.e_mangala_dosha);
            le_arr_planet = mContext.getResources().getStringArray(R.array.e_arr_planet);
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.e_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            le_arr_month = mContext.getResources().getStringArray(R.array.e_arr_month);
            le_arr_bara = mContext.getResources().getStringArray(R.array.e_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.e_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.e_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.e_paksha);
            le_dina = mContext.getResources().getString(R.string.e_dina);
            le_tithi = mContext.getResources().getString(R.string.e_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.e_nakshetra);
            le_lunar_rashi = mContext.getResources().getString(R.string.e_planet_chandra);
        }
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mPref = PrefManager.getInstance(mContext);
            Bundle arg = getArguments();
            pageType = arg.getInt(ARG_POSITION);

            PAGE_TITLE_ENG = arg.getString("PAGE_TITLE_ENG");
            PAGE_TITLE_LOCAL = arg.getString("PAGE_TITLE_LOCAL");

            if(CalendarWeatherApp.isPanchangEng){
                PAGE_TITLE_VAL= PAGE_TITLE_ENG;
            }else {
                PAGE_TITLE_VAL= PAGE_TITLE_LOCAL;
            }


            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            area = mPref.getAreaAdmin();
            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();
            Calendar cal = Calendar.getInstance();
            selDate = 1;
            selMonth = 0;
            selYear = cal.get(Calendar.YEAR);

            displayYearInt = cal.get(Calendar.YEAR);
            displayMonthInt = cal.get(Calendar.MONTH);
            displayDayInt = 1;

            setUp(rootView);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);

            viewModel.getLocationChanged().observe(getViewLifecycleOwner(), picker -> {
                        if (picker)
                            updateLatLng();
                    }
            );

            viewModel.getBirthPlace().observe(getViewLifecycleOwner(), picker -> {
                if (!picker.isEmpty()) {
                    String[] place = picker.split(",", 3);
                    latitude = place[0];
                    longitude = place[1];
                    area = place[2];
                    updateLatLng();

                }
            });

            observerPlanetInfo(selYear, selMonth, selDate, 2);

            switch (pageType) {
                case 0:
                    pageTitle = "Amrit Siddhi Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Amrit Siddhi";
                    break;
                case 1:
                    pageTitle = "Sarvartha Siddhi Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Sarvartha Siddhi";
                    break;
                case 2:
                    pageTitle = "Ravi Pushya Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Ravi Pushya";
                    break;
                case 3:
                    pageTitle = "Guru Pushya Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Guru Pushya";
                    break;
                case 4:
                    pageTitle = "Dwipushkar Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Dwipushkar";
                    break;
                case 5:
                    pageTitle = "Tripushkar Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Tripushkar";
                    break;
                case 6:
                    pageTitle = "Abhijit Nakshatra";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Abhijit";
                    break;
                default:
                    pageTitle = "--- Siddhi Yoga";
                    subTitle = "Know auspicious timing";
                    headerTitle = "Sarvartha Siddhi";
                    break;

            }
            headerTitle=PAGE_TITLE_VAL;

            titleval = mContext.getSupportActionBar().getTitle().toString();
            subTitleVal = mContext.getSupportActionBar().getSubtitle().toString();

            mContext.getSupportActionBar().setTitle(pageTitle);
            mContext.getSupportActionBar().setSubtitle(subTitle);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void updateLatLng() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String latVal = df.format(Double.parseDouble(latitude));
        String lngVal = df.format(Double.parseDouble(longitude));
        String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
        latLng.setText(latLngTxt);
        city.setText(area);
        city.setSelected(true);
    }


    protected void setUp(View rootView) {
        md = new ArrayList<YogaDateTiming>();
        prevYear = rootView.findViewById(R.id.prevYear);
        nextYear = rootView.findViewById(R.id.nextYear);
        header = rootView.findViewById(R.id.header);
        header.setTag(displayYearInt);
        prog = rootView.findViewById(R.id.prog);
        res = getResources();
        maxCalendarYear = Integer.parseInt(res.getString(R.string.maxYear));
        minCalendarYear = Integer.parseInt(res.getString(R.string.minYear));

        panchangInfo = rootView.findViewById(R.id.endlesscal);
        mPanchangAdapter = new YogaTimeAdapter(mContext, md);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        panchangInfo.setLayoutManager(mLayoutManager);
        panchangInfo.setAdapter(mPanchangAdapter);


        placeCntr = rootView.findViewById(R.id.placeCntr);
        city = rootView.findViewById(R.id.city);
        latLng = rootView.findViewById(R.id.latLng);
        updateLatLng();
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayYearInt = Calendar.getInstance().get(Calendar.YEAR);
                header.setTag(displayYearInt);
                getData(displayYearInt);
            }
        });
        placeCntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = mContext.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                frag = fm.findFragmentByTag("locationDialog");
                if (frag != null) {
                    ft.remove(frag);
                }
                LocationDialog shareDialog = LocationDialog.newInstance(mContext, 1);
                shareDialog.show(ft, "locationDialog");

                shareDialog.setLocationChangeEvents(PanchangaYogaFrag.this);
            }
        });


        prevYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int year = (int) header.getTag();
                if ((maxCalendarYear >= year && year > minCalendarYear)) {
                    header.setTag(year - 1);
                    getData(year - 1);
                }
            }
        });
        nextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int year = (int) header.getTag();
                header.setTag(year + 1);

                if ((maxCalendarYear > year && year >= minCalendarYear)) {
                    header.setTag(year + 1);
                    getData(year + 1);
                }
            }
        });
        panchangInfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (newState != 0) {
                        header.setEnabled(false);
                        prevYear.setEnabled(false);
                        nextYear.setEnabled(false);
                    } else {
                        header.setEnabled(true);
                        prevYear.setEnabled(true);
                        nextYear.setEnabled(true);
                    }
                } catch (Exception e) {
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

    }

    public void getData(int year) {
        prog.setVisibility(View.VISIBLE);
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
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);
                new handlecalendarTask(myPanchangHashMap).execute();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_panchanga_yoga, null);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
        mContext.getSupportActionBar().setTitle(titleval);
        mContext.getSupportActionBar().setSubtitle(subTitleVal);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void observerPlanetInfo(int selYear1, int selMonth1, int selDate1, int type) {

        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear1 + 100);
        selCal1.set(Calendar.MONTH, selMonth1);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate1);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), type).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), type).observe(getViewLifecycleOwner(), obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);
                new handlecalendarTask(myPanchangHashMap).execute();
            }

        });

    }


    @Override
    public void onLocationChange(String val, int type) {
        String[] tmp = val.split(" ", 4);

        latitude = String.valueOf(tmp[1]);
        longitude = String.valueOf(tmp[2]);
        area = tmp[3];
        updateLatLng();
        viewModel.setBirthPlace(latitude + "," + longitude + "," + area);
    }

    private class handlecalendarTask extends AsyncTask<String, Integer, Long> {
        HashMap<String, CoreDataHelper> myPanchangHashMap;

        public handlecalendarTask(HashMap<String, CoreDataHelper> panchangHashMap) {
            Log.e("setCellData", "11::::setCellData::::");
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
            Log.e("setCellData", md.size() + "::::setCellData::::");

            mPanchangAdapter.notifyDataSetChanged();

            prevYear.setEnabled(true);
            nextYear.setEnabled(true);
            prog.setVisibility(View.GONE);
            String str = String.format(le_yoga_header,   "" + displayYearInt);
            header.setText(str);
        }
    }


    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap) {
        try {
            md.clear();


            switch (pageType) {
                case 0:
                    sundayStar = ",13,";
                    mondayStar = ",5,";
                    tuesdayStar = ",1,";
                    wednesdayStar = ",17,";
                    thursdayStar = ",8,";
                    fridayStar = ",27,";
                    saturdayStar = ",4,";
                    break;
                case 1:
                    sundayStar = ",8,9,12,13,19,21,26,";
                    mondayStar = ",4,5,8,17,22,";
                    tuesdayStar = ",1,3,9,26,";
                    wednesdayStar = ",4,3,5,17,13,";
                    thursdayStar = ",1,7,8,17,27,";
                    fridayStar = ",1,7,17,22,27,";
                    saturdayStar = ",4,15,22,";
                    break;
                case 2:
                    sundayStar = ",8,";
                    mondayStar = "";
                    tuesdayStar = "";
                    wednesdayStar = "";
                    thursdayStar = "";
                    fridayStar = "";
                    saturdayStar = "";
                    break;
                case 3:
                    sundayStar = "";
                    mondayStar = "";
                    tuesdayStar = "";
                    wednesdayStar = "";
                    thursdayStar = ",8,";
                    fridayStar = "";
                    saturdayStar = "";
                    break;
                case 4:
                    puskarStar = ",5,14,23,";
                    puskarTithi = ",2,7,12,17,22,27,";
                    puskarDay = ",1,3,7,";
                    break;
                case 5:
                    puskarStar = ",16,7,3,11,21,25,";
                    puskarTithi = ",2,7,12,17,22,27,";
                    puskarDay = ",1,3,7,";
                    break;
            }

            if (!mAllPanchangHashMap.isEmpty()) {
                for (int j = 0; j < 12; j++) {
                    Calendar mycal = new GregorianCalendar(displayYearInt, j, 1);
                    int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= daysInMonth; i++) {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(displayYearInt, j, i);
                        String key = displayYearInt + "-" + j + "-" + i;
                        CoreDataHelper myPanchangObj = mAllPanchangHashMap.get(key);
                        Calendar nextDay = Calendar.getInstance();
                        nextDay.setTimeInMillis(cal1.getTimeInMillis());
                        nextDay.add(Calendar.DAY_OF_MONTH, 1);
                        String key1 = nextDay.get(Calendar.YEAR) + "-" + nextDay.get(Calendar.MONTH) + "-" + nextDay.get(Calendar.DAY_OF_MONTH);
                        CoreDataHelper myNextPanchangObj = mAllPanchangHashMap.get(key1);

                        if (pageType==4 || pageType ==5) {
                            getPuskarYoga(myPanchangObj, myNextPanchangObj);
                        } else  if (pageType<4) {
                            getYoga(myPanchangObj, myNextPanchangObj);
                        }else  if (pageType==6) {
                            getAbhijitNakshetra(myPanchangObj, myNextPanchangObj);
                        }


                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public void getAbhijitNakshetra(CoreDataHelper myPanchangObj, CoreDataHelper myNextPanchangObj){

   /* int currMoonSign=myPanchangObj.getMoonSign().currVal;
    int nextMoonSign=myPanchangObj.getMoonSign().nextVal;
    Calendar sunrise = myPanchangObj.getSunRiseCal();
    Calendar nextSunrise = myNextPanchangObj.getSunRiseCal();
    String abhijitMoonSign = ",10,";
    if (abhijitMoonSign.contains("," + currMoonSign + ",")) {
        Calendar starStartTime=null, starEndTime=null;
        starStartTime=myPanchangObj.getMoonSign().currValStartTime;
        starEndTime=myPanchangObj.getMoonSign().currValEndTime;
        long totalDurationInMilli=starEndTime.getTimeInMillis()-starStartTime.getTimeInMillis();
        long totalDuarionInDegMinSec=30*60*60; // 30 deg
        long deg4Min13Sec20InMilli=Math.round((totalDurationInMilli/(30*60*60*1.0)) * (4*60*60+13*60+20));
        long deg6Min40Sec0InMilli=Math.round((totalDurationInMilli/(30*60*60*1.0)) * (6*60*60+40*60));
        Calendar startTimeAbhijit=Calendar.getInstance();
        startTimeAbhijit.setTimeInMillis(starStartTime.getTimeInMillis()+deg6Min40Sec0InMilli);
        Calendar endTimeAbhijit=Calendar.getInstance();
        endTimeAbhijit.setTimeInMillis(starStartTime.getTimeInMillis()+deg6Min40Sec0InMilli+deg4Min13Sec20InMilli);
        YogaDateTiming dataObj = setdata(nextSunrise, startTimeAbhijit, endTimeAbhijit, 1, 21, 1);
      if(!md.contains(dataObj))
        md.add(dataObj);

        //Abhijit is a span of 4 deg 13 mins 20 seconds in the sign Capricorn starting from 6deg 40mins to 10 deg 53 mins 20 seconds.
    }
    else if (abhijitMoonSign.contains("," + nextMoonSign + ",")) {
        Calendar starStartTime=null, starEndTime=null;
        starStartTime=myPanchangObj.getMoonSign().currValEndTime;
        starEndTime=myPanchangObj.getMoonSign().le_nextValEndTime;
        long totalDurationInMilli=starEndTime.getTimeInMillis()-starStartTime.getTimeInMillis();
        long totalDuarionInDegMinSec=30*60*60; // 30 deg
        long deg4Min13Sec20InMilli=Math.round((totalDurationInMilli/(30*60*60*1.0)) * (4*60*60+13*60+20));
        long deg6Min40Sec0InMilli=Math.round((totalDurationInMilli/(30*60*60*1.0)) * (6*60*60+40*60));
        Calendar startTimeAbhijit=Calendar.getInstance();
        startTimeAbhijit.setTimeInMillis(starStartTime.getTimeInMillis()+deg6Min40Sec0InMilli);
        Calendar endTimeAbhijit=Calendar.getInstance();
        endTimeAbhijit.setTimeInMillis(starStartTime.getTimeInMillis()+deg6Min40Sec0InMilli+deg4Min13Sec20InMilli);
        YogaDateTiming dataObj = setdata(nextSunrise, startTimeAbhijit, endTimeAbhijit, 1, 21, 1);
        if(!md.contains(dataObj))
            md.add(dataObj);
        //Abhijit is a span of 4 deg 13 mins 20 seconds in the sign Capricorn starting from 6deg 40mins to 10 deg 53 mins 20 seconds.
    }*/
    int currNakshetra = myPanchangObj.getNakshetra().currVal;
    int nextNakshetra = myPanchangObj.getNakshetra().nextVal;
    int nextToNextNakshetra = myPanchangObj.getNakshetra().nextToNextVal;

    Calendar sunrise = myPanchangObj.getSunRiseCal();
    Calendar nextSunrise = myNextPanchangObj.getSunRiseCal();
    String abhijit4thPadaStar = ",21,";
    String abhijit1stPada = ",22,";
    Calendar startTime = null, endTime = null;

    SimpleDateFormat dateFormat5 = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
    String date5 = dateFormat5.format(sunrise.getTime());
    Log.e("Abhijit",date5+"/"+currNakshetra+"-"+nextNakshetra+"-"+"-"+nextToNextNakshetra+"/-----AbhijitList::");

    CoreDataHelper.Panchanga nakshetra = myPanchangObj.getNakshetra();
    Calendar abhijitStarStartTime=null, abhijitStarEndTime=null;
        Calendar starStartTime=null, starEndTime=null;
        Boolean isAbhijit4thPadaStar=false;
        Boolean isAbhijit1stPadaStar=false;
        if (abhijit4thPadaStar.contains("," + currNakshetra + ",")) {
            starStartTime = nakshetra.currValStartTime;
            starEndTime = nakshetra.currValEndTime;
            isAbhijit4thPadaStar=true;
        } else if (abhijit4thPadaStar.contains("," + nextNakshetra + ",")) {
            starStartTime = nakshetra.currValEndTime;
            starEndTime = nakshetra.le_nextValEndTime;
            isAbhijit4thPadaStar=true;


        } else if (abhijit4thPadaStar.contains("," + nextToNextNakshetra + ",")) {
            starStartTime = nakshetra.le_nextValEndTime;
            starEndTime = nakshetra.nextToNextValEndTime;
            isAbhijit4thPadaStar=true;

        }

        if(isAbhijit4thPadaStar){


            double padaInterval = (starEndTime.getTimeInMillis() - starStartTime.getTimeInMillis()) / 4.0;
            long timeInMilli=Math.round(starStartTime.getTimeInMillis() + (3 * padaInterval));
            abhijitStarStartTime=Calendar.getInstance();
            abhijitStarStartTime.setTimeInMillis(timeInMilli);

          //  long timeInMilli1=Math.round(starStartTime.getTimeInMillis() + (4 * padaInterval));
         //   abhijitStarEndTime=Calendar.getInstance();
          //  abhijitStarEndTime.setTimeInMillis(starEndTime.getTimeInMillis());
        }

    if (abhijit1stPada.contains("," + currNakshetra + ",")) {
        starStartTime = nakshetra.currValStartTime;
        starEndTime = nakshetra.currValEndTime;
        isAbhijit1stPadaStar=true;
    } else if (abhijit1stPada.contains("," + nextNakshetra + ",")) {
        starStartTime = nakshetra.currValEndTime;
        starEndTime = nakshetra.le_nextValEndTime;
        isAbhijit1stPadaStar=true;
    } else if (abhijit1stPada.contains("," + nextToNextNakshetra + ",")) {
        starStartTime = nakshetra.le_nextValEndTime;
        starEndTime = nakshetra.nextToNextValEndTime;
        isAbhijit1stPadaStar=true;

        SimpleDateFormat dateFormat3 = new SimpleDateFormat(DATE_FORMAT_3, Locale.ENGLISH);
        String date1 = dateFormat3.format(starStartTime.getTime());
        Log.e("Abhijit",currNakshetra+"-"+nextNakshetra+"-"+"-"+nextToNextNakshetra+"/-----Abhijit3:"+date1);
        String date2 = dateFormat3.format(starEndTime.getTime());
        Log.e("Abhijit","xxxxx-Abhijit2:"+date2);
    }
    if(isAbhijit1stPadaStar){
        double padaInterval = (starEndTime.getTimeInMillis() - starStartTime.getTimeInMillis()) / 15.0;
        long timeInMilli=Math.round(starStartTime.getTimeInMillis() + (1 * padaInterval));
        abhijitStarEndTime=Calendar.getInstance();
        abhijitStarEndTime.setTimeInMillis(timeInMilli);

    }
    if(isAbhijit4thPadaStar && !isAbhijit1stPadaStar){
        nakshetra = myNextPanchangObj.getNakshetra();
        if (abhijit1stPada.contains("," + currNakshetra + ",")) {
            starStartTime = nakshetra.currValStartTime;
            starEndTime = nakshetra.currValEndTime;
            isAbhijit1stPadaStar=true;
        } else if (abhijit1stPada.contains("," + nextNakshetra + ",")) {
            starStartTime = nakshetra.currValEndTime;
            starEndTime = nakshetra.le_nextValEndTime;
            isAbhijit1stPadaStar=true;
        } else if (abhijit1stPada.contains("," + nextToNextNakshetra + ",")) {
            starStartTime = nakshetra.le_nextValEndTime;
            starEndTime = nakshetra.nextToNextValEndTime;
            isAbhijit1stPadaStar=true;
        }
        if(isAbhijit1stPadaStar){
            double padaInterval = (starEndTime.getTimeInMillis() - starStartTime.getTimeInMillis()) / 15.0;
            long timeInMilli=Math.round(starStartTime.getTimeInMillis() + (1 * padaInterval));
            abhijitStarEndTime=Calendar.getInstance();
            abhijitStarEndTime.setTimeInMillis(timeInMilli);
        }
    }


    if(abhijitStarStartTime!=null && abhijitStarEndTime!=null ){
        md.add(setdataForAbhijit(nextSunrise, abhijitStarStartTime, abhijitStarEndTime, 1, 21, 1));
    }

}

    public void getPuskarYoga(CoreDataHelper myPanchangObj, CoreDataHelper myNextPanchangObj) {
        int currNakshetra = myPanchangObj.getNakshetra().currVal;
        int nextNakshetra = myPanchangObj.getNakshetra().nextVal;
        int nextToNextNakshetra = myPanchangObj.getNakshetra().nextToNextVal;
        int currTithi = myPanchangObj.getTithi().currVal;
        int nextTithi = myPanchangObj.getTithi().nextVal;
        int nextToNextTithi = myPanchangObj.getTithi().nextToNextVal;
        int weekDay = myPanchangObj.getDayOfWeek();
        Calendar sunrise = myPanchangObj.getSunRiseCal();
        Calendar nextSunrise = myNextPanchangObj.getSunRiseCal();

        Calendar startTime = null, endTime = null;

        Boolean isPuskarDay = puskarDay.contains("," + weekDay + ",");
        Boolean isPuskarTithi = (puskarTithi.contains("," + currTithi + ",") || puskarTithi.contains("," + nextTithi + ",") || puskarTithi.contains("," + nextToNextTithi + ","));
        Boolean isPuskarStar = (puskarStar.contains("," + currNakshetra + ",") || puskarStar.contains("," + nextNakshetra + ",") || puskarStar.contains("," + nextToNextNakshetra + ","));

        if (isPuskarDay && isPuskarTithi && isPuskarStar) {
            Log.e("puskarDay", weekDay + "::puskarDay::" + puskarDay);

            int tithi, star, day;
            Calendar tithiStart, tithiEnd, starStart, starEnd, dayStart, dayEnd;
            dayStart = sunrise;
            dayEnd = nextSunrise;
            if (puskarTithi.contains("," + currTithi + ",")) {
                tithi = currTithi;
                tithiStart = myPanchangObj.getTithi().currValStartTime;
                tithiEnd = myPanchangObj.getTithi().currValEndTime;
            } else if (puskarTithi.contains("," + nextTithi + ",")) {
                tithi = nextTithi;
                tithiStart = myPanchangObj.getTithi().currValEndTime;
                tithiEnd = myPanchangObj.getTithi().le_nextValEndTime;
            } else {//if(puskarTithi.contains("," + nextToNextTithi + ",")){
                tithi = nextToNextTithi;
                tithiStart = myPanchangObj.getTithi().le_nextValEndTime;
                tithiEnd = myPanchangObj.getTithi().nextToNextValEndTime;
            }
            if (puskarStar.contains("," + currNakshetra + ",")) {
                star = currNakshetra;
                starStart = myPanchangObj.getNakshetra().currValStartTime;
                starEnd = myPanchangObj.getNakshetra().currValEndTime;
            } else if (puskarStar.contains("," + nextNakshetra + ",")) {
                star = nextNakshetra;
                starStart = myPanchangObj.getNakshetra().currValEndTime;
                starEnd = myPanchangObj.getNakshetra().le_nextValEndTime;
            } else {//if(puskarStar.contains("," + nextToNextNakshetra + ",")){
                star = nextToNextNakshetra;
                starStart = myPanchangObj.getNakshetra().le_nextValEndTime;
                starEnd = myPanchangObj.getNakshetra().nextToNextValEndTime;
            }

            Boolean isCommon = false;
            if ((tithiStart.getTimeInMillis() < starStart.getTimeInMillis()) && (tithiEnd.getTimeInMillis() > starStart.getTimeInMillis()) && (tithiEnd.getTimeInMillis() < starEnd.getTimeInMillis())) {
                startTime = starStart;
                endTime = tithiEnd;
                isCommon = true;

            } else if ((tithiStart.getTimeInMillis() < starStart.getTimeInMillis()) && (tithiEnd.getTimeInMillis() > starStart.getTimeInMillis()) && (tithiEnd.getTimeInMillis() > starEnd.getTimeInMillis())) {
                startTime = starStart;
                endTime = starEnd;
                isCommon = true;
            } else if ((starStart.getTimeInMillis() < tithiStart.getTimeInMillis()) && (starEnd.getTimeInMillis() > tithiStart.getTimeInMillis()) && (starEnd.getTimeInMillis() < tithiEnd.getTimeInMillis())) {
                startTime = tithiStart;
                endTime = starEnd;
                isCommon = true;
            } else if ((starStart.getTimeInMillis() < tithiStart.getTimeInMillis()) && (starEnd.getTimeInMillis() > tithiStart.getTimeInMillis()) && (starEnd.getTimeInMillis() > tithiEnd.getTimeInMillis())) {
                startTime = tithiStart;
                endTime = tithiEnd;
                isCommon = true;
            }
            if (isCommon && startTime.getTimeInMillis() < dayStart.getTimeInMillis()) {
                startTime = dayStart;
            }
            if (isCommon && endTime.getTimeInMillis() > dayEnd.getTimeInMillis()) {
                endTime = dayEnd;
            }
            if (isCommon)
                md.add(setdata(nextSunrise, startTime, endTime, weekDay, star, tithi));

        }

    }

    public void getYoga(CoreDataHelper myPanchangObj, CoreDataHelper myNextPanchangObj) {
        int currNakshetra = myPanchangObj.getNakshetra().currVal;
        int nextNakshetra = myPanchangObj.getNakshetra().nextVal;
        int nextToNextNakshetra = myPanchangObj.getNakshetra().nextToNextVal;
        int weekDay = myPanchangObj.getDayOfWeek();
        Calendar sunrise = myPanchangObj.getSunRiseCal();
        Calendar nextSunrise = myNextPanchangObj.getSunRiseCal();


        Calendar startTime = null, endTime = null;
        switch (weekDay) {
            case 1:
                if (sundayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 1, currNakshetra, 0));
                } else if (sundayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 1, nextNakshetra, 0));
                } else if (sundayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 1, nextToNextNakshetra, 0));
                }
                break;
            case 2:
                if (mondayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 2, currNakshetra, 0));
                } else if (mondayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 2, nextNakshetra, 0));
                } else if (mondayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 2, nextToNextNakshetra, 0));
                }
                break;
            case 3:
                if (tuesdayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 3, currNakshetra, 0));
                } else if (tuesdayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 3, nextNakshetra, 0));
                } else if (tuesdayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 3, nextToNextNakshetra, 0));
                }
                break;
            case 4:
                if (wednesdayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 4, currNakshetra, 0));
                } else if (wednesdayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 4, nextNakshetra, 0));
                } else if (wednesdayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 4, nextToNextNakshetra, 0));
                }
                break;
            case 5:
                if (thursdayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 5, currNakshetra, 0));
                } else if (thursdayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 5, nextNakshetra, 0));
                } else if (thursdayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 5, nextToNextNakshetra, 0));
                }
                break;
            case 6:
                if (fridayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 6, currNakshetra, 0));
                } else if (fridayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 6, nextNakshetra, 0));
                } else if (fridayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 6, nextToNextNakshetra, 0));
                }
                break;
            case 7:
                if (saturdayStar.contains("," + currNakshetra + ",")) {
                    startTime = sunrise;
                    if (myPanchangObj.getNakshetra().currValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().currValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 7, currNakshetra, 0));
                } else if (saturdayStar.contains("," + nextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().currValEndTime;
                    if (myPanchangObj.getNakshetra().le_nextValEndTime.getTimeInMillis() < nextSunrise.getTimeInMillis()) {
                        endTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    } else {
                        endTime = nextSunrise;
                    }
                    md.add(setdata(nextSunrise, startTime, endTime, 7, nextNakshetra, 0));
                } else if (saturdayStar.contains("," + nextToNextNakshetra + ",")) {
                    startTime = myPanchangObj.getNakshetra().le_nextValEndTime;
                    endTime = nextSunrise;
                    md.add(setdata(nextSunrise, startTime, endTime, 7, nextToNextNakshetra, 0));
                }
                break;

        }

    }
    public YogaDateTiming setdataForAbhijit(Calendar nextSunrise, Calendar startTimeCal, Calendar endTimeCal, int weekday, int nakshetra, int tithi) {
        Calendar endTime = null;
        String date, start, end;
        YogaDateTiming ydt = new YogaDateTiming();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat(DATE_FORMAT_3, Locale.ENGLISH);
        date = dateFormat1.format(startTimeCal.getTime());
        start = dateFormat2.format(startTimeCal.getTime());
        Calendar midNightCal = Calendar.getInstance();
        midNightCal.setTimeInMillis(startTimeCal.getTimeInMillis());
        midNightCal.set(Calendar.HOUR_OF_DAY, 23);
        midNightCal.set(Calendar.MINUTE, 59);
        midNightCal.set(Calendar.SECOND, 59);



        if (endTimeCal.getTimeInMillis() < midNightCal.getTimeInMillis()) {
            endTime = endTimeCal;
            end = dateFormat2.format(endTime.getTime());
        } else {
            endTime = endTimeCal;
            end = dateFormat3.format(endTime.getTime());
        }

        ydt.jogaDate = date;
        ydt.jogaDateCal=startTimeCal;
        ydt.title=headerTitle;//+" Nakshetra";
        ydt.startTimeCal=startTimeCal;
        ydt.endTimeCal=endTime;
        ydt.timings ="From "+ start + " To " + end;
        if (tithi > 0)
            ydt.combination = "";
        else
            ydt.combination = "";

        ydt.duration = "Duration : " + printDifference(endTimeCal.getTimeInMillis() - startTimeCal.getTimeInMillis());
        return ydt;
    }

    public YogaDateTiming setdata(Calendar nextSunrise, Calendar startTimeCal, Calendar endTimeCal, int weekday, int nakshetra, int tithi) {
        Calendar endTime = null;
        String date, start, end;
        YogaDateTiming ydt = new YogaDateTiming();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat(DATE_FORMAT_3, Locale.ENGLISH);
        date = dateFormat1.format(startTimeCal.getTime());
        start = dateFormat2.format(startTimeCal.getTime());
        Calendar midNightCal = Calendar.getInstance();
        midNightCal.setTimeInMillis(startTimeCal.getTimeInMillis());
        midNightCal.set(Calendar.HOUR_OF_DAY, 23);
        midNightCal.set(Calendar.MINUTE, 59);
        midNightCal.set(Calendar.SECOND, 59);


        if (endTimeCal.getTimeInMillis() < midNightCal.getTimeInMillis()) {
            endTime = endTimeCal;
            end = dateFormat2.format(endTime.getTime());
        } else {
            endTime = nextSunrise;
            end = dateFormat3.format(endTime.getTime());
        }
        ydt.jogaDate = date;
        ydt.jogaDateCal=startTimeCal;
        ydt.title=headerTitle;//+" Yoga ";
        ydt.startTimeCal=startTimeCal;
        ydt.endTimeCal=endTime;
        ydt.timings ="From "+ start + " To " + end;
        if (tithi > 0)
            ydt.combination = "Yoga Combination: " + le_arr_nakshatra[nakshetra - 1] + ", " + le_arr_tithi[tithi - 1] + ", " + le_arr_bara[weekday - 1];
        else
            ydt.combination = "Yoga Combination: " + le_arr_nakshatra[nakshetra - 1] + ", " + le_arr_bara[weekday - 1];

        ydt.duration = "Yoga Duration : " + printDifference(endTimeCal.getTimeInMillis() - startTimeCal.getTimeInMillis());
        return ydt;
    }

    public String printDifference(long different) {


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedHours + " hour " + elapsedMinutes + " min";
    }

    public static class YogaDateTiming {
        public String jogaDate, timings, duration, combination,title;
        public  Calendar jogaDateCal,startTimeCal,endTimeCal;

    }

}

