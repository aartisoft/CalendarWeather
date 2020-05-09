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

public class TaraBalamListAdapter extends RecyclerView.Adapter<TaraBalamListAdapter.ItemViewHolder> {
    Context mContext;
    HashMap<String, CoreDataHelper> mItems;
    Resources res;

    PrefManager mPref;
    boolean isFirstTime = true;
    int mMonthIndex, year, month, dayofmonth, mPagePos;
    Resources resource;
    int daysInMonth = 0;
    String mLang, pkey_time_to, pkey_time_next, epkey_nakshatra_byakti, pkey_nakshatra_byakti;
    private String[] nakshatra_arr, enakshatra_arr, tarabalam_arr;
    Resources mRes;

    public TaraBalamListAdapter(Context context, HashMap<String, CoreDataHelper> myPanchangHashMap, HashMap<String, ArrayList<String>> monthFestivalmap, int monthIndex, int myear, int mday, int pagePos) {
        mContext = context;
        mPagePos = pagePos;
        mRes = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mLang = mPref.getMyLanguage();
        if (!CalendarWeatherApp.isPanchangEng) {
            enakshatra_arr = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            nakshatra_arr = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            tarabalam_arr = mContext.getResources().getStringArray(R.array.l_arr_tarabalam);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_nakshatra_byakti);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_nakshatra_byakti);
        } else {
            Configuration conf = mRes.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            mRes.updateConfiguration(conf, null); // second arg null means don't change
            enakshatra_arr = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            nakshatra_arr = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            tarabalam_arr = mContext.getResources().getStringArray(R.array.l_arr_tarabalam);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_nakshatra_byakti);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_nakshatra_byakti);
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
    public void onViewDetachedFromWindow(TaraBalamListAdapter.ItemViewHolder holder) {
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
            CoreDataHelper.Panchanga nakshetraObj = myCoreData.getNakshetra();
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

            holder.dateTxt.setText(formatted + " \n(" + txtCurrNak + ")");


            int currNakshetra = nakshetraObj.currVal;
            String nakshetra = "";
            int dailynak = currNakshetra;
            int dailynak2 = nakshetraObj.nextVal;
            LayoutInflater inflater = LayoutInflater.from(mContext);

            holder.tarabalamCntr.removeAllViews();
            for (int birthnak = 1; birthnak <= 27; birthnak++) {
                View viewMyLayout = inflater.inflate(R.layout.inflate_tarabalam, null);
                TextView tarabalamTxt = viewMyLayout.findViewById(R.id.text);

                String text1 = "<STRONG>" + nakshatra_arr[birthnak - 1] + "</STRONG> " + pkey_nakshatra_byakti + " " + time + " " + pkey_time_to + " ";

                int diff;
                if (birthnak > dailynak) {
                    diff = (Math.abs(dailynak + 27 - birthnak) + 1) % 9;

                } else {
                    diff = (Math.abs(birthnak - dailynak) + 1) % 9;

                }

                // birthNakshetra.setText(enakshatra_arr[birthnak - 1]);
                // tarabalam1.setText(tarabalam_arr[diff]);
                //  horaTxt1.setTextColor(Color.parseColor("#d50000"));


                if (diff == 2 || diff == 4 || diff == 6 || diff == 8 || diff == 9 || diff == 0) {
                    // tarabalam1.setTextColor(Color.GREEN);
                    text1 = text1 + "<font color='#FFAB00'>" + tarabalam_arr[diff] + "</font>";
                } else {
                    text1 = text1 + "<font color='#d50000'>" + tarabalam_arr[diff] + "</font>";
                    // tarabalam1.setTextColor(Color.RED);
                }

                if (dailynak2 != 0) {
                    int diff2;
                    if (birthnak > dailynak2) {
                        diff2 = (Math.abs(dailynak2 + 27 - birthnak) + 1) % 9;
                    } else {
                        diff2 = (Math.abs(birthnak - dailynak2) + 1) % 9;
                    }
                    if (diff2 == 2 || diff2 == 4 || diff2 == 6 || diff2 == 8 || diff2 == 9 || diff2 == 0) {
                        text1 = text1 + " " + pkey_time_next + " " + "<font color='#FFAB00'>" + tarabalam_arr[diff2] + "</font>";

                    } else {
                        text1 = text1 + " " + pkey_time_next + " " + "<font color='#d50000'>" + tarabalam_arr[diff2] + "</font>";

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

        if (mLang.contains("or") && !CalendarWeatherApp.isPanchangEng) {
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
