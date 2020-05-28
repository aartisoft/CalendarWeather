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


public class PanchangUtilityWidget {

    private Calendar todayCal;
    private String[] le_arr_tithi;
    private String[] le_arr_rasi_kundali;
    private String[] le_arr_month;
    Context mContext;

    private Resources mRes;
    private int mCalType;

    private String mLang;
    private String le_shraddha;
    private String le_shraddha_parban;
    private String le_shraddha_eko;
    private String le_shraddha_ra;
    private String[] le_arr_nakshatra;
    private String prevShraddha = "";
    String latStr, lngStr;
    String[] le_arr_ritu, le_arr_month_short, le_arr_bara, le_arr_karana, le_arr_masa, le_arr_joga, le_arr_paksha;
    double obsLon, obsLat;
    int mType;
    public PanchangUtilityWidget(int type,Context context, String lang, int calType, String latStr, String lngStr) {
        mContext = context;
        mRes = mContext.getResources();
        mLang = lang;
        mCalType = calType;
        this.latStr = latStr;
        this.lngStr = lngStr;
        mType =type;
        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);

        obsLon = lng * SunMoonCalculator.DEG_TO_RAD;
        obsLat = lat * SunMoonCalculator.DEG_TO_RAD;

        le_arr_month_short = mRes.getStringArray(R.array.e_arr_month_short);
        if (mType == 0) {
            le_shraddha = mRes.getString(R.string.l_shraddha);
            le_shraddha_parban = mRes.getString(R.string.l_shraddha_parban);
            le_shraddha_eko = mRes.getString(R.string.l_shraddha_eko);
            le_shraddha_ra = mRes.getString(R.string.l_shraddha_ra);
            le_arr_tithi = mRes.getStringArray(R.array.l_arr_tithi);
            le_arr_karana = mRes.getStringArray(R.array.l_arr_karana);
            le_arr_nakshatra = mRes.getStringArray(R.array.l_arr_nakshatra);
            le_arr_rasi_kundali = mRes.getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_joga = mRes.getStringArray(R.array.l_arr_joga);
            le_arr_paksha = mRes.getStringArray(R.array.l_arr_paksha);
            le_arr_bara = mRes.getStringArray(R.array.l_arr_bara);
            le_arr_month = mRes.getStringArray(R.array.l_arr_month);
            le_arr_masa = mRes.getStringArray(R.array.l_arr_masa);
            le_arr_ritu = mRes.getStringArray(R.array.l_arr_ritu);
        }else{
            le_shraddha = mRes.getString(R.string.e_shraddha);
            le_shraddha_parban = mRes.getString(R.string.e_shraddha_parban);
            le_shraddha_eko = mRes.getString(R.string.e_shraddha_eko);
            le_shraddha_ra = mRes.getString(R.string.e_shraddha_ra);
            le_arr_tithi = mRes.getStringArray(R.array.e_arr_tithi);
            le_arr_karana = mRes.getStringArray(R.array.e_arr_karana);
            le_arr_nakshatra = mRes.getStringArray(R.array.e_arr_nakshatra);
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
            myPanchangObj.day = myPanchangObj.eday = "" + day;
            myPanchangObj.dayOfWeek = dayOfWeek;
            myPanchangObj.month = le_arr_month[month];
            myPanchangObj.emonth = le_arr_month_short[month];
            myPanchangObj.year = myPanchangObj.eyear = "" + year;


            myPanchangObj.bara = le_arr_bara[dayOfWeek - 1];
            myPanchangObj.ebara = le_arr_bara[dayOfWeek - 1];

            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 1, 0, 0, obsLon, obsLat);
            smc.calcSunAndMoon();

            Calendar sunRiseCal = SunMoonCalculator.getSunRiseDate(smc.sunRise, year, month + 1, day);
            Calendar sunSetCal = SunMoonCalculator.getDateAsDate(smc.sunSet);


            Calendar sunNoonCal = SunMoonCalculator.getDateAsDate(smc.sunTransit);


            long dayTime = Math.abs(sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis()) / 1000;

            long nightTime = (24 * 60 * 60) - dayTime;

            long hinduMidNight = (sunSetCal.getTimeInMillis() / 1000) + (nightTime / 2);

            Calendar midNightCal = Calendar.getInstance();
            midNightCal.setTimeInMillis(hinduMidNight * 1000);


            myPanchangObj.sunRise = getFormattedDate(sunRiseCal);
            myPanchangObj.sunSet = getFormattedDate(sunSetCal);


