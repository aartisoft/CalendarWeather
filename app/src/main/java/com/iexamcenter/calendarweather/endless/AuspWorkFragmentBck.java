package com.iexamcenter.calendarweather.endless;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
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
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.mydata.AuspData;
import com.iexamcenter.calendarweather.mydata.AuspWork;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.utility.AuspTableRowLayout;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.TableDataObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AuspWorkFragmentBck extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    LinearLayout tableContainer;
    public static boolean active = false;
    AuspTableRowLayout rowViewTable;
    MainActivity mContext;
    Resources res;
    HashMap<String, ArrayList<String>> monthFestivalmap;

    HashMap<String, CoreDataHelper> mDayViewHashMap;
    int displayYearInt, displayDayInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt, selectedDay, selectedMonth, selectedYear, pagePos;
    View rootView;
    String[] l_ausp_work, ausp_work_yes_no;
    String ausp_work_date;
    Locale usLocale;
    PrefManager mPref;
    ArrayList<ArrayList<AuspData>> records;
    ArrayList<String> columns, header;
    AuspData[][] myNewArr;
    List<TableDataObject> tableDataObject;
    ProgressBar progressBar;
    ArrayList<rangedate> alrangedate;
    ArrayList<PlanetData> planetDataList;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;

    public static Fragment newInstance() {
        return new AuspWorkFragmentBck();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        pagePos = args.getInt(ARG_POSITION);
        rootView = inflater.inflate(R.layout.fragment_ausp_day, container, false);

        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        res = getResources();
        lrashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
        rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
        erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

        eplanetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
        lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);


        if (!CalendarWeatherApp.isPanchangEng) {
            l_ausp_work = res.getStringArray(R.array.l_arr_ausp_work);
            ausp_work_date = res.getString(R.string.l_ausp_work_date);
            ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
            usLocale = new Locale.Builder().setLanguage(mPref.getMyLanguage()).build();
        } else {
            Configuration conf = res.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            res.updateConfiguration(conf, null); // second arg null means don't change
            l_ausp_work = res.getStringArray(R.array.l_arr_ausp_work);
            ausp_work_date = res.getString(R.string.l_ausp_work_date);
            ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
            usLocale = Locale.US;
            conf.locale = savedLocale;
            res.updateConfiguration(conf, null);

        }

        selectedDay = args.getInt("DAY");
        selectedMonth = args.getInt("MONTH");
        selectedYear = args.getInt("YEAR");

        Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);
        displayYearInt = selectedYear;
        displayMonthInt = selectedMonth;
        displayDayInt = selectedDay;


        tableContainer = rootView.findViewById(R.id.tableContainer);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new DownloadFilesTask().execute(null, null, null);


        return rootView;
    }


    public void getData() {


        ArrayList<AuspData> auspData = AuspWork.setAuspData();

        int year = displayYearInt;
        int mMonthIndex = displayMonthInt;
        columns = new ArrayList<>();
        header = new ArrayList<>();
        records = new ArrayList<>();
        HashMap<String, CoreDataHelper> mItems = mDayViewHashMap;
        Calendar cal;
        Calendar mycal = new GregorianCalendar(year, mMonthIndex, 1);


        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        callRetrograde("RetroJupiter", year);
        for (int position = 0; position < daysInMonth; position++) {
            try {
                String key = year + "" + mMonthIndex + "" + (position + 1);


                CoreDataHelper myCoreData = mItems.get(key);

                cal = Calendar.getInstance();
                cal.set(year, mMonthIndex, (position + 1));
                boolean jupiterBakra = checkDate(cal.getTime());

                SimpleDateFormat format1 = new SimpleDateFormat("EEEE\ndd-MMM", usLocale);
                String formatted = format1.format(cal.getTime());

                columns.add(formatted);

                EphemerisEntity planetInfo = myCoreData.getPlanetInfo();

                // 1-sun,4-venus,6-jupitor

                PlanetData sunData = calculatePlanetInfo(planetInfo.sun, planetInfo.dmsun, 1, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
                PlanetData venusData = calculatePlanetInfo(planetInfo.venus, planetInfo.dmvenus, 4, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
                PlanetData jupitorData = calculatePlanetInfo(planetInfo.jupitor, planetInfo.dmjupitor, 6, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));

                PlanetData chandraData = calculatePlanetInfo(planetInfo.moon, planetInfo.dmmoon, 2, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
                PlanetData saniData = calculatePlanetInfo(planetInfo.saturn, planetInfo.dmsaturn, 7, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));
                PlanetData mangalaData = calculatePlanetInfo(planetInfo.mars, planetInfo.dmmars, 5, year, mMonthIndex, (position + 1), myCoreData.getSunRise().get(Calendar.HOUR_OF_DAY), myCoreData.getSunRise().get(Calendar.MINUTE));


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
                boolean charturStart1 = (lunarPurMonthIndex == 2 && pakshaIndex == 0);// ashadha
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


                //   if (lunarPurMonthIndex > 2 && pakshaIndex == 1 && tithiIndex >= 25)
                //     Log.e("LunarMonth", "xxx:LunarMonth:m:" + myCoreData.getmDay() + "-" + myCoreData.getmMonth() + "-" + myCoreData.getmYear() + ":" + myCoreData.getLunarMonthPurnimantIndex() + ":p:" + myCoreData.getPaksha() + ":pp:" + myCoreData.getTithi().currVal);
                //2  asadha
                // 1 krushna

                //  myCoreData.getPaksha();
                //  asadha kunsha ekadasi to kartika shukla dashami
                //  sarana panchak
                int souraMasa = myCoreData.getAdjustSolarMonth();
                int chandraMasa = myCoreData.getLunarMonthPurnimantIndex();
                int bara = myCoreData.getDayOfWeek();
                int tithi1 = myCoreData.getTithi().currVal < 15 ? myCoreData.getTithi().currVal : (myCoreData.getTithi().currVal - 15);
                int nakshetra1 = myCoreData.getNakshetra().currVal;
                int tithi2 = myCoreData.getTithi().nextVal < 15 ? myCoreData.getTithi().nextVal : (myCoreData.getTithi().nextVal - 15);
                int nakshetra2 = myCoreData.getNakshetra().nextVal;


                int joga1 = myCoreData.getJoga().currVal;
                int joga2 = myCoreData.getJoga().nextVal;
                int paksha = myCoreData.getPaksha() + 1;
                int karana = getKarana(myCoreData.getKarana().val1);

                int karana2 = getKarana(myCoreData.getKarana().val2);
                int karana3 = getKarana(myCoreData.getKarana().val3);
                int karana4 = getKarana(myCoreData.getKarana().val4);


                ArrayList<AuspData> vivahList = new ArrayList<>();

                for (int i = 0; i < auspData.size(); i++) {
                    AuspData dataObj = (AuspData) auspData.get(i).clone();
                    if (position == 0)
                        header.add(l_ausp_work[i]);
                    dataObj.setName(l_ausp_work[i]);
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

                    boolean boolMasa = dataObj.getSouraMasa().contains("|" + souraMasa + "|");
                    boolean boolPakshya = dataObj.getPakshya().contains("|" + paksha + "|");
                    boolean boolBara = dataObj.getBara().contains("|" + bara + "|");
                    boolean boolTithi = dataObj.getTithi().contains("|" + tithi1 + "|") || dataObj.getTithi().contains("|" + tithi2 + "|");
                    boolean boolNakshetra = dataObj.getNakshetra().contains("|" + nakshetra1 + "|") || dataObj.getNakshetra().contains("|" + nakshetra2 + "|");
                    boolean boolJoga = dataObj.getJoga().contains("|" + joga1 + "|") || dataObj.getJoga().contains("|" + joga2 + "|");
                    boolean boolKarana = dataObj.getKarana().contains("|" + karana + "|") || dataObj.getKarana().contains("|" + karana2 + "|") || dataObj.getKarana().contains("|" + karana3 + "|") || dataObj.getKarana().contains("|" + karana4 + "|");

                    boolean commonRule = (boolMasa && boolPakshya
                            && boolBara
                            && boolTithi
                            && boolNakshetra
                            && boolJoga
                            && boolKarana);

                    String date1 = myCoreData.getmDay() + "-" + myCoreData.getmMonth() + "-" + myCoreData.getmYear();
                    if (dataObj.getMangalikaType() == 1) {
                        strictRule = (!kharaMasa && !guruAditya && !singhaGuru && !closerSunJupitor && !closerSunVenus && !isChaturMasa && !isSaranaPanchak && !malamasa && !jupiterBakra && !chandraRabiSaniMangala);
                        if (commonRule && strictRule) {
                            dataObj.setTodayAusp(true);
                            dataObj.setDayInfo(myCoreData);
                        } else {
                            dataObj.setTodayAusp(false);
                            dataObj.setDayInfo(myCoreData);
                        }
                    } else {
                        if (commonRule) {
                            dataObj.setTodayAusp(true);
                            dataObj.setDayInfo(myCoreData);
                        } else {
                            dataObj.setTodayAusp(false);
                            dataObj.setDayInfo(myCoreData);
                        }
                    }
                    Log.e("goodVal", dataObj.isTodayAusp()+":"+commonRule+":"+strictRule+"::"+date1 + "::" + dataObj.getName() + "::" + "goodNaksVal::" + goodVal + ":" + boolMasa + ":" + boolPakshya + ":" + boolBara + ":" + boolTithi + ":" + boolNakshetra + ":" + boolJoga + ":" + boolKarana);

                    vivahList.add(dataObj);

                }
                records.add(vivahList);


                int M = records.size();
                int N = header.size();
                myNewArr = new AuspData[N][M];
                for (int i = 0; i < this.records.size(); i++) {
                    ArrayList<AuspData> vals = this.records.get(i);
                    for (int j = 0; j < vals.size(); j++) {
                        myNewArr[j][i] = vals.get(j);
                    }
                }
                tableDataObject = new ArrayList<>();

                for (AuspData[] aMyNewArr : myNewArr) {
                    TableDataObject rowObj = new TableDataObject(aMyNewArr);

                    tableDataObject.add(rowObj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
                }else{
                    minRange = "1000-3-29"; // remove this one atichar added
                    maxRange = "1000-4-11";// dont add bakra for now
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

    public PlanetData calculatePlanetInfo(String planet, String dmplanet, int type, int selYear, int selMonth, int selDate, int selHour, int selMin) {

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

        long min = Math.round(fractionInSec) / 60;
        long sec = Math.round(fractionInSec) % 60;
        String latLng = remDeg + "° " + lrashiList[index] + " " + min + "′" + " " + sec + "′′";
        pd.latLng = latLng;
        // return latLng;
    }

    public static class PlanetData {
        public String planet, latLng, houseNo;
        public int planetType, direction;
        public double deg;

    }

    private class DownloadFilesTask extends AsyncTask<Void, Integer, Long> {
        // right thread dont remove
        @SuppressLint("WrongThread")
        protected Long doInBackground(Void... urls) {
            HashMap<String, CoreDataHelper> mDayViewHashMap1;
            HashMap<String, ArrayList<String>> monthFestivalmap1;


            mDayViewHashMap1 = CalendarWeatherApp.getHashMapAllPanchang1();
            monthFestivalmap1 = CalendarWeatherApp.getHashMapAllFest();


            monthFestivalmap = new HashMap<>();
            mDayViewHashMap = new HashMap<>();


            mDayViewHashMap.putAll(mDayViewHashMap1);
            monthFestivalmap.putAll(monthFestivalmap1);
            getData();
            rowViewTable = new AuspTableRowLayout(mContext, header, columns, ausp_work_date, ausp_work_yes_no, tableDataObject);

            //  rowViewTable = new AuspTableColumnLayout(mContext, columns, header, ausp_work_date, ausp_work_yes_no, tableDataObject);
            rowViewTable.setGravity(Gravity.CENTER_HORIZONTAL);
            return 1L;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
            tableContainer.removeAllViews();
            // AuspTableColumnLayout rowViewTable = new AuspTableColumnLayout(mContext, columns, header, ausp_work_date,  ausp_work_yes_no,tableDataObject);

            //  rowViewTable.setGravity(Gravity.CENTER_HORIZONTAL);
            tableContainer.addView(rowViewTable);
            progressBar.setVisibility(View.GONE);
        }
    }
}
