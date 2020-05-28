package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class ChandraBalamListAdapter extends RecyclerView.Adapter<ChandraBalamListAdapter.ItemViewHolder> {
    Context mContext;
    HashMap<String, CoreDataHelper> mItems;


    PrefManager mPref;
    boolean isFirstTime = true;
    int mMonthIndex, year, month, dayofmonth, mPagePos;
    Resources resource;
    int daysInMonth = 0;
    String pkey_time_hour, pkey_time_min, pkey_good, pkey_bad, pkey_worst, epkey_good, epkey_bad, epkey_worst, mLang, pkey_time_to, pkey_time_next, epkey_nakshatra_byakti, pkey_nakshatra_byakti;
    private String[] nakshatra_arr, enakshatra_arr;
    Resources mRes;

    public ChandraBalamListAdapter(Context context, HashMap<String, CoreDataHelper> myPanchangHashMap, HashMap<String, ArrayList<String>> monthFestivalmap, int monthIndex, int myear, int mday, int pagePos) {
        mContext = context;
        mPagePos = pagePos;
        mRes = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mLang = mPref.getMyLanguage();
        if (!CalendarWeatherApp.isPanchangEng) {
            nakshatra_arr = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);
            pkey_good = mContext.getResources().getString(R.string.l_good);
            pkey_bad = mContext.getResources().getString(R.string.l_bad);
            pkey_worst = mContext.getResources().getString(R.string.l_worst);

            epkey_good = mContext.getResources().getString(R.string.e_good);
            epkey_bad = mContext.getResources().getString(R.string.e_bad);
            epkey_worst = mContext.getResources().getString(R.string.e_worst);

            // pkey_time_hour= mRes.getString(R.string.pkey_time_hour) ;
            // pkey_time_min=mRes.getString(R.string.pkey_time_min);

        } else {

            Configuration conf = mRes.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            mRes.updateConfiguration(conf, null); // second arg null means don't change

// retrieve resources from desired locale
            nakshatra_arr = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);
            pkey_good = mContext.getResources().getString(R.string.l_good);
            pkey_bad = mContext.getResources().getString(R.string.l_bad);
            pkey_worst = mContext.getResources().getString(R.string.l_worst);

            epkey_good = mContext.getResources().getString(R.string.e_good);
            epkey_bad = mContext.getResources().getString(R.string.e_bad);
            epkey_worst = mContext.getResources().getString(R.string.e_worst);
            //  pkey_time_hour= mRes.getString(R.string.pkey_time_hour) ;
            //  pkey_time_min=mRes.getString(R.string.pkey_time_min);


// restore original locale
            conf.locale = savedLocale;
            mRes.updateConfiguration(conf, null);

        }


        mMonthIndex = monthIndex;
        year = myear;
        month = monthIndex;
        dayofmonth = mday;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayofmonth);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        Calendar mycal = new GregorianCalendar(year, month, dayofmonth);

        daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28


        mItems = myPanchangHashMap;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.inflate_tarabalamlist;


        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(ChandraBalamListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        try {
            setPanchnang(position, holder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return daysInMonth;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView dateTxt;
        LinearLayout tarabalamCntr;

        public ItemViewHolder(View itemView) {
            super(itemView);

            dateTxt = itemView.findViewById(R.id.dateTxt);

            tarabalamCntr = itemView.findViewById(R.id.tarabalamCntr);
        }


    }

    private void setPanchnang(int position, ItemViewHolder holder) {

        try {
            String key = year + "-" + mMonthIndex + "-" + (position + 1);
            CoreDataHelper myCoreData = mItems.get(key);
            CoreDataHelper.Panchanga nakshetraObj = myCoreData.getMoonSign();
            Calendar currValEndTime = nakshetraObj.currValEndTime;
            String time = getFormattedDate(currValEndTime);

            Calendar cal = Calendar.getInstance();
            cal.set(year, mMonthIndex, (position + 1));
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE, dd-MMMM yyyy", Locale.ENGLISH);
            String formatted = format1.format(cal.getTime());

            String currNak = nakshatra_arr[nakshetraObj.currVal - 1];
            String endTimeCurrNak = getFormattedDate(nakshetraObj.currValEndTime);


            String txtCurrNak = currNak + " " + endTimeCurrNak + " " + pkey_time_to;
            if (nakshetraObj.nextVal != 0) {
                String nextNak = nakshatra_arr[nakshetraObj.nextVal - 1];
                txtCurrNak = txtCurrNak + ", " + pkey_time_next + " " + nextNak;
            }

            holder.dateTxt.setText(formatted + "\n(" + txtCurrNak + ")");


            int currNakshetra = nakshetraObj.currVal;
           // String nakshetra = "";
            int dailynak = currNakshetra;
            int dailynak2 = nakshetraObj.nextVal;
            LayoutInflater inflater = LayoutInflater.from(mContext); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            boolean isFirstGood;
            holder.tarabalamCntr.removeAllViews();
            for (int birthnak = 1; birthnak <= 12; birthnak++) {
                View viewMyLayout = inflater.inflate(R.layout.inflate_tarabalam, null);
                TextView tarabalamTxt = viewMyLayout.findViewById(R.id.text);

                String text1 = "<STRONG>" + nakshatra_arr[birthnak - 1] + "</STRONG> " + pkey_nakshatra_byakti + " " + time + " " + pkey_time_to + " ";

                int diff;
                if (birthnak > dailynak) {
                    diff = (Math.abs(dailynak + 12 - birthnak) + 1);
                } else {
                    diff = (Math.abs(birthnak - dailynak) + 1);
                }

                if (diff == 1 || diff == 3 || diff == 6 || diff == 7 || diff == 10 || diff == 11) {
                    isFirstGood = true;
                    //2,4,6,8,9
                    text1 = text1 + "<font color='#FFAB00'>" + pkey_good + "</font>";
                } else {
                    isFirstGood = false;
                    text1 = text1 + "<font color='#d50000'>" + ((diff == 9 || diff == 5 || diff == 2) ? pkey_bad : pkey_worst) + "</font>";

                }

                if (dailynak2 != 0) {
                    int diff2;
                    if (birthnak > dailynak2) {
                        diff2 = (Math.abs(dailynak2 + 12 - birthnak) + 1);
                    } else {
                        diff2 = (Math.abs(birthnak - dailynak2) + 1);
                    }


                    if (diff2 == 1 || diff2 == 3 || diff2 == 6 || diff2 == 7 || diff2 == 10 || diff2 == 11) {
                        //2,4,6,8,9
                        if (isFirstGood) {
                            text1 = "<STRONG>" + nakshatra_arr[birthnak - 1] + "</STRONG> " + pkey_nakshatra_byakti + "<font color='#FFAB00'> " + pkey_good + "</font>";

                        } else {
                            text1 = text1 + " " + pkey_time_next + " " + "<font color='#FFAB00'>" + pkey_good + "</font>";
                        }
                    } else {
                        if (!isFirstGood) {
                            text1 = "<STRONG>" + nakshatra_arr[birthnak - 1] + "</STRONG> " + pkey_nakshatra_byakti + "<font color='#d50000'> " + ((diff2 == 9 || diff2 == 5 || diff2 == 2) ? pkey_bad : pkey_worst) + "</font>";

                        } else {
                            text1 = text1 + " " + pkey_time_next + " " + "<font color='#d50000'>" + ((diff2 == 9 || diff2 == 5 || diff2 == 2) ? pkey_bad : pkey_worst) + "</font>";
                        }

                    }

                }
                tarabalamTxt.setText(Html.fromHtml(text1));
                holder.tarabalamCntr.addView(viewMyLayout);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getFormattedDate(Calendar cal) {

        if ((mLang.contains("or") || mLang.contains("hi")) && !CalendarWeatherApp.isPanchangEng) {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            int calHour = cal.get(Calendar.HOUR_OF_DAY);
            int calMin = cal.get(Calendar.MINUTE);
            int calMonth = cal.get(Calendar.MONTH);
            String calDayNoStr = Utility.getInstance(mContext).getDayNo("" + calDayNo);
            String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
            String prefixTime = "";
            if ((calHour > 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
                prefixTime = mRes.getString(R.string.l_time_night);
            }
            if (calHour >= 4 && calHour < 9) {
                prefixTime = mRes.getString(R.string.l_time_prattha);
            } else if (calHour >= 9 && calHour < 16) {
                prefixTime = mRes.getString(R.string.l_time_diba);
            } else if (calHour >= 16 && calHour < 19) {
                prefixTime = mRes.getString(R.string.l_time_sandhya);
            }
            if (calHour > 12) {
                calHour = calHour - 12;
            }
            String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

            String ldate;


            ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);


            return ldate;
        } else {
            return geteFormattedDate(cal);
        }
    }

    private String geteFormattedDate(Calendar cal) {

        try {

            Date date = cal.getTime();
            DateFormat dateFormat;

            String dt;
            dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            dt = dateFormat.format(date);

            return dt;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
