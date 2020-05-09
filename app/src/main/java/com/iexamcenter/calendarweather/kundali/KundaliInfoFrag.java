package com.iexamcenter.calendarweather.kundali;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.panchang.PanchangUtility;
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

public class KundaliInfoFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    boolean showSettingMenu = true;
    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView mangalDosa, lagna, titleInfo;
    private MainActivity mContext;

    int mType, selYear, selMonth, selDate, selHour, selMin;
    //  MaterialButton datePicker, timePicker;
    ArrayList<PlanetData> planetDataList;
    String[] le_arr_rasi_kundali, le_arr_planet;
    //RecyclerView planetinfo;
    // KundaliInfoListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    String le_ghara;
    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise;
    ArrayList<houseinfo> houseinfos;
    LinearLayout diagramCntr, houseInfoCntr, planeInfoCntr;
    int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali, jogaKundali, karanaKundali, moonSignKundali, sunSignKundali;
    LinearLayout infoHolder, houseHolder, planetHolder;
    // sun,moon,mars,mercury,jupitor,venus,saturn,rahu,ketu,uranus,neptune,pluto
    int[] houseOwnerPlanet = new int[]{4, 3, 2, 1, 0, 2, 3, 4, 5, 6, 6, 5};
    String[] le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_karana, le_arr_joga, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String headerStr;
    String le_samvat, le_dina, le_ritu, le_sal, le_shakaddha, le_paksha;
    String le_lagna, le_mangala_dosha, le_tithi, le_nakshetra, le_joga, le_karana, le_lunar_rashi, le_solar_rasi;
    boolean isMangalaDosha = false;
    String area, latitude, longitude;
    HashMap<Integer, KundaliInfoFrag.houseinfo> houseinfosMap;
    Calendar selCalPrevday;
    boolean considerPrevday = false;
    String nakshetraTithi;
    boolean isMoonSignFirstHalf = false;
    Resources res;
    String[] le_arr_kundali_gana, le_arr_kundali_yoni, le_arr_kundali_varna, le_arr_kundali_guna, le_arr_kundali_nadi, le_arr_kundali_vasya;
    String le_kundali_yoni_title, le_kundali_tara_title, le_kundali_vasya_title, le_kundali_guna_title, le_kundali_gana_title, le_kundali_nadi_title, le_kundali_varna_title;

    public static KundaliInfoFrag newInstance() {

        return new KundaliInfoFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!showSettingMenu)
            menu.clear();
    }

    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_arr_kundali_gana = res.getStringArray(R.array.l_arr_kundali_gana);
            le_arr_kundali_guna = res.getStringArray(R.array.l_arr_kundali_guna);
            le_arr_kundali_varna = res.getStringArray(R.array.l_arr_kundali_varna);
            le_arr_kundali_yoni = res.getStringArray(R.array.l_arr_kundali_yoni);
            le_arr_kundali_nadi = res.getStringArray(R.array.l_arr_kundali_nadi);
            le_arr_kundali_vasya = res.getStringArray(R.array.l_arr_kundali_vasya);
            le_kundali_yoni_title = res.getString(R.string.l_kundali_yoni_title);
            le_kundali_tara_title = res.getString(R.string.l_kundali_tara_title);
            le_kundali_vasya_title = res.getString(R.string.l_kundali_vasya_title);
            le_kundali_guna_title = res.getString(R.string.l_kundali_guna_title);
            le_kundali_gana_title = res.getString(R.string.l_kundali_gana_title);
            le_kundali_nadi_title = res.getString(R.string.l_kundali_nadi_title);
            le_kundali_varna_title = res.getString(R.string.l_kundali_varna_title);

            le_arr_rasi_kundali = res.getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_planet = res.getStringArray(R.array.l_arr_planet);
            le_arr_karana = res.getStringArray(R.array.l_arr_karana);
            le_paksha = res.getString(R.string.l_paksha);
            le_shakaddha = res.getString(R.string.l_shakaddha);
            le_sal = res.getString(R.string.l_sal);
            le_ritu = res.getString(R.string.l_ritu);
            le_dina = res.getString(R.string.l_dina);
            le_ghara = res.getString(R.string.l_ghara);
            le_samvat = res.getString(R.string.l_samvat);
            le_tithi = res.getString(R.string.l_tithi);
            le_nakshetra = res.getString(R.string.l_nakshetra);
            le_joga = res.getString(R.string.l_joga);
            le_karana = res.getString(R.string.l_karana);
            le_lunar_rashi = res.getString(R.string.l_lunar_rashi);
            le_solar_rasi = res.getString(R.string.l_solar_rasi);
            le_lagna = res.getString(R.string.l_lagna);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.l_mangala_dosha);
            le_arr_joga = res.getStringArray(R.array.l_arr_joga);
            le_arr_month = res.getStringArray(R.array.l_arr_month);
            le_arr_bara = res.getStringArray(R.array.l_arr_bara);
            le_arr_paksha = res.getStringArray(R.array.l_arr_paksha);
            le_arr_masa = res.getStringArray(R.array.l_arr_masa);
            le_arr_tithi = res.getStringArray(R.array.l_arr_tithi);
            le_arr_nakshatra = res.getStringArray(R.array.l_arr_nakshatra);
        } else {
            le_arr_kundali_gana = res.getStringArray(R.array.e_arr_kundali_gana);
            le_arr_kundali_guna = res.getStringArray(R.array.e_arr_kundali_guna);
            le_arr_kundali_varna = res.getStringArray(R.array.e_arr_kundali_varna);
            le_arr_kundali_yoni = res.getStringArray(R.array.e_arr_kundali_yoni);
            le_arr_kundali_nadi = res.getStringArray(R.array.e_arr_kundali_nadi);
            le_arr_kundali_vasya = res.getStringArray(R.array.e_arr_kundali_vasya);
            le_kundali_yoni_title = res.getString(R.string.e_kundali_yoni_title);
            le_kundali_tara_title = res.getString(R.string.e_kundali_tara_title);
            le_kundali_vasya_title = res.getString(R.string.e_kundali_vasya_title);
            le_kundali_guna_title = res.getString(R.string.e_kundali_guna_title);
            le_kundali_gana_title = res.getString(R.string.e_kundali_gana_title);
            le_kundali_nadi_title = res.getString(R.string.e_kundali_nadi_title);
            le_kundali_varna_title = res.getString(R.string.e_kundali_varna_title);

            le_arr_rasi_kundali = res.getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_planet = res.getStringArray(R.array.e_arr_planet);
            le_arr_karana = res.getStringArray(R.array.e_arr_karana);
            le_paksha = res.getString(R.string.e_paksha);
            le_shakaddha = res.getString(R.string.e_shakaddha);
            le_sal = res.getString(R.string.e_sal);
            le_ritu = res.getString(R.string.e_ritu);
            le_dina = res.getString(R.string.e_dina);
            le_ghara = res.getString(R.string.e_ghara);
            le_samvat = res.getString(R.string.e_samvat);
            le_tithi = res.getString(R.string.e_tithi);
            le_nakshetra = res.getString(R.string.e_nakshetra);
            le_joga = res.getString(R.string.e_joga);
            le_karana = res.getString(R.string.e_karana);
            le_lunar_rashi = res.getString(R.string.e_lunar_rashi);
            le_solar_rasi = res.getString(R.string.e_solar_rasi);
            le_lagna = res.getString(R.string.e_lagna);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.e_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.e_mangala_dosha);
            le_arr_joga = res.getStringArray(R.array.e_arr_joga);
            le_arr_month = res.getStringArray(R.array.e_arr_month);
            le_arr_bara = res.getStringArray(R.array.e_arr_bara);
            le_arr_paksha = res.getStringArray(R.array.e_arr_paksha);
            le_arr_masa = res.getStringArray(R.array.e_arr_masa);
            le_arr_tithi = res.getStringArray(R.array.e_arr_tithi);
            le_arr_nakshatra = res.getStringArray(R.array.e_arr_nakshatra);
        }

    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            setHasOptionsMenu(true);
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();
            houseInfoCntr = rootView.findViewById(R.id.house);
            planeInfoCntr = rootView.findViewById(R.id.planetInfo);
            //  diagramCntr = rootView.findViewById(R.id.diagramCntr);


            Calendar cal = Calendar.getInstance();
            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);

            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            planetDataList = new ArrayList<>();
            titleInfo = rootView.findViewById(R.id.titleInfo);
            progressBar = rootView.findViewById(R.id.progressbar);
            mangalDosa = rootView.findViewById(R.id.mangalDosa);
            lagna = rootView.findViewById(R.id.lagna);

            Bundle args = getArguments();

            if (args != null && args.getString("TYPE", "INFO").contains("KUNDALI_MATCH")) {
                showSettingMenu = false;
                Log.e("KUNDALI_MATCH", "KUNDALI_MATCH::1:");
                area = args.getString("area");
                latitude = args.getString("latitude");
                longitude = args.getString("longitude");
                selYear = args.getInt("year");
                selMonth = args.getInt("month");
                selDate = args.getInt("dayOfmonth");
                selHour = args.getInt("hour");
                selMin = args.getInt("min");
                observerPlanetInfo();
            } else {
                showSettingMenu = true;
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


                String lon = mPref.getLongitude();
                double lonDouble = Double.parseDouble(lon);
                double lmtMinute = lonDouble * 4;
                double diffLmt = lmtMinute - (5 * 60 + 30);
                Log.e("diffLmt", lmtMinute + "diffLmt:" + diffLmt + "::" + lonDouble + "::" + lon);
                Calendar cal1 = Calendar.getInstance();
                cal1.add(Calendar.MINUTE, (int) diffLmt);


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

        protected void onPostExecute(Long result) {
            try {
                planeInfoCntr.removeAllViews();
                houseInfoCntr.removeAllViews();
                lagna.setText(le_lagna + " " + lagnaLngNow);
                String mangalaDoshaVal = le_mangala_dosha + " - " + (isMangalaDosha ? le_arr_ausp_work_yes_no[0] : le_arr_ausp_work_yes_no[1]);
                mangalDosa.setText(mangalaDoshaVal);
                titleInfo.setText(headerStr);
                houseInfoCntr.addView(houseHolder);
                planeInfoCntr.addView(planetHolder);
                progressBar.setVisibility(View.GONE);
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


                String sunDeg = planetInfo.sun;
                String sunDmDeg = planetInfo.dmsun;
                double lat = Double.parseDouble(latitude);
                Calendar sunRiseVal = Calendar.getInstance(Locale.ENGLISH);
                sunRiseVal.set(Calendar.YEAR, myCoreData.getSunRise().get(Calendar.YEAR));
                sunRiseVal.set(Calendar.MONTH, myCoreData.getSunRise().get(Calendar.MONTH));
                sunRiseVal.set(Calendar.DAY_OF_MONTH, myCoreData.getSunRise().get(Calendar.DAY_OF_MONTH));
                sunRiseVal.set(Calendar.HOUR_OF_DAY, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY));
                sunRiseVal.set(Calendar.MINUTE, myCoreData.getSunRise().get(Calendar.MINUTE));
                sunRiseVal.set(Calendar.SECOND, 0);
                sunRiseVal.set(Calendar.MILLISECOND, 0);
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

                Log.e("latitude1", ":latitude1:X:" + lagnaListAtSunrise.size());
                CoreDataHelper.Panchanga tithi = myCoreData.getTithi();
                if (selCal.getTimeInMillis() <= tithi.currValEndTime.getTimeInMillis()) {
                    tithiKundali = tithi.currVal;
                } else if (selCal.getTimeInMillis() <= tithi.le_nextValEndTime.getTimeInMillis()) {
                    tithiKundali = tithi.nextVal;
                } else {
                    tithiKundali = tithi.nextToNextVal;
                }

                CoreDataHelper.Panchanga nakshetra = myCoreData.getNakshetra();
                if (selCal.getTimeInMillis() <= nakshetra.currValEndTime.getTimeInMillis()) {
                    nakshetraKundali = nakshetra.currVal;
                } else if (selCal.getTimeInMillis() <= nakshetra.le_nextValEndTime.getTimeInMillis()) {
                    nakshetraKundali = nakshetra.nextVal;
                } else {
                    nakshetraKundali = nakshetra.nextToNextVal;
                }

                CoreDataHelper.Panchanga joga = myCoreData.getJoga();
                if (selCal.getTimeInMillis() <= joga.currValEndTime.getTimeInMillis()) {
                    jogaKundali = joga.currVal;
                } else if (selCal.getTimeInMillis() <= joga.le_nextValEndTime.getTimeInMillis()) {
                    jogaKundali = joga.nextVal;
                } else {
                    jogaKundali = joga.nextToNextVal;
                }

                CoreDataHelper.Karana karana = myCoreData.getKarana();
                if (selCal.getTimeInMillis() <= karana.val1ET.getTimeInMillis()) {
                    karanaKundali = karana.val1;
                } else if (selCal.getTimeInMillis() <= karana.val2ET.getTimeInMillis()) {
                    karanaKundali = karana.val2;
                } else if (selCal.getTimeInMillis() <= karana.val3ET.getTimeInMillis()) {
                    karanaKundali = karana.val3;
                } else if (selCal.getTimeInMillis() <= karana.val4ET.getTimeInMillis()) {
                    karanaKundali = karana.val4;
                } else if (selCal.getTimeInMillis() <= karana.val5ET.getTimeInMillis()) {
                    karanaKundali = karana.val5;
                } else {
                    karanaKundali = karana.val6;
                }

                CoreDataHelper.Panchanga moonSign = myCoreData.getMoonSign();

                if (selCal.getTimeInMillis() <= moonSign.currValEndTime.getTimeInMillis()) {
                    long moonSignhalf = moonSign.currValEndTime.getTimeInMillis() - moonSign.currValStartTime.getTimeInMillis();
                    moonSignKundali = moonSign.currVal;
                    if ((selCal.getTimeInMillis() - moonSign.currValStartTime.getTimeInMillis()) > moonSignhalf) {
                        isMoonSignFirstHalf = false;
                    } else {
                        isMoonSignFirstHalf = true;
                    }


                } else {
                    moonSignKundali = moonSign.nextVal;
                    long moonSignhalf = moonSign.le_nextValEndTime.getTimeInMillis() - moonSign.currValEndTime.getTimeInMillis();
                    if ((selCal.getTimeInMillis() - moonSign.currValEndTime.getTimeInMillis()) > moonSignhalf) {
                        isMoonSignFirstHalf = false;
                    } else {
                        isMoonSignFirstHalf = true;
                    }

                }


                CoreDataHelper.Panchanga sunSign = myCoreData.getSunSign();
                if (selCal.getTimeInMillis() <= sunSign.currValEndTime.getTimeInMillis()) {
                    sunSignKundali = sunSign.currVal;
                } else {
                    sunSignKundali = sunSign.nextVal;
                }

                paksha = myCoreData.getPaksha();
                weekDay = myCoreData.getDayOfWeek();
                solarMonth = myCoreData.getAdjustSolarMonth();
                solarDay = myCoreData.getSolarDayVal();
                lunarMonth = myCoreData.getLunarMonthPurnimantIndex();
                lunarDay = myCoreData.getFullMoonLunarDay();

                //int tithiKundali,nakshetraKundali,jogaKundali,karanaKundali,moonSignKundali,sunSignKundali;

                //  Log.e("DAYINFO",":joga:"++":karana:"++":moonSign:"++":sunSign:"++":paksha:"++":weekDay:"+);


                // PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude(), mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());


                lagnaLngAtSunrise = getLatLng(sunDegAtSunrise)[0];


                int size = lagnaListAtSunrise.size();
                Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:" + lagnaListAtSunrise.size());
                double elapse = 0, totalDeg = -1.0;
                double diff = 0;
                int lagnarashi = 0;
                for (int x = 0; x < size; x++) {
                    Calendar end = lagnaListAtSunrise.get(x).end;
                    Calendar start = lagnaListAtSunrise.get(x).start;

                    SimpleDateFormat dateFormat;

                    dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);

                    // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date today = selCal.getTime();
                    Date end1 = end.getTime();
                    Date start1 = start.getTime();
                    dateFormat.format(today);
                    Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:::" + dateFormat.format(today) + "-" + dateFormat.format(start1) + "-" + dateFormat.format(end1));
                    Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:::" + selCal.getTimeInMillis() + "-" + end.getTimeInMillis() + "-" + start.getTimeInMillis());

                    if (selCal.getTimeInMillis() >= start.getTimeInMillis() && selCal.getTimeInMillis() <= end.getTimeInMillis()) {
                        Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:x:" + lagnaListAtSunrise.size());

                        diff = end.getTimeInMillis() - start.getTimeInMillis();
                        elapse = selCal.getTimeInMillis() - start.getTimeInMillis();
                        diff = Math.abs(diff);
                        elapse = Math.abs(elapse);
                        double elapseDeg = ((30.0 / diff) * elapse);
                        int index = lagnarashi = lagnaListAtSunrise.get(x).rashi;
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


                Log.e("nowlagnaval", lagnaLngAtSunrise + "::HOUSE:nowlagnaval:" + lagnaLngNow + ":totalDeg:" + totalDeg + ":diff:" + diff + ":elapse:" + elapse);

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
                    houseinfo obj = new houseinfo();
                    obj.houseno = house;
                    obj.rashi = rashiInHouse.get(house);
                    String planetStr = "";
                    for (int i = 0; i < planetInHouse.size(); i++) {
                        if (planetInHouse.get(i) == house) {
                            planetStr = planetStr + le_arr_planet[planetHM.get(i)] + ",";
                        }

                    }
                    houseinfosMap.put(house, obj);
                    obj.planetList = planetStr;
                    houseinfos.add(obj);
                }


                houseHolder = new LinearLayout(mContext);
                houseHolder.setOrientation(LinearLayout.VERTICAL);
                for (int i = 0; i < houseinfos.size(); i++) {
                    houseinfo obj = houseinfos.get(i);
                    LinearLayout houseHolderRow = new LinearLayout(mContext);
                    houseHolderRow.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    houseHolderRow.setLayoutParams(lp);
                    Log.e("nowlagnaval", "house:" + (i + 1) + ":HOUSE:index:" + obj.houseno + ":rashi:" + obj.rashi + ":currplanet:" + obj.planetList);


                    String ownerHouse = "" + le_arr_planet[houseOwnerPlanet[i]];
                    String currPlanet = "" + obj.planetList;
                    if (!currPlanet.isEmpty()) {
                        currPlanet = currPlanet.replaceAll(",$", "");
                    }
                    String houseNoStr = Utility.getInstance(mContext).getDayNo("" + (i + 1));
                    houseHolderRow.addView(addTv1(houseNoStr, 1f));
                    houseHolderRow.addView(addTv1(currPlanet, 3f));
                    houseHolderRow.addView(addTv1("" + ownerHouse, 1f));
                    houseHolderRow.addView(addTv1("" + le_arr_rasi_kundali[obj.rashi], 1f));
                    houseHolder.addView(houseHolderRow);
                }


                infoHolder = new LinearLayout(mContext);
                infoHolder.setOrientation(LinearLayout.VERTICAL);


                PanchangUtility panchangUtility = new PanchangUtility();
                PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), latitude, longitude, mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());
                String lgajapati;

                String lDay = Utility.getInstance(mContext).getDayNo("" + selDate);
                String lYear = Utility.getInstance(mContext).getDayNo("" + selYear);

                //  String lmonth = Utility.getInstance(mContext).getDayNo(""+selMonth1);
                String monthStr = le_arr_month[selMonth];


                String tithiVal = le_arr_tithi[tithiKundali - 1] + " " + le_tithi;

                String nakshetraVal = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra;

                //  nakshetraTithi=l_nakshatra_arr[nakshetraKundali - 1] +"\n"+"---"+"\n"+l_tithi_fullarr[tithiKundali - 1];
                nakshetraTithi = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_arr_rasi_kundali[moonSignKundali - 1];
                String jogaVal = le_arr_joga[jogaKundali - 1] + " " + le_joga;
                String karanaVal = le_arr_karana[karanaKundali - 1] + " " + le_karana;
                String moonSignVal = le_arr_rasi_kundali[moonSignKundali - 1] + " " + le_lunar_rashi;
                String sunSignVal = le_arr_rasi_kundali[sunSignKundali - 1] + " " + le_solar_rasi;


                int gana = getGana(nakshetraKundali);
                int guna = getGuna(moonSignKundali);
                int varna = getVarna(moonSignKundali);
                int nadi = getNadi(nakshetraKundali);
                int yoni = getYoni(nakshetraKundali);
                //int tara = getTara(nakshetraKundali);
                int vasya = getVasya(moonSignKundali);


                String ganaStr = le_arr_kundali_gana[gana];
                String gunaStr = le_arr_kundali_guna[guna];
                String varnaStr = le_arr_kundali_varna[varna];
                String yoniStr = le_arr_kundali_yoni[yoni];
                String nadiStr = le_arr_kundali_nadi[nadi];
                String vasyaStr = le_arr_kundali_vasya[vasya];
                String janmaTaraStr = le_arr_nakshatra[nakshetraKundali - 1];

                String extraStr = janmaTaraStr + " " + le_kundali_tara_title + ", " + ganaStr + " " + le_kundali_gana_title + ", " + gunaStr + " " + le_kundali_guna_title + ", " + varnaStr + " " + le_kundali_varna_title + ", " + yoniStr + " " + le_kundali_yoni_title + ", " + nadiStr + " " + le_kundali_nadi_title + ", " + vasyaStr + " " + le_kundali_vasya_title;

                Log.e("ganaStr", ",ganaStr-" + ganaStr + ",gunaStr-" + gunaStr + ",varnaStr-" + varnaStr + ",yoniStr-" + yoniStr + ",nadiStr-" + nadiStr + ",janmaTaraStr-" + janmaTaraStr + ",vasyaStr-" + vasyaStr);
                String timeVal = "";//getFormattedDate(selCal);
                if (!myPanchangObj.le_sanSalAnka.equals("0") && mPref.getMyLanguage().contains("or")) {
                    // ok down to 1970, chnage cond before 1970 added
                    lgajapati = mType == 0 ? ", ଗଜପତ୍ୟାଙ୍କ-" + myPanchangObj.le_sanSalAnka : ", Gajapatyanka-" + myPanchangObj.le_sanSalAnka;
                    headerStr = lDay + " " + monthStr + " " + lYear + " " + timeVal + " " + ", " + le_samvat + "-" + myPanchangObj.le_samvata + ", " + le_shakaddha + "-" + myPanchangObj.le_sakaddha + ", " + le_sal + "-" + myPanchangObj.le_sanSal + lgajapati + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_bara;
                } else {
                    headerStr = lDay + " " + monthStr + " " + lYear + " " + timeVal + " " + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_sakaddha + " " + le_shakaddha;
                }
                headerStr = headerStr + ", " + tithiVal + ", " + nakshetraVal + ", " + jogaVal + ", " + karanaVal + ", " + moonSignVal + ", " + sunSignVal + ", " + extraStr;

                planetHolder = new LinearLayout(mContext);
                planetHolder.setOrientation(LinearLayout.VERTICAL);
                isMangalaDosha = false;
                for (int i = 0; i < planetDataList.size() - 3; i++) {
                    PlanetData obj = planetDataList.get(i);
                    LinearLayout planetHolderRow = new LinearLayout(mContext);
                    planetHolderRow.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    planetHolderRow.setLayoutParams(lp);

                    String[] latLng = getLatLng(obj.deg);
                    TextView planetTv = addTv1("" + obj.planet + "(" + latLng[0] + ")", 1f);
                    int drawable = obj.direction == 0 ? R.drawable.ic_rotate_right_black_24dp : R.drawable.ic_rotate_left_black_24dp;
                    int houseNoIndex = planetInHouse.get(i);

                    if (i == 2 && (houseNoIndex == 0 || houseNoIndex == 1 || houseNoIndex == 3 || houseNoIndex == 6 || houseNoIndex == 7 || houseNoIndex == 11)) {
                        isMangalaDosha = true;
                    }

                    planetTv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
                    planetTv.setLayoutParams(param);
                    planetTv.setGravity(Gravity.START);

                    planetHolderRow.addView(planetTv);
                    String houseNoStr = Utility.getInstance(mContext).getDayNo("" + (houseNoIndex + 1));
                    planetHolderRow.addView(addTv1(houseNoStr, 1f));


                    String str1 = le_arr_planet[houseOwnerPlanet[houseNoIndex]];//lplanetList[planetHM.get(i)];
                    //lplanetList[houseOwnerPlanet[houseNoIndex]]
                    planetHolderRow.addView(addTv1("" + str1, 1f));

                    planetHolder.addView(planetHolderRow);

                    //holder.direction.setImageResource(obj.direction == 0 ? R.drawable.ic_rotate_right_black_24dp : R.drawable.ic_rotate_left_black_24dp);
                }
               /* addTv("lagna", lagnaLngNow);
                addTv("lunarMonth", lunarDay + "-" + l_masa_arr[lunarMonth]);
                addTv("solarMonth", solarDay + "-" + lrashiList[solarMonth]);
                addTv("paksha", l_paksha_arr[paksha]);
                addTv("weekDay", l_bara_arr[weekDay - 1]);
                addTv("tithi", l_tithi_fullarr[tithiKundali - 1]);
                addTv("nakshetra", l_nakshatra_arr[nakshetraKundali - 1]);
                addTv("joga", l_joga_arr[jogaKundali - 1]);
                addTv("karana", l_karana_fullarr[karanaKundali - 1]);
                addTv("moonSign", lrashiList[moonSignKundali - 1]);
                addTv("sunSign", lrashiList[sunSignKundali - 1]);*/

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getGana(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        //Sattva Guna,Rakshasa Gana
        int gana = 2, guna = 2;
        if (starNo == 1 || starNo == 5 || starNo == 7 || starNo == 8 || starNo == 13 || starNo == 15 || starNo == 17 || starNo == 22 || starNo == 27) {
            gana = 0;//dev
        } else if (starNo == 2 || starNo == 4 || starNo == 6 || starNo == 11 || starNo == 12 || starNo == 20 || starNo == 21 || starNo == 25 || starNo == 26) {
            gana = 1;//manaba
        }
        return gana;

    }

    private int getGuna(int moonSignKundali) {
        int guna = 2, moonsignNo = moonSignKundali;

        if (moonsignNo == 1 || moonsignNo == 4 || moonsignNo == 7 || moonsignNo == 10) {
            guna = 0;//Rajas Guna
        } else if (moonsignNo == 2 || moonsignNo == 5 || moonsignNo == 8 || moonsignNo == 11) {
            guna = 1;//Tamas Guna
        }
        return guna;
    }

    private int getVasya(int moonSignKundali) {
        int vasya = 4;
        int moonsignNo = moonSignKundali;
        if (moonsignNo == 1 || moonsignNo == 2 || (!isMoonSignFirstHalf && moonsignNo == 9) || (isMoonSignFirstHalf && moonsignNo == 10)) { //the second half of Dhanu, the first half of Makara.
            vasya = 0;//Chatushpada
        } else if (moonsignNo == 3 || moonsignNo == 6 || moonsignNo == 7 || moonsignNo == 11 || (isMoonSignFirstHalf && moonsignNo == 9)) {//the first half of Dhanu,
            vasya = 1;//Nara
        } else if (moonsignNo == 4 || moonsignNo == 12 || (!isMoonSignFirstHalf && moonsignNo == 10)) {//the 2nd half of Makara.
            vasya = 2;//Jalachara
        } else if (moonsignNo == 5) {
            vasya = 2;//Vanachara
        } else if (moonsignNo == 8) {
            vasya = 2;//Keeta
        }
        return vasya;
    }

    private int getVarna(int moonSignKundali) {
        int varna = 3;
        int moonsignNo = moonSignKundali;
        if (moonsignNo == 4 || moonsignNo == 8 || moonsignNo == 12) {
            varna = 0;//Rajas Guna
        } else if (moonsignNo == 1 || moonsignNo == 5 || moonsignNo == 9) {
            varna = 1;//Tamas Guna
        } else if (moonsignNo == 2 || moonsignNo == 6 || moonsignNo == 10) {
            varna = 2;//Tamas Guna
        }
        return varna;
    }

    private int getNadi(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        int nadi = 2;//Antya
        if (starNo == 1 || starNo == 6 || starNo == 13 || starNo == 18 || starNo == 19 || starNo == 25 || starNo == 7 || starNo == 12 || starNo == 24) {
            nadi = 0;//Adi
        } else if (starNo == 2 || starNo == 17 || starNo == 14 || starNo == 23 || starNo == 5 || starNo == 11 || starNo == 20 || starNo == 8 || starNo == 26) {
            nadi = 1;//MAdhya
        }
        return nadi;

    }

    public int getTara(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        int tara = -1;

        if (starNo == 1 || starNo == 24) {
            tara = 0;
        }
        return tara;
    }

    public int getYoni(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        int joni = -1;

        if (starNo == 1 || starNo == 24) {
            joni = 0;
        } else if (starNo == 2 || starNo == 27) {
            joni = 1;
        } else if (starNo == 3 || starNo == 8) {
            joni = 2;
        } else if (starNo == 4 || starNo == 5) {
            joni = 3;
        } else if (starNo == 6 || starNo == 19) {
            joni = 4;
        } else if (starNo == 7 || starNo == 9) {
            joni = 5;
        } else if (starNo == 10 || starNo == 11) {
            joni = 6;
        } else if (starNo == 12 || starNo == 26) {
            joni = 7;
        } else if (starNo == 13 || starNo == 15) {
            joni = 8;
        } else if (starNo == 14 || starNo == 16) {
            joni = 9;
        } else if (starNo == 17 || starNo == 18) {
            joni = 10;
        } else if (starNo == 20 || starNo == 22) {
            joni = 11;
        } else if (starNo == 21) {
            joni = 12;
        } else if (starNo == 23 || starNo == 25) {
            joni = 13;
        }
        return joni;

    }

    public TextView addTv1(String val, float weight) {
        TextView tv = new TextView(mContext);
        tv.setText(val);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.START);

        return tv;

    }

    public void addTv(String title, String val) {
        TextView tv = new TextView(mContext);
        tv.setText(title + ":" + val);

        infoHolder.addView(tv);

    }

    public static class houseinfo {
        public int houseno, rashi;
        public String planetList;
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

    public String getLatLng1(double deg) {
        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        double fractionInSec = (deg - val) * 3600;

        long min = (int) fractionInSec / 60;
        long sec = (int) fractionInSec % 60;

        String remDegStr = "" + remDeg;
        String minStr = "" + min;
        String secStr = "" + sec;
        return remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";
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


        //  DecimalFormat f = new DecimalFormat("##.00");
        // pd.deg = f.format(currPlanetDeg);
        pd.deg = currPlanetDeg;

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
        String latLng = remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";
        pd.latLng = latLng;
        // return latLng1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frag_kundali_info, null);


        return rootView;

    }


    /*
         private void showDatePickerDialog(View v) {
             //  DialogFragment newFragment = new DatePickerFragment();
             // newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
             FragmentManager fm = getFragmentManager();


             DialogFragment newFragment = new DatePickerFragment();
             newFragment.setTargetFragment(KundaliInfoFrag.this, REQUEST_DATE_CODE);
             newFragment.show(fm, "datePicker");

         }

         private void showTimePickerDialog(View v) {
             FragmentManager fm = getFragmentManager();
             DialogFragment newFragment = new TimePickerFragment();
             newFragment.setTargetFragment(KundaliInfoFrag.this, REQUEST_TIME_CODE);
             newFragment.show(fm, "timePicker");
         }

     */
    public void observerPlanetInfo() {
        Log.e("observerPlanetInfo", "observerPlanetInfo:" + latitude + "::" + longitude);
        progressBar.setVisibility(View.VISIBLE);


        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear + 100);
        selCal1.set(Calendar.MONTH, selMonth);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).observe(getViewLifecycleOwner(), obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);

                new handlecalendarTask(myPanchangHashMap).execute();
            }
        });

    }


    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }

}

