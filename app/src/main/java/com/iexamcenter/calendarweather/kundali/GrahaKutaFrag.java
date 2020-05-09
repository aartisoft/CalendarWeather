package com.iexamcenter.calendarweather.kundali;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
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

public class GrahaKutaFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    String dateTime = "", timeStr = "";

    PrefManager mPref;
    String lang;
    Uri uriToImage;
    private MainActivity mContext;

   // static int selYear=2020, selMonth=1, selDate=1, selHour=6, selMin=30;
    //  MaterialButton datePicker, timePicker;
    ArrayList<PlanetData> planetDataList;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    //RecyclerView planetinfo;
    // KundaliInfoListAdapter mPlanetInfoAdapter;

    String pkey_ghara;
    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise;
    ArrayList<houseinfo> houseinfos;

   // int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali, jogaKundali, karanaKundali, moonSignKundali, sunSignKundali;
   // int[] houseOwnerPlanet = new int[]{4, 3, 2, 1, 0, 2, 3, 4, 5, 6, 6, 5};
    //String[] yes_noTitle, l_tithi_fullarr, l_nakshatra_arr, l_karana_fullarr, l_joga_arr, l_month_arr, l_bara_arr, l_paksha_arr, l_masa_arr;
   // String headerStr, kundaliPart1;
  //  String samvat, dina, lritu, lsala, lshakaddha, lpaksha;
   // String lagna_title, mangala_doshaTitle, tithiTitle, nakshetraTitle, jogaTitle, karanaTitle, moonSignTitle, sunSignTitle;
   // boolean isMangalaDosha = false;
    String latitude, longitude;
    HashMap<Integer, GrahaKutaFrag.houseinfo> houseinfosMap;
    Calendar selCalPrevday;
    boolean considerPrevday = false;
    int selYear=2020, selMonth=1, selDate=1, selHour=6, selMin=30;
    MainViewModel viewModel;
    public static GrahaKutaFrag newInstance() {

        return new GrahaKutaFrag();
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

            System.out.println("TIMESTART1:"+System.currentTimeMillis());
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            lrashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);

          //  selYear=2020; selMonth=1; selDate=1; selHour=6; selMin=30;
            /*
            rashiList = mContext.getResources().getStringArray(R.array.rasi_kundali_arr);
            erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

            eplanetList = mContext.getResources().getStringArray(R.array.e_planet_arr);

            l_tithi_fullarr = mContext.getResources().getStringArray(R.array.l_tithi_fullarr);
            l_nakshatra_arr = mContext.getResources().getStringArray(R.array.l_nakshatra_arr);
            l_karana_fullarr = mContext.getResources().getStringArray(R.array.l_karana_fullarr);
            l_joga_arr = mContext.getResources().getStringArray(R.array.l_joga_arr);
            l_month_arr = mContext.getResources().getStringArray(R.array.l_month_arr);
            l_bara_arr = mContext.getResources().getStringArray(R.array.l_bara_arr);
            l_paksha_arr = mContext.getResources().getStringArray(R.array.l_paksha_arr);
            l_masa_arr = mContext.getResources().getStringArray(R.array.l_masa_arr);

            lpaksha = mContext.getResources().getString(R.string.o_paksha);
            lshakaddha = mContext.getResources().getString(R.string.pkey_shakaddha);
            lsala = mContext.getResources().getString(R.string.o_sal);
            lritu = mContext.getResources().getString(R.string.o_ritu);
            dina = mContext.getResources().getString(R.string.o_dina);

            samvat = mContext.getResources().getString(R.string.pkey_samvat);
            tithiTitle = mContext.getResources().getString(R.string.pkey_tithi);
            nakshetraTitle = mContext.getResources().getString(R.string.pkey_nakshetra);
            jogaTitle = mContext.getResources().getString(R.string.pkey_joga);
            karanaTitle = mContext.getResources().getString(R.string.pkey_karana);
            moonSignTitle = mContext.getResources().getString(R.string.pkey_lunar_rashi);
            sunSignTitle = mContext.getResources().getString(R.string.pkey_solar_rasi);

            lagna_title = mContext.getResources().getString(R.string.lagna);
            yes_noTitle = mContext.getResources().getStringArray(R.array.ausp_work_yes_no);
            mangala_doshaTitle = mContext.getResources().getString(R.string.pkey_mangala_dosha);


            pkey_ghara = mContext.getResources().getString(R.string.pkey_ghara);

            */

            Calendar cal = Calendar.getInstance();

            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);


            viewModel = new ViewModelProvider(this).get(MainViewModel.class);

            planetDataList = new ArrayList<>();


            observerPlanetInfo();



        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("TIMESTART1:"+System.currentTimeMillis());
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
                    //  planetInfo = myCoreData.getPlanetInfo();
                }

                planetInfo = myCoreData.getPlanetInfo();


                setPlanetInfo(planetInfo);


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
                int lagnarashi = 0;
                for (int x = 0; x < size; x++) {
                    Calendar end = lagnaListAtSunrise.get(x).end;
                    Calendar start = lagnaListAtSunrise.get(x).start;

                    SimpleDateFormat dateFormat;

                    dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);
                    Date today = selCal.getTime();
                    Date end1 = end.getTime();
                    Date start1 = start.getTime();
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
                    houseinfo obj = new houseinfo();
                    obj.houseno = house;
                    obj.rashi = rashiInHouse.get(house);
                    String planetStr = "";
                    for (int i = 0; i < planetInHouse.size(); i++) {
                        if (planetInHouse.get(i) == house) {
                            planetStr = planetStr + lplanetList[planetHM.get(i)] + ",";
                        }

                    }
                    houseinfosMap.put(house, obj);
                    obj.planetList = planetStr;
                    houseinfos.add(obj);
                }

            }
            for(int i=0;i<houseinfosMap.size();i++){

                System.out.println("TIMESTART1:1:"+ houseinfosMap.get(i).houseno);
                System.out.println("TIMESTART1:2:"+ houseinfosMap.get(i).planetList);
                System.out.println("TIMESTART1:3:"+ houseinfosMap.get(i).rashi);
            }
            System.out.println("TIMESTART1:3:"+ System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class houseinfo {
        public int houseno, rashi;
        public String planetList;
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
            latLng = remDegStr + "° " + lrashiList[index] + " " + minStr + "′" + " " + secStr + "′′";
            deg1Str = degStr.split("\\.")[0];
            deg2Str = degStr.split("\\.")[1];

            degStr = deg1Str + "." + deg2Str;

        } else {
             deg1Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[0]);
            deg2Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[1]);
            remDegStr = Utility.getInstance(mContext).getDayNo("" + remDeg);
            minStr = Utility.getInstance(mContext).getDayNo("" + min);
            secStr = Utility.getInstance(mContext).getDayNo("" + sec);
            latLng = remDegStr + "° " + lrashiList[index] + " " + minStr + "′" + " " + secStr + "′′";
            degStr = deg1Str + "." + deg2Str;
        }
        strArr = new String[]{latLng, degStr};

        return strArr;
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


        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear + 100);
        selCal1.set(Calendar.MONTH, selMonth);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).observe(this, obj -> {

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

