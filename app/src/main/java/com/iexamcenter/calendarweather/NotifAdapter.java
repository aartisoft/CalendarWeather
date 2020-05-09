package com.iexamcenter.calendarweather;
/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;


public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ItemViewHolder> {

    private ArrayList<MySettingsActivity.notif> mItems;
    private Context mContext;
    private Gson gson;
    private PrefManager mPref;
    Resources res;
    String[] tithi_arr;

    public NotifAdapter(Context context, ArrayList<MySettingsActivity.notif> arrList) {
        mItems = arrList;
        mContext = context;
        res = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        tithi_arr = res.getStringArray(R.array.e_arr_tithi15);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_notif, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final MySettingsActivity.notif obj = mItems.get(position);

        holder.title.setTag(obj);

        if (mPref.getMyLanguage().contains("en")) {
            holder.title.setText(obj.eName);
        }else {
           // CalendarWeatherApp.getInstance().setTextView(  holder.title, obj.lName + "(" + obj.eName + ")", 1);
            holder.title.setText(obj.lName + "(" + obj.eName + ")");
        }

        if (mPref.getNotified("NOTIF_" + obj.eName) == 1 || (mPref.getNotified("NOTIF_" + obj.eName) == -1 && (obj.eName.contains(res.getString(R.string.e_dina))))) {
           holder.title.setChecked(true);
        } else {
            holder.title.setChecked(false);
        }
        holder.title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MySettingsActivity.notif obj = (MySettingsActivity.notif) compoundButton.getTag();
                if (b) {

                    mPref.setNotified("NOTIF_" + obj.eName, 1);
                } else {
                    mPref.setNotified("NOTIF_" + obj.eName, 0);
                }
                mPref.load();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.enable);


        }


    }
}