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
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlanetAstaListAdapter extends RecyclerView.Adapter<PlanetAstaListAdapter.ItemViewHolder> {
    Context mContext;
    private String[] planetList,planetArr = {"Moon", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Urance", "Neptune", "Pluto", "Mercury"};
    ArrayList<ArrayList<EphemerisEntity>> planetDataList;
    String[] eplanetList, lplanetList,month_arr,bara_arr;
    ArrayList<String> typeList;
    String planet_start,planet_end,asta_samaya,asta_dina,asta,nasti;

    public PlanetAstaListAdapter(Context context, ArrayList<ArrayList<EphemerisEntity>> planetDataList) {
        mContext = context;
        this.planetDataList = planetDataList;
        eplanetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
        lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
        typeList = new ArrayList<>();
        typeList.clear();

        if (!CalendarWeatherApp.isPanchangEng) {
            planetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            planet_start = mContext.getResources().getString(R.string.l_planet_start);
            planet_end = mContext.getResources().getString(R.string.l_planet_end);
            asta_samaya = mContext.getResources().getString(R.string.l_planet_asta_samaya);
            asta_dina = mContext.getResources().getString(R.string.l_dina);
            asta = mContext.getResources().getString(R.string.l_planet_asta);
            nasti = mContext.getResources().getString(R.string.l_nasti);
            month_arr = mContext.getResources().getStringArray(R.array.l_arr_month);
            bara_arr = mContext.getResources().getStringArray(R.array.l_arr_bara);

        }else{
            planetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            planet_start = mContext.getResources().getString(R.string.e_planet_start);
            planet_end = mContext.getResources().getString(R.string.e_planet_end);
            asta_samaya = mContext.getResources().getString(R.string.e_planet_asta_samaya);
            asta_dina = mContext.getResources().getString(R.string.e_dina);
            asta = "Asta";
            nasti = "nasti";

        }

        for (int i = 0, j = 0; i < planetList.length; i++) {
            if (i != 0 && i < 10) {
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

    private static class AstaData {
        public Date start, end;

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        try {
            ArrayList<EphemerisEntity> obj = planetDataList.get(position);
            if (position == 0) {
                typeList.clear();
            }
            int size = obj.size();
            ArrayList<AstaData> astaDataList = new ArrayList<>();
            String startYear = "",selyear="";
            Date startDate = null, endDate = null;

            for (int i = 0; i < size; i++) {
                EphemerisEntity planetObj = obj.get(i);

                SimpleDateFormat formatterRange = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                if(startYear.isEmpty())
                    startYear = planetObj.year;
                if (i % 2 == 0) {
                    String date1 = planetObj.year + "-" + (Integer.parseInt(planetObj.month) + 1) + "-" + planetObj.day;
                    startDate = formatterRange.parse(date1);
                } else {
                    String date2 = planetObj.year + "-" + (Integer.parseInt(planetObj.month) + 1) + "-" + planetObj.day;
                    endDate = formatterRange.parse(date2);
                }

                if (endDate != null) {
                    AstaData astaData = new AstaData();
                    astaData.start = startDate;
                    astaData.end = endDate;
                    astaDataList.add(astaData);
                    endDate = null;

                }
                if (!planetObj.year.contains(startYear)) {
                    break;
                }

            }
            int size1 = astaDataList.size();
            SimpleDateFormat formatterRange1 = new SimpleDateFormat("EEEE, d-MMM", Locale.ENGLISH);
            holder.ll.removeAllViews();

            String nograhastr, grahastr, selYear1 = "" + startYear;
           String planet= planetArr[position];
            if (!CalendarWeatherApp.isPanchangEng) {
                selYear1 = Utility.getInstance(mContext).getDayNo("" + startYear);
                grahastr = planet + " " + asta +  " " + selYear1;
                nograhastr = planet + " " + asta  +" "+nasti;
            } else {
                grahastr = planet + " " + asta + " "   + selYear1;
                nograhastr = planet + " " + asta  +" "+nasti;
            }

            for (int i = 0; i < size1; i++) {

                AstaData obj1 = astaDataList.get(i);
                String startDateStr = formatterRange1.format(obj1.start);
                String endDateStr = formatterRange1.format(obj1.end);
                // String title = startDateStr + " to " + endDateStr;
                String dayNo;
                long timeInMilli = obj1.end.getTime() - obj1.start.getTime();
                String dayStr= ""+(timeInMilli / (24 * 60 * 60 * 1000));

                int DAY_OF_MONTH,MONTH,DAY_OF_WEEK;
                if (!CalendarWeatherApp.isPanchangEng) {
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(obj1.start);
                     DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);
                     MONTH = cal.get(Calendar.MONTH);
                     DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
                     dayNo = Utility.getInstance(mContext).getDayNo("" + DAY_OF_MONTH);
                    startDateStr = bara_arr[DAY_OF_WEEK - 1] + ", " + dayNo + "-" + month_arr[MONTH];

                    Calendar cal1=Calendar.getInstance();
                    cal1.setTime(obj1.end);
                     DAY_OF_MONTH = cal1.get(Calendar.DAY_OF_MONTH);
                     MONTH = cal1.get(Calendar.MONTH);
                     DAY_OF_WEEK = cal1.get(Calendar.DAY_OF_WEEK);
                     dayNo = Utility.getInstance(mContext).getDayNo("" + DAY_OF_MONTH);
                    endDateStr = bara_arr[DAY_OF_WEEK - 1] + ", " + dayNo + "-" + month_arr[MONTH];

                    dayStr=Utility.getInstance(mContext).getDayNo(""+dayStr);
                }







                View child = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.asta_child, null);

                addView(holder.ll, child, startDateStr, endDateStr,   dayStr);
            }

           // String header;
            if(size1==0){
                holder.txt1.setText(nograhastr);
            }else{
               // header = planetArr[position] + " (bakraStr" + size1 + " times in " + startYear + ")";
                holder.txt1.setText(grahastr);
            }

            //  holder.txt3.setText(formatterRange1.format(maxRangeDate));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void addView(LinearLayout item, View child, String title1Str, String title2Str, String days) {
        String dayStr = asta_samaya+" = " + days + " "+asta_dina;
        title1Str =asta+" "+ planet_start+" " + title1Str;
        title2Str =asta+" "+ planet_end+" " + title2Str;
        TextView title1 = child.findViewById(R.id.title1);
        TextView title2 = child.findViewById(R.id.title2);
        TextView date = child.findViewById(R.id.date);
        title1.setText(title1Str);
        title2.setText(title2Str);
        date.setText(dayStr);
        date.setVisibility(View.GONE);
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
