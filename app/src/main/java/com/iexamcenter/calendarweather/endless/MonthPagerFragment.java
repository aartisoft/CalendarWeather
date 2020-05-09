package com.iexamcenter.calendarweather.endless;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.compat.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.DatePickerFragment;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.home.HinduTimeFrag;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.WeatherResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.sunmoon.SunMoonMainFragment;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;
import com.iexamcenter.calendarweather.wallcalendar.WallCalendarMainFragment;
import com.iexamcenter.calendarweather.weather.WeatherSlidingFragment;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by sasikanta on 11/14/2017.
 * CalendarPagerFragment
 */

public class MonthPagerFragment extends Fragment implements LocationDialog.LocationChangeEvents {
    TextView latLng, currPlace;
    private String JSONFILE = "ONTHISDAYWEATHER.txt";
    // TextView location;
    MaterialButton loc_access;
    ImageView close_loc;
    ImageView currPlaceCntr;
    RelativeLayout locPickerRl;

    TextView weather_date, weather_temp;
    ImageView weather_image;
    public static String ARG_POSITION = "CURRENT_MONTH";
    VerticalViewPager mPager;
    Resources res;
    ViewGroup rootView;
    public static int pagerCurrPos = 1200;
    public int maxCalendarYear;
    public int minCalendarYear;
    ArrayList<String> weatherToken;
    MainActivity activity;
    long sunRiseMilli;
    TextClock digitalClock;
    TextView vedicTime, digitalClockLbl, vedicTimeLbl;
    Timer timer;
    MainViewModel viewModel;
    PrefManager mPref;


