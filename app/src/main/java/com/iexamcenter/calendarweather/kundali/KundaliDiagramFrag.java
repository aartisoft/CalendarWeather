package com.iexamcenter.calendarweather.kundali;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class KundaliDiagramFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    String dateTime = "", timeStr = "";
    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView titleInfo, jataka;
    View constraintLayout;
    Uri uriToImage;
    FloatingActionButton floating_action_button;
    private MainActivity mContext;

    static int selYear, selMonth, selDate, selHour, selMin;
    //  MaterialButton datePicker, timePicker;
    ArrayList<PlanetData> planetDataList;
    String[] le_arr_rasi_kundali;
    //RecyclerView planetinfo;
    // KundaliInfoListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    TextView lagna;
    String le_ghara;
    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise;
    ArrayList<KundaliDiagram.houseinfo> houseinfos;
    LinearLayout diagramCntr;
    int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali, jogaKundali, karanaKundali, moonSignKundali, sunSignKundali;
    int[] houseOwnerPlanet = new int[]{4, 3, 2, 1, 0, 2, 3, 4, 5, 6, 6, 5};
    String[] le_arr_planet,le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_karana, le_arr_joga, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String headerStr, kundaliPart1;
    String le_samvat, le_dina, le_ritu, le_sal, le_shakaddha, le_paksha;
    String le_lagna, le_mangala_dosha, le_tithi, le_nakshetra, le_joga, le_karana, le_lunar_rashi, le_solar_rasi;
    boolean isMangalaDosha = false;
    String latitude, longitude;
    HashMap<Integer, KundaliDiagram.houseinfo> houseinfosMap;
    Calendar selCalPrevday;
    boolean considerPrevday = false;
    String nakshetraTithi;
    int dashaYears, dashaMonths, dashaDays, dashaHrs, dashaMins;
    String dashaPlanetLord;
    TextView separator1,separator2;
    Resources res;
    int mType;
    private static int dateTimePickerCnt = 0;

    public static KundaliDiagramFrag newInstance() {

        return new KundaliDiagramFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    public void createDiagram() {
        diagramCntr.removeAllViews();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int width = screenWidth;
        int height = screenWidth;
        KundaliDiagram pcc = new KundaliDiagram(mContext, width, height, houseinfosMap, nakshetraTithi, le_arr_rasi_kundali);


        pcc.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        diagramCntr.addView(pcc);

    }
    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {

            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_planet = mContext.getResources().getStringArray(R.array.l_arr_planet);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.l_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            le_arr_karana = mContext.getResources().getStringArray(R.array.l_arr_karana);
            le_arr_joga = mContext.getResources().getStringArray(R.array.l_arr_joga);
            le_arr_month = mContext.getResources().getStringArray(R.array.l_arr_month);
            le_arr_bara = mContext.getResources().getStringArray(R.array.l_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.l_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.l_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.l_paksha);
            le_shakaddha = mContext.getResources().getString(R.string.l_shakaddha);
            le_sal = mContext.getResources().getString(R.string.l_sal);
            le_ritu = mContext.getResources().getString(R.string.l_ritu);
            le_dina = mContext.getResources().getString(R.string.l_dina);
            le_samvat = mContext.getResources().getString(R.string.l_samvat);
            le_tithi = mContext.getResources().getString(R.string.l_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.l_nakshetra);
            le_joga = mContext.getResources().getString(R.string.l_joga);
            le_karana = mContext.getResources().getString(R.string.l_karana);
            le_lunar_rashi = mContext.getResources().getString(R.string.l_lunar_rashi);
            le_solar_rasi = mContext.getResources().getString(R.string.l_solar_rasi);
            le_lagna = mContext.getResources().getString(R.string.l_lagna);
            le_arr_ausp_work_yes_no = mContext.getResources().getStringArray(R.array.l_arr_ausp_work_yes_no);
            le_mangala_dosha = mContext.getResources().getString(R.string.l_mangala_dosha);
            le_ghara = mContext.getResources().getString(R.string.l_ghara);

        }else{

            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_planet = mContext.getResources().getStringArray(R.array.e_arr_planet);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.e_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            le_arr_karana = mContext.getResources().getStringArray(R.array.e_arr_karana);
            le_arr_joga = mContext.getResources().getStringArray(R.array.e_arr_joga);
            le_arr_month = mContext.getResources().getStringArray(R.array.e_arr_month);
            le_arr_bara = mContext.getResources().getStringArray(R.array.e_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.e_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.e_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.e_paksha);
            le_shakaddha = mContext.getResources().getString(R.string.e_shakaddha);
            le_sal = mContext.getResources().getString(R.string.e_sal);
            le_ritu = mContext.getResources().getString(R.string.e_ritu);
            le_dina = mContext.getResources().getString(R.string.e_dina);
            le_samvat = mContext.getResources().getString(R.string.e_samvat);
            le_tithi = mContext.getResources().getString(R.string.e_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.e_nakshetra);
            le_joga = mContext.getResources().getString(R.string.e_joga);
            le_karana = mContext.getResources().getString(R.string.e_karana);
            le_lunar_rashi = mContext.getResources().getString(R.string.e_lunar_rashi);
            le_solar_rasi = mContext.getResources().getString(R.string.e_solar_rasi);
            le_lagna = mContext.getResources().getString(R.string.e_lagna);
            le_arr_ausp_work_yes_no = mContext.getResources().getStringArray(R.array.e_arr_ausp_work_yes_no);
            le_mangala_dosha = mContext.getResources().getString(R.string.e_mangala_dosha);
            le_ghara = mContext.getResources().getString(R.string.e_ghara);

        }
    }
    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {

            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            diagramCntr = rootView.findViewById(R.id.diagramCntr);
            lagna = rootView.findViewById(R.id.lagna);
            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();

            Calendar cal = Calendar.getInstance();

            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);

