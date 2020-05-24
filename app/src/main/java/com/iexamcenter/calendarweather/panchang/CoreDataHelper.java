package com.iexamcenter.calendarweather.panchang;


import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CoreDataHelper{

    private Double mDms, mDmm;

    private int dayOfWeek;


    public int getmLunarMonthType() {
        return mLunarMonthType;
    }

    public void setmLunarMonthType(int mLunarMonthType) {
        this.mLunarMonthType = mLunarMonthType;
    }

    private int mLunarMonthType, mDay, mMonth, mYear, newMoonLunarDay, fullMoonLunarDay, solarDayVal, adjustSolarMonth, lunarMonthPurnimantIndex, lunarMonthAmantIndex;
    private static int diffLngInMin, sumLngInMin, moonLngInMin, sunLngInMin;

    private Calendar sunRiseCal, midNight, sunSetCal;
    private Calendar nextDaySunRiseCal;
    private EphemerisEntity mEphemerisObj;

    private int mIstInMin, sunRiseMin;
    private Panchanga tithiDataObj, nakshetraDataObj, jogaDataObj, moonSignDataObj, sunSignDataObj;

    private Karana karanaDataObj;
    private ArrayList<StartEndTime> amritaStartEndTimeList, varjyamStartEndTimeList;

    public Calendar getSunRiseCal() {
        return sunRiseCal;
    }

    public void setSunRiseCal(Calendar sunRiseCal) {
        this.sunRiseCal = sunRiseCal;
    }

    public Calendar getMidNight() {
        return midNight;
    }

    public void setMidNight(Calendar midNight) {
        this.midNight = midNight;
    }

    public Calendar getSunSetCal() {
        return sunSetCal;
    }

    public void setSunSetCal(Calendar sunSetCal) {
        this.sunSetCal = sunSetCal;
    }

    public int getLunarMonthPurnimantIndex() {
        return lunarMonthPurnimantIndex;
    }

    public void setLunarMonthPurnimantIndex(int lunarMonthPurnimantIndex) {
        this.lunarMonthPurnimantIndex = lunarMonthPurnimantIndex;
    }

    public int getLunarMonthAmantIndex() {
        return lunarMonthAmantIndex;
    }

    public void setLunarMonthAmantIndex(int lunarMonthAmantIndex) {
        this.lunarMonthAmantIndex = lunarMonthAmantIndex;
    }


    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public CoreDataHelper(String mSunLng, String mMoonLng, String dms, String dmm, int day, int month, int year, int sunRise, int istInMin, EphemerisEntity ephemerisObj) {
        sunRiseMin = sunRise;
        mIstInMin = istInMin;
        mDay = day;
        mMonth = month;
        mYear = year;
        sunRiseCal = getSunRise();
        nextDaySunRiseCal = getNextDaySunRise();
        mEphemerisObj = ephemerisObj;


        mDms = Double.parseDouble(dms);
        mDmm = Double.parseDouble(dmm);
        diffLngInMin = differenceBetweenLng(mMoonLng, mSunLng);
        sumLngInMin = sumBetweenLng(mMoonLng, mSunLng);
        moonLngInMin = lngToMin(mMoonLng);
        sunLngInMin = lngToMin(mSunLng);


        tithiDataObj = getPanchangaData(1);
        nakshetraDataObj = getPanchangaData(2);
        jogaDataObj = getPanchangaData(3);
        moonSignDataObj = getPanchangaData(4);
        sunSignDataObj = getPanchangaData(5);

        karanaDataObj = getKaranaData();


        amritaStartEndTimeList = getAmritaGhadiyas(nakshetraDataObj);
        varjyamStartEndTimeList = getVarjyamGhadiyas(nakshetraDataObj);

    }

    public EphemerisEntity getPlanetInfo() {
        return mEphemerisObj;
    }

    public int getmDay() {
        return mDay;
    }

    public int getmMonth() {
        return mMonth;
    }

    public int getmYear() {
        return mYear;
    }

    public int getNewMoonLunarDay() {
        return newMoonLunarDay;
    }

    public void setNewMoonLunarDay(int newMoonLunarDay) {
        this.newMoonLunarDay = newMoonLunarDay;
    }

    public int getFullMoonLunarDay() {
        return fullMoonLunarDay;
    }

    public void setFullMoonLunarDay(int fullMoonLunarDay) {
        this.fullMoonLunarDay = fullMoonLunarDay;
    }

    public int getSolarDayVal() {
        return solarDayVal;
    }

    public void setSolarDayVal(int solarDayVal) {
        this.solarDayVal = solarDayVal;
    }

    public int getAdjustSolarMonth() {
        return adjustSolarMonth;
    }

    public void setAdjustSolarMonth(int adjustSolarMonth) {
        this.adjustSolarMonth = adjustSolarMonth;
    }

    public ArrayList<StartEndTime> getAmritaStartEndTimeList() {
        return amritaStartEndTimeList;

    }

    public ArrayList<StartEndTime> getVarjyamStartEndTimeList() {
        return varjyamStartEndTimeList;

    }


    private Karana getKaranaData() {
        double startTime, istInHr = 5.5, remainingDistance;
        double val1ET, val2ET, val3ET, val4ET, val5ET;
        int val1, val2 = 0, val3 = 0, val4 = 0, val5 = 0, val6 = 0;

        int maxSize = 60;
        int degDiffInMin = 6 * 60;
        int ist = 5 * 60 + 30;
        int dayInHr = 24;
        int aHrInMin = 60;

        int val, maximumRangeInMin, minimumRangeInMin, elapsedDistance;

        val = (diffLngInMin / degDiffInMin) + 1;
        maximumRangeInMin = val * degDiffInMin;
        remainingDistance = maximumRangeInMin - diffLngInMin;
        val1ET = (remainingDistance / (mDmm * aHrInMin - mDms * aHrInMin)) * dayInHr;
        minimumRangeInMin = (val - 1) * degDiffInMin;
        elapsedDistance = diffLngInMin - minimumRangeInMin;

        startTime = istInHr - (elapsedDistance / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;

        val2ET = ((remainingDistance + degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;
        val3ET = ((remainingDistance + 2 * degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;

        val4ET = ((remainingDistance + 3 * degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;
        val5ET = ((remainingDistance + 4 * degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;
        Calendar val1Cal, val2Cal, val3Cal, val4Cal, val5Cal;

        Calendar calendarStart = getCalendar();
        calendarStart.add(Calendar.MINUTE, convertHourToMinute(startTime));

        val1Cal = getCalendar();
        val1Cal.add(Calendar.MINUTE, convertHourToMinute(val1ET) + ist);

        val2Cal = getCalendar();
        val2Cal.add(Calendar.MINUTE, convertHourToMinute(val2ET) + ist);

        val3Cal = getCalendar();
        val3Cal.add(Calendar.MINUTE, convertHourToMinute(val3ET) + ist);

        val4Cal = getCalendar();
        val4Cal.add(Calendar.MINUTE, convertHourToMinute(val4ET) + ist);

        val5Cal = getCalendar();
        val5Cal.add(Calendar.MINUTE, convertHourToMinute(val5ET) + ist);


        val1 = getValueDuringSunRise(calendarStart, val1Cal, val, maxSize);

        if (val1 != val) {
            val1Cal = val2Cal;
            val2Cal = val3Cal;
            val3Cal = val4Cal;
            val4Cal = val5Cal;


        }
        if (val1Cal.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            val2 = val1 + 1;
        }
        if (val2Cal.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            val3 = val2 + 1;
        }
        if (val3Cal.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            val4 = val3 + 1;
        }
        if (val4Cal.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            val5 = val4 + 1;
        }
        if (val5Cal.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            val6 = val5 + 1;
        }

        val1 = (val1 > (maxSize + 2)) ? 3 : ((val1 > (maxSize + 1)) ? 2 : ((val1 > maxSize) ? 1 : val1));
        val2 = (val2 > (maxSize + 2)) ? 3 : ((val2 > (maxSize + 1)) ? 2 : ((val2 > maxSize) ? 1 : val2));
        val3 = (val3 > (maxSize + 2)) ? 3 : ((val3 > (maxSize + 1)) ? 2 : ((val3 > maxSize) ? 1 : val3));
        val4 = (val4 > (maxSize + 2)) ? 3 : ((val4 > (maxSize + 1)) ? 2 : ((val4 > maxSize) ? 1 : val4));
        val5 = (val5 > (maxSize + 2)) ? 3 : ((val5 > (maxSize + 1)) ? 2 : ((val5 > maxSize) ? 1 : val5));
        val6 = (val6 > (maxSize + 2)) ? 3 : ((val6 > (maxSize + 1)) ? 2 : ((val6 > maxSize) ? 1 : val6));

      /*  val3 = (val3 > (maxSize+1)) ? 2 : (val3 > (maxSize)) ? 1 : val3;
        val4 = (val4 > (maxSize+1)) ? 2 : (val4 > (maxSize)) ? 1 : val4;
        val5 = (val5 > (maxSize+1)) ? 2 : (val5 > (maxSize)) ? 1 : val5;
        val6 = (val6 > (maxSize+1)) ? 2 : (val6 > (maxSize)) ? 1 : val6;

        val1 = (val1 > (maxSize+1)) ? 2 : val1;
        val2 = (val2 > maxSize+1) ? 2 : val2;
        val3 = (val3 > maxSize+1) ? 2 : val3;
        val4 = (val4 > maxSize+1) ? 2 : val4;
        val5 = (val5 > maxSize+1) ? 2 : val5;
        val6 = (val6 > maxSize+1) ? 2 : val6;*/

        Karana obj = new Karana();
        obj.val1 = val1;
        obj.val2 = val2;
        obj.val3 = val3;
        obj.val4 = val4;
        obj.val5 = val5;
        obj.val6 = val6;
        obj.val1ET = val1Cal;
        obj.val2ET = val2Cal;
        obj.val3ET = val3Cal;
        obj.val4ET = val4Cal;
        obj.val5ET = val5Cal;


        return obj;
    }

    private Panchanga getPanchangaData(int type) {
        double startTime = 0, endTime = 0, istInHr = 5.5, remainingDistance, nextEndTime = 0, nextToNextEndTime = 0;
        int maxSize;
        int degDiffInMin;
        int ist = mIstInMin;

        int dayInHr = 24;
        int aHrInMin = 60;

        int val = 0, maximumRangeInMin, minimumRangeInMin, elapsedDistance;
        switch (type) {
            case 1://tithi
                maxSize = 30;
                degDiffInMin = 12 * 60;
                val = (diffLngInMin / degDiffInMin) + 1;
                maximumRangeInMin = val * degDiffInMin;
                remainingDistance = maximumRangeInMin - diffLngInMin;
                endTime = (remainingDistance / (mDmm * aHrInMin - mDms * aHrInMin)) * dayInHr;
                minimumRangeInMin = (val - 1) * degDiffInMin;
                elapsedDistance = diffLngInMin - minimumRangeInMin;
                startTime = istInHr - (elapsedDistance / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;
                nextEndTime = ((remainingDistance + degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;
                nextToNextEndTime = ((remainingDistance + 2 * degDiffInMin) / ((mDmm * aHrInMin) - (mDms * aHrInMin))) * dayInHr;

                break;
            case 2://Nakshetra
                maxSize = 27;
                degDiffInMin = 13 * 60 + 20;
                val = (moonLngInMin / degDiffInMin) + 1;
                maximumRangeInMin = val * degDiffInMin;
                remainingDistance = maximumRangeInMin - moonLngInMin;
                endTime = (remainingDistance / (mDmm * aHrInMin)) * dayInHr;
                minimumRangeInMin = (val - 1) * degDiffInMin;
                elapsedDistance = moonLngInMin - minimumRangeInMin;
                startTime = istInHr - (elapsedDistance / (mDmm * aHrInMin)) * dayInHr;
                nextEndTime = ((remainingDistance + degDiffInMin) / (mDmm * aHrInMin)) * dayInHr;
                nextToNextEndTime = ((remainingDistance + 2 * degDiffInMin) / (mDmm * aHrInMin)) * dayInHr;
                break;
            case 3://Joga
                maxSize = 27;
                degDiffInMin = 13 * 60 + 20;
                val = (sumLngInMin / degDiffInMin) + 1;
                maximumRangeInMin = val * degDiffInMin;
                remainingDistance = maximumRangeInMin - sumLngInMin;
                endTime = (remainingDistance / (mDmm * aHrInMin + mDms * aHrInMin)) * dayInHr;
                minimumRangeInMin = (val - 1) * degDiffInMin;
                elapsedDistance = sumLngInMin - minimumRangeInMin;
                startTime = istInHr - (elapsedDistance / ((mDmm * aHrInMin) + (mDms * aHrInMin))) * dayInHr;
                nextEndTime = ((remainingDistance + degDiffInMin) / ((mDmm * aHrInMin) + (mDms * aHrInMin))) * dayInHr;
                nextToNextEndTime = ((remainingDistance + 2 * degDiffInMin) / ((mDmm * aHrInMin) + (mDms * aHrInMin))) * dayInHr;
                break;
            case 4://moonSign
                maxSize = 12;
                degDiffInMin = 30 * 60;
                val = (moonLngInMin / degDiffInMin) + 1;
                maximumRangeInMin = val * degDiffInMin;
                remainingDistance = maximumRangeInMin - moonLngInMin;
                endTime = (remainingDistance / (mDmm * aHrInMin)) * dayInHr;
                minimumRangeInMin = (val - 1) * degDiffInMin;
                elapsedDistance = moonLngInMin - minimumRangeInMin;
                startTime = istInHr - (elapsedDistance / (mDmm * aHrInMin)) * dayInHr;
                nextEndTime = ((remainingDistance + degDiffInMin) / (mDmm * aHrInMin)) * dayInHr;
                nextToNextEndTime = ((remainingDistance + 2 * degDiffInMin) / (mDmm * aHrInMin)) * dayInHr;

                break;
            case 5://sunSign
                maxSize = 12;
                degDiffInMin = 30 * 60;
                val = (sunLngInMin / degDiffInMin) + 1;
                maximumRangeInMin = val * degDiffInMin;
                remainingDistance = maximumRangeInMin - sunLngInMin;

                endTime = (remainingDistance / (mDms * aHrInMin)) * dayInHr;
                minimumRangeInMin = (val - 1) * degDiffInMin;
                elapsedDistance = sunLngInMin - minimumRangeInMin;
                startTime = istInHr - (elapsedDistance / (mDms * aHrInMin)) * dayInHr;
                nextEndTime = ((remainingDistance + degDiffInMin) / (mDms * aHrInMin)) * dayInHr;
                nextToNextEndTime = ((remainingDistance + 2 * degDiffInMin) / (mDms * aHrInMin)) * dayInHr;


                break;

            default:
                maxSize = 12;


        }

        Calendar calendarStart = getCalendar();


        calendarStart.add(Calendar.MINUTE, convertHourToMinute(startTime));
        Calendar calendarEnd = getCalendar();
        calendarEnd.add(Calendar.MINUTE, convertHourToMinute(endTime) + ist);
        Calendar calendarNextEnd = getCalendar();
        Calendar calendarNextToNextEnd = getCalendar();


        calendarNextEnd.add(Calendar.MINUTE, convertHourToMinute(nextEndTime) + ist);

        int currVal, nextVal = 0, nextToNextVal = 0;


        currVal = getValueDuringSunRise(calendarStart, calendarEnd, val, maxSize);


        Calendar currCal = calendarEnd;
        Calendar nextCal = null;

        if (currVal > val /*|| (val==maxSize && currVal==1)*/) {

            endTime = nextEndTime;
            nextEndTime = nextToNextEndTime;
            calendarStart = calendarEnd;

            calendarEnd = getCalendar();

            calendarEnd.add(Calendar.MINUTE, convertHourToMinute(endTime) + ist);

            calendarNextEnd = getCalendar();
            calendarNextEnd.add(Calendar.MINUTE, convertHourToMinute(nextEndTime) + ist);
            currCal = calendarEnd;
            nextCal = calendarNextEnd;



        } else if (currVal < val) {


            calendarEnd = calendarStart;
            calendarNextEnd = getCalendar();
            calendarNextEnd.add(Calendar.MINUTE, convertHourToMinute(endTime) + ist);
            currCal = calendarEnd;
            nextCal = calendarNextEnd;


        }
        if (calendarEnd.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            if (currVal < maxSize)
                nextVal = currVal + 1;
            else
                nextVal = 1;
            nextCal = calendarNextEnd;
        }

        if (calendarNextEnd.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
            if (currVal < maxSize)
                nextVal = currVal + 1;
            else
                nextVal = 1;
            if (nextVal < maxSize)
                nextToNextVal = nextVal + 1;
            else
                nextToNextVal = 1;

            nextCal = calendarNextEnd;
        }


        currVal = (currVal > maxSize) ? 1 : currVal;
        nextVal = (nextVal > maxSize) ? 1 : nextVal;
        nextToNextVal = (nextToNextVal > maxSize) ? 1 : nextToNextVal;
        Panchanga obj = new Panchanga();
        obj.currVal = currVal;
        obj.nextVal = nextVal;
        obj.nextToNextVal = nextToNextVal;
        obj.currValEndTime = currCal;
        obj.le_nextValEndTime = nextCal;
        obj.currValStartTime = calendarStart;

        if(nextToNextEndTime>0) {
            calendarNextToNextEnd = getCalendar();
            calendarNextToNextEnd.add(Calendar.MINUTE, convertHourToMinute(nextToNextEndTime) + ist);

            obj.nextToNextValEndTime = calendarNextToNextEnd;
        }


        return obj;
    }

    private String geteFormattedDate(Calendar cal) {

        try {
            Date date = cal.getTime();
            DateFormat dateFormat;

            String dt;


            dateFormat = new SimpleDateFormat("hh:mm a  dd/MMM yyyy", Locale.US);
            return dt = dateFormat.format(date);


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Calendar getSunRise() {
        Calendar cal = getCalendar();
        cal.add(Calendar.MINUTE, sunRiseMin);
        return cal;
    }

    private Calendar getNextDaySunRise() {
        Calendar cal = getCalendar();
        cal.add(Calendar.MINUTE, 24 * 60 + sunRiseMin);
        return cal;
    }

    private int getValueDuringSunRise(Calendar calendarStart, Calendar calendarEnd, int currVal, int maxLength) {
        long sunRiseVal = sunRiseCal.getTimeInMillis();
        if (calendarStart.getTimeInMillis() < sunRiseVal && calendarEnd.getTimeInMillis() > sunRiseVal) {
            return currVal;

        } else if (calendarStart.getTimeInMillis() < sunRiseVal && calendarEnd.getTimeInMillis() < sunRiseVal) {
            if ((currVal + 1) > maxLength) {
                return 1;
            } else {
                return (currVal + 1);
            }
        } else if (calendarStart.getTimeInMillis() > sunRiseVal && calendarEnd.getTimeInMillis() > sunRiseVal) {

            if ((currVal - 1) < 1) {
                return maxLength;
            } else {
                return (currVal - 1);
            }
        }
        return 10;

    }

    private Calendar getCalendar(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private int convertHourToMinute(double hr) {
        int absHr = (int) hr;
        int min = (int) ((hr - absHr) * 60);
        return (absHr * 60 + min);

    }


    private int lngToMin(String moonLng) {
        int degMoon = Integer.parseInt(moonLng.split("_")[0]);
        int minMoon = Integer.parseInt(moonLng.split("_")[1]);

        int degMoonTomin = degMoon * 60;
        return minMoon + degMoonTomin;
    }


    private int sumBetweenLng(String moonLng, String sunLng) {
        int degMoon = Integer.parseInt(moonLng.split("_")[0]);
        int minMoon = Integer.parseInt(moonLng.split("_")[1]);

        int degMoonTomin = degMoon * 60;
        int moonLngDecimal = minMoon + degMoonTomin;

        int degSun = Integer.parseInt(sunLng.split("_")[0]);
        int minSun = Integer.parseInt(sunLng.split("_")[1]);

        int degSunTomin = degSun * 60;
        int sunLngDecimal = minSun + degSunTomin;

        int sum = moonLngDecimal + sunLngDecimal;
        if (sum > (360 * 60)) {

            sum = sum - (360 * 60);

        }
        return sum;
    }

    private int differenceBetweenLng(String moonLng, String sunLng) {
        int degMoon = Integer.parseInt(moonLng.split("_")[0]);
        int minMoon = Integer.parseInt(moonLng.split("_")[1]);

        int degMoonTomin = degMoon * 60;
        int moonLngDecimal = minMoon + degMoonTomin;

        int degSun = Integer.parseInt(sunLng.split("_")[0]);
        int minSun = Integer.parseInt(sunLng.split("_")[1]);

        int degSunTomin = degSun * 60;
        int sunLngDecimal = minSun + degSunTomin;

        if (moonLngDecimal < sunLngDecimal) {

            moonLngDecimal = moonLngDecimal + (360 * 60);

        }
        return moonLngDecimal - sunLngDecimal;


    }


    public Panchanga getTithi() {
        return tithiDataObj;

    }

    public Panchanga getNakshetra() {
        return nakshetraDataObj;
    }

    public Panchanga getSunSign() {

        return sunSignDataObj;
    }

    public Panchanga getMoonSign() {
        return moonSignDataObj;
    }

    public int getPaksha() {
        int paksha = 0;
        if (tithiDataObj.currVal == 30)
            paksha = 1;

        else if (tithiDataObj.currVal > 15)
            paksha = 1;
        return paksha;
    }

    public Karana getKarana() {
        return karanaDataObj;
    }

    public Panchanga getJoga() {

        return jogaDataObj;
    }

    public static class Panchanga {
        public int currVal, nextVal, nextToNextVal,nextToNextNextVal;
        public Calendar currValStartTime, currValEndTime, le_nextValEndTime,nextToNextValEndTime;
    }

    public static class StartEndTime {

        public Calendar startTime, endTime;
    }

    public static class Karana {
        public int val1, val2, val3, val4, val5, val6;
        public Calendar val1ET, val2ET, val3ET, val4ET, val5ET;
    }


    public String getLunarPurnimant(int year) {
        switch (year) {
            case 2015:
                return "8|9|10|11|0|1|1|2-3|4|5|6|7";
            case 2016:
                return "8|9|10|11|1|2|3|4|5|6|7|8";
            case 2017:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2018:
                return "9|10|11|0|1-1|2|3|4|5|6|7|8";
            case 2019:
                return "9|10|11|0|1|2|3|4|5|6|7|8";
            case 2020:
                return "10|11|0|1|2|3|4|5|6|7|8|9-10";
            case 2021:
                return "11|0|1|2|3|4|5|6|7|8|9|10";
            case 2022:
                return "11|0|1|2|3|4|5|6|7|8|9|10";
        }

        return "";
    }


    private ArrayList<StartEndTime> getVarjyamGhadiyas(Panchanga nakshetraDataObj) {
        ArrayList<StartEndTime> startEndTimesList = new ArrayList<>();
        try {

            Double[] xTime = {20.0, 9.6, 12.0, 16.0, 5.6, 8.4, 12.0, 8.0, 12.8, 12.0, 8.0, 7.2, 8.4, 8.0, 5.6, 5.6, 4.0, 5.6, 8.0, 9.6, 8.0, 4.0, 4.0, 7.2, 6.4, 9.6, 12.0};
            int nakshetra1 = nakshetraDataObj.currVal;

            Calendar startTime1 = nakshetraDataObj.currValStartTime;
            Calendar endTime1 = nakshetraDataObj.currValEndTime;


            double startHrNakshetra1 = (startTime1.get(Calendar.HOUR_OF_DAY) + (startTime1.get(Calendar.MINUTE) / 60.0));
            double durationNakshetra1 = (endTime1.getTimeInMillis() - startTime1.getTimeInMillis()) / (1000 * 60 * 60.0);
            double xNakshetra1 = xTime[nakshetra1 - 1];
            double startHrAmrita1 = startHrNakshetra1 + ((xNakshetra1 / 24) * durationNakshetra1);
            double durationAmrita1 = durationNakshetra1 * (1.6 / 24);

            Calendar calendarAmrita1 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));

            calendarAmrita1.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1));

            Calendar calendarAmrita2 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));
            calendarAmrita2.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1) + convertHourToMinute(durationAmrita1));
            StartEndTime obj = new StartEndTime();
            obj.startTime = calendarAmrita1;
            obj.endTime = calendarAmrita2;
            if (sunRiseCal.getTimeInMillis() < calendarAmrita2.getTimeInMillis()) {
                startEndTimesList.add(obj);
            }

            if (nakshetraDataObj.nextVal != 0) {

                int nakshetra2 = nakshetraDataObj.nextVal;
                double xNakshetra2 = xTime[nakshetra2 - 1];
                Calendar startTime2 = nakshetraDataObj.currValEndTime;
                Calendar endTime2 = nakshetraDataObj.le_nextValEndTime;
                double startHrNakshetra2 = (startTime2.get(Calendar.HOUR_OF_DAY) + (startTime2.get(Calendar.MINUTE) / 60.0));
                double durationNakshetra2 = (endTime2.getTimeInMillis() - startTime2.getTimeInMillis()) / (1000 * 60 * 60.0);
                double startHrAmrita2 = startHrNakshetra2 + (xNakshetra2 / 24) * durationNakshetra1;
                double durationAmrita2 = durationNakshetra2 * (1.6 / 24);
                Calendar calendarAmrita3 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                calendarAmrita3.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2));

                Calendar calendarAmrita4 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                calendarAmrita4.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2) + convertHourToMinute(durationAmrita2));

                obj = new StartEndTime();
                obj.startTime = calendarAmrita3;
                obj.endTime = calendarAmrita4;
                if (calendarAmrita3.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
                    startEndTimesList.add(obj);
                }


            }


            if (nakshetra1 == 19) {


                startTime1 = nakshetraDataObj.currValStartTime;
                endTime1 = nakshetraDataObj.currValEndTime;
                startHrNakshetra1 = (startTime1.get(Calendar.HOUR_OF_DAY) + (startTime1.get(Calendar.MINUTE) / 60.0));
                durationNakshetra1 = (endTime1.getTimeInMillis() - startTime1.getTimeInMillis()) / (1000 * 60 * 60.0);
                xNakshetra1 = 22.4;//xTime[nakshetra1 - 1];
                startHrAmrita1 = startHrNakshetra1 + ((xNakshetra1 / 24) * durationNakshetra1);
                durationAmrita1 = durationNakshetra1 * (1.6 / 24);

                calendarAmrita1 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));

                calendarAmrita1.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1));

                calendarAmrita2 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));
                calendarAmrita2.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1) + convertHourToMinute(durationAmrita1));
                obj = new StartEndTime();
                obj.startTime = calendarAmrita1;
                obj.endTime = calendarAmrita2;
                if (sunRiseCal.getTimeInMillis() < calendarAmrita2.getTimeInMillis()) {
                    startEndTimesList.add(obj);
                }

                if (nakshetraDataObj.nextVal != 0) {

                    int nakshetra2 = nakshetraDataObj.nextVal;
                    double xNakshetra2 = xTime[nakshetra2 - 1];
                    Calendar startTime2 = nakshetraDataObj.currValEndTime;
                    Calendar endTime2 = nakshetraDataObj.le_nextValEndTime;
                    double startHrNakshetra2 = (startTime2.get(Calendar.HOUR_OF_DAY) + (startTime2.get(Calendar.MINUTE) / 60.0));
                    double durationNakshetra2 = (endTime2.getTimeInMillis() - startTime2.getTimeInMillis()) / (1000 * 60 * 60.0);
                    double startHrAmrita2 = startHrNakshetra2 + (xNakshetra2 / 24) * durationNakshetra1;
                    double durationAmrita2 = durationNakshetra2 * (1.6 / 24);
                    Calendar calendarAmrita3 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                    calendarAmrita3.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2));

                    Calendar calendarAmrita4 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                    calendarAmrita4.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2) + convertHourToMinute(durationAmrita2));

                    obj = new StartEndTime();
                    obj.startTime = calendarAmrita3;
                    obj.endTime = calendarAmrita4;
                    if (calendarAmrita3.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
                        startEndTimesList.add(obj);
                    }


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return startEndTimesList;
    }

    private ArrayList<StartEndTime> getAmritaGhadiyas(Panchanga nakshetraDataObj) {
        ArrayList<StartEndTime> startEndTimesList = new ArrayList<>();
        try {
            Double[] xTime = {16.8, 19.2, 21.6, 20.8, 15.2, 14.0, 21.6, 17.6, 22.4, 21.6, 17.6, 16.8, 18.0, 17.6, 15.2, 15.2, 13.6, 15.2, 17.6, 19.2, 17.6, 13.6, 13.6, 16.8, 16.0, 19.2, 21.6};
            int nakshetra1 = nakshetraDataObj.currVal;
            Calendar startTime1 = nakshetraDataObj.currValStartTime;
            Calendar endTime1 = nakshetraDataObj.currValEndTime;
            double startHrNakshetra1 = (startTime1.get(Calendar.HOUR_OF_DAY) + (startTime1.get(Calendar.MINUTE) / 60.0));
            double durationNakshetra1 = (endTime1.getTimeInMillis() - startTime1.getTimeInMillis()) / (1000 * 60 * 60.0);
            double xNakshetra1 = xTime[nakshetra1 - 1];
            double startHrAmrita1 = startHrNakshetra1 + ((xNakshetra1 / 24) * durationNakshetra1);
            double durationAmrita1 = durationNakshetra1 * (1.6 / 24);

            Calendar calendarAmrita1 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));

            calendarAmrita1.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1));

            Calendar calendarAmrita2 = getCalendar(startTime1.get(Calendar.DAY_OF_MONTH), startTime1.get(Calendar.MONTH), startTime1.get(Calendar.YEAR));
            calendarAmrita2.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita1) + convertHourToMinute(durationAmrita1));
            StartEndTime obj = new StartEndTime();
            obj.startTime = calendarAmrita1;
            obj.endTime = calendarAmrita2;
            if (sunRiseCal.getTimeInMillis() < calendarAmrita2.getTimeInMillis()) {
                startEndTimesList.add(obj);
            }

            if (nakshetraDataObj.nextVal != 0) {

                int nakshetra2 = nakshetraDataObj.nextVal;
                double xNakshetra2 = xTime[nakshetra2 - 1];
                Calendar startTime2 = nakshetraDataObj.currValEndTime;
                Calendar endTime2 = nakshetraDataObj.le_nextValEndTime;
                double startHrNakshetra2 = (startTime2.get(Calendar.HOUR_OF_DAY) + (startTime2.get(Calendar.MINUTE) / 60.0));
                double durationNakshetra2 = (endTime2.getTimeInMillis() - startTime2.getTimeInMillis()) / (1000 * 60 * 60.0);
                double startHrAmrita2 = startHrNakshetra2 + (xNakshetra2 / 24) * durationNakshetra1;
                double durationAmrita2 = durationNakshetra2 * (1.6 / 24);
                Calendar calendarAmrita3 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                calendarAmrita3.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2));

                Calendar calendarAmrita4 = getCalendar(startTime2.get(Calendar.DAY_OF_MONTH), startTime2.get(Calendar.MONTH), startTime2.get(Calendar.YEAR));
                calendarAmrita4.add(Calendar.MINUTE, convertHourToMinute(startHrAmrita2) + convertHourToMinute(durationAmrita2));

                obj = new StartEndTime();
                obj.startTime = calendarAmrita3;
                obj.endTime = calendarAmrita4;
                if (calendarAmrita3.getTimeInMillis() < nextDaySunRiseCal.getTimeInMillis()) {
                    startEndTimesList.add(obj);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return startEndTimesList;
    }


    public StartEndTime getRahuKalam(int weekDay, double surRise, double dayDuration) {
        StartEndTime startEnd = new StartEndTime();
        try {
            Double[] xfactor = {0.875, 0.125, 0.75, 0.5, 0.625, 0.375, 0.25};

            double rahuKalamStartInMin = (surRise + (dayDuration * xfactor[weekDay - 1]));
            double rahuKalamDuration = ((dayDuration * 0.125));
            Calendar startCal = getCalendar();
            startCal.add(Calendar.MINUTE, convertHourToMinute(rahuKalamStartInMin));
            startEnd.startTime = startCal;

            Calendar endCal = getCalendar();
            endCal.add(Calendar.MINUTE, convertHourToMinute((rahuKalamStartInMin + rahuKalamDuration)));
            startEnd.endTime = endCal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startEnd;

    }

    public StartEndTime getGulikaKalam(int weekDay, double surRise, double dayDuration) {
        StartEndTime startEnd = new StartEndTime();


        try {
            Double[] xfactor = {0.75, 0.625, 0.5, 0.375, 0.25, 0.125, 0.25, 0.0};

            double rahuKalamStartInMin = (surRise + (dayDuration * xfactor[weekDay - 1]));
            double rahuKalamDuration = ((dayDuration * 0.125));
            Calendar startCal = getCalendar();
            startCal.add(Calendar.MINUTE, convertHourToMinute(rahuKalamStartInMin));
            startEnd.startTime = startCal;

            Calendar endCal = getCalendar();
            endCal.add(Calendar.MINUTE, convertHourToMinute((rahuKalamStartInMin + rahuKalamDuration)));
            startEnd.endTime = endCal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startEnd;

    }

    public StartEndTime getYamaKalam(int weekDay, double surRise, double dayDuration) {
        StartEndTime startEnd = new StartEndTime();


        try {
            Double[] xfactor = {0.5, 0.375, 0.25, 0.125, 0.0, 0.75, 0.625};

            double rahuKalamStartInMin = (surRise + (dayDuration * xfactor[weekDay - 1]));
            double rahuKalamDuration = ((dayDuration * 0.125));
            Calendar startCal = getCalendar();
            startCal.add(Calendar.MINUTE, convertHourToMinute(rahuKalamStartInMin));
            startEnd.startTime = startCal;

            Calendar endCal = getCalendar();
            endCal.add(Calendar.MINUTE, convertHourToMinute((rahuKalamStartInMin + rahuKalamDuration)));
            startEnd.endTime = endCal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startEnd;

    }

    public ArrayList<StartEndTime> getDurmuhurtam(int weekDay, double surRise, double dayDuration) {
        StartEndTime startEnd;
        ArrayList<StartEndTime> arrayList = new ArrayList<>();
        double durMuhurtaStart1 = 0, durMuhurtaStart2 = 0, durMuhurtaDuration;
        switch (weekDay) {
            case 1:
                durMuhurtaStart1 = surRise + dayDuration * (10.4 / 12);

                break;
            case 2:
                durMuhurtaStart1 = surRise + dayDuration * (6.4 / 12);
                durMuhurtaStart2 = surRise + dayDuration * (8.8 / 12);
                break;
            case 3:
                durMuhurtaStart1 = surRise + dayDuration * (2.4 / 12);
                durMuhurtaStart2 = (dayDuration + surRise) + ((24 - dayDuration) * (4.8 / 12));
                break;
            case 4:
                durMuhurtaStart1 = surRise + dayDuration * (5.6 / 12);
                break;
            case 5:
                durMuhurtaStart1 = surRise + dayDuration * (4 / 12);
                durMuhurtaStart2 = surRise + dayDuration * (8.8 / 12);
                break;
            case 6:
                durMuhurtaStart1 = surRise + dayDuration * (2.4 / 12);
                durMuhurtaStart2 = surRise + dayDuration * (6.4 / 12);
                break;
            case 7:
                durMuhurtaStart1 = surRise + dayDuration * (1.6 / 12);
                break;
        }
        durMuhurtaDuration = dayDuration * (0.8 / 12);
        startEnd = new StartEndTime();
        Calendar startCal = getCalendar();
        startCal.add(Calendar.MINUTE, convertHourToMinute(durMuhurtaStart1));
        startEnd.startTime = startCal;

        Calendar endCal = getCalendar();
        endCal.add(Calendar.MINUTE, convertHourToMinute((durMuhurtaStart1 + durMuhurtaDuration)));
        startEnd.endTime = endCal;

        arrayList.add(startEnd);
        if (durMuhurtaStart2 != 0) {
            startEnd = new StartEndTime();
            startCal = getCalendar();
            startCal.add(Calendar.MINUTE, convertHourToMinute(durMuhurtaStart2));
            startEnd.startTime = startCal;

            endCal = getCalendar();
            endCal.add(Calendar.MINUTE, convertHourToMinute((durMuhurtaStart2 + durMuhurtaDuration)));
            startEnd.endTime = endCal;

            arrayList.add(startEnd);
        }

        return arrayList;

    }

    public StartEndTime getAbhijitMuhurta(double dayDurationInHr, Calendar noon) {
        Calendar noon1 = (Calendar) noon.clone();
        StartEndTime startEnd = new StartEndTime();
        double interval = dayDurationInHr / 30;

        noon1.add(Calendar.MINUTE, -convertHourToMinute(interval));
        startEnd.startTime = noon1;

        Calendar noon2 = (Calendar) noon.clone();
        noon2.add(Calendar.MINUTE, convertHourToMinute(interval));
        startEnd.endTime = noon2;

        return startEnd;

    }

    public StartEndTime getBrahmaMuhurta(Calendar Sunrise) {
        StartEndTime startEnd = new StartEndTime();
        Calendar Sunrise1 = (Calendar) Sunrise.clone();
        Sunrise1.add(Calendar.DAY_OF_MONTH, 1);
        Sunrise1.add(Calendar.MINUTE, -96);
        startEnd.startTime = Sunrise1;

        Calendar Sunrise2 = (Calendar) Sunrise1.clone();
        Sunrise2.add(Calendar.MINUTE, 48);
        startEnd.endTime = Sunrise2;

        return startEnd;

    }

    public ArrayList<StartEndTime> getAmritaBela(int weekDay, double sunrise, double dayduration) {
        double sunset = sunrise + dayduration;
        ArrayList<StartEndTime> al = new ArrayList<>();

        double varabelaGapDay = dayduration / 8;
        double varabelaGapNight = (24 - dayduration) / 8;
        switch (weekDay) {
            case 1:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 1), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 1), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 2), varabelaGapNight));
                break;
            case 2:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 0), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 5), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 7), varabelaGapDay));

                al.add(getStartEndTime((sunset + varabelaGapNight * 0), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 6), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 7), varabelaGapNight));
                break;
            case 3:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 2), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 4), varabelaGapDay));

                al.add(getStartEndTime((sunset + varabelaGapNight * 4), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 5), varabelaGapNight));
                break;
            case 4:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 1), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 6), varabelaGapDay));

                al.add(getStartEndTime((sunset + varabelaGapNight * 2), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 3), varabelaGapNight));
                break;
            case 5:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 3), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 5), varabelaGapDay));


                al.add(getStartEndTime((sunset + varabelaGapNight * 0), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 1), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 7), varabelaGapNight));
                break;
            case 6:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 0), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 7), varabelaGapDay));


                al.add(getStartEndTime((sunset + varabelaGapNight * 5), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 6), varabelaGapNight));
                break;
            case 7:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 4), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 6), varabelaGapDay));

                al.add(getStartEndTime((sunset + varabelaGapNight * 3), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 4), varabelaGapNight));
                break;

        }


        return al;

    }

    public ArrayList<StartEndTime> getMahendraBela(int weekDay, double sunrise, double dayduration) {
        double sunset = sunrise + dayduration;
        ArrayList<StartEndTime> al = new ArrayList<>();

        double varabelaGapDay = dayduration / 8;
        double varabelaGapNight = (24 - dayduration) / 8;
        switch (weekDay) {
            case 1:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 2), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 5), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 0), varabelaGapNight));
                al.add(getStartEndTime((sunset + varabelaGapNight * 7), varabelaGapNight));
                break;
            case 2:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 2), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 5), varabelaGapNight));
                break;
            case 3:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 3), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 6), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 3), varabelaGapNight));
                break;
            case 4:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 0), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 3), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 7), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 1), varabelaGapNight));
                break;
            case 5:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 0), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 4), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 6), varabelaGapNight));
                break;
            case 6:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 1), varabelaGapDay));
                al.add(getStartEndTime((sunrise + varabelaGapDay * 4), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 4), varabelaGapNight));
                break;
            case 7:
                al.add(getStartEndTime((sunrise + varabelaGapDay * 1), varabelaGapDay));
                al.add(getStartEndTime((sunset + varabelaGapNight * 2), varabelaGapNight));
                break;

        }


        return al;

    }

    private StartEndTime getStartEndTime(double varabelaStart1, double varabelaGap) {
        StartEndTime startEnd;
        startEnd = new StartEndTime();
        Calendar startCal = getCalendar();
        startCal.add(Calendar.MINUTE, convertHourToMinute(varabelaStart1));
        startEnd.startTime = startCal;

        Calendar endCal = getCalendar();
        endCal.add(Calendar.MINUTE, convertHourToMinute((varabelaStart1 + varabelaGap)));
        startEnd.endTime = endCal;
        return startEnd;

    }


    public Panchanga getPuskar(int weekDay, Panchanga tithiVal, Panchanga nakshetraVal, Calendar sunRiseCal) {
        try {
            // sun, tue, sat
            // tithi- 2nd, saptami, 12
            // krutika, purnarbasu,uttar-phalguni,uttar-ashadha, purba-bhadra, bishaka
            //  Calendar currTithiTime = tithiVal.currValEndTime;
            // Calendar nextTithiTime = tithiVal.nextValEndTime;

            //  Calendar nextTithiTime = tithiVal.nextValEndTime;


            int tithi = tithiVal.currVal;
            int nakshetra = nakshetraVal.currVal;
            int tithiNext = tithiVal.nextVal;
            int nakshetraNext = nakshetraVal.nextVal;
            int nakshetraNextToNext = nakshetraVal.nextToNextVal;
            int tithiNextToNext = tithiVal.nextToNextVal;

            boolean bWeekDay = (weekDay == 1 || weekDay == 3 || weekDay == 7);
            boolean bTithi = (tithi == 2 || tithi == 7 || tithi == 12 || tithi == 17 || tithi == 22 || tithi == 27);
            boolean bNakshetra = (nakshetra == 3 || nakshetra == 7 || nakshetra == 12 || nakshetra == 21 || nakshetra == 25 || nakshetra == 16);

            boolean bTithiNext = (tithiNext == 2 || tithiNext == 7 || tithiNext == 12 || tithiNext == 17 || tithiNext == 22 || tithiNext == 27);
            boolean bTithiNextToNext = (tithiNextToNext == 2 || tithiNextToNext == 7 || tithiNextToNext == 12 || tithiNextToNext == 17 || tithiNextToNext == 22 || tithiNextToNext == 27);

            boolean bNakshetraNext = (nakshetraNext == 3 || nakshetraNext == 7 || nakshetraNext == 12 || nakshetraNext == 21 || nakshetraNext == 25 || nakshetraNext == 16);
            boolean bNakshetraNextToNext = (nakshetraNextToNext == 3 || nakshetraNextToNext == 7 || nakshetraNextToNext == 12 || nakshetraNextToNext == 21 || nakshetraNextToNext == 25 || nakshetraNextToNext == 16);


            int puskar = 0, nextPuskar = 0, nextToNextPuskar = 0,nextToNextNextPuskar=0;
            Calendar nextSunRiseCal = getNextDaySunRise();
            Calendar currTithiEndTime = tithiVal.currValEndTime;
            Calendar nextTithiEndTime = tithiVal.le_nextValEndTime;
            Calendar currNakshtraEndTime = nakshetraVal.currValEndTime;
            Calendar nextNakshtraEndTime = nakshetraVal.le_nextValEndTime;
            long nextSunRiseCalMill = nextSunRiseCal.getTimeInMillis();
            long currTithiEndTimeMill = currTithiEndTime.getTimeInMillis();
            long nextTithiEndTimeMill = nextTithiEndTime != null ? nextTithiEndTime.getTimeInMillis() : 0L;
            // long nextTithiEndTimeMill;
            // if(nextTithiEndTime!=null)
            //    nextTithiEndTimeMill = nextTithiEndTime.getTimeInMillis();


            long currNakshtraEndTimeMill = currNakshtraEndTime.getTimeInMillis();
            long nextNakshtraEndTimeMill = nextNakshtraEndTime != null ? nextNakshtraEndTime.getTimeInMillis() : 0L;
            Calendar currPuskarEndTime = null, nextPuskarEndTime = null,nextToNextPuskarEndTime=null;

            if (bWeekDay && bTithi && bNakshetra) {
                puskar = 3;
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currNakshtraEndTime;
                    nextToNextPuskar = 1;

                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 1;

                } else if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 2;
                }

                /*
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currNakshtraEndTime;
                    nextToNextPuskar = 1;

                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 1;

                } else if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 2;
                }*/

            } else if (bWeekDay && bTithiNext && bNakshetraNext) {
                puskar = 1;
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (nextTithiEndTimeMill > nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currNakshtraEndTime;
                    nextToNextPuskar = 3;

                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (nextNakshtraEndTimeMill > nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 3;

                }

            } else if (bWeekDay && bTithiNext && bNakshetra) {
                puskar = 2;
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;

                    nextPuskar = 3;
                    if (nextTithiEndTimeMill > currNakshtraEndTimeMill) {
                        nextPuskarEndTime = currNakshtraEndTime;
                        nextToNextPuskar = 2;
                        nextToNextPuskarEndTime=nextTithiEndTime;
                    }
                    else {
                        nextPuskarEndTime = nextTithiEndTime;
                        nextToNextPuskar = 2;
                        nextToNextPuskarEndTime=currNakshtraEndTime;
                    }
                    nextToNextNextPuskar = 1;





                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 2;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 1;

                }

            } else if (bWeekDay && bTithi && bNakshetraNext) {
                puskar = 2;
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 1;
                    nextPuskarEndTime = currNakshtraEndTime;
                    nextToNextPuskar = 2;
                } else if ((currTithiEndTimeMill > currNakshtraEndTimeMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 3;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 2;

                }

            } else if (bTithi && bNakshetra) {
                puskar = 2;
                if ((currTithiEndTimeMill < currNakshtraEndTimeMill) && (currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 1;
                    nextPuskarEndTime = currNakshtraEndTime;
                    nextToNextPuskar = 0;

                } else if ((currNakshtraEndTimeMill < currTithiEndTimeMill) && (currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 1;
                    nextPuskarEndTime = currTithiEndTime;
                    nextToNextPuskar = 0;
                }


            } else if (bWeekDay && bTithi) {
                puskar = 2;
                if ((currTithiEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 1;
                }

            }
            else if (bWeekDay && bNakshetra) {
                puskar = 2;
                if ((currNakshtraEndTimeMill < nextSunRiseCalMill)) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 1;
                }
            } else if (bWeekDay && bTithiNext) {
                puskar = 1;
                currPuskarEndTime = currTithiEndTime;
                nextPuskar = 2;


                if ((nextTithiEndTimeMill < nextSunRiseCalMill)) {
                    nextPuskarEndTime = nextTithiEndTime;
                    nextToNextPuskar = 1;
                }


            } else if (bWeekDay && bNakshetraNext) {
                puskar = 1;
                currPuskarEndTime = currNakshtraEndTime;
                nextPuskar = 2;
            } else if (bWeekDay && bTithiNextToNext) {
                puskar = 1;
                currPuskarEndTime = nextTithiEndTime;
                nextPuskar = 2;

            } else if (bNakshetraNext && bTithiNext) {

                puskar = 0;
                currPuskarEndTime = (currNakshtraEndTimeMill < currTithiEndTimeMill) ? currNakshtraEndTime : currTithiEndTime;
                nextPuskar = 1;
                nextPuskarEndTime = (currNakshtraEndTimeMill < currTithiEndTimeMill) ? currTithiEndTime : currNakshtraEndTime;
                nextToNextPuskar = 2;

              /*  if ((nextNakshtraEndTimeMill < nextTithiEndTimeMill)  && (nextNakshtraEndTimeMill < nextSunRiseCalMill) ){
                    nextPuskar = 1;
                    nextPuskarEndTime = nextNakshtraEndTime;
                }else if ((nextTithiEndTimeMill < nextNakshtraEndTimeMill)  && (nextTithiEndTimeMill < nextSunRiseCalMill) ){
                    nextPuskar = 1;
                    nextPuskarEndTime = nextTithiEndTime;
                }
                else if ((nextTithiEndTimeMill < nextNakshtraEndTimeMill)  && (nextTithiEndTimeMill < currNakshtraEndTimeMill) ){
                    nextPuskar = 1;

                    nextPuskarEndTime = (currTithiEndTimeMill>currNakshtraEndTimeMill)?currTithiEndTime:currNakshtraEndTime;
                }
                else {
                    nextPuskar = 1;
                    nextPuskarEndTime = nextTithiEndTime;
                }

                nextToNextPuskar = 2;*/

            } else if (bNakshetraNext && bTithiNextToNext) {

                puskar = 0;
                currPuskarEndTime = (currNakshtraEndTimeMill < nextTithiEndTimeMill) ? currNakshtraEndTime : nextTithiEndTime;
                nextPuskar = 1;

                nextPuskarEndTime = (nextNakshtraEndTimeMill < nextTithiEndTimeMill) ? nextNakshtraEndTime : nextTithiEndTime;

                nextToNextPuskar = 2;

                //  nextPuskarEndTime = (nextNakshtraEndTimeMill< nextTithiEndTimeMill)?nextNakshtraEndTime:nextTithiEndTime;


            } else if (bWeekDay && bNakshetraNextToNext) {
                puskar = 1;
                currPuskarEndTime = nextNakshtraEndTime;
                nextPuskar = 2;

            } else if (bTithi || bNakshetra) {
                puskar = 1;

                if (bTithi && bNakshetraNext) {
                    if (currTithiEndTimeMill < currNakshtraEndTimeMill) {
                        currPuskarEndTime = currTithiEndTime;
                        nextPuskar = 0;
                        nextPuskarEndTime = currNakshtraEndTime;
                        nextToNextPuskar = 1;
                    } else if (currNakshtraEndTimeMill < currTithiEndTimeMill) {
                        currPuskarEndTime = currNakshtraEndTime;
                        nextPuskar = 2;
                        nextPuskarEndTime = currTithiEndTime;
                        nextToNextPuskar = 1;
                    } else {
                        currPuskarEndTime = currTithiEndTime;
                        nextPuskar = 2;
                    }
                } else if (bNakshetra && bTithiNext) {
                    if (currTithiEndTimeMill < currNakshtraEndTimeMill) {
                        currPuskarEndTime = currTithiEndTime;
                        nextPuskar = 2;
                        if (nextTithiEndTimeMill < currNakshtraEndTimeMill) {
                            nextPuskarEndTime = nextTithiEndTime;
                            nextToNextPuskar = 1;
                        } else {
                            nextPuskarEndTime = currNakshtraEndTime;
                            nextToNextPuskar = 1;
                        }

                    } else if (currNakshtraEndTimeMill < currTithiEndTimeMill) {
                        currPuskarEndTime = currNakshtraEndTime;
                        nextPuskar = 1;
                    } else {
                        currPuskarEndTime = currNakshtraEndTime;
                        nextPuskar = 2;
                    }
                } else if (bNakshetra) {
                    currPuskarEndTime = currNakshtraEndTime;
                    nextPuskar = 0;
                } else if (bTithi) {
                    currPuskarEndTime = currTithiEndTime;
                    nextPuskar = 0;
                }

            } else if (bNakshetraNext) {
                currPuskarEndTime = currNakshtraEndTime;
                nextPuskar = 1;

            } else if (bTithiNext) {
                currPuskarEndTime = currTithiEndTime;
                nextPuskar = 1;

            } else if (bTithiNextToNext) {
                currPuskarEndTime = nextTithiEndTime;
                nextPuskar = 1;

            } else if (bNakshetraNextToNext) {
                currPuskarEndTime = nextNakshtraEndTime;
                nextPuskar = 1;

            } else if (bWeekDay) {
                puskar = 1;

            }
            Panchanga obj = new Panchanga();
            obj.currVal = puskar;
            obj.nextVal = nextPuskar;
            obj.nextToNextVal = nextToNextPuskar;
            obj.nextToNextNextVal=nextToNextNextPuskar;

            obj.currValEndTime = currPuskarEndTime;
            obj.le_nextValEndTime = nextPuskarEndTime;
            obj.nextToNextValEndTime = nextToNextPuskarEndTime;

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            Panchanga obj = new Panchanga();
            obj.currVal = 0;
            obj.nextVal = 0;
            obj.nextToNextVal = 0;
            obj.currValEndTime = null;
            obj.le_nextValEndTime = null;
            return obj;
        }
    }

    public StartEndTime getVaraBela(int weekDay, double sunrise, double dayduration) {
        StartEndTime startEnd;
        double varabelaStart1 = 0;
        double varabelaGap = dayduration / 8;
        switch (weekDay) {
            case 1:
                varabelaStart1 = sunrise + varabelaGap * 3;
                break;
            case 2:
                varabelaStart1 = sunrise + varabelaGap * 6;
                break;
            case 3:
                varabelaStart1 = sunrise + varabelaGap * 1;
                break;
            case 4:
                varabelaStart1 = sunrise + varabelaGap * 4;
                break;
            case 5:
                varabelaStart1 = sunrise + varabelaGap * 7;
                break;
            case 6:
                varabelaStart1 = sunrise + varabelaGap * 2;
                break;
            case 7:
                varabelaStart1 = sunrise + varabelaGap * 5;
                break;

        }
        startEnd = new StartEndTime();
        Calendar startCal = getCalendar();
        startCal.add(Calendar.MINUTE, convertHourToMinute(varabelaStart1));
        startEnd.startTime = startCal;

        Calendar endCal = getCalendar();
        endCal.add(Calendar.MINUTE, convertHourToMinute((varabelaStart1 + varabelaGap)));
        startEnd.endTime = endCal;


        return startEnd;

    }

    public StartEndTime getKalaBela(int weekDay, double sunrise, double dayduration) {
        StartEndTime startEnd;
        double varabelaStart1 = 0;
        double varabelaGap = dayduration / 8;
        switch (weekDay) {
            case 1:
                varabelaStart1 = sunrise + varabelaGap * 4;
                break;
            case 2:
                varabelaStart1 = sunrise + varabelaGap * 1;
                break;
            case 3:
                varabelaStart1 = sunrise + varabelaGap * 5;
                break;
            case 4:
                varabelaStart1 = sunrise + varabelaGap * 2;
                break;
            case 5:
                varabelaStart1 = sunrise + varabelaGap * 6;
                break;
            case 6:
                varabelaStart1 = sunrise + varabelaGap * 3;
                break;
            case 7:
                varabelaStart1 = sunrise + varabelaGap * 0;
                break;

        }
        startEnd = new StartEndTime();
        Calendar startCal = getCalendar();
        startCal.add(Calendar.MINUTE, convertHourToMinute(varabelaStart1));
        startEnd.startTime = startCal;

        Calendar endCal = getCalendar();
        endCal.add(Calendar.MINUTE, convertHourToMinute((varabelaStart1 + varabelaGap)));
        startEnd.endTime = endCal;


        return startEnd;

    }


    public ArrayList<StartEndTime> getKalaRatri(int weekDay, double sunset, double nightDuration) {
        StartEndTime startEnd;
        ArrayList<StartEndTime> arrayList = new ArrayList<>();
        double kalaRatriStart1 = 0, kalaRatriStart2 = 0;
        double kalaRatriGap = nightDuration / 8;
        switch (weekDay) {
            case 1:
                kalaRatriStart1 = sunset + kalaRatriGap * 5;
                break;
            case 2:
                kalaRatriStart1 = sunset + kalaRatriGap * 3;
                break;
            case 3:
                kalaRatriStart1 = sunset + kalaRatriGap * 1;
                break;
            case 4:
                kalaRatriStart1 = sunset + kalaRatriGap * 6;
                break;
            case 5:
                kalaRatriStart1 = sunset + kalaRatriGap * 4;
                break;
            case 6:
                kalaRatriStart1 = sunset + kalaRatriGap * 2;
                break;
            case 7:
                kalaRatriStart1 = sunset + kalaRatriGap * 0;
                kalaRatriStart2 = sunset + kalaRatriGap * 7;
                break;

        }
        startEnd = new StartEndTime();
        Calendar startCal = getCalendar();
        startCal.add(Calendar.MINUTE, convertHourToMinute(kalaRatriStart1));
        startEnd.startTime = startCal;

        Calendar endCal = getCalendar();
        endCal.add(Calendar.MINUTE, convertHourToMinute((kalaRatriStart1 + kalaRatriGap)));
        startEnd.endTime = endCal;

        arrayList.add(startEnd);
        if (kalaRatriStart2 != 0) {
            startEnd = new StartEndTime();
            startCal = getCalendar();
            startCal.add(Calendar.MINUTE, convertHourToMinute(kalaRatriStart2));
            startEnd.startTime = startCal;

            endCal = getCalendar();
            endCal.add(Calendar.MINUTE, convertHourToMinute((kalaRatriStart2 + kalaRatriGap)));
            startEnd.endTime = endCal;

            arrayList.add(startEnd);
        }

        return arrayList;

    }

}