            myPanchangObj.esunRise = geteFormattedDate(sunRiseCal);
            myPanchangObj.esunSet = geteFormattedDate(sunSetCal);


            CoreDataHelper.Panchanga tithiObj = coreDataObj.getTithi();
            CoreDataHelper.Panchanga sunSignObj = coreDataObj.getSunSign();
            CoreDataHelper.Panchanga nakshetraObj = coreDataObj.getNakshetra();


            CoreDataHelper.Panchanga moonSignObj = coreDataObj.getMoonSign();

            int paksha = coreDataObj.getPaksha();
            myPanchangObj.paksha = le_arr_paksha[paksha];
            myPanchangObj.auspKey = getAuspKeyDate(sunRiseCal);

            myPanchangObj.epaksha = le_arr_paksha[paksha];
            myPanchangObj.pakshaIndex = paksha;


            String currTithi = le_arr_tithi[tithiObj.currVal - 1];
            String ecurrTithi = le_arr_tithi[tithiObj.currVal - 1];
            myPanchangObj.currTithiIndex = tithiObj.currVal - 1;
            myPanchangObj.currTithi = le_arr_tithi[tithiObj.currVal - 1];
            myPanchangObj.ecurrTithi = le_arr_tithi[tithiObj.currVal - 1];

            String nextTithi = "", enextTithi = "", nextToNextTithi = "", enextToNextTithi = "";
            String currTithiDate = "", ecurrTithiDate = "", nextTithiDate = "", enextTithiDate = "";
            if (tithiObj.nextVal != 0) {
                myPanchangObj.nextTithiIndex = tithiObj.nextVal - 1;
                currTithiDate = getFormattedDate(tithiObj.currValEndTime);
                ecurrTithiDate = geteFormattedDate(tithiObj.currValEndTime);
                nextTithi = le_arr_tithi[tithiObj.nextVal - 1];
                enextTithi = le_arr_tithi[tithiObj.nextVal - 1];
            }


