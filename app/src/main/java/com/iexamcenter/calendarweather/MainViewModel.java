package com.iexamcenter.calendarweather;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.iexamcenter.calendarweather.data.AppRepository;
import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    LiveData<List<EphemerisEntity>> mPunchangDataResult;

    Application mContext;

    public MainViewModel(Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application);
        mContext = application;
    }

    public  String getDatePicker1(){
        return mRepository.getDatePicker1();
    }

    public void setDatePicker1(String picker) {
        mRepository.setDatePicker1(picker);
    }



    public  String getTimePicker1(){
        return mRepository.getTimePicker1();
    }

    public void setTimePicker1(String picker) {
        mRepository.setTimePicker1(picker);
    }


    public  String getDatePicker2(){
        return mRepository.getDatePicker2();
    }

    public void setDatePicker2(String picker) {
        mRepository.setDatePicker2(picker);
    }



    public  String getTimePicker2(){
        return mRepository.getTimePicker2();
    }

    public void setTimePicker2(String picker) {
        mRepository.setTimePicker2(picker);
    }

    public MutableLiveData<String> getBirthPlace1(){
        return mRepository.getBirthPlace1();
    }

    public void setBirthPlace1(String picker) {
        mRepository.setBirthPlace1(picker);
    }

    public MutableLiveData<String> getDateTimePicker1(){
        return mRepository.getDateTimePicker1();
    }

    public void setDateTimePicker1(String picker) {
        mRepository.setDateTimePicker1(picker);
    }



    public MutableLiveData<String> getBirthPlace2(){
        return mRepository.getBirthPlace2();
    }

    public void setBirthPlace2(String picker) {
        mRepository.setBirthPlace2(picker);
    }

    public MutableLiveData<String> getDateTimePicker2(){
        return mRepository.getDateTimePicker2();
    }

    public void setDateTimePicker2(String picker) {
        mRepository.setDateTimePicker2(picker);
    }



    public MutableLiveData<String> getBirthPlace(){
        return mRepository.getBirthPlace();
    }

    public void setBirthPlace(String picker) {
        mRepository.setBirthPlace(picker);
    }

    public MutableLiveData<String> getDateTimePicker(){
        return mRepository.getDateTimePicker();
    }

    public void setDateTimePicker(String picker) {
        mRepository.setDateTimePicker(picker);
    }


    public  String getDatePicker(){
        return mRepository.getDatePicker();
    }

    public void setDatePicker(String picker) {
        mRepository.setDatePicker(picker);
    }



    public  String getTimePicker(){
        return mRepository.getTimePicker();
    }

    public void setTimePicker(String picker) {
        mRepository.setTimePicker(picker);
    }





    public void setCurrLang(String lang) {
        mRepository.setCurrLang(lang);
    }
    public MutableLiveData<String> getCurrLang(){
        return mRepository.getCurrLang();
    }



    public void isLocationChanged(Boolean chng) {
        mRepository.isLocationChanged(chng);
    }
    public MutableLiveData<Boolean> getLocationChanged(){
        return mRepository.getLocationChanged();
    }


    public void isEngChanged(Boolean chng) {
        mRepository.isEngChanged(chng);
    }
    public MutableLiveData<Boolean> getEngChanged(){
        return mRepository.getEngChanged();
    }

    public void setPageSubpage(String pageSubpage) {
        mRepository.setPageSubpage(pageSubpage);
    }
    public MutableLiveData<String> getPageSubpage(){
        return mRepository.getPageSubpage();
    }


    public LiveData<List<EphemerisEntity>> getEphemerisData(long curr,int type) {

        return mRepository.getEphemerisData(curr,type);
    }
    public LiveData<List<EphemerisEntity>> getKundaliMatchData(long curr1,long curr2) {

        return mRepository.getKundaliMatchData(curr1,curr2);
    }


    public LiveData<EphemerisEntity> getPlanetInfo(int day, int month, int year) {
        return mRepository.getPlanetInfo(day, month, year);
    }
    public void setYearlyPlanetInfo(int year) {
        Log.e("YearlyPlanetInfo","YearlyPlanetInfo"+year);
         mRepository.setYearlyPlanetInfo(year);
    }
    public LiveData<List<EphemerisEntity>> getYearlyPlanetInfo(int year) {
        return mRepository.getYearlyPlanetInfo( year);
    }
    public LiveData<List<NewsEntity>> getNewsList(String key, int type, int rowId, String lang) {

        return mRepository.getNewsList(key, type, rowId, lang);
    }

    public void startWorkmanager() {
       /* Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();*/
        Data.Builder data = new Data.Builder();
        data.putString("fileNameTimeStamp", "");
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data.build())
                .build();

       // WorkManager.getInstance(mContext).enqueueUniqueWork("MY_WORK_MANAGER", ExistingWorkPolicy.REPLACE, myWorkRequest);


        WorkManager.getInstance(mContext).enqueue(myWorkRequest);
    }

    public LiveData<List<RssEntity>> getAllRss(String myLanguage) {
        return mRepository.getAllRss(myLanguage);

    }
}