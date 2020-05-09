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

public class AuspWorkFragmentLatest extends Fragment {
    public double minTimeDuration=3.5;
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
        return new AuspWorkFragmentLatest();
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
                String key = year + "-" + mMonthIndex + "-" + (position + 1);


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
                boolean masant=false;
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
                int souraMasa = myCoreData.getAdjustSolarMonth();

                 int chandraMasa = myCoreData.getLunarMonthPurnimantIndex()+1;
                int bara = myCoreData.getDayOfWeek();
                int tithi1_30 = myCoreData.getTithi().currVal;
                int tithi2_30 = myCoreData.getTithi().nextVal;


                int tithi1 = myCoreData.getTithi().currVal < 15 ? myCoreData.getTithi().currVal : (myCoreData.getTithi().currVal - 15);
                double tithi1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getTithi().currValEndTime,myCoreData.getSunRiseCal());
                int tithi2 = myCoreData.getTithi().nextVal < 15 ? myCoreData.getTithi().nextVal : (myCoreData.getTithi().nextVal - 15);
                double tithi2Duration = getDuration(myCoreData.getTithi().currValEndTime, myCoreData.getTithi().le_nextValEndTime,myCoreData.getSunRiseCal());

                int nakshetra1 = myCoreData.getNakshetra().currVal;
                double nakshetra1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getNakshetra().currValEndTime,myCoreData.getSunRiseCal());
                int nakshetra2 = myCoreData.getNakshetra().nextVal;
                double nakshetra2Duration = getDuration(myCoreData.getNakshetra().currValEndTime, myCoreData.getNakshetra().le_nextValEndTime,myCoreData.getSunRiseCal());

                boolean isSankranthi = myCoreData.getSolarDayVal() == 1;
                boolean isPurnima = myCoreData.getTithi().currVal == 15;
                boolean isAmabasya = myCoreData.getTithi().currVal == 30;

                System.out.println("isPurnima::"+ myCoreData.getTithi().currVal);


                boolean trihasparsa = (myCoreData.getTithi().currVal != 0 && myCoreData.getTithi().nextVal != 0 && myCoreData.getTithi().nextToNextVal != 0);

                int joga1 = myCoreData.getJoga().currVal;
                double joga1Duration = getDuration(myCoreData.getSunRise(), myCoreData.getJoga().currValEndTime,myCoreData.getSunRiseCal());
                int joga2 = myCoreData.getJoga().nextVal;
                double joga2Duration = getDuration(myCoreData.getJoga().currValEndTime, myCoreData.getJoga().le_nextValEndTime,myCoreData.getSunRiseCal());


                int karana = getKarana(myCoreData.getKarana().val1);
                double karana1Duration = getDuration(myCoreData.getSunRiseCal(), myCoreData.getKarana().val1ET,myCoreData.getSunRiseCal());
                int karana2 = getKarana(myCoreData.getKarana().val2);
                double karana2Duration = getDuration(myCoreData.getKarana().val1ET, myCoreData.getKarana().val2ET,myCoreData.getSunRiseCal());
                int karana3 = getKarana(myCoreData.getKarana().val3);
                double karana3Duration = getDuration(myCoreData.getKarana().val2ET, myCoreData.getKarana().val3ET,myCoreData.getSunRiseCal());
                int karana4 = getKarana(myCoreData.getKarana().val4);
                double karana4Duration = getDuration(myCoreData.getKarana().val3ET, myCoreData.getKarana().val4ET,myCoreData.getSunRiseCal());


                ArrayList<AuspData> vivahList = new ArrayList<>();
                int vivahListIndex=0;

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

                   // boolean boolMasa = dataObj.getSouraMasa().contains("|" + souraMasa + "|");
                    boolean boolMasa = dataObj.getChandraMasa().contains("|" + chandraMasa + "|");
                    boolean boolPakshya = dataObj.getPakshya().contains("|" + paksha + "|");

                   // boolean boolBara = dataObj.getBara().contains("|" + bara + "|");
                    boolean boolTithi;
                    if(dataObj.getMangalikaType()==1){
                         boolTithi = dataObj.getTithi().contains("|" + tithi1_30 + "|") || dataObj.getTithi().contains("|" + tithi2_30 + "|");

                    }else{
                         boolTithi = dataObj.getTithi().contains("|" + tithi1 + "|") || dataObj.getTithi().contains("|" + tithi2 + "|");

                    }
                       //  boolean boolJoga = dataObj.getJoga().contains("|" + joga1 + "|") || dataObj.getJoga().contains("|" + joga2 + "|");
                   //  boolean boolKarana = dataObj.getKarana().contains("|" + karana + "|") || dataObj.getKarana().contains("|" + karana2 + "|") || dataObj.getKarana().contains("|" + karana3 + "|") || dataObj.getKarana().contains("|" + karana4 + "|");

                    boolean boolKarana = isGoodKarana(dataObj, karana, karana2, karana3, karana4, karana1Duration, karana2Duration, karana3Duration, karana4Duration);

                    boolean boolJoga =  isGoodJoga(dataObj, joga1, joga2, joga1Duration, joga2Duration);
                    System.out.println("joga1joga1:"+joga1+":2:"+joga2+"::"+boolJoga+"::"+joga1Duration+"::"+joga2Duration);
                    boolean boolKharaBara = isGoodBara(dataObj, bara, tithi1, tithi2, 0);
                    boolean boolDagdaBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 1);
                    boolean boolBisaBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 2);
                    boolean boolHutasanBara = false;//isGoodBara(dataObj, bara, tithi1, tithi2, 3);
                  //  boolean boolTithi = isGoodTithi(dataObj, bara, tithi1, tithi2, tithi1Duration, tithi2Duration);

                    boolean boolBara = (!boolKharaBara && !boolDagdaBara && !boolBisaBara && !boolHutasanBara);
                    boolean boolNakshetra = ((dataObj.getNakshetra().contains("|" + nakshetra1 + "|") && nakshetra1Duration>minTimeDuration) || (dataObj.getNakshetra().contains("|" + nakshetra2 + "|") && nakshetra2Duration>minTimeDuration));

                   // boolean boolNakshetra2 = isGoodNakshetra(dataObj, bara, nakshetra1, nakshetra2, nakshetra1Duration, nakshetra2Duration);
                    //  Log.e("boolBara",":boolBara:"+boolBara+":boolTithi:"+boolTithi+":boolJoga:"+boolJoga+":boolKarana:"+boolKarana+":boolNakshetra:"+boolNakshetra);
                    // isSankranthi,trihasparsa;


                    dataObj.isTithi = boolTithi;
                    dataObj.isMasa=boolMasa;
                    dataObj.isKharaBara = boolKharaBara;
                    dataObj.isDagdaBara = boolDagdaBara;
                    dataObj.isBisaBara = boolBisaBara;
                    dataObj.isHutasanBara = boolHutasanBara;
                    dataObj.isKarana = boolKarana;
                    dataObj.isJoga = boolJoga;
                    dataObj.isNakshatra = boolNakshetra;
                    dataObj.isSankranti = isSankranthi;
                    dataObj.isTrihaspada = trihasparsa;
                    dataObj.isMasant=masant;

                    boolean commonRule = (boolMasa && boolPakshya
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

                    Log.e("goodVal", dataObj.isTodayAusp() + ":" + commonRule + ":" + strictRule + "::" + date1 + "::" + dataObj.getName() + "::" + "goodNaksVal::" + goodVal + ":" + boolMasa + ":" + boolPakshya + ":" + boolBara + ":" + boolTithi + ":" + boolNakshetra + ":" + boolJoga + ":" + boolKarana);


                    vivahList.add(dataObj);
                   // System.out.println("isSankranthi::"+vivahListIndex+":"+vivahList.size());
                   // if(isSankranthi){
                    //    AuspData dataObj1 = vivahList.get(vivahListIndex - 1);
                    //    dataObj1.setTodayAusp(false);
                    //    dataObj1.isMasant=true;
                    //    vivahList.set(vivahListIndex - 1,dataObj1);
                   // }
                  //  vivahListIndex++;
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

    private double getDuration(Calendar cal1, Calendar cal2,Calendar sunRise) {
        if (cal1 == null || cal2 == null)
            return 0;
        long nextDaySR = sunRise.getTimeInMillis() + 24 * 60 * 60 * 1000;
        if(cal1.getTimeInMillis()>nextDaySR)
            return -1;//next days
        return ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60.0));
    }

    private boolean isGoodNakshetra(AuspData dataObj, int bara, int nakshetra1, int nakshetra2, double nakshetra1Duration, double nakshetra2Duration) {
        boolean isNakshetraGood1, isNakshetraGood2, isNakshetraGood3, isNakshetraGood4;
        switch (bara) {
            case 1:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 4 || nakshetra1 == 12 || nakshetra1 == 21 || nakshetra1 == 26));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 4 || nakshetra2 == 12 || nakshetra2 == 21 || nakshetra2 == 26));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 2);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 2);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);

            case 2:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 7 || nakshetra1 == 15 || nakshetra1 == 22 || nakshetra1 == 23 || nakshetra1 == 24));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 7 || nakshetra2 == 15 || nakshetra2 == 22 || nakshetra2 == 23 || nakshetra2 == 24));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 14);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 14);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);

            case 3:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 2 || nakshetra1 == 10 || nakshetra1 == 11 || nakshetra1 == 20 || nakshetra1 == 25));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 2 || nakshetra2 == 10 || nakshetra2 == 11 || nakshetra2 == 20 || nakshetra2 == 25));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 21);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 21);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);

            case 4:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 3 || nakshetra1 == 16));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 3 || nakshetra2 == 16));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 23);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 23);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);
            case 5:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 1 || nakshetra1 == 8 || nakshetra1 == 13));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 1 || nakshetra2 == 8 || nakshetra2 == 13));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 12);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 12);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);

            case 6:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 5 || nakshetra1 == 14 || nakshetra1 == 17 || nakshetra1 == 27));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra1 == 5 || nakshetra1 == 14 || nakshetra1 == 17 || nakshetra1 == 27));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 18);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 18);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);

            case 7:
                isNakshetraGood1 = (nakshetra1Duration > 4 && (nakshetra1 == 6 || nakshetra1 == 9 || nakshetra1 == 18 || nakshetra1 == 19));
                isNakshetraGood2 = (nakshetra2Duration > 4 && (nakshetra2 == 6 || nakshetra2 == 9 || nakshetra2 == 18 || nakshetra2 == 19));
                isNakshetraGood3 = (nakshetra1Duration > 4 && nakshetra1 == 27);
                isNakshetraGood4 = (nakshetra2Duration > 4 && nakshetra2 == 27);
                return (isNakshetraGood1 || isNakshetraGood2 || isNakshetraGood3 || isNakshetraGood4);


        }



      /*  ସ୍ଥିର: ରୋହିଣୀ, ଉ:ଫାଲ୍ଗୁନି,ଉତ୍ତରାଷାଢ଼ା ଓ ଉ:ଭାଦ୍ରପଦ (ରବିବାର)
        ଚର: ପୁନର୍ବସୁ, ଶ୍ବାତୀ, ଶ୍ରବଣା, ଧନିଷ୍ଠା ଓ    ଶତଭିଷା (ସୋମବାର)
                ଉଗ୍ର: ଦ୍ବିଜା, ମଘା ଓ ୩ ପୂର୍ବା (ମଙ୍ଗଳବାର)
        ମିଶ୍ର: କୃତ୍ତିକା, ବିଶାଖା (ବୁଧବାର )
     କ୍ଷୀପ୍ର: ଅଶ୍ବିନୀ, ପୂଷ୍ୟା, ହସ୍ତା (ଗୁରୁବାର)
     ମୃଦୁ: ମୃଗଶୀରା, ଚିତ୍ରା, ଅନୁରାଧା, ରେବତୀ(ଶୁକ୍ରବାର)
    ତୀକ୍ଷ୍ଣ : ଆର୍ଦ୍ରା, ଅଶ୍ଳେଷା, ଜ୍ୟେଷ୍ଠା, ମୂଳା (ଶନିବାର)

        ବିଭାଗିକୃତ ନକ୍ଷତ୍ର ଗୁଡ଼ିକ ବନ୍ଧନୀ ମଧ୍ୟସ୍ଥ ବାର ଗୁଡ଼ିକରେ ଶୁଭଦାୟକ ଅଟନ୍ତି।

        ଏହି ନକ୍ଷତ୍ର ଗୁଡ଼ିକ ଶୁଭକାର୍ଯ୍ୟ ପାଇଁ ଅନୁପଯୁକ୍ତ।
        କିନ୍ତୁ ଯଦି ଉକ୍ତ ନକ୍ଷତ୍ର ଗୁଡ଼ିକ ନିମ୍ନ ପ୍ରଦତ୍ତ ବାରରେ ଭୋଗ ହେଉଥାଆନ୍ତି ତେବେ ଶୁଭକାର୍ଯ୍ୟ ପାଇଁ ବିଚାରକୁ ନିଆଯାଆନ୍ତି।
        ଦ୍ବିଜା- ରବିବାର, ଚିତ୍ରା- ସୋମବାର, ଉ:ଷାଢ଼ା- ମଙ୍ଗଳବାର, ଧନିଷ୍ଠା- ବୁଧବାର, ଉ:ଫାଲ୍ଗୁନୀ- ଗୁରୁବାର, ଜ୍ଯେଷ୍ଠା- ଶୁକ୍ରବାର, ରେବତୀ- ଶନିବାର।

        */
        return false;

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
                && tithi1Duration > 4) ||
                ((tithi2 == 2 || tithi2 == 3 || tithi2 == 5 || tithi2 == 7 || tithi2 == 11 || tithi2 == 13 || tithi2 == 1 || tithi2 == 6 || tithi2 == 8 || tithi2 == 10)
                        && tithi2Duration > 4));

        if (dataObj.getMangalikaType() !=3) {
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
        return (karana1 > 1 && karana1 < 8 && karana1Duration > minTimeDuration  ) || (karana2 > 1 && karana2 < 8 && karana2Duration > minTimeDuration  ) || (karana3 > 1 && karana3 < 8 && karana3Duration > minTimeDuration )  || (karana4 > 1 && karana4 < 8 && karana4Duration > minTimeDuration);
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
                } else {
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
            Log.e("tableDataObject", "::tableDataObject:::1:" + tableDataObject.get(0).valueList1.length);

            rowViewTable = new AuspTableRowLayout(mContext, header, columns, ausp_work_date, ausp_work_yes_no, tableDataObject);
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