/*

            selDate = 1;
            selMonth =3;
            selYear = 1997;
            selHour = 12;
            selMin = 0;
            mPref.setLatitude("32.2");
            mPref.setLongitude("75.31");
            mPref.load();*/


            viewModel =new ViewModelProvider(this).get(MainViewModel.class);


            planetDataList = new ArrayList<>();
            titleInfo = rootView.findViewById(R.id.titleInfo);
            separator1 = rootView.findViewById(R.id.separator1);
            separator2 = rootView.findViewById(R.id.separator2);
            constraintLayout = rootView.findViewById(R.id.constraintLayout);
            floating_action_button = rootView.findViewById(R.id.floating_action_button);
            jataka = rootView.findViewById(R.id.jataka);
            progressBar = rootView.findViewById(R.id.progressbar);
            floating_action_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // share();
                    doShare("KundaliInfo.png");
                }
            });

            if (!viewModel.getDatePicker().isEmpty() && viewModel.getDatePicker().contains("-")) {
                dateTimePickerCnt++;
                String[] dateArr = viewModel.getDatePicker().split("-");
                selYear = Integer.parseInt(dateArr[0]);
                selMonth = Integer.parseInt(dateArr[1]);
                selDate = Integer.parseInt(dateArr[2]);

            }
            if (!viewModel.getTimePicker().isEmpty() && viewModel.getTimePicker().contains(":")) {
                dateTimePickerCnt++;
                String[] timeArr = viewModel.getTimePicker().split(":");
                selHour = Integer.parseInt(timeArr[0]);
                selMin = Integer.parseInt(timeArr[1]);
            }

            observerPlanetInfo();

           // Helper.getInstance().getSideRealTime();
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

    private class handlecalendarTask extends AsyncTask<String, Integer, Long> {
        HashMap<String, CoreDataHelper> myPanchangHashMap;

        public handlecalendarTask(HashMap<String, CoreDataHelper> panchangHashMap) {
            myPanchangHashMap = panchangHashMap;
        }

        protected Long doInBackground(String... urls) {
            Log.e("moonLng", ":::moonLng:-::-:doInBackground");
            setCalendarData(myPanchangHashMap);
            return 1L;
        }

        protected void onPostExecute(Long result) {
            try {

                if (mPref.getMyLanguage().contains("or") && mType!=1) {
                    titleInfo.setVisibility(View.VISIBLE);
                    jataka.setVisibility(View.VISIBLE);

                } else {
                    titleInfo.setVisibility(View.GONE);
                    jataka.setVisibility(View.GONE);
                }
                // titleInfo1.setText(headerStr1);

                lagna.setText(dateTime + " " + timeStr + "\n" + le_lagna + " " + lagnaLngNow);


                titleInfo.setText(kundaliPart1);
                progressBar.setVisibility(View.GONE);
                createDiagram();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            floating_action_button.setVisibility(View.GONE);
                            takeScreenshot();
                            floating_action_button.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 1000);
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
                    //  planetInfo = myCoreData.getPlanetInfo();
                }

                planetInfo = myCoreData.getPlanetInfo();
                // EphemerisEntity planetInfo = myCoreData.getPlanetInfo();


                setPlanetInfo(planetInfo);
                // PanchangUtility panchangUtility = new PanchangUtility();


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
                    moonSignKundali = moonSign.currVal;
                } else {
                    moonSignKundali = moonSign.nextVal;
                }
                Log.e(":nakshetra:", "DASHA:nakshetra:dob:" + selCal.getTimeInMillis() / 1000);
                Log.e(":nakshetra:", "DASHA:nakshetra:start:" + nakshetra.currValStartTime.getTimeInMillis() / 1000);
                Log.e(":nakshetra:", "DASHA:nakshetra:end:" + nakshetra.currValEndTime.getTimeInMillis() / 1000);
                Log.e(":nakshetra:", "DASHA:nakshetra:moons:" + moonSign.currValStartTime.getTimeInMillis() / 1000);
                Log.e(":nakshetra:", "DASHA:nakshetra:moonE:" + moonSign.currValEndTime.getTimeInMillis() / 1000);

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

                Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:" + lagnaListAtSunrise.size());
                lagnaLngAtSunrise = getLatLng(sunDegAtSunrise)[0];


                int size = lagnaListAtSunrise.size();
                Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:" + lagnaListAtSunrise.size());
                double elapse = 0, totalDeg = -1.0;
                double diff = 0;
                int lagnarashi = 0;
                Log.e("lagnaListAtSunrise", "totalDeg:::size:" + size + "::" + lagnaLngAtSunrise);
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
                    Log.e("lagnaListAtSunrise", "totalDeg::::::" + dateFormat.format(today) + "-" + dateFormat.format(start1) + "-" + dateFormat.format(end1));
                    Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:::" + selCal.getTimeInMillis() + "-" + end.getTimeInMillis() + "-" + start.getTimeInMillis());
                    Log.e("lagnaListAtSunrise", "totalDeg::::" + (selCal.getTimeInMillis() >= start.getTimeInMillis() && selCal.getTimeInMillis() <= end.getTimeInMillis()));

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
                        Log.e("Lagna", elapseDeg + ":LagnaASC:5:" + diff + "::" + elapse + "::" + totalDeg);
                        break;
                    }
                }

                Log.e("Lagna", ":LagnaASC:6:" + totalDeg);

                lagnaLngNow = getLatLng(totalDeg)[0];

                Log.e("Lagna", lagnarashi + ":LagnaASC:X:" + totalDeg + "::" + lagnaLngNow);
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
                    Log.e("nowlagnaval", lagnaLngAtSunrise + "::HOUSE:nowlagnaval:" + lagnaLngNow + ":totalDeg:" + totalDeg + ":diff:" + diff + ":elapse:" + elapse + ":--------:" + rashiHM.size() + ":rashiIndex:" + rashiIndex + "::" + planetDataList.get(i).deg + "::" + planetDataList.get(i).latLng + "::" + planetDataList.get(i).planet);

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

               /* for (int i = 0; i < planetInHouse.size(); i++) {
                    Log.e("nowlagnaval", "planet:" + i  + ":HOUSE:index:" + (planetInHouse.get(i)+1));
                }*/


                PanchangUtility panchangUtility = new PanchangUtility();
                PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), latitude, longitude, mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());
                String lgajapati;

                String lDay = Utility.getInstance(mContext).getDayNo("" + selDate);
                String lYear = Utility.getInstance(mContext).getDayNo("" + selYear);
                String lmonth = Utility.getInstance(mContext).getDayNo("" + (selMonth + 1));
                //  String lmonth = Utility.getInstance(mContext).getDayNo(""+selMonth1);
                String monthStr = le_arr_month[selMonth];


                String tithiVal = le_arr_tithi[tithiKundali - 1] + " " + le_tithi;

                String nakshetraVal = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra;

                //  nakshetraTithi=l_nakshatra_arr[nakshetraKundali - 1] +"\n"+"---"+"\n"+l_tithi_fullarr[tithiKundali - 1];
                nakshetraTithi = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_arr_rasi_kundali[moonSignKundali - 1];
                String jogaVal = le_arr_joga[jogaKundali - 1] + " " + le_joga;
                String karanaVal = le_arr_karana[karanaKundali - 1] + " " + le_karana;
                String moonSignVal = le_lunar_rashi +" "+ le_arr_rasi_kundali[moonSignKundali - 1] ;
                String sunSignVal = le_arr_rasi_kundali[sunSignKundali - 1] + " " + le_solar_rasi;
                String basare = getbara(selCal.get(Calendar.DAY_OF_WEEK) - 1);
