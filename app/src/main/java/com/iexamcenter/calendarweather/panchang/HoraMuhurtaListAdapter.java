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

public class HoraMuhurtaListAdapter extends RecyclerView.Adapter<HoraMuhurtaListAdapter.ItemViewHolder> {
    Context mContext;
    HashMap<String, CoreDataHelper> mItems;
    Resources res;

    PrefManager mPref;
    boolean isFirstTime = true;
    int mMonthIndex, year, month, dayofmonth, mPagePos;
    Resources resource;
    int daysInMonth = 0;
    String grahabhoga, arambha, sesha, pkey_sunset, pkey_sunrise, dayHora, nightHora, mLang, pkey_time_to, pkey_time_next, epkey_nakshatra_byakti, pkey_nakshatra_byakti;
    private String[] hora_arr, enakshatra_arr;
    Resources mRes;

    public HoraMuhurtaListAdapter(Context context, HashMap<String, CoreDataHelper> myPanchangHashMap, HashMap<String, ArrayList<String>> monthFestivalmap, int monthIndex, int myear, int mday, int pagePos) {
        mContext = context;
        mPagePos = pagePos;
        mRes = mContext.getResources();

        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mLang = mPref.getMyLanguage();
        if (!CalendarWeatherApp.isPanchangEng) {
            //enakshatra_arr = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);
            hora_arr = mContext.getResources().getStringArray(R.array.l_arr_hora);
            //tarabalam_arr = mContext.getResources().getStringArray(R.array.l_tarabalam_arr);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);

            dayHora = mContext.getResources().getString(R.string.l_dayhora);
            nightHora = mContext.getResources().getString(R.string.l_nighthora);
            pkey_sunrise = mContext.getResources().getString(R.string.l_sunrise);
            pkey_sunset = mContext.getResources().getString(R.string.l_sunset);
            grahabhoga = mContext.getResources().getString(R.string.l_grahabhoga);
            arambha = mContext.getResources().getString(R.string.l_arambha);
            sesha = mContext.getResources().getString(R.string.l_sesha);
        } else {
            Configuration conf = mRes.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            mRes.updateConfiguration(conf, null); // second arg null means don't change

            hora_arr = mContext.getResources().getStringArray(R.array.l_arr_hora);
            //tarabalam_arr = mContext.getResources().getStringArray(R.array.l_tarabalam_arr);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);

            dayHora = mContext.getResources().getString(R.string.l_dayhora);
            nightHora = mContext.getResources().getString(R.string.l_nighthora);
            pkey_sunrise = mContext.getResources().getString(R.string.l_sunrise);
            pkey_sunset = mContext.getResources().getString(R.string.l_sunset);

            grahabhoga = mContext.getResources().getString(R.string.l_grahabhoga);
            arambha = mContext.getResources().getString(R.string.l_arambha);
            sesha = mContext.getResources().getString(R.string.l_sesha);
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
    public void onViewDetachedFromWindow(HoraMuhurtaListAdapter.ItemViewHolder holder) {
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
            //  CoreDataHelper.Panchanga nakshetraObj = myCoreData.getMoonSign();
            Calendar sunriseCal = myCoreData.getSunRise();
            Calendar sunsetCal = myCoreData.getSunSetCal();

            long diff = sunsetCal.getTimeInMillis() - sunriseCal.getTimeInMillis();
            long diffDay = (diff) / 12;
            long diffNight = (24 * 60 * 60 * 1000 - diff) / 12;


            Calendar cal = Calendar.getInstance();
            cal.set(year, mMonthIndex, (position + 1));
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE, dd-MMMM yyyy", Locale.ENGLISH);
            String formatted = format1.format(cal.getTime());
            String sunRise = getFormattedDate(sunriseCal);
            String sunSet = getFormattedDate(sunsetCal);
            String riseset = formatted + "\n(" + pkey_sunrise + " : " + sunRise + " - " + pkey_sunset + " : " + sunSet + ")";


            holder.dateTxt.setText(riseset);
            LayoutInflater inflater = LayoutInflater.from(mContext); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.tarabalamCntr.removeAllViews();
            int[] planetIndexDayWise = {0, 3, 6, 2, 5, 1, 4};
            int[] planetIndexNightWise = {5, 1, 4, 0, 3, 6, 2};
            int j = planetIndexDayWise[dayOfWeek];
            int k = planetIndexNightWise[dayOfWeek];
            String horaStart, horaEnd;
            for (int i = 0; i < 24; i++, j++, k++) {
                View viewMyLayout = inflater.inflate(R.layout.inflate_hora, null);

                TextView horaTxt = viewMyLayout.findViewById(R.id.text);
                TextView horaTxt1 = viewMyLayout.findViewById(R.id.text1);
                TextView horaTxt2 = viewMyLayout.findViewById(R.id.text2);
                TextView horaTxt3 = viewMyLayout.findViewById(R.id.text3);
                LinearLayout header = viewMyLayout.findViewById(R.id.header);
                TextView title1, title2, title3;
                title1 = viewMyLayout.findViewById(R.id.title1);
                title2 = viewMyLayout.findViewById(R.id.title2);
                title3 = viewMyLayout.findViewById(R.id.title3);
                if (j > 6) {
                    j = 0;
                }
                if (k > 6) {
                    k = 0;
                }
                Calendar hora = Calendar.getInstance();

                if (i < 12) {
                    hora.setTimeInMillis(sunriseCal.getTimeInMillis() + i * diffDay);
                    horaStart = getFormattedDate(hora);
                    hora.setTimeInMillis(sunriseCal.getTimeInMillis() + ((i + 1) * diffDay));
                    horaEnd = getFormattedDate(hora);
                } else {
                    hora.setTimeInMillis(sunsetCal.getTimeInMillis() + (i - 12) * diffNight);
                    horaStart = getFormattedDate(hora);
                    hora.setTimeInMillis(sunsetCal.getTimeInMillis() + ((i - 12 + 1) * diffNight));
                    horaEnd = getFormattedDate(hora);
                }
                header.setVisibility(View.GONE);
                horaTxt.setVisibility(View.GONE);
                if (i == 0) {
                    header.setVisibility(View.VISIBLE);
                    horaTxt.setVisibility(View.VISIBLE);
                    horaTxt.setText(Html.fromHtml(dayHora));
                    title1.setText(grahabhoga);
                    title2.setText(arambha);
                    title3.setText(sesha);


                } else if (i == 12) {
                    title1.setText(grahabhoga);
                    title2.setText(arambha);
                    title3.setText(sesha);
                    header.setVisibility(View.VISIBLE);
                    horaTxt.setVisibility(View.VISIBLE);
                    horaTxt.setText(Html.fromHtml(nightHora));
                }
                String text1 = hora_arr[j];
                //   String text3 = "<STRONG>" + hora_arr[k] + "</STRONG>";
                horaTxt1.setText(text1);
                horaTxt2.setText(Html.fromHtml(horaStart));
                horaTxt3.setText(Html.fromHtml(horaEnd));
                // String dayHoraStart=getFormattedDate(sunriseCal);
                //String nightHoraStart=getFormattedDate(sunsetCal);

                // horaTxt4.setText(Html.fromHtml(nightHoraStart));
                holder.tarabalamCntr.addView(viewMyLayout);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getFormattedDate(Calendar cal) {

        if ((mLang.contains("or") || mLang.contains("hi")   || mLang.contains("mr")   || mLang.contains("gu")) && !CalendarWeatherApp.isPanchangEng) {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            int calHour = cal.get(Calendar.HOUR_OF_DAY);
            int calMin = cal.get(Calendar.MINUTE);
            int calMonth = cal.get(Calendar.MONTH);
            String calDayNoStr = Utility.getInstance(mContext).getDayNo("" + calDayNo);
            String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
            String prefixTime = "";
            if ((calHour >= 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
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
