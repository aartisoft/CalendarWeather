package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.SunMoonCalculator;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PanchangUtilityFeb26 {

    private Calendar todayCal;
    private String[] tithi_arr;
    private String[] rasi_kundali_arr;
    private String[] month_arr;
    private String[] etithi_arr;
    private String[] erasi_kundali_arr;
    Context mContext;

    private Resources mRes;
    private int mCalType;

    private String nasti, mLang;
    private String mshraddha, pada, epada;
    private String mshraddha_parban;
    private String mshraddha_eko;
    private String mshraddha_ra;
    private String emshraddha;
    private String emshraddha_parban;
    private String emshraddha_eko;
    private String jogin_help,ejogin_help,emshraddha_ra, pkey_time_to, epkey_time_to, pkey_time_next, epkey_time_next;
    private String[] e_jogini, l_jogini, e_food, l_food, l_raja_darsan, e_raja_darsan;
    private String[] nakshatra_arr, enakshatra_arr;
    private String prevShraddha = "";

    public PanchangUtilityFeb26() {

    }

    public MyPanchang getMyPunchang(CoreDataHelper coreDataObj, String lang, int calType, String latStr, String lngStr, Context context, int year, int month, int day) {
        try {


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

            // ayana = mContext.getResources().getString(R.string.pkey_ayan);


            String uayana = mContext.getResources().getString(R.string.l_uayan);
            String dayana = mContext.getResources().getString(R.string.l_dayan);

            // eayana = mContext.getResources().getString(R.string.epkey_ayan);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_time_next = mContext.getResources().getString(R.string.e_time_next);
            jogin_help= mContext.getResources().getString(R.string.l_jogin_help);
            ejogin_help= mContext.getResources().getString(R.string.e_jogin_help);

            String euayana = mContext.getResources().getString(R.string.e_uayan);
            String edayana = mContext.getResources().getString(R.string.e_dayan);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            epkey_time_to = mContext.getResources().getString(R.string.e_time_to);
            pada = mContext.getResources().getString(R.string.l_pada);
            epada = mContext.getResources().getString(R.string.e_pada);

            mshraddha = mContext.getResources().getString(R.string.l_shraddha);
            mshraddha_parban = mContext.getResources().getString(R.string.l_shraddha_parban);
            mshraddha_eko = mContext.getResources().getString(R.string.l_shraddha_eko);
            mshraddha_ra = mContext.getResources().getString(R.string.l_shraddha_ra);

            emshraddha = mContext.getResources().getString(R.string.e_shraddha);
            emshraddha_parban = mContext.getResources().getString(R.string.e_shraddha_parban);
            emshraddha_eko = mContext.getResources().getString(R.string.e_shraddha_eko);
            emshraddha_ra = mContext.getResources().getString(R.string.e_shraddha_ra);
            nasti = mContext.getResources().getString(R.string.l_nasti);
            e_jogini = mContext.getResources().getStringArray(R.array.e_arr_jogini);
            l_jogini = mContext.getResources().getStringArray(R.array.l_arr_jogini);
            e_food = mContext.getResources().getStringArray(R.array.e_arr_food);
            l_food = mContext.getResources().getStringArray(R.array.l_arr_food);
            l_raja_darsan = mContext.getResources().getStringArray(R.array.l_arr_raja_darsan);

            e_raja_darsan = mContext.getResources().getStringArray(R.array.e_arr_raja_darsan);

            tithi_arr = mContext.getResources().getStringArray(R.array.l_arr_tithi);
            String[] karana_arr = mContext.getResources().getStringArray(R.array.l_arr_karana);
            nakshatra_arr = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            rasi_kundali_arr = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            String[] joga_arr = mContext.getResources().getStringArray(R.array.l_arr_joga);
            String[] pakshya_arr = mContext.getResources().getStringArray(R.array.l_arr_paksha);
            String[] bara_arr = mContext.getResources().getStringArray(R.array.l_arr_bara);
            month_arr = mContext.getResources().getStringArray(R.array.l_arr_month);
            String[] masa_arr = mContext.getResources().getStringArray(R.array.l_arr_masa);
            String[] ritu_arr = mContext.getResources().getStringArray(R.array.l_arr_ritu);


            etithi_arr = mContext.getResources().getStringArray(R.array.e_arr_tithi);
            String[] ekarana_arr = mContext.getResources().getStringArray(R.array.e_arr_karana);
            enakshatra_arr = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            erasi_kundali_arr = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            String[] ejoga_arr = mContext.getResources().getStringArray(R.array.e_arr_joga);
            String[] epakshya_arr = mContext.getResources().getStringArray(R.array.paksha_arr);
            String[] ebara_arr = mContext.getResources().getStringArray(R.array.e_arr_bara);
            String[] emonth_arr = mContext.getResources().getStringArray(R.array.e_arr_month_short);
            String[] emasa_arr = mContext.getResources().getStringArray(R.array.e_arr_masa);
            String[] eritu_arr = mContext.getResources().getStringArray(R.array.e_arr_ritu);



            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);

            double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;


            int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);
            myPanchangObj.day = myPanchangObj.eday = "" + day;
            myPanchangObj.dayOfWeek = dayOfWeek;
            myPanchangObj.month = month_arr[month];
            myPanchangObj.emonth = emonth_arr[month];
            myPanchangObj.year = myPanchangObj.eyear = "" + year;


            myPanchangObj.bara = bara_arr[dayOfWeek - 1];
            myPanchangObj.ebara = ebara_arr[dayOfWeek - 1];
            int sunriseMin;
            int hr;
            int min;
            double dayDurationInHr, nightDurationInHr;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 1, 0, 0, obsLon, obsLat);
            smc.calcSunAndMoon();

            Calendar sunRiseCal = SunMoonCalculator.getSunRiseDate(smc.sunRise, year, month + 1, day);
           // Calendar sunRiseCal = SunMoonCalculator.getDateAsDate(smc.sunRise);
            Calendar sunSetCal = SunMoonCalculator.getDateAsDate(smc.sunSet);


            Calendar sunNoonCal = SunMoonCalculator.getDateAsDate(smc.sunTransit);
            Calendar moonRiseCal = SunMoonCalculator.getDateAsDate(smc.moonRise);

            Calendar moonSetCal = SunMoonCalculator.getDateAsDate(smc.moonSet);

            if (moonRiseCal != null) {
                myPanchangObj.moonRise = getFormattedDate(moonRiseCal);
                myPanchangObj.emoonRise = geteFormattedDate(moonRiseCal);
            } else {
                myPanchangObj.moonRise = "-";
                myPanchangObj.emoonRise = "-";
            }


            if (moonSetCal != null) {

                myPanchangObj.moonSet = getFormattedDate(moonSetCal);
                myPanchangObj.emoonSet = geteFormattedDate(moonSetCal);
            } else {
                myPanchangObj.moonSet = "-";
                myPanchangObj.emoonSet = "-";
            }

            long dayTime = Math.abs(sunSetCal.getTimeInMillis() - sunRiseCal.getTimeInMillis()) / 1000;

            long nightTime = (24 * 60 * 60) - dayTime;
            dayDurationInHr = (dayTime / (60 * 60.0));
            nightDurationInHr = (nightTime / (60 * 60.0));
            long hinduMidNight = (sunSetCal.getTimeInMillis() / 1000) + (nightTime / 2);

            Calendar midNightCal = Calendar.getInstance();
            midNightCal.setTimeInMillis(hinduMidNight * 1000);

            myPanchangObj.midNight = getFormattedDate(midNightCal);
            myPanchangObj.emidNight = geteFormattedDate(midNightCal);


            myPanchangObj.dayLength = timeConversion(dayTime);
            myPanchangObj.nightLength =  timeConversion(nightTime);
            myPanchangObj.edayLength = etimeConversion(dayTime);
            myPanchangObj.enightLength = etimeConversion(nightTime);

            hr = sunRiseCal.get(Calendar.HOUR_OF_DAY);
            min = sunRiseCal.get(Calendar.MINUTE);

            sunriseMin = (hr * 60) + min;
            myPanchangObj.sunTransit = getFormattedDate(sunNoonCal);
            myPanchangObj.sunRise = getFormattedDate(sunRiseCal);
            myPanchangObj.sunSet = getFormattedDate(sunSetCal);

            myPanchangObj.esunTransit = geteFormattedDate(sunNoonCal);
            myPanchangObj.esunRise = geteFormattedDate(sunRiseCal);
            myPanchangObj.esunSet = geteFormattedDate(sunSetCal);


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
            //myPanchangObj.pada = coreDataObj.getPuskar(dayOfWeek, coreDataObj.getTithi(), coreDataObj.getNakshetra(),sunRiseCal);
            CoreDataHelper.Panchanga puskarObj = coreDataObj.getPuskar(dayOfWeek, coreDataObj.getTithi(), coreDataObj.getNakshetra(), sunRiseCal);

            myPanchangObj.tarashuddhi = getTaraShuddhi(nakshetraObj);
            myPanchangObj.etarashuddhi = geteTaraShuddhi(nakshetraObj);

            myPanchangObj.chandrashuddhi = getChandraShuddhi(moonSignObj);
            myPanchangObj.echandrashuddhi = geteChandraShuddhi(moonSignObj);
            String currPuskar, ecurrPuskar;
            String nextPuskar = "", enextPuskar = "", nextToNextPuskar = "", enextToNextPuskar = "", nextToNextNextPuskar = "", enextToNextNextPuskar = "";
            String currPuskarDate = "", ecurrPuskarDate = "", nextPuskarDate = "", enextPuskarDate = "", nextNextPuskarDate = "", enextNextPuskarDate = "";

            if (puskarObj.currVal != 0) {

                currPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.currVal)  + pada;
                ecurrPuskar = puskarObj.currVal + epada;
                if(puskarObj.currValEndTime!=null) {
                    currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                    ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);

                }


            } else {
                currPuskar = nasti;
                ecurrPuskar = "not avail";

            }
            if (puskarObj.nextToNextNextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                enextPuskarDate = geteFormattedDate(puskarObj.le_nextValEndTime);
                nextNextPuskarDate = getFormattedDate(puskarObj.nextToNextValEndTime);
                enextNextPuskarDate = geteFormattedDate(puskarObj.nextToNextValEndTime);

                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + pada;
                enextPuskar = puskarObj.nextVal + epada;

                nextToNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextVal) + pada;
                enextToNextPuskar = puskarObj.nextToNextVal + epada;

                nextToNextNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextNextVal) + pada;
                enextToNextNextPuskar = puskarObj.nextToNextNextVal + epada;


                myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.puskara[2] = setMySubPanchang(nextToNextPuskar, nextNextPuskarDate);

                myPanchangObj.puskara[3] = setMySubPanchang(nextToNextNextPuskar, "");

                myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);
                myPanchangObj.epuskara[1] = setMySubPanchang(enextPuskar, enextPuskarDate);
                myPanchangObj.epuskara[2] = setMySubPanchang(enextToNextPuskar, enextNextPuskarDate);

                myPanchangObj.epuskara[3] = setMySubPanchang(enextToNextNextPuskar, "");

            }
            else if (puskarObj.nextToNextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                enextPuskarDate = geteFormattedDate(puskarObj.le_nextValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + pada;
                enextPuskar = puskarObj.nextVal + epada;

                nextToNextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextToNextVal) + pada;
                enextToNextPuskar = puskarObj.nextToNextVal + epada;

                myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.puskara[2] = setMySubPanchang(nextToNextPuskar, "");
                myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);
                myPanchangObj.epuskara[1] = setMySubPanchang(enextPuskar, enextPuskarDate);
                myPanchangObj.epuskara[2] = setMySubPanchang(enextToNextPuskar, "");

            }else if (puskarObj.nextVal != 0 && puskarObj.le_nextValEndTime !=null) {

                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);
                nextPuskarDate = getFormattedDate(puskarObj.le_nextValEndTime);
                enextPuskarDate = geteFormattedDate(puskarObj.le_nextValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + pada;
                enextPuskar = puskarObj.nextVal + epada;

                nextToNextPuskar = nasti;
                enextToNextPuskar = "not avail";

                myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.puskara[1] = setMySubPanchang(nextPuskar, nextPuskarDate);
                myPanchangObj.puskara[2] = setMySubPanchang(nextToNextPuskar, "");
                myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);
                myPanchangObj.epuskara[1] = setMySubPanchang(enextPuskar, enextPuskarDate);
                myPanchangObj.epuskara[2] = setMySubPanchang(enextToNextPuskar, "");

            } else if (puskarObj.nextVal != 0) {
                currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);
                nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + pada;
                enextPuskar = puskarObj.nextVal + epada;

                myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.puskara[1] = setMySubPanchang(nextPuskar, "");
                myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);
                myPanchangObj.epuskara[1] = setMySubPanchang(enextPuskar, "");

            } else if (puskarObj.currVal != 0 && !currPuskarDate.isEmpty()) {
                myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);

            }else if (puskarObj.currVal != 0) {
                 myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, "");
                 myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, "");

             }else if (puskarObj.nextVal == 0) {
                 currPuskarDate = getFormattedDate(puskarObj.currValEndTime);
                 ecurrPuskarDate = geteFormattedDate(puskarObj.currValEndTime);
                 nextPuskar = Utility.getInstance(mContext).getDayNo("" + puskarObj.nextVal) + pada;
                 enextPuskar = puskarObj.nextVal + epada;

                 myPanchangObj.puskara[0] = setMySubPanchang(currPuskar, currPuskarDate);
                 myPanchangObj.puskara[1] = setMySubPanchang(nextPuskar, "");
                 myPanchangObj.epuskara[0] = setMySubPanchang(ecurrPuskar, ecurrPuskarDate);
                 myPanchangObj.epuskara[1] = setMySubPanchang(enextPuskar, "");

             }

            ArrayList<CoreDataHelper.StartEndTime> amritaBela = coreDataObj.getAmritaBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            ArrayList<CoreDataHelper.StartEndTime> mahendraBela = coreDataObj.getMahendraBela(dayOfWeek, sunriseMin / 60.0, dayDurationInHr);
            for (int i = 0; i < amritaBela.size(); i++) {
                String startTime = getFormattedDate(amritaBela.get(i).startTime);
                String endTime = getFormattedDate(amritaBela.get(i).endTime);
                String estartTime = geteFormattedDate(amritaBela.get(i).startTime);
                String eendTime = geteFormattedDate(amritaBela.get(i).endTime);
                myPanchangObj.amritaBelaList[i] = setMyBela(startTime, endTime);
                myPanchangObj.eamritaBelaList[i] = setMyBela(estartTime, eendTime);
            }
            for (int i = 0; i < mahendraBela.size(); i++) {
                String startTime = getFormattedDate(mahendraBela.get(i).startTime);
                String endTime = getFormattedDate(mahendraBela.get(i).endTime);
                String estartTime = geteFormattedDate(mahendraBela.get(i).startTime);
                String eendTime = geteFormattedDate(mahendraBela.get(i).endTime);
                myPanchangObj.mahendraBelaList[i] = setMyBela(startTime, endTime);
                myPanchangObj.emahendraBelaList[i] = setMyBela(estartTime, eendTime);
            }

            for (int i = 0; i < durMuhurtamBela.size(); i++) {
                String startTime = getFormattedDate(durMuhurtamBela.get(i).startTime);
                String endTime = getFormattedDate(durMuhurtamBela.get(i).endTime);
                String estartTime = geteFormattedDate(durMuhurtamBela.get(i).startTime);
                String eendTime = geteFormattedDate(durMuhurtamBela.get(i).endTime);
                myPanchangObj.durMuhurtaBela[i] = setMyBela(startTime, endTime);
                myPanchangObj.edurMuhurtaBela[i] = setMyBela(estartTime, eendTime);
            }
            for (int i = 0; i < kalaRatriBela.size(); i++) {
                String startTime = getFormattedDate(kalaRatriBela.get(i).startTime);
                String endTime = getFormattedDate(kalaRatriBela.get(i).endTime);
                String estartTime = geteFormattedDate(kalaRatriBela.get(i).startTime);
                String eendTime = geteFormattedDate(kalaRatriBela.get(i).endTime);
                myPanchangObj.kalaRatriBela[i] = setMyBela(startTime, endTime);
                myPanchangObj.ekalaRatriBela[i] = setMyBela(estartTime, eendTime);
            }

            myPanchangObj.varaBela[0] = setMyBela(getFormattedDate(varaBela.startTime), getFormattedDate(varaBela.endTime));
            myPanchangObj.kalaBela[0] = setMyBela(getFormattedDate(kalaBela.startTime), getFormattedDate(kalaBela.endTime));


            myPanchangObj.abhijitBela[0] = setMyBela(getFormattedDate(abhijitBela.startTime), getFormattedDate(abhijitBela.endTime));
            myPanchangObj.brahmaBela[0] = setMyBela(getFormattedDate(brahmaBela.startTime), getFormattedDate(brahmaBela.endTime));


            myPanchangObj.rahuBela[0] = setMyBela(getFormattedDate(rahuBela.startTime), getFormattedDate(rahuBela.endTime));

            myPanchangObj.gulikaBela[0] = setMyBela(getFormattedDate(gulikaBela.startTime), getFormattedDate(gulikaBela.endTime));
            myPanchangObj.yamaBela[0] = setMyBela(getFormattedDate(yamaBela.startTime), getFormattedDate(yamaBela.endTime));
            myPanchangObj.evaraBela[0] = setMyBela(geteFormattedDate(varaBela.startTime), geteFormattedDate(varaBela.endTime));
            myPanchangObj.ekalaBela[0] = setMyBela(geteFormattedDate(kalaBela.startTime), geteFormattedDate(kalaBela.endTime));

            myPanchangObj.eabhijitBela[0] = setMyBela(geteFormattedDate(abhijitBela.startTime), geteFormattedDate(abhijitBela.endTime));

            myPanchangObj.ebrahmaBela[0] = setMyBela(geteFormattedDate(brahmaBela.startTime), geteFormattedDate(brahmaBela.endTime));


            myPanchangObj.erahuBela[0] = setMyBela(geteFormattedDate(rahuBela.startTime), geteFormattedDate(rahuBela.endTime));

            myPanchangObj.egulikaBela[0] = setMyBela(geteFormattedDate(gulikaBela.startTime), geteFormattedDate(gulikaBela.endTime));
            myPanchangObj.eyamaBela[0] = setMyBela(geteFormattedDate(yamaBela.startTime), geteFormattedDate(yamaBela.endTime));


            ArrayList<CoreDataHelper.StartEndTime> amritaStartEndTimeList = coreDataObj.getAmritaStartEndTimeList();
            ArrayList<CoreDataHelper.StartEndTime> varjyamStartEndTimeList = coreDataObj.getVarjyamStartEndTimeList();
            for (int i = 0; i < amritaStartEndTimeList.size(); i++) {
                String startTimeAmrita = getFormattedDate(amritaStartEndTimeList.get(i).startTime);
                String endTimeAmrita = getFormattedDate(amritaStartEndTimeList.get(i).endTime);
                myPanchangObj.amritaBela[i] = setMyBela(startTimeAmrita, endTimeAmrita);

                String estartTimeAmrita = geteFormattedDate(amritaStartEndTimeList.get(i).startTime);
                String eendTimeAmrita = geteFormattedDate(amritaStartEndTimeList.get(i).endTime);
                myPanchangObj.eamritaBela[i] = setMyBela(estartTimeAmrita, eendTimeAmrita);

            }
            for (int i = 0; i < varjyamStartEndTimeList.size(); i++) {
                String startTimeVarjyam = getFormattedDate(varjyamStartEndTimeList.get(i).startTime);
                String endTimeVarjyam = getFormattedDate(varjyamStartEndTimeList.get(i).endTime);
                myPanchangObj.varjyamBela[i] = setMyBela(startTimeVarjyam, endTimeVarjyam);

                String estartTimeVarjyam = geteFormattedDate(varjyamStartEndTimeList.get(i).startTime);
                String eendTimeVarjyam = geteFormattedDate(varjyamStartEndTimeList.get(i).endTime);
                myPanchangObj.evarjyamBela[i] = setMyBela(estartTimeVarjyam, eendTimeVarjyam);


            }

            int paksha = coreDataObj.getPaksha();
            myPanchangObj.paksha = pakshya_arr[paksha];
            myPanchangObj.auspKey = getAuspKeyDate(sunRiseCal);

            myPanchangObj.epaksha = epakshya_arr[paksha];
            myPanchangObj.pakshaIndex = paksha;


            String currTithi = tithi_arr[tithiObj.currVal - 1];
            String ecurrTithi = etithi_arr[tithiObj.currVal - 1];
            myPanchangObj.currTithiIndex = tithiObj.currVal - 1;
            myPanchangObj.currTithi = tithi_arr[tithiObj.currVal - 1];
            myPanchangObj.ecurrTithi = etithi_arr[tithiObj.currVal - 1];

            String nextTithi = "", enextTithi = "", nextToNextTithi = "", enextToNextTithi = "";
            String currTithiDate = "", ecurrTithiDate = "", nextTithiDate = "", enextTithiDate = "";
            if (tithiObj.nextVal != 0) {
                myPanchangObj.nextTithiIndex = tithiObj.nextVal - 1;
                currTithiDate = getFormattedDate(tithiObj.currValEndTime);
                ecurrTithiDate = geteFormattedDate(tithiObj.currValEndTime);
                nextTithi = tithi_arr[tithiObj.nextVal - 1];
                enextTithi = etithi_arr[tithiObj.nextVal - 1];
            }


            if (tithiObj.nextToNextVal != 0) {

                myPanchangObj.nextToNextTithiIndex = tithiObj.nextToNextVal - 1;
                nextTithiDate = getFormattedDate(tithiObj.le_nextValEndTime);
                enextTithiDate = geteFormattedDate(tithiObj.le_nextValEndTime);
                nextToNextTithi = tithi_arr[tithiObj.nextToNextVal - 1];
                enextToNextTithi = etithi_arr[tithiObj.nextToNextVal - 1];
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


            String currJogini = getJogini(tithiObj.currVal - 1, 0);
            String ecurrJogini = getJogini(tithiObj.currVal - 1, 1);

            String nextJogini = getJogini(tithiObj.nextVal - 1, 0);
            String enextJogini = getJogini(tithiObj.nextVal - 1, 1);

            String nextToNextJogini = getJogini(tithiObj.nextToNextVal - 1, 0);
            String enextToNextJogini = getJogini(tithiObj.nextToNextVal - 1, 1);


            String currJoginiYatra = getJoginiYatra(tithiObj.currVal - 1, 0);
            if(!currJoginiYatra.isEmpty())
                currJoginiYatra=currJoginiYatra+" "+jogin_help;
            String ecurrJoginiYatra = getJoginiYatra(tithiObj.currVal - 1, 1);
            if(!ecurrJoginiYatra.isEmpty())
                ecurrJoginiYatra=ecurrJoginiYatra+" "+ejogin_help;

            String nextJoginiYatra = getJoginiYatra(tithiObj.nextVal - 1, 0);
            if(!nextJoginiYatra.isEmpty())
                nextJoginiYatra=nextJoginiYatra+" "+jogin_help;

            String enextJoginiYatra = getJoginiYatra(tithiObj.nextVal - 1, 1);
            if(!enextJoginiYatra.isEmpty())
                enextJoginiYatra=enextJoginiYatra+" "+ejogin_help;


            String nextToNextJoginiYatra = getJoginiYatra(tithiObj.nextToNextVal - 1, 0);
            if(!nextToNextJoginiYatra.isEmpty())
                nextToNextJoginiYatra=nextToNextJoginiYatra+" "+jogin_help;
            String enextToNextJoginiYatra = getJoginiYatra(tithiObj.nextToNextVal - 1, 1);
            if(!enextToNextJoginiYatra.isEmpty())
                enextToNextJoginiYatra=enextToNextJoginiYatra+" "+ejogin_help;


            myPanchangObj.jogini[0] = setMySubPanchang(currJogini, currTithiDate);
            myPanchangObj.jogini[1] = setMySubPanchang(nextJogini, nextTithiDate);
            myPanchangObj.jogini[2] = setMySubPanchang(nextToNextJogini, "");


            myPanchangObj.ejogini[0] = setMySubPanchang(ecurrJogini, ecurrTithiDate);
            myPanchangObj.ejogini[1] = setMySubPanchang(enextJogini, enextTithiDate);
            myPanchangObj.ejogini[2] = setMySubPanchang(enextToNextJogini, "");

            myPanchangObj.joginiYatra[0] = setMySubPanchang(currJoginiYatra, currTithiDate);
            myPanchangObj.joginiYatra[1] = setMySubPanchang(nextJoginiYatra, nextTithiDate);
            myPanchangObj.joginiYatra[2] = setMySubPanchang(nextToNextJoginiYatra, "");


            myPanchangObj.ejoginiYatra[0] = setMySubPanchang(ecurrJoginiYatra, ecurrTithiDate);
            myPanchangObj.ejoginiYatra[1] = setMySubPanchang(enextJoginiYatra, enextTithiDate);
            myPanchangObj.ejoginiYatra[2] = setMySubPanchang(enextToNextJoginiYatra, "");

            boolean isSnkrant = coreDataObj.getSolarDayVal() == 1;

            String currFood = getFoodProhibited(tithiObj.currVal - 1, 0, isSnkrant);
            String ecurrFood = getFoodProhibited(tithiObj.currVal - 1, 1, isSnkrant);

            String nextFood = getFoodProhibited(tithiObj.nextVal - 1, 0, isSnkrant);
            String enextFood = getFoodProhibited(tithiObj.nextVal - 1, 1, isSnkrant);

            String nextToNextFood = getFoodProhibited(tithiObj.nextToNextVal - 1, 0, isSnkrant);
            String enextToNextFood = getFoodProhibited(tithiObj.nextToNextVal - 1, 1, isSnkrant);

            myPanchangObj.rajaDarsan = getRajaDarsan(nakshetraObj, 0);
            myPanchangObj.erajaDarsan = getRajaDarsan(nakshetraObj, 1);


            myPanchangObj.food[0] = setMySubPanchang(currFood, currTithiDate);
            myPanchangObj.food[1] = setMySubPanchang(nextFood, nextTithiDate);
            myPanchangObj.food[2] = setMySubPanchang(nextToNextFood, "");


            myPanchangObj.efood[0] = setMySubPanchang(ecurrFood, ecurrTithiDate);
            myPanchangObj.efood[1] = setMySubPanchang(enextFood, enextTithiDate);
            myPanchangObj.efood[2] = setMySubPanchang(enextToNextFood, "");

            myPanchangObj.ghataBar = getGhataBara(dayOfWeek, 0);
            myPanchangObj.eghataBar = getGhataBara(dayOfWeek, 1);

            String shraddhaTmp = getShraddha(tithiObj, sunRiseCal, sunSetCal, sunNoonCal);


            myPanchangObj.shraddha = shraddhaTmp.split("__")[0].trim();
            myPanchangObj.eshraddha = shraddhaTmp.split("__")[1].trim();

            int lunarMonthPurnimantIndex = coreDataObj.getLunarMonthPurnimantIndex();
            int lunarMonthAmantIndex = coreDataObj.getLunarMonthAmantIndex();
            int newMoonLunarDay = coreDataObj.getNewMoonLunarDay();
            int fullMoonLunarDay = coreDataObj.getFullMoonLunarDay();
            int lunarMonthType = coreDataObj.getmLunarMonthType();
            myPanchangObj.planetInfo = coreDataObj.getPlanetInfo();



            if (lang.contains("te") || lang.contains("kn") || lang.contains("mr") || lang.contains("gu")) {


                myPanchangObj.elunarDayAmant = "" + newMoonLunarDay;
                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.elunarDayPurimant = "" + newMoonLunarDay;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.lunarMonthAmant = masa_arr[lunarMonthAmantIndex];
                myPanchangObj.lunarMonthPurimant = masa_arr[lunarMonthAmantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthAmant = emasa_arr[lunarMonthAmantIndex];
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthPurimant = emasa_arr[lunarMonthAmantIndex];


            } else if (lang.contains("or") || lang.contains("hi") || lang.contains("en")) {
                myPanchangObj.elunarDayAmant = "" + newMoonLunarDay;
                myPanchangObj.lunarMonthType = lunarMonthType;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + newMoonLunarDay);
                myPanchangObj.elunarDayPurimant = "" + fullMoonLunarDay;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + fullMoonLunarDay);
                myPanchangObj.lunarMonthAmant = masa_arr[lunarMonthAmantIndex];
                myPanchangObj.lunarMonthPurimant = masa_arr[lunarMonthPurnimantIndex];
                myPanchangObj.pLunarMonthIndex = lunarMonthPurnimantIndex;
                myPanchangObj.elunarMonthAmant = emasa_arr[lunarMonthAmantIndex];
                myPanchangObj.aLunarMonthIndex = lunarMonthAmantIndex;
                myPanchangObj.elunarMonthPurimant = emasa_arr[lunarMonthPurnimantIndex];

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



            String sanSalaAnka = "" + getSanSalAnka(year, month, day, myPanchangObj.pLunarMonthIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.tithiKhaya);

            myPanchangObj.sanSalAnka = Utility.getInstance(mContext).getDayNo(sanSalaAnka);
            myPanchangObj.esanSalAnka = sanSalaAnka;


            String k1 = karana_arr[karanaObj.val1 - 1];
            String k2 = "", k3 = "", k4 = "", k5 = "";
            String k1d = "", k2d = "", k3d = "", k4d = "", k5d = "";

            String ek1 = ekarana_arr[karanaObj.val1 - 1];
            String ek2 = "", ek3 = "", ek4 = "", ek5 = "";
            String ek1d = "", ek2d = "", ek3d = "", ek4d = "", ek5d = "";

            if (karanaObj.val2 != 0) {
                k1d = getFormattedDate(karanaObj.val1ET);
                k2 = karana_arr[karanaObj.val2 - 1];

                ek1d = geteFormattedDate(karanaObj.val1ET);
                ek2 = ekarana_arr[karanaObj.val2 - 1];
            }
            if (karanaObj.val3 != 0) {
                k2d = getFormattedDate(karanaObj.val2ET);
                k3 = karana_arr[karanaObj.val3 - 1];

                ek2d = geteFormattedDate(karanaObj.val2ET);
                ek3 = ekarana_arr[karanaObj.val3 - 1];
            }
            if (karanaObj.val4 != 0) {
                k3d = getFormattedDate(karanaObj.val3ET);
                k4 = karana_arr[karanaObj.val4 - 1];
                ek3d = geteFormattedDate(karanaObj.val3ET);
                ek4 = ekarana_arr[karanaObj.val4 - 1];
            }
            if (karanaObj.val5 != 0) {
                k4d = getFormattedDate(karanaObj.val4ET);
                k5 = karana_arr[karanaObj.val5 - 1];
                ek4d = geteFormattedDate(karanaObj.val4ET);
                ek5 = ekarana_arr[karanaObj.val5 - 1];
            }
            if (karanaObj.val6 != 0) {
                k5d = getFormattedDate(karanaObj.val5ET);
                ek5d = geteFormattedDate(karanaObj.val5ET);

            }


            myPanchangObj.karana[0] = setMySubPanchang(k1, k1d);
            myPanchangObj.karana[1] = setMySubPanchang(k2, k2d);
            myPanchangObj.karana[2] = setMySubPanchang(k3, k3d);
            myPanchangObj.karana[3] = setMySubPanchang(k4, k4d);
            myPanchangObj.karana[4] = setMySubPanchang(k5, k5d);

            myPanchangObj.ekarana[0] = setMySubPanchang(ek1, ek1d);
            myPanchangObj.ekarana[1] = setMySubPanchang(ek2, ek2d);
            myPanchangObj.ekarana[2] = setMySubPanchang(ek3, ek3d);
            myPanchangObj.ekarana[3] = setMySubPanchang(ek4, ek4d);
            myPanchangObj.ekarana[4] = setMySubPanchang(ek5, ek5d);

            String currNakshetra = nakshatra_arr[nakshetraObj.currVal - 1];
            String nextNakshetra = "", nextToNextNakshetra = "";
            String currNakshetraDate = "", nextNakshetraDate = "";

            String ecurrNakshetra = enakshatra_arr[nakshetraObj.currVal - 1];
            String enextNakshetra = "", enextToNextNakshetra = "";
            String ecurrNakshetraDate = "", enextNakshetraDate = "";

            if (nakshetraObj.nextVal != 0) {
                currNakshetraDate = getFormattedDate(nakshetraObj.currValEndTime);
                nextNakshetra = nakshatra_arr[nakshetraObj.nextVal - 1];
                ecurrNakshetraDate = geteFormattedDate(nakshetraObj.currValEndTime);
                enextNakshetra = enakshatra_arr[nakshetraObj.nextVal - 1];
            }
            if (nakshetraObj.nextToNextVal != 0) {
                nextNakshetraDate = getFormattedDate(nakshetraObj.le_nextValEndTime);
                nextToNextNakshetra = nakshatra_arr[nakshetraObj.nextToNextVal - 1];
                enextNakshetraDate = geteFormattedDate(nakshetraObj.le_nextValEndTime);
                enextToNextNakshetra = enakshatra_arr[nakshetraObj.nextToNextVal - 1];
            }
            myPanchangObj.currNakshetraIndex = nakshetraObj.currVal - 1;

            myPanchangObj.nakshetra[0] = setMySubPanchang(currNakshetra, currNakshetraDate);
            myPanchangObj.nakshetra[1] = setMySubPanchang(nextNakshetra, nextNakshetraDate);
            myPanchangObj.nakshetra[2] = setMySubPanchang(nextToNextNakshetra, "");

            myPanchangObj.enakshetra[0] = setMySubPanchang(ecurrNakshetra, ecurrNakshetraDate);
            myPanchangObj.enakshetra[1] = setMySubPanchang(enextNakshetra, enextNakshetraDate);
            myPanchangObj.enakshetra[2] = setMySubPanchang(enextToNextNakshetra, "");

            String currJoga = joga_arr[jogaObj.currVal - 1];
            String nextJoga = "", nextToNextJoga = "";
            String currJogaDate = "", nextJogaDate = "";

            String ecurrJoga = ejoga_arr[jogaObj.currVal - 1];
            String enextJoga = "", enextToNextJoga = "";
            String ecurrJogaDate = "", enextJogaDate = "";

            if (jogaObj.nextVal != 0) {
                currJogaDate = getFormattedDate(jogaObj.currValEndTime);
                nextJoga = joga_arr[jogaObj.nextVal - 1];

                ecurrJogaDate = geteFormattedDate(jogaObj.currValEndTime);
                enextJoga = ejoga_arr[jogaObj.nextVal - 1];
            }
            if (jogaObj.nextToNextVal != 0) {
                nextJogaDate = getFormattedDate(jogaObj.le_nextValEndTime);
                nextToNextJoga = joga_arr[jogaObj.nextToNextVal - 1];

                enextJogaDate = geteFormattedDate(jogaObj.le_nextValEndTime);
                enextToNextJoga = ejoga_arr[jogaObj.nextToNextVal - 1];
            }

            myPanchangObj.joga[0] = setMySubPanchang(currJoga, currJogaDate);
            myPanchangObj.joga[1] = setMySubPanchang(nextJoga, nextJogaDate);
            myPanchangObj.joga[2] = setMySubPanchang(nextToNextJoga, "");

            myPanchangObj.ejoga[0] = setMySubPanchang(ecurrJoga, ecurrJogaDate);
            myPanchangObj.ejoga[1] = setMySubPanchang(enextJoga, enextJogaDate);
            myPanchangObj.ejoga[2] = setMySubPanchang(enextToNextJoga, "");

            String currSunSign = rasi_kundali_arr[sunSignObj.currVal - 1];
            String nextSunSign = "";
            String currSunSignDate = "", nextSunSignDate = "";

            String ecurrSunSign = erasi_kundali_arr[sunSignObj.currVal - 1];
            String enextSunSign = "";
            String ecurrSunSignDate = "", enextSunSignDate = "";

            int adjustSolarMonth = coreDataObj.getAdjustSolarMonth();
            int solarDayVal = coreDataObj.getSolarDayVal();

            long sunsignEnd = sunSignObj.currValEndTime.getTimeInMillis();
            if (sunsignEnd > midNightCal.getTimeInMillis() && (sunsignEnd < midNightCal.getTimeInMillis() + (24 * 60 * 60 * 1000L))) {

                myPanchangObj.masant = true;
            }


            if (adjustSolarMonth >= 3 && adjustSolarMonth < 9) {
                myPanchangObj.ayana = dayana;
                myPanchangObj.eAyana = edayana;
            } else {
                myPanchangObj.ayana = uayana;
                myPanchangObj.eAyana = euayana;
            }

            myPanchangObj.sunSign[0] = setMySubPanchang(currSunSign, currSunSignDate);
            myPanchangObj.sunSign[1] = setMySubPanchang(nextSunSign, nextSunSignDate);

            myPanchangObj.esunSign[0] = setMySubPanchang(ecurrSunSign, ecurrSunSignDate);
            myPanchangObj.esunSign[1] = setMySubPanchang(enextSunSign, enextSunSignDate);


            myPanchangObj.esolarDay = "" + solarDayVal;
            myPanchangObj.solarDay = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
            myPanchangObj.solarDayIndex = solarDayVal;

            myPanchangObj.solarMonth = rasi_kundali_arr[adjustSolarMonth - 1];
            myPanchangObj.esolarMonth = erasi_kundali_arr[adjustSolarMonth - 1];
            myPanchangObj.solarMonthIndex = adjustSolarMonth - 1;

            myPanchangObj.monthIndex = month;


            if (lang.contains("bn") || lang.contains("ta") || lang.contains("pa") || lang.contains("ml")) {

                myPanchangObj.elunarDayAmant = "" + solarDayVal;
                myPanchangObj.lunarDayAmant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.elunarDayPurimant = "" + solarDayVal;
                myPanchangObj.lunarDayPurimant = Utility.getInstance(mContext).getDayNo("" + solarDayVal);
                myPanchangObj.lunarMonthAmant = masa_arr[adjustSolarMonth - 1];
                myPanchangObj.lunarMonthPurimant = masa_arr[adjustSolarMonth - 1];
                myPanchangObj.pLunarMonthIndex = adjustSolarMonth - 1;
                myPanchangObj.elunarMonthAmant = emasa_arr[adjustSolarMonth - 1];
                myPanchangObj.aLunarMonthIndex = adjustSolarMonth - 1;
                myPanchangObj.elunarMonthPurimant = emasa_arr[adjustSolarMonth - 1];


            }


            int codeRitu = (int) Math.floor((adjustSolarMonth - 1) / 2.0);
            myPanchangObj.ritu = ritu_arr[codeRitu];
            myPanchangObj.eRitu = eritu_arr[codeRitu];


            String currGhataChandra, ecurrGhataChandra, nextGhataChandra = "", enextGhataChandra = "", nextTonextGhataChandra = "", enextTonextGhataChandra = "";

            String currMoonSign = rasi_kundali_arr[moonSignObj.currVal - 1];
            currGhataChandra = getGhataChandra(moonSignObj.currVal, 0);
            ecurrGhataChandra = getGhataChandra(moonSignObj.currVal, 1);

            String nextMoonSign = "", nextToNextMoonSign = "";
            String currMoonSignDate = "", nextMoonSignDate = "";

            String ecurrMoonSign = erasi_kundali_arr[moonSignObj.currVal - 1];
            String enextMoonSign = "", enextToNextMoonSign = "";
            String ecurrMoonSignDate = "", enextMoonSignDate = "";

            if (moonSignObj.nextVal != 0) {
                if (currGhataChandra.isEmpty()) {
                    currGhataChandra = nasti;
                    ecurrGhataChandra = "not avai";
                }
                currMoonSignDate = getFormattedDate(moonSignObj.currValEndTime);

                nextMoonSign = rasi_kundali_arr[moonSignObj.nextVal - 1];
                ecurrMoonSignDate = geteFormattedDate(moonSignObj.currValEndTime);
                enextMoonSign = erasi_kundali_arr[moonSignObj.nextVal - 1];
                nextGhataChandra = getGhataChandra(moonSignObj.nextVal, 0);
                enextGhataChandra = getGhataChandra(moonSignObj.nextVal, 1);

            }
            if (moonSignObj.nextToNextVal != 0) {
                nextMoonSignDate = getFormattedDate(moonSignObj.le_nextValEndTime);
                nextToNextMoonSign = rasi_kundali_arr[moonSignObj.nextToNextVal - 1];
                enextMoonSignDate = geteFormattedDate(moonSignObj.le_nextValEndTime);
                enextToNextMoonSign = erasi_kundali_arr[moonSignObj.nextToNextVal - 1];

                nextTonextGhataChandra = getGhataChandra(moonSignObj.nextToNextVal, 0);
                enextTonextGhataChandra = getGhataChandra(moonSignObj.nextToNextVal, 1);
            }


            myPanchangObj.moonSign[0] = setMySubPanchang(currMoonSign, currMoonSignDate);
            myPanchangObj.moonSign[1] = setMySubPanchang(nextMoonSign, nextMoonSignDate);
            myPanchangObj.moonSign[2] = setMySubPanchang(nextToNextMoonSign, "");
            myPanchangObj.emoonSign[0] = setMySubPanchang(ecurrMoonSign, ecurrMoonSignDate);
            myPanchangObj.emoonSign[1] = setMySubPanchang(enextMoonSign, enextMoonSignDate);
            myPanchangObj.emoonSign[2] = setMySubPanchang(enextToNextMoonSign, "");

            myPanchangObj.ghataChandra[0] = setMySubPanchang(currGhataChandra, currMoonSignDate);
            myPanchangObj.ghataChandra[1] = setMySubPanchang(nextGhataChandra, nextMoonSignDate);
            myPanchangObj.ghataChandra[2] = setMySubPanchang(nextTonextGhataChandra, "");
            myPanchangObj.eghataChandra[0] = setMySubPanchang(ecurrGhataChandra, ecurrMoonSignDate);
            myPanchangObj.eghataChandra[1] = setMySubPanchang(enextGhataChandra, enextMoonSignDate);
            myPanchangObj.eghataChandra[2] = setMySubPanchang(enextTonextGhataChandra, "");
            return myPanchangObj;
        } catch (Exception e) {
            Log.e("myPanchangObj","myPanchangObj:3:null"+e.getMessage());
            e.printStackTrace();
        }

            Log.e("myPanchangObj","myPanchangObj:2:null");

        return null;
    }

    private String getRajaDarsan(CoreDataHelper.Panchanga nakshetra, int type) {
        if (nakshetra.currVal < 0)
            return "";
        try {
            if (nakshetra.nextVal != 0) {

                String dateStr = " " + getFormattedDate(nakshetra.currValEndTime) + " " + pkey_time_to + ", " + pkey_time_next + " ";
                String edateStr = " " + epkey_time_to + " " + geteFormattedDate(nakshetra.currValEndTime) + ", " + epkey_time_next + " ";

                if (type == 0) {
                    return l_raja_darsan[nakshetra.currVal - 1] + dateStr + l_raja_darsan[nakshetra.nextVal - 1];
                } else {
                    return e_raja_darsan[nakshetra.currVal - 1] + edateStr + e_raja_darsan[nakshetra.nextVal - 1];
                }
            } else {
                if (type == 0) {
                    return l_raja_darsan[nakshetra.currVal - 1];
                } else {
                    return e_raja_darsan[nakshetra.currVal - 1];
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
            String[] food = l_food;
            if (type == 1) {
                food = e_food;
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
                chandrashuddhi = chandrashuddhi.concat(rasi_kundali_arr[birthnak - 1]).concat(", ");
            }
        }
        chandrashuddhi = chandrashuddhi.trim().replaceAll(",$", "");
        chandrashuddhi = chandrashuddhi + " " + getFormattedDate(moonsignObj.currValEndTime) + " " + pkey_time_to;

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
                    chandrashuddhi1 = chandrashuddhi1.concat(rasi_kundali_arr[birthnak - 1]).concat(", ");
                }
            }
            chandrashuddhi1 = chandrashuddhi1.trim().replaceAll(",$", "");
            chandrashuddhi = chandrashuddhi + " " + pkey_time_next + " " + chandrashuddhi1;

        }


        return chandrashuddhi;


    }

    private String geteChandraShuddhi(CoreDataHelper.Panchanga moonsignObj) {
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
                chandrashuddhi = chandrashuddhi.concat(erasi_kundali_arr[birthnak - 1]).concat(", ");
            }
        }
        chandrashuddhi = chandrashuddhi.trim().replaceAll(",$", "");
        chandrashuddhi = chandrashuddhi + " " + epkey_time_to + " " + geteFormattedDate(moonsignObj.currValEndTime) + " ";

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
                    chandrashuddhi1 = chandrashuddhi1.concat(erasi_kundali_arr[birthnak - 1]).concat(", ");
                }
            }
            chandrashuddhi1 = chandrashuddhi1.trim().replaceAll(",$", "");
            chandrashuddhi = chandrashuddhi + " " + epkey_time_next + " " + chandrashuddhi1;

        }


        return chandrashuddhi;


    }

    private String geteTaraShuddhi(CoreDataHelper.Panchanga nakshetraObj) {
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
                nakshetra = nakshetra.concat(enakshatra_arr[birthnak - 1]).concat(", ");
            }
        }
        nakshetra = nakshetra.trim().replaceAll(",$", "");
        nakshetra = nakshetra + " " + epkey_time_to + " " + geteFormattedDate(nakshetraObj.currValEndTime) + " ";

        if (nakshetraObj.nextVal != 0) {
            dailynak = nakshetraObj.nextVal;
            String nakshetra1 = "";
            for (int birthnak = 1; birthnak <= 27; birthnak++) {

                int diff;
                if (birthnak > dailynak) {
                    diff = (Math.abs(dailynak + 27 - birthnak) + 1) % 9;
                } else {
                    diff = (Math.abs(birthnak - dailynak) + 1) % 9;
                }

                if (diff == 2 || diff == 4 || diff == 6 || diff == 8 || diff == 9 || diff == 0) {
                    //2,4,6,8,9
                    nakshetra1 = nakshetra1.concat(enakshatra_arr[birthnak - 1]).concat(", ");
                }
            }
            nakshetra1 = nakshetra1.trim().replaceAll(",$", "");
            nakshetra = nakshetra + " " + epkey_time_next + " " + nakshetra1;

        }


        return nakshetra;


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
                nakshetra = nakshetra.concat(nakshatra_arr[birthnak - 1]).concat(", ");
            }
        }


        nakshetra = nakshetra.trim().replaceAll(",$", "");

        nakshetra = nakshetra + " " + getFormattedDate(nakshetraObj.currValEndTime) + " " + pkey_time_to;

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
                    nakshetra1 = nakshetra1.concat(nakshatra_arr[birthnak - 1]).concat(", ");
                }
            }
            nakshetra1 = nakshetra1.trim().replaceAll(",$", "");

            nakshetra = nakshetra + " " + pkey_time_next + " " + nakshetra1;


        }


        return nakshetra;


    }

    private String getGhataChandra(int moonsign, int type) {
        String ghataChandra = "";
        String[] rasi = (type == 0) ? rasi_kundali_arr : erasi_kundali_arr;
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
        String[] rasi = (type == 0) ? rasi_kundali_arr : erasi_kundali_arr;
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

            String[] jogin = l_jogini;
            if (type == 1) {
                jogin = e_jogini;
            }
/*
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
                    return jogin[5];
                case 4:
                case 19:
                case 12:
                case 27:
                    return jogin[4];
                case 1:
                case 16:
                case 9:
                case 24:
                    return jogin[1];
                case 2:
                case 17:
                case 10:
                case 25:
                    return jogin[2];
                case 6:
                case 21:
                case 14:
                    return jogin[6];
                case 3:
                case 18:
                case 11:
                case 26:
                    return jogin[3];
                case 7:
                case 22:
                case 29:

                    return jogin[7];
*  <item>ପୂର୍ବ ଦିଗେ</item>
            <item>ଉତ୍ତର ଦିଗେ</item>
            <item>ଅଗ୍ନି କୋଣେ</item>
            <item>ନୈଋତ କୋଣେ</item>
            <item>ଦକ୍ଷିଣ ଦିଗେ</item>
            <item>ପଶ୍ଚିମ ଦିଗେ</item>
            <item>ବାୟୁ କୋଣେ</item>
            <item>ଐଶାନ୍ୟ କୋଣେ</item>
            *
            *   <item>ପୂର୍ବ ଦିଗେ</item>
        <item>ଅଗ୍ନି କୋଣେ</item>
        <item>ଦକ୍ଷିଣ ଦିଗେ</item>
        <item>ନୈଋତ କୋଣେ</item>
        <item>ପଶ୍ଚିମ ଦିଗେ</item>
        <item>ବାୟୁ କୋଣେ</item>
        <item>ଉତ୍ତର ଦିଗେ</item>
        <item>ଐଶାନ୍ୟ କୋଣେ</item>
            * */
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

            String[] jogin = l_jogini;
            if (type == 1) {
                jogin = e_jogini;
            }
            switch (tithi) {
                case 0:
                case 8:
                case 15:
                case 23:
                    return jogin[6]+","+jogin[0]+","+jogin[2];
                case 5:
                case 20:
                case 13:
                case 28:
                    return jogin[2]+","+jogin[4]+","+jogin[6];
                case 4:
                case 19:
                case 12:
                case 27:
                    return jogin[0]+","+jogin[2]+","+jogin[4];
                case 1:
                case 16:
                case 9:
                case 24:
                    return  jogin[4]+","+jogin[6]+","+jogin[0];
                case 2:
                case 17:
                case 10:
                case 25:
                    return  jogin[7]+","+jogin[1]+","+jogin[3];
                case 6:
                case 21:
                case 14:
                    return  jogin[3]+","+jogin[5]+","+jogin[7];
                case 3:
                case 18:
                case 11:
                case 26:
                    return  jogin[1]+","+jogin[3]+","+jogin[5];
                case 7:
                case 22:
                case 29:

                    return  jogin[5]+","+jogin[7]+","+jogin[1];


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

    private String etimeConversion(long totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        long totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        long hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return hours + " hrs " + minutes + " mins ";
    }

    private String timeConversion(long totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        long totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        long hours = totalMinutes / MINUTES_IN_AN_HOUR;


        return Utility.getInstance(mContext).getDayNo("" + hours) + mRes.getString(R.string.l_time_hour) + Utility.getInstance(mContext).getDayNo("" + minutes) + mRes.getString(R.string.l_time_min);
    }
    public String getFormattedDateForPanchak(Calendar cal,Context mContext) {
       Resources mRes=mContext.getResources();
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
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + month_arr[calMonth];
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);

                }
            } else if (mCalType == 1) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + month_arr[calMonth];

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

    }

    private String getFormattedDate(Calendar cal) {
        if(cal==null)
            return "";

        if (mLang.contains("or")) {
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
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + month_arr[calMonth];
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);

                }
            } else if (mCalType == 1) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + "" + mRes.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + month_arr[calMonth];

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

          if(year <1970){
              return 0;
          }
            int anka=0;
            for(int i=1970;i<=year;i++){
                anka++;
                if((((""+i).endsWith("0")) || ((""+i).endsWith("6")) || ((""+i).endsWith("01"))) /* && !((""+i).endsWith("10"))*/){
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
        String eeko = "", epar = "", efull = "";

        try {

            if (tithiObj.currValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                full = tithi_arr[tithiObj.currVal - 1];
                efull = etithi_arr[tithiObj.currVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 + (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = tithi_arr[tithiObj.currVal - 1];
                par = tithi_arr[tithiObj.nextVal - 1];

                efull = etithi_arr[tithiObj.currVal - 1];
                epar = etithi_arr[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 - (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = tithi_arr[tithiObj.currVal - 1];
                par = tithi_arr[tithiObj.nextVal - 1];
                efull = etithi_arr[tithiObj.currVal - 1];
                epar = etithi_arr[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = tithi_arr[tithiObj.currVal - 1];
                efull = etithi_arr[tithiObj.currVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = tithi_arr[tithiObj.currVal - 1];
                efull = etithi_arr[tithiObj.currVal - 1];
                par = tithi_arr[tithiObj.nextVal - 1];
                epar = etithi_arr[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > sunNoonCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000)) {
                eko = tithi_arr[tithiObj.currVal - 1];
                eeko = etithi_arr[tithiObj.currVal - 1];
                par = tithi_arr[tithiObj.nextVal - 1];
                epar = etithi_arr[tithiObj.nextVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() < sunNoonCal.getTimeInMillis() && tithiObj.le_nextValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                efullTmp = etithi_arr[tithiObj.currVal - 1];
                if (prevShraddha.contains(efullTmp)) {
                    full = tithi_arr[tithiObj.nextVal - 1];
                    efull = etithi_arr[tithiObj.nextVal - 1];

                } else if (tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() - 3 * 60 * 60 * 1000)) {
                    full = tithi_arr[tithiObj.nextVal - 1];
                    efull = etithi_arr[tithiObj.nextVal - 1];
                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 1.5 * 60 * 60 * 1000) {
                    if (tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000)) {
                        eko = tithi_arr[tithiObj.currVal - 1];
                        eeko = etithi_arr[tithiObj.currVal - 1];

                        full = tithi_arr[tithiObj.nextVal - 1];
                        efull = etithi_arr[tithiObj.nextVal - 1];

                    } else {
                        eko = tithi_arr[tithiObj.currVal - 1];
                        eeko = etithi_arr[tithiObj.currVal - 1];

                        par = tithi_arr[tithiObj.nextVal - 1];
                        epar = etithi_arr[tithiObj.nextVal - 1];
                    }

                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 2 * 60 * 60 * 1000) {

                    par = tithi_arr[tithiObj.nextVal - 1];
                    epar = etithi_arr[tithiObj.nextVal - 1];

                } else {
                    full = tithi_arr[tithiObj.nextVal - 1];
                    eko = tithi_arr[tithiObj.currVal - 1];

                    efull = etithi_arr[tithiObj.nextVal - 1];
                    eeko = etithi_arr[tithiObj.currVal - 1];
                }

            }


            prevShraddha = efullTmp;


        } catch (Exception e) {
            e.printStackTrace();
            if (!full.isEmpty()) {
                full = ", "+full + " " + mshraddha_ra + " " + mshraddha;
            }
            if (!eko.isEmpty()) {
                eko =eko + " " + mshraddha_ra + " " + mshraddha_eko;
            }
            if (!par.isEmpty()) {
                par =   ", "+par + " " + mshraddha_ra + " " + mshraddha_parban;
            }

            if (!efull.isEmpty()) {
                efull = ", "+ efull + " " + emshraddha_ra + " " + emshraddha;
            }
            if (!eko.isEmpty()) {
                eeko =  eeko + " " + emshraddha_ra + " " + emshraddha_eko;
            }
            if (!epar.isEmpty()) {
                epar =   ", "+epar + " " + emshraddha_ra + " " + emshraddha_parban;
            }
          //  String retval=full+eko  + par + "__"+efull  + eeko   + epar;
           // return retval.replaceAll("^,|,$", "");

            String retval= eko +  par  + full + "__" + eeko + epar + efull;
            return retval.replaceAll("^,|,$", "");

        }

        if (!full.isEmpty()) {
            full =  ", "+full + " " + mshraddha_ra + " " + mshraddha;
        }
        if (!eko.isEmpty()) {
            eko = eko + " " + mshraddha_ra + " " + mshraddha_eko;
        }
        if (!par.isEmpty()) {
            par = ", "+par + " " + mshraddha_ra + " " + mshraddha_parban;
        }

        if (!efull.isEmpty()) {
            efull = ", "+efull + " " + emshraddha_ra + " " + emshraddha;
        }
        if (!eko.isEmpty()) {
            eeko = eeko + " " + emshraddha_ra + " " + emshraddha_eko;
        }
        if (!epar.isEmpty()) {
            epar = ", "+epar + " " + emshraddha_ra + " " + emshraddha_parban;
        }

       // String retval=full+eko  + par + "__"+efull  + eeko   + epar;
        String retval= eko +  par  + full + "__" + eeko + epar + efull;
        return retval.replaceAll("^,|,$", "");
    }

    public class MyPanchang {
        boolean masant = false, tithiMala = false, tithiKhaya = false, nextNightCover = false, currNightCover = false;
        public int lunarMonthType, currNakshetraIndex = -1, puskar, dayIndex, monthIndex, yearIndex, solarDayIndex, currTithiIndex = -1, dayOfWeek, pLunarMonthIndex, aLunarMonthIndex, solarMonthIndex, nextTithiIndex = -1, nextToNextTithiIndex = -1, pakshaIndex;
        public String erajaDarsan, rajaDarsan, samvata, esamvata, ecurrTithi, currTithi, chandrashuddhi, echandrashuddhi, tarashuddhi, etarashuddhi, auspKey, sanSal, sakaddha, paksha, sunRise, sunSet, moonRise, moonSet, bara, day, month, year, sunTransit, midNight, dayLength, nightLength;
        public MyBela[] amritaBela = new MyBela[3];
        public MyBela[] varjyamBela = new MyBela[4];
        public MyBela[] rahuBela = new MyBela[1];
        public MyBela[] abhijitBela = new MyBela[1];
        public MyBela[] brahmaBela = new MyBela[1];
        public MyBela[] varaBela = new MyBela[1];
        public MyBela[] kalaBela = new MyBela[1];


        public MyBela[] gulikaBela = new MyBela[1];
        public MyBela[] yamaBela = new MyBela[1];
        public MyBela[] durMuhurtaBela = new MyBela[2];
        public MyBela[] kalaRatriBela = new MyBela[2];

        public MySubPanchang[] tithi = new MySubPanchang[3];
        public MySubPanchang[] puskara = new MySubPanchang[4];
        public MySubPanchang[] nakshetra = new MySubPanchang[3];
        public MySubPanchang[] joga = new MySubPanchang[3];
        public MySubPanchang[] karana = new MySubPanchang[5];
        public MySubPanchang[] sunSign = new MySubPanchang[2];
        public MySubPanchang[] moonSign = new MySubPanchang[3];


        public String lunarDayAmant, lunarMonthAmant, lunarDayPurimant, lunarMonthPurimant, solarDay, solarMonth;

        public String esanSalAnka,sanSalAnka,esanSal, esakaddha, epaksha, esunRise, esunSet, emoonRise, emoonSet, ebara, eday, emonth, eyear, esunTransit, emidNight, edayLength, enightLength;

        public MyBela[] eamritaBela = new MyBela[3];
        public MyBela[] evarjyamBela = new MyBela[4];
        public MyBela[] erahuBela = new MyBela[1];
        public MyBela[] eabhijitBela = new MyBela[1];
        public MyBela[] ebrahmaBela = new MyBela[1];
        public MyBela[] evaraBela = new MyBela[1];
        public MyBela[] ekalaBela = new MyBela[1];

        public MyBela[] egulikaBela = new MyBela[1];
        public MyBela[] eyamaBela = new MyBela[1];
        public MyBela[] edurMuhurtaBela = new MyBela[2];
        public MyBela[] ekalaRatriBela = new MyBela[2];

        public MyBela[] amritaBelaList = new MyBela[6];
        public MyBela[] eamritaBelaList = new MyBela[6];
        public MyBela[] mahendraBelaList = new MyBela[6];
        public MyBela[] emahendraBelaList = new MyBela[6];


        public MySubPanchang[] etithi = new MySubPanchang[3];

        public MySubPanchang[] epuskara = new MySubPanchang[4];
        public MySubPanchang[] jogini = new MySubPanchang[3];
        public MySubPanchang[] ejogini = new MySubPanchang[3];

        public MySubPanchang[] joginiYatra = new MySubPanchang[3];
        public MySubPanchang[] ejoginiYatra = new MySubPanchang[3];

        public MySubPanchang[] food = new MySubPanchang[3];
        public MySubPanchang[] efood = new MySubPanchang[3];

        public MySubPanchang[] enakshetra = new MySubPanchang[3];
        public MySubPanchang[] ejoga = new MySubPanchang[3];
        public MySubPanchang[] ekarana = new MySubPanchang[5];
        public MySubPanchang[] esunSign = new MySubPanchang[2];
        public MySubPanchang[] emoonSign = new MySubPanchang[3];
        public MySubPanchang[] ghataChandra = new MySubPanchang[3];
        public MySubPanchang[] eghataChandra = new MySubPanchang[3];

        public String eghataBar, ghataBar, eRitu, eAyana, ritu, ayana, eshraddha, shraddha, elunarDayAmant, elunarMonthAmant, elunarDayPurimant, elunarMonthPurimant, esolarDay, esolarMonth;
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