//ଚାନ୍ଦ୍ରରାଶି

                if (!myPanchangObj.le_sanSalAnka.equals("0") && mPref.getMyLanguage().contains("or")) {
                    // ok down to 1970, chnage cond before 1970 added
                    lgajapati = ", ଗଜପତ୍ୟାଙ୍କ-" + myPanchangObj.le_sanSalAnka;
                    dateTime = lDay + " " + monthStr + " " + lYear;
                    timeStr = getFormattedDate(selCal);
                    headerStr = dateTime + ", " + le_samvat + "-" + myPanchangObj.le_samvata + ", " + le_shakaddha + "-" + myPanchangObj.le_sakaddha + ", " + le_sal + "-" + myPanchangObj.le_sanSal + lgajapati + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_bara;
                } else {
                    dateTime = lDay + " " + monthStr + " " + lYear;
                    timeStr = geteFormattedDate(selCal);
                    headerStr = dateTime + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_sakaddha + " " + le_shakaddha;
                }


                headerStr = headerStr + ", " + tithiVal + ", " + nakshetraVal + ", " + jogaVal + ", " + karanaVal + ", " + moonSignVal + ", " + sunSignVal;

                String dashaYearsStr = Utility.getInstance(mContext).getDayNo("" + dashaYears);
                String dashaMonthsStr = Utility.getInstance(mContext).getDayNo("" + dashaMonths);
                String dashaDaysStr = Utility.getInstance(mContext).getDayNo("" + dashaDays);
                String dashaHrsStr = Utility.getInstance(mContext).getDayNo("" + dashaHrs);
                String dashaMinsStr = Utility.getInstance(mContext).getDayNo("" + dashaMins);
                kundaliPart1 = "ବୀର ଶ୍ରୀ ଗଜପତି ଗୌଡ଼େଶ୍ବର ନବକୋଟୀ କର୍ଣ୍ଣାଟୋତ୍କଳବର୍ଗେଶ୍ବର ପ୍ରତାପୀ ଶ୍ରୀ ଦିବ୍ଯସିଂହଦେବ ମହାରାଜାଙ୍କ ବିଜୟ ଶୁଭରାଜ୍ଯେ ସମସ୍ତ " + myPanchangObj.le_sanSalAnka + " ଅଙ୍କେ," + le_sal + " " + myPanchangObj.le_sanSal + " " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_paksha + " " + tithiVal + " ଇଂରାଜୀ ତା " + lDay + "|" + lmonth + "|" + lYear + " ରିଖ " + basare + " ବାସରେ ସୂର୍ଯ୍ୟ ଉଦୟକୁ " + lagnaLngNow.split(" ")[1] + " ଲଗ୍ନେ, " + timeStr + " ସମୟକୁ ଓଡ଼ିଶା ଭୂଖଣ୍ଡେ .................ଜିଲ୍ଲା ଅନ୍ତର୍ଗତ................... ଗ୍ରାମେ ଶ୍ରୀ...............  ........ଙ୍କ ସହଧର୍ମିଣୀ ଉଦରେ................ ମ/ୟ ଗର୍ଭକୁ ଶୁଭ କୁମାର/କୁମାରୀ ଜନ୍ମ। ନକ୍ଷତ୍ର " + le_arr_nakshatra[nakshetraKundali - 1] + " " + moonSignVal + " ତଥା ପରମାୟୁ ୧୨୦ ବର୍ଷକୁ ରାଶିନାମ..................... ଓ ଶ୍ରଦ୍ଧାନାମ.......................। କେରଳ ଶାସ୍ତ୍ରୋକ୍ତେ ଜନ୍ମ ସମୟକୁ " + dashaPlanetLord + " ମହାଦଶାରୁ ବାକି " + dashaYearsStr + " ବର୍ଷ " + dashaMonthsStr + " ମାସ " + dashaDaysStr + " ଦିନ " + dashaHrsStr + " ଘଣ୍ଟା " + dashaMinsStr + " ମିନିଟ";

                isMangalaDosha = false;
                for (int i = 0; i < planetDataList.size() - 3; i++) {
                    // PlanetData obj = planetDataList.get(i);

                    int houseNoIndex = planetInHouse.get(i);

                    if (i == 2 && (houseNoIndex == 0 || houseNoIndex == 1 || houseNoIndex == 3 || houseNoIndex == 6 || houseNoIndex == 7 || houseNoIndex == 11)) {
                        isMangalaDosha = true;
                    }

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getbara(int DAY_OF_WEEK) {
        switch (DAY_OF_WEEK) {
            case 0:
                return "ଅାଦିତ୍ୟ";
            case 1:
                return "ଚନ୍ଦ୍ର";
            case 2:
                return "ଭୌମ";
            case 3:
                return "ପଣ୍ଡିତ";
            case 4:
                return "ବୃହସ୍ପତି";
            case 5:
                return "ଭୃଗୁ";
            default:
                return "ସୌରୀ";

        }

    }
/*
    public static class houseinfo {
        public int houseno, rashi;
        public String planetList;
    }
*/
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

        pd.deg = currPlanetDeg;

        if (type == 2) {
            Log.e("moonLng", ":::moonLng:-::-:" + type);
            calc(currPlanetDeg);
        }
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

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.frag_kundali_diagram, null);
        setHasOptionsMenu(true);


        return rootView;

    }


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

    private String geteFormattedDate(Calendar cal) {

        try {
            Date date = cal.getTime();
            DateFormat dateFormat;
            String dt;


            dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            dt = dateFormat.format(date);


            return dt;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getFormattedDate(Calendar cal) {
        Resources res = mContext.getResources();
        PrefManager mPref = PrefManager.getInstance(mContext);
        mPref.load();
        String mLang = mPref.getMyLanguage();
        if (mLang.contains("or") && mType!=1) {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            int calHour = cal.get(Calendar.HOUR_OF_DAY);
            int calMin = cal.get(Calendar.MINUTE);
            int calMonth = cal.get(Calendar.MONTH);
            String calDayNoStr = Utility.getInstance(mContext).getDayNo("" + calDayNo);
            String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
            String prefixTime = "";
            if ((calHour > 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
                prefixTime = res.getString(R.string.l_time_night);
            }
            if (calHour >= 4 && calHour < 9) {
                prefixTime = res.getString(R.string.l_time_prattha);
            } else if (calHour >= 9 && calHour < 16) {
                prefixTime = res.getString(R.string.l_time_diba);
            } else if (calHour >= 16 && calHour < 19) {
                prefixTime = res.getString(R.string.l_time_sandhya);
            }
            if (calHour > 12) {
                calHour = calHour - 12;
            }
            String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

            String ldate;


            ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + res.getString(R.string.l_time_min);


            return ldate;
        } else {
            return geteFormattedDate(cal);
        }
    }

    public void calc(double moonLng1) {
        //
        // int moonLng =1832;//(30*60) + 32;//1832
        int moonLng = (((int) moonLng1) * 60) + (int) ((moonLng1 - (int) moonLng1) * 60); // in minute
        int[] orderOfDasha = {11, 3, 0, 1, 4, 10, 5, 6, 2};
        int[] orderOfDashaYear = {7, 20, 6, 10, 7, 18, 16, 19, 17};
        //int[] orderOfDashaYear = {6,10,17,20,7,16,19,18,7};


        int nakshetraIndex1 = moonLng / 800;
        double nakshetraDiv = moonLng / 800.0;
        double nakshetraDiv1 = nakshetraDiv - ((int) (nakshetraDiv));
        double nakshetraRem = 800 - (nakshetraDiv1 * 800.0);
        int index = nakshetraIndex1 % 9;
        int planetLordIndex = orderOfDasha[index];
        int planetLordYear = orderOfDashaYear[index];

        double yearsrem = (planetLordYear / 800.0) * nakshetraRem;
        dashaPlanetLord = le_arr_planet[planetLordIndex];
        String rem = yearMonthDay(yearsrem);
        Log.e("moonLng", rem + ":::moonLng:-::-:" + yearsrem + "::" + dashaPlanetLord + "::" + planetLordYear);

        // return dasha;

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


    private void takeScreenshot() {
        separator1.setVisibility(View.VISIBLE);
        separator2.setVisibility(View.VISIBLE);
        Bitmap bitmap =Helper.getInstance().loadBitmapFromView(constraintLayout);
        Helper.getInstance().writeFile(bitmap,"KundaliInfo.png",mContext);
        separator1.setVisibility(View.GONE);
        separator2.setVisibility(View.GONE);


    }

    private void doShare(String fileName) {

        File dir = new File(mContext.getFilesDir(), "images");
        File file = new File(dir, fileName);
        if (!file.exists()) {
            Toast.makeText(mContext, R.string.unable_to_share, Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Panchanga Darpana by iExamCenter on Google Play");
        intent.putExtra(Intent.EXTRA_TEXT, "Panchanga Darpana by iExamCenter on Google Play");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, R.string.no_app_available, Toast.LENGTH_SHORT).show();
        }
    }
}

