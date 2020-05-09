package com.iexamcenter.calendarweather.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.dao.EphemerisDao;
import com.iexamcenter.calendarweather.data.local.dao.NewsDao;
import com.iexamcenter.calendarweather.data.local.dao.RssDao;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;

import java.util.List;


public class AppRepository {
    static MutableLiveData<List<EphemerisEntity>> ephemerisData;// = new MutableLiveData<>();


    private static final Object LOCK = new Object();
    private static AppRepository sInstance;
    private boolean mInitialized = false;
    private Context mContext;
    private EphemerisDao ephemerisDao;
    private NewsDao newsDao;
    private RssDao rssDao;
    private static MutableLiveData<List<EphemerisEntity>> mYearlyPlanetInfo;
    private static MutableLiveData<String> mCurrLang = new MutableLiveData<>();
    private static MutableLiveData<String> mPageSubpage = new MutableLiveData<>();
    private static MutableLiveData<Boolean> mLocationChanged = new MutableLiveData<>();
    private static MutableLiveData<Boolean> mIsEngChanged = new MutableLiveData<>();

    private static MutableLiveData<String> mBirthPlace = new MutableLiveData<>();
    private static MutableLiveData<String> mDateTimePicker = new MutableLiveData<>();

    private static MutableLiveData<String> mBirthPlace1 = new MutableLiveData<>();
    private static MutableLiveData<String> mDateTimePicker1 = new MutableLiveData<>();
    private static MutableLiveData<String> mBirthPlace2 = new MutableLiveData<>();
    private static MutableLiveData<String> mDateTimePicker2 = new MutableLiveData<>();

    private static String mDatePicker = "";
    private static String mTimePicker = "";
    private static String mDatePicker1 = "";
    private static String mTimePicker1 = "";
    private static String mDatePicker2 = "";
    private static String mTimePicker2 = "";