            if (tithiObj.nextToNextVal != 0) {

                myPanchangObj.nextToNextTithiIndex = tithiObj.nextToNextVal - 1;
                nextTithiDate = getFormattedDate(tithiObj.le_nextValEndTime);
                enextTithiDate = geteFormattedDate(tithiObj.le_nextValEndTime);
                nextToNextTithi = le_arr_tithi[tithiObj.nextToNextVal - 1];
                enextToNextTithi = le_arr_tithi[tithiObj.nextToNextVal - 1];
            }
            if (((sunRiseCal.getTimeInMillis() - 24 * 60 * 60 * 1000) > tithiObj.currValStartTime.getTimeInMillis()) && (tithiObj.currValEndTime.getTimeInMillis() < sunSetCal.getTimeInMillis())) {
                myPanchangObj.tithiMala = true;
            }
            if (tithiObj.currValStartTime.getTimeInMillis() < sunRiseCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() > (sunRiseCal.getTimeInMillis() + 17 * 60 * 60 * 1000)) {
                myPanchangObj.currNightCover = true;
            }
            if (tithiObj.le_nextValEndTime != null && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunRiseCal.getTimeInMillis() + (24 * 60 * 60 * 1000))) {
                myPanchangObj.tithiKhaya = true;
            }
            if (tithiObj.currValEndTime.getTimeInMillis() < sunSetCal.getTimeInMillis() && tithiObj.le_nextValEndTime != null && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunSetCal.getTimeInMillis() + 6 * 60 * 60 * 1000)) {
                myPanchangObj.nextNightCover = true;
            }
            myPanchangObj.tithi[0] = setMySubPanchang(currTithi, currTithiDate);
            myPanchangObj.tithi[1] = setMySubPanchang(nextTithi, nextTithiDate);
            myPanchangObj.tithi[2] = setMySubPanchang(nextToNextTithi, "");


            myPanchangObj.etithi[0] = setMySubPanchang(ecurrTithi, ecurrTithiDate);
            myPanchangObj.etithi[1] = setMySubPanchang(enextTithi, enextTithiDate);
            myPanchangObj.etithi[2] = setMySubPanchang(enextToNextTithi, "");

            String shraddhaTmp = getShraddha(tithiObj, sunRiseCal, sunSetCal, sunNoonCal);


            myPanchangObj.le_shraddha = shraddhaTmp.split("__")[0].trim();
            myPanchangObj.le_shraddha = shraddhaTmp.split("__")[1].trim();


            int lunarMonthPurnimantIndex = coreDataObj.getLunarMonthPurnimantIndex();
            int lunarMonthAmantIndex = coreDataObj.getLunarMonthAmantIndex();
            int newMoonLunarDay = coreDataObj.getNewMoonLunarDay();
            int fullMoonLunarDay = coreDataObj.getFullMoonLunarDay();
            int lunarMonthType = coreDataObj.getmLunarMonthType();


            if (mLang.contains("te") || mLang.contains("kn") || mLang.contains("mr") || mLang.contains("gu")) {


                myPanchangObj.elunarDayAmant = "" + newMoonLunarDay;
                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.elunarDayPurimant = "" + newMoonLunarDay;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.lunarMonthPurimant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthPurimant = le_arr_masa[lunarMonthAmantIndex];


            } else if (mLang.contains("or") || mLang.contains("hi") || mLang.contains("en")) {
                myPanchangObj.elunarDayAmant = "" + newMoonLunarDay;
                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.elunarDayPurimant = "" + fullMoonLunarDay;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + fullMoonLunarDay);
                myPanchangObj.lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.lunarMonthPurimant = le_arr_masa[lunarMonthPurnimantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthPurnimantIndex;
                myPanchangObj.elunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthPurimant = le_arr_masa[lunarMonthPurnimantIndex];

            }

            String sakaddha = "" + getSakaddha(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya, myPanchangObj.pakshaIndex);
            String samvata = "" + getSamvata(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya, myPanchangObj.pakshaIndex);


            myPanchangObj.sakaddha = Utility.getInstance(mContext).getDayNo(sakaddha);
            myPanchangObj.esakaddha = sakaddha;
            myPanchangObj.samvata = Utility.getInstance(mContext).getDayNo(samvata);
            myPanchangObj.esamvata = samvata;

            String sanSala = "" + getSanSal(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya);
            myPanchangObj.sanSal = Utility.getInstance(mContext).getDayNo(sanSala);
            myPanchangObj.esanSal = sanSala;

            String currNakshetra = le_arr_nakshatra[nakshetraObj.currVal - 1];
            String nextNakshetra = "", nextToNextNakshetra = "";
            String currNakshetraDate = "", nextNakshetraDate = "";

            String ecurrNakshetra = le_arr_nakshatra[nakshetraObj.currVal - 1];
            String enextNakshetra = "", enextToNextNakshetra = "";
            String ecurrNakshetraDate = "", enextNakshetraDate = "";

            if (nakshetraObj.nextVal != 0) {
                currNakshetraDate = getFormattedDate(nakshetraObj.currValEndTime);
                nextNakshetra = le_arr_nakshatra[nakshetraObj.nextVal - 1];
                ecurrNakshetraDate = geteFormattedDate(nakshetraObj.currValEndTime);
                enextNakshetra = le_arr_nakshatra[nakshetraObj.nextVal - 1];
            }
            if (nakshetraObj.nextToNextVal != 0) {
                nextNakshetraDate = getFormattedDate(nakshetraObj.le_nextValEndTime);
                nextToNextNakshetra = le_arr_nakshatra[nakshetraObj.nextToNextVal - 1];
                enextNakshetraDate = geteFormattedDate(nakshetraObj.le_nextValEndTime);
                enextToNextNakshetra = le_arr_nakshatra[nakshetraObj.nextToNextVal - 1];
            }
            myPanchangObj.currNakshetraIndex = nakshetraObj.currVal - 1;

            myPanchangObj.nakshetra[0] = setMySubPanchang(currNakshetra, currNakshetraDate);
            myPanchangObj.nakshetra[1] = setMySubPanchang(nextNakshetra, nextNakshetraDate);
            myPanchangObj.nakshetra[2] = setMySubPanchang(nextToNextNakshetra, "");

            myPanchangObj.enakshetra[0] = setMySubPanchang(ecurrNakshetra, ecurrNakshetraDate);
            myPanchangObj.enakshetra[1] = setMySubPanchang(enextNakshetra, enextNakshetraDate);
            myPanchangObj.enakshetra[2] = setMySubPanchang(enextToNextNakshetra, "");

            int adjustSolarMonth = coreDataObj.getAdjustSolarMonth();
            int solarDayVal = coreDataObj.getSolarDayVal();

            long sunsignEnd = sunSignObj.currValEndTime.getTimeInMillis();
            if (sunsignEnd > midNightCal.getTimeInMillis() && (sunsignEnd < midNightCal.getTimeInMillis() + (24 * 60 * 60 * 1000L))) {

                myPanchangObj.masant = true;
            }

            myPanchangObj.esolarDay = "" + solarDayVal;
            myPanchangObj.solarDay = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
            myPanchangObj.solarDayIndex = solarDayVal;

            myPanchangObj.solarMonth = le_arr_rasi_kundali[adjustSolarMonth - 1];
            myPanchangObj.esolarMonth = le_arr_rasi_kundali[adjustSolarMonth - 1];
            myPanchangObj.solarMonthIndex = adjustSolarMonth - 1;

            myPanchangObj.monthIndex = month;


            if (mLang.contains("bn") || mLang.contains("ta") || mLang.contains("pa") || mLang.contains("ml")) {

                myPanchangObj.elunarDayAmant = "" + solarDayVal;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.elunarDayPurimant = "" + solarDayVal;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.lunarMonthAmant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.lunarMonthPurimant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.pLunarMonthIndex = adjustSolarMonth - 1;
                myPanchangObj.elunarMonthAmant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.aLunarMonthIndex = adjustSolarMonth - 1;
                myPanchangObj.elunarMonthPurimant = le_arr_masa[adjustSolarMonth - 1];


            }


            int codeRitu = (int) Math.floor((adjustSolarMonth - 1) / 2.0);
            myPanchangObj.ritu = le_arr_ritu[codeRitu];
            myPanchangObj.eRitu = le_arr_ritu[codeRitu];

            String currMoonSign = le_arr_rasi_kundali[moonSignObj.currVal - 1];
            String nextMoonSign = "", nextToNextMoonSign = "";
            String currMoonSignDate = "", nextMoonSignDate = "";

            String ecurrMoonSign = le_arr_rasi_kundali[moonSignObj.currVal - 1];
            String enextMoonSign = "", enextToNextMoonSign = "";
            String ecurrMoonSignDate = "", enextMoonSignDate = "";

            if (moonSignObj.nextVal != 0) {

                currMoonSignDate = getFormattedDate(moonSignObj.currValEndTime);

                nextMoonSign = le_arr_rasi_kundali[moonSignObj.nextVal - 1];
                ecurrMoonSignDate = geteFormattedDate(moonSignObj.currValEndTime);
                enextMoonSign = le_arr_rasi_kundali[moonSignObj.nextVal - 1];


            }
            if (moonSignObj.nextToNextVal != 0) {
                nextMoonSignDate = getFormattedDate(moonSignObj.le_nextValEndTime);
                nextToNextMoonSign = le_arr_rasi_kundali[moonSignObj.nextToNextVal - 1];
                enextMoonSignDate = geteFormattedDate(moonSignObj.le_nextValEndTime);
                enextToNextMoonSign = le_arr_rasi_kundali[moonSignObj.nextToNextVal - 1];


            }


            myPanchangObj.moonSign[0] = setMySubPanchang(currMoonSign, currMoonSignDate);
            myPanchangObj.moonSign[1] = setMySubPanchang(nextMoonSign, nextMoonSignDate);
            myPanchangObj.moonSign[2] = setMySubPanchang(nextToNextMoonSign, "");
            myPanchangObj.emoonSign[0] = setMySubPanchang(ecurrMoonSign, ecurrMoonSignDate);
            myPanchangObj.emoonSign[1] = setMySubPanchang(enextMoonSign, enextMoonSignDate);
            myPanchangObj.emoonSign[2] = setMySubPanchang(enextToNextMoonSign, "");

            return myPanchangObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getAuspKeyDate(Calendar cal) {

        Date date = cal.getTime();
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);


        return dateFormat.format(date);
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

        if ((mLang.contains("or") || mLang.contains("hi"))) {
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


    private String getShraddha(CoreDataHelper.Panchanga tithiObj, Calendar sunRiseCal, Calendar sunSetCal, Calendar sunNoonCal) {

        String efullTmp = "";
        String eko = "", par = "", full = "";
        String eeko = "", epar = "", efull = "";

        try {

            if (tithiObj.currValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 + (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];

                efull = le_arr_tithi[tithiObj.currVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 - (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > sunNoonCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000)) {
                eko = le_arr_tithi[tithiObj.currVal - 1];
                eeko = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() < sunNoonCal.getTimeInMillis() && tithiObj.le_nextValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                efullTmp = le_arr_tithi[tithiObj.currVal - 1];
                if (prevShraddha.contains(efullTmp)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    efull = le_arr_tithi[tithiObj.nextVal - 1];

                } else if (tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() - 3 * 60 * 60 * 1000)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    efull = le_arr_tithi[tithiObj.nextVal - 1];
                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 1.5 * 60 * 60 * 1000) {
                    eko = le_arr_tithi[tithiObj.currVal - 1];
                    eeko = le_arr_tithi[tithiObj.currVal - 1];

                    par = le_arr_tithi[tithiObj.nextVal - 1];
                    epar = le_arr_tithi[tithiObj.nextVal - 1];

                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 2 * 60 * 60 * 1000) {

                    par = le_arr_tithi[tithiObj.nextVal - 1];
                    epar = le_arr_tithi[tithiObj.nextVal - 1];

                } else {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    eko = le_arr_tithi[tithiObj.currVal - 1];

                    efull = le_arr_tithi[tithiObj.nextVal - 1];
                    eeko = le_arr_tithi[tithiObj.currVal - 1];
                }

            }


            prevShraddha = efullTmp;


        } catch (Exception e) {
            e.printStackTrace();
            if (!full.isEmpty()) {
                full = full + " " + le_shraddha_ra + " " + le_shraddha;
            }
            if (!eko.isEmpty()) {
                eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
            }
            if (!par.isEmpty()) {
                par = par + " " + le_shraddha_ra + " " + le_shraddha_parban;
            }

            if (!efull.isEmpty()) {
                efull = efull + " " + le_shraddha_ra + " " + le_shraddha;
            }
            if (!eko.isEmpty()) {
                eeko = eeko + " " + le_shraddha_ra + " " + le_shraddha_eko;
            }
            if (!epar.isEmpty()) {
                epar = epar + " " + le_shraddha_ra + " " + le_shraddha_parban;
            }
            return eko + " " + par + " " + full + "__" + eeko + " " + epar + " " + efull;

        }

        if (!full.isEmpty()) {
            full = full + " " + le_shraddha_ra + " " + le_shraddha;
        }
        if (!eko.isEmpty()) {
            eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
        }
        if (!par.isEmpty()) {
            par = par + " " + le_shraddha_ra + " " + le_shraddha_parban;
        }

        if (!efull.isEmpty()) {
            efull = efull + " " + le_shraddha_ra + " " + le_shraddha;
        }
        if (!eko.isEmpty()) {
            eeko = eeko + " " + le_shraddha_ra + " " + le_shraddha_eko;
        }
        if (!epar.isEmpty()) {
            epar = epar + " " + le_shraddha_ra + " " + le_shraddha_parban;
        }


        return eko + " " + par + " " + full + "__" + eeko + " " + epar + " " + efull;
    }

    public class MyPanchang {
        boolean masant = false, tithiMala = false, tithiKhaya = false, nextNightCover = false, currNightCover = false;
        public int lunarMonthType, currNakshetraIndex = -1, puskar, monthIndex, solarDayIndex, currTithiIndex = -1, dayOfWeek, pLunarMonthIndex, aLunarMonthIndex, solarMonthIndex, nextTithiIndex = -1, nextToNextTithiIndex = -1, pakshaIndex;
        public String samvata, esamvata, ecurrTithi, currTithi, chandrashuddhi, tarashuddhi, auspKey, sanSal, sakaddha, paksha, sunRise, sunSet, bara, day, month, year;


        public MySubPanchang[] tithi = new MySubPanchang[3];
        public MySubPanchang[] nakshetra = new MySubPanchang[3];
        public MySubPanchang[] joga = new MySubPanchang[3];
        public MySubPanchang[] karana = new MySubPanchang[5];
        public MySubPanchang[] sunSign = new MySubPanchang[2];
        public MySubPanchang[] moonSign = new MySubPanchang[3];


        public String lunarDayAmant, lunarMonthAmant, lunarDayPurimant, lunarMonthPurimant, solarDay, solarMonth;

        public String esanSal, esakaddha, epaksha, esunRise, esunSet, ebara, eday, emonth, eyear;


        public MySubPanchang[] etithi = new MySubPanchang[3];
        public MySubPanchang[] jogini = new MySubPanchang[3];
        public MySubPanchang[] food = new MySubPanchang[3];

        public MySubPanchang[] enakshetra = new MySubPanchang[3];
        public MySubPanchang[] emoonSign = new MySubPanchang[3];

        public String eRitu, ritu, ayana, le_shraddha,  elunarDayAmant, elunarMonthAmant, elunarDayPurimant, elunarMonthPurimant, esolarDay, esolarMonth;


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
