package com.iexamcenter.calendarweather.planet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlanetInfoListAdapter extends RecyclerView.Adapter<PlanetInfoListAdapter.ItemViewHolder> {
    Context mContext;
    String[] eplanetList, lplanetList, planetList, lrashiList, rashiList;
    ArrayList<PlanetInfoFrag.PlanetData> planetDataList;

    public PlanetInfoListAdapter(Context context, ArrayList<PlanetInfoFrag.PlanetData> planetDataList) {
        mContext = context;
        this.planetDataList = planetDataList;
        if (!CalendarWeatherApp.isPanchangEng) {
            planetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            rashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
        } else {

            planetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
        }

        //  lrashiList = mContext.getResources().getStringArray(R.array.l_rasi_kundali_arr);

        //  erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        layoutRes = R.layout.inflate_planet_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        PlanetInfoFrag.PlanetData obj = planetDataList.get(position);
        //   String txt1 = obj.planet ;//+ "(" + obj.houseNo + ")";
        //  String txt2 = obj.latLng;
        // String txt3 = "" + obj.deg;

        String[] latLng = getLatLng(obj.deg);

        holder.txt1.setText(planetList[(obj.planetType - 1)] + "(" + latLng[0] + ")");

        // holder.txt2.setText(latLng[0]);
        holder.txt3.setText(latLng[1] + "°");
        holder.direction.setImageResource(obj.direction == 0 ? R.drawable.ic_rotate_right_black_24dp : R.drawable.ic_rotate_left_black_24dp);
    }

    @Override
    public int getItemCount() {
        return planetDataList.size();
    }

    public String[] getLatLng(double deg) {
        String[] strArr;

        DecimalFormat f = new DecimalFormat("##.00");
        String degStr = f.format(deg);
        String deg1Str;
        String deg2Str;


        if (deg >= 360) {
            deg = deg - 360;
        }
        int val = Integer.parseInt((("" + deg).split("\\.")[0]));

        int index = val / 30;
        int remDeg = val % 30;
        double fractionInSec = (deg - val) * 3600;
        long min = Math.round(fractionInSec) / 60;
        long sec = Math.round(fractionInSec) % 60;
        String remDegStr;
        String minStr;
        String secStr;
        String latLng;
        if (CalendarWeatherApp.isPanchangEng) {
            remDegStr = "" + remDeg;
            minStr = "" + min;
            secStr = "" + sec;
            latLng = remDegStr + "° " + rashiList[index] + " " + minStr + "′" + " " + secStr + "′′";
            deg1Str = degStr.split("\\.")[0];
            deg2Str = degStr.split("\\.")[1];

            degStr = deg1Str + "." + deg2Str;

        } else {
            deg1Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[0]);
            deg2Str = Utility.getInstance(mContext).getDayNo("" + degStr.split("\\.")[1]);
            remDegStr = Utility.getInstance(mContext).getDayNo("" + remDeg);
            minStr = Utility.getInstance(mContext).getDayNo("" + min);
            secStr = Utility.getInstance(mContext).getDayNo("" + sec);
            Log.e("ViewHolde", rashiList.length + "::ViewHolde::" + index + "::" + deg);
            latLng = remDegStr + "° " + rashiList[index] + " " + minStr + "′" + " " + secStr + "′′";
            degStr = deg1Str + "." + deg2Str;
        }
        strArr = new String[]{latLng, degStr};

        return strArr;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1, txt3;
        public ImageView direction;

        public ItemViewHolder(View itemView) {
            super(itemView);
            direction = itemView.findViewById(R.id.direction);
            txt1 = itemView.findViewById(R.id.txt1);
            // txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
        }


    }
}
