package com.iexamcenter.calendarweather.weather;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.response.ForecastResponse;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.SunMoonCalculator;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ItemViewHolder> {
    private ArrayList<ForecastResponse.WeatherList> mItems;
    private Context mContext;
    private Gson gson;
    ColorStateList csl;
    String mLat, mLng;
    int type;
    PrefManager mPref;
    String timeZoneStr;

    public ForecastAdapter(Context context, ArrayList<ForecastResponse.WeatherList> arrayList, int type, String lat, String lng, String mTimeZoneStr) {
        mItems = arrayList;
        mContext = context;
        timeZoneStr = mTimeZoneStr;
        this.type = type;
        gson = new Gson();
        mLat = lat;
        mLng = lng;
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
    /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_facebook_page, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_forecast, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder ll, int position) {
        ForecastResponse.WeatherList obj = mItems.get(position);


        String pressure = obj.getPressure();
        String icon = obj.getWeather().get(0).getIcon();

        String temp = obj.getTemp().getDay();
        String desc = obj.getWeather().get(0).getDescription();
        String windSpeed = obj.getSpeed();
        String windDeg = obj.getDeg();
        String humidity = obj.getHumidity();
        int date = obj.getDt();
        ll.tvPressure.setText(pressure);

        String IMAGE_URL = "http://openweathermap.org/img/w/" + icon + ".png";
        if (icon.contains("01d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_01d);
        } else if (icon.contains("01n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_01n);
        } else if (icon.contains("02d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_02d);
        } else if (icon.contains("02n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_02n);
        } else if (icon.contains("03d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_03d);
        } else if (icon.contains("03n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_03n);
        } else if (icon.contains("04d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_04d);
        } else if (icon.contains("04n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_04n);
        } else if (icon.contains("09d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_09d);
        } else if (icon.contains("09n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_09n);
        } else if (icon.contains("10d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_10d);
        } else if (icon.contains("10n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_10n);
        } else if (icon.contains("11d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_11d);
        } else if (icon.contains("11n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_11n);
        } else if (icon.contains("50d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_50d);
        } else if (icon.contains("50n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_50n);
        } else if (icon.contains("13d")) {
            ll.imgWeather.setImageResource(R.drawable.ic_13d);
        } else if (icon.contains("13n")) {
            ll.imgWeather.setImageResource(R.drawable.ic_13n);
        }

        //  ll.imgWind.setRotation(Float.parseFloat(windDeg));
        // Utility.getInstance(getActivity()).setWeatherImageView(IMAGE_URL, IMAGE_URL, imgWeather);
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

        ll.imgWind.startAnimation(animSet);
        if (type == 0) {

            Double d1 = Double.parseDouble(temp) * 1.8 + 32;
            Double d2 = Double.parseDouble(humidity);
            //* 1.8 + 32;

            String tmp = "" + Math.round(d1 * 100.0) / 100.0;
            String tmp1 = "" + Math.round(d2 * 100.0) / 100.0;
            ll.tvTemp.setText(tmp + "\u00b0" + "F");
            ll.tvHumidity.setText(tmp1 + "%");

        } else {

            Double d1 = Double.parseDouble(temp);
            Double d2 = Double.parseDouble(humidity);

            temp = "" + Math.round(d1 * 100.0) / 100.0;
            humidity = "" + Math.round(d2 * 100.0) / 100.0;

            ll.tvTemp.setText(temp + "\u00b0" + "C");
            ll.tvHumidity.setText(humidity + "%");
        }

        ll.tvWeatherDesc.setText(desc);

        // tvPressure.setText("Temp" + temp_min + "\u00b0" + "C" + "-" + temp_max + "\u00b0" + "C");

        Double w1 = Double.parseDouble(windSpeed) * 3.6;

        String tempWind = "" + Math.round(w1 * 100.0) / 100.0;

        ll.tvWindVal.setText(tempWind + " km/h");
        ll.tvUpdatedAt.setText(Utility.getInstance(mContext).getDateTime("" + date));
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        long time = Long.parseLong("" + date) * 1000L;

        calendar.setTimeInMillis(time);

        try {

            int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH), h = calendar.get(Calendar.HOUR), m = calendar.get(Calendar.MINUTE), s = calendar.get(Calendar.SECOND);
            double obsLon = Double.parseDouble(mLng) * SunMoonCalculator.DEG_TO_RAD, obsLat = Double.parseDouble(mLat) * SunMoonCalculator.DEG_TO_RAD;
            SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 12, 0, 0, obsLon, obsLat);

            smc.calcSunAndMoon();
            String sunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
            String sunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
            //  ll.tvHumidity.setText(sunRise);
            ll.tvSunrise.setText(sunRise);
            ll.tvSunset.setText(sunSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {


        private TextView tvCityName, tvTemp, tvWeatherDesc, tvHumidity, tvPressure, tvWindVal, tvUpdatedAt, tvSunrise, tvSunset;
        ImageView imgWind, imgWeather;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.my_city);
            tvTemp = itemView.findViewById(R.id.temp);
            tvSunrise = itemView.findViewById(R.id.sunriseVal);
            tvSunset = itemView.findViewById(R.id.sunsetVal);
            tvWeatherDesc = itemView.findViewById(R.id.weatherDesc);
            tvHumidity = itemView.findViewById(R.id.humidityVal);
            tvPressure = itemView.findViewById(R.id.pressureVal);
            tvWindVal = itemView.findViewById(R.id.windDesc);
            imgWind = itemView.findViewById(R.id.wind);
            imgWeather = itemView.findViewById(R.id.weather);
            tvUpdatedAt = itemView.findViewById(R.id.lastupdateVal);

        }

    }
}
