package com.iexamcenter.calendarweather.horoscope;

import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

public class RashiListAdapter extends RecyclerView.Adapter<RashiListAdapter.ItemViewHolder> {
    Resources res;
    Context mContext;
    PrefManager mPref;
    RecyclerView mHoroListView;

    public RashiListAdapter(Context context, RecyclerView horoListView) {
        res = context.getResources();
        mContext = context;
        mHoroListView = horoListView;
        mPref = PrefManager.getInstance(mContext);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_rashi;
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
        // final HoroscopeFrag.OnThisDayItems obj = mrasi_kundali_arr[position];


        holder.icon.setImageResource(getDrawable(position));
        holder.icon.setTag(position);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
              //  mHoroListView.getLayoutManager().scrollToPosition(pos);
                ((LinearLayoutManager) mHoroListView.getLayoutManager()).scrollToPositionWithOffset(pos,0);



            }
        });

    }

    @Override
    public int getItemCount() {
        return 12;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);


        }


    }
}
