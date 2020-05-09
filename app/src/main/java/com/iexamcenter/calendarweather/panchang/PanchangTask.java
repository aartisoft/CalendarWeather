package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.SunMoonCalculator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class PanchangTask {

    public String[] tithi_arr, nakshatra_arr, rasi_kundali_arr, bara_arr, masa_arr, ritu_arr;
    Context mContext;


    private boolean patipadaKhaya = false;

    public HashMap<String, CoreDataHelper> panchangMapping(List<EphemerisEntity> ephemeris, String lang, String latStr, String lngStr, Context context) {


        mContext = context;


        HashMap<String, CoreDataHelper> hs1 = new HashMap<>();


        try {
            int rowSize = ephemeris.size();

            if (rowSize > 0){
                int lunarMonthAmantIndex = -1, lunarMonthPurnimantIndex = -1, lunarMonthType = -1;

                int solarDayVal = 0;
                boolean dayCntStart = false;
                int resetAfterDay = -1, adjustSolarMonth, nextAdjustSolarMonth = 0;
                int fullMoonLunarDay = 0;
                int newMoonLunarDay = 0;


                double lat = Double.parseDouble(latStr);
                double lng = Double.parseDouble(lngStr);

                String timeZoneStr = TimeZone.getDefault().getID();

                TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
                int istInMin = tz.getOffset(Calendar.getInstance().getTimeInMillis()) / (1000 * 60);
                int rowIndex = 0;


                do {
                    EphemerisEntity ephemerisObj = ephemeris.get(rowIndex);
                    String column2 = ephemerisObj.year;
                    String column3 = ephemerisObj.month;
                    String column4 = ephemerisObj.day;
                    String column5 = ephemerisObj.sun;
                    String column6 = ephemerisObj.moon;
                    String column7 = ephemerisObj.dmsun;
                    String column8 = ephemerisObj.dmmoon;
                   // String column9 = ephemerisObj.time;
                   // column9.split(":")

                   // Log.e("EphemerisData","EphemerisData"+column2+"-"+column3+"-"+column4+"::sun"+column5+"moon"+column6+"dmsun"+column7+"dmmoon"+column8);


                    int year = Integer.parseInt(column2);
                    int month = Integer.parseInt(column3);
                    int day = Integer.parseInt(column4);
                    Calendar todayCal = Calendar.getInstance();
                    todayCal.set(Calendar.YEAR, year);
                    todayCal.set(Calendar.MONTH, month);
                    todayCal.set(Calendar.DAY_OF_MONTH, day);
                    todayCal.set(Calendar.HOUR_OF_DAY, 0);
                    todayCal.set(Calendar.MINUTE, 0);
                    todayCal.set(Calendar.SECOND, 1);


                    double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;


                    int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);


                    String hashMapKey = year + "-" + month + "-" + day;

                    int sunriseMin;
                    int hr;
                    int min;

                    SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 14, 28, 52, obsLon, obsLat);
                    smc.calcSunAndMoon();

                    Calendar sunRiseCal = SunMoonCalculator.getSunRiseDate(smc.sunRise, year, month + 1, day);
                    Calendar sunSetCal = SunMoonCalculator.getDateAsDate(smc.sunSet);
                    long diffInMilli = 0;
                    if (sunRiseCal != null && sunSetCal != null) {
                        diffInMilli = sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis();
                    }


                    long dayTime = Math.abs(diffInMilli) / 1000;

                    long nightTime = (24 * 60 * 60) - dayTime;

                    long hinduMidNight = (sunSetCal.getTimeInMillis() / 1000) + (nightTime / 2);

                    Calendar midNightCal = Calendar.getInstance();
                    midNightCal.setTimeInMillis(hinduMidNight * 1000);


                    hr = sunRiseCal.get(Calendar.HOUR_OF_DAY);
                    min = sunRiseCal.get(Calendar.MINUTE);

                    sunriseMin = (hr * 60) + min;


                    CoreDataHelper coreDataObj = new CoreDataHelper(column5, column6, column7, column8, day, month, year, sunriseMin, istInMin,ephemerisObj);

                    coreDataObj.setSunRiseCal(sunRiseCal);
                    coreDataObj.setSunSetCal(sunSetCal);
                    coreDataObj.setMidNight(midNightCal);
                    coreDataObj.setDayOfWeek(dayOfWeek);

                    CoreDataHelper.Panchanga tithiObj = coreDataObj.getTithi();
                    CoreDataHelper.Panchanga sunSignObj = coreDataObj.getSunSign();


                    if (tithiObj.currVal == 1) {
                        newMoonLunarDay = 0;

                        Integer[] lunarMonthArr = getLunarMonthAmant(year, month, day);
                        lunarMonthAmantIndex = lunarMonthArr[1];
                        lunarMonthType = lunarMonthArr[0];
                    } else if (tithiObj.currVal == 30 && tithiObj.nextVal == 1 && tithiObj.nextToNextVal == 2) {
                        Integer[] lunarMonthArr = getLunarMonthAmant(year, month, day);
                        newMoonLunarDay = 0;
                        lunarMonthAmantIndex = lunarMonthArr[1];
                        lunarMonthType = lunarMonthArr[0];

                    }

                    if (tithiObj.currVal == 16) {
                        fullMoonLunarDay = 0;
                        lunarMonthPurnimantIndex = getLunarMonthPurnimant(year, month, day);

                    } else if (tithiObj.currVal == 15 && tithiObj.nextVal == 16 && tithiObj.nextToNextVal == 17) {
                        patipadaKhaya = true;

                    } else if (tithiObj.currVal == 17 && patipadaKhaya) {
                        patipadaKhaya = false;
                        fullMoonLunarDay = 0;
                        lunarMonthPurnimantIndex = getLunarMonthPurnimant(year, month, day);


                    }


                    newMoonLunarDay++;
                    fullMoonLunarDay++;

                    coreDataObj.setmLunarMonthType(lunarMonthType);
                    coreDataObj.setNewMoonLunarDay(newMoonLunarDay);
                    coreDataObj.setFullMoonLunarDay(fullMoonLunarDay);
                    coreDataObj.setLunarMonthAmantIndex(lunarMonthAmantIndex);
                    coreDataObj.setLunarMonthPurnimantIndex(lunarMonthPurnimantIndex);


                    adjustSolarMonth = sunSignObj.currVal;
                    long sunsignEnd = sunSignObj.currValEndTime.getTimeInMillis();

                    if (sunSignObj.nextVal != 0) {


                        nextAdjustSolarMonth = sunSignObj.nextVal;
                        dayCntStart = true;

                        if (lang.contains("bn")) {
                            if (sunsignEnd < midNightCal.getTimeInMillis()) {
                                resetAfterDay = 1;
                            } else {
                                resetAfterDay = 2;
                            }
                        } else if (lang.contains("or")) {

                            if (sunsignEnd < midNightCal.getTimeInMillis()) {

                                resetAfterDay = 0;

                            } else {
                                resetAfterDay = 1;
                            }
                        } else if (lang.contains("ta")) {

                            if (sunsignEnd < sunSetCal.getTimeInMillis()) {

                                resetAfterDay = 0;

                            } else {
                                resetAfterDay = 1;
                            }
                        } else {

                            if (sunsignEnd < sunSetCal.getTimeInMillis()) {

                                resetAfterDay = 0;

                            } else {
                                resetAfterDay = 1;
                            }
                        }

                    }

                    if (resetAfterDay == 0) {
                        solarDayVal = 1;
                        adjustSolarMonth = nextAdjustSolarMonth;

                    }

                    if (lang.contains("bn") && resetAfterDay == 1 && nextAdjustSolarMonth != 1) {
                        adjustSolarMonth = nextAdjustSolarMonth - 1;
                    } else if (lang.contains("bn") && resetAfterDay == 1 && nextAdjustSolarMonth == 1) {
                        adjustSolarMonth = nextAdjustSolarMonth;
                    }
                    resetAfterDay--;

                    coreDataObj.setSolarDayVal(solarDayVal);
                    coreDataObj.setAdjustSolarMonth(adjustSolarMonth);


                    if (dayCntStart) {
                        solarDayVal++;
                    }
                    hs1.put(hashMapKey, coreDataObj);
                    rowIndex++;
                } while (rowIndex < rowSize);


            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return hs1;

    }


    private int getLunarMonthPurnimant(int year, int month, int day) {

        try {
            int lunarMonthPurnimantIndex = 0;


            String purnimantMonthPurnimantStr = getLunarPurnimant(year);
            //  String purnimantMonthPurnimantStr = getLunarAmant(year);
            String[] purnimantMonthArr = purnimantMonthPurnimantStr.split("\\|");
            String purnimantMonth = purnimantMonthArr[month];

            // Log.e("amantMonthStr","amantMonthStr:"+year+":"+amantMonthStr+":"+amantMonth+":||"+month);
            if (purnimantMonth.contains("-")) {
                purnimantMonthArr = purnimantMonth.split("-");
                lunarMonthPurnimantIndex = Integer.parseInt(purnimantMonthArr[0]);
                if (day > 15)
                    lunarMonthPurnimantIndex = Integer.parseInt(purnimantMonthArr[1]);
            } else if (purnimantMonth.contains("*")) {
                purnimantMonth = purnimantMonth.replace("*", "");
                lunarMonthPurnimantIndex = Integer.parseInt(purnimantMonth);
            } else {
                lunarMonthPurnimantIndex = Integer.parseInt(purnimantMonth);
            }

            return lunarMonthPurnimantIndex;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;


    }

    private Integer[] getLunarMonthAmant(int year, int month, int day) {
        Integer[] monthArr = new Integer[2];
        int type;//0-normal, 1-adhika,2-kshaya
        try {
            int lunarMonthAmantIndex = 0;
            String amantMonthStr = getLunarAmant(year);
            String[] amantMonthArr = amantMonthStr.split("\\|");
            String amantMonth = amantMonthArr[month];

            if (amantMonth.contains("-")) {
                amantMonthArr = amantMonth.split("-");
                lunarMonthAmantIndex = Integer.parseInt(amantMonthArr[0]);
                if (day > 15)
                    lunarMonthAmantIndex = Integer.parseInt(amantMonthArr[1]);
                type = 0;
            } else if (amantMonth.contains("*")) {
                amantMonth = amantMonth.replace("*", "");
                lunarMonthAmantIndex = Integer.parseInt(amantMonth);
                type = 1;
            } else if (amantMonth.contains("@")) {
                amantMonth = amantMonth.replace("@", "");
                lunarMonthAmantIndex = Integer.parseInt(amantMonth);
                type = 2;
            } else {
                lunarMonthAmantIndex = Integer.parseInt(amantMonth);
                type = 0;
            }
            monthArr[0] = type;
            monthArr[1] = lunarMonthAmantIndex;
            return monthArr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthArr;
    }

    public String getLunarPurnimant(int year) {
        switch (year) {


           /* case 1947:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1948:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1949:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";

            case 1950:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1951:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1952:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1953:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1954:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1955:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1956:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1957:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1958:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1959:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";

            case 1960:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1961:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 1962:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
                 //doubt
            case 1963:
                return "9|10|11|0|1|2|3|4|5|6@|6|7|8";
            case 1964:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1965:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1966:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1967:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1968:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1969:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";*/
// set properly
            case 1969:
                return "9|10|11|0|1|2-2|3|4|5|6|7|8";
            case 1970:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1971:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1972:
                return "9-10|10|11-0|0|1|2|3|4|5|6|7|8";
            case 1973:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1974:
                return "9|10|11|0|1|2|3|4|4|5|6-7|8";
            case 1975:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1976:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1977:
                return "9|10|11|0|1|2|3-3|4|5|6|7|8";
            case 1978:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1979:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 1980:
                return "9|10|11|0|1-1|2|3|4|5|6|7|8";
            case 1981:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1982:
                return "9|10|11|0|1|2|3|4|5|5|6|7-8";
            //doubt
            case 1983:
                return "10|10|11|0|1|2|3|4|5|6|7|8";
            case 1984:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1985:
                return "9|10|11|0|1|2|3|3-4|5|6|7|8";
            case 1986:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1987:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1988:
                return "9|10|11|0|1|1-2|3|4|5|6|7|8";
            case 1989:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 1990:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1991:
                return "9-10|10|11-0|0|1|2|3|4|5|6|7|8";
            case 1992:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1993:
                return "9|10|11|0|1|2|3|4|4|5-6|7|8";
            case 1994:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1995:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1996:
                return "9|10|11|0|1|2|2-3|4|5|6|7|8";
            case 1997:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1998:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1999:
                return "9|10|11|0|1-1|2|3|4|5|6|7|8";


            case 2000:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2001:
                return "9|10|11|0|1|2|3|4|5|5|6|7-8";
            case 2002:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2003:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2004:
                return "9|10|11|0|1|2|3|3-4|5|6|7|8";
            case 2005:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2006:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2007:
                return "9|10|11|0|1|1|2-3|4|5|6|7|8";
            case 2008:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2009:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2010:
                return "9-10|10|11-0|0|1|2|3|4|5|6|7|8";
            case 2011:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2012:
                return "9|10|11|0|1|2|3|4|4|5-6|7|8";
            case 2013:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2014:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2015:
                return "9|10|11|0|1|2|2|3-4|5|6|7|8";
            case 2016:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2017:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 2018:
                return "9|10|11|0|1-1|2|3|4|5|6|7|8";

            case 2019:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 2020:
                return "9|10|11|0|1|2|3|4|5|5|6|7-8";

            case 2021:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2022:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2023:
                return "9|10|11|0|1|2|3|3|4|5|6|7|8";

            case 2024:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2025:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2026:
                return "9|10|11|0|1|1|2-3|4|5|6|7|8";
            case 2027:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2028:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2029:
                return "9-10|10|11-11|0|1|2|3|4|5|6|7|8";

            case 2030:
                return "9|10|11|0|1|2-3|4|5|6|7|8";
            case 2031:
                return "9|10|11|0|1|2|3|4|4|5-6|7|8";
            case 2032:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2033:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2034:
                return "9|10|11|0|1|2|2|3-4|5|6|7|8";
            case 2035:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 2036:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2037:
                return "9|10|11|0-1|1|2|3|4|5|6|7|8";
            case 2038:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2039:
                return "9|10|11|0|1|2|3|4|5|5|6|7-8";
            case 2040:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 2041:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2042:
                return "9|10|11|0|1|2|3|3|4-5|6|7|8";
            case 2043:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2044:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2045:
                return "9|10|11|0|1-1|2|3|4|5|6|7|8";
            case 2046:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2047:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2048:
                return "9-10|10|11-11|0|1|2|3|4|5|6|7|8";
            case 2049:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2050:
                return "9|10|11|0|1|2|3|4|4|5-6|7|8";
            case 2051:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
                default:
                    return "9|10|11|0|1|2|3|4|5|6|7|8";

        }

       // return "";
    }

    public String getLunarAmant(int year) {
        switch (year) {
            case 2018:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 2019:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
            case 2020:
                return "9|10|11|0|1|2|3|4|5*|5|6|7";

           /* case 1947:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1948:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1949:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";

            case 1950:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1951:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1952:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1953:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1954:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1955:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1956:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1957:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1958:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1959:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";

            case 1960:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1961:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 1962:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
                 //doubt
            case 1963:
                return "9|10|11|0|1|2|3|4|5|6*|6|7|8";
            case 1964:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1965:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1966:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1967:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1968:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1969:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";*/

            case 1969:
                return "9|10|11|0|1|2*|2|3|4|5|6|7";
            case 1970:
                return "8|9|10|11|0|1|2|3|4|5-6|7|8";
            case 1971:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1972:
                return "9|10|11|0*|0|1|2|3|4|5|6|7";
            case 1973:
                return "8|9|10|11|0|1|2-3|4|5|6|7|8";
            case 1974:
                return "9|10|11|0|1|2|3|4*|4|5|6|7";
            case 1975:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 1976:
                return "8|9|10-11|0|1|2|3|4|5|6|7|8";
            case 1977:
                return "9|10|11|0|1|2|3*|3|4|5|6|7";
            case 1978:
                return "8|9|10|11|0|1|2|3|4|5|6|7-8";
            case 1979:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 1980:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 1981:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
            case 1982:
                return "9|10|11|0|1|2|3|4|5*|5|6|7";
            //doubt
            case 1983:
                return "8|10*|10|11|0|1|2|3|4|5|6|7";
            case 1984:
                return "8|9|10|11|0-1|2|3|4|5|6|7|8";
            case 1985:
                return "9|10|11|0|1|2|3*|3|4|5|6|7";
            case 1986:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 1987:
                return "8-9|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1988:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 1989:
                return "8|9|10|11|0|1|2|3|4-5|6|7|8";
            //correct
            case 1990:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 1991:
                return "9|10|11|0*|0|1|2|3|4|5|6|7";
            case 1992:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1993:
                return "9|10|11|0|1|2|3|4*|4|5|6|7";
            case 1994:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 1995:
                return "8-9|9|10|11-0|1|2|3|4|5|6|7|8";
            case 1996:
                return "9|10|11|0|1|2*|2|3|4|5|6|7";
            case 1997:
                return "8|9|10|11|0|1|2|3|4|5|6|7|8";
            case 1998:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 1999:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";


            case 2000:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
            case 2001:
                return "9|10|11|0|1|2|3|4|5*|5|6|7";
            case 2002:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2003:
                return "8|9|10|11|0|1-2|3|4|5|6|7|8";
            case 2004:
                return "9|10|11|0|1|2|3*|3|4|5|6|7";
            case 2005:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2006:
                return "8-9|10|11|0|1|2|3|4|5|6|7|8";
            case 2007:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 2008:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
            case 2009:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2010:
                return "9|10|11|0*|0|1|2|3|4|5|6|7";
            case 2011:
                return "8|9|10|11|0|1|2-3|4|5|6|7|8";
            case 2012:
                return "9|10|11|0|1|2|3|4*|4|5|6|7";
            case 2013:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2014:
                return "8-9|9|10-11|0|1|2|3|4|5|6|7|8";
            case 2015:
                return "9|10|11|0|1|2*|2|3|4|5|6|7";
            case 2016:
                return "8|9|10|11|0|1|2|3|4|5-6|7|8";
            case 2017:
                return "9|10|11|0|1|2|3|4|5|6|7|8";

            case 2021:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2022:
                return "8|9|10|11|0-1|2|3|4|5|6|7|8";
            case 2023:
                return "9|10|11|0|1|2|3*|3|4|5|6|7";
            case 2024:
                return "8|9|10|11|0|1|2|3|4|5|6|7-8";
            case 2025:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2026:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 2027:
                return "8|9|10|11|0|1|2|3|4|5-6|7|8";
            case 2028:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2029:
                return "9|10|11*|11|0|1|2|3|4|5|6|7";
            case 2030:
                return "8|9|10|11|0|1|2-3|4|5|6|7|8";
            case 2031:
                return "9|10|11|0|1|2|3|4*|4|5|6|7";
            case 2032:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2033:
                return "8-9|10|11|0|1|2|3|4|5|6|7|8";
            case 2034:
                return "9|10|11|0|1|2*|2|3|4|5|6|7";
            case 2035:
                return "8|9|10|11|0|1|2|3|4|5-6|7|8";

            case 2036:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2037:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 2038:
                return "8|9|10|11|0|1|2|3-4|5|6|7|8";
            case 2039:
                return "9|10|11|0|1|2|3|4|5*|5|6|7";
            case 2040:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2041:
                return "8|9|10|11|0-1|2|3|4|5|6|7|8";
            case 2042:
                return "9|10|11|0|1|2|3*|3|4|5|6|7";
            case 2043:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            case 2044:
                return "8-9|10|11|0|1|2|3|4|5|6|7|8";
            case 2045:
                return "9|10|11|0|1*|1|2|3|4|5|6|7";
            case 2046:
                return "8|9|10|11|0|1|2|3|4|5-6|7|8";
            case 2047:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2048:
                return "9|10|11*|11|0|1|2|3|4|5|6|7";
            case 2049:
                return "8|9|10|11|0|1|2-3|4|5|6|7|8";
            case 2050:
                return "9|10|11|0|1|2|3|4*|4|5|6|7";
            case 2051:
                return "8|9|10|11|0|1|2|3|4|5|6|7";
            default:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
        }

       // return "";
    }
}
