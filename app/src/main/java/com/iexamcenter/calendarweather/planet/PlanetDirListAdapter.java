package com.iexamcenter.calendarweather.planet;

import android.content.Context;
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
import java.util.Date;
import java.util.Locale;

public class PlanetDirListAdapter extends RecyclerView.Adapter<PlanetDirListAdapter.ItemViewHolder> {
    Context mContext;
    private String[] planetArr = {"Sun", "Moon", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Urance", "Neptune", "Pluto", "Mercury"};
    ArrayList<ArrayList<String>> planetDataList;
    String[] planetList, month_arr, bara_arr;
    ArrayList<String> typeList = new ArrayList<>();
    String graha, planet_start, planet_end, bakra_samaya, bakra_dina, bakraStr, nasti;

    public PlanetDirListAdapter(Context context, ArrayList<ArrayList<String>> mplanetDataList) {
        mContext = context;
         planetDataList = mplanetDataList;
        //   eplanetList = mContext.getResources().getStringArray(R.array.e_planet_arr);
        // lplanetList = mContext.getResources().getStringArray(R.array.l_planet_arr);
        if (!CalendarWeatherApp.isPanchangEng) {
            graha = mContext.getResources().getString(R.string.l_planet_graha);

            planetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            planet_start = mContext.getResources().getString(R.string.l_planet_start);
            planet_end = mContext.getResources().getString(R.string.l_planet_end);
            bakra_samaya = mContext.getResources().getString(R.string.l_planet_bakra_samaya);
            bakra_dina = mContext.getResources().getString(R.string.l_dina);
            bakraStr = mContext.getResources().getString(R.string.l_planet_retro);
            nasti = mContext.getResources().getString(R.string.l_nasti);
            month_arr = mContext.getResources().getStringArray(R.array.l_arr_month);
            bara_arr = mContext.getResources().getStringArray(R.array.l_arr_bara);
        } else {
            graha = mContext.getResources().getString(R.string.e_planet_graha);
            planetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            planet_start = mContext.getResources().getString(R.string.e_planet_start);
            planet_end = mContext.getResources().getString(R.string.e_planet_end);
            bakra_samaya = mContext.getResources().getString(R.string.e_planet_asta_samaya);
            bakra_dina = mContext.getResources().getString(R.string.e_dina);
            bakraStr = "bakra";
            nasti = "nasti";
        }
        for (int i = 0, j = 0; i < planetList.length; i++) {
            if (i > 1 && i < 10) {
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
            ArrayList<String> obj1 = planetDataList.get(position);
            if (position == 0) {
                typeList.clear();
            }
            int size = obj1.size();
            String obj;


            String yearval="0",type="0";
            holder.ll.removeAllViews();

            for (int i = 0; i < size; i++) {
                obj = obj1.get(i);

            String[] rangeDateArr = obj.split(" ");
            String minRange = rangeDateArr[0];
            String maxRange = rangeDateArr[1];
             type = rangeDateArr[2];
            SimpleDateFormat formatterRange = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);

            Date minRangeDate = formatterRange.parse(minRange);
            Date maxRangeDate = formatterRange.parse(maxRange);
            long timeInMilli = maxRangeDate.getTime() - minRangeDate.getTime();
            long days = (timeInMilli / (24 * 60 * 60 * 1000));

            SimpleDateFormat formatterRange1 = new SimpleDateFormat("EEEE, d-MMM", Locale.ENGLISH);
            SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
             yearval = formatterYear.format(minRangeDate);


            String dayNo, startDateStr, endDateStr;
            String dayStr1 = "Retrograde Duration = " + days + " Days", title1Str = "Starts on " + formatterRange1.format(minRangeDate), title2Str = "Ends on " + formatterRange1.format(maxRangeDate), dayStr = "" + days;



            int DAY_OF_MONTH, MONTH, DAY_OF_WEEK;
            if (!CalendarWeatherApp.isPanchangEng) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(minRangeDate);
                DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);
                MONTH = cal.get(Calendar.MONTH);
                DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
                dayNo = Utility.getInstance(mContext).getDayNo("" + DAY_OF_MONTH);
                startDateStr = bara_arr[DAY_OF_WEEK - 1] + ", " + dayNo + "-" + month_arr[MONTH];

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(maxRangeDate);
                DAY_OF_MONTH = cal1.get(Calendar.DAY_OF_MONTH);
                MONTH = cal1.get(Calendar.MONTH);
                DAY_OF_WEEK = cal1.get(Calendar.DAY_OF_WEEK);
                dayNo = Utility.getInstance(mContext).getDayNo("" + DAY_OF_MONTH);
                endDateStr = bara_arr[DAY_OF_WEEK - 1] + ", " + dayNo + "-" + month_arr[MONTH];

                dayStr = Utility.getInstance(mContext).getDayNo("" + dayStr);
                title1Str = bakraStr + " " + planet_start + " " + startDateStr;
                title2Str = bakraStr + " " + planet_end + " " + endDateStr;
                dayStr1 = bakra_samaya + " = " + dayStr + " " + bakra_dina;
                yearval = Utility.getInstance(mContext).getDayNo(yearval);
            }


          //  holder.txt2.setText(title1Str);
           // holder.txt3.setText(title2Str);

                View child = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.asta_child, null);

                addView(holder.ll, child, title1Str, title2Str,   dayStr1);

            //   holder.txt2.setText("Starts on "+formatterRange1.format(minRangeDate));
            //  holder.txt3.setText("Ends on "+formatterRange1.format(maxRangeDate));
            }


           // int index = Integer.parseInt(position) - 1;
            if (size!=0) {
                holder.txt1.setVisibility(View.VISIBLE);

                holder.txt1.setText(planetArr[position] + " " + graha + " " + bakraStr + " " + yearval);
               // typeList.add(type);
            } else {
                holder.txt1.setText(planetArr[position] + " " + graha + " " + bakraStr + " " + nasti);
               // holder.txt1.setVisibility(View.GONE);
            }


            //holder.txt4.setText(dayStr1);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void addView(LinearLayout item, View child, String title1Str, String title2Str, String days) {

        TextView title1 = child.findViewById(R.id.title1);
        TextView title2 = child.findViewById(R.id.title2);
        TextView date = child.findViewById(R.id.date);
        title1.setText(title1Str);
        title2.setText(title2Str);
        date.setText(days);
        date.setVisibility(View.VISIBLE);
        item.addView(child);
    }
    @Override
    public int getItemCount() {
        return planetDataList.size();
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
