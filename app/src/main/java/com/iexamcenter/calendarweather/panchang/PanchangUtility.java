package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.SunMoonCalculator;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PanchangUtility {

    private Calendar todayCal;
    private String[] le_arr_tithi;
    private String[] le_arr_rasi_kundali;
    private String[] le_arr_month;

    Context mContext;

    private Resources mRes;
    private int mCalType;

    private String le_nasti, mLang;
    private String le_shraddha, le_pada;
    private String le_shraddha_parban;
    private String le_shraddha_eko;
    private String le_shraddha_ra;

    private String le_jogin_help, le_time_to, le_time_next;
    private String[] le_arr_jogini, le_arr_food, le_arr_raja_darsan;
    private String[] le_arr_nakshatra;
    private String prevShraddha = "";
    String le_time_min, le_time_hour, le_uayana, le_dayana;
    String[] le_arr_karana, le_arr_joga, le_arr_pakshya, le_arr_bara, le_arr_masa, le_arr_ritu;
    int mType;

    public PanchangUtility() {

    }


    public MyPanchang getMyPunchang(CoreDataHelper coreDataObj, String lang, int calType, String latStr, String lngStr, Context context, int year, int month, int day) {
        try {

            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            MyPanchang myPanchangObj = new MyPanchang();
            todayCal = Calendar.getInstance();
            todayCal.set(Calendar.YEAR, year);
            todayCal.set(Calendar.MONTH, month);
            todayCal.set(Calendar.DAY_OF_MONTH, day);
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 1);


            mLang = lang;
            mCalType = calType;
            mContext = context;
            mRes = mContext.getResources();

            if (mType == 0) {
                le_uayana = mContext.getResources().getString(R.string.l_uayan);
                le_dayana = mContext.getResources().getString(R.string.l_dayan);
                le_time_next = mContext.getResources().getString(R.string.l_time_next);
                le_jogin_help = mContext.getResources().getString(R.string.l_jogin_help);
                le_time_to = mContext.getResources().getString(R.string.l_time_to);
                le_pada = mContext.getResources().getString(R.string.l_pada);
                le_shraddha = mContext.getResources().getString(R.string.l_shraddha);
                le_shraddha_parban = mContext.getResources().getString(R.string.l_shraddha_parban);
                le_shraddha_eko = mContext.getResources().getString(R.string.l_shraddha_eko);
                le_shraddha_ra = mContext.getResources().getString(R.string.l_shraddha_ra);
                le_nasti = mContext.getResources().getString(R.string.l_nasti);
                le_arr_jogini = mContext.getResources().getStringArray(R.array.l_arr_jogini);
                le_arr_food = mContext.getResources().getStringArray(R.array.l_arr_food);
                le_arr_raja_darsan = mContext.getResources().getStringArray(R.array.l_arr_raja_darsan);
                le_arr_tithi = mContext.getResources().getStringArray(R.array.l_arr_tithi);
                le_arr_karana = mContext.getResources().getStringArray(R.array.l_arr_karana);
                le_arr_nakshatra = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
                le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
                le_arr_joga = mContext.getResources().getStringArray(R.array.l_arr_joga);
                le_arr_pakshya = mContext.getResources().getStringArray(R.array.l_arr_paksha);
                le_arr_bara = mContext.getResources().getStringArray(R.array.l_arr_bara);
                le_arr_month = mContext.getResources().getStringArray(R.array.l_arr_month);
                le_arr_masa = mContext.getResources().getStringArray(R.array.l_arr_masa);
                le_arr_ritu = mContext.getResources().getStringArray(R.array.l_arr_ritu);
                le_time_hour = mContext.getResources().getString(R.string.l_time_hour);
                le_time_min = mContext.getResources().getString(R.string.l_time_min);
            } else {
                le_uayana = mContext.getResources().getString(R.string.e_uayan);
                le_dayana = mContext.getResources().getString(R.string.e_dayan);
                le_time_next = mContext.getResources().getString(R.string.e_time_next);
                le_jogin_help = mContext.getResources().getString(R.string.e_jogin_help);
                le_time_to = mContext.getResources().getString(R.string.e_time_to);
                le_pada = mContext.getResources().getString(R.string.e_pada);
                le_shraddha = mContext.getResources().getString(R.string.e_shraddha);
                le_shraddha_parban = mContext.getResources().getString(R.string.e_shraddha_parban);
                le_shraddha_eko = mContext.getResources().getString(R.string.e_shraddha_eko);
                le_shraddha_ra = mContext.getResources().getString(R.string.e_shraddha_ra);
                le_nasti = mContext.getResources().getString(R.string.e_nasti);
                le_arr_jogini = mContext.getResources().getStringArray(R.array.e_arr_jogini);
                le_arr_food = mContext.getResources().getStringArray(R.array.e_arr_food);
                le_arr_raja_darsan = mContext.getResources().getStringArray(R.array.e_arr_raja_darsan);
                le_arr_tithi = mContext.getResources().getStringArray(R.array.e_arr_tithi);
                le_arr_karana = mContext.getResources().getStringArray(R.array.e_arr_karana);
                le_arr_nakshatra = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
                le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
                le_arr_joga = mContext.getResources().getStringArray(R.array.e_arr_joga);
                le_arr_pakshya = mContext.getResources().getStringArray(R.array.e_arr_paksha);
                le_arr_bara = mContext.getResources().getStringArray(R.array.e_arr_bara);
                le_arr_month = mContext.getResources().getStringArray(R.array.e_arr_month);
                le_arr_masa = mContext.getResources().getStringArray(R.array.e_arr_masa);
                le_arr_ritu = mContext.getResources().getStringArray(R.array.e_arr_ritu);
                le_time_hour = mContext.getResources().getString(R.string.e_time_hour);
                le_time_min = mContext.getResources().getString(R.string.e_time_min);
            }


            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);

            double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;


            int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);
            myPanchangObj.le_day = "" + day;
            myPanchangObj.dayOfWeek = dayOfWeek;
            myPanchangObj.le_month = le_arr_month[month];
            myPanchangObj.le_year = "" + year;


            myPanchangObj.le_bara = le_arr_bara[dayOfWeek - 1];
            int sunriseMin;
            int hr;
            int min;
            double dayDurationInHr, nightDurationInHr;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 1, 0, 0, obsLon, obsLat);
            smc.calcSunAndMoon();

            Calendar sunRiseCal = SunMoonCalculator.getSunRiseDate(smc.sunRise, year, month + 1, day);
            Calendar sunSetCal = SunMoonCalculator.getDateAsDate(smc.sunSet);


            Calendar sunNoonCal = SunMoonCalculator.getDateAsDate(smc.sunTransit);
            Calendar moonRiseCal = SunMoonCalculator.getDateAsDate(smc.moonRise);

            Calendar moonSetCal = SunMoonCalculator.getDateAsDate(smc.moonSet);

            if (moonRiseCal != null) {
                myPanchangObj.le_moonRise = getFormattedDate(moonRiseCal);
            } else {
                myPanchangObj.le_moonRise = "-";
            }


            if (moonSetCal != null) {

                myPanchangObj.le_moonSet = getFormattedDate(moonSetCal);
            } else {
                myPanchangObj.le_moonSet = "-";
            }

            long dayTime = Math.abs(sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis()) / 1000;

            long nightTime = (24 * 60 * 60) - dayTime;
            dayDurationInHr = (dayTime / (60 * 60.0));
            nightDurationInHr = (nightTime / (60 * 60.0));
            long hinduMidNight = (sunSetCal.getTimeInMillis() / 1000) + (nightTime / 2);

            Calendar midNightCal = Calendar.getInstance();
            midNightCal.setTimeInMillis(hinduMidNight * 1000);

            myPanchangObj.le_midNight = getFormattedDate(midNightCal);


            myPanchangObj.le_dayLength = timeConversion(dayTime);
            myPanchangObj.le_nightLength = timeConversion(nightTime);

            hr = sunRiseCal.get(Calendar.HOUR_OF_DAY);
            min = sunRiseCal.get(Calendar.MINUTE);

            sunriseMin = (hr * 60) + min;
            myPanchangObj.le_sunTransit = getFormattedDate(sunNoonCal);
            myPanchangObj.le_sunRise = getFormattedDate(sunRiseCal);
            myPanchangObj.le_sunSet = getFormattedDate(sunSetCal);
            CoreDataHelper.Panchanga tithiObj = coreDataObj.getTithi();
            CoreDataHelper.Panchanga sunSignObj = coreDataObj.getSunSign();
            CoreDataHelper.Panchanga nakshetraObj = coreDataObj.getNakshetra();


            CoreDataHelper.Panchanga moonSignObj = coreDataObj.getMoonSign();
            CoreDataHelper.Panchanga jogaObj = coreDataObj.getJoga();
            CoreDataHelper.Karana karanaObj = coreDataObj.getKarana();

            CoreDataHelper.StartEndTime rahuBela = coreDataObj.getRahuKalam(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            CoreDataHelper.StartEndTime gulikaBela = coreDataObj.getGulikaKalam(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            CoreDataHelper.StartEndTime yamaBela = coreDataObj.getYamaKalam(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            ArrayList<CoreDataHelper.StartEndTime> durMuhurtamBela = coreDataObj.getDurmuhurtam(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            ArrayList<CoreDataHelper.StartEndTime> kalaRatriBela = coreDataObj.getKalaRatri(dayOfWeek, (sunriseMin / 60.0) + dayDurationInHr, nightDurationInHr);

            CoreDataHelper.StartEndTime varaBela = coreDataObj.getVaraBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            CoreDataHelper.StartEndTime kalaBela = coreDataObj.getKalaBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);

            CoreDataHelper.StartEndTime abhijitBela = coreDataObj.getAbhijitMuhurta(dayDurationInHr, sunNoonCal);
            CoreDataHelper.StartEndTime brahmaBela = coreDataObj.getBrahmaMuhurta(sunRiseCal);
            CoreDataHelper.Panchanga puskarObj = coreDataObj.getPuskar(dayOfWeek, coreDataObj.getTithi(), coreDataObj.getNakshetra(), sunRiseCal);

            myPanchangObj.le_tarashuddhi = getTaraShuddhi(nakshetraObj);

            myPanchangObj.le_chandrashuddhi = getChandraShuddhi(moonSignObj);
            String currPuskar;
            String nextPuskar = "", nextToNextPuskar = "", nextToNextNextPuskar = "";
            String currPuskarDate = "", nextPuskarDate = "", nextNextPuskarDate = "";

            if (puskarObj.currVal != 0) {

                currPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.currVal) + le_pada;

                if (puskarObj.currValEndTime != null) {
                    currPuskarDate = getFormattedDate(puskarObj.currValEndTime);

                }


            } else {
                currPuskar = le_nasti;

            }
            if (puskarObj.nextToNextNextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                nextNextPuskarDate = getFormattedDate(puskarObj.nextToNextValEndTime);

                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + le_pada;

                nextToNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextVal) + le_pada;

                nextToNextNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextNextVal) + le_pada;


                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.le_puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.le_puskara[2] = setMySubPanchang(nextToNextPuskar, nextNextPuskarDate);

                myPanchangObj.le_puskara[3] = setMySubPanchang(nextToNextNextPuskar, "");


            } else if (puskarObj.nextToNextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + le_pada;

                nextToNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextVal) + le_pada;

                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.le_puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.le_puskara[2] = setMySubPanchang(nextToNextPuskar, "");

            } else if (puskarObj.nextVal != 0 && puskarObj.le_nextValEndTime != null) {

                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + le_pada;

                nextToNextPuskar = le_nasti;

                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.le_puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.le_puskara[2] = setMySubPanchang(nextToNextPuskar, "");

            } else if (puskarObj.nextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + le_pada;

                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.le_puskara[1] = setMySubPanchang(nextPuskar, "");

            } else if (puskarObj.currVal != 0 && !currPuskarDate.isEmpty()) {
                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);

            } else if (puskarObj.currVal != 0) {
                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, "");

            } else if (puskarObj.nextVal == 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + le_pada;

                myPanchangObj.le_puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.le_puskara[1] = setMySubPanchang(nextPuskar, "");

            }

            ArrayList<CoreDataHelper.StartEndTime> amritaBela = coreDataObj.getAmritaBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            ArrayList<CoreDataHelper.StartEndTime> mahendraBela = coreDataObj.getMahendraBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            for (int i = 0; i < amritaBela.size(); i++) {
                String startTime = getFormattedDate(amritaBela.get(i).startTime);
                String endTime = getFormattedDate(amritaBela.get(i).endTime);
                myPanchangObj.le_amritaBelaList[i] = setMyBela(startTime, endTime);
            }
            for (int i = 0; i < mahendraBela.size(); i++) {
                String startTime = getFormattedDate(mahendraBela.get(i).startTime);
                String endTime = getFormattedDate(mahendraBela.get(i).endTime);
                myPanchangObj.le_mahendraBelaList[i] = setMyBela(startTime, endTime);
            }

            for (int i = 0; i < durMuhurtamBela.size(); i++) {
                String startTime = getFormattedDate(durMuhurtamBela.get(i).startTime);
                String endTime = getFormattedDate(durMuhurtamBela.get(i).endTime);
                myPanchangObj.le_durMuhurtaBela[i] = setMyBela(startTime, endTime);
            }
            for (int i = 0; i < kalaRatriBela.size(); i++) {
                String startTime = getFormattedDate(kalaRatriBela.get(i).startTime);
                String endTime = getFormattedDate(kalaRatriBela.get(i).endTime);
                myPanchangObj.le_kalaRatriBela[i] = setMyBela(startTime, endTime);
            }

            myPanchangObj.le_varaBela[0] = setMyBela(getFormattedDate(varaBela.startTime), getFormattedDate(varaBela.endTime));
            myPanchangObj.le_kalaBela[0] = setMyBela(getFormattedDate(kalaBela.startTime), getFormattedDate(kalaBela.endTime));


            myPanchangObj.le_abhijitBela[0] = setMyBela(getFormattedDate(abhijitBela.startTime), getFormattedDate(abhijitBela.endTime));
            myPanchangObj.le_brahmaBela[0] = setMyBela(getFormattedDate(brahmaBela.startTime), getFormattedDate(brahmaBela.endTime));


            myPanchangObj.le_rahuBela[0] = setMyBela(getFormattedDate(rahuBela.startTime), getFormattedDate(rahuBela.endTime));

            myPanchangObj.le_gulikaBela[0] = setMyBela(getFormattedDate(gulikaBela.startTime), getFormattedDate(gulikaBela.endTime));
            myPanchangObj.le_yamaBela[0] = setMyBela(getFormattedDate(yamaBela.startTime), getFormattedDate(yamaBela.endTime));
            ArrayList<CoreDataHelper.StartEndTime> varjyamStartEndTimeList = coreDataObj.getVarjyamStartEndTimeList();

            for (int i = 0; i < varjyamStartEndTimeList.size(); i++) {
                String startTimeVarjyam = getFormattedDate(varjyamStartEndTimeList.get(i).startTime);
                String endTimeVarjyam = getFormattedDate(varjyamStartEndTimeList.get(i).endTime);
                myPanchangObj.le_varjyamBela[i] = setMyBela(startTimeVarjyam, endTimeVarjyam);

            }

            int paksha = coreDataObj.getPaksha();
            myPanchangObj.le_paksha = le_arr_pakshya[paksha];
            myPanchangObj.auspKey = getAuspKeyDate(sunRiseCal);
            myPanchangObj.pakshaIndex = paksha;


            String currTithi = le_arr_tithi[tithiObj.currVal - 1];
            myPanchangObj.currTithiIndex = tithiObj.currVal - 1;
            myPanchangObj.currTithi = le_arr_tithi[tithiObj.currVal - 1];
            String nextTithi = "", nextToNextTithi = "";
            String currTithiDate = "", nextTithiDate = "";
            if (tithiObj.nextVal != 0) {
                myPanchangObj.nextTithiIndex = tithiObj.nextVal - 1;
                currTithiDate = getFormattedDate(tithiObj.currValEndTime);
                nextTithi = le_arr_tithi[tithiObj.nextVal - 1];

            }


            if (tithiObj.nextToNextVal != 0) {

                myPanchangObj.nextToNextTithiIndex = tithiObj.nextToNextVal - 1;
                nextTithiDate = getFormattedDate(tithiObj.le_nextValEndTime);

                nextToNextTithi = le_arr_tithi[tithiObj.nextToNextVal - 1];

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
            myPanchangObj.le_tithi[0] = setMySubPanchang(currTithi, currTithiDate);
            myPanchangObj.le_tithi[1] = setMySubPanchang(nextTithi, nextTithiDate);
            myPanchangObj.le_tithi[2] = setMySubPanchang(nextToNextTithi, "");

            String currJogini = getJogini(tithiObj.currVal - 1, 0);

            String nextJogini = getJogini(tithiObj.nextVal - 1, 0);

            String nextToNextJogini = getJogini(tithiObj.nextToNextVal - 1, 0);

            String currJoginiYatra = getJoginiYatra(tithiObj.currVal - 1, 0);
            if (!currJoginiYatra.isEmpty())
                currJoginiYatra = currJoginiYatra + " " + le_jogin_help;

            String nextJoginiYatra = getJoginiYatra(tithiObj.nextVal - 1, 0);
            if (!nextJoginiYatra.isEmpty())
                nextJoginiYatra = nextJoginiYatra + " " + le_jogin_help;


            String nextToNextJoginiYatra = getJoginiYatra(tithiObj.nextToNextVal - 1, 0);
            if (!nextToNextJoginiYatra.isEmpty())
                nextToNextJoginiYatra = nextToNextJoginiYatra + " " + le_jogin_help;


            myPanchangObj.le_jogini[0] = setMySubPanchang(currJogini, currTithiDate);
            myPanchangObj.le_jogini[1] = setMySubPanchang(nextJogini, nextTithiDate);
            myPanchangObj.le_jogini[2] = setMySubPanchang(nextToNextJogini, "");

            myPanchangObj.le_joginiYatra[0] = setMySubPanchang(currJoginiYatra, currTithiDate);
            myPanchangObj.le_joginiYatra[1] = setMySubPanchang(nextJoginiYatra, nextTithiDate);
            myPanchangObj.le_joginiYatra[2] = setMySubPanchang(nextToNextJoginiYatra, "");


            boolean isSnkrant = coreDataObj.getSolarDayVal() == 1;

            String currFood = getFoodProhibited(tithiObj.currVal - 1, 0, isSnkrant);

            String nextFood = getFoodProhibited(tithiObj.nextVal - 1, 0, isSnkrant);

            String nextToNextFood = getFoodProhibited(tithiObj.nextToNextVal - 1, 0, isSnkrant);

            myPanchangObj.le_rajaDarsan = getRajaDarsan(nakshetraObj, 0);


            myPanchangObj.le_food[0] = setMySubPanchang(currFood, currTithiDate);
            myPanchangObj.le_food[1] = setMySubPanchang(nextFood, nextTithiDate);
            myPanchangObj.le_food[2] = setMySubPanchang(nextToNextFood, "");


            myPanchangObj.le_ghataBar = getGhataBara(dayOfWeek, 0);

            String shraddhaTmp = getShraddha(tithiObj, sunRiseCal, sunSetCal, sunNoonCal);


            myPanchangObj.le_shraddha = shraddhaTmp.trim();

            int lunarMonthPurnimantIndex = coreDataObj.getLunarMonthPurnimantIndex();
            Log.e("myCoreData",":myCoreData:x:xx:x:x:X"+lunarMonthPurnimantIndex);
            int lunarMonthAmantIndex = coreDataObj.getLunarMonthAmantIndex();
            int newMoonLunarDay = coreDataObj.getNewMoonLunarDay();
            int fullMoonLunarDay = coreDataObj.getFullMoonLunarDay();
            int lunarMonthType = coreDataObj.getmLunarMonthType();
            myPanchangObj.planetInfo = coreDataObj.getPlanetInfo();


            if (lang.contains("te") || lang.contains("kn") || lang.contains("mr") || lang.contains("gu")) {

                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthAmantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;

            } else if (lang.contains("or") || lang.contains("hi") || lang.contains("en")) {
                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + fullMoonLunarDay);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[lunarMonthAmantIndex];
                Log.e("myCoreData",":myCoreData:x:xx:x:x:"+lunarMonthPurnimantIndex+"::"+le_arr_masa[lunarMonthPurnimantIndex]);
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[lunarMonthPurnimantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthPurnimantIndex;
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;

            }

            String sakaddha = "" + getSakaddha(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya, myPanchangObj.pakshaIndex);
            String samvata = "" + getSamvata(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya, myPanchangObj.pakshaIndex);


            myPanchangObj.le_sakaddha = Utility.getInstance(mContext).getDayNo(sakaddha);
            myPanchangObj.le_samvata = Utility.getInstance(mContext).getDayNo(samvata);

            String sanSala = "" + getSanSal(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya);

            myPanchangObj.le_sanSal = Utility.getInstance(mContext).getDayNo(sanSala);

            String sanSalaAnka = "" + getSanSalAnka(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya);

            myPanchangObj.le_sanSalAnka = sanSalaAnka.equalsIgnoreCase("0") ? sanSalaAnka : Utility.getInstance(mContext).getDayNo(sanSalaAnka);
            myPanchangObj.le_sanSalAnka = sanSalaAnka.contains("0") ? sanSalaAnka : Utility.getInstance(mContext).getDayNo(sanSalaAnka);


            String k1 = le_arr_karana[karanaObj.val1 - 1];
            String k2 = "", k3 = "", k4 = "", k5 = "";
            String k1d = "", k2d = "", k3d = "", k4d = "", k5d = "";

            if (karanaObj.val2 != 0) {
                k1d = getFormattedDate(karanaObj.val1ET);
                k2 = le_arr_karana[karanaObj.val2 - 1];
            }
            if (karanaObj.val3 != 0) {
                k2d = getFormattedDate(karanaObj.val2ET);
                k3 = le_arr_karana[karanaObj.val3 - 1];
            }
            if (karanaObj.val4 != 0) {
                k3d = getFormattedDate(karanaObj.val3ET);
                k4 = le_arr_karana[karanaObj.val4 - 1];
            }
            if (karanaObj.val5 != 0) {
                k4d = getFormattedDate(karanaObj.val4ET);
                k5 = le_arr_karana[karanaObj.val5 - 1];
            }
            if (karanaObj.val6 != 0) {
                k5d = getFormattedDate(karanaObj.val5ET);

            }


            myPanchangObj.le_karana[0] = setMySubPanchang(k1, k1d);
            myPanchangObj.le_karana[1] = setMySubPanchang(k2, k2d);
            myPanchangObj.le_karana[2] = setMySubPanchang(k3, k3d);
            myPanchangObj.le_karana[3] = setMySubPanchang(k4, k4d);
            myPanchangObj.le_karana[4] = setMySubPanchang(k5, k5d);

            String currNakshetra = le_arr_nakshatra[nakshetraObj.currVal - 1];
            String nextNakshetra = "", nextToNextNakshetra = "";
            String currNakshetraDate = "", nextNakshetraDate = "";

            if (nakshetraObj.nextVal != 0) {
                currNakshetraDate = getFormattedDate(nakshetraObj.currValEndTime);
                nextNakshetra = le_arr_nakshatra[nakshetraObj.nextVal - 1];
            }
            if (nakshetraObj.nextToNextVal != 0) {
                nextNakshetraDate = getFormattedDate(nakshetraObj.le_nextValEndTime);
                nextToNextNakshetra = le_arr_nakshatra[nakshetraObj.nextToNextVal - 1];
            }
            myPanchangObj.currNakshetraIndex = nakshetraObj.currVal - 1;

            myPanchangObj.le_nakshetra[0] = setMySubPanchang(currNakshetra, currNakshetraDate);
            myPanchangObj.le_nakshetra[1] = setMySubPanchang(nextNakshetra, nextNakshetraDate);
            myPanchangObj.le_nakshetra[2] = setMySubPanchang(nextToNextNakshetra, "");

            String currJoga = le_arr_joga[jogaObj.currVal - 1];
            String nextJoga = "", nextToNextJoga = "";
            String currJogaDate = "", nextJogaDate = "";
            if (jogaObj.nextVal != 0) {
                currJogaDate = getFormattedDate(jogaObj.currValEndTime);
                nextJoga = le_arr_joga[jogaObj.nextVal - 1];

            }
            if (jogaObj.nextToNextVal != 0) {
                nextJogaDate = getFormattedDate(jogaObj.le_nextValEndTime);
                nextToNextJoga = le_arr_joga[jogaObj.nextToNextVal - 1];

            }

            myPanchangObj.le_joga[0] = setMySubPanchang(currJoga, currJogaDate);
            myPanchangObj.le_joga[1] = setMySubPanchang(nextJoga, nextJogaDate);
            myPanchangObj.le_joga[2] = setMySubPanchang(nextToNextJoga, "");


            String currSunSign = le_arr_rasi_kundali[sunSignObj.currVal - 1];
            String nextSunSign = "";
            String currSunSignDate = "", nextSunSignDate = "";

            int adjustSolarMonth = coreDataObj.getAdjustSolarMonth();
            int solarDayVal = coreDataObj.getSolarDayVal();

            long sunsignEnd = sunSignObj.currValEndTime.getTimeInMillis();
            if (sunsignEnd > midNightCal.getTimeInMillis() && (sunsignEnd < midNightCal.getTimeInMillis() + (24 * 60 * 60 * 1000L))) {

                myPanchangObj.masant = true;
            }


            if (adjustSolarMonth >= 3 && adjustSolarMonth < 9) {
                myPanchangObj.le_ayana = le_dayana;
            } else {
                myPanchangObj.le_ayana = le_uayana;
            }

            myPanchangObj.le_sunSign[0] = setMySubPanchang(currSunSign, currSunSignDate);
            myPanchangObj.le_sunSign[1] = setMySubPanchang(nextSunSign, nextSunSignDate);
            myPanchangObj.le_solarDay = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
            myPanchangObj.solarDayIndex = solarDayVal;

            myPanchangObj.le_solarMonth = le_arr_rasi_kundali[adjustSolarMonth - 1];
            myPanchangObj.solarMonthIndex = adjustSolarMonth - 1;

            myPanchangObj.monthIndex = month;


            if (lang.contains("bn") || lang.contains("ta") || lang.contains("pa") || lang.contains("ml")) {

                myPanchangObj.le_lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.le_lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.le_lunarMonthAmant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.le_lunarMonthPurimant = le_arr_masa[adjustSolarMonth - 1];
                myPanchangObj.pLunarMonthIndex = adjustSolarMonth - 1;
                myPanchangObj.aLunarMonthIndex = adjustSolarMonth - 1;

            }


            int codeRitu = (int) Math.floor((adjustSolarMonth - 1) / 2.0);
            myPanchangObj.le_ritu = le_arr_ritu[codeRitu];
            String currGhataChandra, nextGhataChandra = "", nextTonextGhataChandra = "", enextTonextGhataChandra = "";

            String currMoonSign = le_arr_rasi_kundali[moonSignObj.currVal - 1];
            currGhataChandra = getGhataChandra(moonSignObj.currVal, 0);

            String nextMoonSign = "", nextToNextMoonSign = "";
            String currMoonSignDate = "", nextMoonSignDate = "";


            if (moonSignObj.nextVal != 0) {
                if (currGhataChandra.isEmpty()) {
                    currGhataChandra = le_nasti;

                }
                currMoonSignDate = getFormattedDate(moonSignObj.currValEndTime);

                nextMoonSign = le_arr_rasi_kundali[moonSignObj.nextVal - 1];
                nextGhataChandra = getGhataChandra(moonSignObj.nextVal, 0);


            }
            if (moonSignObj.nextToNextVal != 0) {
                nextMoonSignDate = getFormattedDate(moonSignObj.le_nextValEndTime);
                nextToNextMoonSign = le_arr_rasi_kundali[moonSignObj.nextToNextVal - 1];

                nextTonextGhataChandra = getGhataChandra(moonSignObj.nextToNextVal, 0);

            }


            myPanchangObj.le_moonSign[0] = setMySubPanchang(currMoonSign, currMoonSignDate);
            myPanchangObj.le_moonSign[1] = setMySubPanchang(nextMoonSign, nextMoonSignDate);
            myPanchangObj.le_moonSign[2] = setMySubPanchang(nextToNextMoonSign, "");

            myPanchangObj.le_ghataChandra[0] = setMySubPanchang(currGhataChandra, currMoonSignDate);
            myPanchangObj.le_ghataChandra[1] = setMySubPanchang(nextGhataChandra, nextMoonSignDate);
            myPanchangObj.le_ghataChandra[2] = setMySubPanchang(nextTonextGhataChandra, "");
            return myPanchangObj;
        } catch (Exception e) {
            Log.e("myPanchangObj", "myPanchangObj:3:null" + e.getMessage());
            e.printStackTrace();
        }

        Log.e("myPanchangObj", "myPanchangObj:2:null");

        return null;
    }

    private String getRajaDarsan(CoreDataHelper.Panchanga nakshetra, int type) {
        if (nakshetra.currVal < 0)
            return "";
        try {
            if (nakshetra.nextVal != 0) {

                String dateStr = " " + getFormattedDate(nakshetra.currValEndTime) + " " + le_time_to + ", " + le_time_next + " ";
                String edateStr = " " + le_time_to + " " + geteFormattedDate(nakshetra.currValEndTime) + ", " + le_time_next + " ";

                if (type == 0) {
                    return le_arr_raja_darsan[nakshetra.currVal - 1] + dateStr + le_arr_raja_darsan[nakshetra.nextVal - 1];
                } else {
                    return le_arr_raja_darsan[nakshetra.currVal - 1] + edateStr + le_arr_raja_darsan[nakshetra.nextVal - 1];
                }
            } else {
                if (type == 0) {
                    return le_arr_raja_darsan[nakshetra.currVal - 1];
                } else {
                    return le_arr_raja_darsan[nakshetra.currVal - 1];
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getFoodProhibited(int tithi, int type, boolean isSankranti) {
        if (tithi < 0)
            return "";
        try {
            String[] food = le_arr_food;
            if (type == 1) {
                food = le_arr_food;
            }
            String foodStr;
            if (tithi > 14 && tithi != 29) {
                tithi = tithi - 15;
            }

            if (tithi == 29) {
                foodStr = food[14];
            } else {
                foodStr = food[tithi];
            }
            if (isSankranti && tithi != 14 && tithi != 29) {

                foodStr = food[tithi] + " " + food[15];
            }

            return foodStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getChandraShuddhi(CoreDataHelper.Panchanga moonsignObj) {
        int currMoonSign = moonsignObj.currVal;
        String chandrashuddhi = "";
        int dailynak = currMoonSign;
        for (int birthnak = 1; birthnak <= 12; birthnak++) {

            int diff;
            if (birthnak > dailynak) {
                diff = (Math.abs(dailynak + 12 - birthnak) + 1);
            } else {
                diff = (Math.abs(birthnak - dailynak) + 1);
            }

            if (diff == 1 || diff == 3 || diff == 6 || diff == 7 || diff == 10 || diff == 11) {
                //2,4,6,8,9
                chandrashuddhi = chandrashuddhi.concat(le_arr_rasi_kundali[birthnak - 1]).concat(", ");
            }
        }
        chandrashuddhi = chandrashuddhi.trim().replaceAll(",$", "");
        chandrashuddhi = chandrashuddhi + " " + getFormattedDate(moonsignObj.currValEndTime) + " " + le_time_to;

        if (moonsignObj.nextVal != 0) {
            dailynak = moonsignObj.nextVal;
            String chandrashuddhi1 = "";
            for (int birthnak = 1; birthnak <= 12; birthnak++) {

                int diff;
                if (birthnak > dailynak) {
                    diff = (Math.abs(dailynak + 12 - birthnak) + 1);
                } else {
                    diff = (Math.abs(birthnak - dailynak) + 1);
                }

                if (diff == 1 || diff == 3 || diff == 6 || diff == 7 || diff == 10 || diff == 11) {

                    //2,4,6,8,9
                    chandrashuddhi1 = chandrashuddhi1.concat(le_arr_rasi_kundali[birthnak - 1]).concat(", ");
                }
            }
            chandrashuddhi1 = chandrashuddhi1.trim().replaceAll(",$", "");
            chandrashuddhi = chandrashuddhi + " " + le_time_next + " " + chandrashuddhi1;

        }


        return chandrashuddhi;


    }


    private String getTaraShuddhi(CoreDataHelper.Panchanga nakshetraObj) {
        int currNakshetra = nakshetraObj.currVal;
        String nakshetra = "";
        int dailynak = currNakshetra;
        for (int birthnak = 1; birthnak <= 27; birthnak++) {
            int diff;
            if (birthnak > dailynak) {
                diff = (Math.abs(dailynak + 27 - birthnak) + 1) % 9;
            } else {
                diff = (Math.abs(birthnak - dailynak) + 1) % 9;
            }


            if (diff == 2 || diff == 4 || diff == 6 || diff == 8 || diff == 9 || diff == 0) {
                //2,4,6,8,9
                nakshetra = nakshetra.concat(le_arr_nakshatra[birthnak - 1]).concat(", ");
            }
        }


        nakshetra = nakshetra.trim().replaceAll(",$", "");

        nakshetra = nakshetra + " " + getFormattedDate(nakshetraObj.currValEndTime) + " " + le_time_to;

        if (nakshetraObj.nextVal != 0) {
            String nakshetra1 = "";
            dailynak = nakshetraObj.nextVal;
            for (int birthnak = 1; birthnak <= 27; birthnak++) {

                int diff;
                if (birthnak > dailynak) {
                    diff = (Math.abs(dailynak + 27 - birthnak) + 1) % 9;
                } else {
                    diff = (Math.abs(birthnak - dailynak) + 1) % 9;
                }
                if (diff == 2 || diff == 4 || diff == 6 || diff == 8 || diff == 9 || diff == 0) {
                    //2,4,6,8,9
                    nakshetra1 = nakshetra1.concat(le_arr_nakshatra[birthnak - 1]).concat(", ");
                }
            }
            nakshetra1 = nakshetra1.trim().replaceAll(",$", "");

            nakshetra = nakshetra + " " + le_time_next + " " + nakshetra1;


        }


        return nakshetra;


    }

    private String getGhataChandra(int moonsign, int type) {
        String ghataChandra = "";
        String[] rasi = (type == 0) ? le_arr_rasi_kundali : le_arr_rasi_kundali;
        switch (moonsign) {
            case 1:
                ghataChandra = rasi[0];
                break;
            case 2:
                ghataChandra = rasi[7];
                break;
            case 3:
                ghataChandra = rasi[5];
                break;

            case 5:
                ghataChandra = rasi[3] + ", " + rasi[9];
                break;
            case 6:
                ghataChandra = rasi[1];
                break;

            case 9:
                ghataChandra = rasi[6] + ", " + rasi[10];
                break;
            case 10:
                ghataChandra = rasi[4];
                break;
            case 11:
                ghataChandra = rasi[2] + ", " + rasi[11];
                break;
            case 12:
                ghataChandra = rasi[8];
                break;


        }
        return ghataChandra;
    }

    private String getGhataBara(int weekday, int type) {
        String ghataBar = "";
        String[] rasi = (type == 0) ? le_arr_rasi_kundali : le_arr_rasi_kundali;
        switch (weekday) {
            case 1:
                ghataBar = rasi[0];
                break;
            case 2:
                ghataBar = rasi[2];
                break;
            case 3:
                ghataBar = rasi[9];
                break;
            case 4:
                ghataBar = rasi[3];
                break;
            case 5:
                ghataBar = rasi[6] + ", " + rasi[10];
                break;
            case 6:
                ghataBar = rasi[7] + ", " + rasi[8] + ", " + rasi[11];
                break;
            case 7:
                ghataBar = rasi[1] + ", " + rasi[4] + ", " + rasi[5];
                break;

        }
        return ghataBar;
    }

    private String getJogini(int tithi, int type) {
        try {

            String[] jogin = le_arr_jogini;
            if (type == 1) {
                jogin = le_arr_jogini;
            }

            switch (tithi) {
                case 0:
                case 8:
                case 15:
                case 23:
                    return jogin[0];
                case 5:
                case 20:
                case 13:
                case 28:
                    return jogin[4];
                case 4:
                case 19:
                case 12:
                case 27:
                    return jogin[2];
                case 1:
                case 16:
                case 9:
                case 24:
                    return jogin[6];
                case 2:
                case 17:
                case 10:
                case 25:
                    return jogin[1];
                case 6:
                case 21:
                case 14:
                    return jogin[5];
                case 3:
                case 18:
                case 11:
                case 26:
                    return jogin[3];
                case 7:
                case 22:
                case 29:

                    return jogin[7];


            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "-";
        }
    }


    private String getJoginiYatra(int tithi, int type) {
        try {
String str="";
            String[] jogin = le_arr_jogini;
            if (type == 1) {
                jogin = le_arr_jogini;
            }
            switch (tithi) {
                case 0:
                case 8:
                case 15:
                case 23:
                    str=jogin[6] + "," + jogin[0] + "," + jogin[2];
                    break;
                case 5:
                case 20:
                case 13:
                case 28:
                    str= jogin[2] + "," + jogin[4] + "," + jogin[6];
                    break;
                case 4:
                case 19:
                case 12:
                case 27:
                    str= jogin[0] + "," + jogin[2] + "," + jogin[4];
                    break;
                case 1:
                case 16:
                case 9:
                case 24:
                    str= jogin[4] + "," + jogin[6] + "," + jogin[0];
                    break;
                case 2:
                case 17:
                case 10:
                case 25:
                    str= jogin[7] + "," + jogin[1] + "," + jogin[3];
                    break;
                case 6:
                case 21:
                case 14:
                    str= jogin[3] + "," + jogin[5] + "," + jogin[7];
                    break;
                case 3:
                case 18:
                case 11:
                case 26:
                    str= jogin[1] + "," + jogin[3] + "," + jogin[5];
                    break;
                case 7:
                case 22:
                case 29:

                    str= jogin[5] + "," + jogin[7] + "," + jogin[1];
                    break;


            }
            if(!str.isEmpty()) {
                String[] strArr = str.split(",");
                String[] strArr1 = strArr[0].split(" ");
                return str.replace(" " + strArr1[1], "") + " " + strArr1[1];
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "-";
        }
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

    private String timeConversion(long totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        long totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        long hours = totalMinutes / MINUTES_IN_AN_HOUR;


        return Utility.getInstance(mContext).getDayNo("" + hours) + le_time_hour + Utility.getInstance(mContext).getDayNo("" + minutes) + le_time_min;
    }

    private String getFormattedDate(Calendar cal) {
        if (cal == null)
            return "";

        if ((mLang.contains("or") || mLang.contains("hi"))&& mType == 0) {
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

    private int getSanSalAnka(int year, int month, int day, int pLunarMonthIndex, int currTithiIndex, int nextTithiIndex, boolean tithiKhaya) {
        try {

            if (year < 1970) {
                return 0;
            }
            int anka = 0;
            for (int i = 1970; i <= year; i++) {
                anka++;
                if (((("" + i).endsWith("0")) || (("" + i).endsWith("6")) || (("" + i).endsWith("01"))) /* && !((""+i).endsWith("10"))*/) {
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

    private String getShraddha(CoreDataHelper.Panchanga tithiObj, Calendar sunRiseCal, Calendar sunSetCal, Calendar sunNoonCal) {

        String efullTmp = "";
        String eko = "", par = "", full = "";
        try {

            if (tithiObj.currValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                full = le_arr_tithi[tithiObj.currVal - 1];

            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 + (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];

            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 - (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > sunNoonCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000)) {
                eko = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() < sunNoonCal.getTimeInMillis() && tithiObj.le_nextValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                efullTmp = le_arr_tithi[tithiObj.currVal - 1];
                if (prevShraddha.contains(efullTmp)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];

                } else if (tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() - 3 * 60 * 60 * 1000)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 1.5 * 60 * 60 * 1000) {
                    if (tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000)) {
                        eko = le_arr_tithi[tithiObj.currVal - 1];

                        full = le_arr_tithi[tithiObj.nextVal - 1];

                    } else {
                        eko = le_arr_tithi[tithiObj.currVal - 1];
                        par = le_arr_tithi[tithiObj.nextVal - 1];
                    }

                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 2 * 60 * 60 * 1000) {

                    par = le_arr_tithi[tithiObj.nextVal - 1];

                } else {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    eko = le_arr_tithi[tithiObj.currVal - 1];
                }

            }


            prevShraddha = efullTmp;


        } catch (Exception e) {
            e.printStackTrace();
            if (!full.isEmpty()) {
                full = ", " + full + " " + le_shraddha_ra + " " + le_shraddha;
            }
            if (!eko.isEmpty()) {
                eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
            }
            if (!par.isEmpty()) {
                par = ", " + par + " " + le_shraddha_ra + " " + le_shraddha_parban;
            }


            String retval = eko + par + full;
            return retval.replaceAll("^,|,$", "");

        }

        if (!full.isEmpty()) {
            full = ", " + full + " " + le_shraddha_ra + " " + le_shraddha;
        }
        if (!eko.isEmpty()) {
            eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
        }
        if (!par.isEmpty()) {
            par = ", " + par + " " + le_shraddha_ra + " " + le_shraddha_parban;
        }


        String retval = eko + par + full;
        return retval.replaceAll("^,|,$", "");
    }

    public class MyPanchang {
        boolean masant = false, tithiMala = false, tithiKhaya = false, nextNightCover = false, currNightCover = false;
        public int lunarMonthType, currNakshetraIndex = -1, puskar, monthIndex, solarDayIndex, currTithiIndex = -1, dayOfWeek, pLunarMonthIndex, aLunarMonthIndex, solarMonthIndex, nextTithiIndex = -1, nextToNextTithiIndex = -1, pakshaIndex;
        public String le_rajaDarsan, le_samvata, currTithi, le_chandrashuddhi, le_tarashuddhi, auspKey, le_sanSal, le_sakaddha, le_paksha, le_sunRise, le_sunSet, le_moonRise, le_moonSet, le_bara, le_day, le_month, le_year, le_sunTransit, le_midNight, le_dayLength, le_nightLength;
        public MyBela[] le_varjyamBela = new MyBela[4];
        public MyBela[] le_rahuBela = new MyBela[1];
        public MyBela[] le_abhijitBela = new MyBela[1];
        public MyBela[] le_brahmaBela = new MyBela[1];
        public MyBela[] le_varaBela = new MyBela[1];
        public MyBela[] le_kalaBela = new MyBela[1];
        public MyBela[] le_gulikaBela = new MyBela[1];
        public MyBela[] le_yamaBela = new MyBela[1];
        public MyBela[] le_durMuhurtaBela = new MyBela[2];
        public MyBela[] le_kalaRatriBela = new MyBela[2];
        public MySubPanchang[] le_tithi = new MySubPanchang[3];
        public MySubPanchang[] le_puskara = new MySubPanchang[4];
        public MySubPanchang[] le_nakshetra = new MySubPanchang[3];
        public MySubPanchang[] le_joga = new MySubPanchang[3];
        public MySubPanchang[] le_karana = new MySubPanchang[5];
        public MySubPanchang[] le_sunSign = new MySubPanchang[2];
        public MySubPanchang[] le_moonSign = new MySubPanchang[3];
        public String le_lunarDayAmant, le_lunarMonthAmant, le_lunarDayPurimant, le_lunarMonthPurimant, le_solarDay, le_solarMonth;
        public String le_sanSalAnka, eday, emonth;
        public MyBela[] le_amritaBelaList = new MyBela[6];
        public MyBela[] le_mahendraBelaList = new MyBela[6];
        public MySubPanchang[] le_jogini = new MySubPanchang[3];
        public MySubPanchang[] le_joginiYatra = new MySubPanchang[3];
        public MySubPanchang[] le_food = new MySubPanchang[3];
        public MySubPanchang[] le_ghataChandra = new MySubPanchang[3];
        public String le_ghataBar, le_ritu, le_ayana, le_shraddha;
        public EphemerisEntity planetInfo;

    }
    public MyBela setMyBela(String startTime, String endTime) {
        MyBela obj = new MyBela();
        obj.startTime = startTime;
        obj.endTime = endTime;
        return obj;
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
