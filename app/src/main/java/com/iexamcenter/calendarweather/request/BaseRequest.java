package com.iexamcenter.calendarweather.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.iexamcenter.calendarweather.utility.PrefManager;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BaseRequest {


    protected JSONObject baseJson;
    private PrefManager mpref = null;

    public BaseRequest(Context ctx) {
        mpref = PrefManager.getInstance(ctx);
        mpref.load();
        baseJson = new JSONObject();
        String version="";
        int versionCode=0;
        String localTime="";
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    ctx.getPackageName(), 0);
             version = info.versionName;
            versionCode = info.versionCode;


            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                    Locale.ENGLISH);
            Date currentLocalTime = calendar.getTime();
            DateFormat date = new SimpleDateFormat("Z");
            localTime = date.format(currentLocalTime);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            baseJson.put("DV", "1");
            baseJson.put("OS", android.os.Build.VERSION.RELEASE);
            baseJson.put("API_VERSION", ""+ versionCode);
            baseJson.put("APP_VERSION", ""+ version);
            baseJson.put("LANG_CODE", ""+ mpref.getMyLanguage());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
