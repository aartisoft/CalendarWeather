package com.iexamcenter.calendarweather.sunmoon;

import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RiseSetListAdapter extends RecyclerView.Adapter<RiseSetListAdapter.ItemViewHolder> {
    Context mContext;
    // ArrayList<Utility.SunDetails> mItems;
    //  ArrayList<Utility.MoonDetails> mItems1;
    private int lastPosition = -1;
    Resources res;
    SunmoonFragment mSmf;
    PrefManager mPref;
    Calendar cal1;
    int monthLength;
    Utility.SunDetails obj;
    Utility.MoonDetails obj1;
    DateFormat dateFormat;
    private String sunRiseTitleL, sunRiseTitleE, sunsetTitleL, sunsetTitleE, moonRiseTitleL, moonRiseTitleE, moonsetTitleL, moonsetTitleE;
    private String sunRiseTitleEV, sunsetTitleEV, moonRiseTitleEV, moonsetTitleEV;

    public RiseSetListAdapter(Context context, SunmoonFragment smf, Calendar cal1) {
        mSmf = smf;
        mContext = context;
         dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
        res = mContext.getResources();
        this.cal1 = cal1;
        mPref = PrefManager.getInstance(mContext);
        monthLength = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
        sunRiseTitleL = mContext.getResources().getString(R.string.l_sunrise);
        sunRiseTitleE = mContext.getResources().getString(R.string.e_sunrise);

        sunsetTitleL = mContext.getResources().getString(R.string.l_sunset);
        sunsetTitleE = mContext.getResources().getString(R.string.e_sunset);


        moonRiseTitleL = mContext.getResources().getString(R.string.l_moonrise);
        moonRiseTitleE = mContext.getResources().getString(R.string.e_moonrise);

        moonsetTitleL = mContext.getResources().getString(R.string.l_moonset);
        moonsetTitleE = mContext.getResources().getString(R.string.e_moonset);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_risesetlist;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        cal1.set(Calendar.DAY_OF_MONTH, position + 1);

         obj = Utility.getInstance(mContext).getTodaySunDetails(cal1);
         obj1 = Utility.getInstance(mContext).getTodayMoonDetails(cal1);

        try {
            if (mSmf.isMore()) {
                holder.container2.setVisibility(View.GONE);
                holder.container1.setVisibility(View.VISIBLE);

            } else {
                holder.container1.setVisibility(View.GONE);
                holder.container2.setVisibility(View.VISIBLE);

            }




            sunRiseTitleEV = obj.sunRise;


            sunsetTitleEV = obj.sunSet;


            moonRiseTitleEV = obj1.moonRise;

            moonsetTitleEV = obj1.moonSet;

            String date1 = obj.date;
            String[] dtArr = date1.split("/");

            Date date = cal1.getTime();

            String strDate = dateFormat.format(date);


            if (mPref.getMyLanguage().contains("en")) {
                holder.sunRiseT.setText(sunRiseTitleE);
                holder.sunSetT.setText(sunsetTitleE);
                holder.dateT.setText(strDate);

            } else {
                //   CalendarWeatherApp.getInstance().setTextView(holder.dateT, eDayStr + "-" + dtArr[0] + "(" + lDayStr + "-" + lDayDigitStr + ")", 0);

//                CalendarWeatherApp.getInstance().setTextView(holder.sunRiseT, sunRiseTitleL + "(" + sunRiseTitleE + ")", 1);
                // CalendarWeatherApp.getInstance().setTextView(holder.sunRiseV, sunRiseTitleEV, 1);

                // CalendarWeatherApp.getInstance().setTextView(holder.sunSetT, sunsetTitleL + "(" + sunsetTitleE + ")", 1);
                holder.sunRiseT.setText(sunRiseTitleL + "(" + sunRiseTitleE + ")");
                holder.sunSetT.setText(sunsetTitleL + "(" + sunsetTitleE + ")");
                //  holder.dateT.setText(strDate+":"+eDayStr + "-" + dtArr[0] + "(" + lDayStr + "-" + lDayDigitStr + ")");
                holder.dateT.setText(strDate);
            }
            //  CalendarWeatherApp.getInstance().setTextView(holder.sunSetV, sunsetTitleEV, 1);

            holder.sunRiseV.setText(sunRiseTitleEV);
            holder.sunSetV.setText(sunsetTitleEV);
            String[] tmpArr1 = moonRiseTitleEV.split(":");
            String[] tmpArr2 = moonsetTitleEV.split(":");
            String dateVal = dtArr[0];
            moonRiseTitleEV = tmpArr1[1] + ":" + tmpArr1[2];
            moonsetTitleEV = tmpArr2[1] + ":" + tmpArr2[2];
            if (tmpArr1[0].contains(dateVal) && tmpArr2[0].contains(dateVal)) {
                if (moonRiseTitleEV.toLowerCase().contains("am")) {

                    holder.moonRiseT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonrise, 0, 0, 0);
                    holder.moonSetT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonset, 0, 0, 0);

                    if (mPref.getMyLanguage().contains("en")) {
                        holder.moonRiseT.setText(moonRiseTitleE);
                        holder.moonSetT.setText(moonsetTitleE);
                    } else {
                        holder.moonRiseT.setText(moonRiseTitleL + "(" + moonRiseTitleE + ")");
                        holder.moonSetT.setText(moonsetTitleL + "(" + moonsetTitleE + ")");
                    }


                    holder.mRise.setText("Moonrise");
                    holder.mSet.setText("Moonset");
                    holder.moonRiseV.setText(moonRiseTitleEV);
                    holder.moonSetV.setText(moonsetTitleEV);
                    holder.mRiseV.setText(moonRiseTitleEV);
                    holder.mSetV.setText(moonsetTitleEV);

                } else {

                    holder.moonRiseT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonset, 0, 0, 0);
                    holder.moonSetT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonrise, 0, 0, 0);
                    if (mPref.getMyLanguage().contains("en")) {
                        holder.moonRiseT.setText(moonsetTitleE);
                        holder.moonSetT.setText(moonRiseTitleE);
                    } else {
                        holder.moonRiseT.setText(moonsetTitleL + "(" + moonsetTitleE + ")");
                        holder.moonSetT.setText(moonRiseTitleL + "(" + moonRiseTitleE + ")");
                    }

                    holder.moonRiseV.setText(moonsetTitleEV);
                    holder.moonSetV.setText(moonRiseTitleEV);
                    holder.mRiseV.setText(moonsetTitleEV);
                    holder.mSetV.setText(moonRiseTitleEV);


                    holder.mRise.setText("Moonset");
                    holder.mSet.setText("Moonrise");

                }
            } else if (tmpArr1[0].contains(dateVal) && !tmpArr2[0].contains(dateVal)) {

                holder.moonRiseT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonrise, 0, 0, 0);
                holder.moonSetT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonset, 0, 0, 0);

                if (mPref.getMyLanguage().contains("en")) {
                    holder.moonRiseT.setText(moonRiseTitleE);
                    holder.moonSetT.setText(moonsetTitleE);
                } else {
                    holder.moonRiseT.setText(moonRiseTitleL + "(" + moonRiseTitleE + ")");
                    holder.moonSetT.setText(moonsetTitleL + "(" + moonsetTitleE + ")");
                }


                holder.moonRiseV.setText(moonRiseTitleEV);
                holder.moonSetV.setText(moonsetTitleEV + "(+)");
                holder.mRiseV.setText(moonRiseTitleEV);
                holder.mSetV.setText(moonsetTitleEV + "(+)");


                holder.mRise.setText("Moonrise");
                holder.mSet.setText("Moonset");

            } else if (!tmpArr1[0].contains(dateVal) && tmpArr2[0].contains(dateVal)) {
                holder.moonRiseT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonset, 0, 0, 0);
                holder.moonSetT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_moonrise, 0, 0, 0);

                if (mPref.getMyLanguage().contains("en")) {
                    holder.moonRiseT.setText(moonsetTitleE);
                    holder.moonSetT.setText(moonRiseTitleE);
                } else {
                    holder.moonSetT.setText(moonRiseTitleL + "(" + moonRiseTitleE + ")");
                    holder.moonRiseT.setText(moonsetTitleL + "(" + moonsetTitleE + ")");
                }

                holder.moonRiseV.setText(moonsetTitleEV);
                holder.moonSetV.setText(moonRiseTitleEV + "(+)");
                holder.mRiseV.setText(moonsetTitleEV);
                holder.mSetV.setText(moonRiseTitleEV + "(+)");


                holder.mRise.setText("Moonset");
                holder.mSet.setText("Moonrise");

            }

            holder.aSunRiseV.setText(obj.aSunRise);
            holder.nSunRiseV.setText(obj.nSunRise);
            holder.cSunRiseV.setText(obj.cSunRise);
            holder.aSunSetV.setText(obj.aSunSet);
            holder.nSunSetV.setText(obj.nSunSet);
            holder.cSunSetV.setText(obj.cSunSet);

            holder.oSunRiseV.setText(obj.sunRise);
            holder.oSunSetV.setText(obj.sunSet);
            holder.noonV.setText(obj.sunTransit);
            holder.azimutalV.setText(obj.sunAz + "°");
            holder.elevtV.setText(obj.sunTransitElev + "°");
            holder.elevV.setText(obj.sunEl + "°");
            holder.distanceV.setText(obj.sunDist);
            holder.mEleV.setText(obj1.moonEl + "°");
            holder.mAzV.setText(obj1.moonAz + "°");
            holder.mAgeV.setText(obj1.age);
            holder.mDistV.setText(obj1.distance);
            holder.mPhaseV.setText(obj1.title);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return monthLength;// mItems.size();
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mRise, mSet, mPhaseV, mRiseV, mSetV, mAzV, mEleV, mAgeV, mDistV, azimutalV, elevtV, elevV, distanceV, noonV, oSunRiseV, aSunRiseV, nSunRiseV, cSunRiseV, oSunSetV, aSunSetV, nSunSetV, cSunSetV, dateT, sunRiseT, sunRiseV, sunSetT, sunSetV, moonRiseT, moonRiseV, moonSetT, moonSetV;

        public LinearLayout container1, container2;


        public ItemViewHolder(View itemView) {
            super(itemView);
            //container = itemView.findViewById(R.id.container);
            container1 = itemView.findViewById(R.id.container1);
            container2 = itemView.findViewById(R.id.container2);

            dateT = itemView.findViewById(R.id.date);
            sunRiseT = itemView.findViewById(R.id.sunRiseT);
            sunRiseV = itemView.findViewById(R.id.sunRiseV);

            sunSetT = itemView.findViewById(R.id.sunSetT);
            sunSetV = itemView.findViewById(R.id.sunSetV);

            moonRiseT = itemView.findViewById(R.id.moonRiseT);
            moonRiseV = itemView.findViewById(R.id.moonRiseV);


            moonSetT = itemView.findViewById(R.id.moonSetT);
            moonSetV = itemView.findViewById(R.id.moonSetV);


            aSunRiseV = itemView.findViewById(R.id.astronomicalSunriseV);
            nSunRiseV = itemView.findViewById(R.id.nauticalSunriseV);
            cSunRiseV = itemView.findViewById(R.id.civilSunriseV);
            oSunRiseV = itemView.findViewById(R.id.officialSunriseV);
            noonV = itemView.findViewById(R.id.noonV);


            aSunSetV = itemView.findViewById(R.id.astronomicalSunsetV);
            nSunSetV = itemView.findViewById(R.id.nauticalSunsetV);
            cSunSetV = itemView.findViewById(R.id.civilSunsetV);
            oSunSetV = itemView.findViewById(R.id.officialSunsetV);

            elevV = itemView.findViewById(R.id.elevV);
            elevtV = itemView.findViewById(R.id.elevtV);
            azimutalV = itemView.findViewById(R.id.azimutalV);
            distanceV = itemView.findViewById(R.id.distanceV);

            mRiseV = itemView.findViewById(R.id.mRiseV);
            mSetV = itemView.findViewById(R.id.mSetV);


            mRise = itemView.findViewById(R.id.mRise);
            mSet = itemView.findViewById(R.id.mSet);

            mEleV = itemView.findViewById(R.id.mEleV);
            mAzV = itemView.findViewById(R.id.mAzV);
            mDistV = itemView.findViewById(R.id.mDistV);
            mAgeV = itemView.findViewById(R.id.mAgeV);
            mPhaseV = itemView.findViewById(R.id.mPhaseV);

        }


    }
}
