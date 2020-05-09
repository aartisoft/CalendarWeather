package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.mydata.AuspData;
import com.iexamcenter.calendarweather.mydata.AuspWork;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.HelperGrahaKuta;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PanchangListAdapter extends RecyclerView.Adapter<PanchangListAdapter.ItemViewHolder> {
    private Context mContext;
    private HashMap<String, CoreDataHelper> mItems;
    private PrefManager mPref;
    private int mMonthIndex, year, month, dayofmonth;
    private Resources res;
    private String le_rashi_re, le_grahakuta, le_grahakuta5, le_grahakuta6, le_inauspicious_timings, le_auspicious_timings, le_auspicious_work, le_jogin_help1, le_festival, le_raja_darsana, le_mrute, le_tarashuddhi, le_chandrashuddhi, le_nasti, le_puskar, le_shraddha, le_dina, le_samvat, le_sunrise, le_sunset, le_noon, le_day_length, le_moonrise, le_moonset, le_midnight, le_night_length;
    private String le_tithi, le_nakshetra, le_solar_rasi, le_lunar_rashi, le_joga, le_karana, le_amrit_bela, le_mahendra_bela, le_abhijit_bela, le_brahma_bela, le_vajyam_bela;
    private String le_vara_bela, le_kala_bela, le_kalaratri_bela, le_durmuhurta_bela, le_rahu_bela, le_gulika_bela, le_yama_bela, le_time_from;
    String le_muhurta_title;
    double sunDegAtSunrise;
    private String mLang, le_ghata_chandra, le_ghatarashi, le_ghatabar;
    private ArrayList<Helper.lagna> lagnaList;
    public double minTimeDuration = 3.5;

    private HashMap<String, ArrayList<String>> mMonthFestivalmap;
    private int layoutType;
    private String le_jogin, le_food, le_lagna_at_sunrise, le_lagna_title, le_time_to, le_time_next;
    int mCalType, daysInMonth;
    ArrayList<String> myFestivallist;
    private PanchangUtility.MyPanchang myPanchangObj;
    Calendar selectedCal;
    String strShuvaKarja;
    String[] le_arr_month;
    String[] le_arr_muhurta;
    Calendar sunriseCal;
    View shareCntr;
    HashMap<Integer, HelperGrahaKuta.houseinfo> houseinfosMap1, houseinfosMap2;
    int mType;


    ArrayList<rangedate> alrangedate;
    ArrayList<AuspData> auspData = AuspWork.setAuspData();
    String[] le_arr_rasi_kundali, le_arr_planet;
    String[] le_arr_ausp_work;
    String le_paksha, le_shakaddha, le_sal, le_ritu;

    public PanchangListAdapter(Context context, HashMap<String, CoreDataHelper> myPanchangHashMap, HashMap<String, ArrayList<String>> monthFestivalmap, int monthIndex, int myear, int mday, int pagePos) {

        mContext = context;
        mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;

        mMonthFestivalmap = monthFestivalmap;
        mMonthIndex = monthIndex;
        year = myear;
        month = monthIndex;
        dayofmonth = mday;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayofmonth);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        Calendar mycal = new GregorianCalendar(year, month, dayofmonth);
        daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        res = mContext.getResources();
        mItems = myPanchangHashMap;
        res = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mLang = mPref.getMyLanguage();

        mCalType = mPref.getClockFormat();

        prepareshuvakarja(year);
    }

    /*
        private void setTitleEng() {
            le_arr_ausp_work = res.getStringArray(R.array.e_arr_ausp_work);
            le_rashi_re =res.getString(R.string.e_rashi_re);
             le_muhurta_title = res.getString(R.string.e_muhurta_title);
            le_muhurta = res.getString(R.string.e_muhurta);
            le_arr_muhurta = res.getStringArray(R.array.e_arr_muhurta);
            le_arr_planet = mContext.getResources().getStringArray(R.array.e_arr_planet);
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);

            le_lagna_at_sunrise = res.getString(R.string.e_lagna_at_sunrise);
            le_lagna_title = res.getString(R.string.e_lagna_title);
            le_time_to = res.getString(R.string.e_time_to);
            le_time_next = res.getString(R.string.e_time_next);
            le_grahakuta = mContext.getResources().getString(R.string.e_grahakuta);
            le_grahakuta5 = mContext.getResources().getString(R.string.e_grahakuta5);
            le_grahakuta6 = mContext.getResources().getString(R.string.e_grahakuta6);


            le_puskar = res.getString(R.string.e_puskar);
            //jogin_help = res.getString(R.string.ejogin_help);
            le_jogin_help1 = res.getString(R.string.e_jogin_help1);
            le_inauspicious_timings = res.getString(R.string.e_inauspicious_timings);
            le_auspicious_timings = res.getString(R.string.e_auspicious_timings);
            le_auspicious_work = res.getString(R.string.e_auspicious_work);
            le_raja_darsana = res.getString(R.string.e_raja_darsana);
            le_mrute = res.getString(R.string.e_mrute);
            le_festival = res.getString(R.string.e_festival);
            le_ghatarashi = res.getString(R.string.e_ghatarashi);
            le_tarashuddhi = res.getString(R.string.e_tarashuddhi);
            le_chandrashuddhi = res.getString(R.string.e_chandrashuddhi);
            le_ghatabar = res.getString(R.string.e_ghatabar);
            le_ghata_chandra = res.getString(R.string.e_ghata_chandra);

            le_dina = res.getString(R.string.e_dina);
            le_shraddha = res.getString(R.string.e_shraddha);

            le_sunrise = res.getString(R.string.e_sunrise);
            le_sunset = res.getString(R.string.e_sunset);
            le_noon = res.getString(R.string.e_noon);
            le_day_length = res.getString(R.string.e_day_length);
            le_moonrise = res.getString(R.string.e_moonrise);
            le_moonset = res.getString(R.string.e_moonset);
            le_midnight = res.getString(R.string.e_midnight);
            le_night_length = res.getString(R.string.e_night_length);
            le_tithi = res.getString(R.string.e_tithi);
            le_nakshetra = res.getString(R.string.e_nakshetra);
            le_solar_rasi = res.getString(R.string.e_solar_rasi);
            le_lunar_rashi = res.getString(R.string.e_lunar_rashi);
            le_joga = res.getString(R.string.e_joga);
            le_karana = res.getString(R.string.e_karana);
            le_amrit_bela = res.getString(R.string.e_amrit_bela);
            le_mahendra_bela = res.getString(R.string.e_mahendra_bela);
            le_abhijit_bela = res.getString(R.string.e_abhijit_bela);
            le_brahma_bela = res.getString(R.string.e_brahma_bela);
            le_vajyam_bela = res.getString(R.string.e_vajyam_bela);
            le_durmuhurta_bela = res.getString(R.string.e_durmuhurta_bela);
            le_rahu_bela = res.getString(R.string.e_rahu_bela);
            le_gulika_bela = res.getString(R.string.e_gulika_bela);
            le_yama_bela = res.getString(R.string.e_yama_bela);
            le_time_from = res.getString(R.string.e_time_from);
            time_to = res.getString(R.string.e_time_to);
            time_next = res.getString(R.string.e_time_next);
            le_jogin = res.getString(R.string.e_jogin);
            le_food = res.getString(R.string.e_food);
            le_vara_bela = res.getString(R.string.e_vara_bela);
            le_kala_bela = res.getString(R.string.e_kala_bela);
            le_kalaratri_bela = res.getString(R.string.e_kalaratri_bela);
        }
    */
    public void getGrahakuta(TextView grahakutaTxt) {
        StringBuilder str = new StringBuilder();
        int sizeAtSunrise = 0, sizeAtMidnight = 0;
        try {
            int size1 = houseinfosMap1.size();
            System.out.println(":GRAHAKUTA::1::" + size1);
            for (int i = 0; i < size1; i++) {
                HelperGrahaKuta.houseinfo objAtSunrise = houseinfosMap1.get(i);
                HelperGrahaKuta.houseinfo objAtMidnight = houseinfosMap2.get(i);

                if (objAtSunrise == null) {
                    grahakutaTxt.setVisibility(View.GONE);
                    return;
                } else if (objAtSunrise.planetList != null) {
                    //System.out.println(":GRAHAKUTA::2::" +obj.planetList.size());
                    sizeAtSunrise = objAtSunrise.planetList.size();
                    sizeAtMidnight = objAtMidnight.planetList.size();
                    String rashi = "";
                    if (sizeAtSunrise == 5) {
                        rashi = le_arr_rasi_kundali[objAtSunrise.rashi];
                        for (int j = 0; j < sizeAtSunrise; j++) {
                            int index = objAtSunrise.planetList.get(j);
                            str.append(le_arr_planet[index]).append(", ");
                        }
                        grahakutaTxt.setVisibility(View.VISIBLE);
                        String str1 = str.toString().trim().replaceAll(",$", "");
                        grahakutaTxt.setText(makeSpannable(le_grahakuta, rashi + "" + le_rashi_re + " " + le_grahakuta5 + " (" + str1 + ")", R.color.myColor6));

                    } else if (sizeAtSunrise == 6) {
                        rashi = le_arr_rasi_kundali[objAtSunrise.rashi];
                        for (int j = 0; j < sizeAtSunrise; j++) {
                            int index = objAtSunrise.planetList.get(j);
                            str.append(le_arr_planet[index]).append(", ");
                        }

                        grahakutaTxt.setVisibility(View.VISIBLE);
                        String str1 = str.toString().trim().replaceAll(",$", "");
                        grahakutaTxt.setText(makeSpannable(le_grahakuta, rashi + " " + le_rashi_re + " " + le_grahakuta6 + " (" + str1 + ")", R.color.myColor6));

                    } else if (sizeAtMidnight == 5) {
                        rashi = le_arr_rasi_kundali[objAtMidnight.rashi];
                        for (int j = 0; j < sizeAtMidnight; j++) {
                            int index = objAtMidnight.planetList.get(j);
                            str.append(le_arr_planet[index]).append(", ");
                        }
                        grahakutaTxt.setVisibility(View.VISIBLE);
                        String str1 = str.toString().trim().replaceAll(",$", "");
                        grahakutaTxt.setText(makeSpannable(le_grahakuta, rashi + "" + le_rashi_re + " " + le_grahakuta5 + " (" + str1 + ")", R.color.myColor6));

                    } else if (sizeAtMidnight == 6) {
                        rashi = le_arr_rasi_kundali[objAtMidnight.rashi];
                        for (int j = 0; j < sizeAtMidnight; j++) {
                            int index = objAtMidnight.planetList.get(j);
                            str.append(le_arr_planet[index]).append(", ");
                        }

                        grahakutaTxt.setVisibility(View.VISIBLE);
                        String str1 = str.toString().trim().replaceAll(",$", "");
                        grahakutaTxt.setText(makeSpannable(le_grahakuta, rashi + " " + le_rashi_re + " " + le_grahakuta6 + " (" + str1 + ")", R.color.myColor6));

                    }

                    //    System.out.println(":GRAHAKUTA::::" + houseinfosMap.get(i).planetList.size() + "::" + houseinfosMap.get(i).houseno);

                }

            }
        } catch (Exception e) {
            System.out.println(selectedCal.get(Calendar.DAY_OF_MONTH) + ":GRAHAKUTA::xy::");
            e.printStackTrace();
        }
        if (str.toString().isEmpty()) {
            System.out.println(selectedCal.get(Calendar.DAY_OF_MONTH) + ":GRAHAKUTA::xz::");
            grahakutaTxt.setVisibility(View.GONE);
        }


    }


    private void setTitleReg() {
        if (mType == 0) {
            le_paksha = res.getString(R.string.l_paksha);
            le_shakaddha = res.getString(R.string.l_shakaddha);
            le_sal = res.getString(R.string.l_sal);
            le_ritu = res.getString(R.string.l_ritu);
            le_rashi_re = res.getString(R.string.l_rashi_re);
            le_arr_ausp_work = res.getStringArray(R.array.l_arr_ausp_work);
            le_muhurta_title = res.getString(R.string.l_muhurta_title);
            //le_muhurta = res.getString(R.string.l_muhurta);
            le_arr_muhurta = res.getStringArray(R.array.l_arr_muhurta);
            le_arr_planet = res.getStringArray(R.array.l_arr_planet);
            le_arr_rasi_kundali = res.getStringArray(R.array.l_arr_rasi_kundali);
            le_grahakuta = res.getString(R.string.l_grahakuta);
            le_grahakuta5 = res.getString(R.string.l_grahakuta5);
            le_grahakuta6 = res.getString(R.string.l_grahakuta6);
            le_lagna_at_sunrise = res.getString(R.string.l_lagna_at_sunrise);
            le_lagna_title = res.getString(R.string.l_lagna_title);
            le_time_to = res.getString(R.string.l_time_to);
            le_time_next = res.getString(R.string.l_time_next);
            le_arr_month = res.getStringArray(R.array.l_arr_month);
            le_puskar = res.getString(R.string.l_puskar);
            //jogin_help = res.getString(R.string.jogin_help);
            le_jogin_help1 = res.getString(R.string.l_jogin_help1);
            le_inauspicious_timings = res.getString(R.string.l_inauspicious_timings);
            le_auspicious_timings = res.getString(R.string.l_auspicious_timings);
            le_auspicious_work = res.getString(R.string.l_auspicious_work);
            le_raja_darsana = res.getString(R.string.l_raja_darsana);
            le_mrute = res.getString(R.string.l_mrute);
            le_nasti = res.getString(R.string.l_nasti);
            le_festival = res.getString(R.string.l_festival);
            le_ghatarashi = res.getString(R.string.l_ghatarashi);
            le_tarashuddhi = res.getString(R.string.l_tarashuddhi);
            le_chandrashuddhi = res.getString(R.string.l_chandrashuddhi);
            le_ghatabar = res.getString(R.string.l_ghatabar);
            le_ghata_chandra = res.getString(R.string.l_ghata_chandra);
            le_dina = res.getString(R.string.l_dina);
            le_shraddha = res.getString(R.string.l_shraddha);
            le_samvat = res.getString(R.string.l_samvat);
            le_sunrise = res.getString(R.string.l_sunrise);
            le_sunset = res.getString(R.string.l_sunset);
            le_noon = res.getString(R.string.l_noon);
            le_day_length = res.getString(R.string.l_day_length);
            le_moonrise = res.getString(R.string.l_moonrise);
            le_moonset = res.getString(R.string.l_moonset);
            le_midnight = res.getString(R.string.l_midnight);
            le_night_length = res.getString(R.string.l_night_length);
            le_tithi = res.getString(R.string.l_tithi);
            le_nakshetra = res.getString(R.string.l_nakshetra);
            le_solar_rasi = res.getString(R.string.l_solar_rasi);
            le_lunar_rashi = res.getString(R.string.l_lunar_rashi);
            le_joga = res.getString(R.string.l_joga);
            le_karana = res.getString(R.string.l_karana);
            le_amrit_bela = res.getString(R.string.l_amrit_bela);
            le_mahendra_bela = res.getString(R.string.l_mahendra_bela);
            le_abhijit_bela = res.getString(R.string.l_abhijit_bela);
            le_brahma_bela = res.getString(R.string.l_brahma_bela);
            le_vajyam_bela = res.getString(R.string.l_vajyam_bela);
            le_durmuhurta_bela = res.getString(R.string.l_durmuhurta_bela);
            le_rahu_bela = res.getString(R.string.l_rahu_bela);
            le_gulika_bela = res.getString(R.string.l_gulika_bela);
            le_yama_bela = res.getString(R.string.l_yama_bela);
            le_time_from = res.getString(R.string.l_time_from);
            // time_to = res.getString(R.string.l_time_to);
            // time_next = res.getString(R.string.l_time_next);
            le_jogin = res.getString(R.string.l_jogin);
            le_food = res.getString(R.string.l_food);
            le_vara_bela = res.getString(R.string.l_vara_bela);
            le_kala_bela = res.getString(R.string.l_kala_bela);
            le_kalaratri_bela = res.getString(R.string.l_kalaratri_bela);
        } else {
            le_paksha = res.getString(R.string.e_paksha);
            le_shakaddha = res.getString(R.string.e_shakaddha);
            le_sal = res.getString(R.string.e_sal);
            le_ritu = res.getString(R.string.e_ritu);
            le_rashi_re = res.getString(R.string.e_rashi_re);
            le_arr_ausp_work = res.getStringArray(R.array.e_arr_ausp_work);
            le_muhurta_title = res.getString(R.string.e_muhurta_title);
            //le_muhurta = res.getString(R.string.e_muhurta);
            le_arr_muhurta = res.getStringArray(R.array.e_arr_muhurta);
            le_arr_planet = res.getStringArray(R.array.e_arr_planet);
            le_arr_rasi_kundali = res.getStringArray(R.array.e_arr_rasi_kundali);
            le_grahakuta = res.getString(R.string.e_grahakuta);
            le_grahakuta5 = res.getString(R.string.e_grahakuta5);
            le_grahakuta6 = res.getString(R.string.e_grahakuta6);
            le_lagna_at_sunrise = res.getString(R.string.e_lagna_at_sunrise);
            le_lagna_title = res.getString(R.string.e_lagna_title);
            le_time_to = res.getString(R.string.e_time_to);
            le_time_next = res.getString(R.string.e_time_next);
            le_arr_month = res.getStringArray(R.array.l_arr_month);
            le_puskar = res.getString(R.string.e_puskar);
            //jogin_help = res.getString(R.string.jogin_help);
            le_jogin_help1 = res.getString(R.string.e_jogin_help1);
            le_inauspicious_timings = res.getString(R.string.e_inauspicious_timings);
            le_auspicious_timings = res.getString(R.string.e_auspicious_timings);
            le_auspicious_work = res.getString(R.string.e_auspicious_work);
            le_raja_darsana = res.getString(R.string.e_raja_darsana);
            le_mrute = res.getString(R.string.e_mrute);
            le_nasti = res.getString(R.string.e_nasti);
            le_festival = res.getString(R.string.e_festival);
            le_ghatarashi = res.getString(R.string.e_ghatarashi);
            le_tarashuddhi = res.getString(R.string.e_tarashuddhi);
            le_chandrashuddhi = res.getString(R.string.e_chandrashuddhi);
            le_ghatabar = res.getString(R.string.e_ghatabar);
            le_ghata_chandra = res.getString(R.string.e_ghata_chandra);
            le_dina = res.getString(R.string.e_dina);
            le_shraddha = res.getString(R.string.e_shraddha);
            le_samvat = res.getString(R.string.e_samvat);
            le_sunrise = res.getString(R.string.e_sunrise);
            le_sunset = res.getString(R.string.e_sunset);
            le_noon = res.getString(R.string.e_noon);
            le_day_length = res.getString(R.string.e_day_length);
            le_moonrise = res.getString(R.string.e_moonrise);
            le_moonset = res.getString(R.string.e_moonset);
            le_midnight = res.getString(R.string.e_midnight);
            le_night_length = res.getString(R.string.e_night_length);
            le_tithi = res.getString(R.string.e_tithi);
            le_nakshetra = res.getString(R.string.e_nakshetra);
            le_solar_rasi = res.getString(R.string.e_solar_rasi);
            le_lunar_rashi = res.getString(R.string.e_lunar_rashi);
            le_joga = res.getString(R.string.e_joga);
            le_karana = res.getString(R.string.e_karana);
            le_amrit_bela = res.getString(R.string.e_amrit_bela);
            le_mahendra_bela = res.getString(R.string.e_mahendra_bela);
            le_abhijit_bela = res.getString(R.string.e_abhijit_bela);
            le_brahma_bela = res.getString(R.string.e_brahma_bela);
            le_vajyam_bela = res.getString(R.string.e_vajyam_bela);
            le_durmuhurta_bela = res.getString(R.string.e_durmuhurta_bela);
            le_rahu_bela = res.getString(R.string.e_rahu_bela);
            le_gulika_bela = res.getString(R.string.e_gulika_bela);
            le_yama_bela = res.getString(R.string.e_yama_bela);
            le_time_from = res.getString(R.string.e_time_from);
            // time_to = res.getString(R.string.e_time_to);
            // time_next = res.getString(R.string.e_time_next);
            le_jogin = res.getString(R.string.e_jogin);
            le_food = res.getString(R.string.e_food);
            le_vara_bela = res.getString(R.string.e_vara_bela);
            le_kala_bela = res.getString(R.string.e_kala_bela);
            le_kalaratri_bela = res.getString(R.string.e_kalaratri_bela);
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        layoutRes = R.layout.inflate_panchanglist_reg;
       /*
        if (mPref.getMyLanguage().contains("en")) {
            layoutType = 1;
        } else {

            if (!CalendarWeatherApp.isPanchangEng) {
                layoutType = 2;

            } else {
                layoutType = 1;
            }


        }*/
        layoutType = mType;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(PanchangListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        try {
            setPanchnang(position, holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return daysInMonth;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView dateTxt, shubhakama, auspicious_timings, inauspicious_timings;

        TextView grahakuta, muhurta_title, muhurta, sarana, jogin_help, rajadarsan, chandrashuddhi, tarashuddhi, shubhakamalist, jogin, food, puskar, amritaBela, mahendarBela, fest, shraddha, txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12, txt13, txt14, txt15, txt16, txt17, txt18, txt19, txt20, txt21, txt22, txt24, txt25, txt26, txt27, txt28, txt29, txt30, txt31, txt32, txt33;
        TextView lagna_title, lagna_timings, lagna1, ghataChandra, ghataBar;
        LinearLayout cnter4, cnter5, cnter7, cnter6, cnter8;
        View divider4, divider5, divider6, divider7, divider8;

        public ItemViewHolder(View itemView) {
            super(itemView);
            grahakuta = itemView.findViewById(R.id.grahakuta);
            lagna_timings = itemView.findViewById(R.id.lagna_timings);
            lagna1 = itemView.findViewById(R.id.lagna1);
            lagna_title = itemView.findViewById(R.id.lagna_title);
            muhurta = itemView.findViewById(R.id.muhurta);
            muhurta_title = itemView.findViewById(R.id.muhurta_title);
            cnter4 = itemView.findViewById(R.id.cntr4);
            cnter5 = itemView.findViewById(R.id.cntr5);
            cnter6 = itemView.findViewById(R.id.cntr6);
            cnter7 = itemView.findViewById(R.id.cntr7);
            cnter8 = itemView.findViewById(R.id.cntr8);
            divider4 = itemView.findViewById(R.id.divider4);
            divider5 = itemView.findViewById(R.id.divider5);
            divider6 = itemView.findViewById(R.id.divider6);
            divider7 = itemView.findViewById(R.id.divider7);
            divider8 = itemView.findViewById(R.id.divider8);


            jogin_help = itemView.findViewById(R.id.jogin_help);
            tarashuddhi = itemView.findViewById(R.id.tarashuddhi);
            rajadarsan = itemView.findViewById(R.id.rajadarsan);
            chandrashuddhi = itemView.findViewById(R.id.chandrashuddhi);
            ghataBar = itemView.findViewById(R.id.ghataBar);
            shubhakamalist = itemView.findViewById(R.id.shubhakamalist);
            shubhakama = itemView.findViewById(R.id.shubhakama);
            auspicious_timings = itemView.findViewById(R.id.auspicious_timings);
            inauspicious_timings = itemView.findViewById(R.id.inauspicious_timings);
            ghataChandra = itemView.findViewById(R.id.ghataChandra);

            puskar = itemView.findViewById(R.id.puskar);
            amritaBela = itemView.findViewById(R.id.amritaBela);
            mahendarBela = itemView.findViewById(R.id.mahendarBela);

            fest = itemView.findViewById(R.id.fest1);
            sarana = itemView.findViewById(R.id.sarana);
            shraddha = itemView.findViewById(R.id.shraddha);
            jogin = itemView.findViewById(R.id.jogin);
            food = itemView.findViewById(R.id.food);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
            txt4 = itemView.findViewById(R.id.txt4);
            txt5 = itemView.findViewById(R.id.txt5);
            txt6 = itemView.findViewById(R.id.txt6);
            txt7 = itemView.findViewById(R.id.txt7);
            txt8 = itemView.findViewById(R.id.txt8);
            txt9 = itemView.findViewById(R.id.txt9);
            txt10 = itemView.findViewById(R.id.txt10);
            txt11 = itemView.findViewById(R.id.txt11);
            txt12 = itemView.findViewById(R.id.txt12);
            txt13 = itemView.findViewById(R.id.txt13);
            txt14 = itemView.findViewById(R.id.txt14);
            txt15 = itemView.findViewById(R.id.txt15);
            txt16 = itemView.findViewById(R.id.txt16);
            txt17 = itemView.findViewById(R.id.txt17);
            txt18 = itemView.findViewById(R.id.txt18);
            txt19 = itemView.findViewById(R.id.txt19);
            txt20 = itemView.findViewById(R.id.txt20);
            txt21 = itemView.findViewById(R.id.txt21);
            txt22 = itemView.findViewById(R.id.txt22);
            txt24 = itemView.findViewById(R.id.txt24);
            txt25 = itemView.findViewById(R.id.txt25);
            txt26 = itemView.findViewById(R.id.txt26);
            txt27 = itemView.findViewById(R.id.txt27);
            txt28 = itemView.findViewById(R.id.txt28);
            txt29 = itemView.findViewById(R.id.txt29);
            txt30 = itemView.findViewById(R.id.txt30);
            txt31 = itemView.findViewById(R.id.txt31);
            txt32 = itemView.findViewById(R.id.txt32);
            txt33 = itemView.findViewById(R.id.txt33);
            dateTxt = itemView.findViewById(R.id.dateTxt);
        }


    }


    public String getLatLng(double deg) {
        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        double fractionInSec = (deg - val) * 3600;

        long min = Math.round(fractionInSec) / 60;
        long sec = Math.round(fractionInSec) % 60;

        String remDegStr = "" + remDeg;
        String minStr = "" + min;
        String secStr = "" + sec;
        if (layoutType != 1) {
            remDegStr = Utility.getInstance(mContext).getDayNo("" + remDeg);
            minStr = Utility.getInstance(mContext).getDayNo("" + min);
            secStr = Utility.getInstance(mContext).getDayNo("" + sec);
        }
        return remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";

    }

    public void setRegional(ItemViewHolder holder) {


        PanchangUtility.MySubPanchang[] tithiArr = myPanchangObj.le_tithi;
        PanchangUtility.MySubPanchang[] jogaArr = myPanchangObj.le_joga;
        PanchangUtility.MySubPanchang[] puskaraArr = myPanchangObj.le_puskara;
        PanchangUtility.MySubPanchang[] joginiArr = myPanchangObj.le_jogini;
        PanchangUtility.MySubPanchang[] joginiYatraArr = myPanchangObj.le_joginiYatra;

        PanchangUtility.MySubPanchang[] foodArr = myPanchangObj.le_food;
        PanchangUtility.MySubPanchang[] nakshetraArr = myPanchangObj.le_nakshetra;
        PanchangUtility.MySubPanchang[] karanaArr = myPanchangObj.le_karana;
        PanchangUtility.MySubPanchang[] sunSignArr = myPanchangObj.le_sunSign;
        PanchangUtility.MySubPanchang[] moonSignArr = myPanchangObj.le_moonSign;
        PanchangUtility.MySubPanchang[] ghataChandraArr = myPanchangObj.le_ghataChandra;
        PanchangUtility.MyBela[] durmuhurtaBelaArr = myPanchangObj.le_durMuhurtaBela;
        PanchangUtility.MyBela[] varjyamBelaArr = myPanchangObj.le_varjyamBela;
        PanchangUtility.MyBela[] rahuBelaArr = myPanchangObj.le_rahuBela;
        PanchangUtility.MyBela[] gulikaBelaArr = myPanchangObj.le_gulikaBela;
        PanchangUtility.MyBela[] yamaBelaArr = myPanchangObj.le_yamaBela;
        PanchangUtility.MyBela[] abhijitBelaArr = myPanchangObj.le_abhijitBela;
        PanchangUtility.MyBela[] brahmaBelaArr = myPanchangObj.le_brahmaBela;
        PanchangUtility.MyBela[] amritaBelaListArr = myPanchangObj.le_amritaBelaList;
        PanchangUtility.MyBela[] varaBelaArr = myPanchangObj.le_varaBela;
        PanchangUtility.MyBela[] kalaBelaArr = myPanchangObj.le_kalaBela;
        PanchangUtility.MyBela[] mahendraBelaListArr = myPanchangObj.le_mahendraBelaList;
        PanchangUtility.MyBela[] kalaRatriBelaArr = myPanchangObj.le_kalaRatriBela;

        StringBuilder joginiStr = getPanchangaValue(joginiArr);
        StringBuilder joginiYatraStr = getPanchangaValue(joginiYatraArr);
        StringBuilder puskaraStr = getPanchangaValue(puskaraArr);
        StringBuilder tithiStr = getPanchangaValue(tithiArr);
        StringBuilder foodStr = getPanchangaValue(foodArr);
        StringBuilder nakshetraStr = getPanchangaValue(nakshetraArr);
        StringBuilder jogaStr = getPanchangaValue(jogaArr);
        StringBuilder karanaStr = getPanchangaValue(karanaArr);
        StringBuilder sunSignStr = getPanchangaValue(sunSignArr);
        StringBuilder moonSignStr = getPanchangaValue(moonSignArr);
        StringBuilder ghataChandraStr = getPanchangaValue(ghataChandraArr);
        StringBuilder varjyamBelaStr = getPanchangaValue(varjyamBelaArr);
        StringBuilder rahuBelaStr = getPanchangaValue(rahuBelaArr);
        StringBuilder gulikaBelaStr = getPanchangaValue(gulikaBelaArr);
        StringBuilder yamaBelaStr = getPanchangaValue(yamaBelaArr);
        StringBuilder brahmaBelaString = getPanchangaValue(brahmaBelaArr);
        StringBuilder abhijitBelaString = getPanchangaValue(abhijitBelaArr);
        StringBuilder durmuhurtaBelaStr = getPanchangaValue(durmuhurtaBelaArr);
        StringBuilder amritaBelaListStr = getPanchangaValue(amritaBelaListArr);
        StringBuilder kalaRatriBelaStr = getePanchangaValue(kalaRatriBelaArr);
        StringBuilder varaBelaStr = getPanchangaValue(varaBelaArr);
        StringBuilder kalaBelaStr = getPanchangaValue(kalaBelaArr);
        StringBuilder mahendraBelaListStr = getPanchangaValue(mahendraBelaListArr);


        String puskarStr;
        System.out.println("puskaraStrpuskaraStr:re:" + puskaraStr);
        if (puskaraStr.toString().isEmpty()) {
            puskarStr = le_nasti;
        } else {
            puskarStr = le_mrute + puskaraStr;
        }


        String ghataChandraSpanStr;

        if (ghataChandraStr.toString().isEmpty() || (ghataChandraStr.toString().contains(le_nasti) && !ghataChandraStr.toString().contains(","))) {
            ghataChandraSpanStr = le_nasti;

        } else {

            ghataChandraSpanStr = ghataChandraStr + " " + le_ghatarashi;
        }
        calculateMuhurta(holder, sunriseCal);
        getGrahakuta(holder.grahakuta);

        // calculateLagna(holder, myPanchangObj.sunRise);
        holder.amritaBela.setText(makeSpannable(le_amrit_bela, amritaBelaListStr.toString(), R.color.myColor6));
        holder.rajadarsan.setText(makeSpannable(le_raja_darsana, myPanchangObj.le_rajaDarsan, R.color.myColor4));
        holder.puskar.setText(makeSpannable(le_puskar, puskarStr, R.color.myColor4));
        holder.shraddha.setText(makeSpannable(le_shraddha, myPanchangObj.le_shraddha, R.color.myColor4));

        holder.ghataBar.setText(makeSpannable(le_ghatabar, myPanchangObj.le_ghataBar, R.color.myColor5));

        holder.ghataChandra.setText(makeSpannable(le_ghata_chandra, ghataChandraSpanStr, R.color.myColor5));
        holder.tarashuddhi.setText(makeSpannable(le_tarashuddhi, myPanchangObj.le_tarashuddhi, R.color.myColor5));
        holder.chandrashuddhi.setText(makeSpannable(le_chandrashuddhi, myPanchangObj.le_chandrashuddhi, R.color.myColor5));

        holder.jogin.setText(makeSpannable(le_jogin, joginiStr.toString(), R.color.myColor5));
        //  holder.jogin_help.setText(jogin_help);
        holder.jogin_help.setText(joginiYatraStr.toString() + le_jogin_help1);

        holder.shubhakama.setText(le_auspicious_work);
        holder.auspicious_timings.setText(le_auspicious_timings);
        holder.inauspicious_timings.setText(le_inauspicious_timings);


        holder.food.setText(makeSpannable(le_food, foodStr.toString(), R.color.myColor4));

        holder.mahendarBela.setText(makeSpannable(le_mahendra_bela, mahendraBelaListStr.toString(), R.color.myColor6));

        holder.txt6.setText(makeSpannable(le_day_length, myPanchangObj.le_dayLength, R.color.myColor2));
        holder.txt7.setText(makeSpannable(le_moonrise, myPanchangObj.le_moonRise, R.color.myColor2));

        holder.txt3.setText(makeSpannable(le_sunrise, myPanchangObj.le_sunRise, R.color.myColor2));

        holder.txt4.setText(makeSpannable(le_sunset, myPanchangObj.le_sunSet, R.color.myColor2));

        holder.txt5.setText(makeSpannable(le_noon, myPanchangObj.le_sunTransit, R.color.myColor2));
        holder.txt10.setText(makeSpannable(le_night_length, myPanchangObj.le_nightLength, R.color.myColor2));
        holder.txt16.setText(makeSpannable(le_tithi, tithiStr.toString(), R.color.myColor3));
        holder.txt18.setText(makeSpannable(le_nakshetra, nakshetraStr.toString(), R.color.myColor3));
        holder.txt19.setText(makeSpannable(le_solar_rasi, sunSignStr.toString(), R.color.myColor3));
        holder.txt20.setText(makeSpannable(le_lunar_rashi, moonSignStr.toString(), R.color.myColor3));
        holder.txt21.setText(makeSpannable(le_joga, jogaStr.toString(), R.color.myColor3));
        holder.txt22.setText(makeSpannable(le_karana, karanaStr.toString(), R.color.myColor3));
        holder.txt24.setText(makeSpannable(le_abhijit_bela, abhijitBelaString.toString(), R.color.myColor6));
        holder.txt25.setText(makeSpannable(le_brahma_bela, brahmaBelaString.toString(), R.color.myColor6));
        holder.txt26.setText(makeSpannable(le_vajyam_bela, varjyamBelaStr.toString(), R.color.myColor7));
        holder.txt27.setText(makeSpannable(le_durmuhurta_bela, durmuhurtaBelaStr.toString(), R.color.myColor7));
        holder.txt28.setText(makeSpannable(le_rahu_bela, rahuBelaStr.toString(), R.color.myColor7));
        holder.txt29.setText(makeSpannable(le_gulika_bela, gulikaBelaStr.toString(), R.color.myColor7));
        holder.txt30.setText(makeSpannable(le_yama_bela, yamaBelaStr.toString(), R.color.myColor7));
        holder.txt31.setText(makeSpannable(le_vara_bela, varaBelaStr.toString(), R.color.myColor7));
        holder.txt32.setText(makeSpannable(le_kala_bela, kalaBelaStr.toString(), R.color.myColor7));
        holder.txt33.setText(makeSpannable(le_kalaratri_bela, kalaRatriBelaStr.toString(), R.color.myColor7));
        holder.txt9.setText(makeSpannable(le_midnight, myPanchangObj.le_midNight, R.color.myColor2));
        holder.txt8.setText(makeSpannable(le_moonset, myPanchangObj.le_moonSet, R.color.myColor2));
        String lgajapati;

        String lDay = Utility.getInstance(mContext).getDayNo(myPanchangObj.le_day);
        String lYear = Utility.getInstance(mContext).getDayNo(myPanchangObj.le_year);


        String headerStr;
        if (!myPanchangObj.le_sanSalAnka.equals("0") && mPref.getMyLanguage().contains("or")) {
            // ok down to 1970, chnage cond before 1970 added
            lgajapati = mType == 0 ? ", ଗଜପତ୍ୟାଙ୍କ-" + myPanchangObj.le_sanSalAnka : ", Gajapatyanka-" + myPanchangObj.le_sanSalAnka;
            headerStr = lDay + " " + myPanchangObj.le_month + " " + lYear + ", " + le_samvat + "-" + myPanchangObj.le_samvata + ", " + le_shakaddha + "-" + myPanchangObj.le_sakaddha + ", " + le_sal + "-" + myPanchangObj.le_sanSal + lgajapati + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_bara;
        } else {
            headerStr = lDay + " " + myPanchangObj.le_month + " " + lYear + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_sakaddha + " " + le_shakaddha;

        }
        holder.dateTxt.setText(headerStr);
        // holder.cnter6.setVisibility(View.VISIBLE);
        //  holder.divider6.setVisibility(View.VISIBLE);
        if (myFestivallist != null && myFestivallist.size() > 0) {
            holder.fest.setVisibility(View.VISIBLE);
            holder.sarana.setVisibility(View.VISIBLE);
            holder.divider6.setVisibility(View.VISIBLE);
            String specialcode = myFestivallist.get(2);
            String festVal = mType == 0 ? myFestivallist.get(0) : myFestivallist.get(1);
            if ((specialcode.startsWith("101") || specialcode.startsWith("1001") || specialcode.startsWith("10001")) && mPref.getMyLanguage().contains("or") && !CalendarWeatherApp.isPanchangEng) {
                holder.sarana.setVisibility(View.VISIBLE);
                festVal = festVal.replace(specialcode.split("@@")[1] + ",", "");
                festVal = festVal.replace(specialcode.split("@@")[1], "");


                PanchangUtility.MySubPanchang[] moonSign = myPanchangObj.le_moonSign;
                if (specialcode.startsWith("101"))
                    holder.sarana.setText(makeSpannable("ଶରଣ ପଞ୍ଚକ", moonSign[0].time + " ଗତେ " + specialcode.split("@@")[1], R.color.myColor7));
                else if (specialcode.startsWith("1001"))
                    holder.sarana.setText(makeSpannable("ଶରଣ ପଞ୍ଚକ", moonSign[0].time + " ଗତେ " + specialcode.split("@@")[1], R.color.myColor7));
                else if (specialcode.startsWith("10001"))
                    holder.sarana.setText(makeSpannable("ଶରଣ ପଞ୍ଚକ", specialcode.split("@@")[1], R.color.myColor7));

            } else {
                holder.sarana.setVisibility(View.GONE);
            }
            if (myPanchangObj.lunarMonthType != 0) {
                holder.fest.setAlpha(0.5f);
                //  holder.fest.setBackgroundColor(Color.RED);
            } else {
                holder.fest.setAlpha(1f);
                // holder.fest.setBackgroundColor(Color.GREEN);
            }
            holder.fest.setText(makeSpannable(le_festival, festVal, R.color.myColor7));
            // holder.fest.setText(festVal);

        } else {
            holder.fest.setVisibility(View.GONE);
            holder.sarana.setVisibility(View.GONE);
            holder.divider6.setVisibility(View.GONE);

        }


        calculateLagna(holder);

        holder.cnter8.setVisibility(View.VISIBLE);
        holder.divider8.setVisibility(View.VISIBLE);
        if (mPref.getMyLanguage().contains("or")) {

            String shuvaKarya = setShuvaKaryaReg();
            if (shuvaKarya.isEmpty()) {
                holder.cnter7.setVisibility(View.GONE);
                holder.divider7.setVisibility(View.GONE);
            } else {
                holder.cnter7.setVisibility(View.VISIBLE);
                holder.divider7.setVisibility(View.VISIBLE);
                holder.shubhakamalist.setText(shuvaKarya);
            }
        }
    }

    private void calculateMuhurta(ItemViewHolder holder, Calendar sunrisecal) {

        String endF;
        String muhurtaStr = "";
        String sunriseTime;
        String total = "30";
        if (layoutType != 1) {
            sunriseTime = le_sunrise + " " + getFormattedDate(sunrisecal);
            total = Utility.getInstance(mContext).getDayNo("" + 30);

        } else {
            sunriseTime = le_sunrise + " " + geteFormattedDate(sunrisecal);
        }
        for (int i = 0; i < 30; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sunrisecal.getTimeInMillis());
            cal.add(Calendar.MINUTE, (i * 48));
            Calendar cal1 = Calendar.getInstance();
            cal1.setTimeInMillis(cal.getTimeInMillis() + 48 * 60 * 1000);
            // String muhurtaName = muhurtaArr[i] + " " + muhurta;
            String muhurtaName = le_arr_muhurta[i];
            String color = "";
            if (i == 25 || i == 28) {
                color = "#ff0000";
            } else if (i == 0 || i == 1 || i == 3 || i == 9 || i == 10 || i == 11 || i == 14 || i == 15 || i == 16 || i == 20) {
                color = "#00447C";
            } else {
                color = "#ff8c00";
            }

            if (layoutType != 1) {
                endF = getFormattedDate(cal1);
                if (i != 29)
                    muhurtaStr = muhurtaStr + " <font color='" + color + "'>" + muhurtaName + "</font> " + endF + " " + le_time_to + ", " + le_time_next + " ";
                else
                    muhurtaStr = muhurtaStr + " <font color='" + color + "'>" + muhurtaName + "</font> " + endF + " " + le_time_to;
            } else {
                endF = geteFormattedDate(cal1);
                if (i != 29)
                    muhurtaStr = muhurtaStr + " <font color='" + color + "'>" + muhurtaName + "</font> " + " " + le_time_to + " " + endF + ", " + le_time_next + " ";
                else
                    muhurtaStr = muhurtaStr + " <font color='" + color + "'>" + muhurtaName + "</font> " + " " + le_time_to + " " + endF;

            }
        }

        String title = "<font color='#007DB8'><strong>" + le_muhurta_title + "</strong></font><font color='#444444'> (" + sunriseTime + ")</font>";
        holder.muhurta_title.setText(Html.fromHtml(title));
        holder.muhurta.setText(Html.fromHtml(muhurtaStr));
    }

    private void calculateLagna(ItemViewHolder holder) {
        String sunriselagnaval = getLatLng(sunDegAtSunrise);
        holder.lagna_timings.setText(le_lagna_at_sunrise + " " + sunriselagnaval);
        holder.lagna_title.setText(le_lagna_title);
        String lagnaStr1 = "";
        int size = lagnaList.size();
        for (int x = 0; x < size; x++) {
            int rashi = lagnaList.get(x).rashi;
            Calendar end = lagnaList.get(x).end;
            String endF;
            String rashiStr = le_arr_rasi_kundali[rashi];
            if (layoutType != 1) {
                endF = getFormattedDate(end);
                if (x != 12)
                    lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> " + endF + " " + le_time_to + ", " + le_time_next + " ";
                else
                    lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> ";
                // lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> " + endF + " " + timeto;
            } else {
                endF = geteFormattedDate(end);
                if (x != 12)
                    lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> " + " " + le_time_to + " " + endF + ", " + le_time_next + " ";
                else
                    lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> ";
                // lagnaStr1 = lagnaStr1 + "<font color='#e75480'>" + rashiStr + "</font> " + " " + timeto + " " + endF;

            }

        }
        holder.lagna1.setText(Html.fromHtml(lagnaStr1));

    }

    private String geteFormattedDate(Calendar cal) {

        try {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            Date date = cal.getTime();
            DateFormat dateFormat;
            int currDayNo = selectedCal.get(Calendar.DAY_OF_MONTH);
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

        if (mLang.contains("or") && mType == 0) {
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

            int currDayNo = selectedCal.get(Calendar.DAY_OF_MONTH);
            if (mCalType == 0) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + "" + res.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + le_arr_month[calMonth];
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + res.getString(R.string.l_time_min);

                }
            } else if (mCalType == 1) {
                if (currDayNo != calDayNo) {
                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + "" + res.getString(R.string.l_time_min) + " " + calDayNoStr + "/" + le_arr_month[calMonth];

                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + res.getString(R.string.l_time_min);

                }
            } else {
                if (currDayNo != calDayNo) {

                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + "" + res.getString(R.string.l_time_min) + "(+)";
                } else {
                    ldate = prefixTime + " " + calHourNoStr + "" + res.getString(R.string.l_time_hour) + " " + calMinStr + res.getString(R.string.l_time_min);

                }
            }
            return ldate;
        } else {
            return geteFormattedDate(cal);
        }
    }

    private String setShuvaKaryaReg() {
        return strShuvaKarja;

    }

    private void setPanchnang(final int position, final ItemViewHolder holder) {

        try {
// commented temporary
            //if (mPref.getMyLanguage().contains("or")) {
            holder.mahendarBela.setVisibility(View.VISIBLE);
            holder.cnter4.setVisibility(View.VISIBLE);
            holder.cnter5.setVisibility(View.VISIBLE);
            holder.cnter6.setVisibility(View.VISIBLE);
            holder.cnter7.setVisibility(View.VISIBLE);
            holder.divider4.setVisibility(View.VISIBLE);
            holder.divider5.setVisibility(View.VISIBLE);
            holder.divider6.setVisibility(View.VISIBLE);
            holder.divider7.setVisibility(View.VISIBLE);

            holder.txt31.setVisibility(View.VISIBLE);
            holder.txt32.setVisibility(View.VISIBLE);
            holder.txt33.setVisibility(View.VISIBLE);

            setTitleReg();


            String key = year + "-" + mMonthIndex + "-" + (position + 1);

            selectedCal = Calendar.getInstance();
            selectedCal.set(year, mMonthIndex, (position + 1));


            myFestivallist = mMonthFestivalmap.get(key);

            CoreDataHelper myCoreData = mItems.get(key);
            PanchangUtility panchangUtility = new PanchangUtility();


            String sunDeg = myCoreData.getPlanetInfo().sun;
            String sunDmDeg = myCoreData.getPlanetInfo().dmsun;
            double lat = Double.parseDouble(mPref.getLatitude());
            sunDegAtSunrise = Helper.getInstance().getPlanetPos(myCoreData.getSunRise(), sunDeg, Double.parseDouble(sunDmDeg));


            lagnaList = Helper.getInstance().getLagna(myCoreData.getSunRise(), sunDegAtSunrise, lat);

            myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mLang, mCalType, mPref.getLatitude(), mPref.getLongitude(), mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());
            EphemerisEntity planetInfo = myCoreData.getPlanetInfo();

            strShuvaKarja = getshuvakarja(myCoreData, planetInfo, (position + 1), mMonthIndex, year);
            sunriseCal = myCoreData.getSunRise();
            //Log.e("HelperGrahaKuta","HelperGrahaKuta:"+year+"-"+month+"-"+dayofmonth);

            houseinfosMap1 = HelperGrahaKuta.getInstance(mContext).getGhahakuta(selectedCal, myCoreData, planetInfo, mPref.getLatitude(), 0);
            houseinfosMap2 = HelperGrahaKuta.getInstance(mContext).getGhahakuta(selectedCal, myCoreData, planetInfo, mPref.getLatitude(), 1);

            setRegional(holder);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Spannable makeSpannable(String title, String val, Integer colorVal) {
        Spannable span = new SpannableString(title + " - " + val);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(res.getColor(colorVal)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;

    }

    private StringBuilder getPanchangaValue(PanchangUtility.MyBela[] arr) {

        if (mPref.getMyLanguage().contains("or")) {
            StringBuilder arrStr = new StringBuilder();
            try {
                for (PanchangUtility.MyBela obj : arr) {
                    if (obj != null) {
                        String time1 = obj.startTime;
                        String time2 = obj.endTime;
                        arrStr.append(time1).append(" " + le_time_from + " ").append(time2).append(", ");
                    }
                }
                if (arrStr.length() > 2)
                    arrStr = arrStr.replace(arrStr.length() - 2, arrStr.length(), "");
                else
                    arrStr = arrStr.append("-");
            } catch (Exception e) {
                arrStr = arrStr.append("-");
                e.printStackTrace();
            }
            return arrStr;
        } else {
            return getePanchangaValue(arr);
        }
    }

    private StringBuilder getePanchangaValue(PanchangUtility.MyBela[] arr) {

        StringBuilder arrStr = new StringBuilder();
        try {
            for (PanchangUtility.MyBela obj : arr) {
                if (obj != null) {
                    String time1 = obj.startTime;
                    String time2 = obj.endTime;
                    arrStr.append(time1).append(" - ").append(time2).append(", ");
                }
            }
            if (arrStr.length() > 2)
                arrStr = arrStr.replace(arrStr.length() - 2, arrStr.length(), "");
            else
                arrStr = arrStr.append("-");
        } catch (Exception e) {

            arrStr = arrStr.append("-");
            e.printStackTrace();
        }
        return arrStr;
    }


    private StringBuilder getePanchangaValue(PanchangUtility.MySubPanchang[] arr) {

        StringBuilder arrStr = new StringBuilder();
        try {
            if (arr[0] != null && arr[0].time.isEmpty()) {
                arrStr.append(arr[0].name);
            } else {
                for (PanchangUtility.MySubPanchang obj : arr) {
                    String name = obj.name;
                    String time = obj.time;
                    if (!time.equals(""))
                        arrStr.append(name).append(" upto ").append(time).append(",");
                    else if (!name.isEmpty())
                        arrStr.append(le_time_next + " " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String arrStr1 = arrStr.toString().replaceAll(",$", "");
        return new StringBuilder(arrStr1);
    }

    private StringBuilder getPanchangaValue(PanchangUtility.MySubPanchang[] arr) {

        if (mPref.getMyLanguage().contains("or")) {
            StringBuilder arrStr = new StringBuilder();
            try {
                if (arr[0].time.isEmpty()) {
                    arrStr.append(arr[0].name);
                } else {
                    for (PanchangUtility.MySubPanchang obj : arr) {
                        String name = obj.name;
                        String time = obj.time;
                        if (!time.isEmpty())
                            arrStr.append(" " + name + " ").append(time + " ").append(le_time_to).append(",");
                        else if (!name.isEmpty())
                            arrStr.append(le_time_next + " " + name);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String arrStr1 = arrStr.toString().replaceAll(",$", "");
            return new StringBuilder(arrStr1);
        } else {
            return getePanchangaValue(arr);
        }
    }


    public void prepareshuvakarja(int year) {
        callRetrograde("RetroJupiter", year);
        // lrashiList = mContext.getResources().getStringArray(R.array.l_rasi_kundali_arr);
        //  rashiList = mContext.getResources().getStringArray(R.array.rasi_kundali_arr);
        //  erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);


    }

    private double getDuration(Calendar cal1, Calendar cal2, Calendar sunRise) {
        if (cal1 == null || cal2 == null)
            return 0;
        long nextDaySR = sunRise.getTimeInMillis() + 24 * 60 * 60 * 1000;
        if (cal1.getTimeInMillis() > nextDaySR)
            return -1;//next days
        return ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60.0));
    }

    public String getshuvakarja(CoreDataHelper myCoreData, EphemerisEntity planetInfo, int day, int mMonthIndex, int year) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(year, mMonthIndex, (day));
            boolean jupiterBakra = checkDate(cal.getTime());
            PlanetData sunData = calculatePlanetInfo(planetInfo.sun, planetInfo.dmsun, 1, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
            PlanetData venusData = calculatePlanetInfo(planetInfo.venus, planetInfo.dmvenus, 4, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
            PlanetData jupitorData = calculatePlanetInfo(planetInfo.jupitor, planetInfo.dmjupitor, 6, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));

            PlanetData chandraData = calculatePlanetInfo(planetInfo.moon, planetInfo.dmmoon, 2, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
            PlanetData saniData = calculatePlanetInfo(planetInfo.saturn, planetInfo.dmsaturn, 7, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
            PlanetData mangalaData = calculatePlanetInfo(planetInfo.mars, planetInfo.dmmars, 5, year, mMonthIndex, day, myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));

            boolean guruAditya = false;
            boolean singhaGuru = false;
            if (sunData.houseNo.contains(jupitorData.houseNo)) {
                guruAditya = true;
            }
            if (jupitorData.houseNo.contains("5")) {
                singhaGuru = true;
            }
            boolean chandraRabiSaniMangala = false;

            if (chandraData.houseNo.contains(sunData.houseNo) || chandraData.houseNo.contains(saniData.houseNo) || chandraData.houseNo.contains(mangalaData.houseNo)) {
                chandraRabiSaniMangala = true;
            }

            boolean closerSunJupitor = Math.abs(jupitorData.deg - sunData.deg) < 11;
            boolean closerSunVenus;
            if (venusData.direction == 1) {
                closerSunVenus = Math.abs(venusData.deg - sunData.deg) < 8;
            } else {
                closerSunVenus = Math.abs(venusData.deg - sunData.deg) < 10;
            }

            int lunarPurMonthIndex = myCoreData.getLunarMonthPurnimantIndex();
            int pakshaIndex = myCoreData.getPaksha();
            int tithiIndex = myCoreData.getTithi().currVal;

            // 6,8,12 added
            // chaita[1 quarter // shukla pasa astami ru purnami] pausa [no ausp]
            boolean kharaMasa1 = (lunarPurMonthIndex == 8);// pausa
            boolean kharaMasa2 = (lunarPurMonthIndex == 11 && pakshaIndex == 1);// pausa
            boolean kharaMasa3 = (lunarPurMonthIndex == 11 && pakshaIndex == 0 && tithiIndex <= 7);

            boolean kharaMasa = (kharaMasa1 || kharaMasa2 || kharaMasa3);
            Log.e("pakshaIndex", "pakshaIndex:::" + pakshaIndex + "::" + lunarPurMonthIndex + "::" + tithiIndex);
            boolean charturStart0 = (lunarPurMonthIndex == 2 && tithiIndex >= 25);// ashadha
            boolean charturStart1 = (lunarPurMonthIndex == 2 && pakshaIndex == 1);// ashadha krushna
            boolean charturStart2 = (lunarPurMonthIndex == 3 || lunarPurMonthIndex == 4 || lunarPurMonthIndex == 5); // srabana and bhadraba
            boolean charturStart3 = (lunarPurMonthIndex == 6 && pakshaIndex == 1);
            boolean charturStart4 = (lunarPurMonthIndex == 6 && pakshaIndex == 0 && tithiIndex <= 10);// kartika sukla dashami

            boolean isChaturMasa = (charturStart0 || charturStart1 || charturStart2 || charturStart3 || charturStart4);

            int currMoonSignIndex = myCoreData.getMoonSign().currVal;
            int nextMoonSignIndex = myCoreData.getMoonSign().nextVal;
            boolean saranaPanchak1 = (currMoonSignIndex == 9 && nextMoonSignIndex == 10);
            boolean saranaPanchak2 = (currMoonSignIndex == 11 && nextMoonSignIndex == 12);
            boolean saranaPanchak3 = ((!saranaPanchak1 && !saranaPanchak2) && (currMoonSignIndex >= 10 && !(currMoonSignIndex == 11 && nextMoonSignIndex == 12) && currMoonSignIndex < 12));
            boolean isSaranaPanchak = (saranaPanchak1 || saranaPanchak2 || saranaPanchak3);

            boolean malamasa = myCoreData.getmLunarMonthType() == 1;

            long sunsignEnd = myCoreData.getSunSign().currValEndTime.getTimeInMillis();
            boolean masant = false;
            if (sunsignEnd > myCoreData.getMidNight().getTimeInMillis() && (sunsignEnd < myCoreData.getMidNight().getTimeInMillis() + (24 * 60 * 60 * 1000l))) {

                masant = true;

            }

            //   if (lunarPurMonthIndex > 2 && pakshaIndex == 1 && tithiIndex >= 25)
            //     Log.e("LunarMonth", "xxx:LunarMonth:m:" + myCoreData.getmDay() + "-" + myCoreData.getmMonth() + "-" + myCoreData.getmYear() + ":" + myCoreData.getLunarMonthPurnimantIndex() + ":p:" + myCoreData.getPaksha() + ":pp:" + myCoreData.getTithi().currVal);
            //2  asadha
            // 1 krushna

            //  myCoreData.getPaksha();
            //  asadha kunsha ekadasi to kartika shukla dashami
            //  sarana panchak
            int paksha = myCoreData.getPaksha() + 1;
            // int souraMasa = myCoreData.getAdjustSolarMonth();
            int chandraMasa = myCoreData.getLunarMonthPurnimantIndex() + 1;
            int bara = myCoreData.getDayOfWeek();
            int tithi1 = myCoreData.getTithi().currVal < 15 ? myCoreData.getTithi().currVal : (myCoreData.getTithi().currVal - 15);
            double tithi1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getTithi().currValEndTime, myCoreData.getSunRiseCal());
            int tithi2 = myCoreData.getTithi().nextVal < 15 ? myCoreData.getTithi().nextVal : (myCoreData.getTithi().nextVal - 15);
            double tithi2Duration = getDuration(myCoreData.getTithi().currValEndTime, myCoreData.getTithi().le_nextValEndTime, myCoreData.getSunRiseCal());

            int nakshetra1 = myCoreData.getNakshetra().currVal;
            double nakshetra1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getNakshetra().currValEndTime, myCoreData.getSunRiseCal());
            int nakshetra2 = myCoreData.getNakshetra().nextVal;
            double nakshetra2Duration = getDuration(myCoreData.getNakshetra().currValEndTime, myCoreData.getNakshetra().le_nextValEndTime, myCoreData.getSunRiseCal());

            boolean isSankranthi = myCoreData.getSolarDayVal() == 1;
            boolean isPurnima = myCoreData.getTithi().currVal == 15;
            boolean isAmabasya = myCoreData.getTithi().currVal == 30;

            System.out.println("isPurnima::" + myCoreData.getTithi().currVal);


            boolean trihasparsa = (myCoreData.getTithi().currVal != 0 && myCoreData.getTithi().nextVal != 0 && myCoreData.getTithi().nextToNextVal != 0);

            int joga1 = myCoreData.getJoga().currVal;
            double joga1Duration = getDuration(myCoreData.getSunRise(), myCoreData.getJoga().currValEndTime, myCoreData.getSunRiseCal());
            int joga2 = myCoreData.getJoga().nextVal;
            double joga2Duration = getDuration(myCoreData.getJoga().currValEndTime, myCoreData.getJoga().le_nextValEndTime, myCoreData.getSunRiseCal());


            int karana = getKarana(myCoreData.getKarana().val1);
            double karana1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getKarana().val1ET, myCoreData.getSunRiseCal());
            int karana2 = getKarana(myCoreData.getKarana().val2);
            double karana2Duration = getDuration(myCoreData.getKarana().val1ET, myCoreData.getKarana().val2ET, myCoreData.getSunRiseCal());
            int karana3 = getKarana(myCoreData.getKarana().val3);
            double karana3Duration = getDuration(myCoreData.getKarana().val2ET, myCoreData.getKarana().val3ET, myCoreData.getSunRiseCal());
            int karana4 = getKarana(myCoreData.getKarana().val4);
            double karana4Duration = getDuration(myCoreData.getKarana().val3ET, myCoreData.getKarana().val4ET, myCoreData.getSunRiseCal());


            ArrayList<AuspData> vivahList = new ArrayList<>();
            int vivahListIndex = 0;

            int size1 = auspData.size();
            for (int i = 0; i < size1; i++) {
                AuspData dataObj = (AuspData) auspData.get(i).clone();

                if (layoutType != 1)
                    dataObj.setName(le_arr_ausp_work[i]);
                // if (position == 0)
                //   header.add(l_ausp_work[i]);

                boolean strictRule = false;
                dataObj.setGuruAditya(guruAditya);
                dataObj.setSinghaGuru(singhaGuru);
                dataObj.setCloserSunJupitor(closerSunJupitor);
                dataObj.setCloserSunVenus(closerSunVenus);
                dataObj.setChaturMasa(isChaturMasa);
                dataObj.setSaranaPanchak(isSaranaPanchak);
                dataObj.setMalamasa(malamasa);
                dataObj.setJupiterBakra(jupiterBakra);
                dataObj.setChandraRabiSaniMangala(chandraRabiSaniMangala);

                boolean isNakshetra1 = dataObj.getNakshetra().contains("|" + nakshetra1 + "|");
                boolean isNakshetra2 = dataObj.getNakshetra().contains("|" + nakshetra2 + "|");
                int goodVal = 0;

                if (isNakshetra1 && isNakshetra2) {
                    goodVal = 3;
                } else if (isNakshetra1) {
                    goodVal = 1;
                } else if (isNakshetra2) {
                    goodVal = 2;
                }


                dataObj.setGoodNakshetra(goodVal);
                boolean boolMasa = dataObj.getChandraMasa().contains("|" + chandraMasa + "|");
                boolean boolPakshya = dataObj.getPakshya().contains("|" + paksha + "|");


                boolean boolKharaBara = isGoodBara(dataObj, bara, tithi1, tithi2, 0);
                boolean boolDagdaBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 1);
                boolean boolBisaBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 2);
                boolean boolHutasanBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 3);

                boolean boolBara = (!boolKharaBara && !boolDagdaBara && !boolBisaBara && !boolHutasanBara);

                boolean boolKarana = false;
                boolean boolTithi = false;
                boolean boolJoga = false;
                boolean boolNakshetra = false;


                ArrayList<String> allGoodTime = new ArrayList<>();

                ArrayList<String> goodTimeTithi = new ArrayList<>(goodTimeTithi(myCoreData, dataObj));
                ArrayList<String> goodTimeNakshetra = new ArrayList<>(goodTimeNakshetra(myCoreData, dataObj));
                ArrayList<String> goodTimeKarana = new ArrayList<>(goodTimeKarana(myCoreData, dataObj));
                ArrayList<String> goodTimeJoga = new ArrayList<>(goodTimeJoga(myCoreData, dataObj));

                //  int mday = myCoreData.getmDay();
                // if ( true || dataObj.getSlno() == 2) {

                if (goodTimeTithi.size() > 0) {
                    boolTithi = true;
                }
                if (goodTimeNakshetra.size() > 0) {
                    boolNakshetra = true;
                }
                if (goodTimeKarana.size() > 0) {
                    boolKarana = true;
                }
                if (goodTimeJoga.size() > 0) {
                    boolJoga = true;
                }


                allGoodTime.addAll(goodTimeTithi);
                allGoodTime.addAll(goodTimeNakshetra);
                allGoodTime.addAll(goodTimeJoga);
                allGoodTime.addAll(goodTimeKarana);
                Set<String> set = new HashSet<>(allGoodTime);
                allGoodTime.clear();
                allGoodTime.addAll(set);

                Collections.sort(allGoodTime, new MyComparator());

                goodTimeTithi.retainAll(goodTimeNakshetra);
                goodTimeTithi.retainAll(goodTimeJoga);
                goodTimeTithi.retainAll(goodTimeKarana);

                ArrayList<String> timeAl = new ArrayList<>();
                String min = "0", max = "0";
                boolean maxFound = false;
                for (int m = 1; m < goodTimeTithi.size(); m++) {
                    maxFound = false;
                    if (min.contains("0"))
                        min = goodTimeTithi.get(m - 1);

                    max = goodTimeTithi.get(m);

                    if ((Double.parseDouble(goodTimeTithi.get(m)) - Double.parseDouble(goodTimeTithi.get(m - 1))) > 1) {
                        maxFound = true;
                        max = goodTimeTithi.get(m - 1);

                        timeAl.add(min + ":" + max);
                        min = max = "0";


                    }

                }
                if (goodTimeTithi.size() == 1) {
                    min = max = goodTimeTithi.get(0);
                }
                if (!maxFound && goodTimeTithi.size() != 0) {
                    timeAl.add(min + ":" + max);


                }
                double minVal = 0, maxVal = 0;
                try {
                    ArrayList<String> goodTimes = new ArrayList<>();
                    for (int k = 0; k < timeAl.size(); k++) {
                        String[] minMax = timeAl.get(k).split(":");
                        for (int L = 0; L < allGoodTime.size(); L++) {
                            if (Double.parseDouble(minMax[0]) == Double.parseDouble(allGoodTime.get(L))) {
                                if (minMax[0].matches("^\\d+\\.\\d{2}$")) {
                                    minVal = Double.parseDouble(allGoodTime.get(L));
                                } else {
                                    minVal = Double.parseDouble(allGoodTime.get(L - 1));
                                }

                            }
                            if (Double.parseDouble(minMax[1]) == Double.parseDouble(allGoodTime.get(L))) {

                                if (minMax[1].matches("^\\d+\\.\\d{2}$")) {
                                    maxVal = Double.parseDouble(allGoodTime.get(L));
                                } else {
                                    maxVal = Double.parseDouble(allGoodTime.get(L + 1));
                                }
                                // break;
                            }

                        }
                        goodTimes.add(minVal + ":" + maxVal);


                    }
                    dataObj.setGoodTimes(goodTimes);
                } catch (Exception e) {
                    Log.e("goodTimeTithi", "::goodTime:MINMAX:5::" + e.getMessage());
                    e.printStackTrace();
                }
                //   }


                if (goodTimeTithi.size() == 0) {
                    dataObj.isTithi = boolTithi = false;
                    dataObj.isKarana = boolKarana = false;
                    dataObj.isJoga = boolJoga = false;
                    dataObj.isNakshatra = boolNakshetra = false;
                } else {
                    dataObj.isTithi = boolTithi;
                    dataObj.isKarana = boolKarana;
                    dataObj.isJoga = boolJoga;
                    dataObj.isNakshatra = boolNakshetra;
                }
                dataObj.setName(dataObj.getName());
                dataObj.isMasa = boolMasa;
                dataObj.isKharaBara = boolKharaBara;
                dataObj.isDagdaBara = boolDagdaBara;
                dataObj.isBisaBara = boolBisaBara;
                dataObj.isHutasanBara = boolHutasanBara;

                dataObj.isSankranti = isSankranthi;
                dataObj.isTrihaspada = trihasparsa;
                dataObj.isMasant = masant;


                boolean commonRule = (boolMasa && boolPakshya && (goodTimeTithi.size() > 0)
                        && boolBara
                        && boolTithi
                        && boolNakshetra
                        && boolJoga
                        && boolKarana
                        && !isSankranthi
                        && !trihasparsa
                        && !isPurnima
                        && !isAmabasya
                        && !masant
                        && !chandraRabiSaniMangala
                );


                String date1 = myCoreData.getmDay() + "-" + myCoreData.getmMonth() + "-" + myCoreData.getmYear();
                if (dataObj.getMangalikaType() != 3) {
                    strictRule = (!kharaMasa && !guruAditya && !singhaGuru && !closerSunJupitor && !closerSunVenus && !isChaturMasa /*&& !isSaranaPanchak*/ && !malamasa && !jupiterBakra && !chandraRabiSaniMangala);
                    if (commonRule && strictRule) {
                        dataObj.setTodayAusp(true);
                        dataObj.setDayInfo(myCoreData);
                    } else {
                        dataObj.setGoodTimes(new ArrayList<>());
                        dataObj.setTodayAusp(false);
                        dataObj.setDayInfo(myCoreData);
                    }
                } else {
                    if (commonRule) {
                        dataObj.setTodayAusp(true);
                        dataObj.setDayInfo(myCoreData);
                    } else {
                        dataObj.setGoodTimes(new ArrayList<>());
                        dataObj.setTodayAusp(false);
                        dataObj.setDayInfo(myCoreData);
                    }
                }

                Log.e("goodVal", dataObj.isTodayAusp() + ":" + commonRule + ":" + strictRule + "::" + date1 + "::" + dataObj.getName() + "::" + "goodNaksVal::" + goodVal + ":" + boolMasa + ":" + boolPakshya + ":" + boolBara + ":" + boolTithi + ":" + boolNakshetra + ":" + boolJoga + ":" + boolKarana);


                vivahList.add(dataObj);


            }

            int size = vivahList.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                if (vivahList.get(i).isTodayAusp())
                    sb.append(vivahList.get(i).getName().trim()).append(", ");

                // Log.e("vivahList", i + ":vivahList::::" + vivahList.get(i).getName() + ":" + vivahList.get(i).isTodayAusp());
            }

            return sb.toString().trim().replaceAll(",$", ""); //sb.deleteCharAt(sb.length() - 1).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    class MyComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            return new BigDecimal(o1).compareTo(new BigDecimal(o2));
        }

    }

    private ArrayList<String> goodTimeJoga(CoreDataHelper myCoreData, AuspData dataObj) {
        CoreDataHelper.Panchanga mainObj = myCoreData.getJoga();
        String settingStr = dataObj.getJoga();

        ArrayList<String> al = new ArrayList<>();
        int currVal = mainObj.currVal;
        int nextVal = mainObj.nextVal;
        if (settingStr.contains("|" + currVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), myCoreData.getSunRiseCal(), mainObj.currValEndTime));

        }
        if (settingStr.contains("|" + nextVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.currValEndTime, mainObj.le_nextValEndTime));

        }


        return al;
    }

    private ArrayList<String> goodTimeNakshetra(CoreDataHelper myCoreData, AuspData dataObj) {
        CoreDataHelper.Panchanga mainObj = myCoreData.getNakshetra();
        String settingStr = dataObj.getNakshetra();

        ArrayList<String> al = new ArrayList<>();
        int currVal = mainObj.currVal;
        int nextVal = mainObj.nextVal;
        int nextNextVal = mainObj.nextToNextVal;
        if (settingStr.contains("|" + currVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), myCoreData.getSunRiseCal(), mainObj.currValEndTime));

        }
        if (settingStr.contains("|" + nextVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.currValEndTime, mainObj.le_nextValEndTime));
        }
        if (settingStr.contains("|" + nextNextVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.le_nextValEndTime, mainObj.nextToNextValEndTime));

        }


        return al;
    }

    private ArrayList<String> goodTimeTithi(CoreDataHelper myCoreData, AuspData dataObj) {
        CoreDataHelper.Panchanga mainObj = myCoreData.getTithi();
        String settingStr = dataObj.getTithi();

        ArrayList<String> al = new ArrayList<>();
        int currVal = mainObj.currVal;
        int nextVal = mainObj.nextVal;
        int nextNextVal = mainObj.nextToNextVal;
        if (dataObj.getMangalikaType() != 1) {
            currVal = currVal < 15 ? currVal : (currVal - 15);
            nextVal = nextVal < 15 ? nextVal : (nextVal - 15);
            nextNextVal = nextNextVal < 15 ? nextNextVal : (nextNextVal - 15);
        }


        if (settingStr.contains("|" + currVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), myCoreData.getSunRiseCal(), mainObj.currValEndTime));

        }
        if (settingStr.contains("|" + nextVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.currValEndTime, mainObj.le_nextValEndTime));

        }
        if (settingStr.contains("|" + nextNextVal + "|")) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.le_nextValEndTime, mainObj.nextToNextValEndTime));

        }


        return al;
    }

    private ArrayList<String> goodTimeKarana(CoreDataHelper myCoreData, AuspData dataObj) {

        CoreDataHelper.Karana mainObj = myCoreData.getKarana();
        String settingStr = dataObj.getKarana();

        Log.e("ArrayList", "GOODTIME:KARANA:");
        ArrayList<String> al = new ArrayList<>();
        int val1 = getKarana(mainObj.val1);
        int val2 = getKarana(mainObj.val2);
        int val3 = getKarana(mainObj.val3);
        int val4 = getKarana(mainObj.val4);
        int val5 = getKarana(mainObj.val5);
        int val6 = getKarana(mainObj.val6);
        Log.e("ArrayList", "GOODTIME:KARANA:");
        if (settingStr.contains("|" + val1 + "|") && mainObj.val1ET != null) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), myCoreData.getSunRiseCal(), mainObj.val1ET));

        }
        if (settingStr.contains("|" + val2 + "|") && mainObj.val2ET != null) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.val1ET, mainObj.val2ET));

        }
        if (settingStr.contains("|" + val3 + "|") && mainObj.val3ET != null) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.val2ET, mainObj.val3ET));

        }
        if (settingStr.contains("|" + val4 + "|") && mainObj.val4ET != null) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.val3ET, mainObj.val4ET));

        }
        if (settingStr.contains("|" + val5 + "|") && mainObj.val5ET != null) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.val4ET, mainObj.val5ET));

        }


        return al;
    }

    private ArrayList<String> getGoodTime(Calendar sunriseTime, Calendar startTime, Calendar endTime) {


        ArrayList<String> al = new ArrayList<String>();
        try {
            Calendar nextDaySunrise = Calendar.getInstance();
            nextDaySunrise.setTimeInMillis(sunriseTime.getTimeInMillis() + (24 * 60 * 60 * 1000));
            if (endTime != null && endTime.getTimeInMillis() > nextDaySunrise.getTimeInMillis()) {
                endTime = nextDaySunrise;
            } else if (endTime == null) {
                endTime = nextDaySunrise;
            }

            int hr1 = startTime.get(Calendar.HOUR_OF_DAY);
            int min1 = startTime.get(Calendar.MINUTE);
            Calendar midNight = Calendar.getInstance();
            midNight.set(sunriseTime.get(Calendar.YEAR), sunriseTime.get(Calendar.MONTH), sunriseTime.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

            // int day2 = endTime.get(Calendar.DAY_OF_MONTH);
            int hr2 = endTime.get(Calendar.HOUR_OF_DAY);
            int min2 = endTime.get(Calendar.MINUTE);
            if (startTime.getTimeInMillis() > midNight.getTimeInMillis()) {
                hr1 = 24 + hr1;
            }
            if (endTime.getTimeInMillis() > midNight.getTimeInMillis()) {
                hr2 = 24 + hr2;
            }

            if (min1 < 10)
                al.add(hr1 + ".0" + min1);
            else
                al.add(hr1 + "." + min1);
            for (int i = hr1 + 1; i <= hr2; i++) {
                al.add("" + i);
            }
            if (min2 < 10)
                al.add(hr2 + ".0" + min2);
            else
                al.add(hr2 + "." + min2);
        } catch (Exception e) {
            Log.e("XXXX", "XXXXX:1:" + e.getMessage());
        }
        return al;
    }


    private boolean isGoodTithi(AuspData dataObj, int bara, int tithi1, int tithi2, double tithi1Duration, double tithi2Duration) {
//ସାଧାରଣତଃ ୨ୟା,୩ୟା,୫ମୀ,୭ମୀ,୧୧ଶୀ ଓ ୧୩ଶୀ ଶ୍ରେଷ୍ଠ ତିଥି।   ପ୍ରତିପଦ, ୬ଷ୍ଠୀ, ୮ମୀ ଓ ୧୦ମୀ (ଅତ୍ୟାବଶ୍ୟକ ବେଳେ ବିଚାରଯୋଗ୍ଯ) ୪ର୍ଥୀ, ୯ମୀ ଓ ୧୪ଶୀ ରିକ୍ତା ତିଥି ହେତୁ ଆଦୌ ଗ୍ରହଣୀୟ ନୁହେଁ।  ଅମାବାସ୍ୟା, ସଂକ୍ରାନ୍ତି ଓ ତ୍ରିହ୍ୟସ୍ପର୍ଷ ଦିବସ ଗୁଡ଼ିକ ସଂକମଣ ତିଥି ହେତୁ ବର୍ଜନୀୟ।
// ପୁନଶ୍ଚ ୭ମୀ ଓ ୧୨ଶୀ ବ୍ରତୋପନୟନ ପାଇଁ  ଅନୁପଯୁକ୍ତ।  ସେହିଭଳି ବିଦ୍ଯାରମ୍ଭ ପାଇଁ ପ୍ରତିପଦ, ୨ୟା ଓ ୧୪ଶୀ ଅନୁପଯୁକ୍ତ ଅଟେ।
//ପ୍ରତିପଦ, ୬ଷ୍ଠୀ, ୧୧ଶୀ ତିଥି ଶୁକ୍ରବାରରେ:
// ୨ୟା, ୭ମୀ, ୧୨ଶୀ ତିଥି ବୁଧବାରରେ:
// ୩ୟା, ୮ମୀ, ୧୩ଶୀ ତିଥି ମଙ୍ଗଳବାରରେ:
// ୪ର୍ଥୀ, ୯ମୀ, ୧୪ଶୀ ତିଥି ଶନିବାରରେ:
// ୫ମୀ, ୧୦ମୀ, ପୂର୍ଣ୍ଣିମା ବା ଅମାବାସ୍ୟା ତିଥି  ଗୁରୁବାରରେ ପଡ଼ୁଥିବ ତେବେ ଏହି ତିଥିଗୁଡ଼ିକୁ ମାଙ୍ଗଳିକ ଶୁଭକାର୍ଯ୍ୟ ବ୍ୟତୀତ ଅନ୍ୟ ଶୁଭକାର୍ଯ୍ୟ ପାଇଁ ବିଚାରକୁ ନିଆଯିବ।
//&& tithi1Duration > 4
        boolean isGoodBad = (((tithi1 == 2 || tithi1 == 3 || tithi1 == 5 || tithi1 == 7 || tithi1 == 11 || tithi1 == 13 || tithi1 == 1 || tithi1 == 6 || tithi1 == 8 || tithi1 == 10)
                && tithi1Duration > minTimeDuration) ||
                ((tithi2 == 2 || tithi2 == 3 || tithi2 == 5 || tithi2 == 7 || tithi2 == 11 || tithi2 == 13 || tithi2 == 1 || tithi2 == 6 || tithi2 == 8 || tithi2 == 10)
                        && tithi2Duration > minTimeDuration));

        if (dataObj.getMangalikaType() != 3) {
            boolean isNotMangalika1 = (bara == 6 && (tithi1 == 1 || tithi1 == 6 || tithi1 == 11));
            boolean isNotMangalika2 = (bara == 4 && (tithi1 == 2 || tithi1 == 7 || tithi1 == 12));
            boolean isNotMangalika3 = (bara == 3 && (tithi1 == 3 || tithi1 == 8 || tithi1 == 13));
            boolean isNotMangalika4 = (bara == 7 && (tithi1 == 4 || tithi1 == 9 || tithi1 == 14));
            boolean isNotMangalika5 = (bara == 5 && (tithi1 == 5 || tithi1 == 10 || tithi1 == 15 || tithi1 == 30));
            return isGoodBad && !(isNotMangalika1 || isNotMangalika2 || isNotMangalika3 || isNotMangalika4 || isNotMangalika5);
        }

        return isGoodBad;

    }

    private boolean isGoodBara(AuspData dataObj, int bara, int tithi1, int tithi2, int type) {
        //  ମଙ୍ଗଳ,ଶନି ଓ ରବି ଖର ବା ନିଷ୍ଠୁର ବାର।
        if (type == 0) {
            boolean kharaBara1 = (bara == 3 || bara == 7);
            return kharaBara1;
        }
        //୧। ଯଦି ରବିବାରରେ ୧୨ଶୀ, ସୋମବାରରେ ୧୧ଶୀ, ମଙ୍ଗଳବାରରେ ୫ମୀ, ବୁଧବାରରେ ୩ୟା, ଗୁରୁବାରରେ ୬ଷ୍ଠୀ, ଶୁକ୍ରବାରରେ ୮ମୀ ଓ ଶନିବାରରେ ୯ମୀ ପଡ଼ୁଥିବ ତେବେ ଏଗୁଡ଼ିକ ଦଗ୍ଧବାର ହେବ ଓ କୌଣସି ଶୁଭକାର୍ଯ୍ୟ ଯୋଗ୍ୟ ନୁହେଁ।
        else if (type == 1) {
            boolean dagdhaBara1 = ((bara == 1 && tithi1 == 12) || (bara == 2 && tithi1 == 11) || (bara == 3 && tithi1 == 5) || (bara == 4 && tithi1 == 3) || (bara == 5 && tithi1 == 6) || (bara == 6 && tithi1 == 8) || (bara == 7 && tithi1 == 9));
            return dagdhaBara1;
        }
        //୨। ଯଦି ରବିବାରରେ ୪ର୍ଥୀ, ସୋମବାରରେ ୬ଷ୍ଠୀ, ମଙ୍ଗଳବାରରେ ୭ମୀ, ବୁଧବାରରେ ୨ୟା, ଗୁରୁବାରରେ ୮ମୀ, ଶୁକ୍ରବାରରେ ୯ମୀ ଓ ଶନିବାରରେ ୭ମୀ ପଡ଼ୁଥିବ ତେବେ ଏଗୁଡ଼ିକ ବିଷବାର ହେବ ଓ କୌଣସି ଶୁଭକାର୍ଯ୍ୟ ଯୋଗ୍ୟ ନୁହେଁ।
        else if (type == 2) {
            boolean bisaBara1 = ((bara == 1 && tithi1 == 4) || (bara == 2 && tithi1 == 6) || (bara == 3 && tithi1 == 7) || (bara == 4 && tithi1 == 2) || (bara == 5 && tithi1 == 8) || (bara == 6 && tithi1 == 9) || (bara == 7 && tithi1 == 7));
            return bisaBara1;
        }
        //୩। ଯଦି ରବିବାରରେ ୧୨ଶୀ, ସୋମବାରରେ ୬ଷ୍ଠୀ, ମଙ୍ଗଳବାରରେ ୭ମୀ, ବୁଧବାରରେ ୮ମୀ, ଗୁରୁବାରରେ ୯ମୀ, ଶୁକ୍ରବାରରେ ୧୦ମୀ ଓ ଶନିବାରରେ ୧୧ଶୀ ପଡ଼ୁଥିବ ତେବେ ଏଗୁଡ଼ିକ ହୁତାଶନବାର ହେବ ଓ କୌଣସି ଶୁଭକାର୍ଯ୍ୟ ଯୋଗ୍ୟ ନୁହେଁ।
        else if (type == 3) {
            boolean hutashanaBara1 = ((bara == 1 && tithi1 == 12) || (bara == 2 && tithi1 == 6) || (bara == 3 && tithi1 == 7) || (bara == 4 && tithi1 == 8) || (bara == 5 && tithi1 == 9) || (bara == 6 && tithi1 == 10) || (bara == 7 && tithi1 == 11));
            return hutashanaBara1;
        }
        return false;
        //  boolean dagdhaBara2 = ((bara == 1 && tithi2 == 12) || (bara == 2 && tithi2 == 11) || (bara == 3 && tithi2 == 5) || (bara == 4 && tithi2 == 3) || (bara == 5 && tithi2 == 6) || (bara == 6 && tithi2 == 8) || (bara == 7 && tithi2 == 9));
        //  boolean bisaBara2 = ((bara == 1 && tithi2 == 4) || (bara == 2 && tithi2 == 6) || (bara == 3 && tithi2 == 7) || (bara == 4 && tithi2 == 2) || (bara == 5 && tithi2 == 8) || (bara == 6 && tithi2 == 9) || (bara == 7 && tithi2 == 7));
        //  boolean hutashanaBara2 = ((bara == 1 && tithi2 == 12) || (bara == 2 && tithi2 == 6) || (bara == 3 && tithi2 == 7) || (bara == 4 && tithi2 == 8) || (bara == 5 && tithi2 == 9) || (bara == 6 && tithi2 == 10) || (bara == 7 && tithi2 == 11));


        // return !(( dagdhaBara1 || bisaBara1 || hutashanaBara1) /*|| (dagdhaBara2 || bisaBara2 || hutashanaBara2)*/);
    }

    private boolean isGoodJoga(AuspData dataObj, int joga1, int joga2, double joga1Duration, double joga2Duration) {
        // ଅତିଗଣ୍ଡ, ଶୁଳ, ଗଣ୍ଡ, ବ୍ୟାଘାତ, ବ୍ୟତୀପାତ,  ପରିଘ, ଇନ୍ଦ୍ର, ବୈଧୃତି, ବିଷ୍କୁମ୍ଭ, ଓ ବଜ୍ର ଅଶୁଭ ଯୋଗ are bad karanas
        return ((!(joga1 == 1 || joga1 == 6 || joga1 == 9 || joga1 == 10 || joga1 == 13 || joga1 == 15 || joga1 == 17 || joga1 == 19 || /*joga1 == 26 ||*/ joga1 == 27) && joga1Duration > minTimeDuration)
                || (!(joga2 == 1 || joga2 == 6 || joga2 == 9 || joga2 == 10 || joga2 == 13 || joga2 == 15 || joga2 == 17 || joga2 == 19 || /*joga2 == 26 ||*/ joga2 == 27)) && joga2Duration > minTimeDuration);
    }

    private boolean isGoodKarana(AuspData dataObj, int karana1, int karana2, int karana3, int karana4, double karana1Duration, double karana2Duration, double karana3Duration, double karana4Duration) {
        // ବିଷ୍ଟି=8, ଶକୁନି=58,ଚତୁଷ୍ପଦ=59,ନାଗ=60 ଓ କିସ୍ତୁଘ୍ନ=1 are bad karanas
        return (karana1 > 1 && karana1 < 8 && karana1Duration > minTimeDuration) || (karana2 > 1 && karana2 < 8 && karana2Duration > minTimeDuration) || (karana3 > 1 && karana3 < 8 && karana3Duration > minTimeDuration) || (karana4 > 1 && karana4 < 8 && karana4Duration > minTimeDuration);
    }


    private int getKarana(int karana) {
        if (karana > 8 && karana < 58) {
            karana = (karana == 9 || karana == 16 || karana == 23 || karana == 30 || karana == 37 || karana == 44 || karana == 51) ? 2 : karana;
            karana = (karana == 10 || karana == 17 || karana == 24 || karana == 31 || karana == 38 || karana == 45 || karana == 52) ? 3 : karana;
            karana = (karana == 11 || karana == 18 || karana == 25 || karana == 32 || karana == 39 || karana == 46 || karana == 53) ? 4 : karana;
            karana = (karana == 12 || karana == 19 || karana == 26 || karana == 33 || karana == 40 || karana == 47 || karana == 54) ? 5 : karana;
            karana = (karana == 13 || karana == 20 || karana == 27 || karana == 34 || karana == 41 || karana == 48 || karana == 55) ? 6 : karana;
            karana = (karana == 14 || karana == 21 || karana == 28 || karana == 35 || karana == 42 || karana == 49 || karana == 56) ? 7 : karana;
            karana = (karana == 15 || karana == 22 || karana == 29 || karana == 36 || karana == 43 || karana == 50 || karana == 57) ? 8 : karana;
        }
        return karana;
    }

    public PlanetData calculatePlanetInfo(String planet, String dmplanet, int type, int selYear, int selMonth, int selDate, int selHour, int selMin) {

        String dir = "F";
        String dm = dmplanet;
        if ((type >= 3 && type <= 10)) {
            dir = dmplanet.substring(0, 1);
            dm = dmplanet.substring(1);
        }

        double dailyMotion = Double.parseDouble(dm);
        PlanetData pd = new PlanetData();

        if (le_arr_planet == null) {
            Log.e("planetList", "planetList:nulll");
        }
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
        Calendar selCal = Calendar.getInstance();
        selCal.set(Calendar.YEAR, selYear);
        selCal.set(Calendar.MONTH, selMonth);
        selCal.set(Calendar.DAY_OF_MONTH, selDate);
        selCal.set(Calendar.HOUR_OF_DAY, selHour);
        selCal.set(Calendar.MINUTE, selMin);


        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, selYear);
        cal1.set(Calendar.MONTH, selMonth);
        cal1.set(Calendar.DAY_OF_MONTH, selDate);
        cal1.set(Calendar.HOUR_OF_DAY, 5);
        cal1.set(Calendar.MINUTE, 30);

        double diff = Math.abs((cal1.getTimeInMillis() / 1000.0) - (selCal.getTimeInMillis() / 1000.0)) / (60 * 60.0);
        double remDeg = ((dailyMotion / 24.0) * diff);
        int deg = Integer.parseInt(planet.split("_")[0]);
        int min = Integer.parseInt(planet.split("_")[1]);
        double minToSec = ((min * 60.0) / 3600.0);
        double planetDeg = deg + minToSec;
        double currPlanetDeg = planetDeg + remDeg;


        if (type == 12) {
            currPlanetDeg = currPlanetDeg + 180.0;
        }
        pd.deg = currPlanetDeg;
        getLatLng(currPlanetDeg, pd);

        return pd;//currPlanetDeg;


    }

    public void getLatLng(double deg, PlanetData pd) {
        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        pd.houseNo = "" + (index + 1);
        double fractionInSec = (deg - val) * 3600;

        long min = (int) fractionInSec / 60;
        long sec = (int) fractionInSec % 60;
        String latLng = remDeg + "° " + le_arr_rasi_kundali[index] + " " + min + "′" + " " + sec + "′′";
        pd.latLng = latLng;
        // return latLng;
    }

    public static class PlanetData {
        public String planet, latLng, houseNo;
        public int planetType, direction;
        public double deg;

    }

    public boolean checkDate(Date d) {
        int size = alrangedate.size();
        for (int i = 0; i < size; i++) {
            Date a = alrangedate.get(i).start;
            Date b = alrangedate.get(i).end;
            if (a.compareTo(d) * d.compareTo(b) >= 0) {
                return true;
            }
        }
        return false;
    }

    public void callRetrograde(String file, int year) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(file + ".txt")));
            String mLine;
            alrangedate = new ArrayList<>();

            while ((mLine = reader.readLine()) != null) {

                String[] rangeDateArr = mLine.split(" ");
                String minRange = rangeDateArr[0];
                String maxRange = rangeDateArr[1];
                // atichar logic should added

                if (rangeDateArr[0].contains("2019")) {
                    minRange = "2019-4-14";
                    maxRange = "2019-4-24";
                } else if (rangeDateArr[0].contains("2020")) {
                    minRange = "2020-3-29";
                    maxRange = "2020-4-11";
                }

                // 14/4/2019 23/4/2019  24/4/2019 9/7
                // 29/3/2020 11/4/2020

                if (minRange.contains("" + year)) {
                    Calendar cal = Calendar.getInstance();
                    String[] startDate = minRange.split("-");
                    int startYear = Integer.parseInt(startDate[0]);
                    int startMonth = Integer.parseInt(startDate[1]) - 1;
                    int startday = Integer.parseInt(startDate[2]);
                    cal.set(startYear, startMonth, startday, 0, 0, 0);

                    Calendar cal1 = Calendar.getInstance();
                    String[] startDate1 = maxRange.split("-");
                    int startYear1 = Integer.parseInt(startDate1[0]);
                    int startMonth1 = Integer.parseInt(startDate1[1]) - 1;
                    int startday1 = Integer.parseInt(startDate1[2]);
                    cal1.set(startYear1, startMonth1, startday1, 23, 59, 59);
                    rangedate rangedate1Obj = new rangedate();
                    rangedate1Obj.start = cal.getTime();
                    rangedate1Obj.end = cal1.getTime();
                    alrangedate.add(rangedate1Obj);
                    //  retroAll.add(mLine+" "+type);

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class rangedate {
        public Date start, end;
    }


}
