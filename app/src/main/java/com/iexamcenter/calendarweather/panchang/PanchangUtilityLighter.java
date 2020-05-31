package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Resources;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.SunMoonCalculator;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PanchangUtilityLighter {

    private Calendar todayCal;
    private String[] le_arr_tithi;
    private String[] le_arr_rasi_kundali;
    private String[] le_arr_month;
    Context mContext;

    private Resources mRes;
    private int mCalType;

    private String mLang;
    String latStr, lngStr;
    String[] le_arr_ritu, le_month_short_arr, le_arr_bara, le_arr_karana, le_arr_masa, le_arr_joga, le_arr_paksha;
    double obsLon, obsLat;
    int mType;

    public PanchangUtilityLighter(int type, Context context, String lang, int calType, String latStr, String lngStr) {
        mContext = context;
        mRes = mContext.getResources();
        mLang = lang;
        mCalType = calType;
        this.latStr = latStr;
        this.lngStr = lngStr;
        mType = type;
        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);

        obsLon = lng * SunMoonCalculator.DEG_TO_RAD;
        obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
        le_month_short_arr = mRes.getStringArray(R.array.e_arr_month_short);
        if (mType == 0) {
            le_arr_tithi = mRes.getStringArray(R.array.l_arr_tithi);
            le_arr_karana = mRes.getStringArray(R.array.l_arr_karana);
            le_arr_rasi_kundali = mRes.getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_joga = mRes.getStringArray(R.array.l_arr_joga);
            le_arr_paksha = mRes.getStringArray(R.array.l_arr_paksha);
            le_arr_bara = mRes.getStringArray(R.array.l_arr_bara);
            le_arr_month = mRes.getStringArray(R.array.l_arr_month);
            le_arr_masa = mRes.getStringArray(R.array.l_arr_masa);
            le_arr_ritu = mRes.getStringArray(R.array.l_arr_ritu);
        } else {
            le_arr_tithi = mRes.getStringArray(R.array.e_arr_tithi);
            le_arr_karana = mRes.getStringArray(R.array.e_arr_karana);
            le_arr_rasi_kundali = mRes.getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_joga = mRes.getStringArray(R.array.e_arr_joga);
            le_arr_paksha = mRes.getStringArray(R.array.e_arr_paksha);
            le_arr_bara = mRes.getStringArray(R.array.e_arr_bara);
            le_arr_masa = mRes.getStringArray(R.array.e_arr_masa);
            le_arr_month = mRes.getStringArray(R.array.e_arr_month);
            le_arr_ritu = mRes.getStringArray(R.array.e_arr_ritu);
        }


    }

    public MyPanchang getMyPunchang(CoreDataHelper coreDataObj) {
        try {

            MyPanchang myPanchangObj = new MyPanchang();
            int year = coreDataObj.getmYear(), month = coreDataObj.getmMonth(), day = coreDataObj.getmDay();
            todayCal = Calendar.getInstance();
            todayCal.set(Calendar.YEAR, year);
            todayCal.set(Calendar.MONTH, month);
            todayCal.set(Calendar.DAY_OF_MONTH, day);
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 1);
            int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);
            myPanchangObj.le_day = "" + day;
            myPanchangObj.le_dayOfWeek = dayOfWeek;
            myPanchangObj.le_month = le_arr_month[month];
            myPanchangObj.le_month_short = le_month_short_arr[month];
            myPanchangObj.le_year = "" + year;
            myPanchangObj.le_bara = le_arr_bara[dayOfWeek - 1];
            // myPanchangObj.le_bara = le_arr_bara[dayOfWeek - 1];
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 1, 0, 0, obsLon, obsLat);
            smc.calcSunAndMoon();

            Calendar sunRiseCal = SunMoonCalculator.getSunRiseDate(smc.sunRise, year, month + 1, day);
            Calendar sunSetCal = SunMoonCalculator.getDateAsDate(smc.sunSet);


            long dayTime = Math.abs(sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis()) / 1000;
            long nightTime = (24 * 60 * 60) - dayTime;
            long hinduMidNight = (sunSetCal.getTimeInMillis() / 1000) + (nightTime / 2);
            Calendar midNightCal = Calendar.getInstance();
            midNightCal.setTimeInMillis(hinduMidNight * 1000);
            CoreDataHelper.Panchanga tithiObj = coreDataObj.getTithi();
            CoreDataHelper.Panchanga sunSignObj = coreDataObj.getSunSign();
            int paksha = coreDataObj.getPaksha();
            myPanchangObj.le_paksha = le_arr_paksha[paksha];
            myPanchangObj.le_pakshaIndex = paksha;


            String currTithi = le_arr_tithi[tithiObj.currVal - 1];
            // String ecurrTithi = le_arr_tithi[tithiObj.currVal - 1];
            myPanchangObj.le_currTithiIndex = tithiObj.currVal - 1;
            myPanchangObj.le_currTithi = le_arr_tithi[tithiObj.currVal - 1];

            String nextTithi = "", nextToNextTithi = "";
            String currTithiDate = "", nextTithiDate = "";
            if (tithiObj.nextVal != 0) {
                myPanchangObj.le_nextTithiIndex = tithiObj.nextVal - 1;
                currTithiDate = getFormattedDate(tithiObj.currValEndTime);
                // ecurrTithiDate = geteFormattedDate(tithiObj.currValEndTime);
                nextTithi = le_arr_tithi[tithiObj.nextVal - 1];
                // enextTithi = le_arr_tithi[tithiObj.nextVal - 1];
            }


            if (tithiObj.nextToNextVal != 0) {

                myPanchangObj.le_nextToNextTithiIndex = tithiObj.nextToNextVal - 1;
                nextTithiDate = getFormattedDate(tithiObj.le_nextValEndTime);
                // enextTithiDate = geteFormattedDate(tithiObj.le_nextValEndTime);
                nextToNextTithi = le_arr_tithi[tithiObj.nextToNextVal - 1];
                // enextToNextTithi = le_arr_tithi[tithiObj.nextToNextVal - 1];
            }
            if (((sunRiseCal.getTimeInMillis() - 24 * 60 * 60 * 1000) > tithiObj.currValStartTime.getTimeInMillis()) && (tithiObj.currValEndTime.getTimeInMillis() < sunSetCal.getTimeInMillis())) {
                myPanchangObj.le_tithiMala = true;
            }
            if (tithiObj.currValStartTime.getTimeInMillis() < sunRiseCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() > (sunRiseCal.getTimeInMillis() + 17 * 60 * 60 * 1000)) {
                myPanchangObj.le_currNightCover = true;
            }
            if (tithiObj.le_nextValEndTime != null && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunRiseCal.getTimeInMillis() + (24 * 60 * 60 * 1000))) {
                myPanchangObj.le_tithiKhaya = true;
            }
            if (tithiObj.currValEndTime.getTimeInMillis() < sunSetCal.getTimeInMillis() && tithiObj.le_nextValEndTime != null && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunSetCal.getTimeInMillis() + 6 * 60 * 60 * 1000)) {
                myPanchangObj.le_nextNightCover = true;
            }
            myPanchangObj.le_tithi[0] = setMySubPanchang(currTithi, currTithiDate);
            myPanchangObj.le_tithi[1] = setMySubPanchang(nextTithi, nextTithiDate);
            myPanchangObj.le_tithi[2] = setMySubPanchang(nextToNextTithi, "");


            int lunarMonthPurnimantIndex = coreDataObj.getLunarMonthPurnimantIndex();
            int lunarMonthAmantIndex = coreDataObj.getLunarMonthAmantIndex();
            int newMoonLunarDay = coreDataObj.getNewMoonLunarDay();
            int fullMoonLunarDay = coreDataObj.getFullMoonLunarDay();
            int lunarMonthType = coreDataObj.getmLunarMonthType();


            if (mLang.contains("te") || mLang.contains("kn") || mLang.contains("mr") || mLang.contains("gu")) {


                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarMonthType = lunarMonthType;
                //   myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                //  myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_pLunarMonthIndex = lunarMonthAmantIndex;
                //  myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_aLunarMonthIndex = lunarMonthAmantIndex;
                // myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthAmantIndex];


            } else if (mLang.contains("or") || mLang.contains("hi") || mLang.contains("en")) {
                // myPanchangObj.le_lunarDayAmant = "" + newMoonLunarDay;
                myPanchangObj.le_lunarMonthType = lunarMonthType;
                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                //  myPanchangObj.le_lunarDayPurimant = "" + fullMoonLunarDay;
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + fullMoonLunarDay);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthPurnimantIndex];
                myPanchangObj.le_pLunarMonthIndex = lunarMonthPurnimantIndex;
                //myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_aLunarMonthIndex = lunarMonthAmantIndex;
                //  myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthPurnimantIndex];

            }

            String sakaddha = "" + getSakaddha(year, month, day, myPanchangObj.le_pLunarMonthIndex, myPanchangObj.le_currTithiIndex, myPanchangObj.le_nextTithiIndex, myPanchangObj.le_tithiKhaya, myPanchangObj.le_pakshaIndex);
            String samvata = "" + getSamvata(year, month, day, myPanchangObj.le_pLunarMonthIndex, myPanchangObj.le_currTithiIndex, myPanchangObj.le_nextTithiIndex, myPanchangObj.le_tithiKhaya, myPanchangObj.le_pakshaIndex);


            myPanchangObj.le_sakaddha = Utility.getInstance(mContext).getDayNo(sakaddha);
            // myPanchangObj.le_sakaddha = sakaddha;
            myPanchangObj.le_samvata = Utility.getInstance(mContext).getDayNo(samvata);
            // myPanchangObj.le_samvata = samvata;

            String sanSala = "" + getSanSal(year, month, day, myPanchangObj.le_pLunarMonthIndex, myPanchangObj.le_currTithiIndex, myPanchangObj.le_nextTithiIndex, myPanchangObj.le_tithiKhaya);
            myPanchangObj.le_sanSal = Utility.getInstance(mContext).getDayNo(sanSala);
            // myPanchangObj.le_sanSal = sanSala;

            String sanSalaAnka = "" + getSanSalAnka(year, month, day, myPanchangObj.le_pLunarMonthIndex, myPanchangObj.le_currTithiIndex, myPanchangObj.le_nextTithiIndex, myPanchangObj.le_tithiKhaya);

            myPanchangObj.le_sanSalAnka = Utility.getInstance(mContext).getDayNo(sanSalaAnka);
            // myPanchangObj.le_sanSalAnka = sanSalaAnka;


            int adjustSolarMonth = coreDataObj.getAdjustSolarMonth();
            int solarDayVal = coreDataObj.getSolarDayVal();

            long sunsignEnd = sunSignObj.currValEndTime.getTimeInMillis();
            if (sunsignEnd > midNightCal.getTimeInMillis() && (sunsignEnd < midNightCal.getTimeInMillis() + (24 * 60 * 60 * 1000L))) {

                myPanchangObj.le_masant = true;
            }

            //  myPanchangObj.le_solarDay = "" + solarDayVal;
            myPanchangObj.le_solarDay = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
            myPanchangObj.le_solarDayIndex = solarDayVal;

            myPanchangObj.le_solarMonth = le_arr_rasi_kundali[adjustSolarMonth - 1];
            //  myPanchangObj.le_solarMonth = le_arr_rasi_kundali[adjustSolarMonth - 1];
            myPanchangObj.le_solarMonthIndex = adjustSolarMonth - 1;

            myPanchangObj.le_monthIndex = month;


            if (mLang.contains("bn") || mLang.contains("ta") || mLang.contains("pa") || mLang.contains("ml")) {

                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                //   myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                // myPanchangObj.le_lunarDayPurimant = "" + solarDayVal;
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.le_pLunarMonthIndex = adjustSolarMonth - 1;
                // myPanchangObj.le_lunarMonthAmant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.le_aLunarMonthIndex = adjustSolarMonth - 1;
                // myPanchangObj.le_lunarMonthPurimant = le_arr_masa[adjustSolarMonth - 1];


            }

            return myPanchangObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getSanSalAnka(int year, int month, int day, int pLunarMonthIndex, int currTithiIndex, int nextTithiIndex, boolean tithiKhaya) {
        try {

            if (year < 1970) {
                return 0;
            }
            int anka = 0;
            for (int i = 1970; i <= year; i++) {
                anka++;
                if (((("" + i).endsWith("0")) || (("" + i).endsWith("6")) || (("" + i).endsWith("01")))) {
                    anka++;
                }

            }

            int sanSal;
            // odia month Val should be new year month and fall after apriil/may or more

            if (month > 6 && ((pLunarMonthIndex >= 4 && currTithiIndex >= 11) || (pLunarMonthIndex >= 5))) {
                sanSal = anka + 1;

            } else {
                sanSal = anka;


            }

            return sanSal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }


    private String geteFormattedDate(Calendar cal) {

        try {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            Date date = cal.getTime();
            DateFormat dateFormat;
            int currDayNo = todayCal.get(Calendar.DAY_OF_MONTH);
            String dt;


            if (mCalType == 0) {
                if (currDayNo != calDayNo) {

                    dateFormat = new SimpleDateFormat("hh:mm a  dd/MMM yyyy", Locale.US);
                    dt = dateFormat.format(date);


                } else {
                    dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    dt = dateFormat.format(date);
                }
            } else if (mCalType == 1) {
                if (currDayNo != calDayNo) {

                    dateFormat = new SimpleDateFormat("HH:mm dd/MMM yyyy", Locale.US);
                    dt = dateFormat.format(date);

                } else {
                    dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    dt = dateFormat.format(date);
                }
            } else {
                if (currDayNo != calDayNo) {
                    dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    dt = dateFormat.format(date);
                    dt = dt + "(+)";

                } else {
                    dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    dt = dateFormat.format(date);
                }
            }

            return dt;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private String getFormattedDate(Calendar cal) {

        if ((mLang.contains("or") || mLang.contains("hi")  || mLang.contains("mr")   || mLang.contains("gu"))) {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            int calHour = cal.get(Calendar.HOUR_OF_DAY);
            int calMin = cal.get(Calendar.MINUTE);
            int calMonth = cal.get(Calendar.MONTH);
            String calDayNoStr = Utility.getInstance(mContext).getDayNo("" + calDayNo);
            String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
            String prefixTime = "";
            if ((calHour > 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
                prefixTime = mRes.getString(R.string.l_time_night);
            }
            if (calHour >= 4 && calHour < 9) {
                prefixTime = mRes.getString(R.string.l_time_prattha);
            } else if (calHour >= 9 && calHour < 16) {
                prefixTime = mRes.getString(R.string.l_time_diba);
            } else if (calHour >= 16 && calHour < 19) {
                prefixTime = mRes.getString(R.string.l_time_sandhya);
            }
            if (calHour > 12) {
                calHour = calHour - 12;
            }
            String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

            String ldate;

            int currDayNo = todayCal.get(Calendar.DAY_OF_MONTH);
            if (mCalType == 0) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + le_arr_month[calMonth];
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);

                }
            } else if (mCalType == 1) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + le_arr_month[calMonth];

                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);

                }
            } else {
                if (currDayNo != calDayNo) {

                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + "(+)";
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);

                }
            }
            return ldate;
        } else {
            return geteFormattedDate(cal);
        }
    }

    private int getSamvata(int year, int month, int day, int pLunarMonthIndex, int currTithiIndex, int nextTithiIndex, boolean tithiKhaya, int pakshaIndex) {
        try {
            // 2019=return "2075|17|9|1941";
            int yearDiff = year - 2019;

            int sanSal;
            if (month > 1 && ((pLunarMonthIndex >= 11 && pakshaIndex == 0 && currTithiIndex >= 0) || pLunarMonthIndex == 0 || (pLunarMonthIndex >= 1 && month > 3))) {
                sanSal = 2075 + yearDiff + 1;

            } else {
                sanSal = 2075 + yearDiff;

            }
            return sanSal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getSakaddha(int year, int month, int day, int pLunarMonthIndex, int currTithiIndex, int nextTithiIndex, boolean tithiKhaya, int pakshaIndex) {
        try {
            // 2019=return "1940|17|9|1941";
            int yearDiff = year - 2019;

            int sanSal;
            // odia month Val should be new year month and fall after apriil/may or more
            //month>2 atlesat more than march
            if (month > 1 && ((pLunarMonthIndex >= 11) || pLunarMonthIndex == 0 || (pLunarMonthIndex >= 0 && month > 2))) {
                sanSal = 1940 + yearDiff + 1;

            } else {
                sanSal = 1940 + yearDiff;

            }
            return sanSal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getSanSal(int year, int month, int day, int pLunarMonthIndex, int currTithiIndex, int nextTithiIndex, boolean tithiKhaya) {
        try {

            int yearDiff = year - 2019;

            int sanSal;
            // odia month Val should be new year month and fall after apriil/may or more

            if (month > 6 && ((pLunarMonthIndex >= 4 && currTithiIndex >= 11) || (pLunarMonthIndex >= 5))) {
                sanSal = 1426 + yearDiff + 1;

            } else {
                sanSal = 1426 + yearDiff;

            }
            return sanSal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }


    public class MyPanchang {
        boolean le_masant = false, le_tithiMala = false, le_tithiKhaya = false, le_nextNightCover = false, le_currNightCover = false;
        public int le_lunarMonthType, le_monthIndex, le_solarDayIndex, le_currTithiIndex = -1, le_dayOfWeek, le_pLunarMonthIndex, le_aLunarMonthIndex, le_solarMonthIndex, le_nextTithiIndex = -1, le_nextToNextTithiIndex = -1, le_pakshaIndex;
        public String le_sanSalAnka, le_samvata, le_currTithi, le_sanSal, le_sakaddha, le_paksha, le_bara, le_day, le_month, le_year;
        public MySubPanchang[] le_tithi = new MySubPanchang[3];
        public String le_lunarDayAmant, le_lunarMonthAmant, le_lunarDayPurimant, le_lunarMonthPurimant, le_solarDay, le_solarMonth;
        public String le_month_short;
        public String le_ritu;
    }


    public MySubPanchang setMySubPanchang(String name, String time) {
        MySubPanchang obj = new MySubPanchang();
        obj.name = name;
        obj.time = time;
        return obj;
    }

    public class MyBela {
        public String startTime, endTime;
    }


    public class MySubPanchang {
        public String name, time;
    }

}
