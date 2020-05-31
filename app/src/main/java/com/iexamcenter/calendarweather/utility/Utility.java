package com.iexamcenter.calendarweather.utility;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utility {
    private static Context mContext;
    private static Utility singleton = new Utility();
    private static PrefManager mPref;


    private static String[] e_month_arr, l_bara_arr, l_number;

    private Utility() {

    }

    private static Resources res;

    public static boolean isConnectedMobile() {
        NetworkInfo info = Connectivity.getNetworkInfo(mContext);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public void writeToFile(String data, Context context, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUpdatedTime(long date) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date * 1000L);
        int cLocDayNo = c.get(Calendar.DAY_OF_MONTH);
        int cLocMonthNo = c.get(Calendar.MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        String dayNoStr = Utility.getInstance(mContext).getDayNo("" + cLocDayNo);
        String monStr = res.getStringArray(R.array.l_arr_month)[cLocMonthNo];
        // String monStr = Utility.getInstance(mContext).getMonth(cLocMonthNo, mContext.getResources());
        String hrStr = Utility.getInstance(mContext).getDayNo("" + hr);
        String minStr = Utility.getInstance(mContext).getDayNo("" + min);

        SimpleDateFormat df = new SimpleDateFormat("@dd MMM, HH:mm", Locale.US);
        /*
        String localZero = Utility.getInstance(mContext).getDayNo("0");
        if (minStr.length() < 2)
            minStr = Utility.getInstance(mContext).getDayNo(localZero) + minStr;
        if (hrStr.length() < 2)
            hrStr = localZero + hrStr;
         String updateDateStr = "@" + dayNoStr + "" + monStr + ", " + hrStr + ":" + minStr;
         if (mPref.getMyLanguage().contains("or"))
             return updateDateStr;
         else*/
        return df.format(c.getTime());
    }

    public void setImgWeather(final ImageView imgWeather1, String icon) {
        int wImg;
        switch (icon) {

            case "01d":
                wImg = R.drawable.ic_01d;
                break;
            case "01n":
                wImg = R.drawable.ic_01n;
                break;
            case "02d":
                wImg = R.drawable.ic_02d;
                break;
            case "02n":
                wImg = R.drawable.ic_02n;
                break;
            case "03d":
                wImg = R.drawable.ic_03d;
                break;
            case "03n":
                wImg = R.drawable.ic_03n;
                break;
            case "04d":
                wImg = R.drawable.ic_04d;
                break;
            case "04n":
                wImg = R.drawable.ic_04n;
                break;
            case "09d":
                wImg = R.drawable.ic_09d;
                break;
            case "09n":
                wImg = R.drawable.ic_09n;
                break;
            case "10d":
                wImg = R.drawable.ic_10d;
                break;
            case "10n":
                wImg = R.drawable.ic_10n;
                break;
            case "11d":
                wImg = R.drawable.ic_11d;
                break;
            case "11n":
                wImg = R.drawable.ic_11n;
                break;
            case "50d":
                wImg = R.drawable.ic_50d;
                break;
            case "50n":
                wImg = R.drawable.ic_50n;
                break;
            case "13d":
                wImg = R.drawable.ic_13d;
                break;
            case "13n":
                wImg = R.drawable.ic_13n;
                break;
            default:
                wImg = R.drawable.ic_01d;
        }
        imgWeather1.setImageResource(wImg);
        mPref.setWidgetTempImg("" + wImg);
    }


    public String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    public static Utility getInstance(Context context) {
        mPref = PrefManager.getInstance(context);
        mPref.load();
        mContext = context;
        res = mContext.getResources();
        if(CalendarWeatherApp.isPanchangEng){
            e_month_arr = res.getStringArray(R.array.e_arr_month);
            l_bara_arr = res.getStringArray(R.array.e_arr_bara);
            l_number = res.getStringArray(R.array.e_arr_number);
        }else{
            e_month_arr = res.getStringArray(R.array.l_arr_month);
            l_bara_arr = res.getStringArray(R.array.l_arr_bara);
            l_number = res.getStringArray(R.array.l_arr_number);
        }



        return singleton;
    }


    public int setAnimation(View viewToAnimate, int position, int lastPosition) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation;

            animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);

            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
        return lastPosition;
    }

    public void newToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);

        toast.show();
    }

    public void newToastLong(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);

        toast.show();
    }

    private static int daysBetween(Date startDate, Date endDate) {
        int daysBetween = 0;
        while (startDate.before(endDate)) {
            startDate.setTime(startDate.getTime() + 86400000);
            daysBetween++;
        }
        return daysBetween;
    }

    public String getTimeAgo(long time) {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        long now = 0;
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        String milli = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.US).format(new Date());
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.US);
        try {
            Date d = f.parse(milli);
            now = d.getTime();
        } catch (ParseException p) {
            p.printStackTrace();
        }


        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public String getMyDateTimeFormat(String str_date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        Date date;
        try {
            date = sdf.parse(str_date);


            String timeOfDay = new SimpleDateFormat("HH:mm", Locale.US).format(date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            java.sql.Timestamp timeStampNow = new Timestamp((new java.util.Date()).getTime());

            long secondDiff = timeStampNow.getTime() / 1000 - timeStampDate.getTime() / 1000;
            int minuteDiff = (int) (secondDiff / 60);
            int hourDiff = (int) (secondDiff / 3600);
            int dayDiff = daysBetween(date, new Date()) - 1;
            if (dayDiff > 0) {

                return "posted " + dayDiff + " days ago @ " + timeOfDay;
            } else if (hourDiff > 0) {
                return "posted " + hourDiff + " hour(s) ago @ " + timeOfDay;
            } else if (minuteDiff > 0) {
                return "posted " + minuteDiff + " minute(s) ago @ " + timeOfDay;
            } else if (secondDiff > 0) {
                return "posted " + secondDiff + " second(s) ago @ " + timeOfDay;
            } else
                return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    public String getLanguageFull() {
        String code = mPref.getMyLanguage();
        String lang = " ";
        if (code.contentEquals("or"))
            return "Odia";
        else if (code.contentEquals("bn"))
            return "Bangla";
        else if (code.contentEquals("hi"))
            return "Hindi";
        else if (code.contentEquals("gu"))
            return "Gujarati";
        else if (code.contentEquals("kn"))
            return "Kannada";
        else if (code.contentEquals("mr"))
            return "Marathi";
        else if (code.contentEquals("ml"))
            return "Malayalam";
        else if (code.contentEquals("pa"))
            return "Punjabi";
        else if (code.contentEquals("ta"))
            return "Tamil";
        else if (code.contentEquals("te"))
            return "Telugu";

        return lang;
    }

    public String getRegLanguageFull() {
        String code = mPref.getMyLanguage();
        String lang = "English";
        if (code.contentEquals("or"))
            return res.getString(R.string.lang_odia);
        else if (code.contentEquals("bn"))
            return res.getString(R.string.lang_bengali);
        else if (code.contentEquals("hi"))
            return res.getString(R.string.lang_hindi);
        else if (code.contentEquals("gu"))
            return res.getString(R.string.lang_gujarati);
        else if (code.contentEquals("kn"))
            return res.getString(R.string.lang_kannada);
        else if (code.contentEquals("mr"))
            return res.getString(R.string.lang_marathi);
        else if (code.contentEquals("ml"))
            return res.getString(R.string.lang_malayalam);
        else if (code.contentEquals("pa"))
            return res.getString(R.string.lang_punjabi);
        else if (code.contentEquals("ta"))
            return res.getString(R.string.lang_tamil);
        else if (code.contentEquals("te"))
            return res.getString(R.string.lang_telugu);

        return lang;
    }

    public String getDayNo(String dayNo) {

        StringBuilder word = new StringBuilder();
        try {
            for (int i = 0; i < dayNo.length(); i++) {
                int digit = Integer.parseInt("" + dayNo.charAt(i));

                word.append(l_number[digit]);

            }
            return word.toString();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return word.toString();
    }

    private String getMonthEng(int month) {
        return e_month_arr[month];
    }


    public String getDayWord(int dayOfWeek) {

        return l_bara_arr[dayOfWeek - 1];

    }

    private static String getDateCurrentTimeZoneForMoon(String timeZoneStr, String datetime) {
        try {
            if (datetime.endsWith(" UT")) {
                datetime = datetime.substring(0, datetime.length() - 3);

            }

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("yyyy/MM/d HH:mm:s", Locale.US);
            //  Log.e("datetime","datetimedatetime:"+datetime);
            Date date = formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStr));
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd:hh:mm a", Locale.US);
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);


        } catch (Exception e) {
            // e.printStackTrace();
        }
        return "-";
    }

    public static boolean isTimeAhead(String datetime) {
        try {
            Calendar cal = Calendar.getInstance();
            datetime = datetime + "/" + cal.get(Calendar.YEAR);

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("M/d/yyyy", Locale.US);
            Date date = formatter1.parse(datetime);
            long timeStamp = date.getTime() + 24 * 60 * 60 * 1000;
            Timestamp timeStampDate = new Timestamp(timeStamp);
            TimeZone tz = TimeZone.getTimeZone("GMT");
            Calendar calendar = Calendar.getInstance(tz, Locale.US);
            long timeStamp1 = calendar.getTimeInMillis();
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            if (timeStamp1 < timeStamp) {

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDateFromString(String datetime) {
        try {

            String[] date1 = datetime.split("/");
            String monthStr = Utility.getInstance(mContext).getMonthEng(Integer.parseInt(date1[0]) - 1);
            return monthStr + "-" + date1[1];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateCurrentTimeZone(String timeZoneStr, String datetime, boolean isSecond) {
        try {
            String format = "hh:mm a";
            if (isSecond)
                format = "hh:mm:ss a";


            if (datetime.endsWith(" UT")) {
                datetime = datetime.substring(0, datetime.length() - 3);
            }


            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("yyyy/MM/d HH:mm:s", Locale.US);
            Date date = formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStr));
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateCurrentTimeZone(String timeZoneStr, String datetime) {
        try {


            if (datetime.endsWith(" UT")) {
                datetime = datetime.substring(0, datetime.length() - 3);
            }


            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("yyyy/MM/d HH:mm:s", Locale.US);
            Date date = formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStr));
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDateTimeForecastGrid(String time1) {
        long time = Long.parseLong(time1) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(time);

        return DateFormat.format("EEEE", cal).toString();

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public String getDateTime(String time1) {
        long time = Long.parseLong(time1) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);

        return DateFormat.format("dd/MM/yyyy hh:mm a", cal).toString();

    }

    public String getDateTimeWeatherHome(String time1) {
        long time = Long.parseLong(time1) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("MM/dd@hh:mm a", cal).toString();

    }


    public static class MoonDetails {
        public String moonRise, moonSet, age, distance, title, date, moonEl, moonAz;


    }

    public static class SunDetails {
        public String aSunRise, aSunSet, nSunRise, nSunSet, cSunRise, cSunSet;

        public String sunRise, sunSet, sunTransit, sunDist, sunTransitElev, date, sunEl, sunAz;
    }

    public SunDetails getTodaySunDetails(Calendar calendar) {
        try {
            mPref.load();
            double lat = Double.parseDouble(mPref.getLatitude());
            double lng = Double.parseDouble(mPref.getLongitude());
            // ArrayList<SunDetails> md = new ArrayList<>();
            String timeZoneStr = TimeZone.getDefault().getID();

            // md.clear();
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);

            double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, hr, min, sec, obsLon, obsLat);
            smc.calcSunAndMoon();


            String sunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
            String sunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
            String sunTransit = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunTransit));


            double sunDist = smc.sunDist * SunMoonCalculator.AU;
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);


            SunDetails mdObj = new SunDetails();
            mdObj.sunAz = df.format(smc.sunAz * SunMoonCalculator.RAD_TO_DEG);
            mdObj.sunEl = df.format(smc.sunEl * SunMoonCalculator.RAD_TO_DEG);
            mdObj.sunRise = sunRise;
            mdObj.sunSet = sunSet;
            mdObj.sunTransit = sunTransit;
            mdObj.sunTransitElev = df.format(smc.sunTransitElev * SunMoonCalculator.RAD_TO_DEG);
            mdObj.sunDist = df.format(sunDist);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            mdObj.date = format.format(calendar.getTime());


            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_ASTRONOMICAL);
            smc.calcSunAndMoon();
            String aSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
            String aSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
            mdObj.aSunRise = aSunRise;
            mdObj.aSunSet = aSunSet;

            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_NAUTICAL);
            smc.calcSunAndMoon();
            String nSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
            String nSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
            mdObj.nSunRise = nSunRise;
            mdObj.nSunSet = nSunSet;

            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_CIVIL);
            smc.calcSunAndMoon();
            String cSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
            String cSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
            mdObj.cSunRise = cSunRise;
            mdObj.cSunSet = cSunSet;

            return mdObj;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public SunDetails getTodaySunDetails(Calendar calendar, boolean isSecond) {
        try {
            mPref.load();
            double lat = Double.parseDouble(mPref.getLatitude());
            double lng = Double.parseDouble(mPref.getLongitude());
            // ArrayList<SunDetails> md = new ArrayList<>();
            String timeZoneStr = TimeZone.getDefault().getID();

            // md.clear();
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);

            double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, hr, min, sec, obsLon, obsLat);
            smc.calcSunAndMoon();


            String sunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise),true);
            String sunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet),true);


            SunDetails mdObj = new SunDetails();
            mdObj.sunRise = sunRise;
            mdObj.sunSet = sunSet;


            return mdObj;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<SunDetails> getSunDetails(Calendar calendar1) {
        try {
            mPref.load();
            ArrayList<SunDetails> md = new ArrayList<>();

            md.clear();

            for (int i = 0; i < calendar1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 1);


                calendar.add(Calendar.DATE, i);


                md.add(i, getTodaySunDetails(calendar));
            }
            return md;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public MoonDetails getTodayMoonDetails(Calendar calendar) {
        try {
            mPref.load();
            double lat = Double.parseDouble(mPref.getLatitude());
            double lng = Double.parseDouble(mPref.getLongitude());
            String timeZoneStr = TimeZone.getDefault().getID();

            int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);

            double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 12, 0, 0, obsLon, obsLat);
            smc.calcSunAndMoon();


            String moonRise = Utility.getInstance(mContext).getDateCurrentTimeZoneForMoon(timeZoneStr, SunMoonCalculator.getDateAsString(smc.moonRise));
            String moonSet = Utility.getInstance(mContext).getDateCurrentTimeZoneForMoon(timeZoneStr, SunMoonCalculator.getDateAsString(smc.moonSet));


            double moonAge = smc.moonAge;

            double moonDistance = smc.moonDist * SunMoonCalculator.AU;
            String moonTitle = "";
            if (moonAge >= 0 && moonAge < 1) {
                moonTitle = "New moon";
            } else if (moonAge >= 1 && moonAge < 7) {
                moonTitle = "Waxing crescent";
            } else if (moonAge >= 7 && moonAge < 8) {
                moonTitle = "First quarter";
            } else if (moonAge >= 8 && moonAge < 15) {
                moonTitle = "Waxing gibbous";
            } else if (moonAge >= 15 && moonAge < 16) {
                moonTitle = "Full moon";
            } else if (moonAge >= 16 && moonAge < 21) {
                moonTitle = "Waning gibbous";
            } else if (moonAge >= 21 && moonAge < 22) {
                moonTitle = "Last quarter";
            } else if (moonAge >= 22) {
                moonTitle = "Waning crescent";
            }
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);


            MoonDetails mdObj = new MoonDetails();
            mdObj.moonAz = df.format(smc.moonAz * SunMoonCalculator.RAD_TO_DEG);
            mdObj.moonEl = df.format(smc.moonEl * SunMoonCalculator.RAD_TO_DEG);
            mdObj.moonRise = moonRise;
            mdObj.moonSet = moonSet;
            mdObj.age = df.format(moonAge);
            mdObj.title = moonTitle;
            mdObj.distance = df.format(moonDistance);
            //  mdObj.date = android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString();
            //  mdObj.date = android.text.format.DateFormat.format(DateFormat.getBestDateTimePattern(Locale.ENGLISH, "dd/MM/yyyy"), calendar).toString();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            mdObj.date = format.format(calendar.getTime());