    public synchronized static AppRepository getInstance(Context ctx) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppRepository(ctx);
                ephemerisData = new MutableLiveData<>();
                mYearlyPlanetInfo = new MutableLiveData<>();
            }
        }
        return sInstance;
    }


    private AppRepository(Context ctx) {
        mContext = ctx;


        ephemerisDao = AppDatabase.getInstance(ctx).ephemerisDao();
        newsDao = AppDatabase.getInstance(ctx).newsDao();
        rssDao = AppDatabase.getInstance(ctx).rssDao();

    }


    public String getDatePicker1() {
        return mDatePicker1;
    }

    public void setDatePicker1(String picker) {
        mDatePicker1 = picker;
    }

    public String getTimePicker1() {
        return mTimePicker1;
    }

    public void setTimePicker1(String picker) {
        mTimePicker1 = picker;
    }

    public String getDatePicker2() {
        return mDatePicker2;
    }

    public void setDatePicker2(String picker) {
        mDatePicker2 = picker;
    }

    public String getTimePicker2() {
        return mTimePicker2;
    }

    public void setTimePicker2(String picker) {
        mTimePicker2 = picker;
    }

    public MutableLiveData<String> getDateTimePicker1() {
        return mDateTimePicker1;
    }

    public void setDateTimePicker1(String picker) {
        mDateTimePicker1.postValue(picker);
    }


    public void setBirthPlace1(String picker) {
        mBirthPlace1.postValue(picker);
    }

    public MutableLiveData<String> getBirthPlace1() {
        return mBirthPlace1;
    }


    public MutableLiveData<String> getDateTimePicker2() {
        return mDateTimePicker2;
    }

    public void setDateTimePicker2(String picker) {
        mDateTimePicker2.postValue(picker);
    }


    public void setBirthPlace2(String picker) {
        mBirthPlace2.postValue(picker);
    }

    public MutableLiveData<String> getBirthPlace2() {
        return mBirthPlace2;
    }


    public LiveData<List<EphemerisEntity>> getKundaliMatchData(long curr1, long curr2) {

        long start1 = curr1 - 1 * 32 * 24 * 60 * 60 * 1000L;
        long end1 = curr1 + 1 * 1 * 24 * 60 * 60 * 1000L;
        long start2 = curr2 - 1 * 32 * 24 * 60 * 60 * 1000L;
        long end2 = curr2 + 1 * 1 * 24 * 60 * 60 * 1000L;

        return ephemerisDao.getAll(start1, end1, start2, end2);

        // return ephemerisDao.getPlanetInfo(selYear1, selMonth1, selDate1,selYear2, selMonth2, selDate2);


    }


    public LiveData<List<EphemerisEntity>> getEphemerisData(long curr, int type) {
        if (type == -1) {
            long start = curr - 1 * 24 * 60 * 60 * 1000L;
            long end = curr + 2 * 24 * 60 * 60 * 1000L;
            return ephemerisDao.getAll(start, end);
        } else if (type == 1) {
            long start = curr - 1 * 32 * 24 * 60 * 60 * 1000L;
            long end = curr + 1 * 40 * 24 * 60 * 60 * 1000L;
            return ephemerisDao.getAll(start, end);
        } else if (type == 2) {
            long start = curr - 1 * 32 * 24 * 60 * 60 * 1000L;
            long end = curr + 1 * (40+365) * 24 * 60 * 60 * 1000L;
            return ephemerisDao.getAll(start, end);
        } else if (type == 3) {
            long start = curr - 1 * 60 * 24 * 60 * 60 * 1000L;
            long end = curr + 1 * 60 * 24 * 60 * 60 * 1000L;
            return ephemerisDao.getAll(start, end);
        }else {
            long start = curr - 1 * 32 * 24 * 60 * 60 * 1000L;
            long end = curr + 1 * 1 * 24 * 60 * 60 * 1000L;

            return ephemerisDao.getAll(start, end);
        }


    }

    public LiveData<EphemerisEntity> getPlanetInfo(int day, int month, int year) {


        return ephemerisDao.getPlanetInfo(day, month, year);
    }

    public void isLocationChanged(Boolean chng) {
        mLocationChanged.postValue(chng);
    }

    public MutableLiveData<Boolean> getLocationChanged() {
        return mLocationChanged;
    }




    public void isEngChanged(Boolean chng) {
        mIsEngChanged.postValue(chng);
    }

    public MutableLiveData<Boolean> getEngChanged() {
        return mIsEngChanged;
    }



    public void setPageSubpage(String pageSubpage) {
        mPageSubpage.postValue(pageSubpage);
    }

    public MutableLiveData<String> getPageSubpage() {
        return mPageSubpage;
    }


    public void setCurrLang(String lang) {
        mCurrLang.postValue(lang);
    }

    public String getDatePicker() {
        return mDatePicker;
    }

    public void setDatePicker(String picker) {
        mDatePicker = picker;
    }

    public String getTimePicker() {
        return mTimePicker;
    }

    public void setTimePicker(String picker) {
        mTimePicker = picker;
    }

    public MutableLiveData<String> getDateTimePicker() {
        return mDateTimePicker;
    }

    public void setDateTimePicker(String picker) {
        mDateTimePicker.postValue(picker);
    }


    public void setBirthPlace(String picker) {
        mBirthPlace.postValue(picker);
    }

    public MutableLiveData<String> getBirthPlace() {
        return mBirthPlace;
    }

    public MutableLiveData<String> getCurrLang() {
        return mCurrLang;
    }

    public void setYearlyPlanetInfo(int year) {
        Log.e("YearlyPlanetInfo", year + "YearlyPlanetInfo" + year);
        // LiveData<List<EphemerisEntity>> data = ephemerisDao.setYearlyPlanetInfo(year);

        //  mYearlyPlanetInfo.setValue();
        // mYearlyPlanetInfo=ephemerisDao.setYearlyPlanetInfo(year);
    }

    public LiveData<List<EphemerisEntity>> getYearlyPlanetInfo(int year) {
        return ephemerisDao.setYearlyPlanetInfo(year);
    }

    public LiveData<List<NewsEntity>> getNewsList(String key1, int type, int rowId, String lang) {

        String key = "%" + key1 + "%";
        //return newsDao.getCatWiseLocalEnNews("%"+key+"%",""+type,lang);
        if (type >= 100) {
            lang = getLang("" + type);
            Log.e("getNewsList", "getNewsList::" + key + ":type:" + type + ":rowId:" + rowId + ":lang:" + lang);

            return newsDao.getLocalNews(key, lang);
        } else if (type == 5)
            return newsDao.getCatTypeLocalHiEnNews(key, "2", lang);
        else if (type == 4)
            return newsDao.getNewsById(rowId);
        else if (type == 3)
            return newsDao.getTitleWiseLocalNews(key, lang);
        else if (type == 2)
            return newsDao.getLocalEnNews(key, lang);
        else if (type == 1)
            return newsDao.getCatWiseLocalHiEnNews(key, lang);
        else if (type == 0)
            return newsDao.getCatWiseLocalNews(key, lang);


        return newsDao.getCatWiseLocalEnNews();
    }

    private String getLang(String key) {

        switch (key) {
            case "100":
                return "en";
            case "101":
                return "hi";
            case "102":
                return "bn";
            case "103":
                return "gu";
            case "104":
                return "kn";
            case "105":
                return "ml";
            case "106":
                return "mr";
            case "107":
                return "or";
            case "108":
                return "pa";
            case "109":
                return "ta";
            case "110":
                return "te";
            default:
                return "en";

        }

    }

    public LiveData<List<RssEntity>> getAllRss(String myLanguage) {
        return rssDao.getAllRss(myLanguage);

    }
}
