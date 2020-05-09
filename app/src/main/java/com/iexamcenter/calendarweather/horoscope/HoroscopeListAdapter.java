package com.iexamcenter.calendarweather.horoscope;

import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;

public class HoroscopeListAdapter extends RecyclerView.Adapter<HoroscopeListAdapter.ItemViewHolder> {
    Resources res;
    Context mContext;
    ArrayList<HoroscopeFrag.horoscopeItem> mItems;
    String[] mrasi_kundali_arr, men_rasi_kundali_arr, mlrasi_kundali_arr;
    private int lastPosition = -1;
    PrefManager mPref;

    public HoroscopeListAdapter(Context context, ArrayList<HoroscopeFrag.horoscopeItem> pglist, String[] rasi_kundali_arr, String[] en_rasi_kundali_arr, String[] lrasi_kundali_arr) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;
        mrasi_kundali_arr = rasi_kundali_arr;
        men_rasi_kundali_arr = en_rasi_kundali_arr;
        mlrasi_kundali_arr = lrasi_kundali_arr;
        mPref = PrefManager.getInstance(mContext);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_rashiphalalist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }
    public int getDrawable(int position) {

        switch (position) {
            case 0:
                return R.drawable.ic_aries_zodiac_symbol_of_frontal_goat_head;
            case 1:
                return R.drawable.ic_taurus_zodiac_sign;

            case 2:
                return R.drawable.ic_gemini_zodiac_symbol;

            case 3:
                return R.drawable.ic_cancer_zodiac_sign;

            case 4:
                return R.drawable.ic_leo_astrological_sign;

            case 5:
                return R.drawable.ic_virgo_zodiac_symbol;

            case 6:
                return R.drawable.ic_libra_balanced_scale_zodiac_symbol;

            case 7:
                return R.drawable.ic_scorpio_zodiac_symbol;

            case 8:
                return R.drawable.ic_sagittarius_zodiac_symbol;

            case 9:
                return R.drawable.ic_capricorn_symbol_with_big_horn;
            case 10:
                return R.drawable.ic_aquarius_zodiac_sign_symbol;
            case 11:
                return R.drawable.ic_pisces_zodiac_sign;

            default:
                return R.drawable.ic_aquarius_zodiac_sign_symbol;

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        try {
            final HoroscopeFrag.horoscopeItem obj = mItems.get(position);
            String rashi = mrasi_kundali_arr[position];
            String lrashi = mlrasi_kundali_arr[position];
            String eRashi = men_rasi_kundali_arr[position];


            if (mPref.getMyLanguage().contains("en")) {
                holder.title1.setText(rashi + ", " + obj.title);
            } else {
                holder.title1.setText(rashi + "(" + lrashi + "), " + obj.title);
                // CalendarWeatherApp.getInstance().setTextView(holder.title1, rashi + "(" + lrashi + "), " + obj.title, 0);

            }
            //holder.title1.setCompoundDrawables(mContext.getResources().getDrawable(getDrawable(position)),null,null,null);
            holder.horoIcon.setImageResource(getDrawable(position));
            //  holder.title1.setCompoundDrawablesWithIntrinsicBounds(getDrawable(position), 0, 0, 0);
            holder.title2.setText(obj.desc);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewDetachedFromWindow(HoroscopeListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2;
        public ImageView horoIcon;

        public LinearLayout container;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horoIcon=itemView.findViewById(R.id.horoIcon);
            title1 = itemView.findViewById(R.id.title);
            title2 = itemView.findViewById(R.id.desc);

        }


    }
}
