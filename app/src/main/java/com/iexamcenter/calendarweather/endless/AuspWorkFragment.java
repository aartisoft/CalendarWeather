package com.iexamcenter.calendarweather.endless;

import android.annotation.SuppressLint;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AuspWorkFragment extends Fragment {
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
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
public void getAppResourse(){
     if (!CalendarWeatherApp.isPanchangEng) {
        // lrashiList = mContext.getResources().getStringArray(R.array.l_rasi_kundali_arr);
         rashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
         lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
         l_ausp_work = res.getStringArray(R.array.l_arr_ausp_work);
         ausp_work_yes_no = res.getStringArray(R.array.l_arr_ausp_work_yes_no);
         ausp_work_date = res.getString(R.string.l_ausp_work_date);
         usLocale = Locale.US;
     }else{
       //  lrashiList = mContext.getResources().getStringArray(R.array.rasi_kundali_arr);
         rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
         lplanetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
         l_ausp_work = res.getStringArray(R.array.e_arr_ausp_work);
         ausp_work_yes_no = res.getStringArray(R.array.e_arr_ausp_work_yes_no);
         ausp_work_date = res.getString(R.string.e_ausp_work_date);
         usLocale = Locale.US;
     }
}
    public static Fragment newInstance() {
        return new AuspWorkFragment();
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
        getAppResourse();
        /* lrashiList = mContext.getResources().getStringArray(R.array.l_rasi_kundali_arr);
        rashiList = mContext.getResources().getStringArray(R.array.rasi_kundali_arr);
        erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

        eplanetList = mContext.getResources().getStringArray(R.array.e_planet_arr);
        lplanetList = mContext.getResources().getStringArray(R.array.l_planet_arr);


        if (!CalendarWeatherApp.isPanchangEng) {
            l_ausp_work = res.getStringArray(R.array.l_ausp_work);
            ausp_work_date = res.getString(R.string.ausp_work_date);
            ausp_work_yes_no = res.getStringArray(R.array.ausp_work_yes_no);
            usLocale = new Locale.Builder().setLanguage(mPref.getMyLanguage()).build();
        } else {
            Configuration conf = res.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            res.updateConfiguration(conf, null); // second arg null means don't change
            l_ausp_work = res.getStringArray(R.array.l_ausp_work);
            ausp_work_date = res.getString(R.string.ausp_work_date);
            ausp_work_yes_no = res.getStringArray(R.array.ausp_work_yes_no);
            usLocale = Locale.US;
            conf.locale = savedLocale;
            res.updateConfiguration(conf, null);

        }*/

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
                boolean masant = false;
                if (sunsignEnd > myCoreData.getMidNight().getTimeInMillis() && (sunsignEnd < myCoreData.getMidNight().getTimeInMillis() + (24 * 60 * 60 * 1000l))) {

                    masant = true;

                }

                int paksha = myCoreData.getPaksha() + 1;

                int chandraMasa = myCoreData.getLunarMonthPurnimantIndex() + 1;
                int bara = myCoreData.getDayOfWeek();


                int tithi1 = myCoreData.getTithi().currVal < 15 ? myCoreData.getTithi().currVal : (myCoreData.getTithi().currVal - 15);
                int tithi2 = myCoreData.getTithi().nextVal < 15 ? myCoreData.getTithi().nextVal : (myCoreData.getTithi().nextVal - 15);

                int nakshetra1 = myCoreData.getNakshetra().currVal;
                 int nakshetra2 = myCoreData.getNakshetra().nextVal;

                boolean isSankranthi = myCoreData.getSolarDayVal() == 1;
                boolean isPurnima = myCoreData.getTithi().currVal == 15;
                boolean isAmabasya = myCoreData.getTithi().currVal == 30;


                boolean trihasparsa = (myCoreData.getTithi().currVal != 0 && myCoreData.getTithi().nextVal != 0 && myCoreData.getTithi().nextToNextVal != 0);
                ArrayList<AuspData> vivahList = new ArrayList<>();

try{


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

                        Collections.sort(allGoodTime, new AuspComparator());

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
                            ArrayList<String> goodTimes=new ArrayList<>();
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
                                goodTimes.add(minVal+":"+maxVal);


                            }
                            dataObj.setGoodTimes(goodTimes);
                        } catch (Exception e) {
                            Log.e("goodTimeTithi", "::goodTime:MINMAX:5::" + e.getMessage());
                            e.printStackTrace();
                        }
                 //   }


                   /* if(goodTimeTithi.size()==0){
                        dataObj.isTithi =boolTithi= false;
                        dataObj.isKarana =boolKarana= false;
                        dataObj.isJoga =boolJoga= false;
                        dataObj.isNakshatra =boolNakshetra= false;
                    }else {*/
                        dataObj.isTithi = boolTithi;
                        dataObj.isKarana = boolKarana;
                        dataObj.isJoga = boolJoga;
                        dataObj.isNakshatra = boolNakshetra;
                   // }
                    dataObj.setName(dataObj.getName());
                    dataObj.isMasa = boolMasa;
                    dataObj.isKharaBara = boolKharaBara;
                    dataObj.isDagdaBara = boolDagdaBara;
                    dataObj.isBisaBara = boolBisaBara;
                    dataObj.isHutasanBara =  boolHutasanBara;

                    dataObj.isSankranti = isSankranthi;
                    dataObj.isTrihaspada =  trihasparsa;
                    dataObj.isMasant =masant;


                    boolean commonRule = (boolMasa && boolPakshya && (goodTimeTithi.size()>0)
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
                            dataObj.setGoodTimes(new ArrayList<>());
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
                    // System.out.println("isSankranthi::"+vivahListIndex+":"+vivahList.size());
                    // if(isSankranthi){
                    //    AuspData dataObj1 = vivahList.get(vivahListIndex - 1);
                    //    dataObj1.setTodayAusp(false);
                    //    dataObj1.isMasant=true;
                    //    vivahList.set(vivahListIndex - 1,dataObj1);
                    // }
                    //  vivahListIndex++;
                }

}catch (Exception e){
    e.printStackTrace();
    Log.e("XXXX","XXXXX"+e.getMessage());
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

    class AuspComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            return new BigDecimal(o1).compareTo(new BigDecimal(o2));
        }

    }

    private double getDuration(Calendar cal1, Calendar cal2, Calendar sunRise) {
        if (cal1 == null || cal2 == null)
            return 0;
        long nextDaySR = sunRise.getTimeInMillis() + 24 * 60 * 60 * 1000;
        if (cal1.getTimeInMillis() > nextDaySR)
            return -1;//next days
        return ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60.0));
    }

    private ArrayList<String> getGoodTime(Calendar sunriseTime, Calendar startTime, Calendar endTime) {


        ArrayList<String> al = new ArrayList<String>();
        try {
            Calendar nextDaySunrise = Calendar.getInstance();
            nextDaySunrise.setTimeInMillis(sunriseTime.getTimeInMillis() + (24 * 60 * 60 * 1000));
            if (endTime!=null && endTime.getTimeInMillis() > nextDaySunrise.getTimeInMillis()) {
                endTime = nextDaySunrise;
            }else if(endTime==null){
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
        }catch (Exception e){
            Log.e("XXXX","XXXXX:1:"+e.getMessage());
        }
        return al;
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


        if (settingStr.contains("|" + currVal + "|")  ) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), myCoreData.getSunRiseCal(), mainObj.currValEndTime));

        }
        if (settingStr.contains("|" + nextVal + "|") ) {
            al.addAll(getGoodTime(myCoreData.getSunRiseCal(), mainObj.currValEndTime, mainObj.le_nextValEndTime));

        }
        if (settingStr.contains("|" + nextNextVal + "|")  ) {
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
        String latLng = remDeg + "° " + rashiList[index] + " " + min + "′" + " " + sec + "′′";
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
