package com.iexamcenter.calendarweather.planet;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PlanetTransListAdapter extends RecyclerView.Adapter<PlanetTransListAdapter.ItemViewHolder> {
    Context mContext;
    private String[] planetArr = {"Sun", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto", "Mercury",};
    ArrayList<ArrayList<PlanetTransFrag.PlanetTransData>> planetDataList;
    String[] eplanetList, bara_arr, month_arr, rashiList, planetList;
    ArrayList<String> typeList;
    Resources mRes;
    String ru, ku, chalana, rashi, graha, thara, nasti;

    public PlanetTransListAdapter(Context context, ArrayList<ArrayList<PlanetTransFrag.PlanetTransData>> planetDataList) {
        mContext = context;
        this.planetDataList = planetDataList;
        typeList = new ArrayList<>();
        typeList.clear();
        mRes = mContext.getResources();

        if (!CalendarWeatherApp.isPanchangEng) {
            planetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            rashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            ru = mContext.getResources().getString(R.string.l_planet_ru);
            rashi = mContext.getResources().getString(R.string.l_planet_rashi);
            ku = mContext.getResources().getString(R.string.l_planet_ku);
            chalana = mContext.getResources().getString(R.string.l_planet_trans);
            thara = mContext.getResources().getString(R.string.l_planet_thara);
            graha = mContext.getResources().getString(R.string.l_planet_graha);
            nasti = mContext.getResources().getString(R.string.l_nasti);

            month_arr = mContext.getResources().getStringArray(R.array.l_arr_month);
            bara_arr = mContext.getResources().getStringArray(R.array.l_arr_bara);


        } else {
            planetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            ru = mContext.getResources().getString(R.string.e_planet_ru);
            rashi = mContext.getResources().getString(R.string.e_planet_rashi);
            ku = mContext.getResources().getString(R.string.e_planet_ku);
            chalana = mContext.getResources().getString(R.string.e_planet_trans);
            thara = mContext.getResources().getString(R.string.e_planet_thara);
            graha = mContext.getResources().getString(R.string.e_planet_graha);
            nasti = "nasti";

        }
        for (int i = 0, j = 0; i < planetList.length; i++) {
            if (i != 1 && i < 10) {
                planetArr[j++] = planetList[i];
            }
        }


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        layoutRes = R.layout.inflate_planet_trans_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        try {
            ArrayList<PlanetTransFrag.PlanetTransData> obj = planetDataList.get(position);
            if (position == 0) {
                typeList.clear();
            }

            int size = obj.size();
            String planet = "";
            holder.ll.removeAllViews();
            planet = planetArr[position];

            int selyear = 0;
            for (int i = 0; i < size; i++) {

                String prevRashi = rashiList[obj.get(i).prevRashi];
                String nextRashi = rashiList[obj.get(i).nextRashi];
                Calendar cal = obj.get(i).date;
                if (selyear == 0) {
                    selyear = cal.get(Calendar.YEAR);

                }
                SimpleDateFormat format = new SimpleDateFormat("EEEE, d-MMM  'at' hh:mm a", Locale.ENGLISH);
                String dateStr = format.format(cal.getTime());


                if (!CalendarWeatherApp.isPanchangEng) {
                    int DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);
                    int MONTH = cal.get(Calendar.MONTH);
                    int DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
                    int HOUR_OF_DAY = cal.get(Calendar.HOUR_OF_DAY);
                    int MINUTE = cal.get(Calendar.MINUTE);
                    String dayNo = Utility.getInstance(mContext).getDayNo("" + DAY_OF_MONTH);
                    String timeStr = getFormattedTime(HOUR_OF_DAY, MINUTE);
                    dateStr = bara_arr[DAY_OF_WEEK - 1] + ", " + dayNo + "-" + month_arr[MONTH] + " " + timeStr;
                }

                String title = prevRashi + " " + rashi + " " + ru + " " + nextRashi + " " + rashi + " " + ku + " " + chalana;
                View child = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.transit_child, null);

                addView(holder.ll, child, title, dateStr);
            }

            String nograhastr, grahastr, selYear1 = "" + selyear;
            if (!CalendarWeatherApp.isPanchangEng) {
                selYear1 = Utility.getInstance(mContext).getDayNo("" + selyear);
                grahastr = planet + " " + graha + " " + chalana + " " + selYear1;
                nograhastr = planet + " " + graha + " " + chalana +" "+nasti;
            } else {
                grahastr = planet + " " + graha + " " + chalana + " " + selYear1;
                nograhastr = planet + " " + graha + " " + chalana +" "+nasti;
            }

            if (!planet.isEmpty()) {
                holder.txt1.setVisibility(View.VISIBLE);
                holder.txt1.setText(grahastr);
            } else {
                holder.txt1.setVisibility(View.GONE);
            }
            if (size == 0) {
                holder.txt1.setVisibility(View.VISIBLE);
                holder.txt1.setText(nograhastr);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addView(LinearLayout item, View child, String titleStr, String dateStr) {

        TextView title = child.findViewById(R.id.title);
        TextView date = child.findViewById(R.id.date);
        title.setText(titleStr);
        date.setText(dateStr);
        item.addView(child);
    }

    @Override
    public int getItemCount() {
        return planetDataList.size();
    }

    private String getFormattedTime(int calHour, int calMin) {

        String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
        String prefixTime = "";
        if ((calHour > 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
            prefixTime = mRes.getString(R.string.l_time_night);
        }
        if (calHour >= 4 && calHour < 9) {
            prefixTime = mRes.getString(R.string.l_time_prattha);
        } else if (calHour >= 9 && calHour < 16) {
            prefixTime = mRes.getString(R.string.l_time_diba);
        } else if (calHour >= 16 && calHour < 19) {
            prefixTime = mRes.getString(R.string.l_time_sandhya);
        }
        if (calHour > 12) {
            calHour = calHour - 12;
        }
        String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

        String ldate;
        ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);
        return ldate;

    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;
        View div;
        LinearLayout ll;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            div = itemView.findViewById(R.id.div);
            txt1 = itemView.findViewById(R.id.txt1);

        }


    }
}
