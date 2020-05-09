package com.iexamcenter.calendarweather.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.gms.common.internal.safeparcel.SafeParcelableSerializer;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlacePicker;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sasikanta_Sahoo on 11/28/2017.
 * Helper
 */

public class Helper {
    private static Helper instance = null;

    private Helper() {
    }

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }
    public void hideKeyboard(MainActivity mContext) {

        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }


    public void getSideRealTime() {
        int yearInt = 2015, monthInt = 9, dayInt = 22, hourOfDay = 6;
        double Lzero = 99.967794687;
        double Lone = 360.98564736628603;
        double Ltwo = 2.907879 * Math.pow(10, -13);
        double Lthree = -5.302 * Math.pow(10, -22);

        Calendar calJD = Calendar.getInstance();
        calJD.set(2000, 0, 1, 0, 0, 0);

        Calendar calNow = Calendar.getInstance();
        calNow.set(yearInt, monthInt, dayInt, hourOfDay, 0, 0);

        int hr = calNow.get(Calendar.HOUR_OF_DAY);
        int min = calNow.get(Calendar.MINUTE);
        double inhour = hr + min / 60.0;

        long diff = (calNow.getTimeInMillis() - calJD.getTimeInMillis());
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        double timeZone = 5.5;
        double JD = (days) + ((inhour - 5.5) / 24);
        // double JD=days+ ((inhour-1)/24);
//77.5946
        //  double westLong = -77.59;
        double westLong = -77.59;
        // double westLong=-5;
        double result = (Lzero + (Lone * JD) + (Ltwo * (JD * JD)) + (Lthree * (JD * JD * JD)) - westLong);

        double theta = result % 360;

        double localSideRealTime = theta / 15;
        Log.e("localSideRealTime", "localSideRealTime:" + localSideRealTime);

        double lat = 12.97; //north
        double cosA = Math.cos(theta);
        double sinA = Math.sin(theta);
        double cosE = Math.cos(23.44);
        double sinE = Math.sin(23.44);
        double tanL = Math.tan(lat);
        double x = (sinA * cosE + tanL * sinE);
        double y = -cosA;

        double var1 = Math.atan2(y, x);
        Log.e("localSideRealTime", "localSideRealTime:asedant:0:" + var1);
        if (x < 0) {
            var1 = var1 + 180;
        } else {
            var1 = var1 + 360;
        }
        Log.e("localSideRealTime", "localSideRealTime:asedant:1:" + var1);
        if (var1 < 180) {
            var1 = var1 + 180;
        } else {
            var1 = var1 - 180;
        }
        Log.e("localSideRealTime", "localSideRealTime:asedant:2:" + var1);





/*


        double A = -6.92416 + (16.90709 * (yearInt / 1000)) - 0.757371 * (yearInt / 1000) * (yearInt / 1000);

        double B = (monthInt + (dayInt / 30)) * (1.1574074 / 1000);
        double ayamansa = A + B;
        Log.e("localSideRealTime", "localSideRealTime:asedant:4:" + ayamansa);
        */


    }

    private double[] shadow = new double[]{
            0.21, 0.42, 0.63, 0.84, 1.05, 1.26, 1.47, 1.69, 1.90, 2.11,
            2.33, 2.55, 2.70, 2.99, 3.21, 3.44, 3.66, 3.90, 4.13, 4.37,
            4.60, 4.85, 5.09, 5.34, 5.59, 5.85, 6.11, 6.38, 6.65, 6.93,
            7.21, 7.50, 7.79, 8.09, 8.40, 8.71, 9.04, 9.37, 9.72, 10.06,
            10.43, 10.80, 11.19, 11.58, 12.00, 12.42, 12.87, 13.33, 13.80, 14.30,
            14.82, 15.35, 15.92, 16.52, 17.13, 17.79, 18.46, 19.20, 19.97, 20.78
    };

    public double getPlanetPos(Calendar sunRise, String planet, double dailyMotion) {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, sunRise.get(Calendar.YEAR));
        cal1.set(Calendar.MONTH, sunRise.get(Calendar.MONTH));
        cal1.set(Calendar.DAY_OF_MONTH, sunRise.get(Calendar.DAY_OF_MONTH));
        cal1.set(Calendar.HOUR_OF_DAY, 5);
        cal1.set(Calendar.MINUTE, 30);

        double diff = ((cal1.getTimeInMillis() / 1000.0) - (sunRise.getTimeInMillis() / 1000.0)) / (60 * 60.0);
        double diff1 = diff;
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

        return currPlanetDeg;
    }

    public ArrayList<lagna> getLagna(Calendar sunRiseCal1, double atSunriseSunPlanetLng, double lat) {
        //file:///Users/sasikantasahoo/Downloads/dokumen.tips_ascendant-calculation.pdf
        //https://iswaryajyotisha.com/pages/library.php?book=Ascendant%20Calculation
        Log.e("Lagna", "LagnaASC:milli:" + sunRiseCal1.getTimeInMillis() + ":sunATRISE:" + atSunriseSunPlanetLng);
        Calendar sunRiseCal = Calendar.getInstance(Locale.ENGLISH);
        sunRiseCal.set(Calendar.YEAR, sunRiseCal1.get(Calendar.YEAR));
        sunRiseCal.set(Calendar.MONTH, sunRiseCal1.get(Calendar.MONTH));
        sunRiseCal.set(Calendar.DAY_OF_MONTH, sunRiseCal1.get(Calendar.DAY_OF_MONTH));
        sunRiseCal.set(Calendar.HOUR_OF_DAY, sunRiseCal1.get(Calendar.HOUR_OF_DAY));
        sunRiseCal.set(Calendar.MINUTE, sunRiseCal1.get(Calendar.MINUTE));
        sunRiseCal.set(Calendar.SECOND, 0);
        sunRiseCal.set(Calendar.MILLISECOND, 0);

        // double lat=12.54;
        int degLat = (int) lat;//Math.round(lat);
        double shadowVal = shadow[degLat - 1];
        Log.e("Lagna", "LagnaASC:1:" + degLat);
        Log.e("Lagna", "LagnaASC:2:" + shadowVal);
        double group1 = shadowVal * 10 * 6;
        double group2 = shadowVal * 8 * 6;
        double group3 = shadowVal * (3.3) * 6;
        double group1Rashimana = 1674;
        double group2Rashimana = 1795;
        double group3Rashimana = 1931;
        double sa1 = (group1Rashimana - group1) * 4;//in sec
        double sa2 = (group2Rashimana - group2) * 4;
        double sa3 = (group3Rashimana - group3) * 4;
        double la1 = (group3Rashimana + group3) * 4;
        double la2 = (group2Rashimana + group2) * 4;
        double la3 = (group1Rashimana + group1) * 4;

        double[] timeTakenByRasiInSec = new double[]{sa1, sa2, sa3, la1, la2, la3, la3, la2, la1, sa3, sa2, sa1};
        // 2 hrs
        //  double[] timeTakenByRasiInSec = new double[]{7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0, 7200.0};
        //  double atSunriseSunPlanetLng=176.24;
        int index = (int) atSunriseSunPlanetLng / 30;
        double currDeg = atSunriseSunPlanetLng % 30;
        double remDeg = 30 - currDeg;
        double timeTaken = timeTakenByRasiInSec[index];
        double remTimeTaken = (timeTaken / 30) * remDeg;
        double remTimeTakenInhr = remTimeTaken / (60 * 60);
        ArrayList<lagna> lagnaList = new ArrayList<>();



       /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);
        Date today = sunRiseCal.getTime();

        dateFormat.format(today);*/

        Log.e("Lagna", "LagnaASC:3:" + remTimeTakenInhr + "::" + remTimeTaken + "::" + currDeg + "::" + remDeg + "::" + timeTaken);

        sunRiseCal.add(Calendar.MILLISECOND, (int) (remTimeTakenInhr * 60 * 60 * 1000));


        Calendar tmp = Calendar.getInstance(Locale.ENGLISH);
        tmp.setTimeInMillis(sunRiseCal.getTimeInMillis());
        tmp.add(Calendar.MILLISECOND, -((int) (timeTaken * 1000)));
        Log.e("Lagna", tmp.getTimeInMillis() + ":LagnaASC:4:" + ((int) (timeTaken * 1000)));
      /*  Date today1 = tmp.getTime();

        dateFormat.format(today1);
        Log.e("lagnaListAtSunrise", dateFormat.format(today1)+"::totalDeg::SR::::" + dateFormat.format(today));*/


        lagna lagnaObj = new lagna();
        lagnaObj.start = tmp;
        lagnaObj.end = sunRiseCal;
        lagnaObj.rashi = index;

        lagnaList.add(lagnaObj);

        int j = 0;

        for (int i = index + 1; i < 12; i++) {
            lagnaList.add(addLagnaTime(lagnaList.get(j++).end, i, timeTakenByRasiInSec[i]));
        }
        for (int k = 0; k <= index; k++) {
            lagnaList.add(addLagnaTime(lagnaList.get(j++).end, k, timeTakenByRasiInSec[k]));
        }
        return lagnaList;


    }


    public lagna addLagnaTime(Calendar baseCal, int rasiIndex, double timeTaken) {
        Calendar tmp = Calendar.getInstance(Locale.ENGLISH);
        tmp.setTimeInMillis(baseCal.getTimeInMillis());
        tmp.add(Calendar.MILLISECOND, ((int) (timeTaken * 1000)));
        lagna lagnaObj = new lagna();
        lagnaObj.start = baseCal;
        lagnaObj.end = tmp;
        lagnaObj.rashi = rasiIndex;
        return lagnaObj;


    }

    public String[] getAddressLatLon(Intent data, MainActivity mContext) {
        // PrefManager mPref = PrefManager.getInstance(mContext);
        try {
            LatLng latLng;
            String address = "";
            double latitude;
            double longitude;
           /* try {

                PlaceEntity var1 = SafeParcelableSerializer.deserializeFromIntentExtra(data, "selected_place", PlaceEntity.CREATOR);
                latLng = var1.getLatLng();
                Log.e("Geocoder", ":Geocoder::::" + var1.toString());
            } catch (Exception e) {
                latLng = null;
                e.printStackTrace();
            }
            if (latLng == null) {*/
            try {
                Place place = PlacePicker.getPlace(mContext, data);
                latLng = place.getLatLng();
                address = place.getAddress().toString();
            } catch (Exception e) {
                latLng = new LatLng(19.88, 86.09);
                e.printStackTrace();
            }

            String[] retVal = new String[]{"" + latLng.latitude, "" + latLng.longitude, address};
            //   }

          /*  Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
*/
/*

            mPref.setLatitude(String.valueOf(latLng.latitude));
            mPref.setLongitude(String.valueOf(latLng.longitude));
            mPref.setWeatherCityId("");
            mPref.setAreaAdmin(address);*/

//String[] retVal=new String[]{latLng.latitude,latLng.longitude,address};


            return retVal;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{};

    }

    public static class lagna {
        public Calendar start, end;
        public int rashi;
    }

    public void setLegend(Context mContext, String[] axis2Arr, ArrayList<Integer> colorVal, LinearLayout legendCntr) {
        legendCntr.removeAllViews();
        for (int i = 0; i < axis2Arr.length; i++) {
            TextView tv = new TextView(mContext);
            tv.setWidth(Helper.getInstance().dpToPx(10, mContext));
            tv.setHeight(Helper.getInstance().dpToPx(10, mContext));
            tv.setText("  ");
            tv.setBackgroundColor(colorVal.get(i));
            legendCntr.addView(tv);
            TextView tv1 = new TextView(mContext);
            String legendText = " " + axis2Arr[i] + " ";
            tv1.setText(legendText);
            tv1.setGravity(Gravity.CENTER_VERTICAL);
            tv1.setTextSize(Helper.getInstance().dpToPx(4, mContext));
            tv1.setTextColor(Color.BLACK);
            legendCntr.addView(tv1);
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return (info != null && info.isConnected());
    }


    public int dpToPx(int dp, Context mContext) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public String truncateNumberIndPer(Double number) {

        // double number = floatNumber.longValue();
        boolean isNeg = false;
        if (number < 0) {
            isNeg = true;
            number = Math.abs(number);
        }

        double thousand = 1000.0;
        double million = 1000000.0;
        double billion = 1000000000.0;
        double trillion = 1000000000000.0;
        String retVal = "";
        if ((number >= thousand) && (number < million)) {
            retVal = calculateFractionIndPer(number, thousand) + " K";
        } else if ((number >= million) && (number < billion)) {
            retVal = calculateFractionIndPer(number, million) + " M";
        } else if ((number >= billion) && (number < trillion)) {
            retVal = calculateFractionIndPer(number, billion) + " B";
        } else if (number == 0) {
            retVal = "";
        } else {
            retVal = Double.toString(number);
        }

        return isNeg ? ("(" + retVal + ")") : retVal;


    }

    private String calculateFractionIndPer(double number, double divisor) {

        String val = String.format(Locale.US, "%.4f", number / (divisor * 1.0));
        String val1 = val.substring(0, val.length() - 3);
        if (val1.contains(".0"))
            return val1.replace(".0", "");
        else
            return val1;

    }

    public String truncateNumber(Float floatNumber) {

        long number = floatNumber.longValue();
        boolean isNeg = false;
        if (number < 0) {
            isNeg = true;
            number = Math.abs(number);
        }

        long thousand = 1000L;
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        String retVal = "";
        if ((number >= thousand) && (number < million)) {
            retVal = calculateFraction(number, thousand) + " K";
        } else if ((number >= million) && (number < billion)) {
            retVal = calculateFraction(number, million) + " M";
        } else if ((number >= billion) && (number < trillion)) {
            retVal = calculateFraction(number, billion) + " B";
        } /*else if (number == 0) {
            retVal = "";
        } */ else {
            retVal = Long.toString(number);
        }

        return isNeg ? ("(" + retVal + ")") : retVal;


    }

    public String truncateNumberYaxisBot(Float floatNumber) {

        long number = floatNumber.longValue();
        boolean isNeg = false;
        if (number < 0) {
            isNeg = true;
            number = Math.abs(number);
        }

        long thousand = 1000L;
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        String retVal = "";
        if ((number >= thousand) && (number < million)) {
            retVal = calculateFraction(number, thousand) + " K";
        } else if ((number >= million) && (number < billion)) {
            retVal = calculateFraction(number, million) + " M";
        } else if ((number >= billion) && (number < trillion)) {
            retVal = calculateFraction(number, billion) + " B";
        } /*else if (number == 0) {
            retVal = "";
        } */ else {
            retVal = Long.toString(number);
        }

        return isNeg ? ("-" + retVal) : retVal;


    }

    private String calculateFraction(long number, long divisor) {

        String val = String.format(Locale.US, "%.4f", number / (divisor * 1.0));
        String val1 = val.substring(0, val.length() - 3);
        if (val1.contains(".0"))
            return val1.replace(".0", "");
        else
            return val1;

    }
    public Bitmap loadBitmapFromView(View v) {

        v.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());

       // v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
       // Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

      //  Canvas c = new Canvas(b);
      //  v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
      //  v.draw(c);
        return b;
    }

    public void combineImages(Bitmap c, Bitmap s, String fileName,Context mContext) {

        if (c != null && s != null) {
            Bitmap cs;
            int width, height;
            if (c.getWidth() > s.getWidth()) {
                width = c.getWidth();

            } else {
                width = s.getWidth();

            }
            height = c.getHeight() + s.getHeight();
            cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas comboImage = new Canvas(cs);
            comboImage.drawColor(Color.WHITE);
            comboImage.drawBitmap(c, 0f, 0f, null);
            comboImage.drawBitmap(s, 0, c.getHeight(), null);

            writeFile(cs, fileName,mContext);
        }
    }

    public void writeFile(Bitmap bitmap, String fileName,Context mContext) {
        try {
        File dir = new File(mContext.getFilesDir(), "images");
        if (!dir.exists()) dir.mkdir();
        File file = new File(dir, fileName);

            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
