package com.iexamcenter.calendarweather.festival;

import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.FullscreenActivity;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ItemViewHolder> {
    Resources res;
    private static MainActivity activity;
    ArrayList<String> mItems;
    MainViewModel viewModel;
    LocationDialog locationDialog;
    PrefManager mPref;
    public CityListAdapter(MainActivity context, ArrayList<String> pglist, int pagePosition, MainViewModel viewModel, LocationDialog locationDialog) {
        res = context.getResources();
        activity = context;
        this.locationDialog=locationDialog;
        this.viewModel=viewModel;
        mItems = pglist;
        mPref= PrefManager.getInstance(context);
        mPref.load();


    }
    public CityListAdapter(FullscreenActivity context, ArrayList<String> pglist, int pagePosition) {
        res = context.getResources();
       // activity = context;

        mItems = pglist;

    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.dropdown_menu_popup_item;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder mholder, int position) {

        String cityName = mItems.get(position);
        String[] tmp = cityName.split("\\|");
        String cityVal = tmp[0] + "<br/><font color='#AAAAAA'>" + tmp[1] + "</font>";
        mholder.txt.setText(Html.fromHtml(cityVal));
        mholder.itemView.setTag(tmp[2]);


        mholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationDialog.updateLoc(v);

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

        TextView txt;

        public ItemViewHolder(View rootView) {
            super(rootView);
            txt = rootView.findViewById(R.id.txt1);

        }


    }


}
