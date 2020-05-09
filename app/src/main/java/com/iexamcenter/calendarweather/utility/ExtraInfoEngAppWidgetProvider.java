package com.iexamcenter.calendarweather.utility;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.dao.EphemerisDao;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.mydata.FestivalData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.panchang.PanchangUtilityWidget;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.WeatherResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by sasikanta on 4/26/2017.
 */

public class ExtraInfoEngAppWidgetProvider extends AppWidgetProvider {
    private Context mContext;

    String time_next, time_to;
    Resources res;
    PrefManager mPref;
    RemoteViews views;
    // AppWidgetManager mAppWidgetManager;
    int mAppWidgetId;
    // private Context context;
    AppWidgetManager appWidgetManager;
    int[] appWidgetIds;
    HashMap<String, CoreDataHelper> mPanchangHashMap;

    HashMap<String, CoreDataHelper> myPanchangHashMap;
    int maxDays, start, month;
    String currLang;
    //private Context context;
    int currMonthInt, currYearInt, currDayInt, weekDayInt, dayOfCurrMonth;

    private String JSONFILE = "ONTHISDAYWEATHER.txt";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        mContext = context;
        mPref = PrefManager.getInstance(context);
        mPref.load();
        currLang = mPref.getMyLanguage();
        updateRes();
        Calendar cal = Calendar.getInstance();
        currYearInt = cal.get(Calendar.YEAR);
        currMonthInt = cal.get(Calendar.MONTH);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);
        weekDayInt = cal.get(Calendar.DAY_OF_WEEK);
        views = new RemoteViews(context.getPackageName(), R.layout.extrainfoappwidgetodia);


        this.appWidgetManager = AppWidgetManager.getInstance(context);
        if (views != null) {
            onCreateLoader(views);
            this.appWidgetManager.updateAppWidget(
                    new ComponentName(context, ExtraInfoEngAppWidgetProvider.class), views);
        }
    }

    private void updateRes() {
        String language = mPref.getMyLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = mContext.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        //  configuration.setLayoutDirection(locale);


        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // stop alarm only if all widgets have been disabled
        this.appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = this.appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.length == 0) {
            // stop alarm
            EnglishAppWidgetAlarm appWidgetAlarm = new EnglishAppWidgetAlarm(context.getApplicationContext());
            appWidgetAlarm.stopAlarm();
        }

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager1, int[] appWidgetIds) {
        try {

            mContext = context;
            mPref = PrefManager.getInstance(context);
            mPref.load();
            updateRes();
            this.appWidgetManager = appWidgetManager1;
            this.appWidgetIds = appWidgetIds;
            views = new RemoteViews(context.getPackageName(), R.layout.extrainfoappwidgetodia);

            final int N = appWidgetIds.length;
            Calendar cal = Calendar.getInstance();
            currYearInt = cal.get(Calendar.YEAR);
            currMonthInt = cal.get(Calendar.MONTH);
            currDayInt = cal.get(Calendar.DAY_OF_MONTH);
            weekDayInt = cal.get(Calendar.DAY_OF_WEEK);
            for (int index = 0; index < N; index++) {
                int appWidgetId = appWidgetIds[index];
                mAppWidgetId = appWidgetId;


                if (views != null) {
                    onCreateLoader(views);
                    this.appWidgetManager.updateAppWidget(appWidgetId, views);


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);


    }

    public void setWeatherView() {
        if (!mPref.getWidgetTemp().isEmpty()) {
            String tmpdata = "";
            String main = mPref.getWidgetTemp();
            if (mPref.getMyLanguage().contains("or")) {

                if (main.contains(".")) {
                    String[] tmpArr = main.split(Pattern.quote("."));
                    tmpdata = tmpArr[0] + "." + tmpArr[1];
                    tmpdata = tmpdata + "\u00b0" + "C";
                } else {

                    tmpdata = main;
                    tmpdata = tmpdata + "\u00b0" + "C";
                }
            } else {
                tmpdata = main;
                tmpdata = tmpdata + "\u00b0" + "C";
            }

            views.setImageViewResource(R.id.img, Integer.parseInt(mPref.getWidgetTempImg()));
            views.setTextViewText(R.id.update_date, getUpdatedTime(mPref.getLastWeatherUpdatedTime()));
            views.setTextViewText(R.id.temp, tmpdata);
            getWeatherDesc(mPref.getWidgetTempDesc(), views);
            views.setViewVisibility(R.id.weather, View.VISIBLE);
        }
    }

    public void weatherData() {
        String json = "{'setup':'x'}";
        loadData(json, Constant.WEATHER_API);


    }

    private void loadData(String profileJson, int api) {
        String lat = mPref.getLatitude();//"86.52";
        String lon = mPref.getLongitude();//"21.05";
        String cityId = mPref.getWeatherCityId();

        switch (api) {
            case Constant.WEATHER_API:
                try {

                    profileJson = "{'lat':'" + lat + "','lon':'" + lon + "','cityId':'" + cityId + "'}";

                    HttpRequestObject mReqobject = new HttpRequestObject(mContext);

                    JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
                    System.out.println("WEATHER:-:" + jsonHeader.toString());
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

                    ApiUtil.getIExamCenterBaseURLClass().getWeatherReport(body).enqueue(new Callback<WeatherResponse>() {


                        @Override
                        public void onResponse
                                (Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                            if (response.isSuccessful()) {
                                handleResponse(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {

                            loadData("{}", Constant.SETUP_WEATHER_API);

                           // Log.d("xxx", "error loading from API");
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.SETUP_WEATHER_API:


                ArrayList weatherToken = new ArrayList<>();
                weatherToken.add("471784d022cb30c32629d494c3e736a6");
                weatherToken.add("e4d381aa70aed6da3de60172c131fb96");
                weatherToken.add("7bb68f0a85dabd0b82efc884eae94c4b");
                weatherToken.add("fbde279ae40290f2dc4dcb1d1d6913ba");
                weatherToken.add("75f210cfeb6baa9068bc3f2afead2048");
                weatherToken.add("428dd54d6bfcd0e28594429c5ce22610");
                weatherToken.add("7dad33565a0e832b1eed22f40e1a26fa");
                weatherToken.add("ee7377c1c2ac84852b5feb047df695c6");
                weatherToken.add("8ba72279963f9baba786e706becf3b9f");
                weatherToken.add(mPref.getWhetherToken());


                Random r1 = new Random();
                int Low1 = 0;
                int High1 = weatherToken.size();
                int Result1 = r1.nextInt(High1 - Low1) + Low1;
                String weatherTokenStr = (String) weatherToken.get(Result1);


                ApiUtil.getWeatherBaseURLClass().getWeatherReport(lat, lon, weatherTokenStr, "metric").enqueue(new Callback<WeatherResponse>() {


                    @Override
                    public void onResponse
                            (Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                        if (response.isSuccessful()) {
                            handleResponse(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        t.printStackTrace();

                    }

                });

                break;

        }
    }

    private void handleResponse(WeatherResponse res) {


        updateRes();


        if (res.getCod() == 200) {
            Gson gson = new Gson();
            String json = gson.toJson(res);

            String cityName = res.getName();
            int date = res.getDt();
            mPref.setLastWeatherUpdatedTime(date);
            String main = res.getMain().getTemp();

            String icon = res.getWeather().get(0).getIcon();
            // String desc = res.getWeather().get(0).getDescription();


            int wImg = R.drawable.ic_01d;
            if (icon.contains("01d")) {
                wImg = R.drawable.ic_01d;
            } else if (icon.contains("01n")) {
                wImg = R.drawable.ic_01n;
            } else if (icon.contains("02d")) {
                wImg = R.drawable.ic_02d;
            } else if (icon.contains("02n")) {
                wImg = R.drawable.ic_02n;
            } else if (icon.contains("03d")) {
                wImg = R.drawable.ic_03d;
            } else if (icon.contains("03n")) {
                wImg = R.drawable.ic_03n;
            } else if (icon.contains("04d")) {
                wImg = R.drawable.ic_04d;
            } else if (icon.contains("04n")) {
                wImg = R.drawable.ic_04n;
            } else if (icon.contains("09d")) {
                wImg = R.drawable.ic_09d;
            } else if (icon.contains("09n")) {
                wImg = R.drawable.ic_09n;
            } else if (icon.contains("10d")) {
                wImg = R.drawable.ic_10d;
            } else if (icon.contains("10n")) {
                wImg = R.drawable.ic_10n;
            } else if (icon.contains("11d")) {
                wImg = R.drawable.ic_11d;
            } else if (icon.contains("11n")) {
                wImg = R.drawable.ic_11n;
            } else if (icon.contains("50d")) {
                wImg = R.drawable.ic_50d;
            } else if (icon.contains("50n")) {
                wImg = R.drawable.ic_50n;
            } else if (icon.contains("13d")) {
                wImg = R.drawable.ic_13d;
            } else if (icon.contains("13n")) {
                wImg = R.drawable.ic_13n;
            }


            views.setImageViewResource(R.id.img, wImg);

            views.setTextViewText(R.id.update_date, getUpdatedTime(date));

            String tmpdata = main + "\u00b0" + "C";

            views.setTextViewText(R.id.temp, tmpdata);
            views.setViewVisibility(R.id.weather, View.VISIBLE);
            mPref.setWidgetTemp(main);
            mPref.setWidgetTempImg("" + wImg);
            mPref.setWeatherCity(cityName);
            // mPref.setWidgetTempTime(formattedDate);

            int id = res.getWeather().get(0).getId();
            mPref.setWidgetTempDesc("" + id);
            //  mPref.setWidgetTempImg("" + wImg);
            getWeatherDesc("" + id, views);


            appWidgetManager.updateAppWidget(new ComponentName(mContext, ExtraInfoEngAppWidgetProvider.class), views);
            Utility.getInstance(mContext).writeToFile(json, mContext, JSONFILE);

        } else if (!mPref.getWidgetTemp().isEmpty()) {
            views.setImageViewResource(R.id.img, Integer.parseInt(mPref.getWidgetTempImg()));
            views.setTextViewText(R.id.update_date, getUpdatedTime(mPref.getLastWeatherUpdatedTime()));
            views.setTextViewText(R.id.temp, mPref.getWidgetTemp());
            getWeatherDesc(mPref.getWidgetTempDesc(), views);
            views.setViewVisibility(R.id.weather, View.VISIBLE);
            appWidgetManager.updateAppWidget(new ComponentName(mContext, ExtraInfoEngAppWidgetProvider.class), views);

        }
        mPref.load();

    }


    public String getUpdatedTime(long date) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date * 1000L);

        SimpleDateFormat df = new SimpleDateFormat("@dd MMM, HH:mm", Locale.US);
        return df.format(c.getTime());

    }


    private class DownloadFilesTask extends AsyncTask<RemoteViews, Integer, RemoteViews> {
        protected RemoteViews doInBackground(RemoteViews... views) {
            FestivalData.setFestivalData(mPref.getMyLanguage());

            mPanchangHashMap = new HashMap<>();
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            cal.set(Calendar.YEAR, 100 + cal.get(Calendar.YEAR));
            long number = cal.getTimeInMillis();

            long start = number - 1 * 32 * 24 * 60 * 60 * 1000L;
            long end = number + 1 * 32 * 24 * 60 * 60 * 1000L;


            EphemerisDao ephemerisDao = AppDatabase.getInstance(mContext).ephemerisDao();


            List<EphemerisEntity> ephemerisList = ephemerisDao.getAll2(start, end);

            PanchangTask ptObj = new PanchangTask();
            /*

            SqliteHelper mOpenHelper = SqliteHelper.getInstance(mContext);
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM COREDATA WHERE " + SqliteHelper.CoreDataEntry.KEY_COREDATA_TIMESTAMP + " > " + start + "  AND " + SqliteHelper.CoreDataEntry.KEY_COREDATA_TIMESTAMP + " < " + end, null);


            myPanchangHashMap = ptObj.panchangMapping(cursor, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), mContext);
            */
            myPanchangHashMap = ptObj.panchangMapping(ephemerisList, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), mContext);


            // views.setTextViewText(R.id.today_date,"XYZZ:"+cursor.getCount()+":"+myPanchangHashMap.size());
            // cursor.close();
            return views[0];
        }

        @Override
        protected void onPostExecute(RemoteViews views) {
            super.onPostExecute(views);
            handleView(views);
        }
    }

    public void handleView(RemoteViews views) {
        try {
            res = mContext.getResources();
            time_next = res.getString(R.string.e_time_next);
            time_to = res.getString(R.string.e_time_to);
            String o_sal = res.getString(R.string.e_sala_sana);
            String o_paksha = res.getString(R.string.e_paksha);
            String riseTxt = mContext.getResources().getString(R.string.e_sunrise);
            String setTxt = mContext.getResources().getString(R.string.e_sunset);

            if (!myPanchangHashMap.isEmpty()) {
                mPanchangHashMap.clear();
                mPanchangHashMap.putAll(myPanchangHashMap);
                String key = currYearInt + "-" + currMonthInt + "-" + currDayInt;
                CoreDataHelper dataVal = mPanchangHashMap.get(key);
                PanchangUtilityWidget pu = new PanchangUtilityWidget(1,mContext, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude());
                PanchangUtilityWidget.MyPanchang myPanchangObj = pu.getMyPunchang(dataVal);


                String sunRise = myPanchangObj.esunRise;
                String sunSet = myPanchangObj.esunSet;
                String solarDay = myPanchangObj.esolarDay;
                String solarMonth = myPanchangObj.esolarMonth;
                String sanSal = myPanchangObj.esanSal;
                //  String ritu = myPanchangObj.eRitu;
                String lunarDayPurimant = myPanchangObj.elunarDayPurimant;
                String lunarMonthPurimant = myPanchangObj.elunarMonthPurimant;
                String paksha = myPanchangObj.epaksha;
                PanchangUtilityWidget.MySubPanchang[] moonSignArr = myPanchangObj.emoonSign;

                PanchangUtilityWidget.MySubPanchang[] tithiArr = myPanchangObj.etithi;
                PanchangUtilityWidget.MySubPanchang[] nakshetraArr = myPanchangObj.enakshetra;
                //   PanchangUtility.MySubPanchang[] jogaArr = myPanchangObj.ejoga;
                //  PanchangUtility.MySubPanchang[] karanaArr = myPanchangObj.ekarana;
                String shraddha = myPanchangObj.le_shraddha;
                //  String strVal = solarDay + " " + solarMonth + ", " + ritu + " " + o_ritu + ", " + sanSal + " " + o_sal;
                // String strVal = solarDay + " " + solarMonth + ", " + sanSal + " " + o_sal;

                StringBuilder moonSignStr = getePanchangaValue(moonSignArr);
                StringBuilder tithiStr = getePanchangaValue(tithiArr);
                StringBuilder nakshetraStr = getePanchangaValue(nakshetraArr);
                //  StringBuilder jogaStr = getePanchangaValue(jogaArr);
                //   StringBuilder karanaStr = getePanchangaValue(karanaArr);
                //  String strVal2 = lunarDayPurimant + " " + lunarMonthPurimant + ", " + paksha + o_paksha + ", " + o_rasi + " " + moonSignStr;
                String strVal = solarDay + " " + solarMonth + ", " + sanSal + " " + o_sal + ", " + lunarDayPurimant + " " + lunarMonthPurimant + ", " + paksha + o_paksha + ", " + tithiStr;
                String strVal2 = tithiStr.toString().trim();
                views.setTextViewText(R.id.sunrise, riseTxt + " " + sunRise);
                views.setTextViewText(R.id.sunset, setTxt + " " + sunSet);
                views.setTextViewText(R.id.today_date, strVal + ", " + strVal2);
                // views.setTextViewText(R.id.today_dina, "xxx");

                Calendar c = Calendar.getInstance();
                int engYear = c.get(Calendar.YEAR);
                int engMonth = c.get(Calendar.MONTH);
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy, EEEE", Locale.US);
                String formattedDate = df.format(c.getTime());
                String strVal1 = formattedDate;
                views.setTextViewText(R.id.momentinfo, strVal1);

                views.setTextViewText(R.id.today_moonsign, moonSignStr.toString().trim());
                views.setTextViewText(R.id.today_nakshetra, nakshetraStr.toString().trim());
                if (mPref.getMyLanguage().contains("or")) {
                    views.setTextViewText(R.id.today_shraddha, shraddha.trim());
                    views.setViewVisibility(R.id.today_shraddha, View.VISIBLE);
                } else {
                    views.setViewVisibility(R.id.today_shraddha, View.GONE);
                }

                ArrayList<String> myFestivallist = FestivalData.calculateFestival(dataVal, currLang, mContext);
                // ArrayList<String> myFestivallist = FestivalData.calculateFestival(myPanchangObj.dayOfWeek, myPanchangObj.dayIndex, myPanchangObj.monthIndex, myPanchangObj.yearIndex, myPanchangObj.solarDayIndex, myPanchangObj.solarMonthIndex, lunarMonth, myPanchangObj.pakshaIndex, myPanchangObj.currTithiIndex, myPanchangObj.nextTithiIndex, myPanchangObj.nextToNextTithiIndex);

                if (myFestivallist.get(0).isEmpty()) {
                    views.setViewVisibility(R.id.festival, View.GONE);
                } else {
                    views.setViewVisibility(R.id.festival, View.VISIBLE);
                    // views.setTextViewText(R.id.festival, myFestivallist.get(1));


                    String specialcode = myFestivallist.get(2);
                    String festVal = myFestivallist.get(1);
                    if ((specialcode.startsWith("101") || specialcode.startsWith("1001") || specialcode.startsWith("10001")) && mPref.getMyLanguage().contains("or")) {
                        views.setViewVisibility(R.id.today_dina, View.VISIBLE);

                        festVal = festVal.replace(specialcode.split("@@")[2] + ",", "");
                        festVal = festVal.replace(specialcode.split("@@")[2], "");
                        PanchangUtilityWidget.MySubPanchang[] moonSign = myPanchangObj.emoonSign;
                        if (specialcode.startsWith("101")) {
                            views.setTextViewText(R.id.today_dina, moonSign[0].time + " gate " + specialcode.split("@@")[2]);
                        } else if (specialcode.startsWith("1001")) {
                            views.setTextViewText(R.id.today_dina, moonSign[0].time + " gate " + specialcode.split("@@")[2]);

                        } else if (specialcode.startsWith("10001")) {
                            views.setTextViewText(R.id.today_dina, specialcode.split("@@")[2]);
                        }

                    } else {
                        views.setViewVisibility(R.id.today_dina, View.GONE);
                    }

                    views.setTextViewText(R.id.festival, festVal);
                }

                updatemywidgetodia(views);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreateLoader(RemoteViews views) {
        new DownloadFilesTask().execute(views);


    }


    private void getWeatherDesc(String widgetTempDesc, RemoteViews views) {

        try {
            int id = Integer.parseInt(widgetTempDesc);
            if (id > 199) {
                views.setViewVisibility(R.id.desc, View.VISIBLE);
                String[] weather_desc_code = res.getStringArray(R.array.weather_desc_code);
                int index = Arrays.asList(weather_desc_code).indexOf("" + id);

                String[] weather_desc_eng = res.getStringArray(R.array.weather_desc_eng);
                views.setTextViewText(R.id.desc, weather_desc_eng[index]);

            } else {
                views.setViewVisibility(R.id.desc, View.GONE);
            }
        } catch (Exception e) {
            views.setViewVisibility(R.id.desc, View.GONE);
            e.printStackTrace();
        }


    }


    private StringBuilder getePanchangaValue(PanchangUtilityWidget.MySubPanchang[] arr) {

        StringBuilder arrStr = new StringBuilder();
        try {
            if (arr[0].time.isEmpty()) {
                arrStr.append(arr[0].name);
            } else {
                for (PanchangUtilityWidget.MySubPanchang obj : arr) {
                    String name = obj.name;
                    String time = obj.time;
                    if (time != "")
                        arrStr.append(name).append(" upto ").append(time).append(",");
                    else if (!name.isEmpty())
                        arrStr.append(time_next + " " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrStr;
    }


    public void updatemywidgetodia(RemoteViews views) {
        RegAppWidgetAlarm appWidgetAlarm = new RegAppWidgetAlarm(mContext.getApplicationContext());
        appWidgetAlarm.startAlarm();

        long currTime = System.currentTimeMillis() / 1000;
        int lastTime = mPref.getLastWeatherUpdatedTime();

        if (!mPref.isFirstUse()) {
            views.setViewVisibility(R.id.others, View.VISIBLE);


            Intent intent2 = new Intent(mContext, MainActivity.class);
            intent2.putExtra("widget_ele_todayinfo", 2);
            intent2.setAction(Long.toString(System.currentTimeMillis()));
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(mContext, 20, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.today_date, pendingIntent2);
            views.setOnClickPendingIntent(R.id.today_dina, pendingIntent2);

            Intent intent3 = new Intent(mContext, MainActivity.class);
            intent3.putExtra("widget_ele_observance", 3);
            intent3.setAction(Long.toString(System.currentTimeMillis()));
            intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent3 = PendingIntent.getActivity(mContext, 30, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.momentinfo, pendingIntent3);

            Intent intent4 = new Intent(mContext, MainActivity.class);
            intent4.putExtra("widget_ele_horoscope", 4);
            intent4.setAction(Long.toString(System.currentTimeMillis()));
            intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent4 = PendingIntent.getActivity(mContext, 40, intent4, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.horoscope, pendingIntent4);

            Intent intent5 = new Intent(mContext, MainActivity.class);
            intent5.putExtra("widget_ele_prayers", 5);
            intent5.setAction(Long.toString(System.currentTimeMillis()));
            intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent5 = PendingIntent.getActivity(mContext, 50, intent5, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.prayers, pendingIntent5);

            Intent intent6 = new Intent(mContext, MainActivity.class);
            intent6.putExtra("widget_ele_today_updates", 6);
            intent6.setAction(Long.toString(System.currentTimeMillis()));
            intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent6 = PendingIntent.getActivity(mContext, 60, intent6, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.news, pendingIntent6);

            Intent intent7 = new Intent(mContext, MainActivity.class);
            intent7.putExtra("widget_ele_event", 7);
            intent7.setAction(Long.toString(System.currentTimeMillis()));
            intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent7 = PendingIntent.getActivity(mContext, 70, intent7, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.event, pendingIntent7);

            Intent intentUpdate = new Intent(mContext, ExtraInfoEngAppWidgetProvider.class);
            intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] idArray = new int[]{mAppWidgetId};
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
            PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                    mContext, mAppWidgetId, intentUpdate,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.update_date, pendingUpdate);

        } else {
            views.setViewVisibility(R.id.others, View.GONE);
        }

        res = mContext.getResources();

        // CalendarWeatherApp.updateAppResource(mContext.getResources(), mContext.getApplicationContext());

        //  calendardata today = parseDay();

       /* if (today.festival.trim().isEmpty()) {
            views.setViewVisibility(R.id.festival, View.GONE);
        } else {
            views.setViewVisibility(R.id.festival, View.VISIBLE);
            views.setTextViewText(R.id.festival, today.festival);
        }*/
        if (mPref.getLatitude() != null && mPref.getLongitude() != null) {
            views.setViewVisibility(R.id.imgRel, View.VISIBLE);
            views.setViewVisibility(R.id.temp, View.VISIBLE);
            views.setViewVisibility(R.id.update_date, View.VISIBLE);
            if (Connectivity.isConnected(mContext)) {
                if (lastTime >= 0 && ((currTime - lastTime) > 30 * 60L)) {
                    weatherData();


                } else {
                    setWeatherView();


                }

            } else {
                setWeatherView();

            }
        }

        this.appWidgetManager.updateAppWidget(mAppWidgetId, views);

    }

}