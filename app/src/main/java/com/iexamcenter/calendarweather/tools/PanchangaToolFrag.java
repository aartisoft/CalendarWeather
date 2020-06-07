package com.iexamcenter.calendarweather.tools;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AppDateTimeDialog;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.kundali.KundaliDashaFrag;
import com.iexamcenter.calendarweather.kundali.KundaliDiagram;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PanchangaToolFrag extends Fragment implements LocationDialog.LocationChangeEvents {
    int dashaYears, dashaMonths, dashaDays, dashaHrs, dashaMins;
    int nakshetraPada;
    LinearLayout ll,diagramCntr, janmaDashaCntr1, currDashaCntr1, dashaCntr;
    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise, nakshetraTithi;
    int lagnarashi;
    ArrayList<KundaliDiagram.houseinfo> houseinfos;
    HashMap<Integer, KundaliDiagram.houseinfo> houseinfosMap;
    Boolean isMangalaDosha;
    String  mangalaDoshaDesc,le_mangal_dosha_desc1;
    String dashaPlanetLord;
    ArrayList<PlanetData> planetDataList;
    public boolean nextClicked = false, prevClicked = false;
    public static final String ARG_POSITION = "POSITION";
    int bYear, bMonth, bDate, bHour, bMin, pageType;
    String placeDateHelp, headerTitle, lbl_place_txt, lbl_date_txt, pageTitle, subTitle;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView txt1,birthDasha, currDasha, placeDateHelpTxt, lbl_place, lbl_date;
    MaterialButton sbmt;

    String titleval, subTitleVal;
    private MainActivity mContext;


    int selYear, selMonth, selDate, selHour, selMin;
    String[] le_arr_planet, le_arr_rasi_kundali;

    int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali;
    String[] le_arr_sarpa_yoga, le_arr_dasha_name, le_name_initials, le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String le_sarpa_dosha_desc1,le_pitru_dosha_desc1,le_pitru_dosha_desc2,le_pitru_dosha_desc3,le_pitru_dosha_desc4,le_pitru_dosha_desc5,le_planet_rashi,le_kala_sarpa_dosha,le_pitru_dosha,le_name_initials_title, le_nakshetra_pada, le_mangala_dosha;
    String le_dina, le_paksha;
    String le_tithi, le_nakshetra, le_lunar_rashi;
    String latitude, longitude;
    int mDiffYear = 0;
    Calendar selCalPrevday;
    boolean considerPrevday = false;

    int year, month, dayOfmonth, hour24, min;
    String area;
    String PAGE_TITLE_VAL,PAGE_TITLE_ENG,PAGE_TITLE_LOCAL;
    Resources res;
    int mType;
    int currDate, currMonth, currYear;
    TextView city, latLng, date, time, placeVal, dateVal, timeVal;
    MaterialButton txt,titleVal;
    LinearLayout placeCntr, dateCntr;
    public final String DATE_FORMAT_1 = "EEEE, dd MMM yyyy";
    public final String DATE_FORMAT_2 = "hh:mm a z";
    DialogFragment appLangDialog;

    public static class dasha {
        public int planet, type = 0;
        double remYear;
        double totalyear;

        public Calendar startDate, endDate;

    }

    ArrayList<dasha> dashaList;
    String planet1stDasha;
    double balanceDashaYear;

    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }

    public static PanchangaToolFrag newInstance() {
        PanchangaToolFrag myFragment = new PanchangaToolFrag();
        return myFragment;
    }


    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_mangal_dosha_desc1=res.getString(R.string.l_mangal_dosha_desc1);
            le_sarpa_dosha_desc1=res.getString(R.string.l_sarpa_dosha_desc1);
            le_pitru_dosha_desc2=res.getString(R.string.l_pitru_dosha_desc2);
            le_pitru_dosha_desc1=res.getString(R.string.l_pitru_dosha_desc1);
            le_pitru_dosha_desc2=res.getString(R.string.l_pitru_dosha_desc2);
            le_pitru_dosha_desc3=res.getString(R.string.l_pitru_dosha_desc3);
            le_pitru_dosha_desc4=res.getString(R.string.l_pitru_dosha_desc4);
            le_pitru_dosha_desc5=res.getString(R.string.l_pitru_dosha_desc5);
            le_planet_rashi=res.getString(R.string.l_planet_rashi);
            le_kala_sarpa_dosha=res.getString(R.string.l_kala_sarpa_dosha);
            le_pitru_dosha=res.getString(R.string.l_pitru_dosha);
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

            le_mangal_dosha_desc1=res.getString(R.string.e_mangal_dosha_desc1);
            le_sarpa_dosha_desc1=res.getString(R.string.e_sarpa_dosha_desc1);
            le_pitru_dosha_desc1=res.getString(R.string.e_pitru_dosha_desc1);
            le_pitru_dosha_desc2=res.getString(R.string.e_pitru_dosha_desc2);
            le_pitru_dosha_desc3=res.getString(R.string.e_pitru_dosha_desc3);
            le_pitru_dosha_desc4=res.getString(R.string.e_pitru_dosha_desc4);
            le_pitru_dosha_desc5=res.getString(R.string.e_pitru_dosha_desc5);
            le_planet_rashi=res.getString(R.string.e_planet_rashi);
            le_kala_sarpa_dosha=res.getString(R.string.e_kala_sarpa_dosha);
            le_arr_sarpa_yoga = mContext.getResources().getStringArray(R.array.e_arr_sarpa_yoga);
            le_pitru_dosha=res.getString(R.string.e_pitru_dosha);
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

    public int getNakshetraPada(Calendar starStartTime, Calendar starEndTime, Calendar selCal) {

        double padaInterval = (starEndTime.getTimeInMillis() - starStartTime.getTimeInMillis()) / 4.0;
        if ((starStartTime.getTimeInMillis() + padaInterval) > selCal.getTimeInMillis())
            return 1;
        else if ((starStartTime.getTimeInMillis() + (2 * padaInterval)) > selCal.getTimeInMillis())
            return 2;
        else if ((starStartTime.getTimeInMillis() + (3 * padaInterval)) > selCal.getTimeInMillis())
            return 3;
        else if ((starStartTime.getTimeInMillis() + (4 * padaInterval)) > selCal.getTimeInMillis())
            return 4;


        return 0;

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

            bDate = selDate = currDate = cal.get(Calendar.DAY_OF_MONTH);
            bMonth = selMonth = currMonth = cal.get(Calendar.MONTH);
            bYear = selYear = currYear = cal.get(Calendar.YEAR);

            bHour = selHour = cal.get(Calendar.HOUR_OF_DAY);
            bMin = selMin = cal.get(Calendar.MINUTE);
            setUp(rootView);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);
            viewModel.getDateTimePicker().observe(getViewLifecycleOwner(), picker -> {
                        String datePicker = viewModel.getDatePicker();
                        setval(datePicker, 1);
                        String timePicker = viewModel.getTimePicker();
                        setval(timePicker, 2);


                    }
            );
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

            switch (pageType) {
                case 3:
                    pageTitle = "Janma Rashi Calculator";
                    subTitle = "Panchanga Darpana";
                    headerTitle = "Janma Rashi/Moon Sign Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 4:
                    pageTitle = "Janma Nakshatra Calculator";
                    subTitle = "Know Birth Star";
                    headerTitle = "Janma Nakshatra/Birth Star Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 5:
                    pageTitle = "Janma Lagna Calculator";
                    subTitle = "Know Birth Ascendant From Birth Kundali";
                    headerTitle = "Janma Lagna/Birth Ascendant Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 6:
                    pageTitle = "Mangala Dosha Calculator";
                    subTitle = "Know Mangalik From Birth Kundali";
                    headerTitle = "Mangala Dosha/Mangalik Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 7:
                    pageTitle = "Pitru Dosha Calculator";
                    subTitle = "Know Pitru Dosha From Birth Kundali";
                    headerTitle = "Pitru Dosha/Pitra Dosh Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 8:
                    pageTitle = "Kala Sarpa Dosha Calculator";
                    subTitle = "Know Sarpa Dosha From Birth Kundali";
                    headerTitle = "Kala Sarpa Dosha/Kala Sarpa Yoga Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;

                case 9:
                    pageTitle = "Graha Dasha Calculator";
                    subTitle = "Know Birth and Current Graha Dasha";
                    headerTitle = "Graha Dasha/Vimshottari Dasha Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;
                case 10:
                    pageTitle = "Baby Name Initials Calculator";
                    subTitle = "Know babies first letter of the name";
                    headerTitle = "Baby Name Initials Result";
                    lbl_place_txt = "BIRTH PLACE";
                    lbl_date_txt = "BIRTH DATE";
                    placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
                    break;

            }
            // headerTitle=PAGE_TITLE_VAL;
            placeDateHelpTxt.setText(placeDateHelp);
            lbl_place.setText(lbl_place_txt);
            lbl_date.setText(lbl_date_txt);

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
        ll= rootView.findViewById(R.id.ll);
        ll.setVisibility(GONE);
        txt1 = rootView.findViewById(R.id.txt1);
        currDasha = rootView.findViewById(R.id.currDasha);
        birthDasha = rootView.findViewById(R.id.birthDasha);
        janmaDashaCntr1 = rootView.findViewById(R.id.janmaDashaCntr1);
        currDashaCntr1 = rootView.findViewById(R.id.currDashaCntr1);
        dashaCntr = rootView.findViewById(R.id.dashaCntr);

        lbl_place = rootView.findViewById(R.id.lbl_place);
        lbl_date = rootView.findViewById(R.id.lbl_date);
        sbmt = rootView.findViewById(R.id.sbmt);
        diagramCntr = rootView.findViewById(R.id.diagramCntr);

        txt = rootView.findViewById(R.id.txt);
        placeVal = rootView.findViewById(R.id.placeVal);
        dateVal = rootView.findViewById(R.id.dateVal);
        timeVal = rootView.findViewById(R.id.timeVal);
        placeDateHelpTxt = rootView.findViewById(R.id.placeDateHelp);

        city = rootView.findViewById(R.id.city);
        latLng = rootView.findViewById(R.id.latLng);
        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        titleVal = rootView.findViewById(R.id.titleVal);
        placeCntr = rootView.findViewById(R.id.placeCntr);
        dateCntr = rootView.findViewById(R.id.dateCntr);
        sbmt.setOnClickListener(v -> {
            txt.setText("");

            nextClicked = prevClicked = false;
            mDiffYear = currYear - selYear;

            if (mDiffYear >= 0) {
                observerPlanetInfo(selYear, selMonth, selDate, 0);
            } else {
                Toast.makeText(mContext, "Invalid date", Toast.LENGTH_LONG).show();
            }


        });


        txt = rootView.findViewById(R.id.txt);


        updateLatLng();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        date.setText(getSetSelectedDateTime(1, cal));
        time.setText(getSetSelectedDateTime(2, cal));
        dateCntr.setOnClickListener(view -> openPicker(0));
        date.setOnClickListener(view -> openPicker(0));
        time.setOnClickListener(view -> openPicker(1));
        placeCntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // changelatlng();

                FragmentManager fm = mContext.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                frag = fm.findFragmentByTag("locationDialog");
                if (frag != null) {
                    ft.remove(frag);
                }
                LocationDialog shareDialog = LocationDialog.newInstance(mContext, 1);
                shareDialog.show(ft, "locationDialog");

                shareDialog.setLocationChangeEvents(PanchangaToolFrag.this);
            }
        });
    }

    public void openPicker(int page) {
        // txt.setText("");
        FragmentTransaction ft0 = getChildFragmentManager().beginTransaction();
        Fragment prev0 = getChildFragmentManager().findFragmentByTag("MYDATETIME");
        if (prev0 != null) {
            ft0.remove(prev0);
        }
        appLangDialog = AppDateTimeDialog.newInstance(mContext);
        Bundle args = new Bundle();
        args.putInt(AppDateTimeDialog.ARG_POSITION, page);
        appLangDialog.setArguments(args);
        appLangDialog.setCancelable(true);
        appLangDialog.show(getChildFragmentManager(), "MYDATETIME");
    }

    public String getSetSelectedDateTime(int type, Calendar selCal) {
        year = selCal.get(Calendar.YEAR);
        month = selCal.get(Calendar.MONTH);
        dayOfmonth = selCal.get(Calendar.DAY_OF_MONTH);
        hour24 = selCal.get(Calendar.HOUR_OF_DAY);
        min = selCal.get(Calendar.MINUTE);
        SimpleDateFormat dateFormat;
        if (type == 1) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        }
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    public void setval(String picker, int type) {
        if (type == 1) {
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                selYear = year = Integer.parseInt(dateArr[0]);
                selMonth = month = Integer.parseInt(dateArr[1]);
                selDate = dayOfmonth = Integer.parseInt(dateArr[2]);
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);
            date.setText(getSetSelectedDateTime(1, cal));


        } else {
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                selHour = hour24 = Integer.parseInt(timeArr[0]);
                selMin = min = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);

            time.setText(getSetSelectedDateTime(2, cal));
        }

        bYear = year;
        bMonth = month;
        bDate = dayOfmonth;
        bHour = selHour;
        bMin = selMin;


    }

    public int mTithiIndex, mNakshetraIndex, mMonthIndex, mPakshya, mMoonSignIndex;


    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap, int selYear, int selMonth, int selDate, int selHour, int selMin, int type) {
        try {
            if (!mAllPanchangHashMap.isEmpty()) {
                CoreDataHelper myCoreData;


                Calendar selCal = Calendar.getInstance();
                selCal.set(Calendar.YEAR, selYear);
                selCal.set(Calendar.MONTH, selMonth);
                selCal.set(Calendar.DAY_OF_MONTH, selDate);
                selCal.set(Calendar.HOUR_OF_DAY, selHour);
                selCal.set(Calendar.MINUTE, selMin);
                selCal.set(Calendar.SECOND, 0);
                selCal.set(Calendar.MILLISECOND, 0);
                Utility.SunDetails sun = Utility.getInstance(mContext).getTodaySunDetails(selCal, true);
                String[] HrMinArr = sun.sunRise.split(" ")[0].split(":");
                Calendar selCalSR = Calendar.getInstance();
                selCalSR.set(Calendar.YEAR, selYear);
                selCalSR.set(Calendar.MONTH, selMonth);
                selCalSR.set(Calendar.DAY_OF_MONTH, selDate);
                selCalSR.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HrMinArr[0]));
                selCalSR.set(Calendar.MINUTE, Integer.parseInt(HrMinArr[1]));
                selCalSR.set(Calendar.SECOND, Integer.parseInt(HrMinArr[2]));
                selCalSR.set(Calendar.MILLISECOND, 0);

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

                CoreDataHelper.Panchanga tithi = myCoreData.getTithi();
                if (selCal.getTimeInMillis() <= tithi.currValEndTime.getTimeInMillis()) {
                    tithiKundali = tithi.currVal;
                } else if (selCal.getTimeInMillis() <= tithi.le_nextValEndTime.getTimeInMillis()) {
                    tithiKundali = tithi.nextVal;
                } else {
                    tithiKundali = tithi.nextToNextVal;
                }

                CoreDataHelper.Panchanga nakshetra = myCoreData.getNakshetra();
                Calendar starStartTime, starEndTime;
                if (selCal.getTimeInMillis() <= nakshetra.currValEndTime.getTimeInMillis()) {
                    nakshetraKundali = nakshetra.currVal;
                    starStartTime = nakshetra.currValStartTime;
                    starEndTime = nakshetra.currValEndTime;
                } else if (selCal.getTimeInMillis() <= nakshetra.le_nextValEndTime.getTimeInMillis()) {
                    nakshetraKundali = nakshetra.nextVal;
                    starStartTime = nakshetra.currValEndTime;
                    starEndTime = nakshetra.le_nextValEndTime;
                } else {
                    nakshetraKundali = nakshetra.nextToNextVal;
                    starStartTime = nakshetra.le_nextValEndTime;
                    starEndTime = nakshetra.nextToNextValEndTime;
                }
                nakshetraPada = getNakshetraPada(starStartTime, starEndTime, selCal);
                String pada = "" + nakshetraPada;
                if (!CalendarWeatherApp.isPanchangEng) {
                    pada = Utility.getInstance(mContext).getDayNo("" + pada);
                }


                CoreDataHelper.Panchanga moonSign = myCoreData.getMoonSign();
                if (selCal.getTimeInMillis() <= moonSign.currValEndTime.getTimeInMillis()) {
                    mMoonSignIndex = moonSign.currVal;
                } else if (selCal.getTimeInMillis() <= moonSign.le_nextValEndTime.getTimeInMillis()) {
                    mMoonSignIndex = moonSign.nextVal;
                } else {
                    mMoonSignIndex = moonSign.nextToNextVal;
                }
                mTithiIndex = tithiKundali;
                mNakshetraIndex = nakshetraKundali;
                mPakshya = myCoreData.getPaksha();
                lunarMonth = mMonthIndex = getHinduMonth(myCoreData);


                weekDay = myCoreData.getDayOfWeek();
                solarMonth = myCoreData.getAdjustSolarMonth();
                solarDay = myCoreData.getSolarDayVal();
                lunarDay = myCoreData.getFullMoonLunarDay();


                // PanchangUtility panchangUtility = new PanchangUtility();
                //  PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), latitude, longitude, mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());
                //  String tithiVal = le_arr_tithi[tithiKundali - 1] + " " + le_tithi;

                //   String nakshetraVal =pada+" "+le_nakshetra_pada+", "+ le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra;
                String nakshetraVal = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra + ", " + pada + " " + le_nakshetra_pada;
                String moonSignVal = le_arr_rasi_kundali[mMoonSignIndex - 1] +" "+le_lunar_rashi+ " " + le_planet_rashi;
                nakshetraTithi = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_arr_rasi_kundali[mMoonSignIndex - 1];

                //  headerStr = myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + "," + tithiVal + "," + nakshetraVal + "," + moonSignVal;
                Calendar calculatedBirthCal = Calendar.getInstance(Locale.ENGLISH);
                calculatedBirthCal.set(Calendar.YEAR, myCoreData.getmYear());
                calculatedBirthCal.set(Calendar.MONTH, myCoreData.getmMonth());
                calculatedBirthCal.set(Calendar.DAY_OF_MONTH, myCoreData.getmDay());
                Calendar actualBirthCal = Calendar.getInstance(Locale.ENGLISH);
                actualBirthCal.set(Calendar.YEAR, bYear);
                actualBirthCal.set(Calendar.MONTH, bMonth);
                actualBirthCal.set(Calendar.DAY_OF_MONTH, bDate);
                actualBirthCal.set(Calendar.HOUR_OF_DAY, bHour);
                actualBirthCal.set(Calendar.MINUTE, bMin);
                String txtVal = "";
                String txtval1="";
                EphemerisEntity planetInfo;
                switch (pageType) {
                    case 3:
                        diagramCntr.setVisibility(View.VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        txtVal = moonSignVal;
                        createDiagram();
                        break;
                    case 4:
                        diagramCntr.setVisibility(View.VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        txtVal = nakshetraVal;
                        createDiagram();
                        break;
                    case 5:
                        diagramCntr.setVisibility(View.VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        txtVal = lagnaLngNow;
                        createDiagram();

                        break;
                    case 6:
                        diagramCntr.setVisibility(View.VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        txtVal = le_mangala_dosha + " - " + (isMangalaDosha ? le_arr_ausp_work_yes_no[0] : le_arr_ausp_work_yes_no[1]);
                        createDiagram();
                        if(isMangalaDosha) {
                            txtval1="<span style=\"font-weight:normal;color:#000000;\">" + mangalaDoshaDesc+"</span>";
                            txt1.setVisibility(VISIBLE);
                            txt1.setText(Html.fromHtml(txtval1));
                        }else{
                            txt1.setVisibility(GONE);
                        }
                        break;

                    case 7:
                        janmaDashaCntr1.removeAllViews();
                        currDashaCntr1.removeAllViews();
                        diagramCntr.setVisibility(VISIBLE);
                        dashaCntr.setVisibility(GONE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        createDiagram();
                        String pitruDosha = getPitruDosha().toString();
                        if (pitruDosha.isEmpty()) {
                            txtVal = le_pitru_dosha+" - "+le_arr_ausp_work_yes_no[1];
                        } else {
                            txtVal = le_pitru_dosha+" - "+le_arr_ausp_work_yes_no[0];
                        }
                        txt.setVisibility(VISIBLE);
                        txtval1="<span style=\"font-weight:normal;color:#000000;\">" + pitruDosha+"</span>";
                        txt1.setVisibility(VISIBLE);
                        txt1.setText(Html.fromHtml(txtval1));

                        break;
                    case 8:
                        diagramCntr.setVisibility(View.VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        getMangalaDosha();
                        createDiagram();
                        String sarpaYoga = checkSarpaDosha();
                        txtVal = (sarpaYoga != null ? sarpaYoga + " - " + le_arr_ausp_work_yes_no[0] : le_kala_sarpa_dosha+" - " + le_arr_ausp_work_yes_no[1]);
                        if(sarpaYoga != null) {
                            txtval1="<span style=\"font-weight:normal;color:#000000;\">" + le_sarpa_dosha_desc1+"</span>";
                            txt1.setVisibility(VISIBLE);
                            txt1.setText(Html.fromHtml(txtval1));
                        }else{
                            txt1.setVisibility(GONE);
                        }
                        break;
                    case 9:
                        janmaDashaCntr1.removeAllViews();
                        currDashaCntr1.removeAllViews();
                        diagramCntr.setVisibility(GONE);
                        dashaCntr.setVisibility(VISIBLE);
                        planetDataList = new ArrayList<>();
                        planetInfo = myCoreData.getPlanetInfo();
                        setPlanetInfo(planetInfo);
                        getLagna(planetInfo, myCoreData, selCal);
                        txt.setVisibility(GONE);
                        break;
                    case 10:
                        String strArr = le_name_initials[nakshetraKundali - 1];
                        String nameInitials = strArr.split("-")[nakshetraPada];
                        txt.setTypeface(Typeface.DEFAULT);
                        txt.setTextColor(Color.BLACK);
                        txtVal = String.format(le_name_initials_title, nakshetraVal, nameInitials);
                        break;


                }
                placeVal.setText("Place - " + city.getText());
                dateVal.setText("Date - " + date.getText());
                timeVal.setText("Time - " + time.getText());
                txt.setText(Html.fromHtml(txtVal));
                titleVal.setText(headerTitle);
                ll.setVisibility(VISIBLE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StringBuilder getPitruDosha() {
//0 sun, 1 moon,10 rahu,11 ketu,6 shani,5 jupitor,4 mangala
        StringBuilder reason = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            Log.e("houseinfosMap", i + ":" + le_arr_planet[i] + ":HOUSEINFO:" + houseinfosMap.get(i).houseno + ":" + houseinfosMap.get(i).planetList + ":" + houseinfosMap.get(i).planetIndexList);
            String planetindex = houseinfosMap.get(i).planetIndexList;
            int houseno = houseinfosMap.get(i).houseno + 1;
            boolean cond1 = (houseno != 5 && houseno != 9 && planetindex.contains(",1,") && planetindex.contains(",10,"));
            boolean cond2 = (houseno != 5 && houseno != 9 && planetindex.contains(",1,") && planetindex.contains(",11,"));
            boolean cond3 = (houseno != 5 && houseno != 9 && planetindex.contains(",1,") && planetindex.contains(",6,"));
            boolean cond4 = (houseno != 5 && houseno != 9 && planetindex.contains(",0,") && planetindex.contains(",10,"));
            boolean cond5 = (houseno != 5 && houseno != 9 && planetindex.contains(",0,") && planetindex.contains(",11,"));
            boolean cond6 = (houseno != 5 && houseno != 9 && planetindex.contains(",0,") && planetindex.contains(",6,"));
            boolean cond7 = (houseno != 5 && houseno != 9 && planetindex.contains(",0,") && planetindex.contains(",4,"));

            boolean cond8 = (houseno != 9 && planetindex.contains(",5,") && planetindex.contains(",10,"));
            boolean cond9 = (houseno != 9 && planetindex.contains(",5,") && planetindex.contains(",11,"));

            boolean cond10 = (houseno == 9 && planetindex.contains(",10,"));
            boolean cond11 = (houseno == 9 && planetindex.contains(",11,"));
            boolean cond12 = (houseno == 9 && planetindex.contains(",6,"));
            boolean cond13 = (houseno == 9 && planetindex.contains(",4,"));

            boolean cond14 = (houseno == 5 && planetindex.contains(",4,"));
            boolean cond15 = (houseno == 5 && planetindex.contains(",6,"));
            boolean cond16 = (houseno == 5 && planetindex.contains(",10,"));
            boolean cond17 = (houseno == 5 && planetindex.contains(",11,"));

            boolean cond18 = (houseno == 1 && planetindex.contains(",10,"));
            boolean cond19 = (houseno == 1 && planetindex.contains(",11,"));
            String tmpStr;
            String hNo=Utility.getInstance(mContext).getDayNo(""+houseno);
            String hNo9=Utility.getInstance(mContext).getDayNo(""+9);
            String hNo5=Utility.getInstance(mContext).getDayNo(""+5);
            if (cond1) {

                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[1],le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
                // reason.append("<br/>" +le_arr_planet[1]).append(" associated  ").append(le_arr_planet[10]).append(" in house - " + houseno);
            }
            if (cond2) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[1],le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[1]).append(" associated ").append(le_arr_planet[11]).append(" in house - " + houseno);
            }
            if (cond3) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[1],le_arr_planet[6],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[1]).append(" associated ").append(le_arr_planet[6]).append(" in house - " + houseno);
            }

            if (cond4) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[0],le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[0]).append(" associated ").append(le_arr_planet[10]).append(" in house - " + houseno);
            }
            if (cond5) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[0],le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[0]).append(" associated ").append(le_arr_planet[11]).append(" in house - " + houseno);
            }
            if (cond6) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[0],le_arr_planet[6],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[0]).append(" associated ").append(le_arr_planet[6]).append(" in house - " + houseno);
            }
            if (cond7) {
                tmpStr=String.format(le_pitru_dosha_desc2,le_arr_planet[0],le_arr_planet[4],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[0]).append(" associated ").append(le_arr_planet[4]).append(" in house - " + houseno);
            }

            if (cond8 && !hNo9.equals(hNo)) {
                tmpStr=String.format(le_pitru_dosha_desc4,hNo9,le_arr_planet[5],le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>9th house lord " + le_arr_planet[5]).append(" associated with ").append(le_arr_planet[10]);
            }else  if (cond8) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[5],hNo9,le_arr_planet[10]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>9th house lord " + le_arr_planet[5]).append(" associated with ").append(le_arr_planet[11]);
            }
            if (cond9 && !hNo9.equals(hNo)) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[11],hNo9,le_arr_planet[5]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>9th house lord " + le_arr_planet[5]).append(" associated with ").append(le_arr_planet[11]);
            }else  if (cond8) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[5],hNo9,le_arr_planet[11]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>9th house lord " + le_arr_planet[5]).append(" associated with ").append(le_arr_planet[11]);
            }
           /* if ((cond9 || cond8) && hNo9.equals(hNo)) {
                tmpStr=String.format(le_pitru_dosha_desc5,hNo9,le_arr_planet[5],le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>9th house lord " + le_arr_planet[5]).append(" associated with ").append(le_arr_planet[11]);
            }*/
            if (cond10) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
                // reason.append("<br/>" +le_arr_planet[10]).append(" associated ").append(" in house - " + houseno);
            }
            if (cond11) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[11]).append(" associated ").append(" in house - " + houseno);
            }
            if (cond12) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[6],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>" +le_arr_planet[6]).append(" associated ").append(" in house - " + houseno);
            }
            if (cond13) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[4],hNo);
                reason.append("<br/>" + tmpStr);
                // reason.append("<br/>" + le_arr_planet[4]).append(" associated ").append(" in house - " + houseno);
            }

            if (cond14 && !hNo.equals(hNo5))  {
                tmpStr=String.format(le_pitru_dosha_desc4,hNo5,le_arr_planet[0],le_arr_planet[4],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[4]);
            }else if (cond14) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[4],hNo5,le_arr_planet[0]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }
            if (cond15  && !hNo.equals(hNo5)) {
                tmpStr=String.format(le_pitru_dosha_desc4,hNo5,le_arr_planet[0],le_arr_planet[6],hNo);
                reason.append("<br/>" + tmpStr);
                // reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[6]);
            }else if (cond15) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[6],hNo5,le_arr_planet[0]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }
            if (cond16  && !hNo.equals(hNo5)) {
                tmpStr=String.format(le_pitru_dosha_desc4,hNo5,le_arr_planet[0],le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[10]);
            }else if (cond16) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[10],hNo5,le_arr_planet[0]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }
            if (cond17 && !hNo.equals(hNo5)) {
                tmpStr=String.format(le_pitru_dosha_desc4,hNo5,le_arr_planet[0],le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }else if (cond17) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[11],hNo5,le_arr_planet[0]);
                reason.append("<br/>" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }
           /* if ((cond14 || cond15 || cond16 || cond17) && hNo.equals(hNo5)) {
                tmpStr=String.format(le_pitru_dosha_desc5,le_arr_planet[11],hNo5,le_arr_planet[0]);
                reason.append("<br/>x" + tmpStr);
                //reason.append("<br/>5th house lord " + le_arr_planet[0]).append(" associated with ").append(le_arr_planet[11]);
            }*/
            if (cond18) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[10],hNo);
                reason.append("<br/>" + tmpStr);
            }
            if (cond19) {
                tmpStr=String.format(le_pitru_dosha_desc1,le_arr_planet[11],hNo);
                reason.append("<br/>" + tmpStr);
            }
        }
        return reason;
    }

    public void getLagna(EphemerisEntity planetInfo, CoreDataHelper myCoreData, Calendar selCal) {
        String sunDeg = planetInfo.sun;
        String sunDmDeg = planetInfo.dmsun;
        Log.e("latitude1", ":latitude1:X:" + latitude + ":sunDeg:" + sunDeg + ":sunDmDeg:" + sunDmDeg);
        double lat = Double.parseDouble(latitude);
        Calendar sunRiseVal = Calendar.getInstance(Locale.ENGLISH);
        sunRiseVal.set(Calendar.YEAR, myCoreData.getSunRise().get(Calendar.YEAR));
        sunRiseVal.set(Calendar.MONTH, myCoreData.getSunRise().get(Calendar.MONTH));
        sunRiseVal.set(Calendar.DAY_OF_MONTH, myCoreData.getSunRise().get(Calendar.DAY_OF_MONTH));
        sunRiseVal.set(Calendar.HOUR_OF_DAY, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY));
        sunRiseVal.set(Calendar.MINUTE, myCoreData.getSunRise().get(Calendar.MINUTE));

        sunRiseVal.set(Calendar.SECOND, 0);
        sunRiseVal.set(Calendar.MILLISECOND, 0);
        Log.e("Lagna", "LagnaASC:sunRiseVal::::Dia:");
        double sunDegAtSunrise1 = Helper.getInstance().getPlanetPos(sunRiseVal, sunDeg, Double.parseDouble(sunDmDeg));
        double dayInt = selDate;
        double A = (16.90709 * selYear) / 1000 - (0.757371 * selYear) / 1000 * selYear / 1000 - 6.92416100010001000;
        double B = (selMonth + (dayInt / 30)) * 1.1574074 / 1000;
        double ayamansa = A + B;
        double sunDegAtSunrise = sunDegAtSunrise1 + ayamansa;
        if (sunDegAtSunrise >= 360) {
            sunDegAtSunrise = sunDegAtSunrise - 360;
        }

        ArrayList<Helper.lagna> lagnaListAtSunrise = Helper.getInstance().getLagna(sunRiseVal, sunDegAtSunrise, lat);

        lagnaLngAtSunrise = getLatLng(sunDegAtSunrise)[0];


        int size = lagnaListAtSunrise.size();
        double elapse = 0, totalDeg = -1.0;
        double diff = 0;
        lagnarashi = 0;
        for (int x = 0; x < size; x++) {
            Calendar end = lagnaListAtSunrise.get(x).end;
            Calendar start = lagnaListAtSunrise.get(x).start;
            SimpleDateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);
            Date today = selCal.getTime();
            dateFormat.format(today);
            if (selCal.getTimeInMillis() >= start.getTimeInMillis() && selCal.getTimeInMillis() <= end.getTimeInMillis()) {


                diff = end.getTimeInMillis() - start.getTimeInMillis();
                Log.e("Lagna", ":LagnaASC:ELEPSE:" + selCal.getTimeInMillis() + "::" + start.getTimeInMillis());
                elapse = selCal.getTimeInMillis() - start.getTimeInMillis();
                diff = Math.abs(diff);
                elapse = Math.abs(elapse);
                double elapseDeg = ((30.0 / diff) * elapse);
                int index = lagnaListAtSunrise.get(x).rashi;
                totalDeg = (index * 30) + elapseDeg;
                totalDeg = totalDeg - ayamansa;

                if (totalDeg < 0) {
                    totalDeg = 360 + totalDeg;
                } else if (totalDeg > 360) {
                    totalDeg = totalDeg - 360;
                }

                lagnarashi = (int) totalDeg / 30;
                break;
            }
        }

        lagnaLngNow = getLatLng(totalDeg)[0];
    }

    public void getMangalaDosha() {
        rashiInHouse = new ArrayList<>();
        int houseRashi = lagnarashi;
        HashMap<Integer, Integer> rashiHM = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            rashiInHouse.add(houseRashi);
            rashiHM.put(houseRashi, i);
            houseRashi++;
            if (houseRashi == 12) {
                houseRashi = 0;
            }
        }


        ArrayList<Integer> planetInHouse = new ArrayList<>();
        HashMap<Integer, Integer> planetHM = new HashMap<>();
        for (int i = 0; i < planetDataList.size() - 3; i++) {
            Integer rashiIndex = (int) (planetDataList.get(i).deg / 30);

            int houseIndex = rashiHM.get(rashiIndex);
            planetInHouse.add(houseIndex);
            planetHM.put(i, planetDataList.get(i).planetType - 1);
        }

        houseinfos = new ArrayList<>();
        houseinfosMap = new HashMap<>();


        for (int house = 0; house < 12; house++) {
            KundaliDiagram.houseinfo obj = new KundaliDiagram.houseinfo();
            obj.houseno = house;
            obj.rashi = rashiInHouse.get(house);
            String planetStr = ",", planetIndexStr = ",";
            for (int i = 0; i < planetInHouse.size(); i++) {
                if (planetInHouse.get(i) == house) {
                    planetStr = planetStr + le_arr_planet[planetHM.get(i)] + ",";
                    planetIndexStr = planetIndexStr + planetHM.get(i) + ",";
                }

            }

            obj.planetList = planetStr;
            obj.planetIndexList = planetIndexStr;
            houseinfos.add(obj);
            houseinfosMap.put(house, obj);
        }


        isMangalaDosha = false;
        for (int i = 0; i < planetDataList.size() - 3; i++) {
            // PlanetData obj = planetDataList.get(i);

            int houseNoIndex = planetInHouse.get(i);

            if (i == 2 && (houseNoIndex == 0 || houseNoIndex == 1 || houseNoIndex == 3 || houseNoIndex == 6 || houseNoIndex == 7 || houseNoIndex == 11)) {
                isMangalaDosha = true;
                String mangalaIsIn=""+(houseNoIndex+1);
                if(!CalendarWeatherApp.isPanchangEng){
                    mangalaIsIn=Utility.getInstance(mContext).getDayNo(mangalaIsIn);
                }
                String tmpStr=String.format(le_mangal_dosha_desc1,mangalaIsIn);

                mangalaDoshaDesc=tmpStr;
            }

        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.fragment_panchanga_tool, null);
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

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 3).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 3).observe(getViewLifecycleOwner(), obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);
                setCalendarData(myPanchangHashMap, selYear1, selMonth1, selDate1, selHour, selMin, type);
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


    public int getHinduMonth(CoreDataHelper myCoreData) {
        String lang = mPref.getMyLanguage();
        if (lang.contains("bn") || lang.contains("ta") || lang.contains("ml") || lang.contains("pa"))
            return myCoreData.getAdjustSolarMonth();
        else if (lang.contains("te") || lang.contains("mr") || lang.contains("kn") || lang.contains("gu")) {
            return myCoreData.getLunarMonthAmantIndex();
        } else if (lang.contains("hi") || lang.contains("or") || lang.contains("en")) {
            return myCoreData.getLunarMonthPurnimantIndex();
        } else {
            return myCoreData.getLunarMonthPurnimantIndex();
        }
    }

    public void setPlanetInfo(EphemerisEntity planetInfo) {


        planetDataList.clear();
        // dont chnage order list


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
        pd.planet = le_arr_planet[type - 1];
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

        pd.deg = currPlanetDeg;

        if (type == 2) {
            calc(currPlanetDeg, 1);
            calc(currPlanetDeg, 2);
        }
        return pd;//currPlanetDeg;


    }


    public void calc(double moonLng1, int birthOrCurrType) {
        //Mahadasha -- Sun , Antar Dasha -- Ketu, Pratyantar Dasha -- Ketu , Shookshma Dasha -- Jupiter
        String[] dashaCategory = le_arr_dasha_name;
        SimpleDateFormat dateFormat;
        String DATE_FORMAT_1 = "dd MMM\nyyyy";
        dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);

        dashaList = new ArrayList<>();

        Calendar currDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();


        birthDate.set(bYear, bMonth, bDate, bHour, bMin, 0);
        currDate.set(Calendar.HOUR_OF_DAY,bHour);
        currDate.set(Calendar.MINUTE,bMin);
        currDate.set(Calendar.SECOND,0);
        //  int birthOrCurrType=1;// 1 birth ,2 curr
        Calendar compCal = Calendar.getInstance();
        if (birthOrCurrType == 1)
            compCal.setTimeInMillis(birthDate.getTimeInMillis());
        else
            compCal.setTimeInMillis(currDate.getTimeInMillis());


        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(birthDate.getTimeInMillis());
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(birthDate.getTimeInMillis());

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
        dasha.type = 1;
        dasha.startDate = Calendar.getInstance();
        dasha.endDate = Calendar.getInstance();

        dasha.startDate.setTimeInMillis(startDate.getTimeInMillis());
        //  dasha.endDate.setTimeInMillis(startDate.getTimeInMillis() + (int)(dasha.remYear * 365.2425 * 24 * 60 * 60 * 1000.0));
        dasha.endDate.setTimeInMillis(startDate.getTimeInMillis());
        int days = (int) (dasha.remYear * (365.2425));
        int lapsedays = ((int) (dasha.totalyear * (365.2425))) - days;
        dasha.endDate.add(Calendar.DAY_OF_MONTH, days);
        dasha.startDate.add(Calendar.DAY_OF_MONTH, -lapsedays);
        startDate = dasha.endDate;

        //String startDayStr = dateFormat.format(birthDate.getTime());
        //String birthDayStr = dateFormat.format(startDate.getTime());
        // Log.e("dashaObj", "DASHACALCULATION::" + startDayStr+"-"+birthDayStr);
        dashaList.clear();
        //  dashaList.add(dasha);
        // dashaList.addAll(calcSubDasha(dasha, 0,2, compCal));
        if (dasha.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {
            dashaList.add(dasha);
            dashaList.addAll(calcSubDasha(dasha, index-1, 2, compCal));
        }

        planet1stDasha = planetLord;
        balanceDashaYear = yearsrem;

        for (int i = 1; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.planet = orderOfDasha[index];
            dasha1.totalyear = orderOfDashaYear[index];
            dasha1.type = 1;// maha dasha
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            if (planetLordIndex == orderOfDasha[index]) {
                dasha1.remYear = planetLordYear;
                dasha1.startDate = startDate;
                dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis());
                days = (int) (dasha1.remYear * (365.2425));
                dasha1.endDate.add(Calendar.DAY_OF_MONTH, days);
                startDate = dasha1.endDate;
            } else {
                dasha1.remYear = dasha1.totalyear;
                dasha1.startDate = startDate;

                dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis());
                days = (int) (dasha1.remYear * (365.2425));
                dasha1.endDate.add(Calendar.DAY_OF_MONTH, days);
                startDate = dasha1.endDate;
            }

            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {
                dashaList.add(dasha1);
                dashaList.addAll(calcSubDasha(dasha1, index - 1, 2, compCal));
            }
        }

        Log.e("dashaObj", "DASHACALCULATION::" + dashaList.size());
        int i = 0;
        String dashaStr = "";

        for (dasha dashaObj : dashaList) {

            if (dashaObj.startDate.getTimeInMillis() <= birthDate.getTimeInMillis()) {
                dashaObj.startDate.setTimeInMillis(birthDate.getTimeInMillis());
            }

            LayoutInflater inflater = getLayoutInflater();
            View myLayout = inflater.inflate(R.layout.inflate_my_dasha, null, false);
            TextView dashaName = myLayout.findViewById(R.id.dashaName);
            TextView dashaPlanet = myLayout.findViewById(R.id.dashaPlanet);
            TextView dashaStart = myLayout.findViewById(R.id.dashaStart);
            TextView dashaEnd = myLayout.findViewById(R.id.dashaEnd);
            if (i % 2 != 0)
                myLayout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_day));
            else
                myLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            String startDateStr = dateFormat.format(dashaObj.startDate.getTime());
            String endDateStr = dateFormat.format(dashaObj.endDate.getTime());
            dashaName.setText(dashaCategory[i++]);
            dashaPlanet.setText(le_arr_planet[dashaObj.planet]);
            dashaStart.setText(startDateStr);
            dashaEnd.setText(endDateStr);
            dashaStr = dashaStr + " > " + le_arr_planet[dashaObj.planet];

            if (birthOrCurrType == 1) {
                janmaDashaCntr1.addView(myLayout);
            } else {
                currDashaCntr1.addView(myLayout);
            }
            Log.e("dashaObj", birthOrCurrType + "-" + i + ":-:DASHACALCULATION:-:" + dashaObj.type + "-" + le_arr_planet[dashaObj.planet] + "-" + startDateStr + "|" + endDateStr + "-" + dashaObj.totalyear);

        }
        if (birthOrCurrType == 1) {
            birthDasha.setText("BIRTH DASHA \n " + dashaStr.substring(3));
        } else {
            currDasha.setText("CURRENT DASHA \n " + dashaStr.substring(3));
        }


        // return dasha;

    }

    public ArrayList<dasha> calcAntarDasha(dasha dashaArg, int index, int type, Calendar compCal) {
        ArrayList<dasha> dashaList = new ArrayList<>();

        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dashaArg.startDate.getTimeInMillis());

        for (int i = 0; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            dasha1.planet = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (dashaArg.totalyear * dashaYear) / 120.0;
            long subDashaDaysMilli = (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000);
            // subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            dasha1.totalyear = subDasha;
            dasha1.type = type;// maha dasha
            dasha1.startDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.endDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.remYear = dasha1.totalyear;
            //dasha1.endDate.add(Calendar.DAY_OF_MONTH, subDashaDays);
            dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis() + subDashaDaysMilli);

            startDate.setTimeInMillis(dasha1.endDate.getTimeInMillis());
            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {

                dashaList.add(dasha1);
                dashaList.addAll(calcAntarDasha1(dasha1, index - 1, 3, compCal));
            }


        }
        return dashaList;

    }

    public ArrayList<dasha> calcAntarDasha1(dasha dashaArg, int index, int type, Calendar compCal) {
        ArrayList<dasha> dashaList = new ArrayList<>();

        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dashaArg.startDate.getTimeInMillis());

        for (int i = 0; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            dasha1.planet = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (dashaArg.totalyear * dashaYear) / 120.0;
            long subDashaDaysMilli = (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000);
            // subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            dasha1.totalyear = subDasha;
            dasha1.type = type;// maha dasha
            dasha1.startDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.endDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.remYear = dasha1.totalyear;
            //dasha1.endDate.add(Calendar.DAY_OF_MONTH, subDashaDays);
            dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis() + subDashaDaysMilli);

            startDate.setTimeInMillis(dasha1.endDate.getTimeInMillis());
            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {

                dashaList.add(dasha1);

                dashaList.addAll(calcAntarDasha2(dasha1, index - 1, 4, compCal));
            }


        }
        return dashaList;

    }

    public ArrayList<dasha> calcAntarDasha2(dasha dashaArg, int index, int type, Calendar compCal) {
        ArrayList<dasha> dashaList = new ArrayList<>();

        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dashaArg.startDate.getTimeInMillis());

        for (int i = 0; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            dasha1.planet = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (dashaArg.totalyear * dashaYear) / 120.0;
            long subDashaDaysMilli = (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000);
            // subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            dasha1.totalyear = subDasha;
            dasha1.type = type;// maha dasha
            dasha1.startDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.endDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.remYear = dasha1.totalyear;
            //dasha1.endDate.add(Calendar.DAY_OF_MONTH, subDashaDays);
            dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis() + subDashaDaysMilli);

            startDate.setTimeInMillis(dasha1.endDate.getTimeInMillis());
            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {

                dashaList.add(dasha1);
                dashaList.addAll(calcAntarDasha3(dasha1, index - 1, 5, compCal));
            }

            // dashaList.addAll(  calcAntarDasha1(dasha1, index-1,4));


        }
        return dashaList;

    }

    public ArrayList<dasha> calcAntarDasha3(dasha dashaArg, int index, int type, Calendar compCal) {
        ArrayList<dasha> dashaList = new ArrayList<>();

        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dashaArg.startDate.getTimeInMillis());

        for (int i = 0; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            dasha1.planet = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (dashaArg.totalyear * dashaYear) / 120.0;
            long subDashaDaysMilli = (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000);
            // subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            dasha1.totalyear = subDasha;
            dasha1.type = type;// maha dasha
            dasha1.startDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.endDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.remYear = dasha1.totalyear;
            //dasha1.endDate.add(Calendar.DAY_OF_MONTH, subDashaDays);
            dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis() + subDashaDaysMilli);

            startDate.setTimeInMillis(dasha1.endDate.getTimeInMillis());
            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {

                dashaList.add(dasha1);
            }

            // dashaList.addAll(  calcAntarDasha1(dasha1, index-1,4));


        }
        return dashaList;

    }

    public ArrayList<dasha> calcSubDasha(dasha dashaArg, int index, int type, Calendar compCal) {
        ArrayList<dasha> dashaList = new ArrayList<>();

        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dashaArg.startDate.getTimeInMillis());

        for (int i = 0; i < 9; i++) {
            if (index < 8) {
                index++;
            } else
                index = 0;
            dasha dasha1 = new dasha();
            dasha1.startDate = Calendar.getInstance();
            dasha1.endDate = Calendar.getInstance();
            dasha1.planet = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (dashaArg.totalyear * dashaYear) / 120.0;
            long subDashaDaysMilli = (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000);
            // subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            dasha1.totalyear = subDasha;
            dasha1.type = type;// maha dasha
            dasha1.startDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.endDate.setTimeInMillis(startDate.getTimeInMillis());
            dasha1.remYear = dasha1.totalyear;
            //dasha1.endDate.add(Calendar.DAY_OF_MONTH, subDashaDays);
            dasha1.endDate.setTimeInMillis(dasha1.startDate.getTimeInMillis() + subDashaDaysMilli);

            startDate.setTimeInMillis(dasha1.endDate.getTimeInMillis());
            if (dasha1.startDate.getTimeInMillis() < compCal.getTimeInMillis() && dasha1.endDate.getTimeInMillis() > compCal.getTimeInMillis()) {

                dashaList.add(dasha1);
                if (type < 5)
                    dashaList.addAll(calcSubDasha(dasha1, index - 1, ++type, compCal));
            }


        }
        return dashaList;

    }

    public String yearMonthDay(double year) {
        int compYear = (int) year;
        double fracYear = year - compYear;
        double days = fracYear * 365.2425;


        int compMonth = (int) days / 30;
        int compDays = (int) days % 30;
        double fracDays = days % 30;
        double remhours = (fracDays - compDays) * 24;
        int compHour = (int) remhours;
        int min = (int) ((remhours - compHour) * 60);

        dashaYears = compYear;
        dashaMonths = compMonth;
        dashaDays = compDays;
        dashaHrs = compHour;
        dashaMins = min;

        return compYear + " year(s) " + compMonth + "month(s) " + compDays + " days " + compHour + " hrs " + min + " min";

    }

    public String[] getLatLng(double deg) {
        String[] strArr;
        // deg=Math.abs(deg);
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

    public void createDiagram() {
        diagramCntr.removeAllViews();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int width = screenWidth;
        int height = screenWidth;
        KundaliDiagram pcc = new KundaliDiagram(mContext, width, height, houseinfosMap, nakshetraTithi, le_arr_rasi_kundali);
        //  for (int i = 0; i < 12; i++) {
        //    Log.e("houseinfosMap", i + "::houseinfosMap::" + houseinfosMap.get(i).houseno + ":" + houseinfosMap.get(i).planetIndexList + "::" + houseinfosMap.get(i).planetList);
        //}


        pcc.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        diagramCntr.addView(pcc);

    }

    public String checkSarpaDosha() {
        String[] KSY = le_arr_sarpa_yoga;// new String[]{"Anant Kala Sarpa Yoga", "Kulik Kala Sarpa Yoga", "Vasuki Kala Sarpa Yoga", "Shankhapal Kala Sarpa Yoga", "Padma Kala Sarpa Yoga", "Maha Padma Kala Sarpa Yoga", "Takshak Kala Sarpa Yoga", "Karkotak Kala Sarpa Yoga", "Shankhachoodh Kala Sarpa Yoga", "Ghatak Kala Sarpa Yoga", "Vishakt Kala Sarpa Yoga", "Shesh Nag Kala Sarpa Yoga"};

        int posRahu = -1, posKetu = -1;
        for (int i = 0; i < 12; i++) {
            if (houseinfosMap.get(i).planetIndexList.contains(",10,")) {
                posRahu = houseinfosMap.get(i).houseno;
            } else if (houseinfosMap.get(i).planetIndexList.contains(",11,")) {
                posKetu = houseinfosMap.get(i).houseno;
            }
            Log.e("houseinfosMap", i + ":SAR:houseinfosMap::" + houseinfosMap.get(i).houseno + ":" + houseinfosMap.get(i).planetIndexList + "::" + houseinfosMap.get(i).planetList);
        }
        Log.e("houseinfosMap", "::houseinfosMap::::::" + posRahu + "-" + posKetu);
        if (posRahu >= 0 && posKetu >= 0) {
            int lowerHouseNo = Math.min(posRahu, posKetu);
            int higherHouseNo = Math.max(posRahu, posKetu);
            Log.e("houseinfosMap", "::houseinfosMap::::::" + lowerHouseNo + "*" + higherHouseNo);
            boolean sarpaYoga1 = true;

            for (int i = lowerHouseNo + 1; i < lowerHouseNo + 6; i++) {
                if (!houseinfosMap.get(i).planetIndexList.contentEquals(",")) {
                    sarpaYoga1 = false;
                    break;
                }
            }
            if (!sarpaYoga1) {
                sarpaYoga1 = true;
                Log.e("houseinfosMap", "::houseinfosMap::::::**" + sarpaYoga1);
                for (int i = higherHouseNo + 1; i < higherHouseNo + 6; i++) {
                    int j = i;
                    if (j > 11) {
                        j = i - 12;
                    }
                    if (!houseinfosMap.get(j).planetIndexList.contentEquals(",")) {
                        sarpaYoga1 = false;
                        break;
                    }
                }
            }
            if (sarpaYoga1) {
                return KSY[posRahu];
            } else {
                return null;
            }

        } else {
            Log.e("houseinfosMap", "::houseinfosMap::FALSE");
            return null;
        }

    }
}

