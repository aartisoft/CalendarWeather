package com.iexamcenter.calendarweather.tools;

import android.content.Intent;
import android.content.res.Resources;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.festival.FestivalFragment;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangUtilityLighter;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class YogaTimeAdapter extends RecyclerView.Adapter<YogaTimeAdapter.ItemViewHolder> {

    Resources res;
    private static MainActivity activity;
    ArrayList<PanchangaYogaFrag.YogaDateTiming> mItems;
    PrefManager mPref;

    public YogaTimeAdapter(MainActivity context, ArrayList<PanchangaYogaFrag.YogaDateTiming> pglist) {
        res = context.getResources();
        mItems=pglist;
        activity = context;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_yoga_time_list;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    ItemViewHolder holder;

    @Override
    public void onBindViewHolder(final ItemViewHolder mholder, int position) {

        holder = mholder;


        PanchangaYogaFrag.YogaDateTiming obj = mItems.get(position);
        mholder.date.setText(obj.title+"\n"+obj.jogaDate);
        mholder.startEnd.setText(obj.timings);
        if(obj.combination.isEmpty()){
            mholder.combination.setText(obj.combination);
            mholder.combination.setVisibility(View.GONE);
        }else{
            mholder.combination.setText(obj.combination);
            mholder.combination.setVisibility(View.VISIBLE);
        }

        mholder.duration.setText(obj.duration);
        mholder.reminder.setTag(obj);
        mholder.reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PanchangaYogaFrag.YogaDateTiming  obj= (PanchangaYogaFrag.YogaDateTiming ) v.getTag();

                String desc=obj.title+"\n"+obj.jogaDate+"\n"+obj.timings+"\n"+obj.combination+"\n"+obj.duration;
                try {
                    Intent intentEvent = new Intent(Intent.ACTION_INSERT)
                            .setType("vnd.android.cursor.item/event")
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, obj.title)
                            .putExtra(CalendarContract.Events.DESCRIPTION, desc)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, obj.startTimeCal.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, obj.endTimeCal.getTimeInMillis())
                            .putExtra(CalendarContract.Events.VISIBLE, false)
                            .putExtra(CalendarContract.Events.ALL_DAY, false)
                            .putExtra(CalendarContract.Reminders.METHOD, true)
                            .putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    activity.startActivity(intentEvent);
                } catch (Exception e) {
                    Toast.makeText(activity, "No Event Calendar Found..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date,duration,combination,startEnd;
        MaterialButton reminder;

        public ItemViewHolder(View rootView) {
            super(rootView);
            reminder= rootView.findViewById(R.id.reminder);
            date = rootView.findViewById(R.id.date);
            duration= rootView.findViewById(R.id.duration);
            combination= rootView.findViewById(R.id.combination);
            startEnd= rootView.findViewById(R.id.startEnd);
        }
    }
    public static class DataCls{
        public Calendar cal;
        public String desc;
        public String title;
    }
}