//System.out.println("formatformat:"+mdObj.date);
            //  mdObj.moonImage = 1;

          /*  if (moonRiseT > moonSetT) {
                mdObj.isRiseFirst = 0;
            } else {
                mdObj.isRiseFirst = 1;
            }*/

            return mdObj;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<MoonDetails> getMoonDetails(Calendar calendar1) {
        try {
            mPref.load();
            ArrayList<MoonDetails> md = new ArrayList<>();

            md.clear();

            for (int i = 0; i < calendar1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 1);
                calendar.add(Calendar.DATE, i);
                md.add(i, getTodayMoonDetails(calendar));
            }
            return md;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getFormattedDate(Calendar cal,String mLang,int mType,Calendar selectedCal,int mCalType,String[] le_arr_month) {

        if ((mLang.contains("or") || mLang.contains("hi")  || mLang.contains("mr")   || mLang.contains("gu")) && mType == 0) {
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
            return geteFormattedDate(cal,selectedCal,mCalType,le_arr_month);
        }
    }
    public  String geteFormattedDate(Calendar cal,Calendar selectedCal,int mCalType,String[] le_arr_month) {

        try {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            Date date = cal.getTime();
            java.text.DateFormat dateFormat;
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

    public String timeConversion(long totalSeconds,String le_time_hour,String le_time_min) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        long totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        long hours = totalMinutes / MINUTES_IN_AN_HOUR;


        return getDayNo("" + hours) + le_time_hour + Utility.getInstance(mContext).getDayNo("" + minutes) + le_time_min;
    }
}
