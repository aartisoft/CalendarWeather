package com.iexamcenter.calendarweather.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;

public class PremiumListAdapter extends RecyclerView.Adapter<PremiumListAdapter.ItemViewHolder> {
    MainActivity mContext;
    PrefManager mPref;
    ArrayList<String> mylist;
    boolean isRewared = false;
    MaterialButton mAds;
    String[] mLe_arr_special_calculator1,mLe_arr_special_calculator2;
    public PremiumListAdapter(MainActivity context, ArrayList<String> mylist, MaterialButton ads, String[] le_arr_special_calculator1, String[] le_arr_special_calculator2) {
        this.mylist = mylist;
        mContext = context;
        mLe_arr_special_calculator1=le_arr_special_calculator1;
        mLe_arr_special_calculator2=le_arr_special_calculator2;
        mAds=ads;
        mPref=PrefManager.getInstance(mContext);
        mPref.load();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_premium_list;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);


        ItemViewHolder vh = new ItemViewHolder(view, new PremiumListAdapter.ItemViewHolder.IMyViewHolderInterface() {

            @Override
            public void gotoPage(View v) {
                if(!isRewared){
                    mAds.performClick();
                    Toast.makeText(mContext,"These are premium features. Unlock it before use.",Toast.LENGTH_LONG).show();
                    return;
                }
                int position = (int) v.getTag();
                String title1=mLe_arr_special_calculator1[position];
                String title2=mLe_arr_special_calculator2[position];

                FragmentManager fm = mContext.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                Bundle b = new Bundle();
                if (mylist.size() == 7) {
                    frag = PanchangaYogaFrag.newInstance();
                    b.putString("PAGE_TITLE_ENG", title2);
                    b.putString("PAGE_TITLE_LOCAL", title1);
                    b.putInt(PanchangaYogaFrag.ARG_POSITION, position);
                    frag.setArguments(b);
                    ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                    ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                    ft.commit();
                } else {


                    switch (position) {
                        case 0:
                            frag = BirthAnniversaryFrag.newInstance();
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            b.putInt(BirthAnniversaryFrag.ARG_POSITION, position);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;
                        case 1:
                            frag = DeathAnniversaryFrag.newInstance();
                            b.putInt(DeathAnniversaryFrag.ARG_POSITION, position);
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;
                        case 2:
                            frag = OtherAnniversaryFrag.newInstance();
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            b.putInt(OtherAnniversaryFrag.ARG_POSITION, position);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;
                        case 11:
                            frag = AgeCalculatorFrag.newInstance();
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            b.putInt(AgeCalculatorFrag.ARG_POSITION, position);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            frag = PanchangaToolFrag.newInstance();
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            b.putInt(PanchangaToolFrag.ARG_POSITION, position);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;
                        default:
                            frag = PanchangaYogaFrag.newInstance();
                            b.putString("PAGE_TITLE_ENG", title2);
                            b.putString("PAGE_TITLE_LOCAL", title1);
                            b.putInt(PanchangaYogaFrag.ARG_POSITION, position);
                            frag.setArguments(b);
                            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
                            ft.commit();
                            break;


                    }
                }
            }

        });
        return vh;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        // String val = mItems.get(position);
        String val = mylist.get(position);
        holder.title1.setText(mLe_arr_special_calculator1[position]);
        holder.titleEng.setText(mLe_arr_special_calculator2[position]);
        holder.titleCntr.setTag(position);
        holder.title1.setTag(position);
        holder.titleEng.setTag(position);
        if(!mPref.getMyLanguage().contains("or")){
            holder.titleEng.setVisibility(View.INVISIBLE);
        }else{
            holder.titleEng.setVisibility(View.VISIBLE);
        }
       // boolean isRewared = false;
        if (mylist.size() > 10) {
            isRewared = CalendarWeatherApp.isRewardedPremiumGrp1;
        } else {
            isRewared = CalendarWeatherApp.isRewardedPremiumGrp2;
        }
        if (isRewared) {
            holder.titleCntr.setBackground(null);
        } else {
            holder.titleCntr.setBackgroundResource(R.drawable.ic_baseline_lock_24);
        }


    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title1,titleEng;
        public LinearLayout titleCntr;

        IMyViewHolderInterface mListener;

        public ItemViewHolder(View itemView, IMyViewHolderInterface listener) {
            super(itemView);
            mListener = listener;
            titleEng= itemView.findViewById(R.id.titleEng);
            title1 = itemView.findViewById(R.id.title);
            titleCntr= itemView.findViewById(R.id.titleCntr);
            title1.setOnClickListener(this);
            titleEng.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.titleEng:
                case R.id.title:
                case R.id.titleCntr:
                    mListener.gotoPage(v);
                    break;

            }
        }

        public interface IMyViewHolderInterface {
            void gotoPage(View v);

        }
    }
}
