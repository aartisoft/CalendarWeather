package com.iexamcenter.calendarweather.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.horoscope.HoroscopeFrag;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class HinduTimeAdapter extends RecyclerView.Adapter<HinduTimeAdapter.ItemViewHolder> {
    Resources res;
    Context mContext;
    List<String> mItems;
    String[] mTimeArr;

    public HinduTimeAdapter(MainActivity context, String[] hindu_time_arr, List<String> timeList) {

        res = context.getResources();
        mContext = context;
        mItems = timeList;
        mTimeArr = hindu_time_arr;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_hindu_time_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        try {
            String val = mItems.get(position);
            String title = mTimeArr[position];

            holder.title.setText(title);
            holder.val.setText(val);
            if (position % 2 == 0)
                holder.cntr.setBackgroundResource(R.color.grey_day);
            else {
                holder.cntr.setBackgroundColor(Color.WHITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title, val;
        LinearLayout cntr;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cntr = itemView.findViewById(R.id.cntr);
            title = itemView.findViewById(R.id.title);
            val = itemView.findViewById(R.id.val);
        }
    }
}
