package com.iexamcenter.calendarweather.kundali;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.dao.EphemerisDao;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.panchang.PanchangUtility;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class KundaliMatchCalcFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    public ArrayList<guna36> guna36List;
    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView titleInfo2;
    private MainActivity mContext;
    double totalScored = 0.0;
    //  static int selYear1, selMonth1, selDate1, selHour1, selMin1;
    //  MaterialButton datePicker, timePicker;
    ArrayList<PlanetData> planetDataList;
    // String[] le_arr_rasi_kundali,  le_arr_planet,   le_arr_day_planet ;
    //RecyclerView planetinfo;
    // KundaliInfoListAdapter mPlanetInfoAdapter;
    LinearLayout pointsCntr;
    String le_ghara;
    ArrayList<Integer> rashiInHouse;
    String lagnaLngNow, lagnaLngAtSunrise;
    ArrayList<houseinfo> houseinfos;
    LinearLayout diagramCntr, houseInfoCntr, planeInfoCntr;
    int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali, jogaKundali, karanaKundali, moonSignKundali, sunSignKundali;
    // sun,moon,mars,mercury,jupitor,venus,saturn
    int[] houseOwnerPlanet = new int[]{4, 3, 2, 1, 0, 2, 3, 4, 5, 6, 6, 5};
    int[] rashiOwnerPlanet = new int[]{2, 5, 3, 1, 0, 3, 5, 2, 4, 6, 6, 4};
    //String[] le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_karana, le_arr_joga, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    //String headerStr1, headerStr2;
    // String le_samvat, le_dina, le_ritu, le_sal, le_shakaddha, le_paksha;
    //  String le_lagna, le_mangala_dosha, le_tithi, le_nakshetra, le_joga, le_karana, le_lunar_rashi, le_solar_rasi;
    boolean isMangalaDosha = false;
    // String latitude1, longitude1;
    HashMap<Integer, KundaliMatchCalcFrag.houseinfo> houseinfosMap;
    Calendar selCalPrevday;
    boolean considerPrevday = false;
    String nakshetraTithi;
    boolean isMoonSignFirstHalf = false;
    int varna1, vasya1, tara1, yoni1, maitri1, gana1, bhakuta1, nadi1;
    int varna2, vasya2, tara2, yoni2, maitri2, gana2, bhakuta2, nadi2;

    public static KundaliMatchCalcFrag newInstance() {

        return new KundaliMatchCalcFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    int mType;
    String[] le_arr_rasi_kundali, le_arr_planet;
    String[] le_arr_ausp_work_yes_no, le_arr_tithi, le_arr_nakshatra, le_arr_karana, le_arr_joga, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String le_samvat, le_dina, le_ritu, le_sal, le_shakaddha, le_paksha;
    String le_lagna, le_mangala_dosha, le_tithi, le_nakshetra, le_joga, le_karana, le_lunar_rashi, le_solar_rasi;

    String[] le_arr_kundali_points, le_arr_day_planet, le_arr_kundali_gana, le_arr_kundali_yoni, le_arr_kundali_varna, le_arr_kundali_guna, le_arr_kundali_nadi, le_arr_kundali_vasya;
    String le_kundali_yoni_title, le_kundali_tara_title, le_kundali_vasya_title, le_kundali_guna_title, le_kundali_gana_title, le_kundali_nadi_title, le_kundali_varna_title;
    Resources res;

    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_arr_kundali_points = res.getStringArray(R.array.l_arr_kundali_points);
            le_arr_kundali_gana = res.getStringArray(R.array.l_arr_kundali_gana);
            le_arr_kundali_guna = res.getStringArray(R.array.l_arr_kundali_guna);
            le_arr_kundali_varna = res.getStringArray(R.array.l_arr_kundali_varna);
            le_arr_kundali_yoni = res.getStringArray(R.array.l_arr_kundali_yoni);
            le_arr_kundali_nadi = res.getStringArray(R.array.l_arr_kundali_nadi);
            le_arr_kundali_vasya = res.getStringArray(R.array.l_arr_kundali_vasya);
            le_kundali_yoni_title = res.getString(R.string.l_kundali_yoni_title);
            le_kundali_tara_title = res.getString(R.string.l_kundali_tara_title);
            le_kundali_vasya_title = res.getString(R.string.l_kundali_vasya_title);
            le_kundali_guna_title = res.getString(R.string.l_kundali_guna_title);
            le_kundali_gana_title = res.getString(R.string.l_kundali_gana_title);
            le_kundali_nadi_title = res.getString(R.string.l_kundali_nadi_title);
            le_kundali_varna_title = res.getString(R.string.l_kundali_varna_title);

            le_arr_rasi_kundali = res.getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_planet = res.getStringArray(R.array.l_arr_planet);
            le_arr_karana = res.getStringArray(R.array.l_arr_karana);
            le_paksha = res.getString(R.string.l_paksha);
            le_shakaddha = res.getString(R.string.l_shakaddha);
            le_sal = res.getString(R.string.l_sal);
            le_ritu = res.getString(R.string.l_ritu);
            le_dina = res.getString(R.string.l_dina);
            le_ghara = res.getString(R.string.l_ghara);
            le_samvat = res.getString(R.string.l_samvat);
            le_tithi = res.getString(R.string.l_tithi);
            le_nakshetra = res.getString(R.string.l_nakshetra);
            le_joga = res.getString(R.string.l_joga);
            le_karana = res.getString(R.string.l_karana);
            le_lunar_rashi = res.getString(R.string.l_lunar_rashi);
            le_solar_rasi = res.getString(R.string.l_solar_rasi);
            le_lagna = res.getString(R.string.l_lagna);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.l_mangala_dosha);
            le_arr_joga = res.getStringArray(R.array.l_arr_joga);
            le_arr_month = res.getStringArray(R.array.l_arr_month);
            le_arr_bara = res.getStringArray(R.array.l_arr_bara);
            le_arr_paksha = res.getStringArray(R.array.l_arr_paksha);
            le_arr_masa = res.getStringArray(R.array.l_arr_masa);
            le_arr_tithi = res.getStringArray(R.array.l_arr_tithi);
            le_arr_nakshatra = res.getStringArray(R.array.l_arr_nakshatra);
            le_arr_day_planet = res.getStringArray(R.array.l_arr_day_planet);
        } else {
            le_arr_kundali_points = res.getStringArray(R.array.e_arr_kundali_points);
            le_arr_kundali_gana = res.getStringArray(R.array.e_arr_kundali_gana);
            le_arr_kundali_guna = res.getStringArray(R.array.e_arr_kundali_guna);
            le_arr_kundali_varna = res.getStringArray(R.array.e_arr_kundali_varna);
            le_arr_kundali_yoni = res.getStringArray(R.array.e_arr_kundali_yoni);
            le_arr_kundali_nadi = res.getStringArray(R.array.e_arr_kundali_nadi);
            le_arr_kundali_vasya = res.getStringArray(R.array.e_arr_kundali_vasya);
            le_kundali_yoni_title = res.getString(R.string.e_kundali_yoni_title);
            le_kundali_tara_title = res.getString(R.string.e_kundali_tara_title);
            le_kundali_vasya_title = res.getString(R.string.e_kundali_vasya_title);
            le_kundali_guna_title = res.getString(R.string.e_kundali_guna_title);
            le_kundali_gana_title = res.getString(R.string.e_kundali_gana_title);
            le_kundali_nadi_title = res.getString(R.string.e_kundali_nadi_title);
            le_kundali_varna_title = res.getString(R.string.e_kundali_varna_title);

            le_arr_rasi_kundali = res.getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_planet = res.getStringArray(R.array.e_arr_planet);
            le_arr_karana = res.getStringArray(R.array.e_arr_karana);
            le_paksha = res.getString(R.string.e_paksha);
            le_shakaddha = res.getString(R.string.e_shakaddha);
            le_sal = res.getString(R.string.e_sal);
            le_ritu = res.getString(R.string.e_ritu);
            le_dina = res.getString(R.string.e_dina);
            le_ghara = res.getString(R.string.e_ghara);
            le_samvat = res.getString(R.string.e_samvat);
            le_tithi = res.getString(R.string.e_tithi);
            le_nakshetra = res.getString(R.string.e_nakshetra);
            le_joga = res.getString(R.string.e_joga);
            le_karana = res.getString(R.string.e_karana);
            le_lunar_rashi = res.getString(R.string.e_lunar_rashi);
            le_solar_rasi = res.getString(R.string.e_solar_rasi);
            le_lagna = res.getString(R.string.e_lagna);
            le_arr_ausp_work_yes_no = res.getStringArray(R.array.e_arr_ausp_work_yes_no);
            le_mangala_dosha = res.getString(R.string.e_mangala_dosha);
            le_arr_joga = res.getStringArray(R.array.e_arr_joga);
            le_arr_month = res.getStringArray(R.array.e_arr_month);
            le_arr_bara = res.getStringArray(R.array.e_arr_bara);
            le_arr_paksha = res.getStringArray(R.array.e_arr_paksha);
            le_arr_masa = res.getStringArray(R.array.e_arr_masa);
            le_arr_tithi = res.getStringArray(R.array.e_arr_tithi);
            le_arr_nakshatra = res.getStringArray(R.array.e_arr_nakshatra);
            le_arr_day_planet = res.getStringArray(R.array.e_arr_day_planet);
        }

    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            setHasOptionsMenu(false);
            mContext.getSupportActionBar().setTitle("Kundali Milana");
            mContext.getSupportActionBar().setSubtitle("");
            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();

            int selYear1, selMonth1, selDate1, selHour1, selMin1;
            int selYear2, selMonth2, selDate2, selHour2, selMin2;
            String latitude1, longitude1, area1, area2, latitude2, longitude2;
            int year1, year2, month1, month2, dayOfmonth1, dayOfmonth2, hour1, hour2, min1, min2;
            guna36List = new ArrayList<>();
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();

            Bundle args = getArguments();
            area1 = args.getString("area1");
            area2 = args.getString("area2");
            latitude1 = args.getString("latitude1");
            longitude1 = args.getString("longitude1");
            latitude2 = args.getString("latitude2");
            longitude2 = args.getString("longitude2");
            year1 = args.getInt("year1");
            year2 = args.getInt("year2");
            month1 = args.getInt("month1");
            month2 = args.getInt("month2");
            dayOfmonth1 = args.getInt("dayOfmonth1");
            dayOfmonth2 = args.getInt("dayOfmonth2");
            hour1 = args.getInt("hour1");
            hour2 = args.getInt("hour2");
            min1 = args.getInt("min1");
            min2 = args.getInt("min2");

            Calendar cal1 = Calendar.getInstance();
            cal1.set(year1, month1, dayOfmonth1, hour1, min1);

            selDate1 = cal1.get(Calendar.DAY_OF_MONTH);
            selMonth1 = cal1.get(Calendar.MONTH);
            selYear1 = cal1.get(Calendar.YEAR);
            selHour1 = cal1.get(Calendar.HOUR_OF_DAY);
            selMin1 = cal1.get(Calendar.MINUTE);

            Calendar cal2 = Calendar.getInstance();
            cal2.set(year2, month2, dayOfmonth2, hour2, min2);

            selDate2 = cal2.get(Calendar.DAY_OF_MONTH);
            selMonth2 = cal2.get(Calendar.MONTH);
            selYear2 = cal2.get(Calendar.YEAR);
            selHour2 = cal2.get(Calendar.HOUR_OF_DAY);
            selMin2 = cal2.get(Calendar.MINUTE);

            /*


             */
            //  diagramCntr = rootView.findViewById(R.id.diagramCntr);
          /*  le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);

            le_arr_planet = mContext.getResources().getStringArray(R.array.e_arr_planet);
            le_arr_planet = mContext.getResources().getStringArray(R.array.l_arr_planet);

            le_arr_day_planet = mContext.getResources().getStringArray(R.array.e_arr_day_planet);
            le_arr_day_planet = mContext.getResources().getStringArray(R.array.l_arr_day_planet);

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

*/
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            planetDataList = new ArrayList<>();
            pointsCntr = rootView.findViewById(R.id.pointsCntr);
            //  titleInfo1 = rootView.findViewById(R.id.titleInfo);
            titleInfo2 = rootView.findViewById(R.id.lagna);
            //  observerPlanetInfo(selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1, 1);
            // observerPlanetInfo(selYear2, selMonth2, selDate2, selHour2, selMin2, latitude2, longitude2, 2);
            Calendar selCal1 = Calendar.getInstance();
            selCal1.set(Calendar.YEAR, selYear1 + 100);
            selCal1.set(Calendar.MONTH, selMonth1);
            selCal1.set(Calendar.DAY_OF_MONTH, selDate1);
            selCal1.set(Calendar.HOUR_OF_DAY, 0);
            selCal1.set(Calendar.MINUTE, 0);

            Calendar selCal2 = Calendar.getInstance();
            selCal2.set(Calendar.YEAR, selYear2 + 100);
            selCal2.set(Calendar.MONTH, selMonth2);
            selCal2.set(Calendar.DAY_OF_MONTH, selDate2);
            selCal2.set(Calendar.HOUR_OF_DAY, 0);
            selCal2.set(Calendar.MINUTE, 0);
            new handlecalendarTask(selCal1, selCal2, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1, selYear2, selMonth2, selDate2, selHour2, selMin2, latitude2, longitude2).execute();


            ViewPager tabViewPager = rootView.findViewById(R.id.tabViewPager);
            KundaliMatchPagerAdapter adapter = KundaliMatchPagerAdapter.newInstance(getChildFragmentManager(), mContext, args);
            TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(tabViewPager);
            tabViewPager.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public int ganaPointCalculate(int val1, int val2) {
        // val1-bride and val2-groom
        int ganaPoint = -1;
        if (val1 == val2) {
            ganaPoint = 6;
        } else if (val1 == 0 && val2 == 1) {
            ganaPoint = 3;
        } else if (val1 == 0 && val2 == 2) {
            ganaPoint = 1;
        } else if (val1 == 1 && val2 == 0) {
            ganaPoint = 5;
        } else if (val1 == 1 && val2 == 2) {
            ganaPoint = 3;
        } else if (val1 == 2 && val2 == 0) {
            ganaPoint = 0;
        } else if (val1 == 2 && val2 == 1) {
            ganaPoint = 0;
        }
        return ganaPoint;

    }

    public int yoniPointCalculate(int i, int j) {
        int[][] yoniPoints = new int[][]{
                {4, 2, 2, 3, 2, 2, 2, 1, 0, 1, 1, 3, 2, 1},
                {2, 4, 3, 3, 2, 2, 2, 2, 3, 1, 2, 3, 2, 0},
                {2, 3, 4, 3, 2, 2, 2, 2, 3, 1, 2, 3, 2, 0},
                {3, 3, 2, 4, 2, 1, 1, 1, 1, 2, 2, 2, 0, 2},
                {2, 2, 1, 2, 4, 2, 1, 2, 2, 1, 0, 2, 1, 1},
                {2, 2, 2, 1, 2, 4, 0, 2, 2, 1, 3, 3, 2, 1},
                {2, 2, 1, 1, 1, 0, 4, 2, 2, 2, 2, 2, 1, 2},
                {1, 2, 3, 1, 2, 2, 2, 4, 3, 0, 3, 2, 2, 1},
                {0, 3, 3, 1, 2, 2, 2, 3, 4, 1, 2, 2, 2, 2},
                {1, 1, 1, 2, 1, 1, 2, 0, 1, 4, 1, 1, 2, 1},
                {1, 2, 2, 2, 0, 3, 2, 3, 2, 1, 4, 2, 2, 1},
                {3, 3, 0, 2, 2, 3, 2, 2, 2, 1, 2, 4, 3, 2},
                {2, 2, 3, 0, 1, 2, 1, 2, 2, 2, 2, 3, 4, 2},
                {1, 0, 1, 2, 1, 1, 2, 1, 2, 1, 1, 2, 2, 4},
        };
        return yoniPoints[i][j];


    }

    public int varnaPointCalculate(int cand1, int cand2) {
        //cand1 bride, cand 2 groom
        // groom varna always  higher or equal than bride

        if (cand2 <= cand1) {
            System.out.println(cand2 + ":2bridegroom1:" + cand1);
            return 1;
        }
        System.out.println(cand2 + ":1bridegroom1:" + cand1);
        return 0;

    }

    public double vasyaPointCalculate(int cand1, int cand2) {
        // cand1-bride cand2-groom
        if (cand1 == cand2 || (cand1 == 0 && cand2 == 4) || (cand1 == 2 && cand2 == 3) || (cand1 == 2 && cand2 == 4)) { //same
            return 2;
        } else if ((cand1 == 0 && cand2 == 2) || (cand1 == 2 && cand2 == 0) || (cand1 == 4 && cand2 == 0) || (cand1 == 4 && cand2 == 2)) {
            return 1;
        } else if ((cand1 == 0 && cand2 == 1) || (cand1 == 1 && cand2 == 0)) {
            return 0.5;
        } else {
            return 0;
        }
    }

    public double taraPointCalculate(int cand1, int cand2) {
        double taraPoint;
        int diff1 = cand2 - cand1;
        int diff2;
        if (diff1 == 0) {
            diff2 = 0;
        } else if (diff1 > 0) {
            diff2 = (27 - cand2) + cand1;
        } else {
            diff2 = Math.abs(diff1);
            diff1 = (27 - cand1) + cand2;

        }
        int diff1Rem = (diff1 % 9);
        int diff2Rem = (diff2 % 9);
        boolean rem1Good = (diff1Rem != 3 && diff1Rem != 5 && diff1Rem != 7);
        boolean rem2Good = (diff2Rem != 3 && diff2Rem != 5 && diff2Rem != 7);
        if (rem1Good && rem2Good)
            taraPoint = 3;
        else if (rem1Good || rem2Good)
            taraPoint = 1.5;
        else
            taraPoint = 0;

        return taraPoint;
    }

    public int bhakutaPointCalculate(int cand1, int cand2) {
        int diff1 = (cand2 - cand1);
        int diff2;
        if (diff1 == 0) {
            diff1 = 1;
            diff2 = 1;

        } else if (diff1 > 0) {
            diff2 = (12 - cand2) + cand1 + 1;
            diff1 = diff1 + 1;
        } else {
            diff2 = Math.abs(diff1) + 1;
            diff1 = (12 - cand1) + cand2 + 1;

        }
        //2/12, 5/9 and 6/8
        int bhakutaPoint = 7;
        if (((diff1 == 2 && diff2 == 12) || (diff1 == 12 && diff2 == 2)) || ((diff1 == 5 && diff2 == 9) || (diff1 == 9 && diff2 == 5)) || ((diff1 == 6 && diff2 == 8) || (diff1 == 8 && diff2 == 6))) {
            bhakutaPoint = 0;
        }
        Log.e("KUNDALI", "KUNDALI:[" + cand1 + "][" + cand2 + "]" + ":diff1:" + diff1 + ":diff2:" + diff2 + ":bhakutaPoint:" + bhakutaPoint);

        return bhakutaPoint;
    }

    public double maitriPointCalculate(int i, int j) {
        /*  Double[][] maitriPoints=new Double[][]{
                {5.0, 5.0, 5.0, 4.0, 5.0, 0.0, 0.0},
                {5.0, 5.0, 4.0, 1.0, 4.0, 0.5, 0.5},
                {5.0, 4.0, 5.0, 0.5, 5.0, 3.0, 0.5},
                {4.0, 1.0, 0.5, 5.0, 0.5, 5.0, 4.0},
                {5.0, 4.0, 5.0, 0.5, 5.0, 0.5, 3.0},
                {0.0, 0.5, 3.0, 5.0, 0.5, 5.0, 5.0},
                {0.0, 0.5, 0.5, 4.0, 3.0, 5.0, 5.0},

        };

         String[][] maitriIntPoints=new String[][]{
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},
            {"Su","Mo","Ma","Me","Ju","Ve","Sa"},

    };

     String[][] maitriPlanetRels=new String[][]{
            {"F","F","F","N","F","E","E"},
            {"F","F","N","F","N","N","N"},
            {"F","F","F","E","F","N","N"},
            {"F","E","N","F","N","F","N"},
            {"F","F","F","E","F","E","N"},
            {"E","E","N","F","N","F","F"},
            {"E","E","E","F","N","F","F"},

    };
      1 <item>ରବି</item>
       2 <item>ଚନ୍ଦ୍ର</item>
       3 <item>ବୁଧ</item>
       4 <item>ଶୁକ୍ର</item>
       5 <item>ମଙ୍ଗଳ</item>
       6 <item>ବୃହସ୍ପତି</item>
       7 <item>ଶନି</item>
        * */
        // Friend=1, Neutral=2,Enemies=3
        //  {"Su","Mo","Ma","Me","Ju","Ve","Sa"},

        int[][] maitriPlanetRels = new int[][]{
                {1, 1, 1, 2, 1, 3, 3},
                {1, 1, 2, 1, 2, 2, 2},
                {1, 1, 1, 3, 1, 2, 2},
                {1, 3, 2, 1, 2, 1, 2},
                {1, 1, 1, 3, 1, 3, 2},
                {3, 3, 2, 1, 2, 1, 1},
                {3, 3, 3, 1, 2, 1, 1},

        };

        int val1 = maitriPlanetRels[i][j];
        int val2 = maitriPlanetRels[j][i];
        double maitriPointVal = 0.0;
        if (val1 == 1 && val2 == 1) {
            maitriPointVal = 5.0;
        } else if ((val1 == 1 && val2 == 2) || (val1 == 2 && val2 == 1)) {
            maitriPointVal = 4.0;
        } else if (val1 == 2 && val2 == 2) {
            maitriPointVal = 3.0;
        } else if ((val1 == 1 && val2 == 3) || (val1 == 3 && val2 == 1)) {
            maitriPointVal = 1;
        } else if ((val1 == 2 && val2 == 3) || (val1 == 3 && val2 == 2)) {
            maitriPointVal = 0.5;
        } else if (val1 == 3 && val2 == 3) {
            maitriPointVal = 0.0;
        }
        Log.e("KUNDALI", "KUNDALI:[" + i + "][" + j + "]" + ":val1:" + val1 + ":val2:" + val2 + ":maitriPointVal:" + maitriPointVal);
        return maitriPointVal;

    }

    public String roundVal(double totalScored) {
        DecimalFormat value = new DecimalFormat("#.#");
        String val = value.format(totalScored);
        if (!val.contains(".")) {
            return Utility.getInstance(mContext).getDayNo(val);
        } else {
            return Utility.getInstance(mContext).getDayNo(val.split("\\.")[0]) + "." + Utility.getInstance(mContext).getDayNo(val.split("\\.")[1]);
        }
    }

    public void addPointsToLayout() {
        //https://imarriages.com/advice/how-kundali-matching-works

        totalScored = 0.0;
        pointsCntr.removeAllViews();
        for (int i = 0; i < 8; i++) {

            View child = getLayoutInflater().inflate(R.layout.inflate_kundali_match_res, null);
            guna36 cand1 = guna36List.get(i);
            guna36 cand2 = guna36List.get(8 + i);
            Log.e("cand1", "36guna:" + (i + 1) + "-" + cand1.type + ":" + cand2.type + "-" + cand1.res + ":" + cand2.res);
            TextView titleTv = child.findViewById(R.id.title);
            TextView candTv1 = child.findViewById(R.id.cand1);
            TextView candTv2 = child.findViewById(R.id.cand2);
            TextView point = child.findViewById(R.id.points);
            TextView total = child.findViewById(R.id.total);
            TextView desc = child.findViewById(R.id.desc);
            titleTv.setTag(1);
            titleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((int) view.getTag()) == 1) {
                        desc.setVisibility(View.VISIBLE);
                        view.setTag(0);
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp, 0, 0, 0);
                    } else {
                        desc.setVisibility(View.GONE);
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_right_black_24dp, 0, 0, 0);
                        view.setTag(1);
                    }
                }
            });

            titleTv.setText(le_arr_kundali_points[cand1.type - 1]);
            candTv1.setText(cand1.name);
            candTv2.setText(cand2.name);
            String max = Utility.getInstance(mContext).getDayNo("" + (i + 1));

            total.setText(max);
            if (i == 0) {//varna
                System.out.println(cand2.name + ":2bridegroom1:" + cand1.name);
                double varnaPoint = varnaPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + varnaPoint;
                point.setText(roundVal(varnaPoint));
                desc.setText("Varna is assigned 1 points out of 36 and yours Varna point is " + varnaPoint + ". Varna Kuta represents the working attitude and capacity. The bridegroom's capacity needs to be higher than that of the bride for smooth running of the family. It also represents mutual love, comfort and Obedience. Grade of spiritual development is also depends on Varna Kuta.");

                //  point.setText(diff1Rem+"-"+diff2Rem+"/"+taraPoint + "/" + diff1+"-"+diff2+"|"+cand1.res+"-"+cand2.res);
            } else if (i == 1) {//vasya
                double vasyaPoint = vasyaPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + vasyaPoint;
                point.setText(roundVal(vasyaPoint));
                desc.setText("Vasya is assigned 2 points out of 36 and yours Vasya point is " + vasyaPoint + ". Vasya Kuta represents Mutual Control or dominance and used to determine whether there will be a dedicated and compatible relationship between two people. It also shows friendship and amenability between the couple.");

                //  point.setText(diff1Rem+"-"+diff2Rem+"/"+taraPoint + "/" + diff1+"-"+diff2+"|"+cand1.res+"-"+cand2.res);
            } else if (i == 2) {//tara
                double taraPoint = taraPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + taraPoint;
                point.setText(roundVal(taraPoint));
                desc.setText("Tara is assigned 3 points out of 36 and yours Tara point is " + taraPoint + ". Tara Kuta is used to calculate the health and well-being of the bride and groom after marriage.  It also represents Luck, auspiciousness and transmission of mutual beneficence between the couple.");

                //  point.setText(diff1Rem+"-"+diff2Rem+"/"+taraPoint + "/" + diff1+"-"+diff2+"|"+cand1.res+"-"+cand2.res);
            } else if (i == 3) {//yoni
                double yoniPoint = yoniPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + yoniPoint;
                point.setText(roundVal(yoniPoint));
                desc.setText("Yoni is assigned 4 points out of 36 and yours Yoni point is " + yoniPoint + ". Yoni Kuta, as name suggests, indicates the physical and sexual compatibility between a couple.");
                //  point.setText(diff1Rem+"-"+diff2Rem+"/"+taraPoint + "/" + diff1+"-"+diff2+"|"+cand1.res+"-"+cand2.res);
            } else if (i == 4) {
                double maitriPoint = maitriPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + maitriPoint;
                point.setText(roundVal(maitriPoint));
                desc.setText("Maitri is assigned 5 points out of 36 and yours Maitri point is " + maitriPoint + ". Maitri Kuta is used to examine the strength of the love between the couple.  It also represents psychological disposition, mental qualities and Affection between the couple.");
            } else if (i == 5) {
                int ganaPoint = ganaPointCalculate(cand2.res, cand1.res);
                totalScored = totalScored + ganaPoint;
                point.setText(roundVal(ganaPoint));
                desc.setText("Gana is assigned 6 points out of 36 and yours Gana point is " + ganaPoint + ". Gana Kuta represents Nature, longevity, wealth, prosperity and love.It is used to identify an individuals temperament");
            } else if (i == 6) { //bhakuta
                int bhakutaPoint = bhakutaPointCalculate(cand1.res, cand1.res);
                totalScored = totalScored + bhakutaPoint;
                point.setText(roundVal(bhakutaPoint));
                desc.setText("Bhakoot is assigned 7 points out of 36 and yours Bhakoot point is " + bhakutaPoint + ". Bhakoot is used to verify the overall health, welfare and prosperity of a family after marriage.It also represents children, wealth, comforts, good luck and growth of the family.");
            } else {

                int nadiPoint = (cand1.res != cand2.res) ? 8 : 0;
                totalScored = totalScored + nadiPoint;
                point.setText(roundVal(nadiPoint));
                desc.setText("Nadi Kuta is assigned 8 points out of 36 and yours Nadi Kuta point is " + nadiPoint + ". Nadi Kuta is to check the genetic compatibility of the bride and groom . It also represents temperaments, nervous energy, affliction and death to the other.");
            }


            pointsCntr.addView(child);
        }


    }

    private class handlecalendarTask extends AsyncTask<String, Integer, Integer> {
        HashMap<String, CoreDataHelper> myPanchangHashMap;
        int selYear1, selMonth1, selDate1, selHour1, selMin1, type;
        String latitude1, longitude1;
        int selYear2, selMonth2, selDate2, selHour2, selMin2, type2;
        String latitude2, longitude2;
        long start1, end1, start2, end2;


        public handlecalendarTask(Calendar selCal1, Calendar selCal2, int selYear1, int selMonth1, int selDate1, int selHour1, int selMin1, String latitude1, String longitude1, int selYear2, int selMonth2, int selDate2, int selHour2, int selMin2, String latitude2, String longitude2) {
            start1 = selCal1.getTimeInMillis() - 1 * 32 * 24 * 60 * 60 * 1000L;
            end1 = selCal1.getTimeInMillis() + 1 * 1 * 24 * 60 * 60 * 1000L;
            start2 = selCal2.getTimeInMillis() - 1 * 32 * 24 * 60 * 60 * 1000L;
            end2 = selCal2.getTimeInMillis() + 1 * 1 * 24 * 60 * 60 * 1000L;
            this.selYear1 = selYear1;
            this.selMonth1 = selMonth1;
            this.selDate1 = selDate1;
            this.selHour1 = selHour1;
            this.selMin1 = selMin1;
            this.latitude1 = latitude1;
            this.longitude1 = longitude1;

            this.selYear2 = selYear2;
            this.selMonth2 = selMonth2;
            this.selDate2 = selDate2;
            this.selHour2 = selHour2;
            this.selMin2 = selMin2;
            this.latitude2 = latitude2;
            this.longitude2 = longitude2;

        }

        protected Integer doInBackground(String... urls) {

            EphemerisDao ephemerisDao = AppDatabase.getInstance(mContext).ephemerisDao();

            List<EphemerisEntity> ephemerisList1 = ephemerisDao.getAll2(start1, end1);
            List<EphemerisEntity> ephemerisList2 = ephemerisDao.getAll2(start2, end2);

            PanchangTask ptObj = new PanchangTask();
            HashMap<String, CoreDataHelper> myPanchangHashMap1 = ptObj.panchangMapping(ephemerisList1, mPref.getMyLanguage(), latitude1, longitude1, mContext);

            PanchangTask ptObj2 = new PanchangTask();
            HashMap<String, CoreDataHelper> myPanchangHashMap2 = ptObj2.panchangMapping(ephemerisList2, mPref.getMyLanguage(), latitude2, longitude2, mContext);

            setCalendarData(myPanchangHashMap1, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1/*, type*/);
            setCalendarData(myPanchangHashMap2, selYear2, selMonth2, selDate2, selHour2, selMin2, latitude2, longitude2/*, type*/);

            return type;
        }

        protected void onPostExecute(Integer result) {
            try {

                if (guna36List.size() > 8) {
                    addPointsToLayout();
                    String msg;
                    if (totalScored < 18) {
                        msg = "Incompatible match. ";
                    } else if (totalScored >= 18 && totalScored <= 24) {
                        msg = "Acceptable match. ";
                    } else if (totalScored > 24 && totalScored < 32) {
                        msg = "Very good match. ";
                    } else {
                        msg = "Perfect match. ";
                    }
                    DecimalFormat value = new DecimalFormat("#.#");
                    String score = value.format(totalScored);
                    // String score=roundVal(totalScored);
                    titleInfo2.setText(msg + score + " points outs of 36 points.");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap, int selYear1, int selMonth1, int selDate1, int selHour1, int selMin1, String latitude1, String longitude1/*, int type*/) {
        try {


            if (!mAllPanchangHashMap.isEmpty()) {

                Calendar selCal = Calendar.getInstance(Locale.ENGLISH);
                selCal.set(Calendar.YEAR, selYear1);
                selCal.set(Calendar.MONTH, selMonth1);
                selCal.set(Calendar.DAY_OF_MONTH, selDate1);
                selCal.set(Calendar.HOUR_OF_DAY, selHour1);
                selCal.set(Calendar.MINUTE, selMin1);
                selCal.set(Calendar.SECOND, 0);
                selCal.set(Calendar.MILLISECOND, 0);
                Utility.SunDetails sun = Utility.getInstance(mContext).getTodaySunDetails(selCal, true);
                Log.e("sunRise", "sunsunRiseRise:" + sun.sunRise);
                String[] HrMinArr = sun.sunRise.split(" ")[0].split(":");
                Calendar selCalSR = Calendar.getInstance(Locale.ENGLISH);
                selCalSR.set(Calendar.YEAR, selYear1);
                selCalSR.set(Calendar.MONTH, selMonth1);
                selCalSR.set(Calendar.DAY_OF_MONTH, selDate1);
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
                    int lselYear = selCalPrevday.get(Calendar.YEAR);
                    int lselMonth = selCalPrevday.get(Calendar.MONTH);
                    int lselDate = selCalPrevday.get(Calendar.DAY_OF_MONTH);
                    String key1 = lselYear + "-" + lselMonth + "-" + lselDate;
                    myCoreData = mAllPanchangHashMap.get(key1);
                    // String key = selYear1 + "" + selMonth1 + "" + selDate1;
                    //  CoreDataHelper myCoreDataForPlanetinfo = mAllPanchangHashMap.get(key);
                    //  planetInfo = myCoreDataForPlanetinfo.getPlanetInfo();

                } else {
                    considerPrevday = false;
                    String key = selYear1 + "-" + selMonth1 + "-" + selDate1;
                    myCoreData = mAllPanchangHashMap.get(key);
                    //  planetInfo = myCoreData.getPlanetInfo();
                }

                planetInfo = myCoreData.getPlanetInfo();
                // EphemerisEntity planetInfo = myCoreData.getPlanetInfo();


                setPlanetInfo(planetInfo, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1/*, type*/);
                // PanchangUtility panchangUtility = new PanchangUtility();


                String sunDeg = planetInfo.sun;
                String sunDmDeg = planetInfo.dmsun;
                double lat = Double.parseDouble(latitude1);
                double sunDegAtSunrise1 = Helper.getInstance().getPlanetPos(myCoreData.getSunRise(), sunDeg, Double.parseDouble(sunDmDeg));

                double dayInt = selDate1;
                double A = (16.90709 * selYear1) / 1000 - (0.757371 * selYear1) / 1000 * selYear1 / 1000 - 6.92416100010001000;
                double B = (selMonth1 + (dayInt / 30)) * 1.1574074 / 1000;
                double ayamansa = A + B;
                double sunDegAtSunrise = sunDegAtSunrise1 + ayamansa;
                if (sunDegAtSunrise >= 360) {
                    sunDegAtSunrise = sunDegAtSunrise - 360;
                }

                ArrayList<Helper.lagna> lagnaListAtSunrise = Helper.getInstance().getLagna(myCoreData.getSunRise(), sunDegAtSunrise, lat);

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
                    long moonSignhalf = moonSign.currValEndTime.getTimeInMillis() - moonSign.currValStartTime.getTimeInMillis();
                    moonSignKundali = moonSign.currVal;
                    if ((selCal.getTimeInMillis() - moonSign.currValStartTime.getTimeInMillis()) > moonSignhalf) {
                        isMoonSignFirstHalf = false;
                    } else {
                        isMoonSignFirstHalf = true;
                    }


                } else {
                    moonSignKundali = moonSign.nextVal;
                    long moonSignhalf = moonSign.le_nextValEndTime.getTimeInMillis() - moonSign.currValEndTime.getTimeInMillis();
                    if ((selCal.getTimeInMillis() - moonSign.currValEndTime.getTimeInMillis()) > moonSignhalf) {
                        isMoonSignFirstHalf = false;
                    } else {
                        isMoonSignFirstHalf = true;
                    }

                }


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


                lagnaLngAtSunrise = getLatLng(sunDegAtSunrise)[0];


                int size = lagnaListAtSunrise.size();
                Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:" + lagnaListAtSunrise.size());
                double elapse = 0, totalDeg = -1.0;
                double diff = 0;
                int lagnarashi = 0;
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
                    Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:::" + dateFormat.format(today) + "-" + dateFormat.format(start1) + "-" + dateFormat.format(end1));
                    Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:::" + selCal.getTimeInMillis() + "-" + end.getTimeInMillis() + "-" + start.getTimeInMillis());

                    if (selCal.getTimeInMillis() >= start.getTimeInMillis() && selCal.getTimeInMillis() <= end.getTimeInMillis()) {
                        Log.e("lagnaListAtSunrise", "lagnaListAtSunrise:x:" + lagnaListAtSunrise.size());

                        diff = end.getTimeInMillis() - start.getTimeInMillis();
                        elapse = selCal.getTimeInMillis() - start.getTimeInMillis();
                        diff = Math.abs(diff);
                        elapse = Math.abs(elapse);
                        double elapseDeg = ((30.0 / diff) * elapse);
                        int index = lagnaListAtSunrise.get(x).rashi;
                        totalDeg = (index * 30) + elapseDeg;
                        totalDeg = totalDeg - ayamansa;
                        if (totalDeg < 0) {
                            totalDeg = 360 - totalDeg;
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


                Log.e("nowlagnaval", lagnaLngAtSunrise + "::HOUSE:nowlagnaval:" + lagnaLngNow + ":totalDeg:" + totalDeg + ":diff:" + diff + ":elapse:" + elapse);

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
                            planetStr = planetStr + le_arr_planet[planetHM.get(i)] + ",";
                        }

                    }
                    houseinfosMap.put(house, obj);
                    obj.planetList = planetStr;
                    houseinfos.add(obj);
                }


                PanchangUtility panchangUtility = new PanchangUtility();
                PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), latitude1, longitude1, mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());
                String lgajapati;

                String lDay = Utility.getInstance(mContext).getDayNo("" + selDate1);
                String lYear = Utility.getInstance(mContext).getDayNo("" + selYear1);
                //  String lmonth = Utility.getInstance(mContext).getDayNo(""+selMonth1);
                String monthStr = le_arr_month[selMonth1];


                String tithiVal = le_arr_tithi[tithiKundali - 1] + " " + le_tithi;

                String nakshetraVal = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra;

                //  nakshetraTithi=l_nakshatra_arr[nakshetraKundali - 1] +"\n"+"---"+"\n"+l_tithi_fullarr[tithiKundali - 1];
                nakshetraTithi = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_arr_rasi_kundali[moonSignKundali - 1];
                String jogaVal = le_arr_joga[jogaKundali - 1] + " " + le_joga;
                String karanaVal = le_arr_karana[karanaKundali - 1] + " " + le_karana;
                String moonSignVal = le_arr_rasi_kundali[moonSignKundali - 1] + " " + le_lunar_rashi;
                String sunSignVal = le_arr_rasi_kundali[sunSignKundali - 1] + " " + le_solar_rasi;

              /*  String[] le_arr_kundali_gana, le_arr_kundali_yoni, le_arr_kundali_varna, le_arr_kundali_guna, le_arr_kundali_nadi, en_kundali_tara, le_arr_kundali_vasya;
                String le_kundali_yoni_title, le_kundali_tara_title, le_kundali_vasya_title, le_kundali_guna_title, le_kundali_gana_title, le_kundali_nadi_title, le_kundali_varna_title;
                le_arr_kundali_gana = mContext.getResources().getStringArray(R.array.l_arr_kundali_gana);
                le_arr_kundali_guna = mContext.getResources().getStringArray(R.array.l_arr_kundali_guna);
                le_arr_kundali_varna = mContext.getResources().getStringArray(R.array.l_arr_kundali_varna);
                le_arr_kundali_yoni = mContext.getResources().getStringArray(R.array.l_arr_kundali_yoni);
                le_arr_kundali_nadi = mContext.getResources().getStringArray(R.array.l_arr_kundali_nadi);
              //  en_kundali_tara = mContext.getResources().getStringArray(R.array.en_kundali_tara);
                le_arr_kundali_vasya = mContext.getResources().getStringArray(R.array.l_arr_kundali_vasya);

                le_kundali_yoni_title = mContext.getResources().getString(R.string.l_kundali_yoni_title);
                le_kundali_tara_title = mContext.getResources().getString(R.string.l_kundali_tara_title);
                le_kundali_vasya_title = mContext.getResources().getString(R.string.l_kundali_vasya_title);
                le_kundali_guna_title = mContext.getResources().getString(R.string.l_kundali_guna_title);
                le_kundali_gana_title = mContext.getResources().getString(R.string.l_kundali_gana_title);
                le_kundali_nadi_title = mContext.getResources().getString(R.string.l_kundali_nadi_title);
                le_kundali_varna_title = mContext.getResources().getString(R.string.l_kundali_varna_title);*/


                int gana = getGana(nakshetraKundali);
                int guna = getGuna(moonSignKundali);
                int varna = getVarna(moonSignKundali);
                int nadi = getNadi(nakshetraKundali);
                int vasya = getVasya(moonSignKundali);
                Log.e("Vasya", vasya + ":Vasya:3:" + vasya);
                int yoni = getYoni(nakshetraKundali);
                int tara = getTara(nakshetraKundali);
                int maitri = getMaitri(moonSignKundali);
                int bhakuta = getBhakuta(moonSignKundali);


                String ganaStr = le_arr_kundali_gana[gana];
                String gunaStr = le_arr_kundali_guna[guna];
                String varnaStr = le_arr_kundali_varna[varna];
                String yoniStr = le_arr_kundali_yoni[yoni];
                String nadiStr = le_arr_kundali_nadi[nadi];
                String vasyaStr = le_arr_kundali_vasya[vasya];
                Log.e("Vasya", vasya + ":Vasya:4:" + vasyaStr);
                String janmaTaraStr = le_arr_nakshatra[nakshetraKundali - 1];
                String maitriStr = le_arr_day_planet[maitri];
                String taraStr = janmaTaraStr;
                String bhakutaStr = le_arr_rasi_kundali[moonSignKundali - 1];

                String extraStr = janmaTaraStr + " " + le_kundali_tara_title + ", " + ganaStr + " " + le_kundali_gana_title + ", " + gunaStr + " " + le_kundali_guna_title + ", " + varnaStr + " " + le_kundali_varna_title + ", " + yoniStr + " " + le_kundali_yoni_title + ", " + nadiStr + " " + le_kundali_nadi_title + ", " + vasyaStr + " " + le_kundali_vasya_title;

                Log.e("ganaStr", ",ganaStr-" + ganaStr + ",gunaStr-" + gunaStr + ",varnaStr-" + varnaStr + ",yoniStr-" + yoniStr + ",nadiStr-" + nadiStr + ",janmaTaraStr-" + janmaTaraStr + ",vasyaStr-" + vasyaStr);
                String headerStr;
                if (!myPanchangObj.le_sanSalAnka.equals("0") && mPref.getMyLanguage().contains("or")) {
                    // ok down to 1970, chnage cond before 1970 added
                    lgajapati = ", ଗଜପତ୍ୟାଙ୍କ-" + myPanchangObj.le_sanSalAnka;
                    headerStr = lDay + " " + monthStr + " " + lYear + ", " + le_samvat + "-" + myPanchangObj.le_samvata + ", " + le_shakaddha + "-" + myPanchangObj.le_sakaddha + ", " + le_sal + "-" + myPanchangObj.le_sanSal + lgajapati + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_bara;
                } else {
                    headerStr = lDay + " " + monthStr + " " + lYear + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_sakaddha + " " + le_shakaddha;
                }
                headerStr = headerStr + ", " + tithiVal + ", " + nakshetraVal + ", " + jogaVal + ", " + karanaVal + ", " + moonSignVal + ", " + sunSignVal + ", " + extraStr;
                guna36 guna36;
                // if (type == 1) {
                //  headerStr1 = headerStr;
                // } else {
                //  headerStr2 = headerStr;
                // }
                guna36 = new guna36();
                guna36.type = 1;
                guna36.res = varna;
                guna36.name = varnaStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 2;
                guna36.res = vasya;
                guna36.name = vasyaStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 3;
                guna36.res = tara;
                guna36.name = taraStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 4;
                guna36.res = yoni;
                guna36.name = yoniStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 5;
                guna36.res = maitri;
                guna36.name = maitriStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 6;
                guna36.res = gana;
                guna36.name = ganaStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 7;
                guna36.res = bhakuta;
                guna36.name = bhakutaStr;
                guna36List.add(guna36);

                guna36 = new guna36();
                guna36.type = 8;
                guna36.res = nadi;
                guna36.name = nadiStr;
                guna36List.add(guna36);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class guna36 {
        public int type, res;
        String name;
    }

    private int getGana(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        //Sattva Guna,Rakshasa Gana
        int gana = 2, guna = 2;
        if (starNo == 1 || starNo == 5 || starNo == 7 || starNo == 8 || starNo == 13 || starNo == 15 || starNo == 17 || starNo == 22 || starNo == 27) {
            gana = 0;//dev
        } else if (starNo == 2 || starNo == 4 || starNo == 6 || starNo == 11 || starNo == 12 || starNo == 20 || starNo == 21 || starNo == 25 || starNo == 26) {
            gana = 1;//manaba
        }
        return gana;

    }

    private int getGuna(int moonSignKundali) {
        int guna = 2, moonsignNo = moonSignKundali;

        if (moonsignNo == 1 || moonsignNo == 4 || moonsignNo == 7 || moonsignNo == 10) {
            guna = 0;//Rajas Guna
        } else if (moonsignNo == 2 || moonsignNo == 5 || moonsignNo == 8 || moonsignNo == 11) {
            guna = 1;//Tamas Guna
        }
        return guna;
    }

    private int getVasya(int moonSignKundali) {
        int vasya = 4;
        int moonsignNo = moonSignKundali;
        if (moonsignNo == 1 || moonsignNo == 2 || (!isMoonSignFirstHalf && moonsignNo == 9) || (isMoonSignFirstHalf && moonsignNo == 10)) { //the second half of Dhanu, the first half of Makara.
            vasya = 0;//Chatushpada
        } else if (moonsignNo == 3 || moonsignNo == 6 || moonsignNo == 7 || moonsignNo == 11 || (isMoonSignFirstHalf && moonsignNo == 9)) {//the first half of Dhanu,
            vasya = 1;//Nara
        } else if (moonsignNo == 4 || moonsignNo == 12 || (!isMoonSignFirstHalf && moonsignNo == 10)) {//the 2nd half of Makara.
            Log.e("Vasya", moonsignNo + ":Vasya:2:" + vasya);
            vasya = 2;//Jalachara
        } else if (moonsignNo == 5) {
            vasya = 3;//Vanachara
        } else if (moonsignNo == 8) {
            Log.e("Vasya", moonsignNo + ":Vasya:1:" + vasya);
            vasya = 4;//Keeta
        }
        return vasya;
    }

    private int getMaitri(int moonSignKundali) {

        return rashiOwnerPlanet[moonSignKundali - 1];
    }

    private int getBhakuta(int moonSignKundali) {

        return moonSignKundali;
    }

    private int getVarna(int moonSignKundali) {
        int varna = 3;
        int moonsignNo = moonSignKundali;
        if (moonsignNo == 4 || moonsignNo == 8 || moonsignNo == 12) {
            varna = 0;//Rajas Guna
        } else if (moonsignNo == 1 || moonsignNo == 5 || moonsignNo == 9) {
            varna = 1;//Tamas Guna
        } else if (moonsignNo == 2 || moonsignNo == 6 || moonsignNo == 10) {
            varna = 2;//Tamas Guna
        }
        return varna;
    }

    private int getNadi(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        int nadi = 2;//Antya
        if (starNo == 1 || starNo == 6 || starNo == 13 || starNo == 18 || starNo == 19 || starNo == 25 || starNo == 7 || starNo == 12 || starNo == 24) {
            nadi = 0;//Adi
        } else if (starNo == 2 || starNo == 17 || starNo == 14 || starNo == 23 || starNo == 5 || starNo == 11 || starNo == 20 || starNo == 8 || starNo == 26) {
            nadi = 1;//MAdhya
        }
        return nadi;

    }

    public int getTara(int nakshetraKundali) {
       /* int starNo = nakshetraKundali;
        int tara = -1;

        if (starNo == 1 || starNo == 24) {
            tara = 0;
        }*/
        return nakshetraKundali;//tara;
    }

    public int getYoni(int nakshetraKundali) {
        int starNo = nakshetraKundali;
        int joni = -1;

        if (starNo == 1 || starNo == 24) {
            joni = 0;
        } else if (starNo == 2 || starNo == 27) {
            joni = 1;
        } else if (starNo == 3 || starNo == 8) {
            joni = 2;
        } else if (starNo == 4 || starNo == 5) {
            joni = 3;
        } else if (starNo == 6 || starNo == 19) {
            joni = 4;
        } else if (starNo == 7 || starNo == 9) {
            joni = 5;
        } else if (starNo == 10 || starNo == 11) {
            joni = 6;
        } else if (starNo == 12 || starNo == 26) {
            joni = 7;
        } else if (starNo == 13 || starNo == 15) {
            joni = 8;
        } else if (starNo == 14 || starNo == 16) {
            joni = 9;
        } else if (starNo == 17 || starNo == 18) {
            joni = 10;
        } else if (starNo == 20 || starNo == 22) {
            joni = 11;
        } else if (starNo == 21) {
            joni = 12;
        } else if (starNo == 23 || starNo == 25) {
            joni = 13;
        }
        return joni;

    }

    public TextView addTv1(String val, float weight) {
        TextView tv = new TextView(mContext);
        tv.setText(val);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.START);

        return tv;

    }


    public static class houseinfo {
        public int houseno, rashi;
        public String planetList;
    }

    public String[] getLatLng(double deg) {
        String[] strArr;

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
            Log.e("degStr", deg + "::" + remDeg + ":remDegdegStr:" + degStr);
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

    public String getLatLng1(double deg) {
        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        double fractionInSec = (deg - val) * 3600;

        long min = (int) fractionInSec / 60;
        long sec = (int) fractionInSec % 60;

        String remDegStr = "" + remDeg;
        String minStr = "" + min;
        String secStr = "" + sec;
        return remDegStr + "° " + le_arr_rasi_kundali[index] + " " + minStr + "′" + " " + secStr + "′′";
    }

    public final String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final String DATE_FORMAT_2 = "hh:mm a";


    public void setPlanetInfo(EphemerisEntity planetInfo, int selYear1, int selMonth1, int selDate1, int selHour1, int selMin1, String latitude1, String longitude1/*, int type*/) {


        planetDataList.clear();
        // dont chnage order list

        planetDataList.add(calculatePlanetInfo(planetInfo.sun, planetInfo.dmsun, 1, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.moon, planetInfo.dmmoon, 2, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));

        planetDataList.add(calculatePlanetInfo(planetInfo.mars, planetInfo.dmmars, 5, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.mercury, planetInfo.dmmercury, 3, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.jupitor, planetInfo.dmjupitor, 6, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));

        planetDataList.add(calculatePlanetInfo(planetInfo.venus, planetInfo.dmvenus, 4, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.saturn, planetInfo.dmsaturn, 7, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));

        planetDataList.add(calculatePlanetInfo(planetInfo.node, planetInfo.dmnode, 11, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.node, planetInfo.dmnode, 12, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));

        planetDataList.add(calculatePlanetInfo(planetInfo.uranus, planetInfo.dmuranus, 8, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.neptune, planetInfo.dmneptune, 9, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));
        planetDataList.add(calculatePlanetInfo(planetInfo.pluto, planetInfo.dmpluto, 10, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1));


    }

    public PlanetData calculatePlanetInfo(String planet, String dmplanet, int type, int selYear1, int selMonth1, int selDate1, int selHour1, int selMin1, String latitude1, String longitude1) {

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
        selCal.set(Calendar.YEAR, selYear1);
        selCal.set(Calendar.MONTH, selMonth1);
        selCal.set(Calendar.DAY_OF_MONTH, selDate1);
        selCal.set(Calendar.HOUR_OF_DAY, selHour1);
        selCal.set(Calendar.MINUTE, selMin1);

        if (!considerPrevday) {
            cal1 = Calendar.getInstance();
            cal1.set(Calendar.YEAR, selYear1);
            cal1.set(Calendar.MONTH, selMonth1);
            cal1.set(Calendar.DAY_OF_MONTH, selDate1);
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


        //  DecimalFormat f = new DecimalFormat("##.00");
        // pd.deg = f.format(currPlanetDeg);
        pd.deg = currPlanetDeg;

      /*  String deg1Str = Utility.getInstance(mContext).getDayNo("" + pd.deg.split("\\.")[0]);
        String deg2Str = Utility.getInstance(mContext).getDayNo("" + pd.deg.split("\\.")[1]);
        pd.deg = deg1Str + "." + deg2Str;
        getLatLng(currPlanetDeg, pd);*/
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


        View rootView = inflater.inflate(R.layout.frag_kundali_match_calc, null);
        setHasOptionsMenu(false);

        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
    }

   /* public void observerPlanetInfo(int selYear1, int selMonth1, int selDate1, int selHour1, int selMin1, String latitude1, String longitude1, int type) {


        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear1 + 100);
        selCal1.set(Calendar.MONTH, selMonth1);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate1);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);


        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 0).observe(this, obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude1, longitude1, mContext);

                new handlecalendarTask(myPanchangHashMap, selYear1, selMonth1, selDate1, selHour1, selMin1, latitude1, longitude1, type).execute();
            }
        });

    }*/


    public static class PlanetData {
        public String planet, latLng;
        public int planetType, direction;
        public double deg;

    }


}

