package com.iexamcenter.calendarweather.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefManager {
    private static final String PROPERTY_APP_WEATHER_UPDATED_TIME = "WEATHER_UPDATED_TIME";
    private static final String PROPERTY_APP_THEME_TYPE = "THEME_TYPE";
    private static final String PROPERTY_APP_CLOCK_FORMAT = "CALENDAR_TYPE";

    private static final String PROPERTY_APP_THISDAY_IMAGE_BASE_URL = "THISDAY_IMAGE_BASE_UR";
    private static final String PROPERTY_APP_WHATISTODAY = "PROPERTY_APP_WHAT_IS_TODAY";
    private static final String PROPERTY_APP_THISDAY = "PROPERTY_APP_THIS";

    private static final String PROPERTY_APP_WIDGET_TEMP = "PROPERTY_APP_WIDGET_TEMP";
    private static final String PROPERTY_APP_WIDGET_TEMP_DESC = "PROPERTY_APP_WIDGET_TEMP_DESC";
    private static final String PROPERTY_APP_WIDGET_TEMP_IMG = "PROPERTY_APP_WIDGET_TEMP_IMG";

    private static final String PROPERTY_APP_HOROSCOPE = "PROPERTY_APP_HOROSCOPE";

    private static final String PROPERTY_APP_UPDATES_VERSION = "PROPERTY_APP_UPDATES_VERSION";

    private static final String PROPERTY_IS_REMOVED_ADS = "IS_REMOVED_ADS";
    private static final String PROPERTY_WHETHER_CURRENT_CITY = "WHETHER_CURRENT_CITY";

    private static final String PROPERTY_WHETHER_TOKEN = "WHETHER_TOKEN";
    private static final String PROPERTY_PUBLICATION = "WALL_CALENDAR_PUBLICATION";

    private static final String PROPERTY_THEME = "THEME";
    private static final String PROPERTY_REPORTING1 = "REPORTING1";
    private static final String PROPERTY_FIRST_USE = "APP_FIRST_USE";

    private static final String PROPERTY_MY_LANGUAGE = "MY_LANGUAGE";

    private static final String PROPERTY_COUNTRY = "COUNTRY";
    private static final String PROPERTY_COUNTRY_CODE = "COUNTRY_CODE";

    private static final String PROPERTY_LATITUDE = "LATITUDE";
    private static final String PROPERTY_LONGITUDE = "LONGITUDE";
    private static final String PROPERTY_AREA_ADMIN = "AREA_ADMIN";
    private static final String PROPERTY_AREA_SUBADMIN = "AREA_SUBADMIN";
    private static final String PROPERTY_FCM_TOKEN = "FCM_TOKEN";
    private static final String PROPERTY_LUNAR_MONTH_TYPE = "LUNAR_MONTH_TYPE";
    private static final String PROPERTY_WEATHER_CITY_ID = "PROPERTY_WEATHER_CITY_ID";
    private static final String PROPERTY_APP_WEATHER_CITY = "PROPERTY_APP_WEATHER_CITY";



    private Context mContext;
    private SharedPreferences pref;
    private Editor editor;
    private int weatherUpdatedTime, clockFormat, themeType, settingReporting1;
    private boolean isTheme, isRemovedAds, isWeatherCurrCity, firstUse;
    private String weatherCity,cityId,horoscope, publication, myLanguage, latitude, longitude, areaAdmin, areaSubadmin, country, countryCode;
    private String widgetTemp, widgetTempDesc, widgetTempImg, whetherToken, whattoday, thisday, thisDayImageBaseUrl;
    private static PrefManager instance;
    private int appUpdatesVersionCode;

    private String fcmToken;


    private PrefManager(Context ctx) {
        mContext = ctx;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context ctx) {

        if (instance == null)
            instance = new PrefManager(ctx);

        return instance;
    }


    public void load() {
        weatherCity = pref.getString(PROPERTY_APP_WEATHER_CITY, "");
        weatherUpdatedTime = pref.getInt(PROPERTY_APP_WEATHER_UPDATED_TIME, 0);
        themeType = pref.getInt(PROPERTY_APP_THEME_TYPE, 0);
        clockFormat = pref.getInt(PROPERTY_APP_CLOCK_FORMAT, 2);

        thisDayImageBaseUrl = pref.getString(PROPERTY_APP_THISDAY_IMAGE_BASE_URL, "");
        fcmToken = pref.getString(PROPERTY_FCM_TOKEN, "");
        appUpdatesVersionCode = pref.getInt(PROPERTY_APP_UPDATES_VERSION, 0);

        widgetTemp = pref.getString(PROPERTY_APP_WIDGET_TEMP, "");
        widgetTempDesc = pref.getString(PROPERTY_APP_WIDGET_TEMP_DESC, "");
        widgetTempImg = pref.getString(PROPERTY_APP_WIDGET_TEMP_IMG, "");

        horoscope = pref.getString(PROPERTY_APP_HOROSCOPE, "");
        cityId = pref.getString(PROPERTY_WEATHER_CITY_ID, "");
        whattoday = pref.getString(PROPERTY_APP_WHATISTODAY, "");
        thisday = pref.getString(PROPERTY_APP_THISDAY, "");
        publication = pref.getString(PROPERTY_PUBLICATION, "govtcal");
        myLanguage = pref.getString(PROPERTY_MY_LANGUAGE, "en");
        whetherToken = pref.getString(PROPERTY_WHETHER_TOKEN, Constant.WHETHER_TOKEN);
        isRemovedAds = pref.getBoolean(PROPERTY_IS_REMOVED_ADS, false);


        isTheme = pref.getBoolean(PROPERTY_THEME, true);

        isWeatherCurrCity = pref.getBoolean(PROPERTY_WHETHER_CURRENT_CITY, true);

        settingReporting1 = pref.getInt(PROPERTY_REPORTING1, 5 * 60);
        firstUse = pref.getBoolean(PROPERTY_FIRST_USE, true);

        country = pref.getString(PROPERTY_COUNTRY, "INDIA");
        countryCode = pref.getString(PROPERTY_COUNTRY_CODE, "IN");

        latitude = pref.getString(PROPERTY_LATITUDE, null);
        longitude = pref.getString(PROPERTY_LONGITUDE, null);
        areaAdmin = pref.getString(PROPERTY_AREA_ADMIN, null);
        areaSubadmin = pref.getString(PROPERTY_AREA_SUBADMIN, null);

    }


    private void setLunarMonthType(int lunarMonthType) {
        editor.putInt(PROPERTY_LUNAR_MONTH_TYPE, lunarMonthType);
        editor.commit();
    }

    public int getThemeType() {
        return this.themeType;
    }

    public void setThemeType(int themeType) {
        this.themeType = themeType;
        editor.putInt(PROPERTY_APP_THEME_TYPE, themeType);
        editor.commit();
    }

    public int getClockFormat() {
        return this.clockFormat;
    }

    public void setClockFormat(int clockFormat) {
        this.clockFormat = clockFormat;
        editor.putInt(PROPERTY_APP_CLOCK_FORMAT, clockFormat);
        editor.commit();
    }

    public String getThisDayImageBaseUrl() {
        return thisDayImageBaseUrl;
    }

    public void setThisDayImageBaseUrl(String thisDayImageBaseUrl) {
        this.thisDayImageBaseUrl = thisDayImageBaseUrl;
        editor.putString(PROPERTY_APP_THISDAY_IMAGE_BASE_URL, thisDayImageBaseUrl);
        editor.commit();
    }

    public String getWidgetTempImg() {
        return widgetTempImg;
    }

    public void setWidgetTempImg(String widgetTempImg) {
        this.widgetTempImg = widgetTempImg;
        editor.putString(PROPERTY_APP_WIDGET_TEMP_IMG, widgetTempImg);
        editor.commit();
    }
    public String getWeatherCity() {
        return weatherCity;
    }

    public void setWeatherCity(String city) {
        this.weatherCity = city;
        editor.putString(PROPERTY_APP_WEATHER_CITY, city);
        editor.commit();
    }

    public String getWidgetTemp() {
        return widgetTemp;
    }

    public void setWidgetTemp(String widgetTemp) {
        this.widgetTemp = widgetTemp;
        editor.putString(PROPERTY_APP_WIDGET_TEMP, widgetTemp);
        editor.commit();
    }

    public void setWidgetTempDesc(String widgetTempDesc) {
        this.widgetTempDesc = widgetTempDesc;
        editor.putString(PROPERTY_APP_WIDGET_TEMP_DESC, widgetTempDesc);
        editor.commit();
    }

    public String getWidgetTempDesc() {
        return widgetTempDesc;
    }

    public String getThisday() {
        return thisday;
    }

    public void setThisday(String thisday) {
        this.thisday = thisday;
        editor.putString(PROPERTY_APP_THISDAY, thisday);
        editor.commit();
    }

    public String getWhattoday() {
        return whattoday;
    }

    public void setWhattoday(String whattoday) {
        this.whattoday = whattoday;
        editor.putString(PROPERTY_APP_WHATISTODAY, whattoday);
        editor.commit();
    }

    public String getFcmToken() {
        return fcmToken;
    }


    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
        editor.putString(PROPERTY_APP_HOROSCOPE, horoscope);
        editor.commit();
    }


    public int getLastWeatherUpdatedTime() {
        return weatherUpdatedTime;
    }

    public void setLastWeatherUpdatedTime(int weatherUpdatedTime) {
        this.weatherUpdatedTime = weatherUpdatedTime;
        editor.putInt(PROPERTY_APP_WEATHER_UPDATED_TIME, weatherUpdatedTime);
        editor.commit();
    }


    public int getAppUpdatesVersionCode() {
        return appUpdatesVersionCode;
    }

    public void setAppUpdatesVersionCode(int appUpdatesVersionCode) {
        this.appUpdatesVersionCode = appUpdatesVersionCode;
        editor.putInt(PROPERTY_APP_UPDATES_VERSION, appUpdatesVersionCode);
        editor.commit();
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String pub) {
        this.publication = pub;
        editor.putString(PROPERTY_PUBLICATION, pub);
        editor.commit();
    }


    public String getMyLanguage() {
        return myLanguage;
    }

    public void setMyLanguage(String lang) {
        if (lang.contains("te")) {
            setLunarMonthType(2);
        } else if (lang.contains("ta") || lang.contains("bn")) {
            setLunarMonthType(1);
        } else {
            setLunarMonthType(3);
        }
        this.myLanguage = lang;
        editor.putString(PROPERTY_MY_LANGUAGE, lang);
        editor.commit();
    }


    public boolean isRemovedAds() {
        return isRemovedAds;
    }

    public void setRemovedAds(boolean removedAds) {
        isRemovedAds = removedAds;
        editor.putBoolean(PROPERTY_IS_REMOVED_ADS, removedAds);
        editor.commit();
    }




    public String getWhetherToken() {
        return whetherToken;
    }


    public boolean isWeatherCurrCity() {
        return isWeatherCurrCity;
    }

    public void isWeatherCurrCity(boolean booll) {
        this.isWeatherCurrCity = booll;
        editor.putBoolean(PROPERTY_WHETHER_CURRENT_CITY, booll);
        editor.commit();
    }


    public int getSettingReporting1() {
        return settingReporting1;
    }

    public void setSettingReporting1(int settingReporting1) {
        this.settingReporting1 = settingReporting1;
        editor.putInt(PROPERTY_REPORTING1, settingReporting1);
        editor.commit();
    }


    public String getCountryCode() {
        return countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        editor.putString(PROPERTY_COUNTRY, country);
        editor.commit();
    }


    public boolean isTheme() {
        return isTheme;
    }

    public void setTheme(boolean theme) {
        this.isTheme = theme;
        editor.putBoolean(PROPERTY_THEME, isTheme);
        editor.commit();
    }


    public boolean isFirstUse() {
        return firstUse;
    }

    public void setFirstUse(boolean firstUse) {
        this.firstUse = firstUse;
        editor.putBoolean(PROPERTY_FIRST_USE, firstUse);
        editor.commit();
    }


    public String getAreaAdmin() {
        return areaAdmin;
    }

    public void setAreaAdmin(String areaAdmin) {
        this.areaAdmin = areaAdmin;
        editor.putString(PROPERTY_AREA_ADMIN, areaAdmin);
        editor.commit();
    }


    public String getAreaSubadmin() {
        return areaSubadmin;
    }


    public String getLatitude() {

        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        editor.putString(PROPERTY_LATITUDE, latitude);
        editor.commit();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        editor.putString(PROPERTY_LONGITUDE, longitude);
        editor.commit();
    }
    public void setWeatherCityId(String cityId) {
        this.cityId = cityId;
        editor.putString(PROPERTY_WEATHER_CITY_ID, cityId);
        editor.commit();
    }
    public String getWeatherCityId() {
        return cityId;
    }
    public int getNotified(String notif) {
        return pref.getInt(notif, -1);
    }

    public void setNotified(String notif, int val) {
        editor.putInt(notif, val);
        editor.commit();
    }

    public long getNewsUpdateTime(String key) {
        return pref.getLong(key, -1L);
    }

    public void setNewsUpdateTime(String key, long val) {
        editor.putLong(key, val);
        editor.commit();
    }
}
