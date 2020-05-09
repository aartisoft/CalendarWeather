package com.iexamcenter.calendarweather.utility;

import android.content.Context;
import android.util.Log;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Sasikanta_Sahoo on 11/28/2017.
 * Helper
 */

public class HelperGrahaKuta {
    private static HelperGrahaKuta instance = null;

    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise;
    ArrayList<houseinfo> houseinfos;
    String latitude, longitude;
    HashMap<Integer, houseinfo> houseinfosMap;
    Context mContext;
    int selYear = 2020, selMonth = 1, selDate = 1, selHour = 6, selMin = 30;

    private HelperGrahaKuta(Context context) {
        mContext = context;
    }

    public static HelperGrahaKuta getInstance(Context mContext) {
        if (instance == null) {
            instance = new HelperGrahaKuta(mContext);
        }
        return instance;
    }

    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    ArrayList<PlanetData> planetDataList;

    public HashMap<Integer, houseinfo> getGhahakuta(Calendar selCal, CoreDataHelper myCoreData, EphemerisEntity planetInfo, String latitude,int type) {
        lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
        lrashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
        planetDataList = new ArrayList<>();
        selYear = selCal.get(Calendar.YEAR);
        selMonth = selCal.get(Calendar.MONTH);
        selDate = selCal.get(Calendar.DATE);
        selHour = selCal.get(Calendar.HOUR_OF_DAY);
        selMin = selCal.get(Calendar.MINUTE);

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

        Calendar calToday = Calendar.getInstance();
        int todayYear = calToday.get(Calendar.YEAR);
        int todayMonth = calToday.get(Calendar.MONTH);
        int todayDay = calToday.get(Calendar.DAY_OF_MONTH);
        if (!(todayYear == selYear && todayMonth == selMonth && todayDay == selDate)) {
            selHour = myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY);
            selMin = myCoreData.getSunRise().get(Calendar.MINUTE);
        }
        if(type==0){
            //during sunrise
            selHour = myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY);
            selMin = myCoreData.getSunRise().get(Calendar.MINUTE);
        }else{
            // just before 12
            selHour = 23;
            selMin = 59;
        }

        setPlanetInfo(planetInfo);

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
            ArrayList<Integer> planetStr = new ArrayList<>();

            for (int i = 0; i < planetInHouse.size(); i++) {
                if (planetInHouse.get(i) == house) {
                    planetStr.add(planetHM.get(i));
                }

            }
            houseinfosMap.put(house, obj);
            // if(planetStr.size()>=5)
            obj.planetList = planetStr;

            houseinfos.add(obj);
        }


        return houseinfosMap;
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

        cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, selYear);
        cal1.set(Calendar.MONTH, selMonth);
        cal1.set(Calendar.DAY_OF_MONTH, selDate);
        cal1.set(Calendar.HOUR_OF_DAY, 5);
        cal1.set(Calendar.MINUTE, 30);


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

    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }

    public static class houseinfo {
        public int houseno, rashi;
        public ArrayList<Integer> planetList;
    }
}