    public static MonthPagerFragment newInstance() {
        return new MonthPagerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPref.isFirstUse()) {
            mPref.setFirstUse(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_calendar_endless, container, false);
        res = getResources();
        setRetainInstance(true);
        setHasOptionsMenu(true);

        CalendarWeatherApp.updateAppResource(activity.getResources(), activity);

        activity = (MainActivity) getActivity();

        maxCalendarYear = Integer.parseInt(res.getString(R.string.maxYear));
        minCalendarYear = Integer.parseInt(res.getString(R.string.minYear));
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        String le_normal_time_unit1, le_vedic_time_unit1;


        if (CalendarWeatherApp.isPanchangEng) {
            le_normal_time_unit1 =res.getString(R.string.e_normal_time_unit1);
            le_vedic_time_unit1 = res.getString(R.string.e_vedic_time_unit1);
        } else {
            le_normal_time_unit1 = res.getString(R.string.l_normal_time_unit1);
            le_vedic_time_unit1 =res.getString(R.string.l_vedic_time_unit1);
        }



        mPager = rootView.findViewById(R.id.viewpager);
        digitalClockLbl = rootView.findViewById(R.id.digitalClockLbl);
        vedicTimeLbl = rootView.findViewById(R.id.vedicTimeLbl);

        digitalClockLbl.setText(le_normal_time_unit1);
        vedicTimeLbl.setText(le_vedic_time_unit1);

        Calendar sunRiseCal = Calendar.getInstance();
        Utility.SunDetails sunrisecal = Utility.getInstance(activity).getTodaySunDetails(sunRiseCal);
        Utility.MoonDetails moonrisecal = Utility.getInstance(activity).getTodayMoonDetails(sunRiseCal);
        try {
            String sunRiseStr = sunrisecal.sunRise;
            String sunSetStr = sunrisecal.sunSet;
            String moonRiseStr = moonrisecal.moonRise;
            String moonSetStr = moonrisecal.moonSet;

            String[] tmp1 = moonRiseStr.split(" ");
            String[] tmp2 = moonSetStr.split(" ");
            String[] tmpArr1 = tmp1[0].split(":");
            String[] tmpArr2 = tmp2[0].split(":");
            int today = sunRiseCal.get(Calendar.DAY_OF_MONTH);
            String moonRiseTime = "-", moonSetTime = "-";

            moonRiseTime = tmpArr1[1] + ":" + tmpArr1[2] + " " + tmp1[1].toLowerCase();
            moonSetTime = tmpArr2[1] + ":" + tmpArr2[2] + " " + tmp2[1].toLowerCase();
            if (Integer.parseInt(tmpArr1[0]) != today) {
                moonRiseTime = tmpArr1[1] + ":" + tmpArr1[2] + " " + tmp1[1].toLowerCase() + "+";
            }
            if (Integer.parseInt(tmpArr2[0]) != today) {
                moonSetTime = tmpArr2[1] + ":" + tmpArr2[2] + " " + tmp2[1].toLowerCase() + "+";
            }


            String[] timeArr = sunRiseStr.split(" ")[0].split(":");
            sunRiseCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
            sunRiseCal.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
            sunRiseCal.set(Calendar.SECOND, 0);
            sunRiseCal.set(Calendar.MILLISECOND, 0);
            TextView sunrise, sunset, moonrise, moonset;
            sunrise = rootView.findViewById(R.id.sunrise);
            sunset = rootView.findViewById(R.id.sunset);
            moonrise = rootView.findViewById(R.id.moonrise);
            moonset = rootView.findViewById(R.id.moonset);
            LinearLayout sunCntr, moonCntr, vedicCntr, gregorianCntr;
            sunCntr = rootView.findViewById(R.id.sunCntr);
            moonCntr = rootView.findViewById(R.id.moonCntr);
            vedicCntr = rootView.findViewById(R.id.vedicCntr);
            gregorianCntr = rootView.findViewById(R.id.gregorianCntr);
            sunCntr.setOnClickListener(view -> openFragment(1));
            moonCntr.setOnClickListener(view -> openFragment(1));
            vedicCntr.setOnClickListener(view -> openFragment(2));
            gregorianCntr.setOnClickListener(view -> openFragment(2));
            sunrise.setText(sunRiseStr.toLowerCase());
            sunset.setText(sunSetStr.toLowerCase());
            moonrise.setText(moonRiseTime);
            moonset.setText(moonSetTime);

        } catch (Exception e) {
            Log.e("moonRiseStr", "moonRiseStr|" + e.getMessage());
            e.printStackTrace();
        }

        sunRiseMilli = sunRiseCal.getTimeInMillis();
        digitalClock = rootView.findViewById(R.id.digitalClock);
        vedicTime = rootView.findViewById(R.id.vedicTime);


        FrameLayout weatherCntr = rootView.findViewById(R.id.weatherCntr);
        weatherCntr.setOnClickListener(view -> openFragment(0));
        mPager.setAdapter(MonthPagerAdapter.newInstance(getChildFragmentManager(), activity));
        mPager.setCurrentItem(pagerCurrPos);

        //  Calendar cal1 = Calendar.getInstance();
        //  int currMonth = cal1.get(Calendar.MONTH);
        //  int currYear = cal1.get(Calendar.YEAR);

        weather_date = rootView.findViewById(R.id.weather_date);
        weather_temp = rootView.findViewById(R.id.weather_temp);
        // weather_city = rootView.findViewById(R.id.weather_city);
        weather_image = rootView.findViewById(R.id.weather_image);


        //  String[] displayValues1 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


        ImageView prev, next, refresh, goto_date;
        TextView month_name, wall_calendar;
        wall_calendar = rootView.findViewById(R.id.wall_calendar);
        prev = rootView.findViewById(R.id.prev);
        next = rootView.findViewById(R.id.next);
        refresh = rootView.findViewById(R.id.refresh);
        goto_date = rootView.findViewById(R.id.goto_date);
        month_name = rootView.findViewById(R.id.month_name);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
        month_name.setText(format.format(cal.getTime()));


        month_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        month_name.setMarqueeRepeatLimit(-1);
        month_name.setSelected(true);
        month_name.setSingleLine(true);

        wall_calendar.setOnClickListener(view -> openFragment(3));
        refresh.setOnClickListener(v -> mPager.setCurrentItem(pagerCurrPos));
        goto_date.setOnClickListener(v -> showDatePicker());
        prev.setOnClickListener(v -> {

            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.MONTH, mPager.getCurrentItem() - pagerCurrPos);
            if (cal2.get(Calendar.YEAR) < minCalendarYear) {
                Toast.makeText(activity, "Sorry, We are not getting calendar data.", Toast.LENGTH_SHORT).show();
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        next.setOnClickListener(v -> {

            Calendar cal22 = Calendar.getInstance();
            cal22.add(Calendar.MONTH, mPager.getCurrentItem() - pagerCurrPos);
            if (cal22.get(Calendar.YEAR) > maxCalendarYear) {
                Toast.makeText(activity, "Sorry, We are not getting calendar data.", Toast.LENGTH_SHORT).show();
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, i - pagerCurrPos);

                SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
                month_name.setText(format.format(cal.getTime()));


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        updateLocationRelated();
        long currTime = System.currentTimeMillis() / 1000;
        int lastTime = mPref.getLastWeatherUpdatedTime();
        if (Connectivity.isConnected(activity)) {
            if (lastTime >= 0 && ((currTime - lastTime) > 30 * 60L) || mPref.getWeatherCityId().isEmpty()) {
                String lat = mPref.getLatitude();//"86.52";
                String lon = mPref.getLongitude();//"21.05";
                String cityId = mPref.getWeatherCityId();
                String json1 = "{'lat':'" + lat + "','lon':'" + lon + "','cityId':'" + cityId + "'}";
                loadData(json1, Constant.WEATHER_API);
            } else {
                setOfflineWeatherData();

            }
        } else {
            setOfflineWeatherData();

        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        return rootView;


    }

    public void openFragment(int type) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment frag;
        FragmentTransaction ft = fm.beginTransaction();
        switch (type) {
            case 0:
                frag = WeatherSlidingFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_WEATHER_TAG);
                ft.addToBackStack(AppConstants.FRAG_WEATHER_TAG);
                ft.commit();
                break;
            case 1:
                frag = SunMoonMainFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_SUN_MOON_TAG);
                ft.addToBackStack(AppConstants.FRAG_SUN_MOON_TAG);
                ft.commit();
                break;
            case 2:
                frag = HinduTimeFrag.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_VEDIC_TAG);
                ft.addToBackStack(AppConstants.FRAG_VEDIC_TAG);
                ft.commit();
                break;
            case 3:
                frag = WallCalendarMainFragment.newInstance();
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_WALL_CAL_TAG);
                ft.addToBackStack(AppConstants.FRAG_WALL_CAL_TAG);
                ft.commit();
                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        timer = new Timer();

        updateVedicTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    private void updateVedicTime() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        long diff = (System.currentTimeMillis() - sunRiseMilli);
                        if (diff < 0) {
                            diff = (System.currentTimeMillis() - (sunRiseMilli - 24 * 60 * 60 * 1000));
                        }
                        // int tag=(int)vedicTime.getTag();
                        //  long diff = (diffFinal+(tag*400)) / 1000;
                        //  24 *60 sec= 1ghati
                        // 24 *60 sec= 60*60 vipal
                        // Log.e("sunRiseCalVALL", "sunRiseCalVALL::::" + diff);

                        double ghati = diff / (24 * 60.0 * 1000);
                        int ghatiVal = (int) ghati;
                        double remGhati = ghati - ghatiVal;
                        double totalVipal = remGhati * 60 * 60;
                        int palVal = (int) (totalVipal / 60.0);
                        int vipalVal = (int) (totalVipal % 60.0);
                        String ghatiStr = "" + ghatiVal;
                        if (ghatiVal < 10)
                            ghatiStr = "0" + ghatiVal;
                        String palStr = "" + palVal;
                        if (palVal < 10)
                            palStr = "0" + palVal;
                        String vipalStr = "" + vipalVal;
                        if (vipalVal < 10)
                            vipalStr = "0" + vipalVal;

                        vedicTime.setText(ghatiStr + ":" + palStr + ":" + vipalStr);

                    }
                });
            }

        }, 0, 400);

    }


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();


        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("dayofmonth", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);

        date.show(activity.getSupportFragmentManager(), "Date Picker");


    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar startCal = Calendar.getInstance();
            startCal.set(Calendar.DAY_OF_MONTH, 1);
            Calendar endCal = Calendar.getInstance();

            endCal.set(year, monthOfYear, 1);

            int monthCnt = monthsBetweenDates(startCal, endCal);


            mPager.setCurrentItem((monthCnt - 1) + pagerCurrPos);


        }
    };

    public int monthsBetweenDates(Calendar start, Calendar end) {


        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    public void updateLocationRelated() {
        currPlaceCntr = rootView.findViewById(R.id.locPickerOutline);
        loc_access = rootView.findViewById(R.id.pick_my_city);
        close_loc = rootView.findViewById(R.id.close_loc);
        locPickerRl = rootView.findViewById(R.id.locPickerRl);
        locPickerRl.setVisibility(View.GONE);

        currPlace = rootView.findViewById(R.id.currPlace);
        latLng = rootView.findViewById(R.id.latLng);
        // location = rootView.findViewById(R.id.location);

        currPlace.setSelected(true);

        currPlaceCntr.setOnClickListener(v -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            Fragment frag;
            FragmentTransaction ft = fm.beginTransaction();
            frag = fm.findFragmentByTag("locationDialog");
            if (frag != null) {
                ft.remove(frag);
            }
            LocationDialog shareDialog = LocationDialog.newInstance(activity, 0);
            shareDialog.show(ft, "locationDialog");
            shareDialog.setLocationChangeEvents(MonthPagerFragment.this);
            //  locPickerRl.setVisibility(View.VISIBLE);
            // close_loc.setVisibility(View.VISIBLE);






/*

            FragmentManager fm = activity.getSupportFragmentManager();

            Fragment frag;
            FragmentTransaction ft = fm.beginTransaction();

            frag = LocationFrag.newInstance();
            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_LOC_TAG);
            ft.addToBackStack(AppConstants.FRAG_LOC_TAG);
            ft.commit();

 */


        });
        close_loc.setOnClickListener(v -> locPickerRl.setVisibility(View.GONE));
        String subPlace = mPref.getAreaSubadmin() == null ? "" : mPref.getAreaSubadmin();
        String place = mPref.getAreaAdmin() == null ? "" : mPref.getAreaAdmin();
        String myPlace = subPlace + "" + place;
        if (!myPlace.isEmpty()) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(6);
            String latVal = df.format(Double.parseDouble(mPref.getLatitude()));
            String lngVal = df.format(Double.parseDouble(mPref.getLongitude()));
            String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
            currPlace.setText(myPlace);
            latLng.setText(latLngTxt);
        } else {
            currPlace.setText("No place name");
        }

        loc_access.setOnClickListener(v -> {

            if (Connectivity.isConnected(activity)) {
                try {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    builder.setLatLngBounds(CalendarWeatherApp.getInstance().getLatLngBounds());

                    activity.startActivityForResult(builder.build(activity), Constant.REQUEST_CODE_LOCATION_PICKER_MAP);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            } else {
                Utility.getInstance(activity).newToast("Please check internet connection.");
            }
        });

    }


    private void loadData(String profileJson, int api) {
        System.out.println("WEATHER:-:XXXXXX1");
        switch (api) {
            case Constant.WEATHER_API:
                try {


                    HttpRequestObject mReqobject = new HttpRequestObject(activity);

                    JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

                    ApiUtil.getIExamCenterBaseURLClass().getWeatherReport(body).enqueue(new Callback<WeatherResponse>() {


                        @Override
                        public void onResponse
                                (Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                            if (response.isSuccessful()) {
                                WeatherResponse res = response.body();

                                if (res.getCod() == 200) {
                                    setWeatherData(res);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(res);
                                    Utility.getInstance(activity).writeToFile(json, activity, JSONFILE);
                                    System.out.println("WEATHER:-:XXXXXX5" + json);

                                }
                            } else {
                                loadData("{}", Constant.SETUP_WEATHER_API);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            //showErrorMessage();
                            loadData("{}", Constant.SETUP_WEATHER_API);

                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case Constant.SETUP_WEATHER_API:
                System.out.println("WEATHER:-:XXXXXX4");
                weatherToken = new ArrayList<>();
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
                String weatherTokenStr = weatherToken.get(Result1);


                try {
                    ApiUtil.getWeatherBaseURLClass().getWeatherReport(mPref.getLatitude(), mPref.getLongitude(), weatherTokenStr, "metric").enqueue(new Callback<WeatherResponse>() {


                        @Override
                        public void onResponse
                                (Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                            if (response.isSuccessful()) {
                                WeatherResponse res = response.body();

                                if (res.getCod() == 200) {
                                    setWeatherData(res);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(res);
                                    Utility.getInstance(activity).writeToFile(json, activity, JSONFILE);


                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {

                            t.printStackTrace();
                        }

                    });
                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;
        }
    }

    public void setWeatherData(WeatherResponse res) {

        if (res == null && res.getName() == null) {
            Utility.getInstance(activity).newToast("Sorry, Could not get data.");
        }
        try {


            if (res.getCod() == 200) {

                Gson gson = new Gson();
                String json = gson.toJson(res);

                String cityName = res.getName();

                int date = res.getDt();

                mPref.setLastWeatherUpdatedTime(date);

                String main = res.getMain().getTemp();

                String icon = res.getWeather().get(0).getIcon();
                String desc = res.getWeather().get(0).getDescription();


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


                weather_image.setImageResource(wImg);

                String updatedTime = Utility.getInstance(activity).getUpdatedTime(date);

                weather_date.setText(updatedTime);

                String tmpdata = main + "\u00b0" + "C";

                weather_temp.setText(tmpdata);
                mPref.setWidgetTemp(main);
                mPref.setWidgetTempDesc(desc);
                int id = res.getWeather().get(0).getId();
                mPref.setWidgetTempDesc("" + id);
                mPref.setWidgetTempImg("" + wImg);
                mPref.setWeatherCity(cityName);

            } else if (!mPref.getWidgetTemp().isEmpty()) {
                setOfflineWeatherData();

            }
            mPref.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOfflineWeatherData() {
        try {
            String main = mPref.getWidgetTemp();
            if (!main.isEmpty()) {
                String updatedTime = Utility.getInstance(activity).getUpdatedTime(mPref.getLastWeatherUpdatedTime());
                if (!mPref.getWidgetTempImg().isEmpty())
                    weather_image.setImageResource(Integer.parseInt(mPref.getWidgetTempImg()));
                weather_date.setText(updatedTime);

                String tmpdata = main + "\u00b0" + "C";


                weather_temp.setText(tmpdata);
            } else {
                weather_date.setText("Connection");
                weather_temp.setText("!Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChange(String val, int type) {
        if (type == 0) {
            String[] tmp = val.split(" ", 4);
            mPref.setLatitude(tmp[1]);
            mPref.setLongitude(tmp[2]);
            mPref.setWeatherCityId("");
            mPref.setAreaAdmin(tmp[3]);
            mPref.load();
            viewModel.isLocationChanged(true);
        }

    }
}
