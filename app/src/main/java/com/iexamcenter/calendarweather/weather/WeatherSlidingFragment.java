package com.iexamcenter.calendarweather.weather;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.ForecastResponse;
import com.iexamcenter.calendarweather.response.WeatherResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherSlidingFragment extends Fragment implements LocationDialog.LocationChangeEvents {

    LinearLayout forecastlist;
    FrameLayout weathercont, locationContainer;
    static String timeZoneStr = "";
    ImageView imgWeather, close_city;
    private TextView tvWindDirVal, tvCityName, tvTemp, tvWeatherDesc, tvHumidity, tvPressure, tvWindVal, tvUpdatedAt;
    private String JSONFILE = "ONTHISDAYWEATHER.txt";
    private String JSONFILEFORCAST = "ONTHISDAYWEATHERFORCAST.txt";
    MaterialButton loc_access, type_city;
    PrefManager mPref;
    TextView converter;
    ImageView cel_farh;
    String temp, humidity;
    ArrayList<String> weatherToken;
    boolean authenticatedInternet = false;
    private String lat, lng;
    private MainActivity mContext;
    View rootView;
    MainViewModel viewModel;

    public static WeatherSlidingFragment newInstance() {

        return new WeatherSlidingFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
        // mContext.unregisterReceiver(mUpdateLocationReceiver);
    }

    public void cel2Far(View v) {
        if (mPref.getLatitude() == null) {
            Toast.makeText(mContext, "Conversion failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        int type = (int) v.getTag();
        if (temp == null) {
            temp = "0";
        }
        if (humidity == null) {
            humidity = "0";
        }
        Double d1 = Double.parseDouble(temp) * 1.8 + 32;

        String tmp = "" + Math.round(d1 * 100.0) / 100.0;

        if (type == 1) {
            tvTemp.setText(tmp + "°F");
            converter.setText("Celcious");
            cel_farh.setImageResource(R.drawable.ic_celcious);
            converter.setTag(0);
            cel_farh.setTag(0);
        } else {
            converter.setText("Fahrenheit");
            tvTemp.setText(temp + "°C");
            cel_farh.setImageResource(R.drawable.ic_fahrenheit);
            converter.setTag(1);
            cel_farh.setTag(1);
        }
    }

    public void updateLocationRelated() {
        if (Connectivity.isConnected(mContext)) {
            locationContainer.setVisibility(View.GONE);
            mPref.isWeatherCurrCity(true);
            String json = "{'setup':'x'}";
            loadData(json, Constant.WEATHER_API);
            loadData(json, Constant.FORCAST_API);
        }
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            Log.e("WEATHERLOAD", "WEATHERLOADED");

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
            timeZoneStr = TimeZone.getDefault().getID();
            lat = mPref.getLatitude();
            lng = mPref.getLongitude();
            mContext.getSupportActionBar().setTitle("Weather & Forecast");
            // mContext.getSupportActionBar().setSubtitle("");

            weathercont = rootView.findViewById(R.id.mainll);
            tvWindDirVal = rootView.findViewById(R.id.windDirVal);
            loc_access = rootView.findViewById(R.id.pick_my_city);
            type_city = rootView.findViewById(R.id.type_my_city);
            locationContainer = rootView.findViewById(R.id.myLocationContainer);
            close_city = rootView.findViewById(R.id.close_my_city);
            tvCityName = rootView.findViewById(R.id.my_city);
            tvTemp = rootView.findViewById(R.id.temp);
            tvWeatherDesc = rootView.findViewById(R.id.weatherDesc);
            tvHumidity = rootView.findViewById(R.id.humidityVal);
            tvPressure = rootView.findViewById(R.id.pressureVal);
            tvWindVal = rootView.findViewById(R.id.windDesc);
            imgWeather = rootView.findViewById(R.id.weather);
            ImageView city_img = rootView.findViewById(R.id.my_city_img);

           /* mUpdateLocationFilter = new IntentFilter("LOCATION_CHANGED");

            mUpdateLocationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        updateLocationRelated();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };*/

            // mContext.registerReceiver(mUpdateLocationReceiver, mUpdateLocationFilter);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            viewModel.getLocationChanged().observe(getViewLifecycleOwner(), isChng -> {

                if (isChng) {
                    try {
                        updateLocationRelated();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });


            ImageView refresh1 = rootView.findViewById(R.id.my_refresh);
            refresh1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setEnabled(false);
                    Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
                        if (Connectivity.isConnected(mContext)) {
                            String json = "{'setup':'x'}";
                            loadData(json, Constant.WEATHER_API);
                            loadData(json, Constant.FORCAST_API);
                            subscriber.onNext(2);
                        } else {
                            subscriber.onNext(1);
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integer -> {
                                if (integer == 1) {
                                    Utility.getInstance(mContext).newToast("Please check internet connection");
                                } else if (integer == 2) {
                                    Utility.getInstance(mContext).newToast("Updating..");
                                }
                                v.setEnabled(true);
                            });

                }
            });
            close_city.setOnClickListener(v -> locationContainer.setVisibility(View.GONE));
            tvCityName.setOnClickListener(view -> {

                if (locationContainer.getVisibility() == View.VISIBLE) {
                    locationContainer.setVisibility(View.GONE);
                } else {

                    locationContainer.setVisibility(View.VISIBLE);
                }


            });
            city_img.setOnClickListener(view -> {

                FragmentManager fm = mContext.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                frag = fm.findFragmentByTag("locationDialog");
                if (frag != null) {
                    ft.remove(frag);
                }
                LocationDialog shareDialog = LocationDialog.newInstance(mContext, 0);
                shareDialog.show(ft, "locationDialog");
                shareDialog.setLocationChangeEvents(WeatherSlidingFragment.this);




                /*
                if (locationContainer.getVisibility() == View.VISIBLE) {
                    locationContainer.setVisibility(View.GONE);
                } else {

                    locationContainer.setVisibility(View.VISIBLE);
                }*/


            });

            forecastlist = rootView.findViewById(R.id.forecastlist);
            converter = rootView.findViewById(R.id.converter);
            cel_farh = rootView.findViewById(R.id.cel_farh);
            converter.setTag(1);
            cel_farh.setTag(1);

            converter.setOnClickListener(v -> cel2Far(v));
            tvUpdatedAt = rootView.findViewById(R.id.lastupdateVal);
            TextView forecast = rootView.findViewById(R.id.forecast);
            forecast.setOnClickListener(view -> openForecast());
            ImageView tvForecast = rootView.findViewById(R.id.more);


            tvForecast.setOnClickListener(v -> openForecast());

            if (mPref.getLatitude() != null && mPref.getLongitude() != null) {
                weathercont.setAlpha(1f);
                locationContainer.setVisibility(View.GONE);
                close_city.setVisibility(View.VISIBLE);

            } else {
                weathercont.setAlpha(0.2f);
                locationContainer.setVisibility(View.VISIBLE);
                close_city.setVisibility(View.GONE);


            }


           /* type_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Connectivity.isConnected(mContext)) {
                        try {

                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                            .build(mContext);
                            mContext.startActivityForResult(intent, Constant.REQUEST_CODE_LOCATION_PICKER);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utility.getInstance(mContext).newToast("Please check internet connection.");
                    }
                }
            });*/
            loc_access.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


/*


                    if (Connectivity.isConnected(mContext)) {

                        try {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            builder.setLatLngBounds(CalendarWeatherApp.getInstance().getLatLngBounds());
                            mContext.startActivityForResult(builder.build(mContext), Constant.REQUEST_CODE_LOCATION_PICKER_MAP);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utility.getInstance(mContext).newToast("Please check internet connection.");
                    }
*/
                }
            });

            if (Connectivity.isConnected(mContext)) {
                mPref.isWeatherCurrCity(true);

                String json = "{'setup':'x'}";
                loadData(json, Constant.WEATHER_API);
                loadData(json, Constant.FORCAST_API);

            } else {

                Gson gson = new Gson();
                WeatherResponse res1 = gson.fromJson(Utility.getInstance(mContext).readFromFile(mContext, JSONFILE), WeatherResponse.class);
                ForecastResponse res2 = gson.fromJson(Utility.getInstance(mContext).readFromFile(mContext, JSONFILEFORCAST), ForecastResponse.class);
                if (res1 != null && res2 != null) {
                    getWeatherData(res1);
                    getForcastData(res2);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getForcastData(ForecastResponse res2) {
        ArrayList<ForecastResponse.WeatherList> dataList = res2.getList();
        for (ForecastResponse.WeatherList objList : dataList) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(mContext);
            View cur_deal = mInflater.inflate(R.layout.inflate_forecastgrid, forecastlist, false);
            TextView tempmax = cur_deal.findViewById(R.id.tempmax);
            TextView tempmin = cur_deal.findViewById(R.id.tempmin);
            TextView tvUpdatedAt1 = cur_deal.findViewById(R.id.lastupdateVal);
            int date1 = objList.getDt();
            tvUpdatedAt1.setText(Utility.getInstance(mContext).getDateTimeForecastGrid("" + date1));

            tempmax.setText("" + Math.round(Double.parseDouble(objList.getTemp().getMax())) + "°C");
            tempmin.setText("" + Math.round(Double.parseDouble(objList.getTemp().getMin())) + "°C");
            String icon = objList.getWeather().get(0).getIcon();
            ImageView imgWeather1 = cur_deal.findViewById(R.id.weather);
            setImgWeather(imgWeather1, icon);

            forecastlist.addView(cur_deal);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.weather_slide_new, container, false);

        return rootView;
    }

    public void openForecast() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("FORECAST");
        if (prev != null) {
            ft.remove(prev);
        }
        final DialogFragment filterDialog = ForecastDialog.newInstance(timeZoneStr);
        Bundle args = new Bundle();
        args.putInt("CONVERTER", (int) converter.getTag());
        args.putString("LAT", lat);
        args.putString("LNG", lng);
        filterDialog.setArguments(args);
        filterDialog.setCancelable(true);
        filterDialog.show(ft, "FORECAST");

    }

    private void setImgWeather(final ImageView imgWeather1, String icon) {
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


    private void getWeatherData(WeatherResponse res) {
        try {


            String cityName = res.getName();
            int date = res.getDt();
            temp = res.getMain().getTemp();
            int cityId = res.getId();
            humidity = res.getMain().getHumidity();
            String pressure = res.getMain().getPressure();
            String windDeg = res.getWind().getDeg();
            if (windDeg != null) {
                Float windDegVal = Float.parseFloat(windDeg);
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);

                String windDegStr;
                if (windDegVal >= 11.25 && windDegVal < 33.75) {
                    windDegStr = "NNE";
                } else if (windDegVal >= 33.75 && windDegVal < 56.25) {
                    windDegStr = "NE";
                } else if (windDegVal >= 56.25 && windDegVal < 78.75) {
                    windDegStr = "ENE";
                } else if (windDegVal >= 78.75 && windDegVal < 101.25) {
                    windDegStr = "E";
                } else if (windDegVal >= 101.25 && windDegVal < 123.75) {
                    windDegStr = "ESE";
                } else if (windDegVal >= 123.75 && windDegVal < 146.25) {
                    windDegStr = "SE";
                } else if (windDegVal >= 146.25 && windDegVal < 168.75) {
                    windDegStr = "SSE";
                } else if (windDegVal >= 168.75 && windDegVal < 191.25) {
                    windDegStr = "S";
                } else if (windDegVal >= 191.25 && windDegVal < 213.75) {
                    windDegStr = "SSW";
                } else if (windDegVal >= 213.75 && windDegVal < 236.25) {
                    windDegStr = "SW";
                } else if (windDegVal >= 236.25 && windDegVal < 258.75) {
                    windDegStr = "WSW";
                } else if (windDegVal >= 258.75 && windDegVal < 281.25) {
                    windDegStr = "W";
                } else if (windDegVal >= 258.75 && windDegVal < 303.75) {
                    windDegStr = "WNW";
                } else if (windDegVal >= 303.75 && windDegVal < 326.25) {
                    windDegStr = "NW";
                } else if (windDegVal >= 326.25 && windDegVal < 348.75) {
                    windDegStr = "NNW";
                } else {
                    windDegStr = "N";
                }
                tvWindDirVal.setText(df.format(windDegVal) + "° (" + windDegStr + ")");


                AnimationSet animSet = new AnimationSet(true);
                animSet.setInterpolator(new DecelerateInterpolator());
                animSet.setFillAfter(true);
                animSet.setFillEnabled(true);

                final RotateAnimation animRotate = new RotateAnimation(0.0f, Float.parseFloat(windDeg),
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                animRotate.setDuration(2000);
                animRotate.setFillAfter(true);
                animSet.addAnimation(animRotate);
            }
            String windSpeed = res.getWind().getSpeed();
            String icon = res.getWeather().get(0).getIcon();
            String desc = res.getWeather().get(0).getDescription();
            String country = res.getSys().getCountry();
            tvPressure.setText(pressure);
            tvCityName.setText("Weather Station: " + cityName + "(" + country + ")");

            setImgWeather(imgWeather, icon);

            if (temp.contains(".")) {
                Double d1 = Double.parseDouble(temp);
                temp = "" + Math.round(d1 * 100.0) / 100.0;
            }


            Double d2 = Double.parseDouble(humidity);


            String humidity = "" + Math.round(d2 * 100.0) / 100.0;


            Double d3 = Double.parseDouble(windSpeed) * 3.6;


            String tmpWind = "" + Math.round(d3 * 100.0) / 100.0;


            tvTemp.setText(temp + "\u00b0" + "C");
            tvWeatherDesc.setText(desc);
            tvHumidity.setText(humidity + "%");

            tvWindVal.setText(tmpWind + " km/h");


            tvUpdatedAt.setText("Updated at: " + Utility.getInstance(mContext).getDateTime("" + date));
            mPref.setWeatherCityId("" + cityId);
            if (mPref.isWeatherCurrCity()) {
                mPref.setWidgetTemp(temp);
                mPref.setWeatherCity(cityName);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date * 1000L);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String profileJson, int api) {
        Log.d("xxx", "loadData:loading from API" + api);
        Random r1 = new Random();
        int Low1 = 0;
        int High1 = weatherToken.size();
        int Result1 = r1.nextInt(High1 - Low1) + Low1;
        String weatherTokenStr = weatherToken.get(Result1);
        String lat = mPref.getLatitude();//"86.52";
        String lon = mPref.getLongitude();//"21.05";
        String cityId = mPref.getWeatherCityId();

        switch (api) {
            case Constant.FORCAST_API:
                try {
                    profileJson = "{'lat':'" + lat + "','lon':'" + lon + "','cityId':'" + cityId + "'}";
                    HttpRequestObject mReqobject = new HttpRequestObject(mContext);

                    JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
                    System.out.println("WEATHER:-:" + jsonHeader.toString());
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

                    ApiUtil.getIExamCenterBaseURLClass().getForecastReport(body).enqueue(new Callback<ForecastResponse>() {


                        @Override
                        public void onResponse
                                (Call<ForecastResponse> call, retrofit2.Response<ForecastResponse> response) {
                            if (response.isSuccessful()) {
                                ForecastResponse res = response.body();
                                if (res == null && res.getCity() == null) {
                                    Utility.getInstance(mContext).newToast("Sorry, Couldnot get data.");
                                }
                                getForcastData(res);
                                Gson gson = new Gson();
                                String json = gson.toJson(res);
                                Utility.getInstance(mContext).writeToFile(json, mContext, JSONFILEFORCAST);
                            } else {
                                loadData("{}", Constant.SETUP_FORCAST_API);
                            }
                        }

                        @Override
                        public void onFailure(Call<ForecastResponse> call, Throwable t) {
                            loadData("{}", Constant.SETUP_FORCAST_API);
                            Log.d("xxx", "error loading from APIFORECAST");
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
                                WeatherResponse res = response.body();
                                if (res == null && res.getName() == null) {
                                    Utility.getInstance(mContext).newToast("Sorry, Could not get data.");
                                }

                                getWeatherData(res);
                                if (res.getCod() == 200) {

                                    Gson gson = new Gson();
                                    String json = gson.toJson(res);
                                    Utility.getInstance(mContext).writeToFile(json, mContext, JSONFILE);

                                } else if (mPref.getLatitude() != null && mPref.getLongitude() != null) {
                                    authenticatedInternet = true;

                                }
                            } else {
                                loadData("{}", Constant.SETUP_WEATHER_API);
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {

                            loadData("{}", Constant.SETUP_WEATHER_API);

                            Log.d("xxx", "error loading from APIWEATHER");
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.SETUP_WEATHER_API:
                try {
                    ApiUtil.getWeatherBaseURLClass().getWeatherReport(mPref.getLatitude(), mPref.getLongitude(), weatherTokenStr, "metric").enqueue(new Callback<WeatherResponse>() {


                        @Override
                        public void onResponse
                                (Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                            if (response.isSuccessful()) {
                                WeatherResponse res = response.body();
                                if (res == null && res.getName() == null) {
                                    Utility.getInstance(mContext).newToast("Sorry, Could not get data.");
                                }

                                getWeatherData(res);
                                if (res.getCod() == 200) {

                                    Gson gson = new Gson();
                                    String json = gson.toJson(res);
                                    Utility.getInstance(mContext).writeToFile(json, mContext, JSONFILE);

                                } else if (mPref.getLatitude() != null && mPref.getLongitude() != null) {
                                    authenticatedInternet = true;

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            //showErrorMessage();
                            t.printStackTrace();
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.SETUP_FORCAST_API:
                try {

                    ApiUtil.getWeatherBaseURLClass().getForecastReport(mPref.getLatitude(), mPref.getLongitude(), 16, weatherTokenStr, "metric").enqueue(new Callback<ForecastResponse>() {


                        @Override
                        public void onResponse
                                (Call<ForecastResponse> call, retrofit2.Response<ForecastResponse> response) {
                            if (response.isSuccessful()) {
                                ForecastResponse res = response.body();
                                if (res == null && res.getCity() == null) {
                                    Utility.getInstance(mContext).newToast("Sorry, Couldnot get data.");
                                }
                                getForcastData(res);
                                Gson gson = new Gson();
                                String json = gson.toJson(res);
                                Utility.getInstance(mContext).writeToFile(json, mContext, JSONFILEFORCAST);
                            }
                        }

                        @Override
                        public void onFailure(Call<ForecastResponse> call, Throwable t) {
                            t.printStackTrace();
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    @Override
    public void onLocationChange(String val, int type) {
        if(type==0) {
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
